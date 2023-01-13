package dev.iwilkey.voxar.entity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
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
	private HashMap<Long, VoxelEntity> activeEntities;
	
	/**
	 * Entity Renderables.
	 */
	private Array<ModelInstance> renderables; 
	
	/**
	 * Iterator for entities...
	 */
	private Iterator<Entry<Long, VoxelEntity>> entityIterator;
	
	/**
	 * All entities that are no longer allowed to be active within the VoxelSpace.
	 */
	private Array<VoxelEntity> trash;
	
	public VoxelEntityManager(VoxelSpace operatingSpace) {
		this.operatingSpace = operatingSpace;
		activeEntities = new HashMap<>();
		entityIterator = activeEntities.entrySet().iterator();
		renderables = new Array<>();
		trash = new Array<>();
	}
	
	/**
	 * @return a random, guaranteed unique identifier for a new VoxelEntity.
	 */
	private long findUID() {
		long UID = -1;
		find: while(true) {
			long tryUID = (long)(random.nextDouble() * Integer.MAX_VALUE);
			if(activeEntities.containsKey(tryUID))
				continue find;
			UID = tryUID;
			break;
		}
		return UID;
	}
	
	@Override
	public void tick() {
		while(entityIterator.hasNext()) {
			VoxelEntity e = (VoxelEntity)entityIterator.next();
			if(e instanceof Viable) {
				if(e.getHealth() <= 0.0f)
					((Viable)e).death();
				else ((Viable)e).life();
			}
			if(e.getHealth() <= 0.0f)
				trash.add(e);
		}
		if(trash.size != 0)
			incinerate();
	}
	
	/**
	 * Manages trashed entities by removing them from the application context.
	 */
	private void incinerate() {
		for(VoxelEntity e : trash) {
			if(e instanceof VoxelRigidbody) {
				btRigidBody body = ((VoxelRigidbody)e).getBody();
				operatingSpace.getOperatingState().getVoxelSpace().getPhysicsEngine().getDynamicsWorld().removeRigidBody(body);
			}
			e.dispose();
			activeEntities.remove(e.getUID());
			renderables.removeValue(e, false);
		}
		trash.clear();
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
		activeEntities.put(e.getUID(), e);
		renderables.add(e);
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
		activeEntities.put(e.getUID(), e);
		renderables.add(e);
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
		return activeEntities.get(uid);
	}
	
	/**
	 * @param uid the unique identification number of the entity desired.
	 * @return true if the entity exists in the VoxelWorld.
	 */
	public boolean verifyExistance(long uid) {
		return activeEntities.containsKey(uid);
	}
	
	/**
	 * @return renderables of entities active within a VoxelSpace.
	 */
	public Array<ModelInstance> getRenderables() {
		return renderables;
	}

	@Override
	public void dispose() {
		
	}

}
