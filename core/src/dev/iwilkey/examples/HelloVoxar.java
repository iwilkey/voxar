package dev.iwilkey.examples;

import dev.iwilkey.voxar.VoxarEngineInterface;

/**
 * @author iwilkey
 */
public final class HelloVoxar<T> extends VoxarEngineInterface<T> {
	
	@Override
	public void begin() {
		// load assets you want to use.
		// configure objects for use.
		assets().register("test", null);
		
	}

	@Override
	public void during() {
		// logic and render requests here.
	}

	@Override
	public void end() {
		// called before switch to new state or engine termination.
		// save stuff here.
		assets().release("test");
	}

}
