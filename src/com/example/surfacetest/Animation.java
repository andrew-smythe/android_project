package com.example.surfacetest;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Animation {
	int frameNumber;
	int duration;
	Bitmap[] frames;
	private int on;
	Paint select=new Paint();
	
	public Animation(int frameNumber, int duration){
		this.frameNumber=frameNumber;
		this.duration=duration;
		this.frames=new Bitmap[frameNumber];
		this.on=0;
	}
	public void addFrame(Bitmap frame){
		frames[on]=frame;
		on++;
	}
	public void play(Canvas canvas, int x, int y) throws InterruptedException{
		for(int i=0; i<frameNumber; i++){
			if(frames[i]!=null){
			canvas.drawBitmap(frames[i], x, y, select);
			wait(50);
			}
		}
		
	}

}
