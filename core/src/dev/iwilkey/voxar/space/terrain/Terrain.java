package dev.iwilkey.voxar.space.terrain;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import dev.iwilkey.voxar.entity.VoxelRigidbody;
import dev.iwilkey.voxar.gfx.Renderable;
import dev.iwilkey.voxar.noise.Perlin2D;
import dev.iwilkey.voxar.physics.PhysicsBodyType;
import dev.iwilkey.voxar.physics.PhysicsPrimitive;
import dev.iwilkey.voxar.space.VoxelSpace;

/**
 * A technically infinite expanse of procedurally generated TerrainChunks.
 * @author iwilkey
 */
public final class Terrain implements Renderable, Disposable {
	
	private final VoxelSpace operatingSpace;
	
	/**
	 * Terrain detail option. The world distance of each line connecting any two vertices. 
	 * Mathematically, (chunkScale * (chunkApothem * 2)) * 4 = perimeter of terrain chunk (in world units).
	 */
	private final long globalChunkScale;
	
	/**
	 * Chunk detail option. The perpendicular distance of the center of the chunk to any side. Keep in mind, chunk area = (apothem * 2)^2! This shouldn't be set too high
	 * for performance reasons.
	*/
	private final int globalChunkApothem;
	
	/**
	 * Terrain detail option. The scale of the generated height map.
	 */
	private final long globalHeightMapAmplifier;
	
	/**
	 * The chunks currently active.
	 */
	private Array<TerrainChunk> activeChunks;
	
	/**
	 * The active RenderableProviders of this Terrain.
	 */
	private Array<ModelInstance> renderables;

	/**
	 * Mathematical utility tool used for procedural terrain generation.
	 */
	private final Perlin2D noise;
	
	/**
	 * Create a new Terrain.
	 * @param seed a value that all procedural generation will be based on.
	 * @param chunkScale the world distance of each line connecting any two vertices. 
	 */
	public Terrain(VoxelSpace operatingSpace, double seed, long globalChunkScale, long globalHeightMapAmplifier, int globalChunkApothem) {
		
		this.operatingSpace = operatingSpace;
		this.globalChunkScale = globalChunkScale;
		this.globalHeightMapAmplifier = globalHeightMapAmplifier;
		this.globalChunkApothem = globalChunkApothem;
		noise = new Perlin2D(seed, globalChunkScale * 50);
		
		activeChunks = new Array<>();
		renderables = new Array<>();
		
		// Create a terrain of size (size by size).
		final int size = 1;
		for(int x = -size; x <= size; x++) {
			for(int z = -size; z <= size; z++) {
				addChunk(x, z);
			}
		}
		
	}
	
	public void addChunk(long chunkX, long chunkZ) {
		TerrainChunk chunk = new TerrainChunk(this, chunkX, chunkZ); // hardcoded chunk apothem for now.
		activeChunks.add(chunk);
		renderables.add(chunk.getRenderableProvider());
		VoxelRigidbody chunkPhysics = new VoxelRigidbody(chunk.getChunkModel(), "chunk", 0.0f, PhysicsPrimitive.MESH, PhysicsBodyType.STATIC);
		chunkPhysics.setUID(0);
		chunkPhysics.getBody().proceedToTransform(chunkPhysics.transform);
		chunkPhysics.getBody().setUserValue(0);
		operatingSpace.getOperatingState().getVoxelSpace().getPhysicsEngine().getDynamicsWorld().addRigidBody(chunkPhysics.getBody());
	}
	
	/**
	 * @return the world distance of each line connecting any two vertices. 
	 */
	public long getGlobalChunkScale() {
		return globalChunkScale;
	}
	
	/**
	 * @return the scale of the generated height map.
	 */
	public long getGlobalHeightMapAmplifier() {
		return globalHeightMapAmplifier;
	}
	
	/**
	 * @return The perpendicular distance of the center of the chunk to any side.
	 */
	public int getGlobalChunkApothem() {
		return globalChunkApothem;
	}
	
	/**
	 * @return the mathematical utility tool used for procedural terrain generation.
	 */
	public Perlin2D getGenerationNoise() {
		return noise;
	}
	
	@Override
	public Array<ModelInstance> getRenderableProviders() {
		return renderables;
	}

	@Override
	public void dispose() {
		for(TerrainChunk c : activeChunks)
			c.dispose();
	}
	
}
