package IO;
import java.io.DataOutputStream;
import Model.User;

public class CommandParser {
	// This cmdParser function is supposed to parse the command and take actions.
    public static boolean cmdParser(String line, User user, DataOutputStream output) {
    	line = line.trim();
    	if (line.length() == 0) {
    		return false;
    	}
        String[] args = line.split("\\s+");
        switch(args[0].toLowerCase()) {
        	case "whoami":
        		IO.print(output, user.getName() + "\n");
        		break;
        	case "ls":
        		IO.print(output, user.getPosition().toString());
        		break;
        	case "go":
        		assert args.length == 2;
        		if (user.go(args[1])) {
        			IO.print(output, "moved to " + user.getPosition().getName() + "\n");
        		}
        		else {
        			IO.print(output, "Wrong direction.\n");
        		}
        		break;
    		default:
    			IO.print(output, "unseen command.\n");
    			break;
        }
        return true;
    }
    // debug purpose:
//    private static void printArgs(String[] args) {
//    	System.out.printf("%d items: ", args.length);
//    	for (String arg : args) {
//    		System.out.printf("%s ", arg);
//    	}
//    	System.out.println();
//    }

}
