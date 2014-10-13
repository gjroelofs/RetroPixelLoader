package net.rpl;

import java.lang.reflect.InvocationTargetException;

import org.newdawn.slick.util.Log;

import rpc.launcher.Launcher;

public class RetroPixelLoader {

	public static final ModLoader modLoader = new ModLoader();

	public static void main(String[] args) throws Exception {
		info("Version: " + RPLVersion.getVersion());
		takeOver();

		modLoader.loadMods();

		info("Launching game...");
		Launcher.main(args);
	}

	private static void takeOver() throws Exception {
		info("Starting takeover...");
		System.setSecurityManager(new RPLSecurityManager());
		info("Done!");
	}

	public static void info(String message) {
		Log.info("RetroPixelLoader - " + message);
	}

	public static void exitHook(int status) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		info("Received exit request with status: " + status);
		info("Unloading mods...");
		modLoader.unloadMods();
		info("Continuing with game shutdown.");
	}
}
