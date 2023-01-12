package dev.iwilkey.voxar.gfx;

import dev.iwilkey.voxar.perspective.VoxelSpacePerspective;

/**
 * Interface for objects capable of rendering 2D or 3D graphics to the screen.
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
}
