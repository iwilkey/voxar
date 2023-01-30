package dev.iwilkey.voxar.gfx;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL20;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Window;
import com.badlogic.gdx.graphics.glutils.HdpiUtils;
import com.badlogic.gdx.utils.Disposable;

import dev.iwilkey.voxar.VoxarEngine;

import imgui.ImGui;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;

/**
 * @author iwilkey
 */
@SuppressWarnings("rawtypes")
public final class Renderer implements ViewportResizable, Disposable {

	public static final int RED_BITS = (1 << 3);
	public static final int GREEN_BITS = (1 << 3);
	public static final int BLUE_BITS = (1 << 3);
	public static final int ALPHA_BITS = (1 << 3);
	public static final int DEPTH_BITS = (1 << 5);
	public static final int STENCIL_BITS = 0x00;
	public static final int MSAA_SAMPLES = 0x03;
	public static final int GLOBAL_GL_LINE_WIDTH = 3;
	
	public static int WINDOW_WIDTH = -1;
	public static int WINDOW_HEIGHT = -1;
	private static final ImGuiImplGlfw DI_GLFW = new ImGuiImplGlfw();
	private static final ImGuiImplGl3 DI_GL3 = new ImGuiImplGl3();
	
	private final Lwjgl3Graphics graphics;
	private final Lwjgl3Window window;
	private final VoxarEngine engine;
	
	public Renderer(VoxarEngine engine) {
		this.engine = engine;
		graphics = (Lwjgl3Graphics)Gdx.graphics;
		window = graphics.getWindow();
		setGlClearColor(0.1f, 0.1f, 0.1f, 1f);
		setGlLineWidth(GLOBAL_GL_LINE_WIDTH);
		registerViewportDimensions(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		createAndLinkImGuiContext();
		GLFW.glfwShowWindow(getGLFWWindowHandle());
	}
	
	public void routine() {
		clearBuffers();
		
		if(engine.getCurrentInterface() == null) {
			ImGui.text("Engine idle.");
		}
		
		renderDIGL3();
	}
	
	private void clearBuffers() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		DI_GLFW.windowFocusCallback(window.getWindowHandle(), true);
		DI_GLFW.newFrame();
		ImGui.newFrame();
	}
	
	private void renderDIGL3() {
		ImGui.render();
		DI_GL3.renderDrawData(ImGui.getDrawData());
	}
	
	private void createAndLinkImGuiContext() {
		ImGui.createContext();
		DI_GLFW.init(window.getWindowHandle(), true);
		DI_GL3.init("#version 120");
	}
	
	public void registerViewportDimensions(int width, int height) {
		HdpiUtils.glViewport(0, 0, width, height);
		WINDOW_WIDTH = width;
		WINDOW_HEIGHT = height;
	}
	
	public void setGlClearColor(float r, float g, float b, float a) {
		Gdx.gl.glClearColor(r, g, b, a);
	}
	
	public void setGlLineWidth(int width) {
		Gdx.gl.glLineWidth(GLOBAL_GL_LINE_WIDTH);
	}
	
	public long getGLFWWindowHandle() {
		return window.getWindowHandle();
	}
	
	public Lwjgl3Graphics getLWJGLGraphics() {
		return graphics;
	}
	
	public VoxarEngine getVoxarEngine() {
		return engine;
	}
	
	@Override
	public void onViewportResize(int newWidth, int newHeight) {
		registerViewportDimensions(newWidth, newHeight);
	}

	@Override
	public void dispose() {
		DI_GLFW.dispose();
		DI_GL3.dispose();
		ImGui.destroyContext();
	}
	
}
