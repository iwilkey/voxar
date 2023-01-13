package dev.iwilkey.voxar.gfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Window;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.glutils.HdpiUtils;
import com.badlogic.gdx.utils.Disposable;

import dev.iwilkey.voxar.gfx.shadows.system.ShadowSystem;
import dev.iwilkey.voxar.gui.Anchor;
import dev.iwilkey.voxar.gui.GuiModule;
import dev.iwilkey.voxar.gui.GuiModuleContents;
import dev.iwilkey.voxar.perspective.VoxelSpacePerspective;
import dev.iwilkey.voxar.state.VoxarEngineState;
import dev.iwilkey.voxar.world.VoxelSpace;
import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;

/**
 * The Voxar engine's 2D, 2.5D, 3D, and GUI renderer.
 * @author iwilkey
 */
public final class VoxarRenderer implements Disposable, RenderResizable {
	
	/*
	 * Back buffer configuration.
	 */
	
	/**
	 * Bit depth for the red color channel.
	 */
	public static final int RED_BITS = (1 << 3);
	
	/**
	 * Bit depth for the green color channel.
	 */
	public static final int GREEN_BITS = (1 << 3);
	
	/**
	 * Bit depth for the blue color channel.
	 */
	public static final int BLUE_BITS = (1 << 3);
	
	/**
	 * Bit depth for the alpha color channel.
	 */
	public static final int ALPHA_BITS = (1 << 3);
	
	/**
	 * Bit depth for depth buffer.
	 */
	public static final int DEPTH_BITS = (1 << 4);
	
	/**
	 * Bit depth for stencil buffer. 
	 */
	public static final int STENCIL_BITS = 0x00;
	
	/**
	 * MSAA samples. 
	 */
	public static final int MSAA_SAMPLES = 0x03;
	
	/*
	 * Runtime variables.
	 */
	
	/**
	 * Current window width of the Voxar rendering canvas, in pixels.
	 */
	public static int WW = -1;
	/**
	 * Current window height of the Voxar rendering canvas, in pixels.
	 */
	public static int WH = -1;
	
	/**
	 * Real-time data pertaining to the back-end LWJGL graphics processes.
	 */
	private final Lwjgl3Graphics graphics;
	
	/**
	 * Real-time data pertaining to the back-end LWJGL window, GLFW, and OpenGL context on the host machine.
	 */
	private final Lwjgl3Window window;
	
	/**
	 * Real-time data pertaining to the Dear ImGui integration rendering process, GLFW, and OpenGL context.
	 */
	private final DearImGuiRenderer gui;
	
	/**
	 * Renderer for 2D or 2.5D raster graphics in viewport (2D) or 3D space.
	 */
	private final RasterRenderer raster;
	
	/**
	 * The Voxar engine's capability to draw batched Renderable instances (3D models) in 3D space.
	 */
	private final ModelBatch gfx3D;
	
	private final GuiModule idleGui = new GuiModule("Voxar Engine", new GuiModuleContents() {
		@Override
		public void contents(String... args) {
			ImGui.text("Notice: Voxar engine is idle.");
		}
	}, ImGuiWindowFlags.NoCollapse, 
	   ImGuiWindowFlags.NoResize,
	   ImGuiWindowFlags.NoMove,
	   ImGuiWindowFlags.AlwaysAutoResize);
	
	private final GuiModule loadingGui = new GuiModule("Voxar", new GuiModuleContents() {
		@Override
		public void contents(String... args) {
			ImGui.text("Loading \"" + args[0] + "\": " 
					+ args[1] + "%" + "|/-\\".charAt((int)(ImGui.getTime() / 0.05f) & 3));
		}
	}, ImGuiWindowFlags.NoCollapse, 
	   ImGuiWindowFlags.NoResize,
	   ImGuiWindowFlags.NoMove,
	   ImGuiWindowFlags.AlwaysAutoResize);

	public VoxarRenderer() {
		// Set up LWJGL graphics
		graphics = (Lwjgl3Graphics)Gdx.graphics;
		window = graphics.getWindow();
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1f);
		WW = Gdx.graphics.getWidth();
		WH = Gdx.graphics.getHeight();
		// Set up raster (2D) renderer.
		raster = new RasterRenderer();
		// Set up 3D renderer.
		gfx3D = new ModelBatch();
		// Set up ImGui renderer.
		gui = new DearImGuiRenderer(window.getWindowHandle());
	}
	
	/**
	 * Clear color buffer, depth buffer, and GUI buffers. Called directly before new data is to be drawn to the screen.
	 */
	void preprocess() {
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		gui.clearBuffer();
	}
	
	/**
	 * Draw 2D and 3D data to screen based on how the current State instructs it.
	 * @param state the current operating state.
	 */
	void process(VoxarEngineState state) {
		if(state.hasVoxelSpace()) {
			
			VoxelSpace space = state.getVoxelSpace();
			VoxelSpacePerspective perspective = space.getRenderingPerspective();
			
			// Render shadows...
			ShadowSystem system = space.getShadowProvider().getShadowSystem();
			system.begin(perspective, space.getRawRenderables());
			system.update();
			for(int i = 0; i < system.getPassQuantity(); i++) {
			    system.begin(i);
			    Camera camera;
			    while((camera = system.next()) != null) {
			    	space.getShadowProvider().getPassBatches().get(i).begin(camera);
			    	space.getShadowProvider().getPassBatches().get(i).render(space.getRawRenderables(), space.getLighting());
			    	space.getShadowProvider().getPassBatches().get(i).end();
			    }
			    camera = null;
			    system.end(i);
			}
			system.end();
			
			HdpiUtils.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1f);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
			
			space.getShadowProvider().getMainBatch().begin(perspective);
			space.getShadowProvider().getMainBatch().render(space.getRenderables(), space.getLighting());
			space.getShadowProvider().getMainBatch().end();
			
			if(raster.get25D() == null)
				raster.set25D(new DecalBatch(new CameraGroupStrategy(perspective)));
			/*
			gfx3D.begin(perspective);
			gfx3D.render(space.getRenderables(), space.getLighting());
			gfx3D.end();
			*/
			raster.render3D(perspective);
			if(space.getPhysicsEngine().isDebugMode()) {
				space.getPhysicsEngine().getDebugRenderer().begin(space.getRenderingPerspective());
				space.getPhysicsEngine().getDynamicsWorld().debugDrawWorld();
				space.getPhysicsEngine().getDebugRenderer().end();
			}

			
		} else {
			preprocess();
		}
		gui.clearBuffer();
	}
	
	/**
	 * Draw raster graphics and GUI to viewport space.
	 */
	void postprocess() {
		raster.render2D();
		raster.tick();
		gui.render2D();
	}
	
	/**
	 * Called when the Voxar engine switches a state to make sure the RasterRenderer does not try and render Raster25's to a state with no VoxelSpace.
	 */
	public void resetCameraGroupStrategy() {
		raster.set25D(null);
	}
	
	/**
	 * Render any State RenderableProviders and GuiModules.
	 * @param state the state to render.
	 */
	public void renderState(VoxarEngineState state) {
		process(state);
		state.gui();
		postprocess();
	}
	
	/**
	 * Engine idles.
	 */
	public void renderNullState() {
		preprocess();
		idleGui.render(Anchor.CENTER);
		postprocess();
	}
	
	/**
	 * Show client information about the progress of the State AssetManager loading process.
	 * @param stateName the state being loaded.
	 * @param loadPercentage the progress, in percentage.
	 */
	public void loading(String stateName, float loadPercentage) {
		preprocess();
		loadingGui.render(Anchor.BOTTOM_RIGHT, 10.0f, stateName, Integer.toString((int)loadPercentage));
		postprocess();
	}
	
	@Override
	public void windowResizeCallback(int nw, int nh) {
		raster.windowResizeCallback(nw, nh);
		WW = nw;
		WH = nh;
	}
	
	/**
	 * @return data pertaining to the back-end LWJGL graphics processes.
	 */
	public Lwjgl3Graphics getGraphics() {
		return graphics;
	}
	
	/**
	 * @return data pertaining to the back-end LWJGL window, GLFW, and OpenGL context on the host machine.
	 */
	public Lwjgl3Window getWindow() {
		return window;
	}
	
	/**
	 * @return data pertaining to the Dear ImGui integration rendering process, GLFW, and OpenGL context.
	 */
	public DearImGuiRenderer getDearImGuiRenderer() {
		return gui;
	}
	
	/**
	 * @return the Voxar engine's capability to draw batched Renderable instances (3D models) in 3D space.
	 */
	public ModelBatch get3D() {
		return gfx3D;
	}
	
	/**
	 * @return the renderer for 2D or 2.5D raster graphics in viewport (2D) or 3D space.
	 */
	public RasterRenderer getRasterRenderer() {
		return raster;
	}
	
	@Override
	public void dispose() {
		gfx3D.dispose();
		raster.dispose();
		gui.dispose();
	}
	
}
