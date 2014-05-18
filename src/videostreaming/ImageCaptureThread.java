package videostreaming;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.swing.JFrame;

import org.apache.commons.codec.binary.Base64;

import videostreaming.messaging.*;


public class ImageCaptureThread implements Runnable {
	
	private DataOutputStream output;
	private DataInputStream	input;
	private byte[] rawimage;
	
	
	Viewer myViewer = null;
	JFrame frame = null;
	
	public ImageCaptureThread( DataInputStream dIS, DataOutputStream dOS)
	{
		this.output = dOS;
		this.input = dIS;
		initViewWindow();
	}
	
	
	public void run() {
		
		System.err.println("Should receive messages to init and start streaming");
		//read response message from server
		receiveResponse();
		sendStartStreamRequest();
		do{
			receiveImages();
		}while(true);
		
	}
	
	private void receiveResponse()
	{
		String strFromServer = null;
		RequestResponse rcvdRespFromServer;
		try{
			strFromServer= input.readUTF();
		}
		catch(IOException ioEx){
			ioEx.printStackTrace();
		}
		
		
		System.err.println(strFromServer);
		rcvdRespFromServer = new StatusResponse();
		rcvdRespFromServer.FromJSON(strFromServer);
//		
//		System.out.println("receiving as client !!!:  "+rcvdRespFromServer.ToJSON());
		
	}
	
	private void sendStartStreamRequest()
	{
		StartStreamRequest request = new StartStreamRequest();
		request = new StartStreamRequest();
		
		try{
			output.writeUTF(request.ToJSON());
//			output.writeUTF("hola");
		}
		catch(IOException ex){
			ex.printStackTrace();
		}
		
		System.err.println("send as client"+request.ToJSON());
	}
	
	private void receiveImages()
	{
		String streamStr = null;
		Stream streamMsgFromServer;
		String imgStr;
		String totalImageStr="";
		
		do{
			try{
				streamStr = input.readUTF();
			}catch(IOException ex){
				ex.printStackTrace();
			}
			
//			System.out.println(streamStr);
			
			streamMsgFromServer = new Stream();
			streamMsgFromServer.FromJSON(streamStr);
			imgStr = streamMsgFromServer.getImageData();
			
			totalImageStr+=imgStr;
			
		 }while(!imgStr.endsWith(streamMsgFromServer.endMessage) );
		
//		System.err.println("sale del loop de recepcion" + totalImageStr.length());
		
//		System.err.println(totalImageStr);
		
		renderView(totalImageStr);
		
	}
	
	
	private void initViewWindow()
	{
		this.myViewer = new Viewer();
		this.frame = new JFrame("CLIENT WINDOW");
		frame.setSize(320, 240);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(myViewer);
	}
	
	private void renderView(String strImage)
	{
		byte[] base64_image = null;

		try {
			base64_image = strImage.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
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


