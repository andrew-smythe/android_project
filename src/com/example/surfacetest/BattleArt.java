package com.example.surfacetest;
import java.util.HashMap;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BattleArt {
	private Bitmap backgrounds;
	private Bitmap charSheet;
	
	private int screenX;
	private int screenY;
	
	private HashMap<String, Bitmap> battleArt;
	
	public BattleArt(Resources r, int screenWidth, int screenHeight) {
		screenX=screenWidth;
		screenY=screenHeight;
		backgrounds = BitmapFactory.decodeResource(r, R.drawable.backgrounds);
		charSheet = BitmapFactory.decodeResource(r, R.drawable.sprites);
		initializeBattleArt();
	}
	
	// Initializes the hash map of tile arts
	private void initializeBattleArt() {
		battleArt = new HashMap<String, Bitmap>();
		battleArt.put("battle1", Bitmap.createScaledBitmap(Bitmap.createBitmap(backgrounds, 0, 128, 640, 192), screenX, screenY, true));
		battleArt.put("hero1_1", Bitmap.createScaledBitmap(Bitmap.createBitmap(charSheet, 362, 25, 16, 16), 55, 55, true));
		battleArt.put("hero1_2", Bitmap.createScaledBitmap(Bitmap.createBitmap(charSheet, 379, 25, 16, 16), 55, 55, true));
		battleArt.put("villain1", Bitmap.createScaledBitmap(Bitmap.createBitmap(charSheet, 134, 288, 16, 16), 55, 55, true));
		
	}
	public Bitmap getTileBitmap(String s) {
		return(battleArt.get(s));
	}
}
