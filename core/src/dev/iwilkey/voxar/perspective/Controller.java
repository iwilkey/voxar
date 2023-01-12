package dev.iwilkey.voxar.perspective;

import com.badlogic.gdx.utils.Disposable;

/**
 * A specific implementation of PerspectiveController, a way to control a VoxelSpacePerspective.
 * @author iwilkey
 */
public abstract class Controller implements PerspectiveController, Disposable {
	
	protected boolean active;
	
	/**
	 * @return whether or not the controller is active.
	 */
	public boolean isActive() {
		return active;
	}
	
	/**
	 * Set whether or not the controller should react to input.
	 * @param active true or false.
	 */
	public void setActive(boolean active) {
		this.active = active;
	}
	
}
