package dev.iwilkey.voxar;

import org.lwjgl.glfw.GLFW;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;

import dev.iwilkey.voxar.clock.Tickable;
import dev.iwilkey.voxar.gfx.Renderer;
import dev.iwilkey.voxar.input.StandardInput;
import dev.iwilkey.voxar.settings.KeyBinding;
import dev.iwilkey.voxar.state.VoxarEngineState;
import imgui.ImGui;

/**
 * This object gives a Voxar developer access to the graphics pipeline, game logic, sound engine, physics engine, 
 * and particle effects of the Voxar engine.
 * 
 * Real-time engine development status (Trello): https://trello.com/b/Q4P5kXKN/voxar-engine-trello! If you are making changes to the source code and
 * plan to submit a merge request, you must outline your changes with the link above.
 * 
 * @author iwilkey
 */
public final class VoxarEngine extends ApplicationAdapter implements Tickable {
	
	/*
	 * The Voxar version system is comprised of three different delineations of version. Please see below the differences.
	 */
	
	/*
	 * GIT_VERSION is updated whenever a branch is committed to. It is formatted like <git branch>-<push #>.
	 * Please note that the push number is changed directly before a branches commit-push sequence.
	 */
	public static final String GIT_VERSION = "master-p12";
	
	/**
	 * ENGINE_VERSION is appended to whenever an update to the engine is released to GitHub or executable. An engine
	 * update is defined as any modification, addition, subtraction, or optimization to Voxar that does not directly
	 * effect gameplay.
	 */
	public static final String ENGINE_VERSION = "0.12";
	
	/**
	 * True if the Voxar engine is in an "idle" state, meaning there is no active state dictating its behavior.
	 */
	public static boolean ENGINE_IDLE;
	
	/**
	 * True if the Voxar engine notices it is running in the host system's background. For example, when the Voxar application is a minimized window.
	 */
	public static boolean ENGINE_PAUSED;
	
	/**
	 * The Voxar engine's current operating State.
	 */
	private VoxarEngineState state;
	
	/**
	 * The Voxar engine's 2D, 2.5D, 3D, and GUI renderer.
	 */
	private Renderer renderer;
	
	/**
	 * The standard input processor of the Voxar engine.
	 */
	private StandardInput input;
	
	/**
	 * Invoke the VoxarEngine with an entry state reference.
	 * @param state the entry state.
	 */
	public VoxarEngine(VoxarEngineState state) {
		// Initialize important variables.
		this.state = state;
		ENGINE_IDLE = true;
		ENGINE_PAUSED = false;
		renderer = null;
		input = null;
	}

	@Override
	public void create() {
		// Create renderer.
		renderer = new Renderer();
		// Create and apply input processor.
		input = new StandardInput(); 
		Gdx.input.setInputProcessor(input);
		// Set up preferences...
		new KeyBinding();
		// Begin entry state.
		this.state.init(this);
		// Show the window.
		GLFW.glfwShowWindow(renderer.getWindowHandle());
	}
	
	@Override
	public void tick() {
		// State will never be null when this function is called.
		state.tick();
		input.tick();
	}

	@Override
	public void render() {
		
		// Handle input and differentiate ImGui vs engine input. TODO: workaround initial window focusing ImGui issue!
		if(ImGui.getIO().getWantCaptureMouse() || ImGui.getIO().getWantCaptureKeyboard()) {
			if(Gdx.input.getInputProcessor() != null)
				Gdx.input.setInputProcessor(null);
		} else {
			if(Gdx.input.getInputProcessor() == null)
				Gdx.input.setInputProcessor(input);
		}
		
		if(this.state == null) {
			ENGINE_IDLE = true;
		} else {
			ENGINE_IDLE = false;
			if(!this.state.isReady()) {
				this.state.load();
			} else {
				tick();
			}
		}
		renderer.render(this.state);
		
	}
	
	@Override
	public void resize(int width, int height) {
		renderer.windowResizeCallback(width, height);
		if(this.state != null)
			this.state.windowResizeCallback(width, height);
	}
	
	/**
	 * Instruct the engine to load, begin, and tick and render a constructed State.
	 * @param state the state to switch to.
	 */
	public void setState(VoxarEngineState state) {
		renderer.resetCameraGroupStrategy();
		if(this.state != null) {
			this.state.end();
			this.state.unloadAssets();
			this.state.dispose();
		}
		this.state = state;
		if(this.state != null) {
			this.state.init(this);
			this.state.load();
		}
	}
	
	@Override
	public void pause() {
		ENGINE_PAUSED = true;
	}
	
	@Override
	public void resume() {
		ENGINE_PAUSED = false;
	}
	
	@Override
	public void dispose() {
		if(state != null)
			state.dispose();
		renderer.dispose();
	}
	
}
