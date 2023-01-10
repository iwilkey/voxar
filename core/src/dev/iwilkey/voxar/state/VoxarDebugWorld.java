package dev.iwilkey.voxar.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;

import dev.iwilkey.voxar.asset.AssetType;
import dev.iwilkey.voxar.asset.VoxarAsset;
import dev.iwilkey.voxar.gfx.RasterRenderer;
import dev.iwilkey.voxar.gfx.VoxarRenderer;
import dev.iwilkey.voxar.input.StandardInput;
import dev.iwilkey.voxar.perspective.PerspectiveController;
import dev.iwilkey.voxar.perspective.VoxelSpacePerspective;
import dev.iwilkey.voxar.physics.PhysicsBodyType;
import dev.iwilkey.voxar.physics.PhysicsPrimitive;
import dev.iwilkey.voxar.settings.VoxarGameplayVariables;
import dev.iwilkey.voxar.settings.KeyBinding;
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
	
	Sprite crosshair;

	@Override
	public void begin() {
		// Set bindings for scene...
		KeyBinding.setBinding("forward", Keys.W);
		KeyBinding.setBinding("ascend", Keys.SPACE);
		KeyBinding.setBinding("descend", Keys.SHIFT_LEFT);
		
		Gdx.input.setCursorCatched(true);
		Gdx.input.setCursorPosition(VoxarRenderer.WW / 2, VoxarRenderer.WH / 2);
		focused = true;
		crosshair = new Sprite(getAssetManager().get("img/crosshair.png", Texture.class));
		crosshair.setPosition((VoxarRenderer.WW / 2) - (crosshair.getWidth() / 2), (VoxarRenderer.WH / 2) - (crosshair.getHeight() / 2));
		setUpSpace();
	}
	
	@Override
	public void gui() {
		
	}

	@Override
	public void process() {
		focus();
		if(focused) RasterRenderer.render(crosshair);
	}

	@Override
	public void end() {}
	
	void focus() {
		if(StandardInput.keyJustDown(Keys.ESCAPE) && focused) {
			focused = false;
			Gdx.input.setCursorCatched(false);
		}
		if(StandardInput.cursorJustDown(Buttons.LEFT) && !focused) {
			focused = true;
			Gdx.input.setCursorCatched(true);
			Gdx.input.setCursorPosition(VoxarRenderer.WW / 2, VoxarRenderer.WH / 2);
		}
	}
	
	void setUpSpace() {
		setVoxelSpace(new VoxelSpace(this));
		for(int x = -10; x < 10; x++) 
			for(int z = -10; z < 10; z++) {
				long cube = getVoxelSpace().getEntityManager().addRigidbody("vox/cube/cube.vox.obj", 0.0f, PhysicsPrimitive.CUBOID, PhysicsBodyType.STATIC);
				getVoxelSpace().getEntityManager().getEntity(cube).setPosition(new Vector3(x, 0, z));
			}
		getVoxelSpace().setPerspectiveController(new PerspectiveController() {
			final float VERT_DEGREE_CLAMP = 90.0f;
			double rotVertAngle = 0.0f;
			Vector3 desiredPosition = new Vector3(0, 2, 0);
			@Override
			public void control(VoxelSpacePerspective perspective) {
				if(focused) {
					// Translation
					if(StandardInput.keyCurrent(KeyBinding.getBinding("forward"))) {
						
					}
					
					if(StandardInput.keyCurrent(KeyBinding.getBinding("ascend")))
						desiredPosition.add(Vector3.Y.scl(VoxarGameplayVariables.ASCENTION_SPEED));
					if(StandardInput.keyCurrent(KeyBinding.getBinding("descend")))
						desiredPosition.sub(Vector3.Y.scl(VoxarGameplayVariables.DECENTION_SPEED));
					perspective.position.interpolate(desiredPosition, VoxarGameplayVariables.TRANSLATION_SMOOTHING_CONSTANT, 
							VoxarGameplayVariables.TRANSLATION_INTERPOLATION_TYPE);
					
					// Look
					int deltaX = Gdx.input.getDeltaX();
					double deltaHor = -deltaX * VoxarGameplayVariables.HORIZONTAL_LOOK_SENSITIVITY;
					perspective.direction.rotate(Vector3.Y, (float)deltaHor);
					int deltaY = Gdx.input.getDeltaY();
					double newVertRotAngle = rotVertAngle;
					double deltaVert = -deltaY * VoxarGameplayVariables.VERTICAL_LOOK_SENSITIVITY;
					newVertRotAngle += deltaVert;
					if(newVertRotAngle >= -VERT_DEGREE_CLAMP && newVertRotAngle <= VERT_DEGREE_CLAMP) {
						rotVertAngle = newVertRotAngle;
						perspective.direction.rotate(new Vector3(-perspective.direction.z, 0, perspective.direction.x), (float)deltaVert);
					}
				}
			}
		});
		
	}
	
	public boolean isFocused() {
		return focused;
	}
	
}
