package DBExceptions;

public class DBParseException extends DBException
{
    String errorMessage;

    public DBParseException(){}

    public DBParseException(String errorMessage)
    {
        super();
        this.errorMessage = errorMessage;
    }

    public String toString()
    {
        return super.toString() + (errorMessage);
    }
}
