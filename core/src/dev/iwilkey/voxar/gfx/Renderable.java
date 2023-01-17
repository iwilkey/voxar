package dev.iwilkey.voxar.gfx;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.utils.Array;

import dev.iwilkey.voxar.entity.VoxelEntity;
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
	
	/**
	 * @return the RenderableProviders of this Renderable object.
	 */
	default public Array<ModelInstance> getRenderableProviders() {
		return null;
	}
	
	/**
	 * @return the RenderableProviders of this entity Renderable object.
	 */
	default public Array<VoxelEntity> getRenderableEntityProviders() {
		return null;
	}
	
}
