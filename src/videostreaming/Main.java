package videostreaming;

import java.util.ArrayList;

import videostreaming.common.Constants;
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

	public static void main(String[] args) {

		CurrentImage currentImage = new CurrentImage();

		/**
		 * parsing arguments
		 */
		setArgumentsFromCommandLine(args);
		ServerConnection connAsServer = new ServerConnection(getServerPort()); // act
																				// as
																				// server

		if (!local) // if streaming == remote then connect to a server
		{
			ClientConnection connAsClient = new ClientConnection(hostname,
					getRemotePort());
			System.err.println("no se si llega aqui");
			connAsClient.establishConnection();
			Thread test = new Thread(new ImageCaptureThread(
					connAsClient.getIn(), connAsClient.getOut()));
			test.start();
//			Thread keyboardCapture = new Thread(new KeyboardCapture(
//					connAsClient.getIn(), connAsClient.getOut()));
//			keyboardCapture.start();

		} else {
			Thread video = new Thread(new VideoCapture(currentImage, "Server"));
			video.start();

		}

		/**
		 * in the next loop, the system acting as a server side must delete the
		 * clients once it was disconnected
		 */
		boolean handover = false;
		while (true) {
			connAsServer.establishConnection();
			if (clientList.size() < Constants.MAX_CLIENTS.getValue()) {
				Client aNewClient = new Client(connAsServer.getIn(),
						connAsServer.getOut(), currentImage);
				clientList.add(aNewClient);
				handover = clientList.size() > Constants.MAX_CLIENTS.getValue() ? true
						: false;
				StatusResponse statusMsgResp = new StatusResponse(local,
						clientList.size(), ratelimit, handover);
				Thread client = new Thread(new ClientThread(aNewClient,
						statusMsgResp, connAsServer.getIn(),
						connAsServer.getOut()));
				client.start();

				System.out.println("cuantos=" + clientList.size());
			} else {
				handover = clientList.size() >= Constants.MAX_CLIENTS
						.getValue() ? true : false;
				StatusResponse statusMsgResp = new StatusResponse(local,
						clientList.size(), ratelimit, handover);
				connAsServer.getOut().write(statusMsgResp.ToJSON());
				System.err.println("From Server" + statusMsgResp.ToJSON());
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
