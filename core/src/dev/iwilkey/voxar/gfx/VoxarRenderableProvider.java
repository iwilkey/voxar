package dev.iwilkey.voxar.gfx;

import com.badlogic.gdx.utils.Disposable;

/**
 * An object that, when inherited from either 2D, 25D, or 3D abstractions, allows renderable
 * data to be processed and shown by the Renderer.
 * @author iwilkey
 */
public abstract class VoxarRenderableProvider implements ViewportResizable, Disposable {
	
	private final long shaderID;
	
	/**
	 * Create a VoxarRenderableProvider. Must supply a shaderID, and that shader must be registered
	 * to the renderer.
	 * @param shaderID the registered shader to render registered instances.
	 */
	public VoxarRenderableProvider(long shaderID) {
		this.shaderID = shaderID;
	}
	
	/**
     * @return the shader ID the Renderer should render this provider with.
     */
	public long getDesiredShader() {
		return shaderID;
	}
	
}
