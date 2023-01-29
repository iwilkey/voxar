package dev.iwilkey.voxar.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;

/**
 * @author iwilkey
 * @param <T> the type being registered.
 */
public abstract class Registrar<T> {
	
	private HashMap<Long, RegistrarProvider<? extends T>> register;
	private Iterator<Entry<Long, RegistrarProvider<? extends T>>> iterator;
	
	public Registrar() {
		register = new HashMap<>();
		iterator = register.entrySet().iterator();
	}
	
	public void register(RegistrarProvider<T> obj) {
		register.put(obj.getUID(), obj);
	}
	
	public void abort(RegistrarProvider<T> obj) {
		register.remove(obj.getUID());
	}
	
	protected long getUniqueIdentifier() {
		while(true) {
			long guess = new Random().nextLong();
			if(!register.containsKey(guess))
				return guess;
		}
	}
	
	public void update() {
		while(iterator.hasNext()) {
			@SuppressWarnings("unchecked")
			RegistrarProvider<T> entry = (RegistrarProvider<T>)iterator.next();
			entry.update();
		}
	}
	
	public HashMap<Long, RegistrarProvider<? extends T>> getRegister() {
		return register;
	}
	
	public RegistrarProvider<? extends T> getRegistrarProvider(long uid) {
		return register.get(uid);
	}
	
}
