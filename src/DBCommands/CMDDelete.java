package DBCommands;

import DBEngine.DBFileIO;
import DBEngine.DBStorage;
import DBEngine.DBTable;
import DBExceptions.DBException;
import DBExceptions.DBParseException;

import java.io.IOException;
import java.util.ArrayList;

public class CMDDelete extends DBQuery
{
    protected ArrayList<Condition> conditionsList;
    protected ArrayList<Boolean> entryBooleans;

    public CMDDelete(String command) throws DBParseException
    {
        super(command);
        conditionsList = new ArrayList<>();
        entryBooleans = new ArrayList<>();
    }

    public void parseQuery() throws DBException
    {
        consumeToken(FROM, tokenStream[counter]);
        tableName = consumeToken(NAME, tokenStream[counter]);
        consumeToken(WHERE, tokenStream[counter]);
        consumeWildConditions(conditionsList);
    }

    public String executeQuery() throws DBException, IOException {
        DBStorage storage = new DBStorage();
        checksBeforeExecution(storage);

        DBTable table = new DBTable(tableName);
        DBFileIO fileIO = new DBFileIO(table);

        fileIO.readTableFromStorage();

        applyConditions(table, conditionsList, entryBooleans);
        table.removeEntries(entryBooleans);

        fileIO.writeTableToStorage();

        return "[OK]";
    }

}
