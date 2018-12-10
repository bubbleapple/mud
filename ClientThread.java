import java.io.*;
import java.net.*;

import IO.CommandParser;
import IO.IO;
import Model.User;
import Model.GameMap;
import java.util.Map;


public class ClientThread extends Thread {
    protected Socket socket;
    private User user;
    private String prompt = ">";
    private Map<Integer, GameMap> maps;
    private int id; // testing purpose

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
        String line;
        user = User.getSampleUser(maps.get(0), id);
        user.updateOutputStream(new IO(output));
        user.print(user.getGameMap().welcome());
        
        while (true) {
            try {
            	user.print(prompt);
                line = input.readLine();
                if ((line == null) || line.equalsIgnoreCase("QUIT")) {
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
}
