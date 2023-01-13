package dev.iwilkey.voxar.world;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelCache;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import dev.iwilkey.voxar.clock.Tickable;
import dev.iwilkey.voxar.entity.VoxelEntityManager;
import dev.iwilkey.voxar.gfx.RenderResizable;
import dev.iwilkey.voxar.gfx.ShadowProvider;
import dev.iwilkey.voxar.gfx.VoxarRenderer;
import dev.iwilkey.voxar.perspective.Controller;
import dev.iwilkey.voxar.perspective.VoxelSpacePerspective;
import dev.iwilkey.voxar.physics.PhysicsEngine;
import dev.iwilkey.voxar.state.VoxarEngineState;

/**
 * A three-dimensional environment with real-time lighting, shadows, entity management, and dynamic physics.
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
	 * OpenGL lighting.
	 */
	private final Environment lighting;
	
	/**
	 * Shadow provider.
	 */
	private final ShadowProvider shadowProvider;
	
	/**
	 * "Camera" that captures the VoxelSpace's Renderables from any perspective.
	 */
	private final VoxelSpacePerspective camera;

	public VoxelSpace(VoxarEngineState operatingState) {
		this.operatingState = operatingState;
		
		// Initialize the RenderableProvider, lighting, and shadows.
		renderables = new ModelCache();
		lighting = new Environment();
		lighting.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		lighting.add(new PointLight().set(1f, 1f, 1f, 5, 10, 1f, 10f));
		shadowProvider = new ShadowProvider();
		
		// Initialize and configure the "camera".
		camera = new VoxelSpacePerspective(67, VoxarRenderer.WW, VoxarRenderer.WH);
		
		// Initialize entity manager.
		entityManager = new VoxelEntityManager(this);
		
		// Initialize physics engine.
		physicsEngine = new PhysicsEngine();
	}
	
	/**
	 * Bake and optimize Renderables active in the VoxelSpace.
	 * @param entities active Renderables.
	 */
	protected void optimizeAndProvideRenderables(Array<ModelInstance> entities) {
		renderables.begin(camera);
		renderables.add(entities);
		renderables.end();
	}
	
	@Override
	public void tick() {
		camera.tick();
		physicsEngine.tick();
		entityManager.tick();
		optimizeAndProvideRenderables(entityManager.getRenderables());
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
	 * @return iterative Renderables.
	 */
	public Array<ModelInstance> getRawRenderables() {
		return entityManager.getRenderables();
	}
	
	/**
	 * @return the lighting and shadow configuration of the VoxelSpace.
	 */
	public Environment getLighting() {
		return lighting;
	}
	
	/**
	 * @return this VoxelSpace's shadow provider.
	 */
	public ShadowProvider getShadowProvider() {
		return shadowProvider;
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
		renderables.dispose();
		entityManager.dispose();
		camera.dispose();
	}
	
}
