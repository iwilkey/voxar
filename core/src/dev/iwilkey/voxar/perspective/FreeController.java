package dev.iwilkey.voxar.perspective;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector3;

import dev.iwilkey.voxar.gfx.Raster2;
import dev.iwilkey.voxar.gfx.RasterRenderer;
import dev.iwilkey.voxar.gfx.VoxarRenderer;
import dev.iwilkey.voxar.input.StandardInput;
import dev.iwilkey.voxar.settings.KeyBinding;

/**
 * A Controller that allows one to traverse a VoxelSpace freely. Includes crosshair.
 * @author iwilkey
 */
public final class FreeController extends Controller {
	
	private static final float VERT_DEGREE_CLAMP = 88.0f;
	
	// Crosshair rendering...
	private Raster2 crosshair;
	private int crosshairDim;
	
	// Vector math...
	private Vector3 xzDirection;
	private Vector3 target;
	
	// Control variables...
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
		horLookSens = 0.2f;
		vertLookSens = 0.2f;
		forwardSpeed = 1.0f;
		strafeSpeed = 1.0f;
		upSpeed = 1.0f;
		translationSmoothingConstant = 0.01f;
		translationInterpolationType = Interpolation.exp10Out;
		
		// Set up vector math...
		xzDirection = new Vector3(1, 0, 0); // Must be the same as initial direction of the active VoxelSpacePerspective.
		target = new Vector3(0, 2, 0); // Must be the same as the initial position of the active VoxelSpacePerspective.
		
		// Set default control bindings...
		KeyBinding.setBinding("free_controller_forward", Keys.W);
		KeyBinding.setBinding("free_controller_backward", Keys.S);
		KeyBinding.setBinding("free_controller_strafe_right", Keys.D);
		KeyBinding.setBinding("free_controller_strafe_left", Keys.A);
		KeyBinding.setBinding("free_controller_ascend", Keys.SPACE);
		KeyBinding.setBinding("free_controller_descend", Keys.SHIFT_LEFT);
		
		// Crosshair initialization...
		crosshair = new Raster2(Gdx.files.internal("img/crosshair.png"));
		crosshair.setTint(Color.WHITE);
		// 12 px x 12 px crosshair by default...
		crosshairDim = 12;
		
		this.active = true;
	}
	
	@Override
	public void control(VoxelSpacePerspective perspective) {
		// Update position no matter if active or not.
		perspective.position.interpolate(target, translationSmoothingConstant, translationInterpolationType);
		
		if(!active)
			return;
		
		renderCrosshair();
		
		// Translation...
		if(StandardInput.keyCurrent(KeyBinding.getBinding("free_controller_forward")))
			target.add(new Vector3(xzDirection.x * forwardSpeed, 0, xzDirection.z * forwardSpeed));
		if(StandardInput.keyCurrent(KeyBinding.getBinding("free_controller_backward")))
			target.sub(new Vector3(xzDirection.x * forwardSpeed, 0, xzDirection.z * forwardSpeed));
		if(StandardInput.keyCurrent(KeyBinding.getBinding("free_controller_strafe_right")))
			target.add(new Vector3(-xzDirection.z * strafeSpeed, 0, xzDirection.x * strafeSpeed));
		if(StandardInput.keyCurrent(KeyBinding.getBinding("free_controller_strafe_left")))
			target.sub(new Vector3(-xzDirection.z * strafeSpeed, 0, xzDirection.x * strafeSpeed));
		if(StandardInput.keyCurrent(KeyBinding.getBinding("free_controller_ascend")))
			target.add(Vector3.Y.scl(upSpeed));
		if(StandardInput.keyCurrent(KeyBinding.getBinding("free_controller_descend")))
			target.sub(Vector3.Y.scl(upSpeed));
		
		// Get mouse delta...
		int deltaX = Gdx.input.getDeltaX();
		int deltaY = Gdx.input.getDeltaY();
		
		// Update horizontal look...
		double deltaHor = -deltaX * horLookSens;
		perspective.direction.rotate(Vector3.Y, (float)deltaHor);
		xzDirection.rotate(Vector3.Y, (float)deltaHor);
		
		// Update vertical look...
		double newVertRotAngle = rotVertAngle;
		double deltaVert = -deltaY * vertLookSens;
		newVertRotAngle += deltaVert;
		if(newVertRotAngle >= -VERT_DEGREE_CLAMP && newVertRotAngle <= VERT_DEGREE_CLAMP) {
			rotVertAngle = newVertRotAngle;
			perspective.direction.rotate(new Vector3(-perspective.direction.z, 0, perspective.direction.x), (float)deltaVert);
		}
	}
	
	/**
	 * Render the crosshair to the screen using the RasterRenderer.
	 */
	private void renderCrosshair() {
		final int x = (VoxarRenderer.WW / 2) - (crosshairDim / 2);
		final int y = (VoxarRenderer.WH / 2) - (crosshairDim / 2);
		RasterRenderer.renderToViewport(crosshair, x, y, crosshairDim, crosshairDim);
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
		KeyBinding.setBinding("free_controller_strafe_right", key);
	}
	
	/**
	 * Set the key that moves the camera to the right.
	 * @param key the key.
	 */
	public void setLeftStrafeKeyBinding(int key) {
		KeyBinding.setBinding("free_controller_strafe_left", key);
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
	 * Set how big the rendered crosshair is, in px / px.
	 * @param px the square side length, in pixels. For example, px = 25, the crosshair is rendered 25 px x 25 px in the center of the screen.
	 */
	public void setCrosshairDimensions(int px) {
		crosshairDim = px;
	}
	
	/**
	 * Set the tint of the crosshair, applied at RasterRenderer render time.
	 * @param color the rgba color.
	 */
	public void setCrosshairColor(Color color) {
		crosshair.setTint(color);
	}
	
	@Override
	public void dispose() {
		crosshair.dispose();
	}
}
