package DBEngine;

import DBCommands.*;
import DBExceptions.DBException;
import DBExceptions.DBParseException;

import java.io.IOException;

public class DBCommandHandler
{
    String command;

    public DBCommandHandler(String command)
    {
        this.command = command;
    }

    /* Wrapper function that parses an SQL query according to the BNF */
    public String handleQuery() throws DBException, IOException {
        command = command.trim();
        preliminaryChecks();

        /* Splits the commands into tokens */
        DBTokenizer tokenizer = new DBTokenizer(command);

        /* Check first token to decide execution path */
        String firstToken = (tokenizer.getFirstToken()).toUpperCase();

        switch (firstToken) {
            case "USE":
                CMDUse use = new CMDUse(command);
                use.parseQuery();
                return use.executeQuery();
            case "CREATE":
                CMDCreate create = new CMDCreate(command);
                create.parseQuery();
                return create.executeQuery();
            case "INSERT":
                CMDInsert insert = new CMDInsert(command);
                insert.parseQuery();
                return insert.executeQuery();
            case "DROP":
                CMDDrop drop = new CMDDrop(command);
                drop.parseQuery();
                return drop.executeQuery();
            case "ALTER":
                CMDAlter alter = new CMDAlter(command);
                alter.parseQuery();
                return alter.executeQuery();
            case "JOIN":
                CMDJoin join = new CMDJoin(command);
                join.parseQuery();
                return join.executeQuery();
            case "UPDATE":
                CMDUpdate update = new CMDUpdate(command);
                update.parseQuery();
                return update.executeQuery();
            case "SELECT":
                CMDSelect select = new CMDSelect(command);
                select.parseQuery();
                return select.executeQuery();
            case "DELETE":
                CMDDelete delete = new CMDDelete(command);
                delete.parseQuery();
                return delete.executeQuery();
            default:
                throw new DBException("Unknown command, please try again.");
        }
    }

    public void preliminaryChecks() throws DBException {
        /* Checks for an empty command */
        if (command.isEmpty()) {
            throw new DBParseException("Command is empty, please try again.");
        }

        /* Check if there is just one instance of a semi-colon and it is at the end */
        if (!command.endsWith(";")) {
            throw new DBParseException("Invalid syntax, please check your semicolons.");
        }
    }
}
