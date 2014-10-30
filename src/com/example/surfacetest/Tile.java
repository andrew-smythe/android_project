package com.example.surfacetest;
import android.graphics.Bitmap;
import java.util.ArrayList;

public class Tile implements Cloneable {
	private Bitmap img = null;
	private Boolean walkable = false;
	private String type;
	
	// Inner object -- may be converted to a class soon
	private Boolean hasObj = false;
	//private ArrayList<Bitmap> oImg;
	//private ArrayList<String> oType;
	private int numObj;
	private int oID;
	private int foID;
	private String oType;
	private String foType;
	
	// Constructor -- sets up the tile
	public Tile(String b, Boolean w, int id, int fid, DBHandler db) {
		type = b;
		img = SpriteSheet.getTileBitmap(type);
		walkable = w;
		numObj = 0;
		oID = id;
		foID = fid;
		if (oID > 0)
			oType = db.getObj(oID);
		if (foID > 0)
			foType = db.getObj(foID);
	}
	
	// Adds an object to the tile -- ie. part of a tree, or a treasure chest
	public void addObject(Bitmap b, String s)
	{
		/*oImg.add(b);
		hasObj = true;
		oType.add(s);
		numObj++;*/
	}
	
	// returns the bitmap for drawing
	public Bitmap getBitmap() {
		return img;
	}
	
	// gets the object's bitmap
	public String getObject() {
		return oType;
	}
	
	// gets the object's bitmap
	public String getFlatObject() {
		return foType;
	}
	
	// get the number of objects on the tile
	public int getNumObjects() {
		return numObj;
	}
	
	public Boolean isWalkable() {
		return walkable;
	}
	
	// toggle walkable
	public void toggleWalkable() {
		if (walkable)
			walkable = false;
		else
			walkable = true;
	}
	
	public boolean hasObject() {
		if (oID == -1)
			return false;
		return true;
	}
	
	public boolean hasFlatObject() {
		if (foID == -1)
			return false;
		return true;
	}
	
	// show the tile type
	public String toString() {
		return type;
	}
}
