package DBCommands;

import DBEngine.DBFileIO;
import DBEngine.DBStorage;
import DBEngine.DBTable;
import DBExceptions.DBException;
import DBExceptions.DBExecutionException;
import DBExceptions.DBParseException;

import java.io.IOException;
import java.util.ArrayList;

public class CMDInsert extends DBQuery
{
    private ArrayList<String> valueList;
    private String value;

    public CMDInsert(String command) throws DBParseException
    {
        super(command);
        valueList = new ArrayList<>();
    }

    public void parseQuery() throws DBException
    {
        consumeToken(INTO, tokenStream[counter]);
        tableName = consumeToken(NAME, tokenStream[counter]);
        consumeToken(VALUES, tokenStream[counter]);
        consumeToken(LEFT_PAREN, tokenStream[counter]);
        consumeValueList();
        consumeToken(RIGHT_PAREN, tokenStream[counter]);
        consumeSemicolon(tokenStream[counter]);
    }

    public void consumeValueList() throws DBParseException
    {
        value = consumeValue(tokenStream[counter]);
        valueList.add(value);

        while (tokenStream[counter].matches(COMMA)){
            consumeToken(COMMA, tokenStream[counter]);
            value = consumeValue(tokenStream[counter]);
            valueList.add(value);
        }
    }

    public String executeQuery() throws IOException, DBException {
        DBStorage storage = new DBStorage();
        storage.checkIfDatabaseSet();
        checksBeforeExecution(storage);

        DBTable table = new DBTable(tableName);
        DBFileIO fileIO = new DBFileIO(table);

        fileIO.readTableFromStorage();

        int id = table.getMaxID();
        table.setMaxID(++id);

        valueList.add(0, String.valueOf(id));

        if(valueList.size() != table.getNumberOfAttributes()){
            throw new DBExecutionException("Too many/little values to insert.");
        }

        String[] valueArr = new String[valueList.size()];
        valueArr = valueList.toArray(valueArr);

        table.addEntryToTable(valueArr);

        fileIO.writeTableToStorage();

        return "[OK]";
    }

}
