package dev.iwilkey.voxar.gfx;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL20;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Window;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelCache;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.glutils.HdpiUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import dev.iwilkey.voxar.entity.VoxelEntity;
import dev.iwilkey.voxar.gui.Anchor;
import dev.iwilkey.voxar.gui.GuiModule;
import dev.iwilkey.voxar.gui.GuiModuleContents;
import dev.iwilkey.voxar.model.NullRenderableProvider;
import dev.iwilkey.voxar.state.VoxarEngineState;

import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;

/**
 * Master Renderer for the Voxar engine.
 * @author iwilkey
 */
public final class Renderer implements ViewportResizable, Disposable {
	
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
	public static final int DEPTH_BITS = (1 << 5);
	
	/**
	 * Bit depth for stencil buffer. 
	 */
	public static final int STENCIL_BITS = 0x00;
	
	/**
	 * MSAA samples for anti-aliasing. 
	 */
	public static final int MSAA_SAMPLES = 0x03;
	
	/**
	 * OpenGL rendering line width.
	 */
	public static final int GLOBAL_GL_LINE_WIDTH = 3;
	
	/**
	 * ID for the 3D batch with standard shader in the gfx3D HashMap.
	 */
	public static final long STANDARD_3D_SHADER = 0L;
	
	/**
	 * ID for the 25D batch with standard shader in the gfx25D HashMap.
	 */
	public static final long STANDARD_25D_SHADER = 0L;
	
	/**
	 * ID for the 2D batch with standard shader in the gfx2D HashMap.
	 */
	public static final long STANDARD_2D_SHADER = 0L;
	
	/**
	 * Current window width of the Voxar rendering canvas, in pixels.
	 */
	public static int WINDOW_WIDTH = -1;
	/**
	 * Current window height of the Voxar rendering canvas, in pixels.
	 */
	public static int WINDOW_HEIGHT = -1;
	
	/**
	 * Real-time data pertaining to the back-end LWJGL graphics processes.
	 */
	private final Lwjgl3Graphics graphics;
	
	/**
	 * Real-time data pertaining to the back-end LWJGL window, GLFW, and OpenGL context on the host machine.
	 */
	private final Lwjgl3Window window;
	
	/**
	 * The Voxar engine's capability to draw RenderableProvider instances in 3D space.
	 */
	private final HashMap<Long, ModelBatch> gfx3D;
	
	/**
	 * Buffer for requested RenderableProviders renders from registered RenderableProvider3Ds.
	 */
	private static Array<VoxarRenderableProvider3D> gfxBuffer3D;
	
	/**
	 * The Voxar engine's capability to draw raster graphics in viewport space.
	 */
	private final HashMap<Long, SpriteBatch> gfx2D;
	
	/**
	 * Buffer for requested Raster2 renders from registered RenderableProvider2Ds.
	 */
	private static Array<VoxarRenderableProvider2D> gfxBuffer2D;
	
	/**
	 * The Voxar engine's capability to draw raster graphics in 3D space.
	 */
	private final HashMap<Long, DecalBatch> gfx25D;
	
	/**
	 * Buffer for requested Raster25 renders from registered RenderableProvider25Ds.
	 */
	@SuppressWarnings("unused")
	private static Array<Raster25> gfxBuffer25D;
	
	/**
	 * Used for when all Renderables have been culled away.
	 */
	@SuppressWarnings("unused")
	private final NullRenderableProvider nullRenderable;
	
	/**
	 * GuiModule rendered when Voxar does not have an active state.
	 */
	private static final GuiModule IDLE_GUI_MODULE = new GuiModule("Voxar Engine", new GuiModuleContents() {
		@Override
		public void contents(String... args) {
			ImGui.text("Notice: Voxar engine is idle.");
		}
	}, ImGuiWindowFlags.NoCollapse, 
	   ImGuiWindowFlags.NoResize,
	   ImGuiWindowFlags.NoMove,
	   ImGuiWindowFlags.AlwaysAutoResize);
	
	/**
	 * GuiModule rendered when Voxar is loading assets for a state.
	 */
	private static final GuiModule LOADING_GUI_MODULE = new GuiModule("Voxar", new GuiModuleContents() {
		@Override
		public void contents(String... args) {
			ImGui.text("Loading \"" + args[0] + "\": " 
					+ args[1] + "%" + "|/-\\".charAt((int)(ImGui.getTime() / 0.05f) & 3));
		}
	}, ImGuiWindowFlags.NoCollapse, 
	   ImGuiWindowFlags.NoResize,
	   ImGuiWindowFlags.NoMove,
	   ImGuiWindowFlags.AlwaysAutoResize);
	
	/**
	 * Dear ImGui GLFW integration encapsulation.
	 */
	private static final ImGuiImplGlfw DI_GLFW = new ImGuiImplGlfw();
	
	/**
	 * Dear ImGui OpenGL integration encapsulation.
	 */
	private static final ImGuiImplGl3 DI_GL3 = new ImGuiImplGl3();
	
	/**
	 * Voxar's master renderer.
	 */
	public Renderer() {
		// Register GL, GLFW, and ImGui variables.
		graphics = (Lwjgl3Graphics)Gdx.graphics;
		window = graphics.getWindow();
		HdpiUtils.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1f);
		Gdx.gl.glLineWidth(GLOBAL_GL_LINE_WIDTH);
		
		// Register starting viewport dimensions.
		WINDOW_WIDTH = graphics.getWidth();
		WINDOW_HEIGHT = graphics.getHeight();
		
		// Initialize rendering batch HashMaps with default shaders.
		gfx3D = new HashMap<>(); 
		gfx3D.put(STANDARD_3D_SHADER, new ModelBatch());
		gfx25D = new HashMap<>();
		gfx25D.put(STANDARD_25D_SHADER, null); // This cannot be initialized until a CameraGroupStrategy can be made (when a scene has a VoxelSpace).
		gfx2D = new HashMap<>();
		gfx2D.put(STANDARD_2D_SHADER, new SpriteBatch());
		
		// Init render call buffers.
		gfxBuffer2D = new Array<>();
		gfxBuffer25D = new Array<>();
		gfxBuffer3D = new Array<>();
		
		// Initialize Dear ImGui.
		ImGui.createContext();
		DI_GLFW.init(window.getWindowHandle(), true);
		DI_GL3.init("#version 120");
		
		// Initialize the NullRenderableProvider.
		nullRenderable = new NullRenderableProvider();
	}
	
	/**
	 * Called before rendering occurs.
	 */
	private void clearBuffers() {
		// Clear GL buffers.
		HdpiUtils.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		
		// Clear ImGui buffers.
		DI_GLFW.windowFocusCallback(window.getWindowHandle(), true);
		DI_GLFW.newFrame();
		ImGui.newFrame();
	}
	
	private void renderDIGL3() {
		ImGui.render();
		DI_GL3.renderDrawData(ImGui.getDrawData());
	}
	
	/**
	 * Voxar engine's rendering process.
	 * @param state the active state (or null if none).
	 */
	ModelCache cache = new ModelCache();
	public void render(VoxarEngineState state) {
		clearBuffers();
		if(state == null) {
			IDLE_GUI_MODULE.show(Anchor.CENTER);
		} else {
			if(!state.isReady()) {
				final int loadPercentage = (int)state.getAssetManager().getProgress();
				LOADING_GUI_MODULE.show(Anchor.BOTTOM_RIGHT, 10.0f, state.getStateName(), Integer.toString((int)loadPercentage));
			} else {
				
				// Go through registered state providers in 3D.
				for(VoxarRenderableProvider3D provider : gfxBuffer3D) {
					// Cull registered instances.
					cache.begin(provider.getRenderingPerspective());
					for(final VoxelEntity e : provider.getRegisteredVoxelEntities()) 
						if(FrustumCulling.sphericalTestWith(e, provider.getRenderingPerspective()))
							cache.add(e);
					for(final ModelInstance p : provider.getRegisteredModelInstances()) 
						if(FrustumCulling.cuboidTestWith(p, provider.getRenderingPerspective()))
							cache.add(p);
					cache.end();
					final ModelBatch shader = gfx3D.get(provider.getDesiredShader());
					shader.begin(provider.getRenderingPerspective());
					shader.render(cache, provider.getRenderingEnvironment());
					shader.end();
				}
				
				// Go through registered state providers in 2D.
				for(VoxarRenderableProvider2D provider : gfxBuffer2D) {
					final SpriteBatch shader = gfx2D.get(provider.getDesiredShader());
					shader.begin();
					for(final Raster2 r : provider.getRegisteredRaster2s()) {
						shader.setColor(r.getTint());
						final int x = (int)r.getBoundingBox().x;
						final int y = (int)r.getBoundingBox().y;
						final int w = (int)r.getBoundingBox().width;
						final int h = (int)r.getBoundingBox().height;
						shader.draw(r.getBindedRaster(), x, y, w, h);
					}
					shader.setColor(Color.WHITE);
					shader.end();
				}
			}
		}
		renderDIGL3();
	}
	
	/*
	private void render25D(Perspective3D perspective) {
		if(gfxBuffer25D.size == 0)
			return;
		for(Raster25 r : gfxBuffer25D) {
			if(r.shouldBillboard()) 
				r.getDecal().lookAt(perspective.position, perspective.up);
			gfx25D.get(STANDARD_25D_SHADER).add(r.getDecal());
		}
		gfx25D.get(STANDARD_25D_SHADER).flush();
		gfxBuffer25D.clear();
	}
	*/
	
	/**
	 * Register a RenderableProvider for rendering.
	 * @param provider the provider.
	 */
	public static void register(VoxarRenderableProvider provider) {
		if(provider instanceof VoxarRenderableProvider3D)
			gfxBuffer3D.add((VoxarRenderableProvider3D)provider);
		if(provider instanceof VoxarRenderableProvider2D)
			gfxBuffer2D.add((VoxarRenderableProvider2D)provider);
	}

	@Override
	public void windowResizeCallback(int nw, int nh) {
		for(Map.Entry<Long, SpriteBatch> entry : gfx2D.entrySet())
			entry.getValue().getProjectionMatrix().setToOrtho2D(0, 0, nw, nh);
		for(VoxarRenderableProvider3D p : gfxBuffer3D)
			p.windowResizeCallback(nw, nh);
		for(VoxarRenderableProvider2D p : gfxBuffer2D)
			p.windowResizeCallback(nw, nh);
		WINDOW_WIDTH = nw;
		WINDOW_HEIGHT = nh;
	}
	
	public long getWindowHandle() {
		return window.getWindowHandle();
	}
	
	public void resetCameraGroupStrategy() {
		gfx25D.get(STANDARD_25D_SHADER).setGroupStrategy(null);
	}

	@Override
	public void dispose() {
		
		// Dispose of batches.
		for(Map.Entry<Long, ModelBatch> entry : gfx3D.entrySet()) 
			entry.getValue().dispose();
		for(Map.Entry<Long, DecalBatch> entry : gfx25D.entrySet()) {
			if(entry.getValue() != null)
				entry.getValue().dispose();
		}
		for(Map.Entry<Long, SpriteBatch> entry : gfx2D.entrySet())
			entry.getValue().dispose();
		
		// Dispose of ImGui resources
		DI_GLFW.dispose();
		DI_GL3.dispose();
		ImGui.destroyContext();

	}

}
