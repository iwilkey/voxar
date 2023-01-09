package dev.iwilkey.voxar;

import dev.iwilkey.voxar.state.VoxarDebugWorld;

/**
 * Client launcher for debugging the Voxar engine.
 * @author iwilkey
 */
public class VoxarClientDebugLauncher {
	public static void main(String[] args) {
		VoxarApplicationConfiguration config = new VoxarApplicationConfiguration("Voxar Engine [DEBUG CLIENT]", true);
		VoxarEngine engine =  new VoxarEngine(new VoxarDebugWorld());
		new VoxarApplication(engine, config);
	}	
}
