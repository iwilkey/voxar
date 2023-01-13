package dev.iwilkey.voxar.gfx.shadows.utils;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.environment.BaseLight;

/** 
 * This Filter does not block lights. All lights are allowed.
 * @author realitix 
 */
public class AllLightFilter implements LightFilter {
	@Override
	public boolean filter (@SuppressWarnings("rawtypes") BaseLight light, Camera camera, Camera mainCamera) {
		return true;
	}
}
