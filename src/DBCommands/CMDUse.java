package DBCommands;

import DBEngine.DBStorage;
import DBExceptions.DBException;
import DBExceptions.DBParseException;
import DBExceptions.DBStorageException;

public class CMDUse extends DBQuery
{
    public CMDUse(String command) throws DBParseException {
        super(command);
    }

    public void parseQuery() throws DBException
    {
        databaseName = consumeToken(NAME, tokenStream[counter]);
        consumeSemicolon(tokenStream[counter]);
    }

    public String executeQuery() throws DBStorageException
    {
        DBStorage fileSystem = new DBStorage();

        if (!fileSystem.checkIfDatabaseExists(databaseName)) {
            throw new DBStorageException("Database does not exist.");
        }

        fileSystem.setCurrentDatabaseName(databaseName);

        return "[OK]";
    }
}
