package com.example.surfacetest;
import java.util.HashMap;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

public class SpriteSheet {
	private static Bitmap tileSheet;
	private static Bitmap tileSheet2;
	private static Bitmap tileSheet3;
	private static Bitmap tileSheet4;
	private static Bitmap charSheet;
	private static Bitmap enemySheet;
	private static Bitmap swords;
	private static Bitmap claw;

	
	private static HashMap<String, Bitmap> tileArt;
	private static HashMap<String, Animation> abilityAnimation;

	
	public static void createSheet(Resources r, int tileSize) {
		// get the tile sheet from resources
		tileSheet = BitmapFactory.decodeResource(r, R.drawable.textures);
		charSheet = BitmapFactory.decodeResource(r, R.drawable.sprites);
		tileSheet2 = BitmapFactory.decodeResource(r, R.drawable.tiles2);
		tileSheet3 = BitmapFactory.decodeResource(r, R.drawable.tiles3);
		tileSheet4 = BitmapFactory.decodeResource(r, R.drawable.desert_tiled);
		enemySheet = BitmapFactory.decodeResource(r, R.drawable.baddies);
		swords = BitmapFactory.decodeResource(r, R.drawable.swords);
		claw = BitmapFactory.decodeResource(r, R.drawable.claw);
		
		// setup the hash map, and then start adding items to it
		initializeTileArt(tileSize);
	}
	
	// Initializes the hash map of tile arts
	private static void initializeTileArt(int tileSize) {
		tileArt = new HashMap<String, Bitmap>();
		abilityAnimation = new HashMap<String, Animation>();
		
		// grass textures
		tileArt.put("grass", Bitmap.createScaledBitmap(Bitmap.createBitmap(tileSheet3, 15, 526, 32, 32), tileSize, tileSize, true));
		tileArt.put("grass2", Bitmap.createScaledBitmap(Bitmap.createBitmap(tileSheet3, 15, 526, 32, 32), tileSize, tileSize, true));
		tileArt.put("grass3", Bitmap.createScaledBitmap(Bitmap.createBitmap(tileSheet3, 81, 525, 32, 32), tileSize, tileSize, true));
		
		// desert textures
		tileArt.put("desert", Bitmap.createScaledBitmap(Bitmap.createBitmap(tileSheet4, 166, 100, 32, 32), tileSize, tileSize, true));
				
		// path textures
		tileArt.put("path_dirt0", Bitmap.createScaledBitmap(Bitmap.createBitmap(tileSheet3, 32, 526, 32, 32), tileSize, tileSize, true));
		tileArt.put("path_dirt1", Bitmap.createScaledBitmap(Bitmap.createBitmap(tileSheet3, 0, 625, 32, 32), tileSize, tileSize, true));
		tileArt.put("path_dirt2", Bitmap.createScaledBitmap(Bitmap.createBitmap(tileSheet3, 32, 625, 32, 32), tileSize, tileSize, true));
		tileArt.put("path_dirt3", Bitmap.createScaledBitmap(Bitmap.createBitmap(tileSheet3, 0, 526, 32, 32), tileSize, tileSize, true));
				
		// water textures
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++)
				tileArt.put("water"+((i*3)+j),Bitmap.createScaledBitmap(Bitmap.createBitmap(tileSheet, 160+(32*i),128+(32*j), 32, 32), tileSize, tileSize, true));
		}
		// tree textures
		tileArt.put("tree1", Bitmap.createScaledBitmap(Bitmap.createBitmap(tileSheet3, 160, 1408, 32, 32), tileSize, tileSize, true));
		tileArt.put("tree2", Bitmap.createScaledBitmap(Bitmap.createBitmap(tileSheet3, 160, 1440, 32, 32), tileSize, tileSize, true));
		
		// bush texture
		tileArt.put("bush", Bitmap.createScaledBitmap(Bitmap.createBitmap(tileSheet3, 192, 1056, 32, 32), tileSize, tileSize, true));
		
		// shrub textures
		tileArt.put("shrub1", Bitmap.createScaledBitmap(Bitmap.createBitmap(tileSheet3, 0, 1024, 32, 32), tileSize, tileSize, true));
		tileArt.put("shrub2", Bitmap.createScaledBitmap(Bitmap.createBitmap(tileSheet3, 32, 1024, 32, 32), tileSize, tileSize, true));
		
		// flower textures
		tileArt.put("flowers", Bitmap.createScaledBitmap(Bitmap.createBitmap(tileSheet3, 64, 1184, 32, 32), tileSize, tileSize, true));
		
		// cactus textures
		tileArt.put("cactus", Bitmap.createScaledBitmap(Bitmap.createBitmap(tileSheet3, 192, 1120, 32, 32), tileSize, tileSize, true));
		
		// small rocks
		tileArt.put("small_rock1", Bitmap.createScaledBitmap(Bitmap.createBitmap(tileSheet3, 224, 1024, 32, 32), tileSize, tileSize, true));
		
		// crack
		tileArt.put("crack1", Bitmap.createScaledBitmap(Bitmap.createBitmap(tileSheet3, 192, 1280, 32, 32), tileSize, tileSize, true));
		tileArt.put("crack2", Bitmap.createScaledBitmap(Bitmap.createBitmap(tileSheet3, 192, 1312, 32, 32), tileSize, tileSize, true));
				
		// house texture objects
		tileArt.put("house0_0", Bitmap.createScaledBitmap(Bitmap.createBitmap(tileSheet2, 488, 248, 16, 16), tileSize, tileSize, true));
		tileArt.put("house0_1", Bitmap.createScaledBitmap(Bitmap.createBitmap(tileSheet2, 504, 248, 16, 16), tileSize, tileSize, true));
		tileArt.put("house0_2", Bitmap.createScaledBitmap(Bitmap.createBitmap(tileSheet2, 520, 248, 16, 16), tileSize, tileSize, true));
		tileArt.put("house0_3", Bitmap.createScaledBitmap(Bitmap.createBitmap(tileSheet2, 536, 248, 16, 16), tileSize, tileSize, true));
		tileArt.put("house0_4", Bitmap.createScaledBitmap(Bitmap.createBitmap(tileSheet2, 552, 248, 16, 16), tileSize, tileSize, true));
		tileArt.put("house0_5", Bitmap.createScaledBitmap(Bitmap.createBitmap(tileSheet2, 568, 248, 16, 16), tileSize, tileSize, true));
		
		tileArt.put("house1_0", Bitmap.createScaledBitmap(Bitmap.createBitmap(tileSheet2, 488, 232, 16, 16), tileSize, tileSize, true));
		tileArt.put("house1_1", Bitmap.createScaledBitmap(Bitmap.createBitmap(tileSheet2, 504, 232, 16, 16), tileSize, tileSize, true));
		tileArt.put("house1_2", Bitmap.createScaledBitmap(Bitmap.createBitmap(tileSheet2, 520, 232, 16, 16), tileSize, tileSize, true));
		tileArt.put("house1_3", Bitmap.createScaledBitmap(Bitmap.createBitmap(tileSheet2, 536, 232, 16, 16), tileSize, tileSize, true));
		tileArt.put("house1_4", Bitmap.createScaledBitmap(Bitmap.createBitmap(tileSheet2, 552, 232, 16, 16), tileSize, tileSize, true));
		tileArt.put("house1_5", Bitmap.createScaledBitmap(Bitmap.createBitmap(tileSheet2, 568, 232, 16, 16), tileSize, tileSize, true));
		
		tileArt.put("house2_0", Bitmap.createScaledBitmap(Bitmap.createBitmap(tileSheet2, 488, 216, 16, 16), tileSize, tileSize, true));
		tileArt.put("house2_1", Bitmap.createScaledBitmap(Bitmap.createBitmap(tileSheet2, 504, 216, 16, 16), tileSize, tileSize, true));
		tileArt.put("house2_2", Bitmap.createScaledBitmap(Bitmap.createBitmap(tileSheet2, 520, 216, 16, 16), tileSize, tileSize, true));
		tileArt.put("house2_3", Bitmap.createScaledBitmap(Bitmap.createBitmap(tileSheet2, 536, 216, 16, 16), tileSize, tileSize, true));
		tileArt.put("house2_4", Bitmap.createScaledBitmap(Bitmap.createBitmap(tileSheet2, 552, 216, 16, 16), tileSize, tileSize, true));
		tileArt.put("house2_5", Bitmap.createScaledBitmap(Bitmap.createBitmap(tileSheet2, 568, 216, 16, 16), tileSize, tileSize, true));
		
		tileArt.put("house3_0", Bitmap.createScaledBitmap(Bitmap.createBitmap(tileSheet2, 488, 200, 16, 16), tileSize, tileSize, true));
		tileArt.put("house3_1", Bitmap.createScaledBitmap(Bitmap.createBitmap(tileSheet2, 504, 200, 16, 16), tileSize, tileSize, true));
		tileArt.put("house3_2", Bitmap.createScaledBitmap(Bitmap.createBitmap(tileSheet2, 520, 200, 16, 16), tileSize, tileSize, true));
		tileArt.put("house3_3", Bitmap.createScaledBitmap(Bitmap.createBitmap(tileSheet2, 536, 200, 16, 16), tileSize, tileSize, true));
		tileArt.put("house3_4", Bitmap.createScaledBitmap(Bitmap.createBitmap(tileSheet2, 552, 200, 16, 16), tileSize, tileSize, true));
		tileArt.put("house3_5", Bitmap.createScaledBitmap(Bitmap.createBitmap(tileSheet2, 568, 200, 16, 16), tileSize, tileSize, true));
		
		tileArt.put("house4_0", Bitmap.createScaledBitmap(Bitmap.createBitmap(tileSheet2, 520, 184, 16, 16), tileSize, tileSize, true));
		tileArt.put("house4_1", Bitmap.createScaledBitmap(Bitmap.createBitmap(tileSheet2, 536, 184, 16, 16), tileSize, tileSize, true));

		tileArt.put("house5_0", Bitmap.createScaledBitmap(Bitmap.createBitmap(tileSheet2, 408, 167, 16, 16), tileSize, tileSize, true));
		tileArt.put("house5_1", Bitmap.createScaledBitmap(Bitmap.createBitmap(tileSheet2, 424, 167, 16, 16), tileSize, tileSize, true));
		tileArt.put("house5_2", Bitmap.createScaledBitmap(Bitmap.createBitmap(tileSheet2, 440, 167, 16, 16), tileSize, tileSize, true));
		tileArt.put("house5_3", Bitmap.createScaledBitmap(Bitmap.createBitmap(tileSheet2, 456, 167, 16, 16), tileSize, tileSize, true));
	
		tileArt.put("house6_0", Bitmap.createScaledBitmap(Bitmap.createBitmap(tileSheet2, 408, 151, 16, 16), tileSize, tileSize, true));
		tileArt.put("house6_1", Bitmap.createScaledBitmap(Bitmap.createBitmap(tileSheet2, 424, 151, 16, 16), tileSize, tileSize, true));
		tileArt.put("house6_2", Bitmap.createScaledBitmap(Bitmap.createBitmap(tileSheet2, 440, 151, 16, 16), tileSize, tileSize, true));
		tileArt.put("house6_3", Bitmap.createScaledBitmap(Bitmap.createBitmap(tileSheet2, 456, 151, 16, 16), tileSize, tileSize, true));
	
		tileArt.put("house7_0", Bitmap.createScaledBitmap(Bitmap.createBitmap(tileSheet2, 408, 135, 16, 16), tileSize, tileSize, true));
		tileArt.put("house7_1", Bitmap.createScaledBitmap(Bitmap.createBitmap(tileSheet2, 424, 135, 16, 16), tileSize, tileSize, true));
		tileArt.put("house7_2", Bitmap.createScaledBitmap(Bitmap.createBitmap(tileSheet2, 440, 135, 16, 16), tileSize, tileSize, true));
		tileArt.put("house7_3", Bitmap.createScaledBitmap(Bitmap.createBitmap(tileSheet2, 456, 135, 16, 16), tileSize, tileSize, true));
		
		tileArt.put("house8_0", Bitmap.createScaledBitmap(Bitmap.createBitmap(tileSheet2, 408, 119, 16, 16), tileSize, tileSize, true));
		tileArt.put("house8_1", Bitmap.createScaledBitmap(Bitmap.createBitmap(tileSheet2, 424, 119, 16, 16), tileSize, tileSize, true));
		tileArt.put("house8_2", Bitmap.createScaledBitmap(Bitmap.createBitmap(tileSheet2, 440, 119, 16, 16), tileSize, tileSize, true));
		tileArt.put("house8_3", Bitmap.createScaledBitmap(Bitmap.createBitmap(tileSheet2, 456, 119, 16, 16), tileSize, tileSize, true));

		tileArt.put("house9_0", Bitmap.createScaledBitmap(Bitmap.createBitmap(tileSheet2, 408, 103, 16, 16), tileSize, tileSize, true));
		tileArt.put("house9_1", Bitmap.createScaledBitmap(Bitmap.createBitmap(tileSheet2, 424, 103, 16, 16), tileSize, tileSize, true));
		tileArt.put("house9_2", Bitmap.createScaledBitmap(Bitmap.createBitmap(tileSheet2, 440, 103, 16, 16), tileSize, tileSize, true));
		tileArt.put("house9_3", Bitmap.createScaledBitmap(Bitmap.createBitmap(tileSheet2, 456, 103, 16, 16), tileSize, tileSize, true));
		
		tileArt.put("house10_0", Bitmap.createScaledBitmap(Bitmap.createBitmap(tileSheet2, 408, 87, 16, 16), tileSize, tileSize, true));
		tileArt.put("house10_1", Bitmap.createScaledBitmap(Bitmap.createBitmap(tileSheet2, 424, 87, 16, 16), tileSize, tileSize, true));
		tileArt.put("house10_2", Bitmap.createScaledBitmap(Bitmap.createBitmap(tileSheet2, 440, 87, 16, 16), tileSize, tileSize, true));
		tileArt.put("house10_3", Bitmap.createScaledBitmap(Bitmap.createBitmap(tileSheet2, 456, 87, 16, 16), tileSize, tileSize, true));
		
		
		// NPC sprites
		tileArt.put("npc1", Bitmap.createScaledBitmap(Bitmap.createBitmap(charSheet, 6, 68, 16, 16), tileSize, tileSize, true));
		
		
		// character battle sprites
		tileArt.put("hero1_1", Bitmap.createScaledBitmap(Bitmap.createBitmap(charSheet, 362, 25, 16, 16), tileSize, tileSize, true));
		tileArt.put("hero1_2", Bitmap.createScaledBitmap(Bitmap.createBitmap(charSheet, 379, 25, 16, 16), tileSize, tileSize, true));
		
		tileArt.put("hero2_1", Bitmap.createScaledBitmap(Bitmap.createBitmap(charSheet, 324, 25, 14, 16), tileSize, tileSize, true));
		tileArt.put("hero2_2", Bitmap.createScaledBitmap(Bitmap.createBitmap(charSheet, 343, 24, 16, 16), tileSize, tileSize, true));
		
		Matrix matrix = new Matrix();
		matrix.preScale(-1.0f, 1.0f);
		
		tileArt.put("hero3_1", Bitmap.createBitmap(tileArt.get("hero1_1"), 0, 0, tileSize, tileSize, matrix, true));
		tileArt.put("hero3_2", Bitmap.createBitmap(tileArt.get("hero1_2"), 0, 0, tileSize, tileSize, matrix, true));
		
		tileArt.put("hero4_1", Bitmap.createScaledBitmap(Bitmap.createBitmap(charSheet, 285, 24, 16, 16), tileSize, tileSize, true));
		tileArt.put("hero4_2", Bitmap.createScaledBitmap(Bitmap.createBitmap(charSheet, 304, 24, 16, 16), tileSize, tileSize, true));
		
		
		// enemy battle sprites
		tileArt.put("villain1", Bitmap.createScaledBitmap(Bitmap.createBitmap(charSheet, 134, 288, 16, 16), tileSize, tileSize, true));
		tileArt.put("sandWurm", Bitmap.createScaledBitmap(Bitmap.createBitmap(enemySheet, 386, 7, 64, 64), 128, 128, true));
		tileArt.put("fireCat", Bitmap.createScaledBitmap(Bitmap.createBitmap(enemySheet, 265, 430, 64, 48), 128, 96, true));
		tileArt.put("lizard", Bitmap.createScaledBitmap(Bitmap.createBitmap(enemySheet, 57, 96, 62, 47), 124, 94, true));
		tileArt.put("goblin", Bitmap.createScaledBitmap(Bitmap.createBitmap(enemySheet, 5, 39, 31, 32), tileSize, tileSize, true));
		tileArt.put("flyingEye", Bitmap.createScaledBitmap(Bitmap.createBitmap(enemySheet, 94, 23, 48, 48), 64, 64, true));
		
		// sword sprite
		tileArt.put("sword", Bitmap.createScaledBitmap(Bitmap.createBitmap(swords, 126, 326, 38, 38), tileSize, tileSize, true));
		
		abilityAnimation.put("claw", generateAnimation(claw, 11,30));
		
	}
	
	public static Animation generateAnimation(Bitmap source, int frameNumber, int duration){
		Animation anima = new Animation(frameNumber, duration);
		for(int j=0; j<Math.ceil(frameNumber/5);j++){
			for(int i=0; i<5; i++){
				if(j*5+i == frameNumber)return anima;
				anima.addFrame(Bitmap.createScaledBitmap(Bitmap.createBitmap(source, 192*i, 192*j, 192, 192), 64, 64, true));
			}
		}
		return anima;
	}
	
	public static Bitmap getTileBitmap(String s) {
		return(tileArt.get(s));
	}
	public static Animation getAnimation(String s){
		return(abilityAnimation.get(s));

		
	}
}
