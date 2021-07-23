package DBEngine;

import java.io.*;
import java.util.ArrayList;

/*
    All table reading and writing to/from storage is done here.
*/

public class DBFileIO
{
    private DBTable table;
    private String tableFilePath;
    private final String EXT = ".tab";

    public DBFileIO(DBTable table)
    {
        DBStorage storage = new DBStorage();
        this.table = table;
        tableFilePath = storage.getCurrentDatabasePath() + table.getTableName() + EXT;
    }

    public void readTableFromStorage() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(tableFilePath));
        String line;

        ArrayList<String> listOfLines = new ArrayList<>();

        while ((line = br.readLine()) != null) {
            listOfLines.add(line);
        }

        importMaxID(listOfLines);
        importColumnHeaders(listOfLines);
        importColumnData(listOfLines);
    }

    private void importMaxID(ArrayList<String> listOfLines)
    {
        String ID = listOfLines.get(0);
        table.setMaxID(Integer.parseInt(ID));
        listOfLines.remove(0);
    }

    private void importColumnHeaders(ArrayList<String> listOfLines){
        String columnHeaders = listOfLines.get(0);
        String[] array = columnHeaders.split("\\t");

        for (String s : array){
            table.addAttribute(s);
        }

        listOfLines.remove(0);
    }

    private void importColumnData(ArrayList<String> listOfLines)
    {
        int i;
        String line;

        for (i = 0; i < listOfLines.size(); i++) {
            line = listOfLines.get(i);
            String[] array = line.split("\\t");
            table.addEntryToTable(array);
        }
    }

    public void writeTableToStorage() throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(tableFilePath));

        bw.write(String.valueOf(table.getMaxID()));
        bw.flush();
        writeColumnHeaders(bw);
        writeEntries(bw);
        bw.close();
    }

    private void writeColumnHeaders(BufferedWriter bw) throws IOException {
        int i;

        bw.newLine();
        for (i = 0; i < table.getNumberOfAttributes(); i++){
            bw.write(table.getColumnHeaderByIndex(i) + "\t");
        }
        bw.flush();
    }

    private void writeEntries(BufferedWriter bw) throws IOException {
        int i, j;

        for (i = 0; i < table.getNumberOfEntries(); i++) {
            bw.newLine();
            ArrayList<String> entry = table.getEntry(i);
            for (j = 0; j < entry.size(); j++) {
                bw.write(entry.get(j) + "\t");
            }
        }
        bw.flush();
    }
}



