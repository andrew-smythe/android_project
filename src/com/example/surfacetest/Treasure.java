package com.example.surfacetest;

public class Treasure {
	String treasure;
	Double probability;
	public Treasure(String tre, Double prob){
		this.treasure=tre;
		this.probability=prob;
	}
	public String getTreasure() {
		return treasure;
	}
	public void setTreasure(String treasure) {
		this.treasure = treasure;
	}
	public Double getProbability() {
		return probability;
	}
	public void setProbability(Double probability) {
		this.probability = probability;
	}

}
