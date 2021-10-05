package DBExceptions;

public class DBExecutionException extends DBException
{
    String errorMessage;

    public DBExecutionException(String errorMessage)
    {
        super();
        this.errorMessage = errorMessage;
    }

    public String toString()
    {
        return super.toString() + errorMessage;
    }

}
