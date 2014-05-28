package videostreaming;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import videostreaming.common.Constants;
import videostreaming.messaging.OverloadResponse;
import videostreaming.messaging.RequestResponse;
import videostreaming.messaging.StatusResponse;
import videostreaming.messaging.RequestResponseFactory;

public class Main {
	static ArrayList<Client> clientList = new ArrayList<Client>();
	StatusResponse status;

	private static String hostname;
	private static int serverPort;
	private static int remotePort;
	private static int rate;
	private static boolean local;
	private static boolean ratelimit;
	
	
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
			socket = notOverloadedReceived(socket);		
			Thread test = new 
			Thread(new ImageCaptureThread(socket, getServerPort(),currentImage));
			
			test.start();
		} else {
			Thread video = new Thread(new VideoCapture(currentImage, "Server"));
			video.start();

		}
		
		/**
		 * in the next loop, the system acting as a server side must delete the
		 * clients once it was disconnected
		 */
		boolean handover = true;
		StatusResponse statusMsgResp = null;
		
		while (true) {
			socket = connAsServer.establishConnection();

			if (clientList.size() < Constants.MAX_CLIENTS.getValue()) {
				Client aNewClient = new Client(socket, currentImage);
				clientList.add(aNewClient);
				
//				if( clientList.size() > Constants.MAX_CLIENTS.getValue() ){
//					handover = true;
//				}else { handover = false; }

				statusMsgResp = new
				StatusResponse(local,clientList.size()-1, ratelimit, handover); 
				
				Thread client = new 
				Thread(new ClientThread(aNewClient, statusMsgResp, clientList));
				
				client.start();
			} else {
				OutputStream outputStream = null;
				PrintWriter out;
				
				OverloadResponse overLoadResp = null;
				
				String ipOfServer = null;
				if(!hostname.equals("")){
					ipOfServer = hostname;
				}
				
				overLoadResp = 
				new OverloadResponse(clientList, ipOfServer, remotePort );
				
				try{
					outputStream = socket.getOutputStream();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
				out = new PrintWriter(outputStream, true);
				out.println(overLoadResp.ToJSON());
			}
		}
	}
	
	private static Socket notOverloadedReceived(Socket connectionSocket)
	{
		Socket retSocket = null;
		
		retSocket = receiveResponse(connectionSocket);
		
		return retSocket;
	}
	
	private static Socket receiveResponse( Socket initialSocket ) {
		String strFromServer = null;
		RequestResponse rcvdRespFromServer;
		RequestResponseFactory helperOverloadMsg = new RequestResponseFactory();
		String newIpAddr=null;
		int newPort;
		ClientConnection connAsClient = null;
		InputStream inputStream = null;
		BufferedReader in;
		
		do{
		
			try {
				inputStream = initialSocket.getInputStream();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			in = new BufferedReader(new InputStreamReader(inputStream));
			
			try {
				strFromServer = in.readLine();
			} catch (IOException ioEx) {
				ioEx.printStackTrace();
			}
	
			System.err.println(strFromServer);
			
			rcvdRespFromServer = helperOverloadMsg.FromJSON(strFromServer);
			if(rcvdRespFromServer!=null)
			{
				newIpAddr =((OverloadResponse) rcvdRespFromServer).getAllClients().get(0).getIpAddress();
				newPort  = ((OverloadResponse) rcvdRespFromServer).getAllClients().get(0).getServicePort();
				
				System.err.println("reconnecting to: "+newIpAddr+" port:"+newPort);
				connAsClient = new ClientConnection(newIpAddr,newPort);
				initialSocket = connAsClient.establishConnection();
			}
		}while(rcvdRespFromServer!=null);
		
		
		return initialSocket;
//		rcvdRespFromServer = new StatusResponse();
//		rcvdRespFromServer.FromJSON(strFromServer);
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
