package dev.iwilkey.voxar.gui;

import dev.iwilkey.voxar.gfx.Renderer;

import imgui.ImGui;
import imgui.ImVec2;

/**
 * A collection of static resources that aid in the management of GuiModule's placement on the screen.
 * @author iwilkey
 */
public final class GuiAlignment {
	
	/**
	 * Utility method to numerically calculate, in pixels, the (x, y) coordinates of a window based on the anchor described.
	 * @param dimensions the size of the window (width, height).
	 * @param anchor the conceptual anchor described.
	 * @return (x, y) where the window should be positioned.
	 */
	public static ImVec2 anchorTo(ImVec2 dimensions, Anchor anchor) {
		ImVec2 ret = new ImVec2(-1, -1);
		final int ww = Renderer.WINDOW_WIDTH;
		final int hh = Renderer.WINDOW_HEIGHT;
		final int cax = ww / 2;
		final int cay = hh / 2;
		final int dx = (int)dimensions.x;
		final int dy = (int)dimensions.y;
		final int cdx = dx / 2;
		final int cdy = dy / 2;
		switch(anchor) {
			case CENTER:
				ret = new ImVec2(cax - cdx, cay - cdy);
				break;
			case TOP_CENTER:
				ret = new ImVec2(cax - cdx, 0);
				break; 
			case TOP_RIGHT:
				ret = new ImVec2(ww - dx, 0);
				break;
			case CENTER_LEFT:
				ret = new ImVec2(0, cay - cdy);
				break;
			case CENTER_RIGHT:
				ret = new ImVec2(ww - dx, cay - cdy);
				break;
			case BOTTOM_LEFT:
				ret = new ImVec2(0, hh - dy);
				break;
			case BOTTOM_CENTER:
				ret = new ImVec2(cax - cdx, hh - dy);
				break;
			case BOTTOM_RIGHT:
				ret = new ImVec2(ww - dx, hh - dy);
				break;
			case TOP_LEFT:
			default:	
				ret = new ImVec2(0, 0);
		}
		return ret;
	}
	
	/** 
	 * Align an ImGui window using conceptual "anchors" that describe relative screen position depending on
	 * current window dimensions.
	 * @param anchor the anchor chosen.
	 */
	public static void align(Anchor anchor) {
		ImVec2 pos = anchorTo(new ImVec2(ImGui.getWindowWidth(), ImGui.getWindowHeight()), anchor);
		ImGui.setWindowPos(pos.x, pos.y);
	}
	
	/** 
	 * Align an ImGui window using conceptual "anchors" that describe relative screen position depending on
	 * current window dimensions. This override allows a floating point offset from the anchor positioning (padding).
	 * @param anchor the anchor chosen.
	 * @param padX offset on the x-axis.
	 * @param padY offset on the y-axis.
	 */
	public static void align(Anchor anchor, float padX, float padY) {
		ImVec2 pos = anchorTo(new ImVec2(ImGui.getWindowWidth(), ImGui.getWindowHeight()), anchor);
		ImGui.setWindowPos(pos.x + padX, pos.y + padY);
	}
	
	/**
	 * Align an ImGui window with a linear interpolation between the anchor chosen and the center of the screen.
	 * @param anchor the chosen anchor.
	 * @param lerpCenter a percentage ([0.0f, 100.0f]) that will split the difference between the anchoring chosen
	 * and the center of the screen. 
	 * 
	 * In other words, the following formula will be applied:
	 * (new window position) = (anchorPos + ((centerPos - anchorPos) * (lerpCenter / 100.0f))
	 */
	public static void align(Anchor anchor, float lerpCenter) {
		lerpCenter = (float)Math.min(100.0f, lerpCenter);
		lerpCenter = (float)Math.max(0.0f, lerpCenter);
		ImVec2 center = anchorTo(new ImVec2(ImGui.getWindowWidth(), ImGui.getWindowHeight()), Anchor.CENTER);
		ImVec2 anc = anchorTo(new ImVec2(ImGui.getWindowWidth(), ImGui.getWindowHeight()), anchor);
		float x = anc.x + ((center.x - anc.x) * (lerpCenter / 100.0f));
		float y = anc.y + ((center.y - anc.y) * (lerpCenter / 100.0f));
		ImGui.setWindowPos(x, y);
	}

}
