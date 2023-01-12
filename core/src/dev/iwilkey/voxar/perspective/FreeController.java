package dev.iwilkey.voxar.perspective;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector3;

import dev.iwilkey.voxar.input.StandardInput;
import dev.iwilkey.voxar.settings.KeyBinding;

/**
 * A PerspectiveController that allows one to traverse a VoxelSpace freely.
 * @author iwilkey
 */
public final class FreeController implements PerspectiveController {
	
	// Control variables...
	private static final float VERT_DEGREE_CLAMP = 90.0f;
	private Vector3 xzDirection;
	private Vector3 target;
	private boolean active;
	private double rotVertAngle;
	private float horLookSens;
	private float vertLookSens;
	private float forwardSpeed;
	private float strafeSpeed;
	private float upSpeed;
	private float translationSmoothingConstant;
	private Interpolation translationInterpolationType;
	
	public FreeController() {
		// Set default control variables...
		rotVertAngle = 0.0f;
		horLookSens = 0.3f;
		vertLookSens = 0.3f;
		forwardSpeed = 1.0f;
		strafeSpeed = 1.0f;
		upSpeed = 1.0f;
		translationSmoothingConstant = 0.01f;
		translationInterpolationType = Interpolation.exp10Out;
		active = true;
		xzDirection = new Vector3(1, 0, 0);
		target = new Vector3(0, 2, 0);
		
		// Set default control bindings...
		KeyBinding.setBinding("free_controller_forward", Keys.W);
		KeyBinding.setBinding("free_controller_backward", Keys.S);
		KeyBinding.setBinding("free_controller_stafe_right", Keys.D);
		KeyBinding.setBinding("free_controller_stafe_left", Keys.A);
		KeyBinding.setBinding("free_controller_ascend", Keys.SPACE);
		KeyBinding.setBinding("free_controller_descend", Keys.SHIFT_LEFT);
	}
	
	@Override
	public void control(VoxelSpacePerspective perspective) {	
		perspective.position.interpolate(target, translationSmoothingConstant, 
				translationInterpolationType);
		if(!active) return;
		// Translation...
		if(StandardInput.keyCurrent(KeyBinding.getBinding("free_controller_forward")))
			target.add(new Vector3(xzDirection.x * forwardSpeed, 0, xzDirection.z * forwardSpeed));
		if(StandardInput.keyCurrent(KeyBinding.getBinding("free_controller_backward")))
			target.sub(new Vector3(xzDirection.x * forwardSpeed, 0, xzDirection.z * forwardSpeed));
		if(StandardInput.keyCurrent(KeyBinding.getBinding("free_controller_stafe_right")))
			target.add(new Vector3(-xzDirection.z * strafeSpeed, 0, xzDirection.x * strafeSpeed));
		if(StandardInput.keyCurrent(KeyBinding.getBinding("free_controller_stafe_left")))
			target.sub(new Vector3(-xzDirection.z * strafeSpeed, 0, xzDirection.x * strafeSpeed));
		if(StandardInput.keyCurrent(KeyBinding.getBinding("free_controller_ascend")))
			target.add(Vector3.Y.scl(upSpeed));
		if(StandardInput.keyCurrent(KeyBinding.getBinding("free_controller_descend")))
			target.sub(Vector3.Y.scl(upSpeed));
		// Look...
		int deltaX = Gdx.input.getDeltaX();
		double deltaHor = -deltaX * horLookSens;
		perspective.direction.rotate(Vector3.Y, (float)deltaHor);
		xzDirection.rotate(Vector3.Y, (float)deltaHor);
		int deltaY = Gdx.input.getDeltaY();
		double newVertRotAngle = rotVertAngle;
		double deltaVert = -deltaY * vertLookSens;
		newVertRotAngle += deltaVert;
		if(newVertRotAngle >= -VERT_DEGREE_CLAMP && newVertRotAngle <= VERT_DEGREE_CLAMP) {
			rotVertAngle = newVertRotAngle;
			perspective.direction.rotate(new Vector3(-perspective.direction.z, 0, perspective.direction.x), (float)deltaVert);
		}
	}
	
	/**
	 * Set the sensitivity of the horizontal look.
	 * @param horLookSens the value.
	 */
	public void setHorizontalLookSensitivity(float horLookSens) {
		this.horLookSens = horLookSens;
	}
	
	/**
	 * Set the sensitivity of the vertical look.
	 * @param horLookSens the value.
	 */
	public void setVerticalLookSensitivity(float vertLookSens) {
		this.vertLookSens = vertLookSens;
	}
	
	/**
	 * Set the forward speed of translation.
	 * @param forwardSpeed the speed.
	 */
	public void setForwardSpeed(float forwardSpeed) {
		this.forwardSpeed = forwardSpeed;
	}
	
	/**
	 * Set the strafe speed of translation.
	 * @param forwardSpeed the speed.
	 */
	public void setStrafeSpeed(float strafeSpeed) {
		this.strafeSpeed = strafeSpeed;
	}
	
	/**
	 * Set the up speed of translation.
	 * @param upSpeed the speed.
	 */
	public void setUpSpeed(float upSpeed) {
		this.upSpeed = upSpeed;
	}
	
	/**
	 * Set the interpolation constant of the translation.
	 * @param translationSmoothingConstant the value.
	 */
	public void setTranslationInterpolationConstant(float translationSmoothingConstant) {
		this.translationSmoothingConstant = translationSmoothingConstant;
	}
	
	/**
	 * Set the interpolation type of the translation.
	 * @param translationInterpolationType the interpolation type.
	 */
	public void setInterpolationType(Interpolation translationInterpolationType) {
		this.translationInterpolationType = translationInterpolationType;
	}
	
	/**
	 * Set the key that moves the camera forward.
	 * @param key the key.
	 */
	public void setForwardKeyBinding(int key) {
		KeyBinding.setBinding("free_controller_forward", key);
	}
	
	/**
	 * Set the key that moves the camera backward.
	 * @param key the key.
	 */
	public void setBackwardKeyBinding(int key) {
		KeyBinding.setBinding("free_controller_backward", key);
	}
	
	/**
	 * Set the key that moves the camera to the right.
	 * @param key the key.
	 */
	public void setRightStrafeKeyBinding(int key) {
		KeyBinding.setBinding("free_controller_stafe_right", key);
	}
	
	/**
	 * Set the key that moves the camera to the right.
	 * @param key the key.
	 */
	public void setLeftStrafeKeyBinding(int key) {
		KeyBinding.setBinding("free_controller_stafe_left", key);
	}
	
	/**
	 * Set the key that moves the camera up.
	 * @param key the key.
	 */
	public void setAscendKeyBinding(int key) {
		KeyBinding.setBinding("free_controller_ascend", key);
	}
	
	/**
	 * Set the key that moves the camera down.
	 * @param key the key.
	 */
	public void setDescendKeyBinding(int key) {
		KeyBinding.setBinding("free_controller_descend", key);
	}
	
	/**
	 * Set whether or not the controller should react to input.
	 * @param active true or false.
	 */
	public void setActive(boolean active) {
		this.active = active;
	}
}
