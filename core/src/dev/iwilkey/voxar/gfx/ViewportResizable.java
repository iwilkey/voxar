package dev.iwilkey.voxar.gfx;

/**
 * Interface for objects requiring a callback when the application window is resized. 
 * @author iwilkey
 */
public interface ViewportResizable {
	/**
	 * Called when the application window resizes.
	 * @param nw new width, in pixels
	 * @param nh new height, in pixels
	 */
	public void windowResizeCallback(int nw, int nh);
}
