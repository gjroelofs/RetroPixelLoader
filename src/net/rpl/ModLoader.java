package net.rpl;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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
	private final List<ModWrapper> mods;
	private final List<Class> modClasses;
	private static final File MOD_DIRECTORY = new File("mods/");

	private static URLClassLoader classLoader;

	public ModLoader() {
		mods = new ArrayList<ModWrapper>();
		modClasses = new ArrayList<Class>();
		classLoader = new ModClassLoader();
	}

	public void loadMods() throws ZipException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, URISyntaxException, IllegalArgumentException, InvocationTargetException {
		RetroPixelLoader.info("Starting mod loading...");

		if (MOD_DIRECTORY.exists() && MOD_DIRECTORY.isDirectory())
			MOD_DIRECTORY.mkdir();

		for (File file : MOD_DIRECTORY.listFiles()) {
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

		for (Class clazz : modClasses) {
			ModWrapper mod = new ModWrapper(clazz.newInstance());

			RetroPixelLoader.info(String.format("Loading mod - id: %s - name: %s - version: %s ", mod.getId(), mod.getName(), mod.getVersion()));

			mod.load();

			mods.add(mod);
		}

		RetroPixelLoader.info("Mod loading complete!");
	}

	public void unloadMods() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		for (ModWrapper mod : mods) {
			RetroPixelLoader.info(String.format("Unloading mod - id: %s - name: %s - version: %s ", mod.getId(), mod.getName(), mod.getVersion()));

			mod.unload();
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

			if (file.isDirectory()) {
				List<File> allFiles = getAllFiles(file);

				for (File f : allFiles) {
					if (f.getName().endsWith(".class")) {
						String className = f.getPath().substring(file.getPath().length() + 1).replace("/", ".").replace(".class", "");
						Class clazz = getClassLoader().loadClass(className);

						if (clazz.isAnnotationPresent(Mod.class)) {
							modClasses.add(clazz);
						}
					} else {
						((ModClassLoader) getClassLoader()).addURL(f.toURI().toURL());
					}
				}
			}
		}
	}

	public List<File> getAllFiles(File file) {
		List<File> files = new ArrayList<File>();

		if (file.isFile()) {
			files.add(file);
			return files;
		}

		for (File f : file.listFiles()) {
			if (file.isDirectory()) {
				files.addAll(getAllFiles(f));
			} else {
				files.add(f);
			}
		}

		return files;
	}

	public void loadMod(ZipFile file) throws IOException, ClassNotFoundException {
		for (Enumeration<? extends ZipEntry> entries = file.entries(); entries.hasMoreElements();) {
			ZipEntry entry = entries.nextElement();

			String name = entry.getName();

			if (name.endsWith("/") || !name.endsWith(".class"))
				continue;

			name = name.replace('/', '.').replace(".class", "");

			Class clazz = getClassLoader().loadClass(name);
			if (clazz.isAnnotationPresent(Mod.class))
				modClasses.add(clazz);
		}
	}

	public List<ModWrapper> getMods() {
		return mods;
	}

	public ModWrapper getMod(String id) {
		for (ModWrapper mod : mods)
			if (mod.getId().equals(id))
				return mod;
		return null;
	}

	public static URLClassLoader getClassLoader() {
		return classLoader;
	}
}
