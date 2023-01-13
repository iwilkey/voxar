package dev.iwilkey.voxar.entity;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Disposable;

/**
 * Anything with distinct and independent existence inside of a VoxelSpace.
 * @author iwilkey
 */
public class VoxelEntity extends ModelInstance implements Disposable {
	
	/**
	 * The name of the entity. Does not need to be unique.
	 */
	private String name = "";
	
	/**
	 * The unique identification number of the entity. If active in a VoxelSpace, it is a non-negative and unique integer.
	 */
	private long uid = -1L;
	
	/**
	 * Health value of entity. If <= 0, the entity is not longer allowed to be active in the VoxelSpace.
	 */
	private double health = 1f;
	
	/**
	 * The dimensions of this entity. Used mostly for frustum culling.
	 */
	private BoundingBox bounding;
	
	/**
	 * Return the center of the bounding box.
	 */
	private Vector3 centerOfBounding;
	
	/**
	 * The dimensions of the object.
	 */
	private Vector3 dimensions; 
	
	/**
	 * Radius of the object.
	 */
	private float radius;
	
	/**
	 * A new instance of a VoxelEntity.
	 * @param name entity's name.
	 */
	public VoxelEntity(Model model, String name) {
		super(model, name);
		this.name = name;
		bounding = new BoundingBox();
		centerOfBounding = new Vector3();
		dimensions = new Vector3();
		radius = 0.0f;
		calculateBoundingBox(bounding);
		bounding.getCenter(centerOfBounding);
		bounding.getDimensions(dimensions);
		radius = dimensions.len() / 2f;
	}
	
	/**
	 * Decrease the VoxelEntity's health by variable amount.
	 * @param amount the amount.
	 */
	public final void hurt(float amount) {
		this.health -= amount;
	}
	
	/**
	 * Set the unique identifier of the VoxelEntity. Should only be called by the VoxelEntityManager.
	 * @param uid
	 */
	public final void setUID(long uid) {
		this.uid = uid;
	}
	
	/**
	 * Set the position of the VoxelEntity in its VoxelSpace.
	 * @param position the new position of the entity.
	 */
	public final void setPosition(Vector3 position) {
		transform.setToTranslation(position);
		validateRigidbodyTransform();
	}
	
	/**
	 * Set the rotation of the VoxelEntity in its VoxelSpace.
	 * @param axis the axis of rotation.
	 * @param degrees the value or rotation, in degrees.
	 */
	public final void setRotation(Vector3 axis, float degrees) {
		transform.setToRotation(axis, degrees);
		validateRigidbodyTransform();
	}
	
	/**
	 * Called when the transform of an object has been updated. If the VoxelEntity is a VoxelRigidbody, the 
	 * rigidbody is set to reflect the transformation that has just occured.
	 */
	private void validateRigidbodyTransform() {
		if(this instanceof VoxelRigidbody)
			((VoxelRigidbody)this).getBody().proceedToTransform(transform);
	}
	
	/**
	 * @return the unique identification number of entity. If -1, the entity is not active in the VoxelSpace and should be disposed of.
	 */
	public final long getUID() {
		return uid;
	}
	
	/**
	 * @return the name of the entity. Might not be unique.
	 */
	public final String getName() {
		return name;
	}
	
	/**
	 * @return health value of entity. If <= 0, the entity is not longer allowed to be active in the VoxelSpace.
	 */
	public final double getHealth() {
		return health;
	}
	
	/**
	 * @return the current position of the VoxelEntity in the VoxelSpace.
	 */
	public final Vector3 getPosition() {
		Vector3 pos = new Vector3();
		transform.getTranslation(pos);
		return pos;
	}
	
	/**
	 * @return the current rotation of the VoxelEntity in the VoxelSpace.
	 */
	public final Quaternion getRotation() {
		Quaternion rot = new Quaternion();
		transform.getRotation(rot);
		return rot;
	}
	
	/**
	 * @return the center of the object's bounding box.
	 */
	public final Vector3 getCenterOfBounding() {
		return centerOfBounding;
	}
	
	/**
	 * @return physical dimensions of the object.
	 */
	public final Vector3 getDimensions() {
		return dimensions;
	}
	
	/**
	 * @return radius of the object (from center).
	 */
	public final float getRadius() {
		return radius;
	}

	@Override
	public void dispose() {}
	
}
