package dev.iwilkey.voxar;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;

/**
 * The instantiation of this object creates a Voxar runtime capable of processing and rendering VoxarEngineStates.
 * @author iwilkey
 */
public final class VoxarApplication extends Lwjgl3Application {
	
	/**
	 * To begin a Voxar application, you must supply the engine and a configuration.
	 * @param engine an instantiated, running engine.
	 * @param config an application configuration.
	 */
	public VoxarApplication(VoxarEngine engine, VoxarApplicationConfiguration config) {
		super(engine, config);
	}

}
