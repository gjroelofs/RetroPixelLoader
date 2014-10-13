package net.rpl;

import java.lang.reflect.InvocationTargetException;
import java.security.Permission;

public class RPLSecurityManager extends SecurityManager {
	@Override
	public void checkExit(int status) {
		try {
			RetroPixelLoader.exitHook(status);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void checkPermission(Permission perm) {
	}
}
