package net.rpl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.rpl.Mod.ModLoad;
import net.rpl.Mod.ModUnload;

public class ModWrapper {

	private final Object instance;
	private final String id;
	private final String name;
	private final String version;
	private Method loadMethod;
	private Method unloadMethod;

	public ModWrapper(Object instance) {
		this.instance = instance;
		this.id = instance.getClass().getDeclaredAnnotation(Mod.class).id();
		this.name = instance.getClass().getDeclaredAnnotation(Mod.class).name();
		this.version = instance.getClass().getDeclaredAnnotation(Mod.class).version();

		for (Method m : instance.getClass().getDeclaredMethods()) {
			if (m.isAnnotationPresent(ModLoad.class))
				loadMethod = m;
			if (m.isAnnotationPresent(ModUnload.class))
				unloadMethod = m;
		}
	}

	public Object getInstance() {
		return instance;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getVersion() {
		return version;
	}

	public void load() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		loadMethod.invoke(instance);
	}

	public void unload() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		unloadMethod.invoke(instance);
	}
}
