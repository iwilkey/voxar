package dev.iwilkey.voxar.physics;

/**
 * A simple three-dimensional shape used for optimizing the PhysicsEngine's simulation process.
 * voxel models will need to be fit to one of these primitive geometric shapes.
 * @author iwilkey
 */
public enum PhysicsPrimitive {
	/**
	 * A cube or rectangular prism.
	 */
	CUBOID,
	
	/**
	 * A simple sphere.
	 */
	SPHERE,
	
	/**
	 * A simple cone.
	 */
	CONE,
	
	/**
	 * A cylinder with half spheres encapsulating the top and bottom respectively.
	 */
	CAPSULE,
	
	/**
	 * A simple cylinder.
	 */
	CYLINDER
}
