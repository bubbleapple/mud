package IO;

import java.io.IOException;
import java.io.DataOutputStream;

public class IO {
	private DataOutputStream output;
	public IO(DataOutputStream output) {
		this.output = output;
	}
	public void print(String s) {
		try {
			output.writeChars(s);			
			output.flush();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}
}
