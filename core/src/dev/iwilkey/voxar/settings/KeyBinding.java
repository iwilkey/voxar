package dev.iwilkey.voxar.settings;

import java.util.HashMap;

/**
 * Customizable key bindings for a Voxar application. Used globally.
 * @author iwilkey
 */
public final class KeyBinding {
	
	/**
	 * Map of VoxarApplication bindings.
	 */
	private static HashMap<String, Integer> bindings;
	
	public KeyBinding() {
		bindings = new HashMap<>();
	}
	
	/**
	 * Set a binding by name.
	 * @param name the name.
	 * @param key the key.
	 */
	public static void setBinding(String name, int key) {
		bindings.put(name, key);
	}
	
	/**
	 * @param binding the name of the binding.
	 * @return the key associated.
	 */
	public static int getBinding(String binding) {
		return bindings.get(binding);
	}

}
