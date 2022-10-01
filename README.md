<h1 align="center">
  <br>
    <img src=./docs/db.svg alt="DB" width="100"></a>
  <br>
  DB
  <br>
</h1>

<h4 align="center">A relational database server built from scratch in Java.</h4>

<p align="center">
  <a href="#Features">Features</a> |
  <a href="#Usage">Usage</a> |
  <a href="#Design">Design</a> |
  <a href="#License">License</a>
</p>

<p align="center">
<img src="./docs/logo.jpg/../DB.gif" width=75% />
</p>

# Features
- Supports standard SQL commands
- Persistent data storage
- Error handling

# Design
The application is split into three core modules that are needed to ensure that the server is running - *DBCommands*, *DBEngine* and *DBExceptions*.

## DBCommands
The *DBCommands* module contains classes that implement each of the supported SQL commands.

| Class | Description |
| --- | ----------- |
| DBQuery |  The parent class|
| CMDx | Child classes of *DBQuery* where 'x' corresponds to each supported SQL command. |
| Condition | A class that tokenizes a user-input condition and outputs a parameterized string that enables the DBEngine module to evaluate the condition. |

## DBEngine
The *DBEngine* module contains the core classes that deal with file I/O and command parsing.

| Class | Description |
| --- | ----------- |
| DBCommandHandler |  Performs preliminary checks on the SQL command entered by the user on the command line. |
| DBTokenizer |  Tokenizes the user's input string to check if the SQL command is valid. |
| DBFileIO | Deals with writing to and from the storage media for the purposes of persistent storage. |
| DBStorage | Ensures the file system is robust and error free during read / write operations. |
| DBTable |  Represents a table in a relational database management system. |
| DBExpressionCalculator |  Evaluates criteria within an SQL command. |

## DBExceptions
The *DBExceptions* module contains exception classes and these exceptions are thrown when the RDBMS reaches an error state.

| Exception Class | Description |
| --- | ----------- |
| DBException |  Parent class of any exception thrown by the server. |
| DBParseException | Error in the SQL query. |
| DBExecutionException | Invalid criteria within a valid SQL query. |
| DBStorageExecution | Error in the persistent file system.  |

# Usage
## Overview
The server and client can be launched via the *DBServer* and *DBClient* classes respectively. The database server listens on port 8888 in order to receive incoming messages. These incoming commands are then passed to the `handleCommand` method for processing.

## Querying the Server
The client will communicate with the server using a simplified version of the SQL database query language. A brief overview of the query language is provided below:

| Class | Description |
| --- | ----------- |
| USE    | changes the database against which the following queries will be run |
| CREATE | constructs a new database or table (depending on the provided parameters) |
| INSERT | adds a new record (row) to an existing table |
| SELECT | searches for records that match the given condition |
| UPDATE | changes the existing data contained within a table |
| ALTER | changes the structure (columns) of an existing table |
| DELETE | removes records that match the given condition from an existing table |
| DROP | removes a specified table from a database, or removes the entire database |
| JOIN | performs an inner join on two tables (returning all permutations of all matching records) |

The following section defines the BNF grammar that the queries would need to conform to.

## BNF Grammar
The following BNF grammar can be used to construct commands to query the server. 

```
<Command>        ::=  <CommandType>;

<CommandType>    ::=  <Use> | <Create> | <Drop> | <Alter> | <Insert> |
                      <Select> | <Update> | <Delete> | <Join>

<Use>            ::=  USE <DatabaseName>

<Create>         ::=  <CreateDatabase> | <CreateTable>

<CreateDatabase> ::=  CREATE DATABASE <DatabaseName>

<CreateTable>    ::=  CREATE TABLE <TableName> | CREATE TABLE <TableName> (<AttributeList>)

<Drop>           ::=  DROP <Structure> <StructureName>

<Structure>      ::=  DATABASE | TABLE

<Alter>          ::=  ALTER TABLE <TableName> <AlterationType> <AttributeName>

<Insert>         ::=  INSERT INTO <TableName> VALUES (<ValueList>)

<Select>         ::=  SELECT <WildAttribList> FROM <TableName> |
                      SELECT <WildAttribList> FROM <TableName> WHERE <Condition> 

<Update>         ::=  UPDATE <TableName> SET <NameValueList> WHERE <Condition> 

<Delete>         ::=  DELETE FROM <TableName> WHERE <Condition>

<Join>           ::=  JOIN <TableName> AND <TableName> ON <AttributeName> AND <AttributeName>

<NameValueList>  ::=  <NameValuePair> | <NameValuePair>,<NameValueList>

<NameValuePair>  ::=  <AttributeName>=<Value>

<AlterationType> ::=  ADD | DROP

<ValueList>      ::=  <Value> | <Value>,<ValueList>

<Value>          ::=  '<StringLiteral>' | <BooleanLiteral> | <FloatLiteral> | <IntegerLiteral>

<BooleanLiteral> ::=  true | false

<WildAttribList> ::=  <AttributeList> | *

<AttributeList>  ::=  <AttributeName> | <AttributeName>,<AttributeList>

<Condition>      ::=  (<Condition>) AND (<Condition>)  |
                      (<Condition>) OR (<Condition>)   |
                      <AttributeName><Operator><Value>

<Operator>       ::=   ==   |   >   |   <   |   >=   |   <=   |   !=   |   LIKE
```

## Sample Queries
A valid **SELECT** query assuming a *people* table exists in the database would be
<pre>SELECT * FROM people WHERE Name=='Steve';</pre>
and similarly,
<pre>SELECT    *  FROM     people  WHERE   Name  ==  'Steve' ;</pre>
is valid and acceptable, being equivalent to the first query.

# License

```
Copyright (c) 2021 Keane Fernandes

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```