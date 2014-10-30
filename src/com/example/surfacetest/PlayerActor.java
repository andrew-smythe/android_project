package com.example.surfacetest;

import android.graphics.Bitmap;

public class PlayerActor extends GameActor{
	
	private Engine engine;

	public PlayerActor(GameActor base, Engine engine) {
		super(base.name, base.battleSprite, base.HP, base.MP, base.stats.get("pATK"), base.stats.get("mATK"),
				base.stats.get("mDEF"), base.stats.get("pDEF"), base.stats.get("SPD"), base.abilities);
		this.engine = engine;
	}

	Action startTurn(PlayerActor[] players, EnemyActor[] enemies) {
		engine.select = -1;
		engine.target = null;
		while(engine.select == -1 || engine.target == null);
		
			
		Ability move = (Ability) this.abilities.get(engine.select);
		GameActor[] targets;
		if(move.targetOptions.split("")[1].equalsIgnoreCase("a")){
			if(move.targetOptions.split("")[2].equalsIgnoreCase("e"))targets=enemies;
			else targets = players;
			return new Action(move, targets);
		}
		else{
			targets=new GameActor[1];
			targets[0]=engine.target;
			return new Action(move, targets);
		}
	}
	
	public void stepUp(){
		if(this==engine.current){
			if (locat<80)locat+=1;
		}
		else{
			if (locat>0)locat-=1;
		}
	}
	
}
