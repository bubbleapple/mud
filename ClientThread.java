import java.io.*;
import java.net.*;


public class ClientThread extends Thread {
    protected Socket socket;

    public ClientThread(Socket clientSocket) {
        this.socket = clientSocket;
    }

    public void run() {
        InputStream inp = null;
        BufferedReader brinp = null;
        DataOutputStream out = null;
        try {
            inp = socket.getInputStream();
            // from javadoc: 
            // In general, each read request made of a Reader causes a
            // corresponding read request to be made of the underlying
            // character or byte stream.  It is therefore advisable to wrap
            // a BufferedReader around any Reader whose read() operations
            // may be costly, such as FileReaders and InputStreamReaders.
            brinp = new BufferedReader(new InputStreamReader(inp));
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            return;
        }
        String line;
        try {
        	out.writeChars("\n\nWelcome to the new world!!\n\n");
        } catch(IOException e) {
        	e.printStackTrace();
            return;
        }
        while (true) {
            try {
                line = brinp.readLine();
                if ((line == null) || line.equalsIgnoreCase("QUIT")) {
                    socket.close();
                    return;
                } else {
                	CommandParser.cmdParser(line);
                    out.writeBytes(line + "\n\r");
                    out.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }
}
