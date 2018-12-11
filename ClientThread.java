import java.io.*;
import java.net.*;

import IO.CommandParser;
import IO.IO;
import IO.Auth;
import Model.User;
import Model.GameMap;
import Utils.DB.DBUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;


public class ClientThread extends Thread {
    protected Socket socket;
    private User user;
    private String prompt = ">";
    private Map<Integer, GameMap> maps;
    private int id; // testing purpose
    private Connection con;

    public ClientThread(Socket clientSocket, Map<Integer, GameMap> maps, int id) {
        this.socket = clientSocket;
        this.maps = maps;
        this.id = id;
    }

    public void run() {
        BufferedReader input = null;
        DataOutputStream output = null;
        try {
            // from javadoc: 
            // In general, each read request made of a Reader causes a
            // corresponding read request to be made of the underlying
            // character or byte stream.  It is therefore advisable to wrap
            // a BufferedReader around any Reader whose read() operations
            // may be costly, such as FileReaders and InputStreamReaders.
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
        	System.err.println("Unable to create input or output pipe(s).");
            return;
        }

        // print welcome message
//        IO out = new IO(output);
//        out.print("Welcome to Mud Game <933 Adventure>! Please login or register[login/register]:");

        // create a sql connection for this thread
        // TODO: this seems very vulnerable to DOS attack, maybe use a connection pool later
//        con = DBUtils.getNewConnection();

        // login or register
//        user = new Auth(input, out, con).auth();
        user = User.getSampleUser(maps.get(0), id);
//        if(user == null) {
//            // System.out.println("User quit without login or register");
//            clientQuit();
//            return;
//        }

         
        user.updateOutputStream(new IO(output));
        user.print(user.getGameMap().welcome());

        String line;
        while (true) {
            try {
            	user.print(prompt);
                line = input.readLine();
                if ((line == null) || line.equalsIgnoreCase("QUIT")) {
//                    clientQuit();
                	user.quit();
                	socket.close();
                    return;
                } else {
                	CommandParser.cmdParser(line, user);
                    output.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    public void clientQuit() {
        try {
            // release user in node, store update in db
            if(user != null) {
            	user.quit(con);
            }
            con.close();
            socket.close();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}
