package dev.iwilkey.voxar;

import dev.iwilkey.voxar.asset.VoxarAssetManager;

/**
 * @author iwilkey
 */
@SuppressWarnings("rawtypes")
public abstract class VoxarEngineInterface<T> {
	
	private VoxarEngine engine;
	
	public VoxarEngineInterface() {
		this.engine = null;
	}
	
	public abstract void begin();
	public abstract void during();
	public abstract void end();
	
	/**
	 * Gives this interface access to the Voxar engine's asset manager.
	 * @return the asset manager.
	 */
	@SuppressWarnings("unchecked")
	public VoxarAssetManager<T> assets() {
		return engine.getAssetManager();
	}
	
	/**
	 * Gives this interface access to the Voxar engine. Should not be called by any object besides the engine itself.
	 * @param engine the engine.
	 */
	protected void setEngine(VoxarEngine engine) {
		this.engine = engine;
	}
	
}
