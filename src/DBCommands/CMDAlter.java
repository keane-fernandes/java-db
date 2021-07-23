package DBCommands;

import DBEngine.DBFileIO;
import DBEngine.DBStorage;
import DBEngine.DBTable;
import DBExceptions.DBException;
import DBExceptions.DBExecutionException;
import DBExceptions.DBParseException;

import java.io.IOException;
import java.util.ArrayList;

public class CMDAlter extends DBQuery
{
    private String alterationType;

    public CMDAlter(String command) throws DBParseException
    {
        super(command);
    }

    public void parseQuery() throws DBException
    {
        consumeToken(TABLE, tokenStream[counter]);
        tableName = consumeToken(NAME, tokenStream[counter]);
        alterationType = consumeToken(ALTERATION_TYPE, tokenStream[counter]).toUpperCase();
        attributeName = consumeToken(NAME, tokenStream[counter]);
        consumeSemicolon(tokenStream[counter]);
    }

    public String executeQuery() throws DBException, IOException {
        DBStorage storage = new DBStorage();
        checksBeforeExecution(storage);
        DBTable table = new DBTable(tableName);
        DBFileIO fileIO = new DBFileIO(table);

        fileIO.readTableFromStorage();

        switch (alterationType){
            case "ADD":
                addAttributeToTable(table);
                break;
            case "DROP":
                dropAttributeFromTable(table);
                break;
            default:
                throw new DBExecutionException("Unknown alteration type, please try again.");
        }

        fileIO.writeTableToStorage();
        return "[OK]";
    }

    private void addAttributeToTable(DBTable table) throws DBExecutionException {

        if (table.doesColumnExist(attributeName)){
            throw new DBExecutionException("Attribute already exists, try another one");
        }

        table.addAttribute(attributeName);

        for (int i = 0; i < table.getNumberOfEntries(); i++){
            ArrayList<String> entry = table.getEntry(i);
            entry.add(" ");
        }
    }

    private void dropAttributeFromTable(DBTable table) throws DBExecutionException {
        if(attributeName.equals("id")){
            throw new DBExecutionException("You cannot drop the id column in the table.");
        }

        if (!table.doesColumnExist(attributeName)) {
            throw new DBExecutionException("The attribute does not exist in the table.");
        }

        table.dropAttribute(attributeName);
    }
}
