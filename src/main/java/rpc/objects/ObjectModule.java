package rpc.objects;

import java.util.ArrayList;
import java.util.Properties;
import java.util.WeakHashMap;

import net.rpl.ObjectRegistry;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

import rpc.SaveModule;
import rpc.launcher.Game;
import rpc.map.MapModule;
import rpc.utilities.WorkAssignment;

public class ObjectModule {
	private static WeakHashMap<Integer, ObjectModule> stateInstance = new WeakHashMap();
	private MapModule map;
	private SaveModule save;
	private ObjectRangeBuilder orb;
	private ArrayList<ObjectBase> objectList = new ArrayList();
	private byte[][] objectArray;
	private int objectIDCount;

	public ObjectModule() {
		stateInstance.put(Integer.valueOf(Game.getCS()), this);
	}

	public void initController() throws SlickException {
		this.map = MapModule.getState();
		this.save = SaveModule.getState();
		this.objectArray = new byte[this.map.getMapWidth()][this.map.getMapHeight()];
		loadObjectSave();
		this.orb = new ObjectRangeBuilder();
	}

	public void render(Graphics g, boolean debug) throws SlickException {
		for (int x = 0; x < this.objectList.size(); x++)
			((ObjectBase) this.objectList.get(x)).render(g, debug);
	}

	public void update() throws SlickException {
		for (int x = 0; x < this.objectList.size(); x++) {
			((ObjectBase) this.objectList.get(x)).update();
		}
		this.orb.update();
	}

	public void addObject(int tileX, int tileY, int objectWidth, int objectHeight, String baseType, int subType) throws SlickException {
		if (baseType.equals("objectLumberMill")) {
			this.objectList.add(new ObjectLumberMill(tileX, tileY, objectWidth, objectHeight, subType));
		}
		if (baseType.equals("objectLumberShack")) {
			this.objectList.add(new ObjectLumberShack(tileX, tileY, objectWidth, objectHeight, subType));
		}
		if (baseType.equals("wallStone"))
			this.objectList.add(new WallStone(tileX, tileY, objectWidth, objectHeight, subType));
		if (baseType.equals("wallWoodBasic"))
			this.objectList.add(new WallWoodBasic(tileX, tileY, objectWidth, objectHeight, subType));
		if (baseType.equals("wallStoneSturdy")) {
			this.objectList.add(new WallStoneSturdy(tileX, tileY, objectWidth, objectHeight, subType));
		}
		if (baseType.equals("objectSmallHovel")) {
			this.objectList.add(new ObjectSmallHovel(tileX, tileY, objectWidth, objectHeight, subType));
		}
		if (baseType.equals("objectSmallShack")) {
			this.objectList.add(new ObjectSmallShack(tileX, tileY, objectWidth, objectHeight, subType));
		}
		if (baseType.equals("objectTent")) {
			this.objectList.add(new ObjectTent(tileX, tileY, objectWidth, objectHeight, subType));
		}
		if (baseType.equals("objectFarm")) {
			this.objectList.add(new ObjectFarm(tileX, tileY, objectWidth, objectHeight, subType));
		}
		if (baseType.equals("objectSmallFarm")) {
			this.objectList.add(new ObjectSmallFarm(tileX, tileY, objectWidth, objectHeight, subType));
		}
		if (baseType.equals("objectFoodStorage")) {
			this.objectList.add(new ObjectFoodStorage(tileX, tileY, objectWidth, objectHeight, subType));
		}
		if (baseType.equals("objectVillageCenter")) {
			this.objectList.add(new ObjectVillageCenter(tileX, tileY, objectWidth, objectHeight, subType));
		}
		if (baseType.equals("objectStoneMasonry")) {
			this.objectList.add(new ObjectStoneMasonry(tileX, tileY, objectWidth, objectHeight, subType));
		}
		if (baseType.equals("objectStoneShack")) {
			this.objectList.add(new ObjectStoneShack(tileX, tileY, objectWidth, objectHeight, subType));
		}
		if (baseType.equals("objectSmallWoodTorch")) {
			this.objectList.add(new ObjectSmallWoodTorch(tileX, tileY, objectWidth, objectHeight, subType));
		}
		
		ObjectRegistry.addObject(objectList, tileX, tileY, objectWidth, objectHeight, baseType, subType);
		
		for (int w = 0; w < objectWidth; w++)
			for (int h = 0; h < objectHeight; h++)
				this.objectArray[(w + tileX)][(h + tileY)] = 1;
	}

	public int getObjectArrayPosition(int objectID) {
		for (int x = 0; x < this.objectList.size(); x++) {
			if (((ObjectBase) this.objectList.get(x)).getID() == objectID) {
				return x;
			}
		}
		return 0;
	}

	public void removeObject(int arrayID) throws SlickException {
		int objectWidth = ((ObjectBase) this.objectList.get(arrayID)).getObjectWidth();
		int objectHeight = ((ObjectBase) this.objectList.get(arrayID)).getObjectHeight();
		int objectX = ((ObjectBase) this.objectList.get(arrayID)).getObjectX();
		int objectY = ((ObjectBase) this.objectList.get(arrayID)).getObjectY();
		for (int w = 0; w < objectWidth; w++) {
			for (int h = 0; h < objectHeight; h++) {
				this.objectArray[(w + objectX)][(h + objectY)] = 0;
			}
		}
		((ObjectBase) this.objectList.get(arrayID)).removeAllVillagers();
		this.objectList.remove(arrayID);
	}

	public boolean intersects(Shape mouse) {
		for (int x = 0; x < this.objectList.size(); x++) {
			if (((ObjectBase) this.objectList.get(x)).intersects(mouse)) {
				return true;
			}
		}
		return false;
	}

	public Integer getObjectID(Shape mouse) {
		for (int x = 0; x < this.objectList.size(); x++) {
			if (((ObjectBase) this.objectList.get(x)).intersects(mouse)) {
				return Integer.valueOf(x);
			}
		}
		return null;
	}

	public boolean isObject(int tileX, int tileY) {
		if (this.objectArray[tileX][tileY] == 0)
			return false;
		return true;
	}

	public Rectangle getObjectRectangle(int x) {
		return ((ObjectBase) this.objectList.get(x)).getIntersectBox();
	}

	public ArrayList<ObjectBase> getObjectList() {
		return this.objectList;
	}

	public void loadObjectSave() throws SlickException {
		Properties properties = this.save.getObjectData();
		try {
			int objectCount = Integer.parseInt(properties.getProperty("objectCount", "0"));
			if (objectCount > 0)
				for (int x = 0; x < objectCount; x++) {
					String baseType = properties.getProperty("object_baseType_" + x);
					int subType = Integer.parseInt(properties.getProperty("object_subType_" + x));
					int objectWidth = Integer.parseInt(properties.getProperty("object_width_" + x));
					int objectHeight = Integer.parseInt(properties.getProperty("object_height_" + x));
					int objectX = Integer.parseInt(properties.getProperty("object_x_" + x));
					int objectY = Integer.parseInt(properties.getProperty("object_y_" + x));
					addObject(objectX, objectY, objectWidth, objectHeight, baseType, subType);
				}
		} catch (NullPointerException npe) {
			System.out.println("No object save file exists.");
		}
	}

	public Properties saveProperties() {
		Properties properties = new Properties();
		if (this.objectList.size() == 0) {
			properties.setProperty("objectCount", "0");
		} else {
			properties.setProperty("objectCount", Integer.toString(this.objectList.size()));
			for (int x = 0; x < this.objectList.size(); x++) {
				properties.setProperty("object_baseType_" + x, ((ObjectBase) this.objectList.get(x)).getBaseType());
				properties.setProperty("object_subType_" + x, Integer.toString(((ObjectBase) this.objectList.get(x)).getSubType()));
				properties.setProperty("object_width_" + x, Integer.toString(((ObjectBase) this.objectList.get(x)).getObjectWidth()));
				properties.setProperty("object_height_" + x, Integer.toString(((ObjectBase) this.objectList.get(x)).getObjectHeight()));
				properties.setProperty("object_x_" + x, Integer.toString(((ObjectBase) this.objectList.get(x)).getObjectX()));
				properties.setProperty("object_y_" + x, Integer.toString(((ObjectBase) this.objectList.get(x)).getObjectY()));
			}
		}
		return properties;
	}

	public int objectCount(String object, int subType) {
		int count = 0;
		for (int x = 0; x < this.objectList.size(); x++) {
			ObjectBase objectCheck = (ObjectBase) this.objectList.get(x);
			if ((objectCheck.getBaseType().equals(object)) && (objectCheck.getSubType() == subType))
				count++;
		}
		return count;
	}

	public int assignID() {
		int ID = this.objectIDCount;
		this.objectIDCount += 1;
		return ID;
	}

	public WorkAssignment searchForResource(String resourceType) {
		for (int x = 0; x < this.objectList.size(); x++) {
			ObjectBase objectCheck = (ObjectBase) this.objectList.get(x);
			if ((resourceType.equals("wood")) && (objectCheck.getWood() > 0))
				return new WorkAssignment(objectCheck, objectCheck.getReturnOnMapX(), objectCheck.getReturnOnMapY(), "wood");
			if ((resourceType.equals("stone")) && (objectCheck.getStone() > 0))
				return new WorkAssignment(objectCheck, objectCheck.getReturnOnMapX(), objectCheck.getReturnOnMapY(), "stone");
			if ((resourceType.equals("crystal")) && (objectCheck.getCrystal() > 0))
				return new WorkAssignment(objectCheck, objectCheck.getReturnOnMapX(), objectCheck.getReturnOnMapY(), "crystal");
			if ((resourceType.equals("food")) && (objectCheck.getFood() > 0))
				return new WorkAssignment(objectCheck, objectCheck.getReturnOnMapX(), objectCheck.getReturnOnMapY(), "food");
		}
		return null;
	}

	public ObjectBase getBuildAtLocation(int tileX, int tileY) {
		for (int x = 0; x < this.objectList.size(); x++) {
			ObjectBase objectCheck = (ObjectBase) this.objectList.get(x);
			if ((tileX == objectCheck.getObjectX() + objectCheck.getReturnX()) && (tileY == objectCheck.getObjectY() + objectCheck.getReturnY())) {
				return objectCheck;
			}
		}
		return null;
	}

	public byte[][] getRangeMap() {
		return this.orb.getRangeMap();
	}

	public static ObjectModule getState() {
		return (ObjectModule) stateInstance.get(Integer.valueOf(Game.getCS()));
	}

	public static void removeState(int state) {
		stateInstance.remove(Integer.valueOf(state));
	}

}
