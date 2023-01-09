package dev.iwilkey.voxar.physics;

/**
 * A physics body type describes how a VoxelRigidbody will be treated
 * by the PhysicsEngine.
 * @author iwilkey
 */
public enum PhysicsBodyType {
	/**
	 * A static body does not collide with other static or kinematic bodies
	 * and stays in one place.
	 */
	STATIC,
	
	/**
	 * A kinematic body does not collide with other static or kinematic bodies but
	 * can translate programmatically.
	 */
	KINEMATIC,
	
	/**
	 * A dynamic body reacts to forces, impulses, collisions, and any other world event.
	 */
	DYNAMIC
}
