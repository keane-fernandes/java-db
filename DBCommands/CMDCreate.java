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

public class CMDCreate extends DBQuery {
    private boolean createDatabaseFlag;
    protected ArrayList<String> attributeList;

    public CMDCreate() {
    }

    public CMDCreate(String command) throws DBParseException {
        super(command);
        attributeList = new ArrayList<>();
    }

    public void parseQuery() throws DBException {
        switch (tokenStream[counter].toUpperCase()) {
            case "DATABASE":
                createDatabaseFlag = true;
                incrementCounter();
                databaseName = consumeToken(NAME, tokenStream[counter]);
                consumeSemicolon(tokenStream[counter]);
                break;
            case "TABLE":
                createDatabaseFlag = false;
                incrementCounter();
                tableName = consumeToken(NAME, tokenStream[counter]);
                CMDCreateTable createTable = new CMDCreateTable(attributeList);
                createTable.continueParsing();
                break;
            default:
                throw new DBParseException("Invalid query");
        }
    }


    public String executeQuery() throws DBException, IOException {
        DBStorage storage = new DBStorage();

        if (createDatabaseFlag) {
            storage.databaseStorageInit(databaseName);
        } else {
            runCreateTableCommand(storage);
        }

        return "[OK]";
    }

    public void runCreateTableCommand(DBStorage storage) throws DBException, IOException {
        storage.checkIfDatabaseSet();

        DBTable table = new DBTable(tableName);
        DBFileIO fileIO = new DBFileIO(table);

        if (storage.checkIfTableExists(tableName)) {
            throw new DBStorageException("Table already exists, try new table name.");
        }

        table.addAttribute("id");

        if (!attributeList.isEmpty()) {
            addAttributesToTable(table);
        }

        fileIO.writeTableToStorage();
    }

    public void addAttributesToTable(DBTable table) throws DBExecutionException {
        for (String attribute : attributeList){
            if(table.doesColumnExist(attribute)){
                throw new DBExecutionException("Attribute already exists in table.");
            }

            table.addAttribute(attribute);
        }
    }

}
