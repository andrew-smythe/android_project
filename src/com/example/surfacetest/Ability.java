package com.example.surfacetest;

import android.graphics.Color;

public class Ability {
	String name;
	int base;
	int cost;
	String aModifier;
	String dModifier;
	String targetOptions;
	String damageType;
	int abl_color;
	Animation anima;
	
	public Ability(){
		this.name = "ATTACK";
		this.base = -10;
		this.cost = 0;
		this.aModifier = "pATK";
		this.dModifier = "pDEF";
		this.targetOptions = "1E";
		this.damageType = "Slashing";
		this.abl_color=Color.argb(255,137,137,150);
		this.anima=SpriteSheet.getAnimation("claw");
	}
	
	public Ability(String name,	int base, int cost, String aModifier, String dModifier, String targetOptions, String damageType, int color){
		this.name = name;
		this.base = base;
		this.cost = cost;
		this.aModifier = aModifier;
		this.dModifier = dModifier;
		this.targetOptions = targetOptions;
		this.damageType = damageType;	
		this.abl_color=color;
		this.anima=SpriteSheet.getAnimation("claw");

	}
}
