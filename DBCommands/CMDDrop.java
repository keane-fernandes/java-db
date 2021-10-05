package DBCommands;

import DBEngine.DBStorage;
import DBExceptions.DBException;
import DBExceptions.DBExecutionException;
import DBExceptions.DBParseException;

public class CMDDrop extends DBQuery
{
    protected String structure;
    protected String structureName;

    public CMDDrop(String command) throws DBParseException {
        super(command);
    }

    public void parseQuery() throws DBException
    {
        structure = consumeToken(STRUCTURE, tokenStream[counter]).toUpperCase();
        structureName = consumeToken(NAME, tokenStream[counter]);
    }

    public String executeQuery() throws DBException
    {
        DBStorage storage = new DBStorage();
        storage.checkIfDatabaseSet();

        switch (structure){
            case "TABLE":
                if(!storage.checkIfTableExists(structureName)){
                    throw new DBExecutionException("Table does not exist.");
                }
                storage.dropTable(structureName);
                break;
            case "DATABASE":
                storage.dropDatabase(structureName);
                break;
            default:
                throw new DBExecutionException("Please provide valid structure.");
        }

        return "[OK]";
    }

}
