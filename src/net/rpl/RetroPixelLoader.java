package net.rpl;

//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.PrintStream;
//import java.text.SimpleDateFormat;
//import java.util.Date;

//import javassist.ClassPool;
//import javassist.CtClass;
//import javassist.CtMethod;

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

//		ClassPool pool = ClassPool.getDefault();
//
//		CtClass thiz = pool.get("net.rpl.RetroPixelLoader");
//		CtClass launcher = pool.get("rpc.launcher.Launcher");
//
//		CtMethod newMain = thiz.getDeclaredMethod("newMain");
//		CtMethod oldMain = launcher.getDeclaredMethod("main");
//
//		oldMain.setBody(newMain, null);
//
//		launcher.toClass();

		info("Done!");
	}

//	void newMain(String[] args) {
//		try {
//			boolean inEclipse = Boolean.parseBoolean(System.getProperty("runInEclipse"));
//
//			if (!inEclipse) {
//				File nativesFolder = new File("logs/");
//				if (!nativesFolder.exists()) {
//					nativesFolder.mkdirs();
//				}
//
//				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
//				Date date = new Date();
//				File[] listOfFiles = nativesFolder.listFiles();
//
//				for (File f : listOfFiles) {
//					if (f.isFile() && date.getTime() - f.lastModified() > 1209600000L) {
//						f.delete();
//					}
//				}
//
//				PrintStream var11 = new PrintStream(new FileOutputStream("logs/" + dateFormat.format(date) + ".txt"));
//				System.setOut(var11);
//			}
//
//			String var8 = (new File("lib/natives/")).getAbsolutePath();
//			System.setProperty("java.library.path", var8 + ";" + (new File("lib/jars/")).getAbsolutePath() + ";" + System.getProperty("java.library.path"));
//			System.setProperty("org.lwjgl.librarypath", var8);
//
//			Game.launchGame();
//		} catch (Exception e) {
//			System.out.println("exception thrown :(");
//		}
//	}

	public static void info(String message) {
		Log.info("RetroPixelLoader - " + message);
	}
}
