package videostreaming;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import javax.swing.JPanel;

import videostreaming.common.Constants;

public class Viewer extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BufferedImage image;
//	int posx;
//	int posy;

	private int[] toIntArray(byte[] barr) {
		int[] result = new int[barr.length];
		for (int i = 0; i < barr.length; i++)
			result[i] = barr[i];
		return result;
	}

	public Viewer() {
//		posx = (this.getHeight() - Constants.VIDEO_HEIGHT.getValue()) / 2;
//		posy = (this.getWidth() - Constants.VIDEO_WIDTH.getValue()) / 2;
		image = new BufferedImage(Constants.VIDEO_WIDTH.getValue(),
				Constants.VIDEO_HEIGHT.getValue(), BufferedImage.TYPE_3BYTE_BGR);
	}

	public void ViewerInput(byte[] image_bytes) {
		WritableRaster raster = image.getRaster();
//		System.out.println("x: " + posx + " - y: " + posy);
		raster.setPixels(0, 0, Constants.VIDEO_WIDTH.getValue(),
				Constants.VIDEO_HEIGHT.getValue(), toIntArray(image_bytes));
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, null); // see javadoc for more info on the
										// parameters
	}

}
