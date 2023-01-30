package dev.iwilkey.voxar.utils;

import com.badlogic.gdx.utils.Disposable;

/**
 * @author iwilkey
 * @param <T> the type to be registered.
 */
public abstract class Task<T> implements Disposable {
	
	private final long uid;
	private final TaskManager<T> manager;
	
	public Task(TaskManager<T> manager) {
		this.manager = manager;
		uid = manager.register(this);
	}
	
	public abstract void update();
	
	public long getUID() {
		return uid;
	}
	
	public TaskManager<T> getManager() {
		return manager;
	}
	
}
