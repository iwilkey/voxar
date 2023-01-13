package dev.iwilkey.voxar.gfx;

import com.badlogic.gdx.graphics.g3d.RenderableProvider;

import dev.iwilkey.voxar.perspective.VoxelSpacePerspective;

/**
 * Interface for objects capable of rendering or capable of being rendered.
 * @author iwilkey
 */
public interface Renderable {
	/**
	 * Run unique render process for rendering object in 2D.
	 */
	default public void render2D() {}
	
	/**
	 * Run unique render process for rendering object (generic).
	 */
	default public void render() {}
	
	/**
	 * Run unique render process for rendering object in 3D space.
	 * @param perspective the VoxelSpacePerspective of VoxelSpace object is rendered in.
	 */
	default public void render3D(VoxelSpacePerspective perspective) {}
	
	/**
	 * @return a RenderableProvider of this Renderable object.
	 */
	default public RenderableProvider getRenderableProvider() {
		return null;
	}
}
