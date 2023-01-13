package dev.iwilkey.voxar.world;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.Disposable;

public final class Terrain implements Disposable {
	
	private Model model;
	
	// Terrain settings
	private static final long TERRAIN_CHUNK_SIZE = 128;
	
	public Terrain() {
		generate(0, 0);
	}
	
	/**
	 * Procedurally generates a new terrain chunk mesh based on starting point.
	 */
	private void generate(long startX, long startZ) {
		ModelBuilder builder = new ModelBuilder();
		builder.begin();
		
		
		
		model = builder.end();
	}
	
	/**
	 * @return the model representing the current terrain.
	 */
	public Model getGeneratedModel() {
		return model;
	}
	
	/**
	 * @return a renderable instance of the terrain mesh.
	 */
	public ModelInstance get() {
		return new ModelInstance(model);
	}

	@Override
	public void dispose() {
		model.dispose();
	}

}
