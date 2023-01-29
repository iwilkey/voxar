package dev.iwilkey.voxar.utils;

/**
 * @author iwilkey
 * @param <T> the type to be registered.
 */
public abstract class RegistrarProvider<T> {
	
	private Registrar<T> registrar;
	private long uid;
	
	public RegistrarProvider(Registrar<T> registrar) {
		uid = registrar.getUniqueIdentifier();
		registrar.register(this);
	}
	
	public abstract void update();
	
	public long getUID() {
		return uid;
	}
	
	public Registrar<T> getRegistrar() {
		return registrar;
	}
	
}
