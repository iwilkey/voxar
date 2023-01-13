package dev.iwilkey.voxar.gfx;

import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.utils.Array;

import dev.iwilkey.voxar.gfx.shadows.system.ShadowSystem;
import dev.iwilkey.voxar.gfx.shadows.system.classical.ClassicalShadowSystem;

public final class ShadowProvider {
	
	private Array<ModelBatch> passBatches;
	
	private ModelBatch mainBatch;
	
	private ShadowSystem system = new ClassicalShadowSystem();
	
	public ShadowProvider() {
		passBatches = new Array<>();
		system.init();
		for(int i = 0; i < system.getPassQuantity(); i++) 
		    passBatches.add(new ModelBatch(system.getPassShaderProvider(i)));
		mainBatch = new ModelBatch(system.getShaderProvider());
	}
	
	public ShadowSystem getShadowSystem() {
		return system;
	}
	
	public ModelBatch getMainBatch() {
		return mainBatch;
	}
	
	public Array<ModelBatch> getPassBatches() {
		return passBatches;
	}
	
}
