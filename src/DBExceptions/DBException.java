package DBExceptions;

public class DBException extends Exception
{
    String error = "[ERROR]: ";
    String message = "";

    public DBException(){}

    public DBException(String message)
    {
        this.message = message;
    }

    public String toString()
    {
        return error + message;
    }
}
