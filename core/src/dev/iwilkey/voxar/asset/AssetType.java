package dev.iwilkey.voxar.asset;

/**
 * VoxarAssets can belong to one of several types, and thus need to be treated uniquely. 
 * This enumeration lists all supported formats the Voxar engine will parse and load.
 * @author iwilkey
 */
public enum AssetType {
	/**
	 * A collection of three-dimensional vertices, normals, texture coordinates, faces, and materials. 
	 * Official Voxar assets use voxel models in *.obj format.
	 */
	MODEL,
	
	/**
	 * A standard OpenGL ES texture.
	 */
	TEXTURE
}
