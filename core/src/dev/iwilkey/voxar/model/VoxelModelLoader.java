package dev.iwilkey.voxar.model;

import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

/**
 * Voxel model utilities.
 * @author iwilkey
 */
public final class VoxelModelLoader {
	
	/**
	 * A Model utility function that orients and scales voxel *.obj data for ease-of-use and physics engine support. 
	 * @param model referenced Model data.
	 * @param tag name of referenced resource to organize Model nodes.
	 * @return a corrected voxel model ready to be directly used by the Terrafort engine.
	 */
	public static Model voxify(Model model, String tag) {
		ModelBuilder builder = new ModelBuilder();
		builder.begin();
		builder.node().id = tag;
		builder.part(model.meshParts.first(), new Material(model.materials.get(0)));
		model = builder.end();
		model.getNode(tag).translation.add(calculateCenterOffset(model));
		model.calculateTransforms();
		return model;
	}
	
	/**
	 * @param model referenced Model data.
	 * @return the normalized voxel model dimensions in VoxelSpace.
	 */
	public static Vector3 getModelDimensions(Model model) {
		BoundingBox box = new BoundingBox();
		model.calculateBoundingBox(box);
		Vector3 out = new Vector3();
		box.getDimensions(out);
		return out;
	}
	
	/**
	 * @param model referenced Model data.
	 * @return returns the correction amount to make sure the model specified is correctly encapsulated.
	 */
	public static Vector3 calculateCenterOffset(Model model) {
		Vector3 dim = getModelDimensions(model);
		Vector3 ret = new Vector3(0, -(dim.y / 2f), 0);
		return ret;
	}
	
}
