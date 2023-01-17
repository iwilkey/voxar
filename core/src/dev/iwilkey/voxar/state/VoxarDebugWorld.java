package dev.iwilkey.voxar.state;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector3;

import dev.iwilkey.voxar.asset.AssetType;
import dev.iwilkey.voxar.asset.VoxarAsset;
import dev.iwilkey.voxar.gfx.Raster25;
import dev.iwilkey.voxar.input.StandardInput;
import dev.iwilkey.voxar.perspective.FreeController;
import dev.iwilkey.voxar.physics.PhysicsBodyType;
import dev.iwilkey.voxar.physics.PhysicsPrimitive;
import dev.iwilkey.voxar.physics.Raycast;
import dev.iwilkey.voxar.physics.VoxarPhysicsTag;
import dev.iwilkey.voxar.physics.vRigidBody;
import dev.iwilkey.voxar.space.VoxelSpace;

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
		
		if(StandardInput.cursorJustDown(Buttons.RIGHT)) {
			vRigidBody hitobject = Raycast.hitObject(getVoxelSpace().getRenderingPerspective(), getVoxelSpace().getPhysicsEngine().getDynamicsWorld(), 500f);
			if(hitobject != null) {
				if(hitobject.getTag() != VoxarPhysicsTag.TERRAIN) {
					hitobject.activate();
					hitobject.applyCentralForce(getVoxelSpace().getRenderingPerspective().direction.cpy().scl(1000.0f));
				}
			}
		}
		
		if(StandardInput.cursorJustDown(Buttons.LEFT)) {
			Vector3 hitpoint = Raycast.hitPoint(getVoxelSpace().getRenderingPerspective(), getVoxelSpace().getPhysicsEngine().getDynamicsWorld(), 500f);
			if(hitpoint != null) {
				long cube = getVoxelSpace().getEntityManager().addRigidbody("vox/cube/cube.vox.obj", 1.0f, PhysicsPrimitive.CUBOID, PhysicsBodyType.DYNAMIC);
				getVoxelSpace().getEntityManager().getEntity(cube).setPosition(hitpoint.cpy().add(0, 0.5f, 0));
			}
			
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
		
		// Create a terrain inside voxel space.
		double terrainSeed = new Random().nextDouble(1, 10000);
		long globalChunkScale = new Random().nextLong(5, 15);
		long globalHeightAmp = new Random().nextLong(50, 300);
		int globalChunkApothem = new Random().nextInt(5, 10);
		getVoxelSpace().createTerrain(terrainSeed, globalChunkScale, globalHeightAmp, globalChunkApothem);
		
		// Set up space perspective.
		controller = new FreeController();
		getVoxelSpace().setPerspectiveController(controller);
		
	}
	
	public boolean isFocused() {
		return focused;
	}
	
}
