package dev.iwilkey.voxar.entity;

/**
 * Interface for any object, mainly a VoxelEntity, that is capable of existing or surviving. 
 * @author iwilkey
 */
public interface Viable {
	/**
	 * Called the instant an entity becomes active inside of a VoxelSpace.
	 */
	public void spawn();
	
	/**
	 * Called repeatedly as long as an entity is active inside of a VoxelSpace.
	 */
	public void life();
	
	/**
	 * Called the instant an entity becomes non-existent inside of a VoxelSpace.
	 */
	public void death();
}
