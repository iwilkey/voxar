package dev.iwilkey.voxar.gfx;

import com.badlogic.gdx.utils.Disposable;

import imgui.ImGui;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;

/**
 * Java port of Dear ImGui integrated within Voxar's GLFW and OpenGL context.
 * @author iwilkey
 * @author ocornut (Dear ImGui creator)
 */
public final class DearImGuiRenderer implements Disposable, Renderable {
	
	/**
	 * GLFW window pointer.
	 */
	private long windowHandle;
	
	/**
	 * Dear ImGui GLFW integration encapsulation.
	 */
	private static final ImGuiImplGlfw diGlfw = new ImGuiImplGlfw();
	
	/**
	 * Dear ImGui OpenGL integration encapsulation.
	 */
	private static final ImGuiImplGl3 diGl3 = new ImGuiImplGl3();
	
	public DearImGuiRenderer(long windowHandle) {
		this.windowHandle = windowHandle;
		ImGui.createContext();
		diGlfw.init(windowHandle, true);
		diGl3.init("#version 120");
	}
	
	/**
	 * Clear Dear ImGui's buffers for next rendering cycle.
	 */
	public void clearBuffer() {
		diGlfw.windowFocusCallback(windowHandle, true);
		diGlfw.newFrame();
		ImGui.newFrame();
	}
	
	@Override
	public void render2D() {
		ImGui.render();
		diGl3.renderDrawData(ImGui.getDrawData());
	}
	
	/**
	 * @return the GLFW window pointer.
	 */
	public long getWindowHandle() {
		return windowHandle;
	}
	
	public static ImGuiImplGlfw getImGuiGlfw() {
		return diGlfw;
	}
	
	@Override
	public void dispose() {
		diGl3.dispose();
		diGlfw.dispose();
		ImGui.destroyContext();
	}

}
