package videostreaming;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import videostreaming.messaging.StopStreamRequest;
import videostreaming.messaging.StopStreamResponse;

public class StopStreamCapture implements Runnable {
	private PrintWriter out;
	private BufferedReader in;
	private boolean running = true;

	public StopStreamCapture(BufferedReader dIS, PrintWriter dOS) {
		this.out = dOS;
		this.in = dIS;
		this.running = true;
	}

	@Override
	public void run() {
		receiveStopStreamRequest();
		try {

			sendStopStreamResponse();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void receiveStopStreamRequest() {
		String strFromServer = null;
		StopStreamRequest stopStreamRequest;
		try {
			strFromServer = in.readLine();
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		}

		System.err.println(strFromServer);
		stopStreamRequest = new StopStreamRequest();
		stopStreamRequest.FromJSON(strFromServer);
	}

	private void sendStopStreamResponse() throws IOException {
		StopStreamResponse response = new StopStreamResponse();
		// request = new StopStreamRequest();
		out.println(response.ToJSON());
		System.err.println("StopStreamResponse: " + response.ToJSON());
		this.running = false;
	}

	public boolean isRunning() {
		return running;
	}

}
