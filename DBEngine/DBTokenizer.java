package DBEngine;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import DBCommands.*;

public class DBTokenizer
{
    String command;

    public DBTokenizer(String command)
    {
        this.command = command;
    }

    /* Returns an array of strings split on spaces */
    public String[] splitOnSpaces()
    {
        return command.split("\\s");
    }

    /* Returns the first token */
    public String getFirstToken()
    {
        String[] splitString = command.split("\\s");
        return splitString[0];
    }

    /*
        Splits a command into tokens taking care of string literals,
        operators and parentheses
    */
    public String[] splitIntoTokens()
    {
        DBQuery regexes = new DBQuery();

        /* First deal with string literals */
        ArrayList<String> stringLiterals = new ArrayList<>();

        Pattern p = Pattern.compile(regexes.STRING_LITERAL);
        Matcher m = p.matcher(command);

        while(m.find()){
            stringLiterals.add(m.group(1));
        }

        command = command.replaceAll(regexes.STRING_LITERAL, "'");
        command = command.replaceAll(regexes.OPERATOR, " $1 ");
        command = command.replaceAll("([;,()])", " $1 ");
        command = command.trim().replaceAll("\\s+", " ");

        String[] tokens = command.split(" ");

        substituteTokens(tokens, stringLiterals);

        return tokens;
    }

    /* Compiles a final list of tokens for parsing */
    public void substituteTokens(String[] tokens, ArrayList<String> stringLiterals)
    {
        int i, j;

        for (i = 0, j = 0; i < tokens.length; i++)
        {
            if (tokens[i].equals("'"))
            {
                String temp = stringLiterals.get(j++);
                tokens[i] = temp;
            }
        }
    }

}