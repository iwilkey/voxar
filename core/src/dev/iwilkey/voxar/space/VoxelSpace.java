package dev.iwilkey.voxar.space;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelCache;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import dev.iwilkey.voxar.clock.Tickable;
import dev.iwilkey.voxar.entity.VoxelEntity;
import dev.iwilkey.voxar.entity.VoxelEntityManager;
import dev.iwilkey.voxar.gfx.FrustumCulling;
import dev.iwilkey.voxar.gfx.RenderResizable;
import dev.iwilkey.voxar.gfx.VoxarRenderer;
import dev.iwilkey.voxar.perspective.Controller;
import dev.iwilkey.voxar.perspective.VoxelSpacePerspective;
import dev.iwilkey.voxar.physics.PhysicsEngine;
import dev.iwilkey.voxar.space.terrain.Terrain;
import dev.iwilkey.voxar.state.VoxarEngineState;

/**
 * A three-dimensional environment with real-time lighting, real-time point shadows (coming soon), entity management, and dynamic physics.
 * @author iwilkey
 */
public final class VoxelSpace implements Disposable, Tickable, RenderResizable {
	
	/**
	 * The state that the VoxelSpace belongs to.
	 */
	private final VoxarEngineState operatingState;
	
	/**
	 * The entity manager.
	 */
	private final VoxelEntityManager entityManager;
	
	/**
	 * The physics engine.
	 */
	private final PhysicsEngine physicsEngine;
	
	/**
	 * RenderableProvider of all active VoxelEntities, baked and optimized.
	 */
	private final ModelCache renderables;
	
	/**
	 * List of RenderableProviders that are currently visable to the VoxelSpacePerspecive.
	 */
	private Array<ModelInstance> culledRenderables;
	
	/**
	 * OpenGL lighting.
	 */
	private final Environment lighting;
	
	/**
	 * Terrain.
	 */
	private Terrain terrain;
	
	/**
	 * "Camera" that captures the VoxelSpace's Renderables from any perspective.
	 */
	private final VoxelSpacePerspective camera;

	public VoxelSpace(VoxarEngineState operatingState) {
		this.operatingState = operatingState;
		// Initialize the RenderableProvider.
		renderables = new ModelCache();
		culledRenderables = new Array<>();
		
		// Lighting and shadows.
		lighting = new Environment();
		lighting.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.6f, 0.6f, 0.6f, 1f));
		lighting.set(new ColorAttribute(ColorAttribute.Fog, 0.1f, 0.1f, 0.1f, 1f));
		lighting.add(new DirectionalLight().set(1f, 1f, 1f, -1, -1, -1));
		
		// Initialize and configure the camera.
		camera = new VoxelSpacePerspective(67, VoxarRenderer.WW, VoxarRenderer.WH);
		
		// Initialize entity manager.
		entityManager = new VoxelEntityManager(this);
		
		// Initialize physics engine.
		physicsEngine = new PhysicsEngine();
		
		// Initialize terrain.
		terrain = null;
	}
	
	/**
	 * Bake and optimize RenderableProviders active in the VoxelSpace.
	 * @param entities active RenderableProviders.
	 */
	protected void optimizeAndProvideRenderables() {
		
		culledRenderables.clear();
		
		// Cull entities.
		if(entityManager.getRenderableEntityProviders().size != 0) {
			for(final VoxelEntity e : entityManager.getRenderableEntityProviders()) 
				if(FrustumCulling.sphericalTestWith(e, camera))
					culledRenderables.add(e);
		}
		
		// Cull terrain chunks (if terrain exists).
		if(terrain != null) {
			for(final ModelInstance t : terrain.getRenderableProviders()) {
				if(FrustumCulling.cuboidTestWith(t, camera))
					culledRenderables.add(t);
			}
		}
		
		// Add culled RenderableProviders to Space renderables.
		if(culledRenderables.size != 0) {
			renderables.begin(camera);
			renderables.add(culledRenderables);
			renderables.end();
		}
		
	}
	
	@Override
	public void tick() {
		camera.tick();
		physicsEngine.tick();
		entityManager.tick();
		optimizeAndProvideRenderables();
	}
	
	@Override
	public void windowResizeCallback(int nw, int nh) {
		camera.viewportWidth = nw;
		camera.viewportHeight = nh;
		camera.update();
	}
	
	/**
	 * Set the controller for the VoxelSpacePerspective.
	 * @param controller the controller.
	 */
	public void setPerspectiveController(Controller controller) {
		camera.setController(controller);
	}
	
	/**
	 * Set a terrain object to the VoxelSpace.
	 * @param terrain the terrain object.
	 */
	public void setTerrain(Terrain terrain) {
		this.terrain = terrain;
	}
	
	/**
	 * @return the VoxelSpace's physics engine.
	 */
	public PhysicsEngine getPhysicsEngine() {
		return physicsEngine;
	}
	
	/**
	 * @return the VoxelSpace's camera.
	 */
	public VoxelSpacePerspective getRenderingPerspective() {
		return camera;
	}
	
	/**
	 * @return a baked and optimized RenderableProvider.
	 */
	public ModelCache getRenderables() {
		return renderables;
	}
	
	/**
	 * @return iterative entity Renderables.
	 */
	public Array<VoxelEntity> getRawRenderables() {
		return entityManager.getRenderableEntityProviders();
	}
	
	/**
	 * @return return RenderableProviders visible through the VoxelSpacePerspective.
	 */
	public Array<ModelInstance> getCulledRenderables() {
		return culledRenderables;
	}
	
	/**
	 * @return the amount of RenderableProviders that should be rendered this frame.
	 */
	public long getCulledRenderablesSize() {
		return culledRenderables.size;
	}
	
	/**
	 * @return the lighting and shadow configuration of the VoxelSpace.
	 */
	public Environment getLighting() {
		return lighting;
	}
	
	/**
	 * @return the VoxelSpace's entity manager.
	 */
	public VoxelEntityManager getEntityManager() {
		return entityManager;
	}
	
	/**
	 * @return the state the VoxelSpace is active inside.
	 */
	public VoxarEngineState getOperatingState() {
		return operatingState;
	}

	@Override
	public void dispose() {
		physicsEngine.dispose();
		if(terrain != null)
			terrain.dispose();
		renderables.dispose();
		entityManager.dispose();
		camera.dispose();
	}
	
}
