package dev.iwilkey.voxar.settings;

import com.badlogic.gdx.math.Interpolation;

/**
 * A collection of variables that change how Voxar operates.
 * @author iwilkey
 *
 */
public class VoxarGameplayVariables {

	// Transformation variables. Hopefully the variable names are self explanatory..!
	public static float HORIZONTAL_LOOK_SENSITIVITY = 0.3f;
	public static float VERTICAL_LOOK_SENSITIVITY = 0.3f;
	public static float FORWARD_MOTION_SPEED = 1.0f;
	public static float BACKWARD_MOTION_SPEED = 1.0f;
	public static float STRAFE_SPEED = 1.0f;
	public static float ASCENTION_SPEED = 1.0f;
	public static float DECENTION_SPEED = 1.0f;
	/**
	 * Reference: https://libgdx.com/wiki/math-utils/interpolation
	 */
	public static float TRANSLATION_SMOOTHING_CONSTANT = 0.01f;
	/**
	 * Reference: https://libgdx.com/wiki/math-utils/interpolation
	 */
	public static Interpolation TRANSLATION_INTERPOLATION_TYPE = Interpolation.exp10Out;
	
}
