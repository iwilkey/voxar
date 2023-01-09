package dev.iwilkey.voxar.perspective;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;

import dev.iwilkey.voxar.clock.Tickable;
import dev.iwilkey.voxar.input.StandardInput;
import dev.iwilkey.voxar.settings.VoxarGameplayVariables;
import dev.iwilkey.voxar.settings.VoxarKeyBindings;

/**
 * An abstraction of OrthographicCamera, adding high-level control of the rendering perspective of a VoxelSpace.
 * @author iwilkey
 */
public final class VoxelSpacePerspective extends PerspectiveCamera implements Tickable {
	
	public static final float VERT_DEGREE_CLAMP = 90.0f;
	
	/**
	 * The desired position of the camera. Used for smooth interpolation.
	 */
	private Vector3 desiredPosition = new Vector3();
	
	/**
	 * The current angle of the vertical look rotation, in degrees.
	 */
	private double rotVertAngle = 0.0f;

	public VoxelSpacePerspective(int fov, int viewportWidth, int viewportHeight) {
		super(fov, viewportWidth, viewportHeight);
		position.set(0, 2, 0);
		desiredPosition = position.cpy();
		direction.set(Vector3.X);
		near = 1f;
		setRenderDistance(100);
		update();
	}

	@Override
	public void tick() {
		
		// Handle translation on x, z plane and y axis.
		if(StandardInput.keyCurrent(VoxarKeyBindings.MOVE_FORWARD)) 
			desiredPosition.add(new Vector3(direction.x * VoxarGameplayVariables.FORWARD_MOTION_SPEED, 
					0, direction.z * VoxarGameplayVariables.FORWARD_MOTION_SPEED));
		if(StandardInput.keyCurrent(VoxarKeyBindings.MOVE_BACKWARD)) 
			desiredPosition.sub(new Vector3(direction.x * VoxarGameplayVariables.BACKWARD_MOTION_SPEED, 
					0, direction.z * VoxarGameplayVariables.BACKWARD_MOTION_SPEED));
		if(StandardInput.keyCurrent(VoxarKeyBindings.STRAFE_LEFT)) 
			desiredPosition.add(new Vector3(direction.z * VoxarGameplayVariables.STRAFE_SPEED, 
					0, -direction.x * VoxarGameplayVariables.STRAFE_SPEED));
		if(StandardInput.keyCurrent(VoxarKeyBindings.STRAFE_RIGHT))
			desiredPosition.sub(new Vector3(direction.z * VoxarGameplayVariables.STRAFE_SPEED, 
					0, -direction.x * VoxarGameplayVariables.STRAFE_SPEED));
		if(StandardInput.keyCurrent(VoxarKeyBindings.DESCEND))
			desiredPosition.sub(Vector3.Y.scl(VoxarGameplayVariables.ASCENTION_SPEED));
		if(StandardInput.keyCurrent(VoxarKeyBindings.ASCEND))
			desiredPosition.add(Vector3.Y.scl(VoxarGameplayVariables.DECENTION_SPEED));
		position.interpolate(desiredPosition, VoxarGameplayVariables.TRANSLATION_SMOOTHING_CONSTANT, 
				VoxarGameplayVariables.TRANSLATION_INTERPOLATION_TYPE);
		
		// Handle the change in look.
		int deltaX = Gdx.input.getDeltaX();
		double deltaHor = -deltaX * VoxarGameplayVariables.HORIZONTAL_LOOK_SENSITIVITY;
		direction.rotate(Vector3.Y, (float)deltaHor);
	
		int deltaY = Gdx.input.getDeltaY();
		double newVertRotAngle = rotVertAngle;
		double deltaVert = -deltaY * VoxarGameplayVariables.VERTICAL_LOOK_SENSITIVITY;
		newVertRotAngle += deltaVert;
		if(newVertRotAngle >= -VERT_DEGREE_CLAMP && newVertRotAngle <= VERT_DEGREE_CLAMP) {
			rotVertAngle = newVertRotAngle;
			direction.rotate(new Vector3(-direction.z, 0, direction.x), (float)deltaVert);
		}
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
	
}
