package dev.iwilkey.voxar;

import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import dev.iwilkey.voxar.audio.Audio;
import dev.iwilkey.voxar.gfx.VoxarRenderer;

/**
 * Default application environment configuration for any Voxar application.
 * @author iwilkey
 */
public class VoxarApplicationConfiguration extends Lwjgl3ApplicationConfiguration {
	
	/**
	 * 5% of the primary display dimensions, the smallest a window can be in windowed mode.
	 */
	public static final float MIN_SCREEN_WIDTH_RATIO = 0.05f;
	
	/**
	 * 35% of the primary display dimensions, the default size of a window in windowed mode.
	 */
	public static final float DEFAULT_SCREEN_WIDTH_RATIO = 0.35f;
	
	/**
	 * Default aspect ratio of a Voxar application.
	 */
	public static final float DEFAULT_ASPECT_RATIO = (16 / 9.0f);

	/**
	 * Voxar application configuration.
	 * @param appName the name of the Voxar engine implementation.
	 */
	public VoxarApplicationConfiguration(String appName, boolean fullscreen) {
		
		// Set window title.
		this.setTitle(appName);
		
		// Set FPS target.
		this.setForegroundFPS(120);
		
		DisplayMode display = Lwjgl3ApplicationConfiguration.getDisplayMode();
		// Set fullscreen mode, if desired.
		if(fullscreen) 
			this.setFullscreenMode(display);
		else {
			// Set windowed mode size and minimum size.
			final int width = (int)(display.width * DEFAULT_SCREEN_WIDTH_RATIO);
			final int height =(int)(width * (1 / DEFAULT_ASPECT_RATIO));
			this.setWindowedMode(width, height);
		}
		
		// Set minimum dimensions in windowed mode.
		final int minHostScreenWidth = (int)(display.width * MIN_SCREEN_WIDTH_RATIO);
		final int minHostScreenHeight = (int)(minHostScreenWidth * (1 / DEFAULT_ASPECT_RATIO));
		this.setWindowSizeLimits(minHostScreenWidth, minHostScreenHeight, -1, -1);
		
		// Resizable by default.
		this.setResizable(true);
		
		// Try to set the window visible later to try and combat the focusing issue referenced on Trello.
		this.setInitialVisible(false);
		
		// Set up OpenGL back buffer.
		this.setBackBufferConfig(VoxarRenderer.RED_BITS, 
				VoxarRenderer.GREEN_BITS, 
				VoxarRenderer.BLUE_BITS, 
				VoxarRenderer.ALPHA_BITS, 
				VoxarRenderer.DEPTH_BITS, 
				VoxarRenderer.STENCIL_BITS, 
				VoxarRenderer.MSAA_SAMPLES);
		
		// Enable OpenGL debug output.
		this.enableGLDebugOutput(true, System.out);
		
		// Configure OpenAL.
		this.setAudioConfig(Audio.SIMULTANEOUS_SOURCES, 
				Audio.BUFFER_SIZE, 
				Audio.BUFFER_COUNT);
		
	}
	
}
