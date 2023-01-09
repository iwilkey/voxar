package dev.iwilkey.voxar.model;

import com.badlogic.gdx.graphics.g3d.Model;

/**
 * A data structure to encapsulate data pertaining to a loaded Voxar model, voxifed and ready to be used in any Renderable context.
 * @author iwilkey
 */
public class VoxelModel {
	
	/**
	 * The name of the model.
	 */
	private String name;
	
	/**
	 * The path to the model, relative to the ./assets/ directory.
	 */
	private String path;
	
	/**
	 * Model data loaded in memory.
	 */
	private Model model;
	
	public VoxelModel(String name, String path, Model model) {
		this.path = path;
		this.name = name;
		this.model = model;
	}
	
	/**
	 * @return the name of the model.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return the path of the model, relative to the ./assets/ directory.
	 */
	public String getPath() {
		return path;
	}
	
	/**
	 * @return the model data, loaded in memory.
	 */
	public Model getModel() {
		return model;
	}
	
}
