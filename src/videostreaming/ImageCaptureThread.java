package videostreaming;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

import javax.swing.JFrame;

import org.apache.commons.codec.binary.Base64;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import videostreaming.common.Constants;
import videostreaming.common.ProtocolMessages;
import videostreaming.messaging.*;

/**
 * 
 * @author santiago
 *
 */
public class ImageCaptureThread implements Runnable {

	private PrintWriter out;
	private BufferedReader in;
	private int servicePort;
	StartStreamResponse startStreamResponse;
	Thread keyboardThread;
	KeyboardCapture keyboardCapture;
	Viewer myViewer = null;
	JFrame frame = null;
	CurrentImage img;

	private OutputStream outputStream;
	private InputStream inputStream;
	private Socket socketRef;
	private int rate;
	
	public ImageCaptureThread(Socket socket, int servicePort, CurrentImage image, int ratelimit) {
		rate = ratelimit;
		socketRef = socket;
		
		this.servicePort = servicePort;
		this.img = image;
		try{
			
			inputStream = socket.getInputStream();
			outputStream = socket.getOutputStream();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		out = new PrintWriter(outputStream, true);
		in = new BufferedReader(new InputStreamReader(inputStream));

		initViewWindow();
		keyboardCapture = new KeyboardCapture();
		keyboardThread = new Thread(keyboardCapture);
	}

	public void run() {
		keyboardThread.start();
		
		try {
			sendStartStreamRequest();
		} catch (IOException e) {
			e.printStackTrace();
		}
		receiveStartStreamResponse();
		
		do {
			receiveImages();
		} while (keyboardCapture.isRunning());
		
		sendStopStreamRequest();

		receiveStopStreamResponse();
		
		//Don't close socket until all messages have been 'consumed properly til get StopSTreamed
		System.out.println("Connection closed!!!");
		
		out.close();
		try{
			in.close();
			socketRef.close();
		}catch(IOException ex){
			ex.printStackTrace();
		}
		
		System.exit(-1);
	}

	private void receiveStartStreamResponse() {
		String strFromServer = null;
		StartStreamResponse startStreamResponse;
		try {
			strFromServer = in.readLine();
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		}

		startStreamResponse = new StartStreamResponse();
		startStreamResponse.FromJSON(strFromServer);
	}

	private void sendStartStreamRequest() throws IOException {
		StartStreamRequest request = new StartStreamRequest(this.servicePort, this.rate);
		out.println(request.ToJSON());
	}
	
	private void sendStopStreamRequest(){
		StopStreamRequest stopStreamReq = new StopStreamRequest();
		String str = stopStreamReq.ToJSON();
		out.println(str);
	}
	
	private void receiveStopStreamResponse(){
		String strFromServer = null;
		
		JSONObject obj = null;
		final JSONParser parser = new JSONParser();
		
		do{
			try{
				strFromServer = in.readLine();
			}
			catch(IOException ex){
				ex.printStackTrace();
			}

			try{
				obj = (JSONObject) parser.parse(strFromServer);
			}catch(ParseException ex){
				ex.printStackTrace();
			}
		}while(!obj.get(ProtocolMessages.Response.getValue()).equals(ProtocolMessages.StoppedStream.getValue()));
		
	}

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
			streamMsgFromServer.getImageData();

			if (streamMsgFromServer.isCorrectMessage()) {
				renderView(streamMsgFromServer.getImageData());
				img.setImageToDisplay(streamMsgFromServer.getImageData().getBytes("UTF-8"));
			} else {
				StopStreamResponse stopStreamResp = new StopStreamResponse();
				stopStreamResp.FromJSON(streamStr);
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

//	public boolean isRunning() {
//		return running;
//	}

}
