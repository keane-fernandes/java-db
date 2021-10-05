package DBCommands;

import DBEngine.DBFileIO;
import DBEngine.DBStorage;
import DBEngine.DBTable;
import DBExceptions.DBException;
import DBExceptions.DBExecutionException;
import DBExceptions.DBParseException;

import java.io.IOException;
import java.util.ArrayList;

public class CMDSelect extends DBQuery
{
    protected boolean allFlag;
    protected ArrayList<String> attributeList;
    protected ArrayList<Condition> conditionsList;
    protected ArrayList<Boolean> entryBooleans;
    protected ArrayList<Boolean> columnBooleans;

    public CMDSelect(String command) throws DBParseException
    {
        super(command);
        attributeList = new ArrayList<>();
        conditionsList = new ArrayList<>();
        entryBooleans = new ArrayList<>();
        columnBooleans = new ArrayList<>();
    }

    public void parseQuery() throws DBException
    {
        consumeAllOrSomeAttributes();

        consumeToken(FROM, tokenStream[counter]);
        tableName = consumeToken(NAME, tokenStream[counter]);

        switch(tokenStream[counter].toUpperCase()){
            case ";":
                consumeSemicolon(tokenStream[counter]);
                break;
            case "WHERE":
                consumeToken(WHERE, tokenStream[counter]);
                consumeWildConditions(conditionsList);
                break;
            default:
                throw new DBParseException("Invalid query.");
        }
    }

    public void consumeAllOrSomeAttributes() throws DBParseException {
        switch(tokenStream[counter]) {
            case "*":
                consumeToken(ALL, tokenStream[counter]);
                allFlag = true;
                break;
            default:
                allFlag = false;
                consumeAttributeList(attributeList);
        }
    }

    public String executeQuery() throws DBException, IOException
    {
        DBStorage storage = new DBStorage();
        checksBeforeExecution(storage);
        DBTable table = new DBTable(tableName);
        DBFileIO fileIO = new DBFileIO(table);

        fileIO.readTableFromStorage();

        applyConditions(table, conditionsList, entryBooleans);
        filterAttributes(table);

        return buildResponse(table);
    }

    private void filterAttributes(DBTable table) throws DBExecutionException {
        if (allFlag){
            for (int i = 0; i < table.getNumberOfAttributes(); i++){
                columnBooleans.add(Boolean.TRUE);
            }
            return;
        }

        checkIfAttributeInTable(table, attributeList);

        for (int i = 0; i < table.getNumberOfAttributes(); i++){
            String check = table.getColumnHeaderByIndex(i);
            if(attributeList.contains(check)){
                columnBooleans.add(Boolean.TRUE);
            }
            else {
                columnBooleans.add(Boolean.FALSE);
            }
        }
    }

    private String buildResponse(DBTable table)
    {
        StringBuilder sb = new StringBuilder();

        sb.append("[OK]" + "\n");
        appendHeaders(table, sb);
        appendEntries(table, sb);
        return sb.toString();
    }

    private void appendHeaders(DBTable table, StringBuilder sb)
    {
        int i;

        for (i = 0; i < table.getNumberOfAttributes(); i++){
            if(columnBooleans.get(i)){
                sb.append(table.getColumnHeaderByIndex(i) + "\t");
            }
        }
        sb.append("\n");
    }

    private void appendEntries(DBTable table, StringBuilder sb)
    {
        int i, j, maxEntries, maxAttributes;
        ArrayList<String> entry;

        maxEntries = table.getNumberOfEntries();
        maxAttributes = table.getNumberOfAttributes();

        for (i = 0; i < maxEntries; i++){
            if (entryBooleans.get(i))
            {
                entry = table.getEntry(i);
                for (j = 0; j < maxAttributes; j++){
                    if(columnBooleans.get(j))
                        sb.append(entry.get(j) + "\t");
                }
                sb.append("\n");
            }
        }
    }
}
