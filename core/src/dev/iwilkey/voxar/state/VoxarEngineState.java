package dev.iwilkey.voxar.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import dev.iwilkey.voxar.VoxarEngine;
import dev.iwilkey.voxar.asset.AssetType;
import dev.iwilkey.voxar.asset.VoxarAsset;
import dev.iwilkey.voxar.asset.exception.VoxarAssetNotFound;
import dev.iwilkey.voxar.asset.exception.VoxarAssetNotReferenced;
import dev.iwilkey.voxar.clock.Tickable;
import dev.iwilkey.voxar.gfx.RenderResizable;
import dev.iwilkey.voxar.model.VoxelModel;
import dev.iwilkey.voxar.space.VoxelSpace;

/**
 * A VoxarEngineState is an abstract implementation of the Voxar engine's capabilities. When a concrete extension of this
 * class is created, the Voxar engine can use it to dictate how it behaves.
 * @author iwilkey
 */
public abstract class VoxarEngineState implements Disposable, Tickable, RenderResizable {
	
	/**
	 * A list of assets that exist in the "dev.iwilkey.Voxar.asset.VoxarAsset.java" enumeration.
	 * If they are in this list, that means they have been loaded by the state's AssetManager and can be used
	 * at any time during the state's lifetime.
	 */
	private VoxarAsset[] referencedAssets;
	
	/**
	 * All loaded voxel models in current state.
	 */
	private Array<VoxelModel> loadedVoxelModels;
	
	/**
	 * The name of the state.
	 */
	private String name;
	
	/**
	 * This boolean will be true if and only if the AssetManager has successfully loaded all referenced assets in referencedAssets.
	 */
	private boolean ready;
	
	/**
	 * State's access to the Voxar engine.
	 */
	private VoxarEngine engine;
	
	/**
	 * AssetManager belonging to the state constructed. If the state is active and ready = true, all assets referenced in construction will have been loaded
	 * and are ready for use at any time.
	 */
	private AssetManager assets;
	
	/**
	 * This is an optional variable in a state. A VoxelSpace is essentially just a 3D perspective environment with dynamic physics. If this
	 * variable is not null, the renderer will automatically render all RenderableProviders active in the world. If null, no problem.
	 */
	private VoxelSpace space;
	
	public VoxarEngineState(String name, VoxarAsset... referencedAssets) {
		this.name = name;
		this.referencedAssets = referencedAssets;
		loadedVoxelModels = new Array<>();
		this.ready = false;
		this.engine = null;
		this.assets = null;
		this.space = null;
	}
	
	/**
	 * Called within the context of a running Voxar engine. Verifies and loads state assets.
	 * @param engine the voxar engine.
	 */
	public void init(VoxarEngine engine) {
		this.engine = engine;
		assets = new AssetManager();
		for(VoxarAsset a : referencedAssets) {
			try {
				a.load(assets);
			} catch (VoxarAssetNotFound e) {
				e.printStackTrace();
				Gdx.app.exit();
			}
		}
	}

	/**
	 * Loads an engine state's required, unloaded assets (if any.) It will call "begin()" when finished.
	 */
	public final void load() {
		if(assets.isFinished()) {
			for(VoxarAsset a : referencedAssets)
				if(a.getType() == AssetType.MODEL)
					a.voxify(assets, loadedVoxelModels);
			ready = true;
			begin();
			return;
		}
		assets.update();
	}

	@Override
	public final void tick() {
		if(hasVoxelSpace())
			space.tick();
		process();
	}
	
	/**
	 * Called when a states assets have been loaded and it is ready to begin.
	 */
	public abstract void begin();
	/**
	 * Called when any Dear ImGui elements should be dealt with.
	 */
	public abstract void gui();
	/**
	 * Called every tick. This is where engine logic, input, and rendering calls should go.
	 */
	public abstract void process();
	/**
	 * Called when the engine has been instructed to change to a different state, or terminate.
	 */
	public abstract void end();
	
	/**
	 * Tells the engine if the state is ready to be ticked and rendered.
	 * @return if the state's AssetManager has loaded all required assets.
	 */
	public boolean isReady() {
		return ready;
	}
	
	/**
	 * Return a VoxelModel of the referenced path. Must be referenced in the construction of the VoxarEngineState.
	 * @param path the path to the model, relative to the ./assets/ directory.
	 * @return the loaded voxel model.
	 * @throws VoxarAssetNotReferenced
	 */
	public VoxelModel getLoadedVoxelModel(String path) throws VoxarAssetNotReferenced {
		// Check if referenced...
		boolean found = false;
		for(VoxarAsset a : referencedAssets) {
			if(path.equals(a.getPath())) {
				found = true;
				break;
			}
		}
		if(!found)
			throw new VoxarAssetNotReferenced(path);
		// Find it...
		for(VoxelModel m : loadedVoxelModels) {
			if(m.getPath().equals(path))
				return m;
		}
		// This would never happen, because it has to be loaded if it's in referencedAssets.
		return null;
	}
	
	/**
	 * Gives access to the Voxar engine's assets, input, rendering, sound, and state methods.
	 * @return the Voxar engine object (core).
	 */
	public VoxarEngine getEngine() {
		return engine;
	}
	
	/**
	 * @return a list of the state's referenced, and if active and ready, loaded assets.
	 */
	public VoxarAsset[] getReferencedAssets() {
		return referencedAssets;
	}
	
	/**
	 * Use this method to set the space variable. If you pass in a VoxelSpace, your state is now three dimensional.
	 * @param world the VoxelSpace object to act as the 3D world.
	 */
	protected void setVoxelSpace(VoxelSpace world) {
		this.space = world;
	}
	
	/**
	 * @return the AssetManager object where, if state is active and ready, all assets are loaded and ready to be used.
	 */
	public AssetManager getAssetManager() {
		return assets;
	}

	/**
	 * @return the progress of the AssetManager at the beginning of a state as it loads referenced assets.
	 */
	public float getAssetLoadPercentage() {
		return assets.getProgress();
	}
	
	/**
	 * @return if the state has an active VoxelSpace (three dimensional).
	 */
	public boolean hasVoxelSpace() {
		return space != null;
	}
	
	/**
	 * @return the state's VoxelSpace object data.
	 */
	public VoxelSpace getVoxelSpace() {
		return space;
	}

	/**
	 * This method will dispose of all loaded assets. This should be used when switching a state, as the resources will no longer need to be loaded.
	 */
	public void unloadAssets() {
		assets.clear();
	}
	
	/**
	 * This method gives the name of the state.
	 * @return the name of the state.
	 */
	public String getStateName() {
		return name;
	}
	
	@Override
	public final void windowResizeCallback(int nw, int nh) {
		if(space != null)
			space.windowResizeCallback(nw, nh);
	}
	
	@Override
	public final void dispose() {
		assets.dispose();
	}
	
}
