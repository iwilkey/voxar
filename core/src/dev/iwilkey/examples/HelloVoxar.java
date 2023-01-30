package dev.iwilkey.examples;

import dev.iwilkey.voxar.VoxarEngineInterface;

/**
 * @author iwilkey
 */
public final class HelloVoxar extends VoxarEngineInterface {
	
	@Override
	public void begin() {
		// load assets you want to use.
		// configure objects for use.
	}

	@Override
	public void during() {
		// logic and render requests here.
	}

	@Override
	public void end() {
		// called before switch to new state or engine termination.
		// save stuff here.
	}

}
