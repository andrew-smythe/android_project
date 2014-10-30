package com.example.surfacetest;

public class Tactics {
	public Tactics(){
	}
	
	public Action getAction(PlayerActor[] players, EnemyActor[] enemies) {
		Ability aby = new Ability();
		GameActor[] tgt = new GameActor[1];
		for(int i =0; i<players.length; i++){
			if(players[i].isAlive == true)tgt[i]=(players[i]);
			return new Action(aby, tgt);
		}
		return null;
	}

}
