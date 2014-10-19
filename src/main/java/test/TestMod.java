package test;

import net.rpl.Mod;
import net.rpl.ObjectRegistry;
import net.rpl.Mod.ModLoad;
import net.rpl.Mod.ModUnload;

@Mod(id = "TestMod", name = "TestMod", version = "0.0.1")
public class TestMod {
	@ModLoad
	public void load() {
		System.out.println("Loaded");
		ObjectRegistry.registerObject("objectTest", ObjectTest.class);
	}

	@ModUnload
	public void unload() {
		System.out.println("Unloaded!");
	}
}
