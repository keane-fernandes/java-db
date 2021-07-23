package DBEngine;

import DBExceptions.DBExecutionException;

public class DBExpressionCalculator
{
    public DBExpressionCalculator(){}

    public boolean evaluateCondition(String operand, String operator, String criterion) throws DBExecutionException {
        switch (operator){
            case "==":
                return operand.equals(criterion);
            case "!=":
                return !operand.equals(criterion);
            case "LIKE":
                checkString(criterion);
                return operand.contains(criterion);
            case "<":
                checkFloat(operand, criterion);
                return Float.parseFloat(operand) < Float.parseFloat(criterion);
            case ">":
                checkFloat(operand, criterion);
                return Float.parseFloat(operand) > Float.parseFloat(criterion);
            case ">=":
                checkFloat(operand, criterion);
                return Float.parseFloat(operand) >= Float.parseFloat(criterion);
            case "<=":
                checkFloat(operand, criterion);
                return Float.parseFloat(operand) <= Float.parseFloat(criterion);
            default:
                return false;
        }
    }


    public void checkFloat(String operand, String criterion) throws DBExecutionException {
        try{
            Float.parseFloat(operand);
            Float.parseFloat(criterion);
        }
        catch (NumberFormatException nfe){
            throw new DBExecutionException("Attribute cannot be converted to number.");
        }
    }

    public void checkString(String criterion) throws DBExecutionException {

        int digitCount = 0;

        for (int i = 0; i < criterion.length(); i++){
            if ( Character.isDigit(criterion.charAt(i)) ){
                digitCount++;
            }
        }
        if (digitCount == criterion.length()){
            throw new DBExecutionException("Like can only be used with strings.");
        }

    }

    public Boolean evaluateBoolean(Boolean b1, Boolean b2, String operator)
    {
        switch (operator.toUpperCase()){
            case "AND":
                return b1 && b2;
            case "OR":
                return b1 || b2;
            default:
                return Boolean.FALSE;
        }
    }
}
