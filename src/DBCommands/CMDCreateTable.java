package DBCommands;

import DBExceptions.DBParseException;

import java.util.ArrayList;

public class CMDCreateTable extends CMDCreate
{
    ArrayList<String> attributeList;

    public CMDCreateTable(ArrayList<String> attributeList)
    {
        this.attributeList = attributeList;
    }

    public void continueParsing() throws DBParseException
    {
        switch(tokenStream[counter]) {
            case "(":
                consumeToken(LEFT_PAREN, tokenStream[counter]);
                consumeAttributeList(attributeList);
                consumeToken(RIGHT_PAREN, tokenStream[counter]);
                consumeSemicolon(tokenStream[counter]);
                break;
            case ";":
                consumeSemicolon(tokenStream[counter]);
                break;
            default:
                throw new DBParseException("Invalid Query");
        }
    }
}
