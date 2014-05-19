package videostreaming;

import javax.swing.JFrame;

import org.apache.commons.codec.binary.Base64;
import org.bridj.Pointer;

import videostreaming.common.Constants;

import com.github.sarxos.webcam.ds.buildin.natives.Device;
import com.github.sarxos.webcam.ds.buildin.natives.DeviceList;
import com.github.sarxos.webcam.ds.buildin.natives.OpenIMAJGrabber;

public class VideoCapture implements Runnable {
	static Viewer myViewer = new Viewer();
	static JFrame frame = new JFrame("Simple Stream Viewer");
	static OpenIMAJGrabber grabber = new OpenIMAJGrabber();
	CurrentImage img;

	public VideoCapture(CurrentImage image, String title) {
		img = image;

		frame.setSize(Constants.WINDOW_WIDTH.getValue(),
				Constants.WINDOW_HEIGHT.getValue());
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(myViewer);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setTitle(title);

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

	public VideoCapture() {
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

			myViewer.ViewerInput(raw_image);
			frame.repaint();

			try {
				Thread.sleep(Constants.TIME_CAPTURE.getValue());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
