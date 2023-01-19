package dev.iwilkey.voxar.noise;

/**
 * An implementation of Ken Perlin's gradient noise generation in 2D.
 * @author iwilkey
 */
public final class Perlin2D {
	
	/**
	 * Ken's original permutation array for selecting gradient vectors.
	 */
	private int[] p;
	
	/**
	 * Adjust the time at which the Perlin noise is captured and returned.
	 */
	private double time;
	
	/**
	 * Adjust the frequency at which the Perlin noise changes.
	 */
	private double frequency;

	/**
	 * Generate noise using default scale.
	 * @param time the time the snapshot of noise is taken at.
	 */
	public Perlin2D(double time, double frequency) {
		this.time = time;
		this.frequency = frequency;
		initPermutationArray();
	}
	
	public double getNoiseAt(double x, double y) {
		
		// Adjust the desired input by "time" and "frequency" which can be set outside of Perlin2D class.
		x = (x / frequency) + time;
		y = (y / frequency) + time;
		
		// Get integer part of input bitwise AND to confine to range [0, 255].
		int xi = (int)Math.floor(x) & 255;
		int yi = (int)Math.floor(y) & 255;

		/*
		 * Get values from permutation table that will be used to identify gradient vectors in later steps. 
		 * The index used will give us the same gradient vector every time the same values are entered. 
		 * We do this four times because we need four gradient vectors for each corner of rectangle. 
		 */
		int g1 = p[p[xi] + yi];
		int g2 = p[p[xi + 1] + yi];
		int g3 = p[p[xi] + yi + 1];
		int g4 = p[p[xi + 1] + yi + 1];
		
		/**
		 * Now, we find distance vector values using the fractional part of the input. The distance vector is coming from
		 * one corner of the rectangle pointing towards the desired point.
		 */
		double xf = x - Math.floor(x);
		double yf = y - Math.floor(y);
		
		/*
		 * Find the direction dot gradient vector for all corners of the rectangle.
		 */
		double d1 = grad(g1, xf, yf);
		double d2 = grad(g2, xf - 1, yf);
		double d3 = grad(g3, xf, yf - 1);
		double d4 = grad(g4, xf - 1, yf - 1);
		
		/*
		 * Use Fade Function to avoid visible artifacts from simple linear interpolation step.
		 */
		double u = fade(xf);
		double v = fade(yf);
		
		/*
		 * Finally, final noise value can be ascertained by using linear interpolation.
		 */
		
		// Interpolate over x-axis and use dot product from top of rectangle.
		double x1i = lerp(u, d1, d2);
		// Interpolate over x-axis and use dot product from bottom of rectangle.
		double x2i = lerp(u, d3, d4);
	
		return lerp(v, x1i, x2i);
	}
	
	/**
	 * Simple linear interpolation.
	 */
	private double lerp(double amount, double left, double right) {
		return ((1 - amount) * left + amount * right);
	}
	
	/**
	 * Fast Fade Function implementation.
	 * @param t
	 * @return the Fade Function value at t where fade(t) = (6t^5 - 15t^4 + 10t^3).
	 */
	private double fade(double t) {
		return t * t * t * (t * (t * 6 - 15) + 10);
	}
	
	/**
	 * @param hash value from permutation table (gradient vector).
	 * @param xf fractional part of input (direction vector x component).
	 * @param yf fractional part of input (direction vector y component).
	 * @return the calculated dot product between the gradient vector from hash table and the input direction vector.
	 */
	private double grad(int hash, double xf, double yf) {
		/* 
		 * Bitwise 3 because, in 2D, we have four possible gradient vectors to choose from each corner of rectangle.
		 * We can use these vectors to calculate the dot product with the direction vector input.
		 */
		switch(hash & 3) {
			case 0: return xf + yf;
			case 1: return -xf + yf;  
			case 2: return xf - yf;
			case 3: return -xf - yf;
			default: return 0.0f;
		}
	}
	
	/**
	 * Initialize a permutation array using Ken's original permutation list.
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
		p = new int[512];
		for(int i = 0; i < 256; i++) 
			p[256 + i] = p[i] = kensList[i];
	}
	
	/**
	 * @return time at which the Perlin noise is captured and returned.
	 */
	public double getTime() {
		return time;
	}
	
	/**
	 * @return the frequency at which the Perlin noise changes.
	 */
	public double getFrequency() {
		return frequency;
	}
	
}
