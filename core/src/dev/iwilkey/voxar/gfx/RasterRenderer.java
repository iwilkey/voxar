package dev.iwilkey.voxar.gfx;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import dev.iwilkey.voxar.clock.Tickable;

/**
 * Renderer for two-dimensional raster graphics in viewport space. Can be statically referenced.
 * @author iwilkey
 */
public final class RasterRenderer implements Renderable, Tickable, Disposable {
	
	/**
	 * The Terrafort engine's capability to draw batched quads using indices.
	 */
	private final SpriteBatch gfx2D;
	
	/**
	 * The raster graphics buffer for sprites to be rendered.
	 */
	private static Array<Sprite> gfxBuffer;
	
	public RasterRenderer() {
		gfx2D = new SpriteBatch();
		gfxBuffer = new Array<>();
	}
	
	@Override
	public void tick() {
		gfxBuffer.clear();
	}
	
	@Override
	public void render() {
		gfx2D.begin();
		for(Sprite s : gfxBuffer) 
			s.draw(gfx2D);
		gfx2D.end();
	}
	
	/**
	 * Render a Sprite to viewport space.
	 * @param sprite the sprite.
	 */
	public static void render(Sprite sprite) {
		gfxBuffer.add(sprite);
	}
	
	@Override
	public void dispose() {
		gfx2D.dispose();
		
	}
	
}
