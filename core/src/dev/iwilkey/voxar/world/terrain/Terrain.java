package dev.iwilkey.voxar.world.terrain;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import dev.iwilkey.voxar.gfx.Renderable;
import dev.iwilkey.voxar.noise.Perlin;

/**
 * A technically infinite expanse of procedurally generated TerrainChunks.
 * @author iwilkey
 */
public final class Terrain implements Renderable, Disposable {
	
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
	private final Perlin noise;
	
	/**
	 * Create a new Terrain.
	 * @param seed a value that all procedural generation will be based on.
	 * @param chunkScale 
	 */
	public Terrain(double seed, long globalChunkScale, long globalHeightMapAmplifier, int globalChunkApothem) {
		this.globalChunkScale = globalChunkScale;
		this.globalHeightMapAmplifier = globalHeightMapAmplifier;
		this.globalChunkApothem = globalChunkApothem;
		noise = new Perlin(seed, globalChunkScale + 1.0f);
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
	public Perlin getGenerationNoise() {
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
