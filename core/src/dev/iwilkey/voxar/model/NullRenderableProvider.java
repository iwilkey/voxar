package dev.iwilkey.voxar.model;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

/**
 * Used when the VoxelSpace culls all active entities and the VoxarRenderer has no 3D RenderableProviders available. 
 * It provides OpenGL a reason to use default shader during render time when all RenderableProviders have been culled away.
 * @author iwilkey
 */
public final class NullRenderableProvider {
	
	private Model model;
	
	/**
	 * Provide OpenGL a reason to use default shader during render time when all RenderableProviders have been culled away.
	 */
	private RenderableProvider renderable;
	
	@SuppressWarnings("deprecation")
	public NullRenderableProvider() {
		// The null Renderable is a sphere with no dimensions.
		ModelBuilder modelBuilder = new ModelBuilder();
		modelBuilder.begin();
		MeshPartBuilder meshBuilder;
		meshBuilder = modelBuilder.part("part2", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal, new Material());
		meshBuilder.sphere(0, 0, 0, 10, 10);
		model = modelBuilder.end();
		this.renderable = new ModelInstance(model);
	}
	
	/**
	 * @return OpenGL's reason to use default shader when no RenderableProviders are active.
	 */
	public RenderableProvider getProvider() {
		return renderable;
	}
	
}
