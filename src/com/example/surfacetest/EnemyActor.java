package com.example.surfacetest;

import java.util.Random;
import android.graphics.Bitmap;
import android.util.Log;


public class EnemyActor extends GameActor{
	
	Treasure treasure;
	private Engine engine;
	
	public EnemyActor(GameActor base, Engine engine, Treasure treasure) {
		super(base.name, base.battleSprite, base.HP, base.MP, base.stats.get("pATK"), base.stats.get("mATK"),
				base.stats.get("mDEF"), base.stats.get("pDEF"), base.stats.get("SPD"), base.abilities);
		this.engine = engine;
		this.treasure=treasure;
	}

	
	Action startTurn(PlayerActor[] players, EnemyActor[] enemies) {
		GameActor[] tgt = new GameActor[1];
		Random targeter = new Random();
		int i;
		while(true){
			i = targeter.nextInt(players.length);
			Log.i("TESTER", this.name+" i = "+i);
			Log.i("TESTER", this.name+ " player = "+players[i].name);
			if(players[i].isAlive == true){
				Log.i("TESTER", this.name+" Alive player = "+players[i].name);
				tgt[0]=players[i];
				return new Action((Ability) this.abilities.get(0), tgt);
			}
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
