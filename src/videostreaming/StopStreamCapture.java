package videostreaming;

import java.io.BufferedReader;
import java.io.IOException;

import videostreaming.messaging.StopStreamRequest;

public class StopStreamCapture implements Runnable {
	private BufferedReader in;
	private boolean isRequestedToStop = false;

	public StopStreamCapture(BufferedReader dIS) {
		this.in = dIS;
		this.isRequestedToStop = false;
	}

	@Override
	public void run() {
		while (!this.isRequestedToStop) {
			receiveStopStreamRequest();
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

		stopStreamRequest = new StopStreamRequest();
		stopStreamRequest.FromJSON(strFromServer);
		this.isRequestedToStop = true;
	}

	public boolean isRequestedToStop() {
		return isRequestedToStop;
	}

}
