package com.example.surfacetest;

import android.graphics.Bitmap;

public class PlayerSprite {
	private int direction;
	private int step;
	private int xLoc;
	private int yLoc;
	
	public PlayerSprite(int x, int y) {
		direction = 0;
		step = 1;
		xLoc = x;
		yLoc = y;
	}
	
	public void faceLeft() {
		direction = 0;
		xLoc--;
	}
	public void faceUp() {
		direction = 1;
		yLoc--;
	}
	public void faceRight() {
		direction = 2;
		xLoc++;
	}
	public void faceDown() {
		direction = 3;
		yLoc++;
	}
	
	public void iterate() {
		if (step == 1)
			step = 2;
		else
			step = 1;
	}
	
	public Bitmap getBitmap() {
		// left facing
		if (direction == 0) {
			if (step == 1)
				return SpriteSheet.getTileBitmap("hero1_1");
			else
				return SpriteSheet.getTileBitmap("hero1_2");
		}
		else if (direction == 1) {
			if (step == 1)
				return SpriteSheet.getTileBitmap("hero2_1");
			else 
				return SpriteSheet.getTileBitmap("hero2_2");
		}
		else if (direction == 2) {
			if (step == 1)
				return SpriteSheet.getTileBitmap("hero3_1");
			else
				return SpriteSheet.getTileBitmap("hero3_2");
		}
		else {
			if (step == 1)
				return SpriteSheet.getTileBitmap("hero4_1");
			else
				return SpriteSheet.getTileBitmap("hero4_2");
		}
	}
	
	public int getX() {
		return xLoc;
	}
	
	public int getY() {
		return yLoc;
	}
}
