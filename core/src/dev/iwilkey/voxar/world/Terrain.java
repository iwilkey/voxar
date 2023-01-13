package dev.iwilkey.voxar.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder.VertexInfo;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.Disposable;

import dev.iwilkey.voxar.gfx.Renderable;
import dev.iwilkey.voxar.noise.NoiseGenerator;

public final class Terrain implements Renderable, Disposable {
	
	private ModelInstance instance;
	private Model model;
	private Mesh mesh;
	
	private final NoiseGenerator noise;
	
	// Terrain settings
	private static final long TERRAIN_CHUNK_APOTHEM = 10; // Keep in mind, chunk area = (TERRAIN_CHUNK_APOTHEM * 2)^2!
	private static final long TERRAIN_UNIT_SUBDIVISION = 10;
	
	public Terrain(int seed) {
		noise = new NoiseGenerator(seed);
		generateChunkCenteredAtSubdivision(0L, 0L); // Use Perlin noise to generate terrain mesh from [x +- TERRAIN_CHUNK_SIZE, z +- TERRAIN_CHUNK_SIZE].
	}
	
	/**
	 * Procedurally generates a new terrain chunk mesh based on center point.
	 */
	private void generateChunkCenteredAtSubdivision(long centerX, long centerZ) {
		centerX *= TERRAIN_UNIT_SUBDIVISION;
		centerZ *= TERRAIN_UNIT_SUBDIVISION;
		ModelBuilder modelBuilder = new ModelBuilder();
		modelBuilder.begin();
		MeshPartBuilder meshBuilder;
		final long dim = (TERRAIN_CHUNK_APOTHEM * TERRAIN_UNIT_SUBDIVISION);
		for(long x = centerX - dim; x < centerX + dim; x += TERRAIN_UNIT_SUBDIVISION) {
			for(long z = centerZ - dim; z < centerZ + dim; z += TERRAIN_UNIT_SUBDIVISION) {
				
				@SuppressWarnings("unused")
				float y = (float)(noise.noise(x, z));
				
				String name = "loc_" + Long.toString(x) + "_" + Long.toString(z);
				float posX = x + (TERRAIN_UNIT_SUBDIVISION / 2f);
				float negX = x - (TERRAIN_UNIT_SUBDIVISION / 2f);
				float posZ = z + (TERRAIN_UNIT_SUBDIVISION / 2f);
				float negZ = z - (TERRAIN_UNIT_SUBDIVISION / 2f);
				posX += (TERRAIN_UNIT_SUBDIVISION / 2f);
				negX += (TERRAIN_UNIT_SUBDIVISION / 2f);
				posZ += (TERRAIN_UNIT_SUBDIVISION / 2f);
				negZ += (TERRAIN_UNIT_SUBDIVISION / 2f);
				meshBuilder = modelBuilder.part(name, GL20.GL_LINES, Usage.Position | Usage.Normal, new Material(ColorAttribute.createDiffuse(Color.WHITE)));
				VertexInfo i0 = new VertexInfo().setPos(negX, 0, negZ).setNor(0, 1, 0).setCol(Color.WHITE);
				VertexInfo i1 = new VertexInfo().setPos(negX, 0, posZ).setNor(0, 1, 0).setCol(Color.WHITE);
				VertexInfo i2 = new VertexInfo().setPos(posX, 0, posZ).setNor(0, 1, 0).setCol(Color.WHITE);
				VertexInfo i3 = new VertexInfo().setPos(posX, 0, negZ).setNor(0, 1, 0).setCol(Color.WHITE);
				meshBuilder.triangle(i0, i1, i2);
				meshBuilder.triangle(i0, i2, i3);
			}
		}
		model = modelBuilder.end();
		instance = new ModelInstance(model);
	}
	
	/**
	 * @return the model representing the current terrain.
	 */
	public Mesh getCurrentMesh() {
		return mesh;
	}
	
	@Override
	public ModelInstance getRenderableProvider() {
		return instance;
	}

	@Override
	public void dispose() {
		model.dispose();
	}

}
