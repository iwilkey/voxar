package dev.iwilkey.voxar.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector3;

import dev.iwilkey.voxar.asset.AssetType;
import dev.iwilkey.voxar.asset.VoxarAsset;
import dev.iwilkey.voxar.gfx.Raster25;
import dev.iwilkey.voxar.gfx.RasterRenderer;
import dev.iwilkey.voxar.gfx.VoxarRenderer;
import dev.iwilkey.voxar.input.StandardInput;
import dev.iwilkey.voxar.perspective.FreeController;
import dev.iwilkey.voxar.physics.PhysicsBodyType;
import dev.iwilkey.voxar.physics.PhysicsPrimitive;
import dev.iwilkey.voxar.world.VoxelSpace;

/**
 * Implementation of a VoxarEngineState for testing and debugging new Voxar engine features.
 * @author iwilkey
 */
public final class VoxarDebugWorld extends VoxarEngineState {
	
	private boolean focused;

	public VoxarDebugWorld() {
		super("Voxar Engine Debug World",
				new VoxarAsset("cube", "vox/cube/cube.vox.obj", AssetType.MODEL),
				new VoxarAsset("crosshair", "img/crosshair.png", AssetType.TEXTURE));
	}

	FreeController controller;
	Raster25 test;

	@Override
	public void begin() {
		Gdx.input.setCursorCatched(true);
		Gdx.input.setCursorPosition(VoxarRenderer.WW / 2, VoxarRenderer.WH / 2);
		focused = true;
		test = new Raster25(Gdx.files.internal("img/crosshair.png"));
		setUpSpace();
	}
	
	@Override
	public void gui() {
		
	}

	@Override
	public void process() {
		focus();
		RasterRenderer.renderToVoxelWorld(test, 2, 2, 2, 1.0f, 1.0f, true);
	}

	@Override
	public void end() {}
	
	void focus() {
		if(StandardInput.keyJustDown(Keys.ESCAPE) && focused) {
			focused = false;
			Gdx.input.setCursorCatched(false);
			controller.setActive(false);	
		}
		if(StandardInput.cursorJustDown(Buttons.LEFT) && !focused) {
			focused = true;
			Gdx.input.setCursorCatched(true);
			controller.setActive(true);
		}
	}
	
	void setUpSpace() {
		setVoxelSpace(new VoxelSpace(this));
		for(int x = -10; x < 10; x++) 
			for(int z = -10; z < 10; z++) {
				long cube = getVoxelSpace().getEntityManager().addRigidbody("vox/cube/cube.vox.obj", 0.0f, PhysicsPrimitive.CUBOID, PhysicsBodyType.STATIC);
				getVoxelSpace().getEntityManager().getEntity(cube).setPosition(new Vector3(x, 0, z));
			}
		controller = new FreeController();
		getVoxelSpace().setPerspectiveController(controller);
	}
	
	public boolean isFocused() {
		return focused;
	}
	
}
