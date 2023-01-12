package dev.iwilkey.voxar.perspective;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

import dev.iwilkey.voxar.clock.Tickable;

/**
 * An abstraction of PerspectiveCamera, adding high-level control of the rendering perspective of a VoxelSpace through Controllers.
 * @author iwilkey
 */
public final class VoxelSpacePerspective extends PerspectiveCamera implements Tickable, Disposable {
	
	/**
	 * Current perspective controller.
	 */
	Controller controller;

	public VoxelSpacePerspective(int fov, int viewportWidth, int viewportHeight) {
		super(fov, viewportWidth, viewportHeight);
		position.set(0, 2, 0);
		direction.set(Vector3.X);
		near = 1f;
		setRenderDistance(1000);
		update();
	}

	@Override
	public void tick() {
		if(controller == null) return;
		controller.control(this);
		update();
	}
	
	/**
	 * Set the desired render distance of the camera.
	 * @param dist the distance.
	 */
	public void setRenderDistance(float dist) {
		far = dist;
		update();
	}
	
	/**
	 * Set the perspective controller.
	 * @param controller the controller.
	 */
	public void setController(Controller controller) {
		this.controller = controller;
	}

	@Override
	public void dispose() {
		controller.dispose();
	}
	
}
