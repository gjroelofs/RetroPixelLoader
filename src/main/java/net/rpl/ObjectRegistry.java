package net.rpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import rpc.objects.ObjectBase;

public class ObjectRegistry {
	private ObjectRegistry() {
	}

	private static Map<String, Class> objects = new HashMap<String, Class>();

	public static void addObject(ArrayList<ObjectBase> objectList, int tileX, int tileY, int objectWidth, int objectHeight, String baseType, int subType) {
		try {
			Class clazz = objects.get(baseType);
			if (clazz == null)
				return;

			// TODO this is a really hacky way to do it. Could cause problems
			// because there are no checks.
			objectList.add((ObjectBase) clazz.getConstructors()[0].newInstance(tileX, tileY, objectWidth, objectHeight, subType));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void registerObject(String baseType, Class objectClass) {
		if (objects.keySet().contains(baseType)) {
			throw new RPLException("Duplicate baseType registration: " + baseType + " with class: " + objectClass.getName());
		}
		objects.put(baseType, objectClass);
	}
}
