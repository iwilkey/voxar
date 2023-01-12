package dev.iwilkey.voxar.gfx;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * A implementation of Raster that can be rendered in viewport (2D) space by the RasterRenderer.
 * @author iwilkey
 */
public final class Raster2 extends Raster {
	
	private Rectangle boundingBox;
	private Color tint;
	
	/**
	 * A new 2D Raster object.
	 * @param file the path to the raster graphics file. Example: "assets/texture.png".
	 */
	public Raster2(FileHandle file) {
		super(file);
		boundingBox = new Rectangle(0, 0, 0, 0);
		tint = Color.WHITE;
	}
	
	/**
	 * Set Raster position in screen coordinates.
	 * @param position the position.
	 */
	public void setPosition(Vector2 position) {
		boundingBox.x = position.x;
		boundingBox.y = position.y;
	}
	
	/**
	 * Set dimensions of the Raster in screen space.
	 * @param dimensions the dimensions.
	 */
	public void setDimensions(Vector2 dimensions) {
		boundingBox.width = dimensions.x;
		boundingBox.height = dimensions.y;
	}

	/**
	 * @return bounding box of Raster in screen space for rendering.
	 */
	public Rectangle getBoundingBox() {
		return boundingBox;
	}
	
	/**
	 * Set the tint of the Raster object.
	 * @param tint the tint.
	 */
	public void setTint(Color tint) {
		this.tint = tint;
	}
	
	/**
	 * Set the tint of the Raster that will be applied at RasterRenderer rendering time.
	 * @param r red value [0, 255].
	 * @param g green value [0, 255].
	 * @param b blue value [0, 255].
	 */
	public void setTint255(int r, int g, int b) {
		tint.set(r / 255.0f, g / 255.0f, b / 255.0f, tint.a);
	}
	
	/**
	 * Set the tint of the Raster that will be applied at RasterRenderer rendering time.
	 * @param r red value [0.0, 1.0].
	 * @param g green value [0.0, 1.0].
	 * @param b blue value [0.0, 1.0].
	 */
	public void setTint(float r, float g, float b) {
		tint.set(r, g, b, tint.a);
	}
	
	/**
	 * Set the rendering opacity of the Raster object.
	 * @param opacity the opacity [0.0, 1.0].
	 */
	public void setOpacity(double opacity) {
		opacity = Math.min(1.0, opacity);
		opacity = Math.max(0, opacity);
		tint.set(tint.r, tint.g, tint.b, (float)opacity);
	}
	
	/**
	 * @return the selected tint of the Raster object.
	 */
	public Color getTint() {
		return tint;
	}
	
}
