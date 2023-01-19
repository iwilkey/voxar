package dev.iwilkey.voxar.gfx;

import com.badlogic.gdx.utils.Array;

/**
 * An object that, when inherited, allows Raster2 to be processed and rendered by the Renderer.
 * @author iwilkey
 */
public abstract class VoxarRenderableProvider2D extends VoxarRenderableProvider {
	
	private Array<Raster2> renderableTextures;
	
	public VoxarRenderableProvider2D() {
		super(Renderer.STANDARD_2D_SHADER);
		renderableTextures = new Array<>();
		Renderer.register(this);
	}
	
	/**
	 * Register a texture instance to be rendered each call.
	 * @param instance
	 */
	public void registerInstance(Raster2 instance) {
		renderableTextures.add(instance);
	}
	
	/**
	 * Remove a texture instance from being rendered.
	 */
	public void abortInstance() {
		renderableTextures.removeValue(null, false);
	}
	
	/**
	 * @return the registered texture regions to be rendered text frame.
	 */
	public Array<Raster2> getRegisteredRaster2s() {
		return renderableTextures;
	}
	
}
