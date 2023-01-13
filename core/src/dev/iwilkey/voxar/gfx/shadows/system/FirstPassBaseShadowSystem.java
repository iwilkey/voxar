package dev.iwilkey.voxar.gfx.shadows.system;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;

import dev.iwilkey.voxar.gfx.shadows.utils.*;

/** 
 * FirstPassBaseShadowSystem assumes that the first pass renders all depth map in one texture.
 * @author realitix 
 */
public abstract class FirstPassBaseShadowSystem extends BaseShadowSystem {

	protected static int FIRST_PASS = 0;

	public FirstPassBaseShadowSystem () {
		super();
	}

	public FirstPassBaseShadowSystem (NearFarAnalyzer nearFarAnalyzer, ShadowMapAllocator allocator,
		DirectionalAnalyzer directionalAnalyzer, LightFilter lightFilter) {
		super(nearFarAnalyzer, allocator, directionalAnalyzer, lightFilter);
	}

	@Override
	protected void init (int n) {
		if (n == FIRST_PASS) init1();
	}

	protected void init1 () {
		frameBuffers[FIRST_PASS] = new FrameBuffer(Pixmap.Format.RGBA8888, allocator.getWidth(), allocator.getHeight(), true);
	}

	@Override
	protected void beginPass (int n) {
		super.beginPass(n);
		if (n == FIRST_PASS) beginPass1();
	};

	protected void beginPass1 () {
		allocator.begin();
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glEnable(GL20.GL_SCISSOR_TEST);
	}

	@Override
	protected void endPass (int n) {
		super.endPass(n);
		if (n == FIRST_PASS) endPass1();
	};

	protected void endPass1 () {
		allocator.end();
		Gdx.gl.glDisable(GL20.GL_SCISSOR_TEST);
	}
}
