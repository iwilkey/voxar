package dev.iwilkey.voxar.gfx;

import com.badlogic.gdx.utils.Array;

import dev.iwilkey.voxar.perspective.Perspective3D;

public abstract class VoxarRenderableProvider25D extends VoxarRenderableProvider {
	
	private Perspective3D perspective;
	
	private Array<Raster25> renderableRaster25;
	
	public VoxarRenderableProvider25D(Perspective3D perspective) {
		super(Renderer.STANDARD_25D_SHADER);
		this.perspective = perspective;
		renderableRaster25 = new Array<>();
		
	}
	
	/**
	 * Register a texture instance to be rendered each call.
	 * @param instance
	 */
	public void registerInstance(Raster25 instance) {
		renderableRaster25.add(instance);
	}
	
	/**
	 * Remove a texture instance from being rendered.
	 */
	public void abortInstance() {
		renderableRaster25.removeValue(null, false);
	}
	
	/**
     * @return the rendering perspective of this RenderableProvider25D.
     */
	public Perspective3D getRenderingPerspective() {
		return perspective;
	}
	
	/**
	 * @return the registered texture regions to be rendered text frame.
	 */
	public Array<Raster25> getRegisteredRaster25s() {
		return renderableRaster25;
	}
	
}
