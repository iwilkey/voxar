package dev.iwilkey.voxar.gui;

import imgui.ImGui;

/**
 * An encapsulation of Dear ImGui's modular GUI window management. Used for clean ImGui, alignment, and GUI window interaction.
 * @author iwilkey
 */
public final class GuiModule {
	
	/**
	 * The name of the module.
	 */
	private String name = "";
	
	/**
	 * The ImGui window flags active.
	 */
	private int flags = 0x00;
	
	/**
	 * The contents of the GuiModule.
	 */
	private GuiModuleContents contents = null;
	
	public GuiModule(String name, GuiModuleContents contents) {
		this.name = name;
		this.contents = contents;
	}
	
	public GuiModule(String name, GuiModuleContents contents, int... flags) {
		this.name = name;
		this.contents = contents;
		for(int i : flags)
			addFlag(i);
	}
	
	/**
	 * Render the GuiModule with optional arguments.
	 * @param args data to pass.
	 */
	public void show(String... args) {
		ImGui.begin(name, flags);
		contents.contents(args);
		ImGui.end();
	}
	
	/**
	 * Render the GuiModule with a specified Anchor and optional arguments.
	 * @param anchor a specified Anchor.
	 * @param args data to pass.
	 */
	public void show(Anchor anchor, String... args) {
		ImGui.begin(name, flags);
		GuiAlignment.align(anchor);
		contents.contents(args);
		ImGui.end();
	}
	
	/**
	 * Render the GuiModule with a specified Anchor, offset (x, y), and optional arguments.
	 * @param anchor a specified Anchor.
	 * @param offX offset in the x direction.
	 * @param offY offset in the y direction.
	 * @param args data to pass.
	 */
	public void show(Anchor anchor, int offX, int offY, String... args) {
		ImGui.begin(name, flags);
		GuiAlignment.align(anchor, offX, offY);
		contents.contents(args);
		ImGui.end();
	}
	
	/**
	 * Render the GuiModule with a specified Anchor, center linear interpolation, and optional arguments.
	 * @param anchor a specified Anchor.
	 * @param lerpCenter linear interpolation percentage.
	 * @param args data to pass.
	 */
	public void show(Anchor anchor, float lerpCenter, String... args) {
		ImGui.begin(name, flags);
		GuiAlignment.align(anchor, lerpCenter);
		contents.contents(args);
		ImGui.end();
	}
	
	/**
	 * Set the contents of the GuiModule.
	 * @param contents the contents.
	 */
	public void setModuleContents(GuiModuleContents contents) {
		this.contents = contents;
	}
	
	/**
	 * Set the name of the GuiModule window.
	 * @param name the name.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Set the ImGui window flags of the GuiModule.
	 * @param flags the flags.
	 */
	public void setFlags(int flags) {
		this.flags = flags;
	}
	
	/**
	 * Add flags to the ImGui window flags of the GuiModule.
	 * @param flags the flags.
	 */
	public void addFlags(int[] flags) {
		for(int i : flags)
			addFlag(i);
	}
	
	/**
	 * Add a flag to the ImGui window flags of the GuiModule.
	 * @param flag the flag.
	 */
	public void addFlag(int flag) {
		flags |= flag;
	}
	
	/**
	 * Remove a flag from the ImGui window flags of the GuiModule.
	 * @param flag the flag.
	 */
	public void removeFlag(int flag) {
		flags &= ~(flag);
	}
	
	/**
	 * Remove multiple flags fromt he ImGui window flags of the GuiModule.
	 * @param flags the flags.
	 */
	public void removeFlags(int[] flags) {
		for(int i : flags) 
			removeFlag(i);
	}

}
