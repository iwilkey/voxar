package dev.iwilkey.voxar.noise;

/**
 * An implementation of Ken Perlin's gradient noise generation. Supports 1D, 2D, and 3D noise and will generate it all from a single seed.
 * @author alksily (original author)
 * @author iwilkey
 */
public class Perlin {
	
	/**
	 * Default map size.
	 */
	private static final double DEFAULT_SCALE = 35.0f;
	
	/**
	 * Ken's original permutation array for 3D generation.
	 */
	private int[] permutation;
	
	private double scale;
	private double seed;
	
	/**
	 * Generate noise using default scale.
	 * @param seed the seed.
	 */
	public Perlin(double seed) {
		this.seed = seed;
		scale = DEFAULT_SCALE;
		initPermutationArray();
	}
	
	/**
	 * Generate noise using custom scale.
	 * @param seed the seed.
	 * @param scale the scale.
	 */
	public Perlin(double seed, double size) {
		this.seed = seed;
		this.scale = size;
		initPermutationArray();
	}
	
	/**
	 * Initialize a permutation array (for 3D noise generation) using Ken's original permutation list.
	 */
	private void initPermutationArray() {
		final int[] kensList = new int[] {
			151, 160, 137, 91, 90, 15, 131, 13, 201, 95, 96, 53, 194, 233, 7, 225, 140, 36, 
            103, 30, 69, 142, 8, 99, 37, 240, 21, 10, 23, 190, 6, 148, 247, 120, 234, 75, 0, 
            26, 197, 62, 94, 252, 219, 203, 117, 35, 11, 32, 57, 177, 33, 88, 237, 149, 56, 
            87, 174, 20, 125, 136, 171, 168, 68, 175, 74, 165, 71, 134, 139, 48, 27, 166, 
            77, 146, 158, 231, 83, 111, 229, 122, 60, 211, 133, 230, 220, 105, 92, 41, 55, 
            46, 245, 40, 244, 102, 143, 54, 65, 25, 63, 161, 1, 216, 80, 73, 209, 76, 132, 
            187, 208, 89, 18, 169, 200, 196, 135, 130, 116, 188, 159, 86, 164, 100, 109, 
            198, 173, 186, 3, 64, 52, 217, 226, 250, 124, 123, 5, 202, 38, 147, 118, 126, 
            255, 82, 85, 212, 207, 206, 59, 227, 47, 16, 58, 17, 182, 189, 28, 42, 223, 183, 
            170, 213, 119, 248, 152, 2, 44, 154, 163, 70, 221, 153, 101, 155, 167, 43, 
            172, 9, 129, 22, 39, 253, 19, 98, 108, 110, 79, 113, 224, 232, 178, 185, 112, 
            104, 218, 246, 97, 228, 251, 34, 242, 193, 238, 210, 144, 12, 191, 179, 162, 
            241, 81, 51, 145, 235, 249, 14, 239, 107, 49, 192, 214, 31, 181, 199, 106, 
            157, 184, 84, 204, 176, 115, 121, 50, 45, 127, 4, 150, 254, 138, 236, 205, 
            93, 222, 114, 67, 29, 24, 72, 243, 141, 128, 195, 78, 66, 215, 61, 156, 180
		};
		permutation = new int[512];
		for(int i = 0; i < 256; i++) 
			permutation[256 + i] = permutation[i] = kensList[i];
	}
	
	/**
	 * 1D Perlin noise sample at (x).
	 * @param x x coordinate.
	 * @return 1D Perlin noise at coordinate x.
	 */
	public double getNoise1(double x) {
		double ret = 0.0;
		double currSize = scale;
		double startingSize = currSize;
		while(currSize >= 1.0) {
			ret += perlin((x / scale), (0f / scale), (0f / scale)) * scale;
			currSize /= 2.0f;
		}
		return ret / startingSize;
    }
	
	/**
	 * 2D Perlin noise sample at (x, y).
	 * @param x x coordinate.
	 * @param z z coordinate.
	 * @return 2D Perlin noise sample at coordinate (x, z).
	 */
	public double getNoise2(double x, double z) {
		double ret = 0.0;
		double currSize = scale;
		double startingSize = currSize;
		while(currSize >= 1.0) {
			ret += perlin((x / scale), (z / scale), (0f / scale)) * scale;
			currSize /= 2.0f;
		}
		return ret / startingSize;
	}
	
	/**
	 * 3D Perlin noise sample at (x, y, z).
	 * @param x x coordinate.
	 * @param y y coordinate.
	 * @param z z coordinate.
	 * @return 3D noise sample at coordinate (x, y, z).
	 */
	public double getNoise3(double x, double y, double z) {
		double ret = 0.0;
		double currSize = scale;
		double startingSize = currSize;
		while(currSize >= 1.0) {
			ret += perlin((x / scale), (y / scale), (z / scale)) * scale;
			currSize /= 2.0f;
		}
		return ret / startingSize;
	}
	
	/**
	 * @return the seed used to generate the most recent terrain.
	 */
	public double getSeed() {
		return seed;
	}
	
	/**
	 * Implementation of Perlin noise algorithm (for 3D generation, and just slicing based on dimension desired).
	 */
	private double perlin(double x, double y, double z) {
		x += seed;
		y += seed;
		x += seed;
		// Find unit cube that contains point.
		int X = (int)Math.floor(x) & 255;
		int Y = (int)Math.floor(y) & 255;
		int Z = (int)Math.floor(z) & 255;
		// Find relative x, y, z of point in cube.
		x -= Math.floor(x);
		y -= Math.floor(y); 
		z -= Math.floor(z);
		// Compute fade curves for each x, y, z.
		double u = smoothstep(x);
		double v = smoothstep(y);
		double w = smoothstep(z);
		// Hash coordinates of the 8 cube corners.
		int A = permutation[X] + Y;
		int AA = permutation[A] + Z;
		int AB = permutation[A + 1] + Z;
		int B = permutation[X + 1] + Y;
		int BA = permutation[B] + Z;
		int BB = permutation[B + 1] + Z;
		// Return added and blended results from all 8 corners of the cube.
		return  lerp(w, 
			    lerp(v,
		        lerp(u,
		        gradient(permutation[AA], x, y, z),
				gradient(permutation[BA], x - 1, y, z)),
				lerp(u,
				gradient(permutation[AB], x, y - 1, z),
				gradient(permutation[BB], x - 1, y - 1, z))),
			    lerp(v,
			    lerp(u,
			    gradient(permutation[AA + 1], x, y, z - 1),
				gradient(permutation[BA + 1], x - 1, y, z - 1)),
				lerp(u,
				gradient(permutation[AB + 1], x, y - 1,	z - 1),
				gradient(permutation[BB + 1], x - 1, y - 1, z - 1))));
	}
	
	/**
	 * "Smoothstep" utility.
	 */
	private double smoothstep(double t) {
		return t * t * t * (t * (t * 6 - 15) + 10);
	}
	
	/**
	 * Linear interpolation utility between [a, b].
	 */
	private double lerp(double t, double a, double b) {
		return a + t * (b - a);
	}
	
	/**
	 * Convert lower 4 bits of hash code into 12 gradient directions.
	 */
	private double gradient(int hash, double x, double y, double z) {
		int h = hash & 0xF;
		double u = (h < 0x8) ? x : y;
		double v = (h < 0x4) ? y : ((h == 0xC || h == 0xE) ? x : z);
		return ((h & 0x1) == 0x0 ? u : -u) + ((h & 0x2) == 0x0 ? v : -v);
		
	}
	
}
