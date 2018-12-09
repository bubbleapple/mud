import java.io.*;
import java.net.*;

import IO.CommandParser;
import IO.IO;
import Model.User;

public class ClientThread extends Thread {
    protected Socket socket;
    private User user;
    private String prompt = ">";

    public ClientThread(Socket clientSocket) {
        this.socket = clientSocket;
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
        user = User.getSampleUser();
        IO.print(output, user.getMap().welcome());
        
        while (true) {
            try {
            	IO.print(output, prompt);
                line = input.readLine();
                if ((line == null) || line.equalsIgnoreCase("QUIT")) {
                    socket.close();
                    return;
                } else {
                	CommandParser.cmdParser(line, user, output);
                    output.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }
}
