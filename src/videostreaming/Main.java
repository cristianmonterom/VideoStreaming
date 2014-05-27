package videostreaming;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;



import videostreaming.common.Constants;
import videostreaming.messaging.OverloadResponse;
import videostreaming.messaging.StatusResponse;

public class Main {
	static ArrayList<Client> clientList = new ArrayList<Client>();
	StatusResponse status;

	private static String hostname;
	private static int serverPort;
	private static int remotePort;
	private static int rate;

	private static boolean local;
	private static boolean ratelimit;
	
	
	
//	static ArrayList<Client> pruebas = new ArrayList<Client>();	//erase it only for tests
	
	/**
	 * This is the main for video streaming project
	 * @param args
	 */
	public static void main(String[] args) {

		CurrentImage currentImage = new CurrentImage();
		Socket socket = new Socket();

		/**
		 * parsing arguments
		 */
		setArgumentsFromCommandLine(args);
		ServerConnection connAsServer = new ServerConnection(getServerPort()); 

		if (!local) // if streaming == remote then connect to a server
		{
			ClientConnection connAsClient;
			connAsClient = new ClientConnection(hostname,getRemotePort());
			socket = connAsClient.establishConnection();
			Thread test = new 
			Thread(new ImageCaptureThread(socket, getServerPort()));
			
			test.start();
		} else {
			Thread video = new Thread(new VideoCapture(currentImage, "Server"));
			video.start();

		}
		
		
		/**
		 * in the next loop, the system acting as a server side must delete the
		 * clients once it was disconnected
		 */
		boolean handover = false;
		StatusResponse statusMsgResp = null;
		
		while (true) {
			socket = connAsServer.establishConnection();
			if (clientList.size() < Constants.MAX_CLIENTS.getValue()) {
				Client aNewClient = new Client(socket, currentImage);
				clientList.add(aNewClient);
				
				if( clientList.size() > Constants.MAX_CLIENTS.getValue() ){
					handover = true;
				}else { handover = false; }

				statusMsgResp = new
				StatusResponse(local,clientList.size(), ratelimit, handover); //check disscusion board
				
				Thread client = new 
				Thread(new ClientThread(aNewClient, statusMsgResp, clientList));
				
				client.start();
				
//				System.out.println("comprobando index del ob actual de:"+" _source:"+Thread.currentThread().getStackTrace()[1].getFileName()+clientList.indexOf(aNewClient));

//				System.out.println("cuantos=" + clientList.size());
			} else {
				OutputStream outputStream = null;
				PrintWriter out;
				
				OverloadResponse overLoadResp;
				overLoadResp = new 
				OverloadResponse(clientList, hostname, serverPort);
				
				if( clientList.size() > Constants.MAX_CLIENTS.getValue() ){
					handover = true;
				}else { handover = false; }
				
//				statusMsgResp = new
//				StatusResponse(local, clientList.size(), ratelimit, handover);			
				
				try{
					outputStream = socket.getOutputStream();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
				out = new PrintWriter(outputStream, true);
				out.println(overLoadResp.ToJSON());
				System.err.println("From Server" + overLoadResp.ToJSON());
			}
		}
	}

	private static void setArgumentsFromCommandLine(String[] args) {
		ArgumentParser parser = new ArgumentParser();
		parser.Parse(args);

		hostname = parser.getHostname();
		serverPort = parser.getServerPort();
		remotePort = parser.getRemotePort();
		rate = parser.getRate();

		local = !hostname.equals("") ? false : true;
		ratelimit = rate > 100 ? true : false;

		System.out.println("Parameters:");
		System.out.println("hostname=" + hostname);
		System.out.println("serverPort=" + serverPort);
		System.out.println("remoteport=" + remotePort);
		System.out.println("rate=" + rate);
	}

	public static int getServerPort() {
		return serverPort;
	}

	public static int getRemotePort() {
		return remotePort;
	}

}
