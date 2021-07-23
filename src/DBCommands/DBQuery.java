/*
    The DBCommand package allows the user to execute all
    of the commands stated in the BNF grammar provided.
    It contains query wide attributes which are populated
    as the command is parsed. Additionally, it contains the
    regular expressions for keywords, identifiers, operators
    and tokens in the provided BNF.
 */
package DBCommands;

import DBEngine.DBExpressionCalculator;
import DBEngine.DBStorage;
import DBEngine.DBTable;
import DBEngine.DBTokenizer;
import DBExceptions.DBException;
import DBExceptions.DBExecutionException;
import DBExceptions.DBParseException;

import java.util.ArrayList;


public class DBQuery
{
    /* Query-wide attributes */
    static protected int counter;
    static protected String[] tokenStream;
    protected String databaseName;
    protected String tableName;
    protected String attributeName;
    protected String conditionOperator;

    /* Regexes required for keywords and identifiers */
    public final String NAME = "[a-zA-Z0-9]+";
    public final String STRING_LITERAL = "('([^'\\t]*?)')";
    public final String FLOAT_LITERAL = "[+-]?([0-9]*[.])[0-9]+";
    public final String BOOLEAN_LITERAL = "(?i)(TRUE|FALSE)";
    public final String INTEGER_LITERAL = "[+-]?[0-9]+";
    public final String STRUCTURE = "(?i)(DATABASE|TABLE)";
    public final String COMMA = "[,]";
    public final String LEFT_PAREN = "[(]";
    public final String RIGHT_PAREN = "[)]";
    public final String SEMICOLON = "[;]";
    public final String OPERATOR = "(?i)(<=|>=|==|!=|<|>|LIKE|=)";
    public final String ALL = "[*]";
    public final String ALTERATION_TYPE = "(?i)(ADD|DROP)";
    public final String INTO = "(?i)(INTO)";
    public final String FROM = "(?i)(FROM)";
    public final String SET = "(?i)(SET)";
    public final String WHERE = "(?i)(WHERE)";
    public final String ON = "(?i)(ON)";
    public final String BOOLEAN_AND = "(?i)(AND)";
    public final String VALUES = "(?i)(VALUES)";
    public final String TABLE = "(?i)(TABLE)";
    public final String BOOLEAN_OP = "(?i)(AND|OR)";
    public final String EQUALS = "(=)";

    public DBQuery()
    {

    }

    public DBQuery(String command) throws DBParseException
    {
        counter = 1;
        DBTokenizer tokenizer = new DBTokenizer(command);
        tokenStream = tokenizer.splitIntoTokens();
        consumeSemicolon(tokenStream[(tokenStream.length-1)]);
    }

    /* Populates the <AttributeList> token in the BNF */
    public void consumeAttributeList(ArrayList<String> attributeList) throws DBParseException
    {

        attributeName = consumeToken(NAME, tokenStream[counter]);
        attributeList.add(attributeName);

        while (tokenStream[counter].matches(COMMA))
        {
            consumeToken(COMMA, tokenStream[counter]);
            attributeName = consumeToken(NAME, tokenStream[counter]);
            attributeList.add(attributeName);
        }
    }

    /* Consumes a token based on its regular expression */
    public String consumeToken(String tokenRegex, String token) throws DBParseException
    {
        if(!token.matches(tokenRegex)) {
            throw new DBParseException("Invalid query.");
        }
        incrementCounter();
        return token;
    }

    public void incrementCounter() throws DBParseException {
        counter++;
        if (counter >= tokenStream.length){
            throw new DBParseException("Reached end of query (potentially no semicolon).");
        }
    }

    public void consumeSemicolon(String token) throws DBParseException {
        if (!token.matches(SEMICOLON))
        {
            throw new DBParseException("Semicolon missing at the end.");
        }
    }

    /* Consumes a <Value> token */
    public String consumeValue(String token) throws DBParseException
    {

        if (token.matches(FLOAT_LITERAL) || token.matches(BOOLEAN_LITERAL) || token.matches(INTEGER_LITERAL)) {
            incrementCounter();
            return token;
        }

        if (token.matches(STRING_LITERAL)) {
            incrementCounter();
            return token.substring(1,(token.length() - 1));
        }

        throw new DBParseException("Enter a valid value.");

    }

    /* Consumes a <Condition> token */
    public Condition consumeCondition() throws DBParseException
    {
        Condition c = new Condition();

        c.setOperand(consumeToken(NAME, tokenStream[counter]));
        c.setOperator(consumeToken(OPERATOR, tokenStream[counter]));
        c.setCriterion(consumeValue(tokenStream[counter]));

        return c;
    }

    /* Consumes one or more <Condition> tokens */
    public void consumeWildConditions(ArrayList<Condition> conditionsList) throws DBParseException
    {
        if(tokenStream[counter].matches(LEFT_PAREN)){
            consumeBracketedCondition(conditionsList);
        }
        else {
            conditionsList.add(consumeCondition());
            counter = counter - 3;
            conditionsList.add(consumeCondition());
            conditionOperator = "AND";
        }
    }

    /* Consumes a bracketed <Condition> along parentheses and operator */
    public void consumeBracketedCondition(ArrayList<Condition> conditionsList) throws DBParseException
    {
        consumeToken(LEFT_PAREN, tokenStream[counter]);
        conditionsList.add(consumeCondition());
        consumeToken(RIGHT_PAREN, tokenStream[counter]);
        conditionOperator = consumeToken(BOOLEAN_OP, tokenStream[counter]);
        consumeToken(LEFT_PAREN, tokenStream[counter]);
        conditionsList.add(consumeCondition());
        consumeToken(RIGHT_PAREN, tokenStream[counter]);
    }

    /*
        Evaluates a <Condition> token and populates a boolean array of results corresponding
        to each entry evaluated
     */
    public void evaluateCondition(DBTable table, Condition condition, ArrayList<Boolean> results) throws DBExecutionException
    {
        DBExpressionCalculator dbec = new DBExpressionCalculator();
        int columnIndex;
        String operand;

        String attributeName = condition.getOperand();
        String operator = condition.getOperator();
        String criterion = condition.getCriterion();

        if (!table.doesColumnExist(attributeName)){
            throw new DBExecutionException("Attribute not found in table.");
        }

        columnIndex = table.getIndexOfColumnHeader(attributeName);

        for (int i = 0; i < table.getNumberOfEntries(); i++){
            operand = table.getTableValue(i, columnIndex);
            results.add(dbec.evaluateCondition(operand, operator, criterion));
        }
    }

    /* Evaluates multiple conditions and provides a final resulting boolean array */
    public void applyConditions(DBTable table, ArrayList<Condition> conditionsList, ArrayList<Boolean> entryBooleans) throws DBExecutionException {

        ArrayList<Boolean> booleanArray1 = new ArrayList<>();
        ArrayList<Boolean> booleanArray2 = new ArrayList<>();

        if (conditionsList.isEmpty()){
            for (int i = 0; i < table.getNumberOfEntries(); i++){
                entryBooleans.add(Boolean.TRUE);
            }
            return;
        }

        evaluateCondition(table, conditionsList.get(0), booleanArray1);
        evaluateCondition(table, conditionsList.get(1), booleanArray2);

        evaluateBooleanTable(booleanArray1, booleanArray2, entryBooleans);
    }

    /* Populates the final boolean array for nested conditions */
    private void evaluateBooleanTable(ArrayList<Boolean> b1, ArrayList<Boolean> b2, ArrayList<Boolean> entryBooleans)
    {
        DBExpressionCalculator dbec = new DBExpressionCalculator();

        for (int i = 0; i < b1.size(); i++){
            entryBooleans.add(dbec.evaluateBoolean(b1.get(i), b2.get(i), conditionOperator));
        }
    }

    /* Checks if database is set and the selected table exists in the set database */
    public void checksBeforeExecution(DBStorage storage) throws DBException {
        storage.checkIfDatabaseSet();
        if(!storage.checkIfTableExists(tableName)){
            throw new DBExecutionException("Table does not exist.");
        }
    }

    public void checkIfAttributeInTable(DBTable table, ArrayList<String> attributeList) throws DBExecutionException {
        for (String s : attributeList){
            if(!table.doesColumnExist(s)){
                throw new DBExecutionException("Attribute does not exist in table.");
            }
        }
    }
}
