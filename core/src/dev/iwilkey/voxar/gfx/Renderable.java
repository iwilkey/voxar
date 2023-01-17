package dev.iwilkey.voxar.gfx;

import com.badlogic.gdx.graphics.g3d.ModelInstance;

import dev.iwilkey.voxar.entity.VoxelEntity;

public interface Renderable {
	
	public void render(ModelInstance instance);
	
	default public void render(VoxelEntity entity) {
		render(entity);
	}
	
}
