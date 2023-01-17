package dev.iwilkey.voxar.space;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;

import dev.iwilkey.voxar.clock.Tickable;
import dev.iwilkey.voxar.entity.VoxelEntityManager;
import dev.iwilkey.voxar.gfx.RenderableProvider3D;
import dev.iwilkey.voxar.gfx.Renderer;
import dev.iwilkey.voxar.perspective.Controller;
import dev.iwilkey.voxar.perspective.Perspective3D;
import dev.iwilkey.voxar.physics.PhysicsEngine;
import dev.iwilkey.voxar.space.terrain.Terrain;
import dev.iwilkey.voxar.state.VoxarEngineState;

/**
 * A three-dimensional environment with real-time lighting, real-time point shadows (coming soon), entity management, and dynamic physics.
 * @author iwilkey
 */
public final class VoxelSpace extends RenderableProvider3D implements Tickable {
	
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
	 * Terrain.
	 */
	private Terrain terrain;

	public VoxelSpace(VoxarEngineState operatingState) {
		super(new Perspective3D(67, Renderer.WINDOW_WIDTH, Renderer.WINDOW_HEIGHT), new Environment());
		this.operatingState = operatingState;
		
		getRenderingEnvironment().set(new ColorAttribute(ColorAttribute.AmbientLight, 0.6f, 0.6f, 0.6f, 1f));
		getRenderingEnvironment().set(new ColorAttribute(ColorAttribute.Fog, 0.1f, 0.1f, 0.1f, 1f));
		getRenderingEnvironment().add(new DirectionalLight().set(1f, 1f, 1f, -1, -1, -1));
		
		// Initialize entity manager.
		entityManager = new VoxelEntityManager(this);
		
		// Initialize physics engine.
		physicsEngine = new PhysicsEngine();
		
		// Initialize terrain.
		terrain = null;
	}
	
	@Override
	public void tick() {
		getRenderingPerspective().tick();
		physicsEngine.tick();
		entityManager.tick();
	}
	
	/**
	 * Set the controller for the VoxelSpacePerspective.
	 * @param controller the controller.
	 */
	public void setPerspectiveController(Controller controller) {
		getRenderingPerspective().setController(controller);
	}
	
	/**
	 * Create a terrain inside VoxelSpace.
	 * @param seed a value that all procedural generation will be based on.
	 * @param globalChunkScale the world distance of each line connecting any two vertices. 
	 * @param globalHeightMapAmplifier The scale of the generated height map.
	 * @param globalChunkApothem The perpendicular distance of the center of the chunk to any side. Keep in mind, chunk area = (apothem * 2)^2! This shouldn't be set too high
	 * for performance reasons.
	 */
	public void createTerrain(double seed, long chunkScale, long heightMap, int chunkApothem) {
		terrain = new Terrain(this, seed, chunkScale, heightMap, chunkApothem);
	}
	
	/**
	 * @return the VoxelSpace's physics engine.
	 */
	public PhysicsEngine getPhysicsEngine() {
		return physicsEngine;
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
		super.dispose();
		if(terrain != null)
			terrain.dispose();
		physicsEngine.dispose();
		entityManager.dispose();
	}
	
}
