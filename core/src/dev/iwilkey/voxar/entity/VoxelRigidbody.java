package dev.iwilkey.voxar.entity;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.Collision;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCapsuleShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btConeShape;
import com.badlogic.gdx.physics.bullet.collision.btCylinderShape;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;

import dev.iwilkey.voxar.model.VoxelModelLoader;
import dev.iwilkey.voxar.physics.PhysicsBodyType;
import dev.iwilkey.voxar.physics.PhysicsEngine;
import dev.iwilkey.voxar.physics.PhysicsPrimitive;
import dev.iwilkey.voxar.physics.UniqueMotion;
import dev.iwilkey.voxar.physics.UniqueTransformProcess;
import dev.iwilkey.voxar.physics.VoxarPhysicsTag;
import dev.iwilkey.voxar.physics.vRigidBody;

/**
 * A VoxelEntity abstraction with properties that allow for a VoxelSpace's PhysicsEngine to simulate it's transform.
 * @author iwilkey
 */
public class VoxelRigidbody extends VoxelEntity {
	
	/**
	 * Information that tells the Bullet physics back-end how to calculate motion and collisions for it's shape and mass.
	 */
	private final btRigidBody.btRigidBodyConstructionInfo constructionInfo;
	
	/**
	 * An object that holds important data for the Bullet physics back-end to simulate it's motion and collision.
	 */
	private final vRigidBody body;
	
	/**
	 * Data regarding the motion of the VoxelRigidbody.
	 */
	private final UniqueMotion uniqueMotion;
	
	/**
	 * Mass of the object.
	 */
	private final float mass;
	
	/**
	 * The body type of the physics object. See documentation of "dev.iwilkey.Voxar.physics.PhysicsBodyType".
	 */
	private final PhysicsBodyType bodyType;
	
	/**
	 * The local inertia of the rigidbody.
	 */
	private Vector3 localInertia = new Vector3();

	public VoxelRigidbody(Model model, String name, float mass, PhysicsPrimitive primitive, PhysicsBodyType bodyType, VoxarPhysicsTag tag) {
		super(model, name);
		this.mass = mass;
		
		// Fit the referenced Model into the btCollisionShape based on the PhysicsPrimitive selected.
		Vector3 bounds = VoxelModelLoader.getModelDimensions(model);
		btCollisionShape shape;
		switch (primitive) {
			case CUBOID:
				shape = new btBoxShape(new Vector3(bounds.x / 2f, bounds.y / 2f, bounds.z / 2f));
				break;
			case SPHERE:
				shape = new btSphereShape(bounds.x / 2f);
				break;
			case CONE:
				shape = new btConeShape(bounds.y, bounds.x / 2f);
				break;
			case CAPSULE:
				shape = new btCapsuleShape(bounds.x / 2f, bounds.y / 2f);
				break;
			case MESH:
				shape = Bullet.obtainStaticNodeShape(model.nodes);
				break;
			default:;
				shape = new btCylinderShape(new Vector3(bounds.x / 2f, bounds.y / 2f, bounds.z / 2f));
				break;
		}
		
		// Calculate local inertia based on mass.
		if(mass > 0f)
			shape.calculateLocalInertia(mass, localInertia);
		else
			localInertia.set(0, 0, 0);
		
		// Construct the btRigidBody with the construction info.
		constructionInfo = new btRigidBody.btRigidBodyConstructionInfo(mass, null, shape, localInertia);
		body = new vRigidBody(constructionInfo);
		constructionInfo.dispose();
		
		body.setTag(tag);
		
		// Create a new UniqueMotion object and assign it to the VoxelRigidbody.
		uniqueMotion = new UniqueMotion(getUID());
		uniqueMotion.setTransform(transform);
		body.setMotionState(uniqueMotion);
		
		// Set collision flags based on the PhysicsBodyType selected.
		this.bodyType = bodyType;
		switch(this.bodyType) {
			case STATIC:
				body.setMassProps(0, new Vector3(0, 0, 0));
				body.setCollisionFlags(body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_STATIC_OBJECT);
				body.setContactCallbackFlag(PhysicsEngine.STATIC_FLAG);
				break;
			case KINEMATIC:
				body.setCollisionFlags(body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_KINEMATIC_OBJECT);
				body.setActivationState(Collision.DISABLE_DEACTIVATION);
				body.setContactCallbackFlag(PhysicsEngine.KINEMATIC_FLAG);
				break;
			case DYNAMIC:
				body.setCollisionFlags(body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
				body.setContactCallbackFlag(PhysicsEngine.DYNAMIC_FLAG);
				body.setContactCallbackFilter(PhysicsEngine.STATIC_FLAG | PhysicsEngine.KINEMATIC_FLAG);
				break;
		}
	}
	
	/**
	 * Set a new UniqueTransformProcess. See documentation of "dev.iwilkey.Voxar.physics.UniqueTransformProcess".
	 * @param process the unique transform process. 
	 */
	public void setUniqueTransformProcess(UniqueTransformProcess process) {
		uniqueMotion.setUniqueTransformProcess(process);
	}
	
	/**
	 * @return the btRigidBody object active in the VoxelSpace.
	 */
	public btRigidBody getBody() {
		return body;
	}
	
	/**
	 * @return the mass of the object.
	 */
	public float getMass() {
		return mass;
	}
	
	/**
	 * @return the body type of the object.
	 */
	public PhysicsBodyType getBodyType() {
		return bodyType;
	}
	
	@Override
	public void dispose() {
		body.dispose();
		uniqueMotion.dispose();
	}

}
