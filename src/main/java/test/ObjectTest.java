package test;

import javax.swing.JOptionPane;

import org.newdawn.slick.SlickException;

import rpc.objects.ObjectBase;

public class ObjectTest extends ObjectBase {
	public ObjectTest(int tileX, int tileY, int oW, int oH, int sT) throws SlickException {
		super(tileX, tileY, oW, oH);
		this.name = "ObjectTest";

		this.baseType = "objectTest";
		this.subType = 2;
		JOptionPane.showConfirmDialog(null, "It works!");
		initObject();
	}

	protected void initBuilt() throws SlickException {
		this.map.updateObject(this.objectX, this.objectY, baseType);
		this.baseRange = 32;
		this.hitPointsMax = 50;

		this.rangeX = 0;
		this.rangeY = 0;
	}
}
