package DBCommands;

import DBEngine.DBExpressionCalculator;
import DBEngine.DBFileIO;
import DBEngine.DBStorage;
import DBEngine.DBTable;
import DBExceptions.DBException;
import DBExceptions.DBExecutionException;
import DBExceptions.DBParseException;
import DBExceptions.DBStorageException;

import java.io.IOException;

public class CMDJoin extends DBQuery
{
    protected String joinTableOne;
    protected String joinTableTwo;
    protected String joinAttributeOne;
    protected String joinAttributeTwo;

    public CMDJoin(String command) throws DBParseException {
        super(command);
    }

    public void parseQuery() throws DBException
    {
        joinTableOne = consumeToken(NAME, tokenStream[counter]);
        consumeToken(BOOLEAN_AND, tokenStream[counter]);
        joinTableTwo = consumeToken(NAME, tokenStream[counter]);
        consumeToken(ON, tokenStream[counter]);
        joinAttributeOne = consumeToken(NAME, tokenStream[counter]);
        consumeToken(BOOLEAN_AND, tokenStream[counter]);
        joinAttributeTwo = consumeToken(NAME, tokenStream[counter]);
        consumeSemicolon(tokenStream[counter]);
    }

    public String executeQuery() throws DBException, IOException {
        DBStorage storage = new DBStorage();
        storage.checkIfDatabaseSet();

        if( !storage.checkIfTableExists(joinTableOne) || !storage.checkIfTableExists(joinTableOne) ){
            throw new DBStorageException("Join tables do not exist.");
        }

        DBTable tableOne = new DBTable(joinTableOne);
        DBFileIO dbFileIOOne = new DBFileIO(tableOne);
        dbFileIOOne.readTableFromStorage();

        DBTable tableTwo = new DBTable(joinTableTwo);
        DBFileIO dbFileIOTwo = new DBFileIO(tableTwo);
        dbFileIOTwo.readTableFromStorage();

        checkIfAttributesExist(tableOne, tableTwo);

        return joinTables(tableOne, tableTwo);
    }

    public String joinTables(DBTable tableOne, DBTable tableTwo) throws DBExecutionException {
        int i, j, indexID, indexJoinID, indexResult;

        indexID = tableOne.getIndexOfColumnHeader(joinAttributeOne);
        indexJoinID = tableTwo.getIndexOfColumnHeader(joinAttributeTwo);
        indexResult = 1;

        DBExpressionCalculator dbe = new DBExpressionCalculator();
        StringBuilder sb = new StringBuilder();

        sb.append("id\t");
        appendHeaders(tableOne, sb);
        appendHeaders(tableTwo, sb);
        sb.append("\n");

        for(i = 0; i < tableOne.getNumberOfEntries(); i++){
            for(j = 0; j < tableTwo.getNumberOfEntries(); j++) {
                if( dbe.evaluateCondition( (tableOne.getTableValue(i, indexID)), "==",
                        (tableTwo.getTableValue(j, indexJoinID)))) {
                    sb.append(indexResult++ + "\t");
                    appendEntry(tableOne, i, sb);
                    appendEntry(tableTwo, j, sb);
                    sb.append("\n");
                }
            }
        }
        return sb.toString();
    }

    public void appendHeaders(DBTable table, StringBuilder sb){

        int i;

        for (i = 1; i < table.getNumberOfAttributes(); i++){
            sb.append(table.getTableName() + "." + table.getColumnHeaderByIndex(i) + "\t");
        }
    }

    public void appendEntry(DBTable table, int entryIndex, StringBuilder sb){
        int i;

        for(i = 1; i < table.getNumberOfAttributes(); i++){
            sb.append(table.getTableValue(entryIndex, i) + "\t");
        }
    }

    public void checkIfAttributesExist(DBTable tableOne, DBTable tableTwo) throws DBExecutionException {
        if(!tableOne.doesColumnExist(joinAttributeOne) || !tableTwo.doesColumnExist(joinAttributeTwo)){
            throw new DBExecutionException("Check your join attributes.");
        }
    }
}
