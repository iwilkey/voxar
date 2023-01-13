package dev.iwilkey.voxar.state;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;

import dev.iwilkey.voxar.asset.AssetType;
import dev.iwilkey.voxar.asset.VoxarAsset;
import dev.iwilkey.voxar.gfx.Raster25;
import dev.iwilkey.voxar.gfx.RasterRenderer;
import dev.iwilkey.voxar.input.StandardInput;
import dev.iwilkey.voxar.perspective.FreeController;
import dev.iwilkey.voxar.world.VoxelSpace;

/**
 * Implementation of a VoxarEngineState for testing and debugging new Voxar engine features.
 * @author iwilkey
 */
public final class VoxarDebugWorld extends VoxarEngineState {
	
	static Random random = new Random();
	
	private boolean focused;

	public VoxarDebugWorld() {
		super("Voxar Engine Debug World",
				new VoxarAsset("cube", "vox/cube/cube.vox.obj", AssetType.MODEL),
				new VoxarAsset("crosshair", "img/crosshair.png", AssetType.TEXTURE));
	}

	FreeController controller;
	Raster25 test;
	PointLight light;

	@Override
	public void begin() {
		Gdx.input.setCursorCatched(true);
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
		RasterRenderer.renderRasterInWorldSpace(test, 2, 2, 2, 1.0f, 1.0f, true);
		light.setPosition(getVoxelSpace().getRenderingPerspective().position);
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
		// Generate terrain (test).
		
		
		// Set up space perspective.
		controller = new FreeController();
		getVoxelSpace().setPerspectiveController(controller);
		
		// Add some lights.
		getVoxelSpace().getLighting().add(new DirectionalLight().set(1, 1, 1, -1f, -0.8f, -0.2f));
		light = new PointLight().set(1f, 1f, 1f, getVoxelSpace().getRenderingPerspective().position.x, 
				getVoxelSpace().getRenderingPerspective().position.y, getVoxelSpace().getRenderingPerspective().position.z, 5f);
		getVoxelSpace().getLighting().add(light);
		
	}
	
	public boolean isFocused() {
		return focused;
	}
	
}
