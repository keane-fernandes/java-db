import DBEngine.DBCommandHandler;
import DBEngine.DBStorage;
import DBExceptions.DBException;

import java.io.*;
import java.net.*;

/* The default folder is called fileSystem and resides in the same directory as /src */
class DBServer
{
    final static char EOT = 4;
    static DBStorage storage;

    public DBServer(int portNumber)
    {
        try {
            storage = new DBStorage();
            storage.rootStorageInit();
            ServerSocket serverSocket = new ServerSocket(portNumber);
            System.out.println("Server Listening");
            while(true) processNextConnection(serverSocket);
        } catch(IOException ioe) {
            System.err.println(ioe);
        }
    }

    private void processNextConnection(ServerSocket serverSocket)
    {
        try {
            Socket socket = serverSocket.accept();
            BufferedReader socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter socketWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            System.out.println("Connection Established");
            while(true) processNextCommand(socketReader, socketWriter);
        } catch(IOException ioe) {
            System.err.println(ioe);
        } catch(NullPointerException npe) {
            System.out.println("Connection Lost");
        }
    }

    private void processNextCommand(BufferedReader socketReader, BufferedWriter socketWriter) throws IOException, NullPointerException
    {
        String incomingCommand = socketReader.readLine();
        try{
            DBCommandHandler handler = new DBCommandHandler(incomingCommand);
            socketWriter.write(handler.handleQuery());
        } catch(DBException dbe) {
            socketWriter.write(dbe.toString());
        } catch(IOException ioe) {
            socketWriter.write("[ERROR]: " + ioe.toString());
        }
        socketWriter.write("\n" + EOT + "\n");
        socketWriter.flush();
    }

    public static void main(String args[])
    {
        DBServer server = new DBServer(8888);
    }
}
