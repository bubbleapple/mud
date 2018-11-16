class CommandParser {
	// This cmdParser function is supposed to parse the command and take actions.
    public static boolean cmdParser(String line) {
    	line = line.trim();
    	if (line.length() == 0) {
    		return false;
    	}
        String[] args = line.split("\\s+");
        printArgs(args);
        return true;
    }
    // debug purpose:
    private static void printArgs(String[] args) {
    	System.out.printf("%d items: ", args.length);
    	for (String arg : args) {
    		System.out.printf("%s ", arg);
    	}
    	System.out.println();
    }

}
