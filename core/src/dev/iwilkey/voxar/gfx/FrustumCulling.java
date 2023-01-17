package dev.iwilkey.voxar.gfx;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

import dev.iwilkey.voxar.entity.VoxelEntity;
import dev.iwilkey.voxar.perspective.Perspective3D;

/**
 * Utility class to perform 3D frustum culling for any active RenderableProvider in a VoxelSpace.
 * @author iwilkey
 */
public final class FrustumCulling {
	
	/**
	 * Perform a spherical frustum test with any ModelInstance or abstraction.
	 * @param instance ModelInstance or abstraction to be tested.
	 * @param camera the camera.
	 * @return whether or not the instance is visible in the camera's frustum using an encapsulating sphere.
	 */
	public static boolean sphericalTestWith(ModelInstance instance, Perspective3D camera) {
		// If the entity is a VoxelEntity, this test can be performed much faster.
		if(instance instanceof VoxelEntity) {
			Vector3 position = Vector3.Zero;
			instance.transform.getTranslation(position);
			position.add(((VoxelEntity)instance).getCenterOfBounding());
			return camera.frustum.sphereInFrustum(position, ((VoxelEntity)instance).getRadius());
		}
		
		// Otherwise, manually calculate bounding.
		BoundingBox bounding = new BoundingBox();
		Vector3 centerOfBounding = new Vector3();
		Vector3 dimensions = new Vector3();
		float radius = 0.0f;
		instance.calculateBoundingBox(bounding);
		bounding.getCenter(centerOfBounding);
		bounding.getDimensions(dimensions);
		radius = dimensions.len() / 2f;
		
		// Perform frustum test.
		Vector3 position = Vector3.Zero;
		instance.transform.getTranslation(position);
		position.add(centerOfBounding);
		return camera.frustum.sphereInFrustum(position, radius);
	}
	
	/**
	 * Perform a cuboid frustum test with any ModelInstance or abstraction.
	 * @param instance ModelInstance or abstraction to be tested.
	 * @param camera the camera.
	 * @return whether or not the instance is visible in the camera's frustum using an encapsulating cuboid.
	 */
	public static boolean cuboidTestWith(ModelInstance instance, Perspective3D camera) {
		// If the entity is a VoxelEntity, this test can be performed much faster.
		if(instance instanceof VoxelEntity) {
			Vector3 position = Vector3.Zero;
			instance.transform.getTranslation(position);
			position.add(((VoxelEntity)instance).getCenterOfBounding());
			return camera.frustum.boundsInFrustum(position, ((VoxelEntity)instance).getDimensions());
		}
		
		// Otherwise, manually calculate bounding.
		BoundingBox bounding = new BoundingBox();
		Vector3 centerOfBounding = new Vector3();
		Vector3 dimensions = new Vector3();
		instance.calculateBoundingBox(bounding);
		bounding.getCenter(centerOfBounding);
		bounding.getDimensions(dimensions);
		
		// Perform frustum test.
		Vector3 position = Vector3.Zero;
		instance.transform.getTranslation(position);
		position.add(centerOfBounding);
		return camera.frustum.boundsInFrustum(position, dimensions);
		
	}
	
}
