package dev.iwilkey.voxar.gfx.shadows.utils;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;

/** 
 * Directional Analyzer computes the camera's properties needed by directional light. Implementation should use main camera
 * frustum and scene objects to encompass all casting shadow objects.
 * @author realitix 
 */
public interface DirectionalAnalyzer {
	/** Compute the camera dimension based on directional light. Camera should be an orthographic camera.
	 * @param light Current directional light
	 * @param out Updated camera
	 * @param mainCamera Main Scene camera
	 * @return Camera Camera out for chaining */
	public Camera analyze (DirectionalLight light, Camera out, Camera mainCamera);
}
