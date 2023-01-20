package dev.iwilkey.voxar.perspective;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

import dev.iwilkey.voxar.gfx.Raster2;
import dev.iwilkey.voxar.gfx.Renderer;
import dev.iwilkey.voxar.gfx.VoxarRenderableProvider2D;

/**
 * A simple 2D rendering of a crosshair in the center of the screen.
 * @author iwilkey
 */
public final class Crosshair extends VoxarRenderableProvider2D {
	
	private int width = 10;
	private int height = 10;
	private Raster2 renderable;
	
	public Crosshair() {
		renderable = new Raster2(Gdx.files.internal("img/crosshair.png"));
		update();
		registerInstance(renderable);
	}
	
	private void update() {
		renderable.setPosition(new Vector2((Renderer.WINDOW_WIDTH / 2) - (width / 2), (Renderer.WINDOW_HEIGHT / 2) - (height / 2)));
		renderable.setDimensions(new Vector2(width, height));
	}
	
	/**
	 * Edit the dimensions of the crosshair.
	 * @param dim the dimensions (width, height).
	 */
	public void setDimensions(Vector2 dim) {
		width = (int)dim.x;
		height = (int)dim.y;
		update();
	}
	
	@Override
	public void windowResizeCallback(int nw, int nh) {
		update();
	}

	@Override
	public void dispose() {
		renderable.dispose();
	}

}