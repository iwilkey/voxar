package dev.iwilkey.voxar.entity;

import java.util.Random;

import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import dev.iwilkey.voxar.asset.exception.VoxarAssetNotReferenced;
import dev.iwilkey.voxar.clock.Tickable;
import dev.iwilkey.voxar.model.VoxelModel;
import dev.iwilkey.voxar.physics.PhysicsBodyType;
import dev.iwilkey.voxar.physics.PhysicsPrimitive;
import dev.iwilkey.voxar.world.VoxelSpace;

/**
 * A required core class for a VoxelSpace if the use of any VoxelEntity is desired. 
 * Keeps track of active entities and provides their respective Renderables. Disposes of entities that no longer are allowed to be
 * active within a VoxelSpace.
 * @author iwilkey
 */
public final class VoxelEntityManager implements Disposable, Tickable {
	
	private static final Random random = new Random();
	
	/**
	 * The VoxelSpace the entity manager is operating for.
	 */
	private VoxelSpace operatingSpace;
	
	/**
	 * All active entities within the VoxelSpace.
	 */
	private Array<VoxelEntity> activeEntities;
	
	/**
	 * All entities that are no longer allowed to be active within the VoxelSpace.
	 */
	private Array<VoxelEntity> deathrow;
	
	public VoxelEntityManager(VoxelSpace operatingSpace) {
		this.operatingSpace = operatingSpace;
		activeEntities = new Array<>();
		deathrow = new Array<>();
	}
	
	/**
	 * @return a random, guaranteed unique identifier for a new VoxelEntity.
	 */
	private long findUID() {
		long UID = -1;
		restart: while(true) {
			long tryUID = (long)(random.nextDouble() * Integer.MAX_VALUE);
			for(VoxelEntity e : activeEntities)
				if(e.getUID() == tryUID)
					continue restart;
			UID = tryUID;
			break;
		}
		return UID;
	}
	
	@Override
	public void tick() {
		for(VoxelEntity e : activeEntities) {
			if(e instanceof Viable) {
				if(e.getHealth() <= 0.0f)
					((Viable)e).death();
				else ((Viable)e).life();
			}
			if(e.getHealth() <= 0.0f)
				deathrow.add(e);
		}
		if(deathrow.size != 0) 
			garbageControl();
	}
	
	/**
	 * Manages dead entities by removing them from the application context.
	 */
	private void garbageControl() {
		for(VoxelEntity e : deathrow) {
			if(e instanceof VoxelRigidbody) {
				btRigidBody body = ((VoxelRigidbody)e).getBody();
				operatingSpace.getOperatingState().getVoxelSpace().getPhysicsEngine().getDynamicsWorld().removeRigidBody(body);
			}
			e.dispose();
			activeEntities.removeValue(e, false);
		}
		deathrow.clear();
	}
	
	/**
	 * Add a new entity to the VoxelWorld.
	 * @param asset the referenced asset.
	 * @return the unique identifier for the asset, now active in the VoxelSpace.
	 */
	public long addEntity(String pathToModel) {
		VoxelModel model = null;
		try {
			model = operatingSpace.getOperatingState().getLoadedVoxelModel(pathToModel);
		} catch (VoxarAssetNotReferenced e1) {
			e1.printStackTrace();
			System.exit(-1);
		}
		VoxelEntity e = new VoxelEntity(model.getModel(), model.getName());
		e.setUID(findUID());
		activeEntities.add(e);
		if(e instanceof Viable)
			((Viable)e).spawn();
		return e.getUID();
	}
	
	/**
	 * Add a new physics rigidbody to the VoxelWorld.
	 * @param asset the referenced asset.
	 * @param mass mass of the object, in kg.
	 * @param shape PhysicsPrimitive shape of the object.
	 * @param bodyType the physics body type of the object.
	 * @return the unique identifier for the asset, now active in the VoxelSpace.
	 */
	public long addRigidbody(String pathToModel, float mass, PhysicsPrimitive primitive, PhysicsBodyType bodyType) {
		VoxelModel model = null;
		try {
			model = operatingSpace.getOperatingState().getLoadedVoxelModel(pathToModel);
		} catch (VoxarAssetNotReferenced e1) {
			e1.printStackTrace();
			System.exit(-1);
		}
		VoxelRigidbody e = new VoxelRigidbody(model.getModel(), model.getName(), mass, primitive, bodyType);
		e.setUID(findUID());
		activeEntities.add(e);
		if(e instanceof Viable)
			((Viable)e).spawn();
		e.getBody().proceedToTransform(e.transform);
		e.getBody().setUserValue((int)e.getUID());
		operatingSpace.getOperatingState().getVoxelSpace().getPhysicsEngine().getDynamicsWorld().addRigidBody(e.getBody());
		return e.getUID();
	}
	
	/**
	 * @param uid the unique identification number of the entity desired.
	 * @return the VoxelEntity object, null if non-existent.
	 */
	public VoxelEntity getEntity(long uid) {
		for(VoxelEntity e : activeEntities)
			if(e.getUID() == uid) 
				return e;
		return null;
	}
	
	/**
	 * @param uid the unique identification number of the entity desired.
	 * @return true if the entity exists in the VoxelWorld.
	 */
	public boolean verifyExistance(long uid) {
		for(VoxelEntity e : activeEntities)
			if(e.getUID() == uid)
				return true;
		return false;
	}
	
	/**
	 * @return the entire list of active entities active within a VoxelSpace.
	 */
	public Array<VoxelEntity> getRenderables() {
		return activeEntities;
	}

	@Override
	public void dispose() {
		
	}

}
