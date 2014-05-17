package videostreaming;

import java.io.DataInputStream;
import java.io.DataOutputStream;


public class Client {
	private DataOutputStream output;
	private DataInputStream	input;
	private byte[] rawImage;
	
	public Client(DataInputStream dIs, DataOutputStream dOs, byte[] image)
	{
		input = dIs;
		output = dOs;
		rawImage = image;
		System.err.println("cliente numero loquesea");
	}

	public DataOutputStream getOutput() {
		return output;
	}

	public DataInputStream getInput() {
		return input;
	}

	public byte[] getRawImage() {
		return rawImage;
	}

}
