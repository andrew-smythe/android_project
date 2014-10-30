package com.example.surfacetest;
import android.graphics.Canvas;
import android.content.Context;
import android.database.CursorIndexOutOfBoundsException;
import android.util.Log;

import java.util.ArrayList;

public class WorldMap {
	private Tile[][] map = null;
	private int sizeX;
	private int sizeY;
	private int xOffset;
	private int yOffset;
	private int viewSize;
	private DBHandler db;
	
	// Create a WorldMap -- we must create the entire world
	public WorldMap(int x, int y, int size, int xOff, int yOff, Context context) {
		
		// get size information
		sizeX = x;
		sizeY = y;
		xOffset = xOff;
		yOffset = yOff;
		
		// initialize the map
		map = new Tile[size+2][size+2];
		
		if (map == null)
			Log.d("ERROR", "No tiles allocated!");
			
		Log.d("Map size", ""+map.length);
		
		// get the viewport size
		viewSize = size;
		
		// initialize database handler
		db = new DBHandler(context);
		//db.onUpgrade(db.getWritableDatabase(), 1, 1);

		// check if the database is empty
		if (db.hasEmptyMap()) {
			Log.d("DB", "The database is currently empty.");

			// create a blank world! (covered in grass)
			db.addTiles();
			
			// add object types to the database
			db.addObject("tree");			// 1
			db.addObject("bush");			// 2
			db.addObject("shrub1");			// 3
			db.addObject("shrub2");			// 4
			db.addObject("flowers");		// 5
			db.addObject("cactus");			// 6
			db.addObject("small_rock1");	// 7
			db.addObject("crack1");			// 8
			db.addObject("crack2");			// 9
			
			// house objects
			db.addObject("house0_0");		// 10
			db.addObject("house0_1");		// 11
			db.addObject("house0_2");		// 12
			db.addObject("house0_3");		// 13
			db.addObject("house0_4");		// 14
			db.addObject("house0_5");		// 15
			db.addObject("house1_0");		// 16
			db.addObject("house1_1");		// 17
			db.addObject("house1_2");		// 18
			db.addObject("house1_3");		// 19
			db.addObject("house1_4");		// 20
			db.addObject("house1_5");		// 21
			db.addObject("house2_0");		// 22
			db.addObject("house2_1");		// 23
			db.addObject("house2_2");		// 24
			db.addObject("house2_3");		// 25
			db.addObject("house2_4");		// 26
			db.addObject("house2_5");		// 27
			db.addObject("house3_0");		// 28
			db.addObject("house3_1");		// 29
			db.addObject("house3_2");		// 30
			db.addObject("house3_3");		// 31
			db.addObject("house3_4");		// 32
			db.addObject("house3_5");		// 33
			db.addObject("house4_0");		// 34
			db.addObject("house4_1");		// 35
			
			db.addObject("house5_0");		// 36
			db.addObject("house5_1");		// 37
			db.addObject("house5_2");		// 38
			db.addObject("house5_3");		// 39
			db.addObject("house6_0");		// 40
			db.addObject("house6_1");		// 41
			db.addObject("house6_2");		// 42
			db.addObject("house6_3");		// 43
			db.addObject("house7_0");		// 44
			db.addObject("house7_1");		// 45
			db.addObject("house7_2");		// 46
			db.addObject("house7_3");		// 47
			db.addObject("house8_0");		// 48
			db.addObject("house8_1");		// 49
			db.addObject("house8_2");		// 50
			db.addObject("house8_3");		// 51
			db.addObject("house9_0");		// 52
			db.addObject("house9_1");		// 53
			db.addObject("house9_2");		// 54
			db.addObject("house9_3");		// 55
			db.addObject("house10_0");		// 56
			db.addObject("house10_1");		// 57
			db.addObject("house10_2");		// 58
			db.addObject("house10_3");		// 59
			
			db.addObject("npc1");			// 60
			db.addObject("sword");			// 61
		}
		else {
			Log.d("DB", "The database is not empty.");
		}
		
		// create the current map
		for (int i = -1; i < viewSize+1; i++) {
			for (int j = -1; j < viewSize+1; j++) {
				map[i+1][j+1] = db.getTile((i+1)+xOffset, (j+1)+yOffset);
			}
		}
		
		/* DEBUG
		  for (int i = 0; i < x; i++) {
			for (int j = 0; j < y; j++) {
				Log.d("Tile "+i+", "+j, ""+db.getTile(x, y));
			}
		}*/
	}
	
	public Tile[][] getMap() {
		/*if (offX+size > sizeX || offY+size > sizeY) {
			return null;
		}
		else {
			// create a subset of the world map
			Tile[][] subMap = new Tile[size][size];
			
			// fill the submap
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					subMap[i][j] = db.getTile(i+offX, j+offY);
					//subMap[i][j] = map[i+offX][j+offY];
				}
			}
			// return the submap
			return subMap;
		}*/
		return map;
	}
	
	// method to shift the map in a certain direction
	public void shiftMap(int dir) {
		if (dir == 0) {
			// shift the map to the left
			xOffset++;
			for (int i = 0; i < (map.length-1); i++) {
				map[i] = map[i+1].clone();
			}
			//for (int i = 0; i < (map[map.length-1].length); i++) {
				//map[map.length-1][i] = db.getTile((map.length-1)+xOffset, i+yOffset);
			//}
			map[map.length-1] = db.getCol(map.length-1+xOffset, yOffset, map.length);
		}
		else if (dir == 1) {
			// shift the map upwards
			yOffset++;
			for (int i = 0; i < map.length; i++) {
				for (int j = 0; j < (map[i].length-1); j++) {
					map[i][j] = map[i][j+1];
					
				}
			}
			// fetch the new row
			Tile[] newRow = db.getRow(yOffset+(map.length-1), xOffset, map.length);
			for (int i = 0; i < map.length; i++) {
				map[i][map[i].length-1] = newRow[i];
			}	
		}
		else if (dir == 2) {
			// shift the map to the right
			xOffset--;
			for (int i = map.length-1; i > 0; i--) {
				map[i] = map[i-1].clone();
			}
			//for (int i = 0; i < (map[0].length); i++) {
				//map[0][i] = db.getTile(xOffset, i+yOffset);
			//}
			map[0] = db.getCol(xOffset, yOffset, map.length);
		}
		else if (dir == 3) {
			// shift the map downwards
			yOffset--;
			for (int i = 0; i < map.length; i++) {
				for (int j = map[i].length-1; j > 0; j--) {
					map[i][j] = map[i][j-1];
				}
			}
			// fetch the new row
			Tile[] newRow = db.getRow(yOffset, xOffset, map.length);
			for (int i = 0; i < map.length; i++) {
				map[i][0] = newRow[i];
			}
		}	
		Log.d("X offset:", xOffset+"");
		Log.d("Y offset: ", ""+yOffset);	
	}
	
	// method for drawing a tree to the world map
	public void drawTree(int x, int y) {
		/* 
		 * TO DO: Update this to work with the database.
		 
		// check that the specified area lies within
		// the world map
		if (x+3 > sizeX || y+4 > sizeY)
			return;
		
		// check that there is nothing that will
		// intersect with our tree
		Boolean intersect = false;
		//for (int i = x; i < x+3; i++) {
		//	for (int j = y; j < y+2; j++) {
		//		if (!map[i][j].isWalkable())
		//			intersect = true;	
		//	}
		//}
		
		for (int i = y+2; i < y+4; i++) {
			if (!map[x+1][i].isWalkable())
				intersect = true;	
		}
		
		// if there is an intersection, end the function
		if (intersect)
			return;
		
		// continue to draw the tree
		for (int i = x; i < x+3; i++) {
			for (int j = y; j < y+2; j++) {
				// add an object to the tile
				map[i][j].addObject(SpriteSheet.getTileBitmap("treeTop"+(((i-x)*2)+(j-y))), "treeTop"+(((i-x)*2)+(j-y)));
			}
		}
		
		// draw the tree trunk
		for (int i = y+2; i < y+4; i++) {
			// add an object to the tile
			map[x+1][i].addObject(SpriteSheet.getTileBitmap("treeTrunk"+(i-(y+2))), "treeTrunk"+(i-(y+2)));
		}
		// can't walk through a tree trunk
		map[x+1][y+3].toggleWalkable();
		*/
	}
	
	public int getXOffset() {
		return xOffset;
	}
	public int getYOffset() {
		return yOffset;
	}
}
