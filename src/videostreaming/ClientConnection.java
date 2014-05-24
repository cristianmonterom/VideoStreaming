package videostreaming;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import videostreaming.common.Constants;

public class ClientConnection extends Connection {

	String hostname = "";
	int port = 0;
	public ClientConnection(String host) {
		this.hostname = host;
		System.out.println("ejecuta constructor");
	}

	public ClientConnection(String host, int _port) {
		this.hostname = host;
		this.port = _port;
		System.out.println("ejecuta constructor");
	}

	@Override
	public void establishConnection() {
		try {
			socket = new Socket(this.hostname, this.port);
//			socket = new Socket(this.hostname, Constants.PORT.getValue());
			inputStream = socket.getInputStream();
			outputStream = socket.getOutputStream();
			out = new PrintWriter(outputStream, true);
			in = new BufferedReader(new InputStreamReader(inputStream));

		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}
}
