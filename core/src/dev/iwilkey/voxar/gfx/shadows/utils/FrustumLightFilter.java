package dev.iwilkey.voxar.gfx.shadows.utils;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.environment.BaseLight;
import com.badlogic.gdx.math.Frustum;
import com.badlogic.gdx.math.collision.BoundingBox;

/** 
 * This Filter allows lights that are in camera frustum.
 * @author realitix 
 */
public class FrustumLightFilter implements LightFilter {
	/** Bounding box used for computation */
	protected BoundingBox bb = new BoundingBox();

	@Override
	public boolean filter (@SuppressWarnings("rawtypes") BaseLight light, Camera camera, Camera mainCamera) {
		Frustum f1 = mainCamera.frustum;
		Frustum f2 = camera.frustum;
		bb.inf();

		for (int i = 0; i < f2.planePoints.length; i++) {
			bb.ext(f2.planePoints[i]);
		}

		if (f1.boundsInFrustum(bb)) {
			return true;
		}

		return false;
	}
}
