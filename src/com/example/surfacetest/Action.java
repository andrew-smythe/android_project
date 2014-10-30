package com.example.surfacetest;

public class Action {
	Ability ability;
	GameActor[] targets;
	
	public Action(Ability ability, GameActor[] targets){
		this.ability=ability;
		this.targets=targets;
	}
}
