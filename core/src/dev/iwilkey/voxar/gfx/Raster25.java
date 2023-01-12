package dev.iwilkey.voxar.gfx;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g3d.decals.Decal;

/**
 * A implementation of Raster that can be rendered in 3D space by the RasterRenderer. Raster25 stands for Raster 2.5D, indicating it is still a 2D texture but it is rendered in 3D space.
 * @author iwilkey
 */
public final class Raster25 extends Raster {
	
	private Decal decal;
	private boolean billboard;
	
	public Raster25(FileHandle file) {
		super(file);
		decal = Decal.newDecal(1, 1, getBindedRaster(), true);
		billboard = true;
	}
	
	/**
	 * @return the binded raster capable of being rendered in 3D space.
	 */
	public Decal getDecal() {
		return decal;
	}
	
	/**
	 * Set whether or not the Raster25 should always look at the VoxelSpacePerspective.
	 * @param billboard true or false.
	 */
	public void setBillboard(boolean billboard) {
		this.billboard = billboard;
	}
	
	/**
	 * @return if the Raster25 should always look at the VoxelSpacePerspective.
	 */
	public boolean shouldBillboard() {
		return billboard;
	}

}
