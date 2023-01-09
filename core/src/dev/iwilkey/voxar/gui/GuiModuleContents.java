package dev.iwilkey.voxar.gui;

/** 
 * ImGui widgets encapsulated inside of a GuiModule (ImGui window).
 * @author iwilkey
 */
public interface GuiModuleContents {
	/**
	 * Calls to ImGui that describe the functionality of the GuiModule that owns it.
	 * @param args variable array of Strings that allow the transfer of information from outside the scope of the contents() function.
	 */
	public void contents(String... args);
}
