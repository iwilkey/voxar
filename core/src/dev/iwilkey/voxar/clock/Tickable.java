package dev.iwilkey.voxar.clock;

/**
 * Interface for objects that should have logic applied before rendering.
 * @author iwilkey
 */
public interface Tickable {
	/**
	 * Called directly before rendering is to occur.
	 */
	public void tick();
}
