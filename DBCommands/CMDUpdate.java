package DBCommands;

import DBEngine.DBFileIO;
import DBEngine.DBStorage;
import DBEngine.DBTable;
import DBExceptions.DBException;
import DBExceptions.DBExecutionException;
import DBExceptions.DBParseException;
import DBExceptions.DBStorageException;

import java.io.IOException;
import java.util.ArrayList;

public class CMDUpdate extends DBQuery {
    protected ArrayList<NameValuePair> nameValueList;
    protected ArrayList<Condition> conditionsList;
    protected ArrayList<Boolean> entryBooleans;
    protected ArrayList<Boolean> columnBooleans;

    private class NameValuePair {
        private NameValuePair() {
        }

        String name;
        String value;

        private void setName(String name) {
            this.name = name;
        }

        private String getName() {
            return name;
        }

        private String getValue() {
            return value;
        }

        private void setValue(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "NameValuePair{" + "name='" + name + '\'' + ", value='" + value + '\'' + '}';
        }
    }

    public CMDUpdate(String command) throws DBParseException {
        super(command);
        conditionsList = new ArrayList<>();
        entryBooleans = new ArrayList<>();
        columnBooleans = new ArrayList<>();
    }

    public void parseQuery() throws DBException {
        tableName = consumeToken(NAME, tokenStream[counter]);
        consumeToken(SET, tokenStream[counter]);
        consumeNameValueList();
        consumeToken(WHERE, tokenStream[counter]);
        consumeWildConditions(conditionsList);
        consumeSemicolon(tokenStream[counter]);
    }

    public void consumeNameValueList() throws DBParseException {
        nameValueList = new ArrayList<>();
        nameValueList.add(consumeNameValuePair());

        while (tokenStream[counter].matches(COMMA)) {
            consumeToken(COMMA, tokenStream[counter]);
            nameValueList.add(consumeNameValuePair());
        }
    }

    public NameValuePair consumeNameValuePair() throws DBParseException {
        NameValuePair nvp = new NameValuePair();

        nvp.setName(consumeToken(NAME, tokenStream[counter]));
        consumeToken(EQUALS, tokenStream[counter]);
        nvp.setValue(consumeValue(tokenStream[counter]));

        return nvp;
    }

    public String executeQuery() throws DBException, IOException {
        DBStorage storage = new DBStorage();
        storage.checkIfDatabaseSet();

        if (!storage.checkIfTableExists(tableName)) {
            throw new DBStorageException("Table does not exist");
        }

        DBTable table = new DBTable(tableName);
        DBFileIO fileIO = new DBFileIO(table);

        fileIO.readTableFromStorage();

        applyConditions(table, conditionsList, entryBooleans);
        checkNVPsAreValid(table, nameValueList);
        makeModifications(table, nameValueList);

        fileIO.writeTableToStorage();

        return "[OK]";
    }

    public void makeModifications(DBTable table, ArrayList<NameValuePair> nameValueList) {

        int columnIndex;
        String value;

        for (int i = 0; i < table.getNumberOfEntries(); i++) {
            if (entryBooleans.get(i)) {
                for(NameValuePair nvp : nameValueList){
                    columnIndex = table.getIndexOfColumnHeader(nvp.getName());
                    value = nvp.getValue();
                    table.setTableValue(i, columnIndex, value);
                }
            }
        }
    }

    public void checkNVPsAreValid(DBTable table, ArrayList<NameValuePair> nameValueList) throws DBExecutionException {
        for (NameValuePair nvp : nameValueList) {
            if (!table.doesColumnExist(nvp.getName())) {
                throw new DBExecutionException("The attribute trying to be updated does not exist.");
            }
        }
    }
}
