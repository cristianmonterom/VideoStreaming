package videostreaming;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.swing.JFrame;

import org.apache.commons.codec.binary.Base64;

import videostreaming.messaging.RequestResponse;
import videostreaming.messaging.StartStreamRequest;
import videostreaming.messaging.StatusResponse;
import videostreaming.messaging.Stream;

public class ImageCaptureThread implements Runnable {

	private PrintWriter out;
	private BufferedReader in;

	Viewer myViewer = null;
	JFrame frame = null;

	public ImageCaptureThread(BufferedReader dIS, PrintWriter dOS) {
		this.out = dOS;
		this.in = dIS;
		initViewWindow();
	}

	public void run() {

		System.err
				.println("Should receive messages to init and start streaming");
		// read response message from server
		receiveResponse();
		try {
			sendStartStreamRequest();
		} catch (IOException e) {
			e.printStackTrace();
		}
		do {
			receiveImages();
		} while (true);

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

	private void sendStartStreamRequest() throws IOException {
		StartStreamRequest request = new StartStreamRequest();
		request = new StartStreamRequest();

		out.println(request.ToJSON());

		System.err.println("send as client" + request.ToJSON());
	}

	private void receiveImages() {
		String streamStr = null;
		Stream streamMsgFromServer;

		try {
			streamStr = in.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

		streamMsgFromServer = new Stream();
		streamMsgFromServer.FromJSON(streamStr);
		renderView(streamMsgFromServer.getImageData());
	}

	private void initViewWindow() {
		this.myViewer = new Viewer();
		this.frame = new JFrame("CLIENT WINDOW");
		frame.setSize(320, 240);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(myViewer);
	}

	private void renderView(String strImage) {
		byte[] base64_image = null;

		try {
			base64_image = strImage.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		byte[] nobase64_image = Base64.decodeBase64(base64_image);
		/* Decompress the image */
		byte[] decompressed_image = Compressor.decompress(nobase64_image);
		/* Give the raw image bytes to the viewer. */
		myViewer.ViewerInput(decompressed_image);
		frame.repaint();
	}

}
