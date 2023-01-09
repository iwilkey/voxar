package dev.iwilkey.voxar.audio;

/**
 * Voxar engine audio manager.
 * @author iwilkey
 *
 */
public final class Audio {
	
	/*
	 * OpenAL configuration.
	 */
	
	/**
	 * The maximum number of sources that can be played simultaneously.
	 */
	public static final int SIMULTANEOUS_SOURCES = 16;
	
	/**
	 * The audio device buffer size in samples.
	 */
	public static final int BUFFER_SIZE = 512;
	
	/**
	 * The audio device buffer count.
	 */
	public static final int BUFFER_COUNT = 9;
	
}
