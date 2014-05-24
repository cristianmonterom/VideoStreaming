package videostreaming;

import java.awt.Graphics;
import java.awt.KeyEventDispatcher;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

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
		image = new BufferedImage(Constants.VIDEO_WIDTH.getValue(),
				Constants.VIDEO_HEIGHT.getValue(), BufferedImage.TYPE_3BYTE_BGR);
		

//		KeyEventDispatcher keyEventDispatcher = new KeyEventDispatcher() {
//			  @Override
//			  public boolean dispatchKeyEvent(final KeyEvent e) {
//			    if (e.getID() == KeyEvent.KEY_TYPED) {
//			      System.out.println(e);
//			    }
//			    // Pass the KeyEvent to the next KeyEventDispatcher in the chain
//			    return false;
//			  }
//			};
//			addKeyListener(keyEventDispatcher);
		//		 InputMap im = getInputMap(WHEN_FOCUSED);
//	        ActionMap am = getActionMap();
//
//	        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "onEnter");
//
//	        am.put(im, new AbstractAction() {
//	            public void actionPerformed(ActionEvent e) {
//	                System.exit(0);
//	            }
//	        });

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

//	@Override
//	public void keyPressed(KeyEvent arg0) {
//		System.out.println("key" + arg0);
//		if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
//			System.exit(0);
//		}
//		
//	}
//
//	@Override
//	public void keyReleased(KeyEvent arg0) {
//		System.out.println("keya" + arg0);
//		if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
//			System.exit(0);
//		}
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void keyTyped(KeyEvent arg0) {
//		System.out.println("keyb" + arg0);
//		if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
//			System.exit(0);
//		}
//		// TODO Auto-generated method stub
//		
//	}

//		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(keyEventDispatcher);
}
