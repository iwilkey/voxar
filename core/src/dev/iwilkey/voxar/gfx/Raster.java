package dev.iwilkey.voxar.gfx;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * A graphical object comprised of 2D pixel data that can be rendered in 2D or 2.5D depending on it's implementation.
 * @author iwilkey
 */
public abstract class Raster extends Texture {
	
	private TextureRegion bindedRaster;

	public Raster(FileHandle file) {
		super(file);
		bindedRaster = new TextureRegion(this);
	}
	
	/**
	 * @return the binded Texture (raster data) able to be rendered by the RasterRenderer.
	 */
	public TextureRegion getBindedRaster() {
		return bindedRaster;
	}
	
}
