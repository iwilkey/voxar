package dev.iwilkey.voxar.gfx;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import dev.iwilkey.voxar.perspective.Perspective3D;

/**
 * Interface for objects that can provide RenderableProviders for objects that can render them in 3D space.
 * @author iwilkey
 */
public abstract class RenderableProvider3D implements ViewportResizable, Disposable {
	
	private final long shaderID;
	private final Perspective3D renderingPerspective;
	private final Environment renderingEnvironment;
	private Array<ModelInstance> renderableModelInstances;

	public RenderableProvider3D(Perspective3D renderingPerspective, Environment renderingEnvironment) {
		this.renderingPerspective = renderingPerspective;
		this.renderingEnvironment = renderingEnvironment;
		shaderID = Renderer.STANDARD_3D_SHADER;
		renderableModelInstances = new Array<>();
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
	 * Remove an instance that is being rendered each call.
	 * @param instance the instance.
	 */
	public void abortInstance(ModelInstance instance) {
		renderableModelInstances.removeValue(instance, false);
	}
	
	/**
	 * @return list of RenderableProviders to be culled and rendered.
	 */
	public Array<ModelInstance> getRegisteredModelInstances() {
		return renderableModelInstances;
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
    
    /**
     * @return the shader ID the Renderer should render this provider with.
     */
    public long getDesiredShaderID() {
    	return shaderID;
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
