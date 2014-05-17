package videostreaming;

import java.io.DataInputStream;
import java.io.DataOutputStream;


public class Client {
	private DataOutputStream output;
	private DataInputStream	input;
	private CurrentImage image;
	
	public Client(DataInputStream dIs, DataOutputStream dOs,CurrentImage image)
	{
		input = dIs;
		output = dOs;
		this.image = image;
		System.err.println("cliente numero loquesea");
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

}
