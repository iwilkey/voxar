package dev.iwilkey.voxar.gfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import dev.iwilkey.voxar.clock.Tickable;
import dev.iwilkey.voxar.perspective.VoxelSpacePerspective;

/**
 * Renderer for two-dimensional raster graphics in viewport space. Can be statically referenced.
 * @author iwilkey
 */
public final class RasterRenderer implements Renderable, RenderResizable, Tickable, Disposable {
	
	/**
	 * The Voxar engine's capability to draw batched Rasters to viewport space.
	 */
	private final SpriteBatch gfx2D;
	
	/**
	 * The Voxar engine's capability to to draw batched (2D) Rasters in 3D space -> (2.5D :P)
	 */
	private DecalBatch gfx25D;
	
	/**
	 * The raster graphics buffer for Rasters to be rendered to viewport space. vBuffer = "Viewport buffer".
	 */
	private static Array<Raster2> vBuffer;
	
	/**
	 * The raster graphics buffer for Rasters to rendered in 3D space. tsBuffer = "Three-space buffer".
	 */
	private static Array<Raster25> tsBuffer;

	public RasterRenderer() {
		gfx2D = new SpriteBatch();
		gfx25D = null; // This cannot be initialized until a CameraGroupStrategy can be made (when a scene has a VoxelSpace).
		vBuffer = new Array<>();
		tsBuffer = new Array<>();
	}
	
	@Override
	public void tick() {
		vBuffer.clear();
		tsBuffer.clear();
	}
	
	@Override
	public void render2D() {
		if(vBuffer.size == 0) 
			return;
		gfx2D.begin();
		for(Raster2 s : vBuffer) {
			gfx2D.setColor(s.getTint());
			final int x = (int)s.getBoundingBox().x;
			final int y = (int)s.getBoundingBox().y;
			final int w = (int)s.getBoundingBox().width;
			final int h = (int)s.getBoundingBox().height;
			gfx2D.draw(s.getBindedRaster(), x, y, w, h);
		}
		gfx2D.setColor(Color.WHITE);
		gfx2D.end();
	}
	
	@Override
	public void render3D(VoxelSpacePerspective perspective) {
		if(tsBuffer.size == 0)
			return;
		for(Raster25 r : tsBuffer) {
			if(r.shouldBillboard()) 
				r.getDecal().lookAt(perspective.position, perspective.up);
			gfx25D.add(r.getDecal());
		}
		gfx25D.flush();
	}
	
	/**
	 * Render a Raster2 to viewport space.
	 * @param raster the Raster2.
	 * @param x the x coordinate in screen space.
	 * @param y the y coordinate in screen space.
	 * @param width the rendering width in screen space.
	 * @param height the the rendering height in screen space.
	 */
	public static void renderToViewport(Raster2 raster, int x, int y, int width, int height) {
		raster.setPosition(new Vector2(x, y));
		raster.setDimensions(new Vector2(width, height));
		vBuffer.add(raster);
	}
	
	/**
	 * Render a Raster25 to 3D space.
	 * @param raster the Raster25.
	 * @param x the x coordinate in world space.
	 * @param y the y coordinate in world space.
	 * @param z the y coordinate in world space.
	 * @param scaleX the raster x-axis scale in world space.
	 * @param scaleY the raster y-axis scale in world space.
	 * @param billboard should the Raster25 always face the VoxelSpacePerspective?
	 */
	public static void renderToVoxelWorld(Raster25 raster, float x, float y, float z, float scaleX, float scaleY, boolean billboard) {
		raster.getDecal().setPosition(x, y, z);
		raster.getDecal().setScaleX(scaleX);
		raster.getDecal().setScaleY(scaleY);
		raster.setBillboard(billboard);
		tsBuffer.add(raster);
	}
	
	/**
	 * @return the Voxar engine's capability to to draw batched Rasters (2D) in 3D space. Only available if the current state has a VoxelSpace active.
	 */
	public DecalBatch get25D() {
		return gfx25D;
	}
	
	/**
	 * Set the renderer for Raster25. Should only ever be called by the VoxarRenderer.
	 * @param batch the new batch with updated CameraGroupStratgy.
	 */
	public void set25D(DecalBatch batch) {
		gfx25D = batch;
	}
	
	/**
	 * @return the Voxar engine's capability to draw batched Rasters to viewport space.
	 */
	public SpriteBatch get2D() {
		return gfx2D;
	}
	
	@Override
	public void windowResizeCallback(int nw, int nh) {
		gfx2D.getProjectionMatrix().setToOrtho2D(0, 0, nw, nh);
	}
	
	@Override
	public void dispose() {
		gfx2D.dispose();
		gfx25D.dispose();
	}
	
}
