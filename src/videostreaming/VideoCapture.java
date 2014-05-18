package videostreaming;

import java.io.UnsupportedEncodingException;

import javax.swing.JFrame;

import org.apache.commons.codec.binary.Base64;
import org.bridj.Pointer;


import com.github.sarxos.webcam.ds.buildin.natives.Device;
import com.github.sarxos.webcam.ds.buildin.natives.DeviceList;
import com.github.sarxos.webcam.ds.buildin.natives.OpenIMAJGrabber;

public class VideoCapture implements Runnable {
	static Viewer myViewer = new Viewer();
	static JFrame frame = new JFrame("Simple Stream Viewer");
	static OpenIMAJGrabber grabber = new OpenIMAJGrabber();
	CurrentImage img;

	public VideoCapture(CurrentImage image) {
		img = image;
		
		frame.setSize(320, 240);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(myViewer);

		grabber = new OpenIMAJGrabber();

		Device device = null;
		Pointer<DeviceList> devices = grabber.getVideoDevices();
		for (Device d : devices.get().asArrayList()) {
			device = d;
			break;
		}

		boolean started = grabber.startSession(320, 240, 30,
				Pointer.pointerTo(device));
		if (!started) {
			throw new RuntimeException("Not able to start native grabber!");
		}
	}

	public VideoCapture(JFrame _frame, Viewer _myViewer) {
		this.frame = _frame;
		this.myViewer = _myViewer;
	}

	@Override
	public void run() {
		while (true) {
			grabber.nextFrame();
			/* Get the raw bytes of the frame. */
			byte[] raw_image = grabber.getImage().getBytes(320 * 240 * 3);
			byte[] compressed_image = Compressor.compress(raw_image);
			/* Prepare the date to be sent in a text friendly format. */
			byte[] base64_image = Base64.encodeBase64(compressed_image);
			
			
			img.setImageToDisplay(base64_image.clone());
			
			String str = null;
			try {
				str = new String(img.getImageToDisplay(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			System.err.println(str);

			myViewer.ViewerInput(raw_image);
			frame.repaint();
			
			
			try {
				Thread.sleep(1000);
				System.err.println("durmiendo");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//thread = new Thread(this, threadname);
//			System.out.println("New thread: ");
//			thread.sleep(100);
		}
	}


}
