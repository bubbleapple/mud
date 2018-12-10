package IO;
import java.io.DataOutputStream;
import java.util.Map;

import Model.MapNode;
import Model.User;

public class CommandParser {
	// This cmdParser function is supposed to parse the command and take actions.
    public static boolean cmdParser(String line, User user) {
    	line = line.trim();
    	if (line.length() == 0) {
    		return false;
    	}
        String[] args = line.split("\\s+");
        switch(args[0].toLowerCase()) {
        	case "whoami":
        		user.print(user.getName() + "\n");
        		break;
        	case "ls":
        		user.print(user.getPosition().toString());
        		break;
        	case "go":
        		assert args.length == 2;
        		if (user.move(args[1])) {
        			user.print("moved to " + user.getPosition().getName() + "\n");
        		}
        		else {
        			user.print("Wrong direction.\n");
        		}
        		break;
    		default:
    			user.print("Unsupported command.\n");
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
