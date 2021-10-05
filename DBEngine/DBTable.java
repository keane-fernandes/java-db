package DBEngine;

import java.util.ArrayList;
import java.util.Iterator;


/* The data structure used to store a table in the application */
public class DBTable
{
    private String name;
    private ArrayList<String> columnHeaders;
    private ArrayList<ArrayList<String>> columnData;
    private int maxID;

    public DBTable(String name)
    {
        this.name = name;
        columnHeaders = new ArrayList<>();
        columnData = new ArrayList<ArrayList<String>>();
        maxID = 0;
    }

    public String getTableName()
    {
        return name;
    }

    public int getNumberOfAttributes()
    {
        return columnHeaders.size();
    }

    public int getNumberOfEntries()
    {
        return columnData.size();
    }

    public ArrayList<String> getEntry(int index)
    {
        return columnData.get(index);
    }

    public String getColumnHeaderByIndex(int index)
    {
        return columnHeaders.get(index);
    }

    public void addAttribute(String name)
    {
        columnHeaders.add(name);
    }

    public void dropAttribute(String name) {
        int columnIndex = columnHeaders.indexOf(name);

        columnHeaders.remove(name);

        for (int i = 0; i < columnData.size(); i++){
            columnData.get(i).remove(columnIndex);
        }
    }

    public int getMaxID()
    {
        return maxID;
    }

    public void setMaxID(int maxID)
    {
        this.maxID = maxID;
    }

    public void addEntryToTable(String[] entry)
    {
        int i;
        columnData.add(new ArrayList<>());

        for( i = 0; i < entry.length; i++) {
            columnData.get(columnData.size()-1).add(entry[i]);
        }
    }
    
    public int getIndexOfColumnHeader(String columnHeader)
    {
        return columnHeaders.indexOf(columnHeader);
    }

    public boolean doesColumnExist(String columnName)
    {
        if(columnHeaders.contains(columnName)){
            return true;
        }
        return false;
    }

    public String getTableValue(int entryIndex, int columnIndex)
    {
        return columnData.get(entryIndex).get(columnIndex);
    }

    public void setTableValue(int entryIndex, int columnIndex, String value)
    {
        columnData.get(entryIndex).set(columnIndex, value);
    }
    
    public void removeEntries(ArrayList<Boolean> entryBooleans) {
        Iterator<ArrayList<String>> iterator = columnData.iterator();
        int i = 0;

        while (iterator.hasNext()) {
            ArrayList<String> entry = iterator.next();
            if(entryBooleans.get(i))
            {
               iterator.remove();
            }
            i++;
        }
    }
}
