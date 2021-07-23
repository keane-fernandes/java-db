package DBEngine;

import DBExceptions.DBStorageException;

import java.util.*;
import java.io.*;

/*
    All file, database and storage maintenance is done here. The default directory for file storage is
    called fileSystem and this directory is created upon instantiation of the DBServer class. This directory
    is created in the project folder (same hierarchical level as /src).
*/

public class DBStorage
{
    private static String currentDatabase = "";
    private static final String rootName = "fileSystem";
    private static final String rootPath = System.getProperty("user.dir") + File.separator + rootName + File.separator;
    private ArrayList<String> listOfDatabases;
    private ArrayList<String> listOfTables;
    private final String EXTENSION = ".tab";

    public DBStorage(){}

    /* Initializes the fileSystem directory for persistent storage */
    public void rootStorageInit() throws IOException
    {
        File rootFolder = new File (rootPath);
        if (!rootFolder.isDirectory()) {
            if(!rootFolder.mkdir()) {
                throw new IOException("Unable to initialise file system for database.");
            }
        }
    }

    public void databaseStorageInit (String databaseName) throws DBStorageException, IOException
    {
        updateListOfDatabases();

        if (checkIfDatabaseExists(databaseName)) {
            throw new DBStorageException("Database already exists, please use a different name.");
        }

        /* Checks if directory creation is successful */
        File dataBaseFolder = new File(rootPath + databaseName);

        if (!dataBaseFolder.mkdir())
        {
            throw new IOException("Unable to create database.");
        }
    }

    public void checkIfDatabaseSet() throws DBStorageException
    {
        if(currentDatabase.isEmpty()){
            throw new DBStorageException("Please USE a valid database before proceeding with other commands.");
        }
    }

    public void updateListOfDatabases()
    {
        File fileObject = new File(rootPath);
        listOfDatabases = new ArrayList<>();
        File[] databasesInFileSystem = fileObject.listFiles();

        for (File f : databasesInFileSystem) {
            if (f.isDirectory()) {
                listOfDatabases.add(f.getName());
            }
        }
    }

    public void updateListOfTables()
    {
        File fileObject = new File(getCurrentDatabasePath());
        listOfTables = new ArrayList<>();
        File[] tablesInDatabase = fileObject.listFiles();

        for (File f : tablesInDatabase){
            if (!f.isDirectory()) {
                listOfTables.add(f.getName());
            }
        }
    }

    public boolean checkIfDatabaseExists(String name)
    {
        updateListOfDatabases();

        for (String s : listOfDatabases) {
            if (s.equals(name)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkIfTableExists(String name)
    {
        updateListOfTables();
        String filename = name + EXTENSION;

        for (String s : listOfTables) {
            if (s.equals(filename)) {
                return true;
            }
        }
        return false;
    }

    public String getRootPath() {
        return rootPath;
    }

    public void setCurrentDatabaseName(String name)
    {
        currentDatabase = name;
    }

    public String getCurrentDatabasePath()
    {
        return rootPath + currentDatabase + File.separator;
    }

    public void dropDatabase(String databaseName) throws DBStorageException {
        String filePath = getRootPath() + File.separator + databaseName;

        File f = new File(filePath);

        if(!f.exists()){
            throw new DBStorageException("Database does not exist.");
        }

        for(File file : f.listFiles()){
            file.delete();
        }
        f.delete();
    }

    public void dropTable(String tableName)
    {
        String filePath = getCurrentDatabasePath() + tableName + EXTENSION;
        File f = new File(filePath);
        f.delete();
    }

}
