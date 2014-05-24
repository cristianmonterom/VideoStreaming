package videostreaming;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import videostreaming.messaging.StartStreamRequest;
import videostreaming.messaging.StartStreamResponse;
import videostreaming.messaging.StatusResponse;
import videostreaming.messaging.StopStreamResponse;
import videostreaming.messaging.Stream;

public class ClientThread implements Runnable {
	Client client;
	StatusResponse response;
	StartStreamResponse startStreamResponse = new StartStreamResponse();
	StopStreamResponse stopStreamResponse = new StopStreamResponse();
	Thread stopStreamThread;
	StopStreamCapture stopStreamCapture;
	private boolean running;
	private PrintWriter out;
	private BufferedReader in;

	public ClientThread(Client client, StatusResponse status,
			BufferedReader dIS, PrintWriter dOS) {
		this.out = dOS;
		this.in = dIS;
		this.client = client;
		this.response = status;
		this.running = true;
		stopStreamCapture = new StopStreamCapture(this.in, this.out);
		stopStreamThread = new Thread(stopStreamCapture);
	}

	public void run() {
		stopStreamThread.start();
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

		sendStartStreamResponse();
		// aafasdfa
	}

	private void sendStartStreamResponse() {
		String str = startStreamResponse.ToJSON();
		client.getOut().println(str);
		System.err.println("start stream RESPONSE SENT" + str);
	}

	private void sendStopStreamResponse() {
		String str = stopStreamResponse.ToJSON();
		client.getOut().println(str);
		System.err.println("start stream RESPONSE SENT" + str);
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

	public boolean isRunning() {
		return running;
	}

}
