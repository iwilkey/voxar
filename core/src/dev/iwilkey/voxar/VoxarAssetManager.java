package dev.iwilkey.voxar;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;

/**
 * @author iwilkey
 */
public final class VoxarAssetManager extends AssetManager {
	
	public void registerAsset(String uniqueName, String... path) {
		if(path.length == 0) {
			System.out.println("You have called register() method in VoxarAssetManager but did not supply any path.");
			return;
		}
		String assetPath = "";
		for(String p : path)
			assetPath += p + "/";
		FileHandle assetHandle = Gdx.files.local(assetPath);
	}
	
}
