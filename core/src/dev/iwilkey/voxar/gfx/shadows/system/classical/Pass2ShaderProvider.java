package dev.iwilkey.voxar.gfx.shadows.system.classical;

import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.utils.BaseShaderProvider;
import com.badlogic.gdx.utils.GdxRuntimeException;

/** 
 * @author realitix 
 */
public class Pass2ShaderProvider extends BaseShaderProvider {
	public final Pass2Shader.Config config;

	public Pass2ShaderProvider (final Pass2Shader.Config config) {
		if (config == null) throw new GdxRuntimeException("Pass2ShaderProvider needs config");
		this.config = config;
	}

	@Override
	protected Shader createShader (final Renderable renderable) {
		return new Pass2Shader(renderable, config);
	}
}
