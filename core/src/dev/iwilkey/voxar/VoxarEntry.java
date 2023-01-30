package dev.iwilkey.voxar;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;

/**
 * @author iwilkey
 */
public final class VoxarEntry<T> extends Lwjgl3Application {
	
	public VoxarEntry(VoxarEngine<T> engine, VoxarApplicationConfiguration config) {
		super(engine, config);
	}

}
