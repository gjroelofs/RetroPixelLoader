package net.rpl;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

public class ModLoader {
	private final List<Mod> mods;
	private final List<Class<?>> modClasses;
	private static final File MOD_DIRECTORY = new File("mods/");

	private static URLClassLoader classLoader;

	public ModLoader() {
		mods = new ArrayList<Mod>();
		modClasses = new ArrayList<Class<?>>();
		classLoader = new ModClassLoader();
	}

	public void loadMods() throws ZipException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, URISyntaxException {
		RetroPixelLoader.info("Starting mod loading...");

		for (File file : MOD_DIRECTORY.listFiles()) {
			RetroPixelLoader.info("Found modfile: " + file.getPath());

			URL entryUrl = null;
			if (file.getName().endsWith(".zip")) {
				loadMod(new ZipFile(file));
				entryUrl = new URL("jar:" + file.toURI().toURL() + "!/");
			}
			if (file.getName().endsWith(".jar")) {
				loadMod(new JarFile(file));
				entryUrl = new URL("" + file.toURI().toURL() + "");
			}

			if (entryUrl != null)
				((ModClassLoader) getClassLoader()).addURL(entryUrl);
		}

		loadModsFromClasspath();

		for (Class<?> clazz : modClasses) {
			Mod mod = (Mod) clazz.newInstance();
			RetroPixelLoader.info(String.format("Loading mod - id: %s - name: %s - version: %s ", mod.id(), mod.name(), mod.version()));
			mods.add(mod);
		}
	}

	public void loadModsFromClasspath() throws URISyntaxException, ClassNotFoundException, ZipException, IOException {
		for (URL url : getClassLoader().getURLs()) {
			if (url.toString().startsWith("jar:"))
				url = new URL(url.toString().substring(4));

			File file = new File(url.toURI());

			if (file.getPath().contains("lib/jars/"))
				continue;

			if (file.getName().endsWith(".zip") || file.getName().endsWith(".jar")) {
				loadMod(new ZipFile(file));
			}
		}
	}

	public void loadMod(ZipFile file) throws IOException, ClassNotFoundException {
		for (Enumeration<? extends ZipEntry> entries = file.entries(); entries.hasMoreElements();) {
			ZipEntry entry = entries.nextElement();

			String name = entry.getName();

			if (name.endsWith("/") || !name.endsWith(".class"))
				continue;

			name = name.replace('/', '.').replace(".class", "");

			Class<?> clazz = getClassLoader().loadClass(name);
			if (clazz.isAnnotationPresent(Mod.class))
				modClasses.add(clazz);
		}
	}

	public List<Mod> getMods() {
		return mods;
	}

	public static URLClassLoader getClassLoader() {
		return classLoader;
	}
}
