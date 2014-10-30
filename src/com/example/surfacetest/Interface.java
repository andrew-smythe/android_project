package com.example.surfacetest;
import java.util.Scanner;

public class Interface {
	Engine engine;
	String message;
    private static Scanner scanner = new Scanner( System.in );
	
	public Interface(Engine engine) {
		this.engine=engine;
		this.message=null;
	} 
	
	public void BeginCombat(){		
		this.message = "A fight breaks out!";
	}	
	
	public void WhoseTurn(){
		this.message = "It is " +engine.current.name+"'s turn!";			
	}
	
	public void tooLittle(){
		this.message = (engine.current.name + " doesn't have the strength of will!");
	}
	
	public void CombatDesription(GameActor current, GameActor defender, int dmg){
		this.message = (current.name + " deals " + dmg + " to " + defender.name);
	}
	
	public void DeathDescription(GameActor victim){
		this.message = (victim.name + " has fallen!");		
	}
	
	public void Victory(){
		this.message = ("You are Victorious!");	
	}
	
	public void Lose(){
		this.message = ("You are Defeated!");	
	}
	
	public void Treasure(String item){
		this.message = ("You pick up a " +item);	
	}
}