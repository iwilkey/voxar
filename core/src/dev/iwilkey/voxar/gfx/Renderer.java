package dev.iwilkey.voxar.gfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Window;
import com.badlogic.gdx.graphics.glutils.HdpiUtils;

public final class Renderer {

	public static final int RED_BITS = (1 << 3);
	public static final int GREEN_BITS = (1 << 3);
	public static final int BLUE_BITS = (1 << 3);
	public static final int ALPHA_BITS = (1 << 3);
	public static final int DEPTH_BITS = (1 << 5);
	public static final int STENCIL_BITS = 0x00;
	public static final int MSAA_SAMPLES = 0x03;
	public static final int GLOBAL_GL_LINE_WIDTH = 3;
	public static final long STANDARD_3D_SHADER = 0L;
	public static final long STANDARD_25D_SHADER = 0L;
	public static final long STANDARD_2D_SHADER = 0L;
	public static int WINDOW_WIDTH = -1;
	public static int WINDOW_HEIGHT = -1;
	private final Lwjgl3Graphics graphics;
	private final Lwjgl3Window window;
	
	public Renderer() {
		graphics = (Lwjgl3Graphics)Gdx.graphics;
		window = graphics.getWindow();
		setGlClearColor(0.1f, 0.1f, 0.1f, 1f);
		setGlLineWidth(GLOBAL_GL_LINE_WIDTH);
		registerViewportDimensions();
	}
	
	public void setGlClearColor(float r, float g, float b, float a) {
		Gdx.gl.glClearColor(r, g, b, a);
	}
	
	public void setGlLineWidth(int width) {
		Gdx.gl.glLineWidth(GLOBAL_GL_LINE_WIDTH);
	}
	
	public void registerViewportDimensions() {
		HdpiUtils.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		WINDOW_WIDTH = graphics.getWidth();
		WINDOW_HEIGHT = graphics.getHeight();
	}
	
	public long getGLFWWindowHandle() {
		return window.getWindowHandle();
	}
	
	public Lwjgl3Graphics getLWJGLGraphics() {
		return graphics;
	}
	
}
