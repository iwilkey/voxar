package dev.iwilkey.voxar;

import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import dev.iwilkey.voxar.audio.Audio;
import dev.iwilkey.voxar.gfx.Renderer;

/**
 * @author iwilkey
 */
public class VoxarApplicationConfiguration extends Lwjgl3ApplicationConfiguration {

	public static final float MIN_SCREEN_WIDTH_RATIO = 0.05f;
	public static final float DEFAULT_SCREEN_WIDTH_RATIO = 0.50f;
	public static final float DEFAULT_ASPECT_RATIO = (16 / 9.0f);

	/**
	 * Voxar application configuration.
	 * @param appName the name of the Voxar engine implementation.
	 */
	public VoxarApplicationConfiguration(String appName, boolean fullscreen) {
		
		this.setTitle(appName);
		this.setForegroundFPS(120);
		DisplayMode display = Lwjgl3ApplicationConfiguration.getDisplayMode();
		
		if(fullscreen) 
			this.setFullscreenMode(display);
		else {
			final int width = (int)(display.width * DEFAULT_SCREEN_WIDTH_RATIO);
			final int height =(int)(width * (1 / DEFAULT_ASPECT_RATIO));
			this.setWindowedMode(width, height);
		}

		final int minHostScreenWidth = (int)(display.width * MIN_SCREEN_WIDTH_RATIO);
		final int minHostScreenHeight = (int)(minHostScreenWidth * (1 / DEFAULT_ASPECT_RATIO));
		this.setWindowSizeLimits(minHostScreenWidth, minHostScreenHeight, -1, -1);
		this.setResizable(true);
		this.setInitialVisible(false);
		this.setBackBufferConfig(Renderer.RED_BITS, 
				Renderer.GREEN_BITS, 
				Renderer.BLUE_BITS, 
				Renderer.ALPHA_BITS, 
				Renderer.DEPTH_BITS, 
				Renderer.STENCIL_BITS, 
				Renderer.MSAA_SAMPLES);
		this.enableGLDebugOutput(true, System.out);
		this.setAudioConfig(Audio.SIMULTANEOUS_SOURCES, 
				Audio.BUFFER_SIZE, 
				Audio.BUFFER_COUNT);
	}
	
}
