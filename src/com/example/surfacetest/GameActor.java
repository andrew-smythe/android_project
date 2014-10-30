package com.example.surfacetest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
//import android.graphics.Bitmap;

import android.graphics.Bitmap;

public class GameActor implements Serializable {
	
	//Bitmap battleSprite;
	String name;
	int HP, MP, currHP, currMP, turn, locat;
	float xLoc, yLoc;
	Boolean isAlive =true;
	Bitmap battleSprite;
	Engine engine;
	ArrayList abilities;
	HashMap<String, Integer> stats;
	HashMap sEff, res;
	
	
	public GameActor(String name, Bitmap sprite, int hp, int mp, int pATK, int mATK, int mDEF, int pDEF, int SPD, ArrayList<Ability> abilities){
		this.name=name;
		this.battleSprite = sprite;
		this.HP=hp;
		this.currHP=hp;
		this.MP=mp;
		this.currMP=mp;
		this.turn=20;
		this.locat= 0;
		
		this.abilities = abilities;
		stats = new HashMap<String, Integer>();
		sEff = new HashMap();
		res = new HashMap();
		
		this.setStat("pATK", pATK);
		this.setStat("mATK", mATK);
		this.setStat("pDEF", pDEF);
		this.setStat("mDEF", mDEF);
		this.setStat("SPD", SPD);
		this.engine=engine;
	}
	
	Action startTurn(PlayerActor[] players, EnemyActor[] enemies)
	{
		return null;
	}
	
	public Bitmap getImage(){
		return battleSprite ;	
	}
	
	public ArrayList getAbilities(){
		return abilities;
	}
	
	public void addAbility(Ability newSkill) {
		this.abilities.add(newSkill);
	}
	
	public void removeAbility(Ability newSkill) {
		this.abilities.remove(newSkill);
	}
	
	public int getStat(String stat){
		return (Integer) stats.get(stat);
	}
	
	public void setStat(String stat, int value) {
		this.stats.put(stat, value);
	}
	
	public StatusEffect getStatusEffect(String effect){
		return (StatusEffect) sEff.get(effect);
	}
	
	public void setStatusEffect(String effect, StatusEffect statuseffect) {
		this.sEff.put(effect, statuseffect);
	}
	
	public Double getResistance(String resistance){
		return (Double) res.get(resistance);
	}
	
	public void setResistance(String resistance, Double value) {
		this.res.put(resistance, value);
	}

	public int getHP() {
		return HP;
	}

	public void setHP(int hP) {
		HP = hP;
	}

	public int getMP() {
		return MP;
	}

	public void setMP(int mP) {
		MP = mP;
	}

	public int getCurrHP() {
		return currHP;
	}

	public void setCurrHP(int currHP) {
		this.currHP = currHP;
	}

	public int getCurrMP() {
		return currMP;
	}

	public void setCurrMP(int currMP) {
		this.currMP = currMP;
	}
}
