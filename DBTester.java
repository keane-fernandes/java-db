import DBCommands.*;
import DBEngine.DBCommandHandler;
import DBEngine.DBStorage;
import DBExceptions.DBException;

import java.io.IOException;
import java.util.ArrayList;

public class DBTester
{

    public DBTester(){}

    public static void main(String args[])
    {
        testCMDUse();
        testCMDCreate();
        testCMDDrop();
        testCMDAlter();
        testCMDInsert();
        testCMDSelect();
        testCMDUpdate();
        testCMDDelete();
        testCMDJoin();
        try {
            testingScript();
        }
        catch (IOException ioe) {
            ioe.toString();
        }
        System.out.println("Success: All tests passed!");
    }

    public static void testCMDUse()
    {
        ArrayList<String> commands = new ArrayList<>();

        commands.add("USE databasename;");
        commands.add("  USE          databasename   ;    ");
        commands.add("use  databasename;");
        commands.add("   UsE      dataBasename     ;     ");

        try{

            for (String s : commands)
            {
                CMDUse query = new CMDUse(s);
                query.parseQuery();
            }
        }
        catch (DBException dbe)
        {
            System.out.println(dbe.toString());
            System.exit(1);
        }

        System.out.println("Completed CMDUse tests.");
    }
    public static void testCMDCreate()
    {
        ArrayList<String> commands = new ArrayList<>();

        commands.add("Create database keane;");
        commands.add("  CREATE      table     tablename   ;    ");
        commands.add("create database databasename;");
        commands.add("   CreaTE    DatAbAse    dataBasename     ;     ");
        commands.add("CREATE TABLE tablename;");
        commands.add("   CREATE   TABLE     tablename    ;   ");
        commands.add("create table tablename;");
        commands.add("creaTE TAbLE tablename;");
        commands.add("CREATE TABLE tablename(name, age, dateofbirth);");
        commands.add("CREATE TABLE tablename(name);");
        commands.add("CREATE TABLE tablename(name1   , age1);");
        commands.add("CREATE TABLE tablename(name1    ,age1    , dateofbirth);");
        commands.add("CREATE TABLE tablename(name1   ,age1  , dataeofbirth);");

        try{

            for (String s : commands) {
                CMDCreate query = new CMDCreate(s);
                query.parseQuery();
            }
        }
        catch (DBException dbe)
        {
            System.out.println(dbe.toString());
            System.exit(1);
        }
        System.out.println("Completed CMDCreate tests.");
    }
    public static void testCMDDrop()
    {
        ArrayList<String> commands = new ArrayList<>();
        commands.add("DROP database db1;");
        commands.add("    DROP table table1;");
        commands.add("DROP TabLE    table1;");
        commands.add(" DROP table table1;");


        try{

            for (String s : commands)
            {
                CMDDrop query = new CMDDrop(s);
                query.parseQuery();
            }
        }
        catch (DBException dbe)
        {
            System.out.println(dbe.toString());
            System.exit(1);
        }

        System.out.println("Completed CMDDrop tests.");
    }
    public static void testCMDAlter()
    {
        ArrayList<String> commands = new ArrayList<>();

        commands.add("ALTER TABLE tablename ADD attribute;");
        commands.add("ALTER TABLE tablename DROP attribute;");

        try {
            for (String s : commands) {
                CMDAlter query = new CMDAlter(s);
                query.parseQuery();
            }
        } catch (DBException dbe) {
            System.out.println(dbe.toString());
            System.exit(1);
        }

        System.out.println("Completed CMDAlter tests.");

    }
    public static void testCMDInsert()
    {
        ArrayList<String> commands = new ArrayList<>();

        commands.add("INSERT INTO marks VALUES ('Steve', 65, true);");
        commands.add("INSERT INTO marks VALUES ('Dave', 55, true);");
        commands.add("INSERT INTO marks VALUES ('Bob', 35, false);");
        commands.add("INSERT INTO marks VALUES ('Clive', 20, false);");
        commands.add("INSERT INTO marks VALUES ('Clive');");
        commands.add("INSERT INTO marks VALUES ('Clive', false);");

        try{

            for (String s : commands)
            {
                CMDInsert query = new CMDInsert(s);
                query.parseQuery();
            }
        }
        catch (DBException dbe)
        {
            System.out.println(dbe.toString());
            System.exit(1);
        }
        System.out.println("Completed CMDInsert tests.");
    }
    public static void testCMDSelect()
    {
        ArrayList<String> commands = new ArrayList<>();

        commands.add("SELECT * FROM table1;");
        commands.add("SELECT name,age FROM table1 ;");
        commands.add("   SELECT *    FROM    marks    WHERE    pass     ==   'true' ;  ");
        commands.add("SELECT * FROM marks WHERE (pass == true) AND (age>5);");

        try {

            for (String s : commands) {
                CMDSelect query = new CMDSelect(s);
                query.parseQuery();
            }
        } catch (DBException dbe) {
            System.out.println(dbe.toString());
            System.exit(1);
        }

        System.out.println("Completed CMDSelect tests.");

    }
    public static void testCMDUpdate()
    {
        ArrayList<String> commands = new ArrayList<>();

        commands.add("UPDATE marks SET mark  =   38  ,  age  =  5 WHERE name == 'Clive';");

        try {

            for (String s : commands) {
                CMDUpdate query = new CMDUpdate(s);
                query.parseQuery();
            }
        } catch (DBException dbe) {
            System.out.println(dbe.toString());
            System.exit(1);
        }

        System.out.println("Completed CMDUpdate tests.");
    }
    public static void testCMDDelete()
    {
        ArrayList<String> commands = new ArrayList<>();

        commands.add("DELETE FROM tablename WHERE name == 'Keane';");

        try {

            for (String s : commands) {
                CMDDelete query = new CMDDelete(s);
                query.parseQuery();
            }
        } catch (DBException dbe) {
            System.out.println(dbe.toString());
            System.exit(1);
        }

        System.out.println("Completed CMDDelete tests.");
    }
    public static void testCMDJoin()
    {
        ArrayList<String> commands = new ArrayList<>();

        commands.add("JOIN table1 AND table2 ON attribute1 AND attribute2;");

        try {

            for (String s : commands) {
                CMDJoin query = new CMDJoin(s);
                query.parseQuery();
            }
        } catch (DBException dbe) {
            System.out.println(dbe.toString());
            System.exit(1);
        }

        System.out.println("Completed CMDJoin tests.");

    }
    public static void testingScript() throws IOException {
        DBStorage storage = new DBStorage();
        storage.rootStorageInit();

        ArrayList<String> commands = new ArrayList<>();

        /* Basic/core set of test queries */
        commands.add("USE markbook;");
        commands.add("DROP TABLE marks;");
        commands.add("DROP DATABASE markbook;");
        commands.add("CREATE DATABASE markbook;");
        commands.add("USE markbook;");
        commands.add("CREATE TABLE marks (name, mark, pass);");
        commands.add("INSERT INTO marks VALUES ('Steve', 65, true);");
        commands.add("INSERT INTO marks VALUES ('Dave', 55, true);");
        commands.add("INSERT INTO marks VALUES ('Bob', 35, false);");
        commands.add("INSERT INTO marks VALUES ('Clive', 20, false);");
        commands.add("SELECT * FROM marks;");
        commands.add("SELECT * FROM marks where name != 'Dave';");
        commands.add("SELECT * FROM marks WHERE (name == 'Dave') OR (pass == true);");
        commands.add("SELECT * FROM marks WHERE pass == true;");
        commands.add("UPDATE marks SET mark = 38 WHERE name == 'Clive';");
        commands.add("SELECT * FROM marks WHERE name == 'Clive';");
        commands.add("DELETE FROM marks WHERE name == 'Dave';");
        commands.add("SELECT * FROM marks;");
        commands.add("DELETE FROM marks WHERE mark < 40;");
        commands.add("SELECT * FROM marks;");

        /* More substantial sample database */
        commands.add("USE imdb;");
        commands.add("DROP TABLE actors;");
        commands.add("DROP TABLE movies;");
        commands.add("DROP TABLE roles;");
        commands.add("DROP DATABASE imdb;");
        commands.add("CREATE DATABASE imdb;");
        commands.add("USE imdb;");
        commands.add("CREATE TABLE actors (name, nationality, awards);");
        commands.add("INSERT INTO actors VALUES ('Hugh Grant', 'British', 3);");
        commands.add("INSERT INTO actors VALUES ('Toni Collette', 'Australian', 12);");
        commands.add("INSERT INTO actors VALUES ('James Caan', 'American', 8);");
        commands.add("INSERT INTO actors VALUES ('Emma Thompson', 'British', 10);");
        commands.add("CREATE TABLE movies (name, genre);");
        commands.add("INSERT INTO movies VALUES ('Mickey Blue Eyes', 'Comedy');");
        commands.add("INSERT INTO movies VALUES ('About a Boy', 'Comedy');");
        commands.add("INSERT INTO movies VALUES ('Sense and Sensibility', 'Period Drama');");
        commands.add("SELECT id FROM movies WHERE name == 'Mickey Blue Eyes';");
        commands.add("SELECT id FROM movies WHERE name == 'About a Boy';");
        commands.add("SELECT id FROM movies WHERE name == 'Sense and Sensibility';");
        commands.add("SELECT id FROM actors WHERE name == 'Hugh Grant';");
        commands.add("SELECT id FROM actors WHERE name == 'Toni Collette';");
        commands.add("SELECT id FROM actors WHERE name == 'James Caan';");
        commands.add("SELECT id FROM actors WHERE name == 'Emma Thompson';");
        commands.add("CREATE TABLE roles (name, movieID, actorID);");
        commands.add("INSERT INTO roles VALUES ('Edward', 3, 1);");
        commands.add("INSERT INTO roles VALUES ('Frank', 1, 3);");
        commands.add("INSERT INTO roles VALUES ('Fiona', 2, 2);");
        commands.add("INSERT INTO roles VALUES ('Elinor', 3, 4);");

        /* Advanced test queries */
        commands.add("SELECT * FROM actors WHERE awards < 5;");
        commands.add("ALTER TABLE actors ADD age;");
        commands.add("SELECT * FROM actors;");
        commands.add("UPDATE actors SET age = 45 WHERE name == 'Hugh Grant';");
        commands.add("SELECT * FROM actors WHERE name == 'Hugh Grant';");
        commands.add("SELECT nationality FROM actors WHERE name == 'Hugh Grant';");
        commands.add("ALTER TABLE actors DROP age;");
        commands.add("SELECT * FROM actors WHERE name == 'Hugh Grant';");
        commands.add("SELECT * FROM actors WHERE (awards > 5) AND (nationality == 'British');");
        commands.add("SELECT * FROM (awards > 5) AND ((nationality == 'British') OR (nationality == 'Australian'));");
        commands.add("SELECT * FROM actors WHERE name LIKE 'an';");
        commands.add("SELECT * FROM actors WHERE awards >= 10;");
        commands.add("DELETE FROM actors WHERE name == 'Hugh Grant';");
        commands.add("DELETE FROM actors WHERE name == 'James Caan';");
        commands.add("DELETE FROM actors WHERE name == 'Emma Thompson';");
        commands.add("SELECT * FROM actors;");
        commands.add("JOIN actors AND roles ON id AND actorID;");
        commands.add("JOIN movies AND roles ON id AND movieID;");
        commands.add("DROP TABLE actors;");
        commands.add("SELECT * FROM actors;");
        commands.add("DROP DATABASE imdb;");
        commands.add("USE imdb;");

        /* Robustness testing queries (imdb database create again) */
        commands.add("CREATE DATABASE imdb;");
        commands.add("USE imdb;");
        commands.add("CREATE TABLE actors (name, nationality, awards);");
        commands.add("INSERT INTO actors VALUES ('Hugh Grant', 'British', 3);");
        commands.add("INSERT INTO actors VALUES ('Toni Collette', 'Australian', 12);");
        commands.add("INSERT INTO actors VALUES ('James Caan', 'American', 8);");
        commands.add("INSERT INTO actors VALUES ('Emma Thompson', 'British', 10);");
        commands.add("CREATE TABLE movies (name, genre);");
        commands.add("INSERT INTO movies VALUES ('Mickey Blue Eyes', 'Comedy');");
        commands.add("INSERT INTO movies VALUES ('About a Boy', 'Comedy');");
        commands.add("INSERT INTO movies VALUES ('Sense and Sensibility', 'Period Drama');");
        commands.add("SELECT * FROM actors");
        commands.add("SELECT * FROM crew;");
        commands.add("SELECT spouse FROM actors;");
        commands.add("SELECT * FROM actors WHERE name == 'Hugh Grant;");
        commands.add("SELECT * FROM actors WHERE name > 10;");
        commands.add("SELECT name age FROM actors;");
        commands.add("SELECT * FROM actors awards > 10;");
        commands.add("SELECT * FROM actors WHERE name LIKE 10;");
        commands.add("    SELECT * FROM actors WHERE awards > 10;");
        commands.add("USE ebay;");


        /* My own test commands */
        commands.add("DROP DATABASE imdb;");
        commands.add("DROP DATABASE markbook;");
        commands.add("CREATE DATABASE testdb;");
        commands.add("USE testdb;");
        commands.add("CREATE TABLE biodata;");
        commands.add("ALTER TABLE biodata ADD name;");
        commands.add("ALTER TABLE biodata ADD weight;");
        commands.add("ALTER TABLE biodata ADD height;");
        commands.add("SELECT * FROM biodata;");
        commands.add("INSERT INTO biodata VALUES ('Keane', 85.4, 1.77);");
        commands.add("INSERT INTO biodata VALUES ('Sohini', 62.5, 1.54);");
        commands.add("INSERT INTO biodata VALUES ('Kenzia', 61.7, 1.65);");
        commands.add("INSERT INTO biodata VALUES ('Celine', 64.6, 1.55);");
        commands.add("INSERT INTO biodata VALUES ('Michael', 90.1, 1.90);");
        commands.add("SELECT * FROM biodata;");
        commands.add("SELECT name, height FROM biodata WHERE (name == 'Sohini') AND (name == 'Kenzia');");
        commands.add("SELECT name, height FROM biodata WHERE (name == 'Sohini') OR (name == 'Kenzia');");
        commands.add("SELECT name FROM biodata WHERE (height >= 1.877) AND (weight <= 90.11);");
        commands.add("UPDATE biodata SET name='MichaelChanged',weight=95.0 WHERE (height >= 1.877) AND (weight <= 90.11);");
        commands.add("SELECT * FROM biodata;");
        commands.add("DELETE FROM biodata WHERE weight <=89.999;");
        commands.add("SELECT * FROM biodata;");
        commands.add("DROP DATABASE testdb;");

        for (String s:commands)
        {
            try {
                DBCommandHandler ch = new DBCommandHandler(s);
                System.out.println(s);
                System.out.println(ch.handleQuery());
            }
            catch (DBException dbe)
            {
                System.out.println(dbe.toString());
            }
            catch (IOException ioe)
            {
                System.out.println(ioe.toString());
            }
        }
    }
}

