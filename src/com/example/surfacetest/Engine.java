package com.example.surfacetest;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

public class Engine implements Runnable{
	PlayerActor[] players;
	int playerNumber;
	EnemyActor[] enemies;
	int enemyNumber;
	GameActor current;
	GameActor selected;
	GameActor targeted;
	ArrayList<GameActor> all;
	Random treasure;
	Interface face;
	Boolean ready;
	public int select;
	public GameActor target;
	Action action;
	private Context context;
	public boolean initialized = false;
	public boolean playersAlive = false;
	public boolean enemiesAlive = false;
	
	public Engine(Context c){
		context = c;
		selected  = null;
		targeted = null;
		current = null;
		ready = false;
		this.face = new Interface(this);
		treasure = new Random();

		this.players = new PlayerActor[4];
		playerNumber =0;
		this.enemies = new EnemyActor[9];
		enemyNumber=0;
		this.all = new ArrayList<GameActor>();		
		
	}
	
	public void addPlayer(GameActor newActor, int alignment){
		GameActor[] newActorTeam;
		int teamNumber;
		
		if(alignment == 0){
			newActorTeam = players;
			teamNumber=playerNumber;
		}
		else{
			newActorTeam = enemies;
			teamNumber=enemyNumber;
		}	
		
		if(newActorTeam.length<=teamNumber){
			Log.i("Engine", "Party Full "+newActorTeam.length);
			return;
		}
		else{
			if(alignment == 0)newActorTeam[teamNumber] = new PlayerActor(newActor, this);
			else newActorTeam[teamNumber] = new EnemyActor(newActor, this, new Treasure("SALMON", .5));
			all.add(newActorTeam[teamNumber]);
			if(alignment == 0) playerNumber += 1;
			else enemyNumber += 1;
		}
	}
	
	public void run() {
		try {
			initialized = true;
			face.BeginCombat();
			Thread.sleep(4000);

			playersAlive = false;
			enemiesAlive = false;
			for (int i = 0; i < playerNumber; i++) {
				if (players[i].isAlive == true)
					playersAlive = true;
			}
			for (int i = 0; i < enemyNumber; i++) {
				if (enemies[i].isAlive == true)
					enemiesAlive = true;
			}
			while(playersAlive == true && enemiesAlive == true){
				GameActor current = getNext();
				face.WhoseTurn();
				Thread.sleep(1000);
				
				do{
					action = current.startTurn(players, enemies);
					if(action.ability.cost>current.currMP){
						face.tooLittle();
						Thread.sleep(300);
					}
				}while(action.ability.cost>current.currMP);
				calculate(current, action.targets, action.ability);
				playersAlive = false;
				enemiesAlive = false;
				for (int i = 0; i < playerNumber; i++) {
					if (players[i].isAlive == true)
						playersAlive = true;
				}
				for (int i = 0; i < enemyNumber; i++) {
					if (enemies[i].isAlive == true)
						enemiesAlive = true;
				}
			}
			if(playersAlive == true){
				face.Victory();
				Thread.sleep(4000);
				for(int i =0; i<enemies.length; i++){
					//if(enemies[i].treasure.probability<=treasure.nextDouble())face.Treasure(enemies[i].treasure.treasure);
					//Thread.sleep(1500);
				}
				((CombatActivity)context).finish(true);
				return;
			}
			else{
				face.Lose();
				Thread.sleep(4000);
				((CombatActivity)context).finish(false);
				return;
			}
		} catch (InterruptedException e) {
			Log.i("BATTLE", "Failure "+current.name);
		}
	}
	
	public GameActor getNext(){
		for(int i=0; i<all.size(); i++){
			((GameActor)(all.get(i))).turn=((GameActor)(all.get(i))).turn+((GameActor) all.get(i)).getStat("SPD");
		}
		GameActor next = (GameActor) all.get(0);
		for(int i=1; i<all.size(); i++){
			if(((GameActor)(all.get(i))).turn>next.turn)next=(GameActor) all.get(i);
		}
	
		current =next;
		current.turn -= 40;
		face.WhoseTurn();
		return next;	
	}
	
	public void calculate(GameActor attacker, GameActor[] defender, Ability abl) throws InterruptedException{
		attacker.currMP-=abl.cost;
		for(int i=0; i<defender.length; i++){
			if(defender[i]==null)return;
			else if(defender[i].isAlive==true){
				int dmg = abl.base - attacker.getStat(abl.aModifier) + defender[i].getStat(abl.aModifier);
				if(defender[i].getResistance(abl.damageType) != null)dmg *= defender[i].getResistance(abl.damageType);
				face.CombatDesription(current, defender[i], dmg);
				Thread.sleep(2000);
				defender[i].setCurrHP(defender[i].getCurrHP()+dmg);
				if(defender[i].currHP <= 0){
					defender[i].currHP = 0;
					defender[i].isAlive = false;
					face.DeathDescription(defender[i]);
					Thread.sleep(2000);
					all.remove(defender[i]);
				}
			}
		}
	}
}
