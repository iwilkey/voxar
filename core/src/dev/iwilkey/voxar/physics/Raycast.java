package dev.iwilkey.voxar.physics;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.physics.bullet.collision.ClosestRayResultCallback;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;

import dev.iwilkey.voxar.gfx.Renderer;
import dev.iwilkey.voxar.perspective.Perspective3D;

/**
 * Utility to deal with interacting with VoxelEntities and Terrain from a specific VoxelSpacePerspecitive.
 * @author iwilkey
 */
public final class Raycast {
	
	/**
	 * Utility method to perform a full Raycast. Used to extrapolate specific information desired by public functions.
	 */
	private static ClosestRayResultCallback raytest(Perspective3D persp, btDynamicsWorld world, float distance) {
		Vector3 rayFrom = new Vector3();
		Vector3 rayTo = new Vector3();
		Vector3 rayFromWorld = new Vector3();
		Vector3 rayToWorld   = new Vector3();
		
		Ray pickRay = persp.getPickRay(Renderer.WINDOW_WIDTH / 2f, Renderer.WINDOW_HEIGHT / 2f);
		rayFrom.set(pickRay.origin);
		rayTo.set(pickRay.direction).scl(distance).add(rayFrom);
		
		// Using the ClosestRay callback.
		final ClosestRayResultCallback callback = new ClosestRayResultCallback(rayFrom, rayTo);
		callback.setClosestHitFraction(1f);
		rayFromWorld.set(rayFrom.x, rayFrom.y, rayFrom.z);
		rayToWorld.set(rayTo.x, rayTo.y, rayTo.z);
		world.rayTest(rayFromWorld, rayToWorld, callback);
		
		if(callback.hasHit())
			return callback;
		
		return null;
	}
	
	/**
	 * Perform a Raycast to return the position the ray hit a physics object.
	 * @param persp the perspective.
	 * @param world the physics engine's dynamics world.
	 * @param distance the distance of the ray.
	 * @return a vector specifying the ray hitpoint, null otherwise.
	 */
	public static Vector3 hitPoint(Perspective3D persp, btDynamicsWorld world, float distance) {
		ClosestRayResultCallback result = raytest(persp, world, distance);
		Vector3 ret = new Vector3();
		if(result != null) {
			result.getHitPointWorld(ret);
			return ret;
		}
		return null;
	}
	
	/**
	 * Perform a Raycast to return the vRigidBody hit by the ray.
	 * @param persp the perspective.
	 * @param world the physics engine's dynamics world.
	 * @param distance the distance of the ray.
	 * @return the vRigidBody hit by ray, null otherwise.
	 */
	public static vRigidBody hitObject(Perspective3D persp, btDynamicsWorld world, float distance) {
		ClosestRayResultCallback result = raytest(persp, world, distance);
		if(result != null) 
			return (vRigidBody)result.getCollisionObject();
		return null;
	}
	
}
