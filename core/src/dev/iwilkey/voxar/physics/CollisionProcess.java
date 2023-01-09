package dev.iwilkey.voxar.physics;

/**
 * The process to undergo when two VoxelRigidbodies, or groups of alike classification, collide.
 * @author iwilkey
 */
public interface CollisionProcess {
	/**
	 * The process to undergo when two VoxelRigidbodies, or groups of alike classification, collide for the
	 * first time after being spatially separated.
	 * @param obj1
	 * @param obj2
	 */
	public void onInitialContact(vRigidBody obj1, vRigidBody obj2);
	
	/**
	 * The process to undergo as two VoxelRigidbodies, or groups of alike classification, are not 
	 * spatially separated.
	 * @param obj1
	 * @param obj2
	 */
	public void duringContact(vRigidBody obj1, vRigidBody obj2);
}
