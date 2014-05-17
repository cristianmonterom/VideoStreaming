package videostreaming;

import javax.swing.JFrame;

import org.bridj.Pointer;

import com.github.sarxos.webcam.ds.buildin.natives.Device;
import com.github.sarxos.webcam.ds.buildin.natives.DeviceList;
import com.github.sarxos.webcam.ds.buildin.natives.OpenIMAJGrabber;

public class VideoCapture implements Runnable {
	static Viewer myViewer = new Viewer();
	static JFrame frame = new JFrame("Simple Stream Viewer");
	static OpenIMAJGrabber grabber = new OpenIMAJGrabber();

	public VideoCapture() {
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

			myViewer.ViewerInput(raw_image);
			frame.repaint();
			//thread = new Thread(this, threadname);
//			System.out.println("New thread: ");
//			thread.sleep(100);
		}
	}

}
