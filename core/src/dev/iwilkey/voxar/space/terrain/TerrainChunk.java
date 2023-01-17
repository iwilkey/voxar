package dev.iwilkey.voxar.space.terrain;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.Disposable;

import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder.VertexInfo;

/**
 * A manageable subset, or chunk, of a Terrain object.
 * @author iwilkey
 */
public final class TerrainChunk implements Disposable {
	
	private static final int DEEP_COLOR = 0xbbbbbbff;
	private static final int HIGH_COLOR = 0xffffffff;
	@SuppressWarnings("unused")
	private static final int MESH_OUTLINE_COLOR = 0xffffffff;
	
	/**
	 * Parent terrain.
	 */
	private Terrain terrain;
	
	/**
	 * X location of the chunk, in terrain space.
	 */
	private long chunkX;
	
	/**
	 * Z location of the chunk, in terrain space.
	 */
	private long chunkZ;
	
	// Renderable model.
	private Model model;
	
	public TerrainChunk(Terrain terrain, long chunkX, long chunkZ) {
		this.terrain = terrain;
		final int gca = terrain.getGlobalChunkApothem();
		final long gcs = terrain.getGlobalChunkScale();
		final long sf = 2 * gca * gcs;
		this.chunkX = chunkX * sf;
		this.chunkZ = chunkZ * sf;
		generate();
	}
	
	/**
	 * Procedurally generates a new terrain chunk mesh based on chunk position.
	 */
	private void generate() {
		ModelBuilder modelBuilder = new ModelBuilder();
		modelBuilder.begin();
		modelBuilder.node().id = "chunk";
		MeshPartBuilder meshBuilder;
		final long unit = terrain.getGlobalChunkScale();
		final long dim = (terrain.getGlobalChunkApothem() * unit);
		
		for(long x = chunkX - dim; x < chunkX + dim; x += unit) {
			for(long z = chunkZ - dim; z < chunkZ + dim; z += unit) {
				
				String meshName = "terrainchunk_" + Long.toString(chunkX) + "_" + Long.toString(chunkZ) + "_loc_" + Long.toString(x) + "_" + Long.toString(z);
				
				float xx = x + unit;
				float zz = z + unit;
				
				// Generate vertices for loc.
				float hnlcv = Math.abs((float)terrain.getGenerationNoise().getNoiseAt(x, z)) * terrain.getGlobalHeightMapAmplifier();
				float hflcv = Math.abs((float)terrain.getGenerationNoise().getNoiseAt(x, zz)) * terrain.getGlobalHeightMapAmplifier();
				float hfrcv = Math.abs((float)terrain.getGenerationNoise().getNoiseAt(xx, zz)) * terrain.getGlobalHeightMapAmplifier();
				float hnrcv = Math.abs((float)terrain.getGenerationNoise().getNoiseAt(xx, z)) * terrain.getGlobalHeightMapAmplifier();
				
				VertexInfo nlcv = new VertexInfo().setPos(x, hnlcv, z).setNor(0, 1, 0)
						.setCol(new Color(interpolateColors(DEEP_COLOR, HIGH_COLOR, hnlcv / terrain.getGlobalHeightMapAmplifier())));
				VertexInfo flcv = new VertexInfo().setPos(x, hflcv, zz).setNor(0, 1, 0)
						.setCol(new Color(interpolateColors(DEEP_COLOR, HIGH_COLOR, hflcv / terrain.getGlobalHeightMapAmplifier())));
				VertexInfo frcv = new VertexInfo().setPos(xx, hfrcv, zz).setNor(0, 1, 0)
						.setCol(new Color(interpolateColors(DEEP_COLOR, HIGH_COLOR, hfrcv / terrain.getGlobalHeightMapAmplifier())));
				VertexInfo nrcv = new VertexInfo().setPos(xx, hnrcv, z).setNor(0, 1, 0)
						.setCol(new Color(interpolateColors(DEEP_COLOR, HIGH_COLOR, hnrcv / terrain.getGlobalHeightMapAmplifier())));
				
				// Triangle render (colored, filled in).
				meshBuilder = modelBuilder.part(meshName, GL20.GL_TRIANGLES, Usage.Position | Usage.Normal | Usage.ColorUnpacked, new Material(ColorAttribute.createDiffuse(Color.WHITE)));
				meshBuilder.rect(nlcv, flcv, frcv, nrcv);
				
			   /*
				* Render mesh outline (can be taxing for renderer!)...
				*/
				/*
				final float outlineYDiff = 0.15f;
				nlcv.position.add(0, outlineYDiff, 0);
				flcv.position.add(0, outlineYDiff, 0);
				frcv.position.add(0, outlineYDiff, 0);
				nrcv.position.add(0, outlineYDiff, 0);
				meshBuilder = modelBuilder.part(meshName + "_outline", GL20.GL_LINES, Usage.Position | Usage.Normal, new Material(ColorAttribute.createDiffuse(new Color(MESH_OUTLINE_COLOR))));
				meshBuilder.rect(nlcv, flcv, frcv, nrcv);
				*/
			}
		}
		model = modelBuilder.end();
		terrain.registerInstance(new ModelInstance(model));
	}
	
	/**
	 * Return a color that is a percentage ([0.0f, 1.0f]), mixture of two colors.
	 * @param color1 first color, in rgba8888 form.
	 * @param color2 second color, in rgba8888 form.
	 * @param fraction the fractional mixture.
	 * @return the rgba8888 color.
	 */
	private int interpolateColors(int color1, int color2, float fraction) {
		// Extrapolate components...
		int r1 = (color1 >> 24) & 0xff;
		int r2 = (color2 >> 24) & 0xff;
		int g1 = (color1 >> 16) & 0xff;
		int g2 = (color2 >> 16) & 0xff;
		int b1 = (color1 >> 8) & 0xff;
		int b2 = (color2 >> 8) & 0xff;
		int nr = (int)((r2 - r1) * fraction + r1) << 24;
		int ng = (int)((g2 - g1) * fraction + g1) << 16;
		int nb = (int)((b2 - b1) * fraction + b1) << 8;
		return (nr | ng | nb | 0xff);
	}
	
	/**
	 * @return the chunk model.
	 */
	public Model getChunkModel() {
		return model;
	}


	@Override
	public void dispose() {
		model.dispose();
	}
	
}
