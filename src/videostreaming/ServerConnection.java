package videostreaming;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import videostreaming.common.Constants;

public class ServerConnection extends Connection {
	private int port = 0;

	public ServerConnection() {
		try {
			server = new ServerSocket(Constants.PORT.getValue());
			socket = new Socket();
		} catch (IOException connEx) {
			connEx.printStackTrace();
		}
	}

	public ServerConnection(int _port) {
		try {
			this.port = _port;
			server = new ServerSocket(this.port);
			socket = new Socket();
		} catch (IOException connEx) {
			connEx.printStackTrace();
		}
	}

	@Override
	public void establishConnection() {
		try {
			System.out.println("esperando conexion");
			socket = server.accept();
			inputStream = socket.getInputStream();
			outputStream = socket.getOutputStream();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		out = new PrintWriter(outputStream, true);
		in = new BufferedReader(new InputStreamReader(inputStream));
	}
}
