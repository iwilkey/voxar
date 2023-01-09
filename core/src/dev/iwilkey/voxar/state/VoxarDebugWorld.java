package dev.iwilkey.voxar.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;

import dev.iwilkey.voxar.asset.AssetType;
import dev.iwilkey.voxar.asset.VoxarAsset;
import dev.iwilkey.voxar.gfx.RasterRenderer;
import dev.iwilkey.voxar.gfx.VoxarRenderer;
import dev.iwilkey.voxar.physics.PhysicsBodyType;
import dev.iwilkey.voxar.physics.PhysicsPrimitive;
import dev.iwilkey.voxar.world.VoxelSpace;

/**
 * Implementation of a VoxarEngineState for testing and debugging new Voxar engine features.
 * @author iwilkey
 */
public final class VoxarDebugWorld extends VoxarEngineState {

	public VoxarDebugWorld() {
		super("Voxar Engine Debug World",
				new VoxarAsset("cube", "vox/cube/cube.vox.obj", AssetType.MODEL),
				new VoxarAsset("crosshair", "img/crosshair.png", AssetType.TEXTURE));
	}
	
	Sprite crosshair;

	@Override
	public void begin() {
		setVoxelSpace(new VoxelSpace(this));
		// Catch cursor to middle.
		Gdx.input.setCursorCatched(true);
		Gdx.input.setCursorPosition(VoxarRenderer.WW / 2, VoxarRenderer.WH / 2);
		// Create crosshair sprite with referenced texture.
		crosshair = new Sprite(getAssetManager().get("img/crosshair.png", Texture.class));
		crosshair.setPosition((VoxarRenderer.WW / 2) - (crosshair.getWidth() / 2), (VoxarRenderer.WH / 2) - (crosshair.getHeight() / 2));
		// Create debug terrain (physics objects).
		for(int x = -10; x < 10; x++) 
			for(int z = -10; z < 10; z++) {
				long cube = getVoxelSpace().getEntityManager().addRigidbody("vox/cube/cube.vox.obj", 0.0f, PhysicsPrimitive.CUBOID, PhysicsBodyType.STATIC);
				getVoxelSpace().getEntityManager().getEntity(cube).setPosition(new Vector3(x, 0, z));
			}
	}
	
	@Override
	public void gui() {
		
	}

	@Override
	public void process() {
		RasterRenderer.render(crosshair);
	}

	@Override
	public void end() {}

}
