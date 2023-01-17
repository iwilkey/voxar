package dev.iwilkey.voxar.perspective;

/**
 * Interface for controlling a camera from an engine state.
 * @author iwilkey
 */
public interface PerspectiveController {
	/**
	 * Move / rotate the camera. Called every tick.
	 * @param perspective the camera.
	 */
	public void control(Perspective3D perspective);
}
