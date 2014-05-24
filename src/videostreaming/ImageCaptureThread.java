package videostreaming;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.swing.JFrame;

import org.apache.commons.codec.binary.Base64;

import videostreaming.common.Constants;
import videostreaming.messaging.RequestResponse;
import videostreaming.messaging.StartStreamRequest;
import videostreaming.messaging.StartStreamResponse;
import videostreaming.messaging.StatusResponse;
import videostreaming.messaging.StopStreamResponse;
import videostreaming.messaging.Stream;

public class ImageCaptureThread implements Runnable {

	private PrintWriter out;
	private BufferedReader in;
	StartStreamResponse startStreamResponse;
	Thread keyboardThread;
	KeyboardCapture keyboardCapture;
	Viewer myViewer = null;
	JFrame frame = null;
	private boolean running;

	public ImageCaptureThread(BufferedReader dIS, PrintWriter dOS) {
		this.out = dOS;
		this.in = dIS;
		this.running = true;
		initViewWindow();
		keyboardCapture = new KeyboardCapture(this.in, this.out);
		keyboardThread = new Thread(keyboardCapture);
	}

	public void run() {
		keyboardThread.start();
		
		System.err
				.println("Should receive messages to init and start streaming");
		// read response message from server
		receiveResponse();
		try {
			sendStartStreamRequest();
		} catch (IOException e) {
			e.printStackTrace();
		}
		receiveStartStreamResponse();
		do {
			receiveImages();
			if (!keyboardCapture.isRunning()) {
				this.running = false;
			}
		} while (running);
	}

	private void receiveResponse() {
		String strFromServer = null;
		RequestResponse rcvdRespFromServer;
		try {
			strFromServer = in.readLine();
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		}

		System.err.println(strFromServer);
		rcvdRespFromServer = new StatusResponse();
		rcvdRespFromServer.FromJSON(strFromServer);
	}

	private void receiveStartStreamResponse() {
		String strFromServer = null;
		StartStreamResponse startStreamResponse;
		try {
			strFromServer = in.readLine();
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		}

		System.err.println(strFromServer);
		startStreamResponse = new StartStreamResponse();
		startStreamResponse.FromJSON(strFromServer);
	}

	private void sendStartStreamRequest() throws IOException {
		StartStreamRequest request = new StartStreamRequest();
		request = new StartStreamRequest();

		out.println(request.ToJSON());

		System.err.println("send as client" + request.ToJSON());
	}

//	private void sendStartStreamResponse() {
//		String str = startStreamResponse.ToJSON();
//		out.println(str);
//		System.err.println("start stream RESPONSE SENT" + str);
//	}

	private void receiveImages() {
		String streamStr = null;
		Stream streamMsgFromServer;

		try {
			do {
				streamStr = in.readLine();
			} while (streamStr.isEmpty());
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			streamMsgFromServer = new Stream();
			streamMsgFromServer.FromJSON(streamStr);

			if (streamMsgFromServer.isCorrectMessage()) {
				renderView(streamMsgFromServer.getImageData());
			} else {
				StopStreamResponse stopStreamResponse = new StopStreamResponse();
				stopStreamResponse.FromJSON(streamStr);
			}

		} catch (UnsupportedEncodingException e) {
			StopStreamResponse stopStreamResponse = new StopStreamResponse();
			stopStreamResponse.FromJSON(streamStr);
		}
	}

	private void initViewWindow() {
		this.myViewer = new Viewer();
		this.frame = new JFrame("Client");
		frame.setSize(Constants.WINDOW_WIDTH.getValue(),
				Constants.WINDOW_HEIGHT.getValue());
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(myViewer);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
	}

	private void renderView(String strImage)
			throws UnsupportedEncodingException {
		byte[] base64_image = null;

		// try {
		base64_image = strImage.getBytes("UTF-8");
		// } catch (UnsupportedEncodingException e) {
		// e.printStackTrace();
		// }

		byte[] nobase64_image = Base64.decodeBase64(base64_image);
		/* Decompress the image */
		byte[] decompressed_image = Compressor.decompress(nobase64_image);
		/* Give the raw image bytes to the viewer. */
		myViewer.ViewerInput(decompressed_image);
		frame.repaint();
	}

	public boolean isRunning() {
		return running;
	}

}
