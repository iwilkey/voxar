package dev.iwilkey.voxar.asset;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.utils.Array;

import dev.iwilkey.voxar.asset.exception.VoxarAssetNotFound;
import dev.iwilkey.voxar.model.VoxelModel;
import dev.iwilkey.voxar.model.VoxelModelLoader;

/**
 * A reference to an asset of type AssetType in the ./assets/ directory.
 * @author iwilkey
 */
public class VoxarAsset {
	
	/**
	 * The name of the asset.
	 */
	private String name;
	
	
	/**
	 * the path of the asset relative to the ./assets/ directory.
	 */
	private String path;
	
	/**
	 * The type of the asset.
	 */
	private AssetType type;
	
	public VoxarAsset(String name, String path, AssetType type) {
		this.name = name;
		this.path = path;
		this.type = type;
	}
	
	/**
	 * Instruct the VoxarEngineState's AssetManager to load in this asset.
	 * @param stateAssetManager the state's asset manager.
	 * @throws VoxarAssetNotFound an exception if this asset can not be found in the ./assets/ directory.
	 */
	public void load(AssetManager stateAssetManager) throws VoxarAssetNotFound {
		if(!Gdx.files.internal(path).exists())
			throw new VoxarAssetNotFound(path);
		switch(type) {
			case MODEL:
				stateAssetManager.load(path, Model.class);
				break;
			case TEXTURE:
				stateAssetManager.load(path, Texture.class);
				break;
			default: return;
		}
	}
	
	/**
	 * Back-end utility function all referenced models must go through. Do not use this unless you know what you're doing.
	 */
	public Model voxify(AssetManager stateAssetManager, Array<VoxelModel> models) {
		Model model = VoxelModelLoader.voxify(stateAssetManager.get(path), name);
		models.add(new VoxelModel(name, path, model));
		return model;
	}
	
	/**
	 * @return the name of the asset.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return the path of the asset relative to the ./assets/ directory.
	 */
	public String getPath() {
		return path;
	}
	
	/**
	 * @return the type of the asset.
	 */
	public AssetType getType() {
		return type;
	}

}
