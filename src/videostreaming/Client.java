package videostreaming;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.PrintWriter;

public class Client {
	private DataOutputStream output;
	private DataInputStream input;
	private CurrentImage image;

	private PrintWriter out;
	private BufferedReader in;

	public Client(DataInputStream dIs, DataOutputStream dOs, CurrentImage image) {
		input = dIs;
		output = dOs;
		this.image = image;
	}

	public Client(BufferedReader dIs, PrintWriter dOs, CurrentImage image) {
		in = dIs;
		out = dOs;
		this.image = image;
	}

	public DataOutputStream getOutput() {
		return output;
	}

	public DataInputStream getInput() {
		return input;
	}

	public CurrentImage getImage() {
		return image;
	}

	public PrintWriter getOut() {
		return out;
	}



	public BufferedReader getIn() {
		return in;
	}



}
