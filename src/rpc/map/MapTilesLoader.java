package rpc.map;

import gnu.trove.map.hash.THashMap;
import gnu.trove.map.hash.TIntObjectHashMap;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

import rpc.utilities.OrderedPair;

public class MapTilesLoader {
	private Image terrain;
	private Image terrainCollision;
	private Image terrainShadows;
	private ArrayList<SpriteSheet> tileSheets = new ArrayList();
	private ArrayList<SpriteSheet> tileSheetsCollision = new ArrayList();
	private ArrayList<SpriteSheet> tileSheetsShadows = new ArrayList();

	private TIntObjectHashMap<Image> tiles = new TIntObjectHashMap();
	private TIntObjectHashMap<Image> objects = new TIntObjectHashMap();
	private TIntObjectHashMap<Image> tilesCollision = new TIntObjectHashMap();
	private TIntObjectHashMap<Image> tilesShadows = new TIntObjectHashMap();
	private THashMap<String, Integer> tileSetToGID = new THashMap();
	private TIntObjectHashMap<String> GIDToTileSet = new TIntObjectHashMap();
	private TIntObjectHashMap<OrderedPair> tileCoordinates = new TIntObjectHashMap();

	private Properties tileSetProperties = new Properties();

	private String[] propertiesList = "name,blocked,blocklight,shadow,light,layer,cover,type,collision,resource,crops,miniMapColor".split(",");

	MapTilesLoader() throws SlickException {
		this.terrain = new Image("res/maps/terrain.png", false, 9728);
		this.terrainCollision = new Image("res/maps/terrainCollision.png", false, 9728);
		this.terrainShadows = new Image("res/maps/terrainShadows.png", false, 9728);

		loadTerrain(3, 0, "terrainDirtLightBrown", 1);
		loadTerrain(4, 0, "terrainDirtLightBrownAccent", 201);
		loadTerrain(3, 1, "terrainDirtBrown", 401);
		loadTerrain(4, 1, "terrainDirtBrownAccent", 601);
		loadTerrain(3, 2, "terrainDirtDarkBrown", 801);
		loadTerrain(4, 2, "terrainDirtDarkBrownAccent", 1001);

		loadTerrain(3, 3, "terrainGrassGreen", 10001);
		loadTerrain(4, 3, "terrainGrassGreenAccent", 10201);
		loadTerrain(3, 4, "terrainGrassYellowBrown", 10401);
		loadTerrain(4, 4, "terrainGrassYellowBrownAccent", 10601);
		loadTerrain(3, 5, "terrainGrassEmeraldGreen", 10801);
		loadTerrain(4, 5, "terrainGrassEmeraldGreenAccent", 11001);

		loadTerrain(3, 6, "terrainGravelGray", 20001);
		loadTerrain(4, 6, "terrainGravelGrayAccent", 20201);
		loadTerrain(3, 7, "terrainGravelBlue", 20401);
		loadTerrain(4, 7, "terrainGravelBlueAccent", 20601);
		loadTerrain(3, 8, "terrainGravelRed", 20801);
		loadTerrain(4, 8, "terrainGravelRedAccent", 21001);

		loadTerrain(3, 9, "terrainTreesGreen", 30001);
		loadTerrain(4, 9, "terrainTreesGreenAccent", 30201);
		loadTerrain(5, 9, "terrainTreesGreenCollected", 30801);
		loadTerrain(3, 10, "terrainTreesBrown", 30401);
		loadTerrain(4, 10, "terrainTreesBrownAccent", 30601);
		loadTerrain(5, 10, "terrainTreesBrownCollected", 31001);

		loadTerrain(3, 11, "terrainWater", 40001);
		loadTerrain(4, 11, "terrainWaterAccent", 40201);

		loadTerrain(3, 12, "terrainLava", 50001);
		loadTerrain(4, 12, "terrainLavaAccent", 50201);

		loadTerrain(3, 13, "terrainRock", 60001);
		loadTerrain(4, 13, "terrainRockAccent", 60201);
		loadTerrain(5, 13, "terrainRockCollected", 60401);

		loadTerrain(3, 14, "terrainSandTan", 70001);
		loadTerrain(4, 14, "terrainSandTanAccent", 70201);
		loadTerrain(3, 15, "terrainSandBlack", 70401);
		loadTerrain(4, 15, "terrainSandBlackAccent", 70601);
		loadTerrain(3, 16, "terrainSandRed", 70801);
		loadTerrain(4, 16, "terrainSandRedAccent", 71001);

		loadTerrain(3, 17, "terrainBricks", 80001);
		loadTerrain(4, 17, "terrainBricksAccent", 80201);

		loadTerrain(3, 18, "terrainTiles", 90001);
		loadTerrain(4, 18, "terrainTilesAccent", 90201);

		loadTerrain(3, 19, "terrainCrystalsRed", 100001);
		loadTerrain(4, 19, "terrainCrystalsRedAccent", 100201);
		loadTerrain(3, 20, "terrainCrystalsGreen", 100401);
		loadTerrain(4, 20, "terrainCrystalsGreenAccent", 100601);
		loadTerrain(3, 21, "terrainCrystalsBlue", 100801);
		loadTerrain(4, 21, "terrainCrystalsBlueAccent", 101001);

		loadTerrain(3, 22, "terrainCarrots", 110001);
		loadTerrain(3, 22, "terrainCarrotsAccent", 110201);
		loadTerrain(4, 22, "terrainCarrotsCollected", 110401);

		loadTerrain(2, 19, "wallStone", 1000001);
		loadObject(66, 99, 1, 1, "wallStoneConstructionPhase1", 1001001);
		loadObject(66, 98, 1, 1, "wallStoneConstructionPhase2", 1001201);
		loadObject(66, 97, 1, 1, "wallStoneConstructionPhase3", 1001401);
		loadObject(66, 96, 1, 1, "wallStoneConstructionPhase4", 1001601);
		loadObject(66, 95, 1, 1, "wallStoneConstructionPhase5", 1001801);
		loadObject(67, 95, 1, 1, "wallStoneConstruction", 1002201);

		loadTerrain(2, 20, "wallStoneSturdy", 1010001);
		loadObject(66, 104, 1, 1, "wallStoneSturdyConstructionPhase1", 1011001);
		loadObject(66, 103, 1, 1, "wallStoneSturdyConstructionPhase2", 1011201);
		loadObject(66, 102, 1, 1, "wallStoneSturdyConstructionPhase3", 1011401);
		loadObject(66, 101, 1, 1, "wallStoneSturdyConstructionPhase4", 1011601);
		loadObject(66, 100, 1, 1, "wallStoneSturdyConstructionPhase5", 1011801);
		loadObject(67, 100, 1, 1, "wallStoneSturdyConstruction", 1012201);

		loadTerrain(2, 21, "wallWoodBasic", 1020001);
		loadObject(66, 109, 1, 1, "wallWoodBasicConstructionPhase1", 1021001);
		loadObject(66, 108, 1, 1, "wallWoodBasicConstructionPhase2", 1021201);
		loadObject(66, 107, 1, 1, "wallWoodBasicConstructionPhase3", 1021401);
		loadObject(66, 106, 1, 1, "wallWoodBasicConstructionPhase4", 1021601);
		loadObject(66, 105, 1, 1, "wallWoodBasicConstructionPhase5", 1021801);
		loadObject(67, 105, 1, 1, "wallWoodBasicConstruction", 1022201);

		loadObject(0, 28, 11, 7, "objectLumberMill", 2000001);
		loadObject(0, 21, 11, 7, "objectLumberMillFill1", 2000201);
		loadObject(0, 14, 11, 7, "objectLumberMillFill2", 2000401);
		loadObject(0, 7, 11, 7, "objectLumberMillFill3", 2000601);
		loadObject(0, 0, 11, 7, "objectLumberMillFill4", 2000801);
		loadObject(11, 28, 11, 7, "objectLumberMillConstructionPhase1", 2001001);
		loadObject(11, 21, 11, 7, "objectLumberMillConstructionPhase2", 2001201);
		loadObject(11, 14, 11, 7, "objectLumberMillConstructionPhase3", 2001401);
		loadObject(11, 7, 11, 7, "objectLumberMillConstructionPhase4", 2001601);
		loadObject(11, 0, 11, 7, "objectLumberMillConstructionPhase5", 2001801);
		loadObject(0, 35, 11, 7, "objectLumberMillAbandoned", 2002001);
		loadObject(11, 35, 11, 7, "objectLumberMillConstruction", 2002201);

		loadObject(22, 24, 8, 6, "objectLumberShack", 2010001);
		loadObject(22, 18, 8, 6, "objectLumberShackFill1", 2010201);
		loadObject(22, 12, 8, 6, "objectLumberShackFill2", 2010401);
		loadObject(22, 6, 8, 6, "objectLumberShackFill3", 2010601);
		loadObject(22, 0, 8, 6, "objectLumberShackFill4", 2010801);
		loadObject(30, 24, 8, 6, "objectLumberShackConstructionPhase1", 2011001);
		loadObject(30, 18, 8, 6, "objectLumberShackConstructionPhase2", 2011201);
		loadObject(30, 12, 8, 6, "objectLumberShackConstructionPhase3", 2011401);
		loadObject(30, 6, 8, 6, "objectLumberShackConstructionPhase4", 2011601);
		loadObject(30, 0, 8, 6, "objectLumberShackConstructionPhase5", 2011801);
		loadObject(30, 30, 8, 6, "objectLumberShackConstruction", 2012001);
		loadObject(22, 30, 8, 6, "objectLumberShackAbandoned", 2012201);

		loadObject(32, 43, 5, 5, "objectSmallHovel", 2020001);
		loadObject(32, 68, 5, 5, "objectSmallHovelConstructionPhase1", 2021001);
		loadObject(32, 63, 5, 5, "objectSmallHovelConstructionPhase2", 2021201);
		loadObject(32, 58, 5, 5, "objectSmallHovelConstructionPhase3", 2021401);
		loadObject(32, 53, 5, 5, "objectSmallHovelConstructionPhase4", 2021601);
		loadObject(32, 48, 5, 5, "objectSmallHovelConstructionPhase5", 2021801);
		loadObject(32, 73, 5, 5, "objectSmallHovelConstruction", 2022001);
		loadObject(32, 78, 5, 5, "objectSmallHovelAbandoned", 2022201);

		loadObject(37, 43, 4, 4, "objectSmallShack", 2030001);
		loadObject(37, 63, 4, 4, "objectSmallShackConstructionPhase1", 2031001);
		loadObject(37, 59, 4, 4, "objectSmallShackConstructionPhase2", 2031201);
		loadObject(37, 55, 4, 4, "objectSmallShackConstructionPhase3", 2031401);
		loadObject(37, 51, 4, 4, "objectSmallShackConstructionPhase4", 2031601);
		loadObject(37, 47, 4, 4, "objectSmallShackConstructionPhase5", 2031801);
		loadObject(37, 67, 4, 4, "objectSmallShackConstruction", 2032001);
		loadObject(37, 71, 4, 4, "objectSmallShackAbandoned", 2032201);

		loadObject(41, 43, 4, 4, "objectTent", 2040001);
		loadObject(41, 63, 4, 4, "objectTentConstructionPhase1", 2041001);
		loadObject(41, 59, 4, 4, "objectTentConstructionPhase2", 2041201);
		loadObject(41, 55, 4, 4, "objectTentConstructionPhase3", 2041401);
		loadObject(41, 51, 4, 4, "objectTentConstructionPhase4", 2041601);
		loadObject(41, 47, 4, 4, "objectTentConstructionPhase5", 2041801);
		loadObject(41, 67, 4, 4, "objectTentConstruction", 2042001);
		loadObject(41, 71, 4, 4, "objectTentAbandoned", 2042201);

		loadObject(0, 112, 7, 7, "objectSmallFarm", 2050001);
		loadObject(0, 105, 7, 7, "objectSmallFarmFill1", 2050201);
		loadObject(0, 98, 7, 7, "objectSmallFarmFill2", 2050401);
		loadObject(0, 91, 7, 7, "objectSmallFarmFill3", 2050601);
		loadObject(0, 84, 7, 7, "objectSmallFarmFill4", 2050801);
		loadObject(7, 112, 7, 7, "objectSmallFarmConstructionPhase1", 2051001);
		loadObject(7, 105, 7, 7, "objectSmallFarmConstructionPhase2", 2051201);
		loadObject(7, 98, 7, 7, "objectSmallFarmConstructionPhase3", 2051401);
		loadObject(7, 91, 7, 7, "objectSmallFarmConstructionPhase4", 2051601);
		loadObject(7, 84, 7, 7, "objectSmallFarmConstructionPhase5", 2051801);
		loadObject(7, 119, 7, 7, "objectSmallFarmConstruction", 2052001);
		loadObject(0, 119, 7, 7, "objectSmallFarmAbandoned", 2052201);

		loadObject(0, 70, 9, 7, "objectFarm", 2060001);
		loadObject(0, 63, 9, 7, "objectFarmFill1", 2060201);
		loadObject(0, 56, 9, 7, "objectFarmFill2", 2060401);
		loadObject(0, 49, 9, 7, "objectFarmFill3", 2060601);
		loadObject(0, 42, 9, 7, "objectFarmFill4", 2060801);
		loadObject(9, 70, 9, 7, "objectFarmConstructionPhase1", 2061001);
		loadObject(9, 63, 9, 7, "objectFarmConstructionPhase2", 2061201);
		loadObject(9, 56, 9, 7, "objectFarmConstructionPhase3", 2061401);
		loadObject(9, 49, 9, 7, "objectFarmConstructionPhase4", 2061601);
		loadObject(9, 42, 9, 7, "objectFarmConstructionPhase5", 2061801);
		loadObject(9, 77, 9, 7, "objectFarmConstruction", 2062001);
		loadObject(0, 77, 9, 7, "objectFarmAbandoned", 2062201);

		loadObject(18, 70, 7, 7, "objectFoodStorage", 2070001);
		loadObject(18, 63, 7, 7, "objectFoodStorageFill1", 2070201);
		loadObject(18, 56, 7, 7, "objectFoodStorageFill2", 2070401);
		loadObject(18, 49, 7, 7, "objectFoodStorageFill3", 2070601);
		loadObject(18, 42, 7, 7, "objectFoodStorageFill4", 2070801);
		loadObject(25, 70, 7, 7, "objectFoodStorageConstructionPhase1", 2071001);
		loadObject(25, 63, 7, 7, "objectFoodStorageConstructionPhase2", 2071201);
		loadObject(25, 56, 7, 7, "objectFoodStorageConstructionPhase3", 2071401);
		loadObject(25, 49, 7, 7, "objectFoodStorageConstructionPhase4", 2071601);
		loadObject(25, 42, 7, 7, "objectFoodStorageConstructionPhase5", 2071801);
		loadObject(25, 77, 7, 7, "objectFoodStorageConstruction", 2072001);
		loadObject(18, 77, 7, 7, "objectFoodStorageAbandoned", 2072201);

		loadObject(62, 0, 13, 13, "objectVillageCenter", 2080001);
		loadObject(62, 65, 13, 13, "objectVillageCenterConstructionPhase1", 2081001);
		loadObject(62, 52, 13, 13, "objectVillageCenterConstructionPhase2", 2081201);
		loadObject(62, 39, 13, 13, "objectVillageCenterConstructionPhase3", 2081401);
		loadObject(62, 26, 13, 13, "objectVillageCenterConstructionPhase4", 2081601);
		loadObject(62, 13, 13, 13, "objectVillageCenterConstructionPhase5", 2081801);
		loadObject(62, 78, 13, 13, "objectVillageCenterConstruction", 2082001);

		loadObject(46, 36, 8, 9, "objectStoneMasonry", 2090001);
		loadObject(46, 27, 8, 9, "objectStoneMasonryFill1", 2090201);
		loadObject(46, 18, 8, 9, "objectStoneMasonryFill2", 2090401);
		loadObject(46, 9, 8, 9, "objectStoneMasonryFill3", 2090601);
		loadObject(46, 0, 8, 9, "objectStoneMasonryFill4", 2090801);
		loadObject(54, 36, 8, 9, "objectStoneMasonryConstructionPhase1", 2091001);
		loadObject(54, 27, 8, 9, "objectStoneMasonryConstructionPhase2", 2091201);
		loadObject(54, 18, 8, 9, "objectStoneMasonryConstructionPhase3", 2091401);
		loadObject(54, 9, 8, 9, "objectStoneMasonryConstructionPhase4", 2091601);
		loadObject(54, 0, 8, 9, "objectStoneMasonryConstructionPhase5", 2091801);
		loadObject(54, 45, 8, 9, "objectStoneMasonryConstruction", 2092001);
		loadObject(46, 45, 8, 9, "objectStoneMasonryAbandoned", 2092201);

		loadObject(46, 78, 8, 6, "objectStoneShack", 2100001);
		loadObject(46, 72, 8, 6, "objectStoneShackFill1", 2100201);
		loadObject(46, 66, 8, 6, "objectStoneShackFill2", 2100401);
		loadObject(46, 60, 8, 6, "objectStoneShackFill3", 2100601);
		loadObject(46, 54, 8, 6, "objectStoneShackFill4", 2100801);
		loadObject(54, 78, 8, 6, "objectStoneShackConstructionPhase1", 2101001);
		loadObject(54, 72, 8, 6, "objectStoneShackConstructionPhase2", 2101201);
		loadObject(54, 66, 8, 6, "objectStoneShackConstructionPhase3", 2101401);
		loadObject(54, 60, 8, 6, "objectStoneShackConstructionPhase4", 2101601);
		loadObject(54, 54, 8, 6, "objectStoneShackConstructionPhase5", 2101801);
		loadObject(54, 84, 8, 6, "objectStoneShackConstruction", 2102001);
		loadObject(46, 84, 8, 6, "objectStoneShackAbandoned", 2102201);

		loadObject(38, 0, 1, 1, "objectSmallWoodTorch", 2110001);
		loadObject(38, 5, 1, 1, "objectSmallWoodTorchConstructionPhase1", 2111001);
		loadObject(38, 4, 1, 1, "objectSmallWoodTorchConstructionPhase2", 2111201);
		loadObject(38, 3, 1, 1, "objectSmallWoodTorchConstructionPhase3", 2111401);
		loadObject(38, 2, 1, 1, "objectSmallWoodTorchConstructionPhase4", 2111601);
		loadObject(38, 1, 1, 1, "objectSmallWoodTorchConstructionPhase5", 2111801);
		loadObject(38, 6, 1, 1, "objectSmallWoodTorchConstruction", 2112001);
		loadObject(38, 7, 1, 1, "objectSmallWoodTorchAbandoned", 2112201);
	}

	void reloadSheet() throws SlickException {
		this.terrain.destroy();
		this.terrainCollision.destroy();
		this.terrainShadows.destroy();
		this.terrain = new Image("res/maps/terrain.png", false, 9728);
		this.terrainCollision = new Image("res/maps/terrainCollision.png", false, 9728);
		this.terrainShadows = new Image("res/maps/terrainShadows.png", false, 9728);
	}

	private void loadTerrain(int posX, int posY, String sheetName, int startGID) {
		loadTerrain(posX, posY, sheetName, sheetName, startGID);
	}

	private void loadTerrain(int posX, int posY, String sheetName, String propertiesName, int startGID) {
		Properties properties = new Properties();
		InputStream input = null;
		this.tileSheets.add(new SpriteSheet(this.terrain.getSubImage(posX * 200, posY * 40, 200, 40), 8, 8));
		this.tileSheetsCollision.add(new SpriteSheet(this.terrainCollision.getSubImage(posX * 200, posY * 40, 200, 40), 8, 8));
		this.tileSheetsShadows.add(new SpriteSheet(this.terrainShadows.getSubImage(posX * 200, posY * 40, 200, 40), 8, 8));

		int tileSheet = this.tileSheets.size() - 1;
		int tileSheetH = ((SpriteSheet) this.tileSheets.get(tileSheet)).getHorizontalCount();
		int tileSheetV = ((SpriteSheet) this.tileSheets.get(tileSheet)).getVerticalCount();

		boolean hasFile = false;
		this.tileSetToGID.put(sheetName, Integer.valueOf(startGID));
		this.GIDToTileSet.put(startGID, sheetName);
		try {
			input = new FileInputStream("res/maps/" + sheetName + ".properties");
			properties.load(input);
			hasFile = true;
		} catch (IOException e) {
			System.out.println("No properties file found for " + sheetName);
		}

		for (int x = 0; x < tileSheetH; x++) {
			for (int y = 0; y < tileSheetV; y++) {
				this.tiles.put(startGID + x * tileSheetV + y, ((SpriteSheet) this.tileSheets.get(tileSheet)).getSprite(x, y));
				this.tilesCollision.put(startGID + x * tileSheetV + y, ((SpriteSheet) this.tileSheetsCollision.get(tileSheet)).getSprite(x, y));
				this.tilesShadows.put(startGID + x * tileSheetV + y, ((SpriteSheet) this.tileSheetsShadows.get(tileSheet)).getSprite(x, y));
				this.tileCoordinates.put(startGID + x * tileSheetV + y, OrderedPair.getOrderedPair(posX * 200 / 8 + x, posY * 40 / 8 + y));

				if (hasFile) {
					for (int p = 0; p < this.propertiesList.length; p++) {
						if (!properties.getProperty(x * tileSheetV + y + 1 + "_" + this.propertiesList[p], "null").equals("null")) {
							this.tileSetProperties.setProperty(startGID + x * tileSheetV + y + "_" + this.propertiesList[p], properties.getProperty(x * tileSheetV + y + 1 + "_" + this.propertiesList[p]));
						}
					}
				}
			}
		}

		for (int p = 0; p < this.propertiesList.length; p++)
			if (!properties.getProperty(this.propertiesList[p], "null").equals("null"))
				this.tileSetProperties.setProperty(sheetName + "_" + this.propertiesList[p], properties.getProperty(this.propertiesList[p]));
	}

	private void loadObject(int posX, int posY, int objW, int objH, String sheetName, int startGID) {
		loadObject(posX, posY, objW, objH, sheetName, sheetName, startGID);
	}

	private void loadObject(int posX, int posY, int objW, int objH, String sheetName, String propertiesName, int startGID) {
		Properties properties = new Properties();
		InputStream input = null;
		this.tileSheets.add(new SpriteSheet(this.terrain.getSubImage(posX * 8, posY * 8, objW * 8, objH * 8), 8, 8));
		this.tileSheetsCollision.add(new SpriteSheet(this.terrainCollision.getSubImage(posX * 8, posY * 8, objW * 8, objH * 8), 8, 8));
		this.tileSheetsShadows.add(new SpriteSheet(this.terrainShadows.getSubImage(posX * 8, posY * 8, objW * 8, objH * 8), 8, 8));

		int tileSheet = this.tileSheets.size() - 1;
		int tileSheetH = ((SpriteSheet) this.tileSheets.get(tileSheet)).getHorizontalCount();
		int tileSheetV = ((SpriteSheet) this.tileSheets.get(tileSheet)).getVerticalCount();

		boolean hasFile = false;
		this.tileSetToGID.put(sheetName, Integer.valueOf(startGID));
		this.GIDToTileSet.put(startGID, sheetName);

		this.objects.put(startGID, this.terrain.getSubImage(posX * 8, posY * 8, objW * 8, objH * 8));
		try {
			input = new FileInputStream("res/maps/" + propertiesName + ".properties");
			properties.load(input);
			hasFile = true;
		} catch (IOException e) {
			System.out.println("No properties file found for " + propertiesName);
		}

		for (int x = 0; x < tileSheetH; x++) {
			for (int y = 0; y < tileSheetV; y++) {
				this.tiles.put(startGID + x * tileSheetV + y, ((SpriteSheet) this.tileSheets.get(tileSheet)).getSprite(x, y));
				this.tilesCollision.put(startGID + x * tileSheetV + y, ((SpriteSheet) this.tileSheetsCollision.get(tileSheet)).getSprite(x, y));
				this.tilesShadows.put(startGID + x * tileSheetV + y, ((SpriteSheet) this.tileSheetsShadows.get(tileSheet)).getSprite(x, y));
				this.tileCoordinates.put(startGID + x * tileSheetV + y, OrderedPair.getOrderedPair(posX + x, posY + y));

				if (hasFile) {
					for (int p = 0; p < this.propertiesList.length; p++) {
						if (!properties.getProperty(x * tileSheetV + y + 1 + "_" + this.propertiesList[p], "null").equals("null")) {
							this.tileSetProperties.setProperty(startGID + x * tileSheetV + y + "_" + this.propertiesList[p], properties.getProperty(x * tileSheetV + y + 1 + "_" + this.propertiesList[p]));
						}
					}
				}
			}
		}

		for (int p = 0; p < this.propertiesList.length; p++) {
			if (!properties.getProperty(this.propertiesList[p], "null").equals("null")) {
				this.tileSetProperties.setProperty(sheetName + "_" + this.propertiesList[p], properties.getProperty(this.propertiesList[p]));
			}
		}
		this.tileSetProperties.setProperty(sheetName + "_objectHeight", Integer.toString(objH));
		this.tileSetProperties.setProperty(sheetName + "_objectWidth", Integer.toString(objW));
	}

	public int getTileSetGID(String tileSet) {
		return ((Integer) this.tileSetToGID.get(tileSet)).intValue();
	}

	public int getTileSetGID(int tileId) {
		return tileId / 200 * 200 + 1;
	}

	public OrderedPair getTileCoordinates(int tileId) {
		return (OrderedPair) this.tileCoordinates.get(tileId);
	}

	public TIntObjectHashMap<Image> getTiles() {
		return this.tiles;
	}

	public TIntObjectHashMap<Image> getTilesCollision() {
		return this.tilesCollision;
	}

	public TIntObjectHashMap<Image> getTilesShadows() {
		return this.tilesShadows;
	}

	public Image getTerrain() {
		return this.terrain;
	}

	public Image getTerrainCollision() {
		return this.terrainCollision;
	}

	public Image getTerrainShadows() {
		return this.terrainShadows;
	}

	public Image getObjectImage(int tileGID) {
		return (Image) this.objects.get(tileGID);
	}

	public Image getWallImage(int tileGID) {
		return (Image) this.tiles.get(tileGID + 79);
	}

	public String getTileProperty(int tileID, String property, String def) {
		return this.tileSetProperties.getProperty(tileID + "_" + property, def);
	}

	public String getTileSetProperty(String tileSet, String property, String def) {
		return this.tileSetProperties.getProperty(tileSet + "_" + property, def);
	}

	public String getTileSetPropertyByID(int tileId, String property, String def) {
		String tileSet = (String) this.GIDToTileSet.get(getTileSetGID(tileId));
		return this.tileSetProperties.getProperty(tileSet + "_" + property, def);
	}

	public String getTileSetByGID(int tileId) {
		return (String) this.GIDToTileSet.get(getTileSetGID(tileId));
	}
}
