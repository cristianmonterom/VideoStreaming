package videostreaming;
import java.io.IOException;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;

import videostreaming.messaging.*;
import videostreaming.common.*;

import org.kohsuke.args4j.Option;


public class Main {
	static ArrayList<Client> clientList = new ArrayList<Client>();  //this is intended to keep track of all clients
	StatusResponse status;
	
	private static String hostname;
	private static int serverPort;
	private static int remotePort;
	private static int rate;
	
	private static boolean local;
	private static boolean ratelimit;
	
	
	
	public static void main (String[] args){
		
		ServerConnection connAsServer = new ServerConnection();		//act as server
		CurrentImage currentImage = new CurrentImage();

		/**
		 * parsing arguments
		 */
		setArgumentsFromCommandLine(args);
				
		if(hostname.equals("jeje"))		//if streaming == remote then connect to a server
		{
			ClientConnection connAsClient = new ClientConnection("localhost");
			System.err.println("no se si llega aqui");
			connAsClient.establishConnection();
			Thread test = new Thread(new ImageCaptureThread(connAsClient.getInput(),connAsClient.getOutput()));
			test.run();
			
		}
		else{
			Thread video = new Thread(new VideoCapture(currentImage));
			video.start();
		}
		
		


/**
 * in the next loop, the system acting as a server side must delete the clients once it was disconnected
 */
		boolean handover = false;
		while(true){
			connAsServer.establishConnection();
			if(clientList.size()<Constants.MAX_CLIENTS.getValue()){		//here the prog must accept connections while clients <3 (improve to handle many more
				Client aNewClient = new Client(connAsServer.getInput(), connAsServer.getOutput(),currentImage);
				clientList.add(aNewClient);
				handover = clientList.size()>Constants.MAX_CLIENTS.getValue()?true:false;
				StatusResponse statusMsgResp = new StatusResponse(local, clientList.size(), ratelimit,handover);
				Thread client = new Thread(new ClientThread(aNewClient,statusMsgResp)); 
				client.start();

				System.out.println("cuantos="+clientList.size());
			}else{
				handover = clientList.size()>=Constants.MAX_CLIENTS.getValue()?true:false;
				StatusResponse statusMsgResp = new StatusResponse(local, clientList.size(), ratelimit, handover);
				try{
					connAsServer.getOutput().writeUTF(statusMsgResp.ToJSON());
				}catch(IOException ex){
					ex.printStackTrace();
				}
				System.err.println("From Server"+statusMsgResp.ToJSON());
				System.err.println("");
			}
			
		}
		
	}
	
	private static void setArgumentsFromCommandLine(String[] args)
	{
		ArgumentParser parser = new ArgumentParser();
		parser.Parse(args);
		
		hostname = parser.getHostname();
		serverPort = parser.getServerPort();
		remotePort = parser.getRemotePort();
		rate = parser.getRate();
		
		
		local = !hostname.equals("")?false:true;
		ratelimit = rate>100?true:false;
		
		System.out.println("Parameters:");
		System.out.println("hostname="+hostname);
		System.out.println("serverPort="+serverPort);
		System.out.println("remoteport="+remotePort);
		System.out.println("rate="+rate);
	}

}
