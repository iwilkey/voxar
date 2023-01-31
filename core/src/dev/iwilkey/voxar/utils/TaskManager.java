package dev.iwilkey.voxar.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;

import com.badlogic.gdx.utils.Disposable;

import dev.iwilkey.voxar.VoxarEngine;

/**
 * Manages a mapping of active {@link Task}s.
 * @author iwilkey
 * @param <T> the task type registered.
 */
@SuppressWarnings("unchecked")
public abstract class TaskManager<T> implements Disposable {
	
	private final long uid;
	
	private HashMap<Long, Task<? extends T>> register;
	private Iterator<Entry<Long, Task<? extends T>>> registerIterator;
	
	public TaskManager(VoxarEngine<T> engine) {
		uid = engine.registerTaskManager(this);
		register = new HashMap<>();
		registerIterator = register.entrySet().iterator();
	}
	
	/**
	 * Register a new {@link Task} 
	 * @param obj
	 * @return
	 */
	public long register(Task<T> obj) {
		long uid = getUniqueIdentifier();
		register.put(uid, obj);
		return uid;
	}
	
	public void abort(Task<T> obj) {
		register.remove(obj.getUID());
	}
	
	public void update() {
		while(registerIterator.hasNext()) {
			Task<? extends T> entry = (Task<? extends T>)registerIterator.next();
			entry.update();
		}
	}
	
	private long getUniqueIdentifier() {
		while(true) {
			long guess = new Random().nextLong();
			if(!register.containsKey(guess))
				return guess;
		}
	}
	
	public HashMap<Long, Task<? extends T>> getRegister() {
		return register;
	}
	
	public Task<? extends T> getRegistrarProvider(long uid) {
		return register.get(uid);
	}
	
	public long getUID() {
		return uid;
	}
	
	@Override
	public void dispose() {
		while(registerIterator.hasNext()) {
			Task<? extends T> entry = (Task<? extends T>)registerIterator.next();
			entry.dispose();
		}
	}	
}
