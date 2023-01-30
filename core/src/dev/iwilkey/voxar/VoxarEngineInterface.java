package dev.iwilkey.voxar;

/**
 * @author iwilkey
 */
@SuppressWarnings("rawtypes")
public abstract class VoxarEngineInterface {
	
	@SuppressWarnings("unused")
	private VoxarEngine engine;
	
	public VoxarEngineInterface() {
		this.engine = null;
	}
	
	public abstract void begin();
	public abstract void during();
	public abstract void end();
	
	protected void setEngine(VoxarEngine engine) {
		this.engine = engine;
	}
	
}
