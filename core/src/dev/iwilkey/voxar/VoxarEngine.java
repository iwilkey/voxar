package dev.iwilkey.voxar;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;

import dev.iwilkey.voxar.gfx.Renderer;
import dev.iwilkey.voxar.gfx.ViewportResizable;
import dev.iwilkey.voxar.utils.TaskManager;

/**
 * @author iwilkey
 */
@SuppressWarnings("unchecked")
public final class VoxarEngine<T> extends ApplicationAdapter {
	
	public static final String GIT_VERSION = "master-p1";
	public static final String ENGINE_VERSION = "0.01";
	
	private Renderer renderer;
	private VoxarAssetManager assetManager;
	private VoxarEngineInterface engineInterface;
	private HashMap<Long, TaskManager<T>> tasks;
	private Iterator<Entry<Long, TaskManager<T>>> taskIterator;
	
	public VoxarEngine(VoxarEngineInterface entryState) {
		renderer = null;
		assetManager = null;
		engineInterface = null;
		tasks = null;
		taskIterator = null;
		engineInterface = entryState;
	}
	
	@Override
	public void create() {
		renderer = new Renderer(this);
		assetManager = new VoxarAssetManager();
		tasks = new HashMap<>();
		taskIterator = tasks.entrySet().iterator();
		setInterface(engineInterface);
	}
	
	protected void tasks() {
		// update active tasks.
		while(taskIterator.hasNext()) {
			TaskManager<T> manager = (TaskManager<T>)taskIterator.next();
			manager.update();
		}
	}

	@Override
	public void render() {
		tasks();
		if(this.engineInterface != null)
			this.engineInterface.during();
		renderer.routine();
	}
	
	@Override
	public void resize(int width, int height) {
		while(taskIterator.hasNext()) {
			TaskManager<T> manager = (TaskManager<T>)taskIterator.next();
			if(manager instanceof ViewportResizable)
				((ViewportResizable)manager).onViewportResize(width, height);
		}
		renderer.onViewportResize(width, height);
	}
	
	public void setInterface(VoxarEngineInterface interf) {
		if(interf != null)
			interf.end();
		this.engineInterface = interf;
		if(this.engineInterface != null)
			this.engineInterface.begin();
	}
	
	public long registerTaskManager(TaskManager<T> manager) {
		while(true) {
			long guess = new Random().nextLong();
			if(!tasks.containsKey(guess)) {
				tasks.put(guess, manager);
				return guess;
			}
		}
	}
	
	public Renderer getRenderer() {
		return renderer;
	}
	
	public VoxarEngineInterface getCurrentInterface() {
		return engineInterface;
	}
	
	public VoxarAssetManager getAssetManager() {
		return assetManager;
	}
	
	@Override
	public void dispose() {
		// dispose of all tasks.
		while(taskIterator.hasNext()) {
			TaskManager<T> manager = (TaskManager<T>)taskIterator.next();
			manager.dispose();
		}
		if(engineInterface != null)
			engineInterface.end();
		renderer.dispose();
		assetManager.dispose();
	}
	
}
