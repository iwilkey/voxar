package dev.iwilkey.voxar.state;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;

import dev.iwilkey.voxar.asset.AssetType;
import dev.iwilkey.voxar.asset.VoxarAsset;
import dev.iwilkey.voxar.gfx.Raster25;
import dev.iwilkey.voxar.gfx.RasterRenderer;
import dev.iwilkey.voxar.input.StandardInput;
import dev.iwilkey.voxar.perspective.FreeController;
import dev.iwilkey.voxar.physics.PhysicsBodyType;
import dev.iwilkey.voxar.physics.PhysicsPrimitive;
import dev.iwilkey.voxar.space.VoxelSpace;
import dev.iwilkey.voxar.space.terrain.Terrain;

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
	Raster25 zeroZeroMarker;
	PointLight light;

	@Override
	public void begin() {
		Gdx.input.setCursorCatched(true);
		focused = true;
		zeroZeroMarker = new Raster25(Gdx.files.internal("img/crosshair.png"));
		setUpSpace();
	}
	
	@Override
	public void gui() {
		
	}

	@Override
	public void process() {
		focus();
		RasterRenderer.renderRasterInWorldSpace(zeroZeroMarker, 0, 5, 0, 5.0f, 5.0f, true);
		light.setPosition(getVoxelSpace().getRenderingPerspective().position);
		
		if(StandardInput.cursorJustDown(Buttons.LEFT)) {
			long cube = getVoxelSpace().getEntityManager().addRigidbody("vox/cube/cube.vox.obj", 1.0f, PhysicsPrimitive.CUBOID, PhysicsBodyType.DYNAMIC);
			getVoxelSpace().getEntityManager().getEntity(cube).setPosition(getVoxelSpace().getRenderingPerspective().position.cpy().add(0, 10.0f, 0));
		}
		
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
		double terrainSeed = 1000;
		long globalChunkScale = 10L;
		long globalHeightAmp = 100L;
		int globalChunkApothem = 10;
		getVoxelSpace().setTerrain(new Terrain(getVoxelSpace(), terrainSeed, globalChunkScale, globalHeightAmp, globalChunkApothem));
		
		// Set up space perspective.
		controller = new FreeController();
		getVoxelSpace().setPerspectiveController(controller);
		
		// Add a point light to follow camera.
		light = new PointLight().set(0.8f, 1f, 1f, getVoxelSpace().getRenderingPerspective().position.x, 
				getVoxelSpace().getRenderingPerspective().position.y, getVoxelSpace().getRenderingPerspective().position.z, 10f);
		
	}
	
	public boolean isFocused() {
		return focused;
	}
	
}
