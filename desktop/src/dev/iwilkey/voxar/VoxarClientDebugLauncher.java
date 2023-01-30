package dev.iwilkey.voxar;

import dev.iwilkey.examples.HelloVoxar;

/**
 * Client launcher for debugging the Voxar engine.
 * @author iwilkey
 */
@SuppressWarnings("rawtypes")
public class VoxarClientDebugLauncher<T> {
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		VoxarApplicationConfiguration config = new VoxarApplicationConfiguration("Voxar Engine [DEBUG CLIENT]", false);
		HelloVoxar entryState = new HelloVoxar();
		VoxarEngine engine =  new VoxarEngine(entryState);
		new VoxarEntry(engine, config);
	}	
}
