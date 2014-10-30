package com.example.surfacetest;

import java.util.Random;

public class Abl_Store {
	Ability[] wAbilities;
	Ability[] sAbilities;
	int skillNumber;
	String[] pTypes = {"pierce","slash","bludgeon", "corrosive", "explosive"};
	String[] mTypes = {"fire","ice","elec","rock","wind","water","dark","light","arcane"};
	
	String name, aMod, dMod, target, type;
	int base, cost, color;
	
	

	
	public Abl_Store(int num){
		Random gen=new Random();
		this.skillNumber=num;
		this.wAbilities=new Ability[this.skillNumber];
		this.sAbilities=new Ability[this.skillNumber];
		for(int i=0; i<this.skillNumber; i++){
			cost=5;
	
			int temp=gen.nextInt(40);
			this.base=10+temp;
			this.cost+=Math.ceil(temp/4);
			
			int simp=gen.nextInt(2);
			if(simp<1){
				this.aMod="pATK";
				this.dMod="pDEF";
			}
			else {
				this.aMod="mATK";
				this.dMod="mDEF";
			}
			
			if(this.aMod.equals("pATK")){
				this.type=this.pTypes[gen.nextInt(pTypes.length)];
			}
			else{
				this.type=this.mTypes[gen.nextInt(mTypes.length)];			
			}
			
			if(gen.nextInt(100)>24){
				target="AE";
				cost=cost+5;
			}
			else target="1E";
			
			
		}
	}
}
