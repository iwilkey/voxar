package dev.iwilkey.voxar.physics;

import com.badlogic.gdx.math.Matrix4;

/**
 * Interface for each unique VoxelRigidbody to process it's transform in real-time.
 * @author iwilkey
 */
public interface UniqueTransformProcess {
	/**
	 * @param uid the VoxelRigidbody's unique identification number.
	 * @param worldTrans the VoxelRigidbody's current transform in world space.
	 * @param args variable arguments for data out of the scope of the method.
	 */
	public void processTransform(long uid, Matrix4 worldTrans, String... args);
}
