package dev.iwilkey.voxar.physics;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.bullet.linearmath.btMotionState;

/**
 * An abstraction of btMotionState, an interface class that allows the dynamics world to synchronize and 
 * interpolate the updated world transforms with graphics. Each VoxelRigidbody has
 * a UniqueMotion object, giving unique and real-time information about the transform
 * of every active VoxelRigidbody.
 * @author iwilkey
 */
public final class UniqueMotion extends btMotionState {
	
	/**
	 * The unique identification number of the VoxelRigidbody registered by the 
	 * VoxelEntityManager.
	 */
	private long uid = -1;
	
	/**
	 * See documentation of "dev.iwilkey.Voxar.physics.UniqueTransformProcess".
	 */
	private UniqueTransformProcess process = null;
	
	/**
	 * The current transform of the VoxelRigidbody.
	 */
	private Matrix4 transform = null;
	
	public UniqueMotion(long uid) {
		this.uid = uid;
	}
	
	/**
	 * Obtain the current transform of the VoxelRigidbody in world space.
	 * @param worldTrans the Matrix4 object to store the returned transform.
	 */
	@Override
	public void getWorldTransform(Matrix4 worldTrans) {
		worldTrans.set(transform);
	}
	
	/**
	 * Set the current transform of the VoxelRigidbody in world space.
	 */
	@Override
	public void setWorldTransform(Matrix4 worldTrans) {
		if(process != null)
			process.processTransform(uid, worldTrans);
		transform.set(worldTrans);
	}
	
	/**
	 * @return the current transform of the VoxelRigidbody.
	 */
	public Matrix4 getTransform() {
		return transform;
	}
	
	/**
	 * Set the VoxelRigidbody's unique process to undergo with data pertaining to it's current transform.
	 * @param process the process.
	 */
	public void setUniqueTransformProcess(UniqueTransformProcess process) {
		this.process = process;
	}
	
	/**
	 * Set the transformation of the VoxelRigidbody manually.
	 * @param trans the transformation.
	 */
	public void setTransform(Matrix4 trans) {
		transform = trans;
	}

}
