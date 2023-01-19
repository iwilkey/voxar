package dev.iwilkey.voxar.gfx;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.utils.Array;

import dev.iwilkey.voxar.entity.VoxelEntity;
import dev.iwilkey.voxar.perspective.Perspective3D;

/**
 * An object that, when inherited, allows 3D RenderableProviders to be rendered by the Renderer.
 * @author iwilkey
 */
public abstract class VoxarRenderableProvider3D extends VoxarRenderableProvider {
	
	private final Perspective3D renderingPerspective;
	private final Environment renderingEnvironment;
	private Array<ModelInstance> renderableModelInstances;
	private Array<VoxelEntity> renderableVoxelEntityInstances;

	public VoxarRenderableProvider3D(Perspective3D renderingPerspective, Environment renderingEnvironment) {
		super(Renderer.STANDARD_3D_SHADER);
		this.renderingPerspective = renderingPerspective;
		this.renderingEnvironment = renderingEnvironment;
		renderableModelInstances = new Array<>();
		renderableVoxelEntityInstances = new Array<>();
		Renderer.register(this);
	}
	
	/**
	 * Register an instance to be rendered each render call.
	 * @param instance the instance.
	 */
	public void registerInstance(ModelInstance instance) {
		renderableModelInstances.add(instance);
	}
	
	/**
	 * Register a VoxelEntity to be rendered each render call.
	 * @param entity the entity.
	 */
	public void registerInstance(VoxelEntity entity) {
		renderableVoxelEntityInstances.add(entity);
	}
	
	/**
	 * Remove an instance that is being rendered each call.
	 * @param instance the instance.
	 */
	public void abortInstance(ModelInstance instance) {
		renderableModelInstances.removeValue(instance, false);
		renderableVoxelEntityInstances.removeValue((VoxelEntity)instance, false);
	}
	
	/**
	 * @return list of RenderableProviders to be culled and rendered.
	 */
	public Array<ModelInstance> getRegisteredModelInstances() {
		return renderableModelInstances;
	}
	
	/**
	 * @return list of VoxelEntities to be culled and rendered.
	 */
	public Array<VoxelEntity> getRegisteredVoxelEntities() {
		return renderableVoxelEntityInstances;
	}
    
    /**
     * @return the rendering space of this RenderableProvider3D.
     */
    public Perspective3D getRenderingPerspective() {
    	return renderingPerspective;
    }
    
    /**
     * @return rendering environment.
     */
    public Environment getRenderingEnvironment() {
    	return renderingEnvironment;
    }
    
    @Override
	public void windowResizeCallback(int nw, int nh) {
    	renderingPerspective.viewportWidth = nw;
    	renderingPerspective.viewportHeight = nh;
    	renderingPerspective.update();
	}
    
    @Override
    public void dispose() {
    	renderingPerspective.dispose();
    }
	
}
