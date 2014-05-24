package videostreaming;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import videostreaming.messaging.StopStreamRequest;
import videostreaming.messaging.StopStreamResponse;

public class KeyboardCapture implements Runnable {
	private PrintWriter out;
	private BufferedReader in;
	private boolean running = true;
	public KeyboardCapture(BufferedReader dIS, PrintWriter dOS) {
		this.out = dOS;
		this.in = dIS;
		this.running = true;
	}

	@Override
	public void run() {
		System.out.print("Press Enter to end close: ");
		Scanner kbd = new Scanner(System.in);
		String readString = kbd.nextLine();
		while (readString != null) {
			System.out.println(readString);
			if (readString.equals("")) {
				try {
					sendStopStreamRequest();
				} catch (IOException e) {
					System.err.println("Error sending stop stream message");
					e.printStackTrace();
				}
				receiveStopStreamResponse();
				this.running = false;
				System.out.println("Disconnected.");
//				System.exit(0);
			}
			if (kbd.hasNextLine())
				readString = kbd.nextLine();
			else
				readString = null;
		}

	}

	private void sendStopStreamRequest() throws IOException {
		StopStreamRequest request = new StopStreamRequest();
		request = new StopStreamRequest();
		out.println(request.ToJSON());
		System.err.println("StopStreamRequest: " + request.ToJSON());
	}
	
	private void receiveStopStreamResponse() {
		String strFromServer = null;
		StopStreamResponse stopStreamResponse;
		try {
			strFromServer = in.readLine();
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		}

		System.err.println(strFromServer);
		stopStreamResponse = new StopStreamResponse();
		stopStreamResponse.FromJSON(strFromServer);
	}

	public boolean isRunning() {
		return running;
	}
}
