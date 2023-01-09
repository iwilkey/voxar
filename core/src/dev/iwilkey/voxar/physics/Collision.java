package dev.iwilkey.voxar.physics;

import com.badlogic.gdx.physics.bullet.collision.ContactListener;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;

/**
 * An abstraction of ContactListener, an interface that allows the PhysicsEngine to notify
 * when a contact/collision is detected between two VoxelRigidbodies or groups of alike classification.
 * @author iwilkey
 */
public final class Collision extends ContactListener  {
	
	/**
	 * The process to undergo when two VoxelRigidbodies, or groups of alike classification, collide.
	 */
	private CollisionProcess process;
	
	public Collision() {
		setCollisionProcess(null);
	}
	
	/**
	 * Set the process to undergo when two VoxelRigidbodies collide.
	 * @param process the process.
	 */
	public void setCollisionProcess(CollisionProcess process) {
		this.process = process;
	}
	
	@Override
	public void onContactStarted(btCollisionObject obj1, btCollisionObject obj2) {
		if(process == null) return;
		process.onInitialContact((vRigidBody)obj2, (vRigidBody)obj1);
	}
	@Override
	public void onContactProcessed(btCollisionObject obj1, btCollisionObject obj2) {
		if(process == null) return;
		process.duringContact((vRigidBody)obj1, (vRigidBody)obj2);
	}

}
