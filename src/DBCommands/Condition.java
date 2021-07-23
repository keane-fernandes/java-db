package DBCommands;

public class Condition extends DBQuery
{
    private String operand;
    private String operator;
    private String criterion;

    public Condition(){}

    public void setOperand(String operand)
    {
        this.operand = operand;
    }

    public void setOperator(String operator)
    {
        this.operator = operator;
    }

    public void setCriterion(String criterion)
    {
        this.criterion = criterion;
    }
    public String getOperand()
    {
        return operand;
    }

    public String getOperator()
    {
        return operator;
    }

    public String getCriterion()
    {
        return criterion;
    }


    @Override
    public String toString() {
        return "Condition{" + "operand='" + operand + '\'' + ", operator='"
                + operator + '\'' + ", criterion='" + criterion + '\'' + '}';
    }

}
