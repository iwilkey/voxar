package dev.iwilkey.voxar.asset;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

/**
 * A wrapping of libGDX's AssetManager. Efficiently manages assets during runtime.
 * @author iwilkey
 */
public final class VoxarAssetManager<T> extends AssetManager {
	
	/**
	 * Register an asset for use during some portion of Voxar runtime.
	 * @param path the path to the asset, relative to the "./assets/" directory.
	 * @param type the asset's type.
	 */
	public void register(String path, AssetType type) {
		if(!Gdx.files.internal(path).exists()) {
			System.out.println("You are trying to register an asset of invalid path: \"" + path + "\"");
			Gdx.app.exit();
		}
		if(isLoaded(path)) {
			System.out.println("You are trying to regsiter an asset that is already loaded: \"" + path + "\"");
			return;
		}	
		switch(type) {
			case VOXEL_MODEL:
				
				// Voxify...
				
				break;
			case TEXTURE:
				load(path, Texture.class);
				break;
			default:;
		}
	}
	
	/**
	 * Returns an instance of asset requested, as long as it is registered.
	 * @param path the path to the asset, relative to the "./assets/" directory.
	 * @return the asset.
	 */
	public T retrieve(String path) {
		if(!isLoaded(path)) {
			System.out.println("You are trying to retrieve an asset that is not loaded: \"" + path + "\"");
			Gdx.app.exit();
		}
		return get(path);
	}
	
	/**
	 * Release an asset from memory. Be smart about when to use this!
	 * @param path the path to the loaded asset, relative to the "./assets/" directory.
	 */
	public void release(String path) {
		if(!isLoaded(path)) {
			System.out.println("You are trying to release an asset that is not loaded: \"" + path + "\"");
			return;
		}
		unload(path);
	}
	
	/**
	 * Release all loaded assets from memory. Called automatically when the VoxarEngine is instructed to switch interfaces.
	 */
	public void releaseAll() {
		clear();
	}
	
}
