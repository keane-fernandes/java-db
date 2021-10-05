package DBExceptions;

public class DBStorageException extends DBException
{
    String errorMessage;

    public DBStorageException(){}

    public DBStorageException(String errorMessage)
    {
        super();
        this.errorMessage = errorMessage;
    }

    public String toString()
    {
        return super.toString() + (errorMessage);
    }
}