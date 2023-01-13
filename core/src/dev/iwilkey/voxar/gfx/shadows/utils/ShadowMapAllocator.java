package dev.iwilkey.voxar.gfx.shadows.utils;

import com.badlogic.gdx.graphics.g3d.environment.BaseLight;

/** 
 * Shadow map allocator return texture region for each light
 * @author realitix 
 */
public interface ShadowMapAllocator {

	/** Result of the allocator analyze */
	public class ShadowMapRegion {
		public int x, y, width, height;
	}

	/** Begin the texture allocation */
	public void begin ();

	/** End the texture allocation */
	public void end ();

	/** Find the next texture region for the current light
	 * @param light Current light
	 * @return ShadowMapRegion or null if no more space on texture */
	public ShadowMapRegion nextResult (@SuppressWarnings("rawtypes") BaseLight light);

	/** Return shadow map width.
	 * @return int */
	public int getWidth ();

	/** Return shadow map height.
	 * @return int */
	public int getHeight ();
}
