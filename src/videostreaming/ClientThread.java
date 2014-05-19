package videostreaming;

import java.io.IOException;

import videostreaming.messaging.StartStreamRequest;
import videostreaming.messaging.StatusResponse;
import videostreaming.messaging.Stream;

public class ClientThread implements Runnable {
	Client client;
	StatusResponse response;

	public ClientThread(Client client, StatusResponse status) {
		this.client = client;
		this.response = status;
	}

	public void run() {
		sendStatusResponse();
		receiveRequest();
		try {
			Thread.sleep(800);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}

		// While condition execute following
		do {
			sendImage();
		} while (true);

	}

	private void sendStatusResponse() {
		String str = response.ToJSON();
		client.getOut().println(str);
		System.err.println("mensaje sttus response enviado ok" + str);
	}

	private void receiveRequest() {
		StartStreamRequest resquestRcvd = new StartStreamRequest();
		String rcvdReqStr = "";

		try {
			rcvdReqStr = client.getIn().readLine();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		resquestRcvd.FromJSON(rcvdReqStr);
	}

	private void sendImage() {
		byte[] rawImage;

		Stream streamMsg = null;
		String str = null;

		rawImage = client.getImage().getImageToDisplay();

		try {
			str = new String(rawImage, "UTF-8");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		streamMsg = new Stream(false, str);
		client.getOut().println(streamMsg.ToJSON());
	}

}
