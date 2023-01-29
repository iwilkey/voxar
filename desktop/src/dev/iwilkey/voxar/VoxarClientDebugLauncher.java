package dev.iwilkey.voxar;

/**
 * Client launcher for debugging the Voxar engine.
 * @author iwilkey
 */
public class VoxarClientDebugLauncher {
	public static void main(String[] args) {
		VoxarApplicationConfiguration config = new VoxarApplicationConfiguration("Voxar Engine [DEBUG CLIENT]", false);
		VoxarEngine engine =  new VoxarEngine();
		new VoxarApplication(engine, config);
	}	
}
