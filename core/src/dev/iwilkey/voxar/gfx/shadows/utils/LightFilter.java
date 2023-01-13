package dev.iwilkey.voxar.gfx.shadows.utils;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.environment.BaseLight;

/** 
 * Select only casting shadow lights. Allows to optimize shadow system.
 * @author realitix 
 */
public interface LightFilter {
	/** Return true if light should be used for shadow computation.
	 * @param light Current light
	 * @param camera Light's camera
	 * @param mainCamera Main scene camera
	 * @return boolean */
	public boolean filter (@SuppressWarnings("rawtypes") BaseLight light, Camera camera, Camera mainCamera);
}
