package videostreaming;

import java.io.DataInputStream;
import java.io.DataOutputStream;


public class Client {
	private DataOutputStream output;
	private DataInputStream	input;
	
	public Client(DataInputStream dIs, DataOutputStream dOs)
	{
		input = dIs;
		output = dOs;
		System.err.println("cliente numero loquesea");
	}

	public DataOutputStream getOutput() {
		return output;
	}

	public DataInputStream getInput() {
		return input;
	}

}
