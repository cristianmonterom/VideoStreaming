package videostreaming;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
	private CurrentImage image;

	private PrintWriter out;
	private BufferedReader in;
	private OutputStream outputStream;
	private InputStream inputStream;


	public Client(Socket socket, CurrentImage image) {
		
		try{
			inputStream = socket.getInputStream();
			outputStream = socket.getOutputStream();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		out = new PrintWriter(outputStream, true);
		in = new BufferedReader(new InputStreamReader(inputStream));

		this.image = image;
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
