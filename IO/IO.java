package IO;

import java.io.IOException;
import java.io.DataOutputStream;

public class IO {
	public static void print(DataOutputStream output, String s) {
		try {
			output.writeChars(s);
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}
}
