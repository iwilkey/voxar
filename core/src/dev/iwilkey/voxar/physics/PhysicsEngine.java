package dev.iwilkey.voxar.physics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.DebugDrawer;
import com.badlogic.gdx.physics.bullet.collision.btBroadphaseInterface;
import com.badlogic.gdx.physics.bullet.collision.btCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btDbvtBroadphase;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btDispatcher;
import com.badlogic.gdx.physics.bullet.dynamics.btConstraintSolver;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;
import com.badlogic.gdx.utils.Disposable;

import dev.iwilkey.voxar.clock.Tickable;

/**
 * Implementation of a Bullet physics back-end to simulate real-time translations, collisions,
 * impulses, and forces of all dynamic VoxelRigidbodies active in a VoxelSpace.
 * @author iwilkey
 */
public final class PhysicsEngine implements Disposable, Tickable {
	
	/**
	 * btRigidBody collision flag for kinematic VoxelRigidbodies.
	 */
	public final static byte KINEMATIC_FLAG = (1 << 1);
	
	/**
	 * btRigidBody collision flag for static VoxelRigidbodies.
	 */
	public final static byte STATIC_FLAG = (1 << 2);
	
	/**
	 * btRigidBody collision flag for dynamic VoxelRigidbodies.
	 */
	public final static byte DYNAMIC_FLAG = (1 << 3);
	
	/**
	 * The number of substeps for each simulation step, used by Bullet internally. 
	 * Increasing this number will increase the resolution of the simulation, albeit
	 * more computationally demanding.
	 */
	private final static short MAX_SUB_STEPS = 0x05;
	
	/**
	 * A fall-back amount to default the physics simulation delta-time
	 * if the frame rate of the host machine is too low.
	 */
	private final static float FIXED_TIME_STEP = 1f / 60f;
	
	/**
	 * Allows to configure Bullet collision detection stack allocator size, 
	 * default collision algorithms and persistent manifold pool size.
	 */
	private final btCollisionConfiguration collisionConfig;
	
	/**
	 * Interface class can be used in combination with broadphase 
	 * to dispatch calculations for overlapping pairs.
	 */
	private final btDispatcher dispatcher;
	
	/**
	 * Provides an interface to detect AABB-overlapping object pairs.
	 */
	private final btBroadphaseInterface broadphase;
	
	/**
	 * Interface class for several dynamics implementation, basic, discrete, 
	 * parallel, and continuous etc.
	 */
	private final btDynamicsWorld dynamicsWorld;
	
	/**
	 * Provides solver interface.
	 */
	private final btConstraintSolver constraintSolver;
	
	/**
	 * Detects collisions between two VoxelRigidbodies or groups of alike classification.
	 */
	private final Collision collision;
	
	/**
	 * Special gfx batch used to render debug information about the state of btRigidBodies 
	 * active in a VoxelSpace.
	 */
	private final DebugDrawer debugRenderer;
	
	/**
	 * Used to tell the Renderer if the PhysicsEngine debugRenderer should be drawn.
	 */
	private boolean debugMode = false;
	
	public PhysicsEngine() {
		// Initialize Bullet back-end.
		Bullet.init();
		
		// Create Bullet objects.
		collisionConfig = new btDefaultCollisionConfiguration();
		dispatcher = new btCollisionDispatcher(collisionConfig);
		broadphase = new btDbvtBroadphase();
		constraintSolver = new btSequentialImpulseConstraintSolver();
		dynamicsWorld = new btDiscreteDynamicsWorld(dispatcher, 
				broadphase, constraintSolver, collisionConfig);
		
		// Initialize Collision handler.
		collision = new Collision();
		
		// Create gfx renderer.
		debugRenderer = new DebugDrawer();
		debugRenderer.setDebugMode(btIDebugDraw.DebugDrawModes.DBG_MAX_DEBUG_DRAW_MODE);
		dynamicsWorld.setDebugDrawer(debugRenderer);
		
		setWorldGravity(new Vector3(0, -9.8f, 0));
	}
	
	@Override
	public void tick() {
		// Simulate real-time translations, collisions, impulses, and forces 
		// of all dynamic VoxelRigidbodies active in a VoxelSpace.
		dynamicsWorld.stepSimulation((float)Gdx.graphics.getDeltaTime(), 
				MAX_SUB_STEPS, FIXED_TIME_STEP);
	}
	
	/**
	 * Set whether or not the Renderer should render the PhysicsEngine's debugRenderer.
	 * @param verdict true or false.
	 */
	public void setDebugMode(boolean verdict) {
		this.debugMode = verdict;
	}
	
	/**
	 * Set the process to undergo when two VoxelRigidbodies, or groups of alike classification, collide.
	 * @param process the process.
	 */
	public void setCollisionProcess(CollisionProcess process) {
		collision.setCollisionProcess(process);
	}
	
	/**
	 * @return whether or not the Renderer should render the PhysicsEngine's debugRenderer.
	 */
	public boolean isDebugMode() {
		return debugMode;
	}
	
	/**
	 * Set the VoxelSpace's constant gravitational force vector.
	 * @param grav the gravitational force vector.
	 */
	public void setWorldGravity(Vector3 grav) {
		dynamicsWorld.setGravity(grav);
	}
	
	/**
	 * @return the PhysicsEngine's special gfx debug renderer batch.
	 */
	public DebugDrawer getDebugRenderer() {
		return debugRenderer;
	}
	
	/**
	 * @return the interface to the operating DynamicsWorld, and interface class for several dynamics 
	 * implementation, basic, discrete, parallel, and continuous etc.
	 */
	public btDynamicsWorld getDynamicsWorld() {
		return dynamicsWorld;
	}

	@Override
	public void dispose() {
		dispatcher.dispose();
		constraintSolver.dispose();
		broadphase.dispose();
		collisionConfig.dispose();
	}

}
