package net.rpl;

import org.newdawn.slick.util.Log;

//import rpc.launcher.Game;
import rpc.launcher.Launcher;

public class RetroPixelLoader {
	@SuppressWarnings("unused")
	public static void main(String[] args) throws Exception {
		info("Version x.y.z.a loaded.");
		takeOver();

		ModLoader ml = new ModLoader();
		ml.loadMods();

		if (false) {
			info("Launching game...");
			Launcher.main(args);
		}
	}

	private static void takeOver() throws Exception {
		info("Starting takeover...");
		info("Done!");
	}

	public static void info(String message) {
		Log.info("RetroPixelLoader - " + message);
	}
}
