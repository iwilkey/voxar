package dev.iwilkey.voxar.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;

import dev.iwilkey.voxar.clock.Tickable;
import dev.iwilkey.voxar.settings.VoxarKeyBindings;

/**
 * An implementation of InputProcessor that gives common properties of GLFW input and high-level functions to utilize it. Statically
 * available across all components of the Voxar engine. It should only be extended for custom input functionality, no other InputProcessor
 * should be set.
 * @author iwilkey
 */
public final class StandardInput implements InputProcessor, Tickable {
	
	/**
	 * Input state, just activated.
	 */
	public static final byte ACTIVATE = 0x1 << 0x2;
	
	/**
	 * Input state, current.
	 */
	public static final byte CURRENT = ACTIVATE >> 0x1;
	
	/**
	 * Input state, just disabled.
	 */
	public static final byte DISABLE = ACTIVATE >> 0x2;
	
	/**
	 * Input state, not currently interacted with.
	 */
	public static final byte NONE = ACTIVATE >> 0x3;
	
	/**
	 * The respective states of all possible keys.
	 */
	private static byte[] keyState;
	
	/**
	 * The respective states of all possible cursor buttons.
	 */
	private static byte[] cursorState;
	
	/**
	 * Position of the cursor.
	 */
	private static Vector2 cursorPos;
	
	/**
	 * Position of the scrollwheel.
	 */
	private static Vector2 scrollWheelPos;
	
	public StandardInput() {
		keyState = new byte[0x100];
		cursorState = new byte[0x10];
		cursorPos = new Vector2();
		scrollWheelPos = new Vector2();
	}
	
	/*
	 * Implemented InputProcessor methods.
	 */
	
	@Override
	public boolean keyDown(int keycode) {
		keyState[keycode] = ACTIVATE;
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		keyState[keycode] = DISABLE;
		return true;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		cursorState[pointer] = ACTIVATE;
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		cursorState[pointer] = DISABLE;
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		cursorPos.x = screenX;
		cursorPos.y = screenY;
		return false;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		return false;
	}
	
	/*
	 * Unimplemented InputProcessor methods. 
	 */
	
	@Override
	public boolean keyTyped(char character) { return false; }
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) { return false; }
	
	/*
	 * Standard input high-level functions.
	 */
	
	/**
	 * @param keycode the key interested in.
	 * @return if the key has just been pressed down.
	 */
	public static boolean keyJustDown(int keycode) {
		return keyState[keycode] == ACTIVATE;
	}
	
	/**
	 * @param keycode the key interested in.
	 * @return if the key is currently pressed.
	 */
	public static boolean keyCurrent(int keycode) {
		return keyState[keycode] == CURRENT;
	}
	
	/**
	 * @param keycode the key interested in.
	 * @return if the key has just been released.
	 */
	public static boolean keyJustUp(int keycode) {
		return keyState[keycode] == DISABLE;
	}
	
	/**
	 * @param pointer the button interested in.
	 * @return if the cursor button has just been pressed down.
	 */
	public static boolean cursorJustDown(int pointer) {
		return cursorState[pointer] == ACTIVATE;
	}
	
	/**
	 * @param pointer the button interested in.
	 * @return if the cursor button is currently pressed.
	 */
	public static boolean cursorCurrent(int pointer) {
		return cursorState[pointer] == CURRENT;
	}
	
	/**
	 * @param pointer the button interested in.
	 * @return if the cursor button has just been released.
	 */
	public static boolean cursorJustUp(int pointer) {
		return cursorState[pointer] == DISABLE;
	}
	
	/**
	 * @return the current position of the cursor on the screen, in pixels.
	 */
	public static Vector2 getCursorPosition() {
		return cursorPos;
	}
	
	/**
	 * @return the current position of the scrollwheel.
	 */
	public static Vector2 getScrollwheelPosition() {
		return scrollWheelPos;
	}

	@Override
	public void tick() {
		if(keyJustDown(VoxarKeyBindings.EXIT_GAME)) 
			Gdx.app.exit();
		// Update the states of input data structures.
		for(int i = 0; i < 256; i++) {
			
			// Handle keys.
			switch(keyState[i]) {
				case DISABLE:
					keyState[i] = NONE;
					break;
				case ACTIVATE:
					keyState[i] >>= 0x1;
					break;
			}
			
			// Handle cursor.
			if(i < 16) {
				switch(cursorState[i]) {
					case DISABLE:
						cursorState[i] = NONE;
						break;
					case ACTIVATE:
						cursorState[i] >>= 0x1;
						break;
				}
			}
		}
		// Reset scrollwheel.
		scrollWheelPos = Vector2.Zero;
	}
}
