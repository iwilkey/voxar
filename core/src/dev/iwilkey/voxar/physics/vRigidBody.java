package dev.iwilkey.voxar.physics;

import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;

/**
 * An abstraction of btRigidBody simply adding a VoxarPhysicsTag for aid in group collision processing.
 * @author iwilkey
 */
public final class vRigidBody extends btRigidBody {
	
	/**
	 * The tag of this tfRigidBody.
	 */
	private VoxarPhysicsTag tag = VoxarPhysicsTag.ALL;

	public vRigidBody(btRigidBodyConstructionInfo constructionInfo) {
		super(constructionInfo);
	}
	
	/**
	 * Set the tag of this tfRigidBody.
	 * @param tag the physics tag.
	 */
	public void setTag(VoxarPhysicsTag tag) {
		this.tag = tag;
	}
	
	/**
	 * @return the physics tag of this tfRigidBody.
	 */
	public VoxarPhysicsTag getTag() {
		return tag;
	}
	
}
