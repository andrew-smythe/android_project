package com.example.surfacetest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.view.MotionEvent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;

public class CombatSurface extends SurfaceView implements SurfaceHolder.Callback {
	
	private Engine engine;
	
	Canvas canvas;
	Rect bounds;
	
	private PanelThread _thread;
	private Thread engineThread;
	private Paint textpaint;
	private Paint infoPaint = new Paint();
	private BattleArt sheet;
	private int screenHeight;
	private int screenWidth;
	private Boolean firstDraw = true;
	private float xSaveTouch, ySaveTouch;
	private int ablSelect;
	
	// Database handler class
	private DBHandler db;
	
	
	public CombatSurface(Context context) {
		super(context);
		getHolder().addCallback(this);
	}
	
	public CombatSurface (Context context, AttributeSet attrs) {
		super(context, attrs);
		getHolder().addCallback(this);	
	}
	
    public void onDraw(Canvas canvas) {     	
		// on the first draw, setup the world
		if (firstDraw) {
			initialize(canvas);
			firstDraw = false;
		}	
		Bitmap toDraw = sheet.getTileBitmap("battle1");
		canvas.drawBitmap(toDraw, 0, 0, null);
		
		if (engine.face.message != null) {
			String s = engine.face.message;
			
			textpaint.getTextBounds(s, 0, s.length(), bounds);
			bounds.offset(canvas.getWidth()/2-bounds.width()/2, canvas.getHeight()/9);
			bounds.inset(-10, -5);
							
			textpaint.setColor(Color.BLACK);
			textpaint.setStyle(Style.FILL);
			canvas.drawRect(bounds, textpaint);
				    
			textpaint.setColor(Color.WHITE);
			textpaint.setStyle(Style.STROKE);
			textpaint.setStrokeWidth(3);
			canvas.drawRect(bounds, textpaint);
				    
			textpaint.setStrokeWidth(1);
			textpaint.setStyle(Style.FILL);
			canvas.drawText(s, this.canvas.getWidth()/2, canvas.getHeight()/9, textpaint);
		}
		
		Paint select = new Paint();
		ColorFilter filter = new LightingColorFilter(Color.CYAN, 1);
	
		float spaceBetween = (float) ((screenHeight*.9-220)/(engine.playerNumber+1));
		float stepUp = 80;
		for (int i = 0; i < engine.playerNumber; i++) {
				toDraw = engine.players[i].getImage();
				engine.players[i].stepUp();
				engine.players[i].xLoc = (float) (screenWidth-screenWidth*.2-engine.players[i].locat);
				engine.players[i].yLoc= (float) (10+(spaceBetween*(i+1))+(55*i));
				
				if(engine.players[i]==engine.selected && engine.current!=null){
									
					float center_x, center_y, radius;
					center_x = engine.players[i].xLoc+toDraw.getWidth()/2;
					center_y = engine.players[i].yLoc+toDraw.getHeight()/2;
					radius=(float) (toDraw.getHeight()*1.25);
					final RectF oval = new RectF();
					oval.set(center_x - radius, center_y - radius, center_x + radius, center_y + radius);
					Paint ovalPaint=new Paint();
					ovalPaint.setStyle(Style.FILL);
					
					infoPaint.setColor(Color.BLACK);
					
					Ability temp = (Ability) engine.current.abilities.get(0);
					if(engine.current.currMP >=temp.cost)ovalPaint.setColor(temp.abl_color);
					else ovalPaint.setColor(Color.argb(255,128,128,128));
					if(ablSelect == 0){
						ovalPaint.setAlpha(255);
						infoPaint.setColor(Color.YELLOW);
					}
					else{ 
						ovalPaint.setAlpha(75);
						infoPaint.setColor(Color.BLACK);
					}
					canvas.drawArc(oval, 210, 120, true, ovalPaint);					
					Path mArc = new Path();
					mArc.addArc(oval, 210, 120);
					canvas.drawTextOnPath(temp.name, mArc, 0, 20, infoPaint);      

					temp = (Ability) engine.current.abilities.get(1);
					if(engine.current.currMP >=temp.cost)ovalPaint.setColor(temp.abl_color);
					else ovalPaint.setColor(Color.argb(255,128,128,128));
					if(ablSelect == 1){
						ovalPaint.setAlpha(255);
						infoPaint.setColor(Color.YELLOW);
					}
					else{
						ovalPaint.setAlpha(75);
						infoPaint.setColor(Color.BLACK);
					}
					canvas.drawArc(oval, 330, 120, true, ovalPaint);
					mArc = new Path();
					mArc.addArc(oval, 330, 120);
					canvas.drawTextOnPath(temp.name, mArc, 0, 20, infoPaint);    
					
					temp = (Ability) engine.current.abilities.get(2);
					if(engine.current.currMP >=temp.cost)ovalPaint.setColor(temp.abl_color);
					else ovalPaint.setColor(Color.argb(255,128,128,128));
					if(ablSelect == 2){
						ovalPaint.setAlpha(255);
						infoPaint.setColor(Color.YELLOW);
					}
					else{
						ovalPaint.setAlpha(75);
						infoPaint.setColor(Color.BLACK);
					}
					canvas.drawArc(oval, 90, 120, true, ovalPaint);
					mArc = new Path();
					mArc.addArc(oval, 90, 120);
					canvas.drawTextOnPath(temp.name, mArc, 0, 20, infoPaint);    
					
				}
				
				
				canvas.drawBitmap(toDraw, engine.players[i].xLoc, engine.players[i].yLoc, select);
				infoPaint.setColor(Color.BLACK);
				canvas.drawText(engine.players[i].name, (float) (screenWidth-screenWidth*.2+toDraw.getWidth()*2), engine.players[i].yLoc+toDraw.getHeight()/2-15, this.infoPaint);
				infoPaint.setColor(Color.RED);
				canvas.drawText((engine.players[i].currHP +"/"+ engine.players[i].HP), (float) (screenWidth-screenWidth*.2+toDraw.getWidth()*2), engine.players[i].yLoc+toDraw.getHeight()/2, this.infoPaint);
				infoPaint.setColor(Color.BLUE);
				canvas.drawText((engine.players[i].currMP +"/"+ engine.players[i].MP), (float) (screenWidth-screenWidth*.2+toDraw.getWidth()*2), engine.players[i].yLoc+toDraw.getHeight()/2+15, this.infoPaint);
		}

		spaceBetween = (float) ((screenHeight*.9-220)/(engine.enemyNumber+1));
		stepUp = 80;
		for (int i = 0; i < engine.enemyNumber; i++) {
			toDraw = engine.enemies[i].getImage();
			engine.enemies[i].stepUp();
			engine.enemies[i].xLoc = (float) (screenWidth*.2)-55+engine.enemies[i].locat;
			engine.enemies[i].yLoc= (float) (10+(spaceBetween*(i+1))+(55*i));
			
			if(engine.enemies[i]==engine.selected && engine.current!=null){
				float center_x, center_y, radius;
				center_x = engine.enemies[i].xLoc+toDraw.getWidth()/2;
				center_y = engine.enemies[i].yLoc+toDraw.getHeight()/2;
				radius=(float) (toDraw.getHeight()*1.25);
				final RectF oval = new RectF();
				oval.set(center_x - radius, center_y - radius, center_x + radius, center_y + radius);
				Paint ovalPaint=new Paint();
				ovalPaint.setStyle(Style.FILL);
				
				infoPaint.setColor(Color.BLACK);
				
				Ability temp = (Ability) engine.current.abilities.get(0);
				ovalPaint.setColor(temp.abl_color);
				if(ablSelect == 0){
					ovalPaint.setAlpha(255);
					infoPaint.setColor(Color.YELLOW);
				}
				else{ 
					ovalPaint.setAlpha(75);
					infoPaint.setColor(Color.BLACK);
				}
				canvas.drawArc(oval, 210, 120, true, ovalPaint);					
				Path mArc = new Path();
				mArc.addArc(oval, 210, 120);
				canvas.drawTextOnPath(temp.name, mArc, 0, 20, infoPaint);      

				temp = (Ability) engine.current.abilities.get(1);
				ovalPaint.setColor(temp.abl_color);
				if(ablSelect == 1){
					ovalPaint.setAlpha(255);
					infoPaint.setColor(Color.YELLOW);
				}
				else{
					ovalPaint.setAlpha(75);
					infoPaint.setColor(Color.BLACK);
				}
				canvas.drawArc(oval, 330, 120, true, ovalPaint);
				mArc = new Path();
				mArc.addArc(oval, 330, 120);
				canvas.drawTextOnPath(temp.name, mArc, 0, 20, infoPaint);    
				
				temp = (Ability) engine.current.abilities.get(2);
				ovalPaint.setColor(temp.abl_color);
				if(ablSelect == 2){
					ovalPaint.setAlpha(255);
					infoPaint.setColor(Color.YELLOW);
				}
				else{
					ovalPaint.setAlpha(75);
					infoPaint.setColor(Color.BLACK);
				}
				canvas.drawArc(oval, 90, 120, true, ovalPaint);	
				mArc = new Path();
				mArc.addArc(oval, 90, 120);
				canvas.drawTextOnPath(temp.name, mArc, 0, 20, infoPaint);				
			}
			
			
			canvas.drawBitmap(toDraw, engine.enemies[i].xLoc, engine.enemies[i].yLoc, select);
			infoPaint.setColor(Color.BLACK);
			canvas.drawText(engine.enemies[i].name, (float) (engine.enemies[i].xLoc-40), engine.enemies[i].yLoc+toDraw.getHeight()/2-15, this.infoPaint);
			infoPaint.setColor(Color.RED);
			canvas.drawText((engine.enemies[i].currHP +"/"+ engine.enemies[i].HP), (float) (engine.enemies[i].xLoc-40), engine.enemies[i].yLoc+toDraw.getHeight()/2, this.infoPaint);
			infoPaint.setColor(Color.BLUE);
			canvas.drawText((engine.enemies[i].currMP +"/"+ engine.enemies[i].MP), (float) (engine.enemies[i].xLoc-40), engine.enemies[i].yLoc+toDraw.getHeight()/2+15, this.infoPaint);
		}

    }
	   
    class PanelThread extends Thread {
        private SurfaceHolder _surfaceHolder;
        private CombatSurface _panel;
        private boolean _run = false;

        public PanelThread(SurfaceHolder surfaceHolder, CombatSurface panel) {
            _surfaceHolder = surfaceHolder;
            _panel = panel;
        }

        public void setRunning(boolean run) { //Allow us to stop the thread
            _run = run;
        }

        public void run() {
            Canvas c;
            while (_run) {     //When setRunning(false) occurs, _run is 
                c = null;      //set to false and loop ends, stopping thread

                try {
                    c = _surfaceHolder.lockCanvas(null);
                    synchronized (_surfaceHolder) {
                     postInvalidate();

                    }
                } 
                finally {
                    if (c != null) {
                        _surfaceHolder.unlockCanvasAndPost(c);
                    }
                }
            }
        }
    }
    
    public void surfaceCreated(SurfaceHolder holder) {	
    	setWillNotDraw(false); 							//Allows us to use invalidate() to call onDraw()
     	_thread = new PanelThread(getHolder(), this); 	//Start the thread that
        _thread.setRunning(true);                     	//will make calls to 
        _thread.start();                              	//onDraw()
    }
    
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) { 
    }
    
    public void surfaceDestroyed(SurfaceHolder holder) {
    	try {
    		_thread.setRunning(false);                //Tells thread to stop
    		_thread.join();                           //Removes thread from mem.
    	} 
    	catch (InterruptedException e) {}
    }
    
    public void initialize(Canvas canvas) {
     	screenHeight = canvas.getHeight();
		screenWidth = canvas.getWidth();
		sheet = new BattleArt(getResources(), screenWidth, screenHeight);
		
		this.canvas=canvas;
		this.textpaint=new Paint();
	    this.bounds= new Rect();
	    
	    textpaint.setStyle(Style.FILL);
		textpaint.setTextSize(canvas.getHeight()/18);
		textpaint.setTextAlign(Paint.Align.CENTER);
		
		infoPaint.setTextSize(canvas.getHeight()/40);
	    infoPaint.setColor(Color.RED);
	    infoPaint.setTextAlign(Paint.Align.CENTER);
	    Typeface temp = Typeface.defaultFromStyle(Typeface.BOLD);
		infoPaint.setTypeface(temp);
		db = new DBHandler(this.getContext());
		startEngine();
		
    }
    
	public void drawMessage(String s){
		if(s==null)return;
		 
		return;
	}
    
    public void startEngine(){
		engine = new Engine(this.getContext());
		// Add the players to the battle
		engine.addPlayer(db.getActor("Bob"), 0);
		engine.addPlayer(db.getActor("Edric"), 0);
		engine.addPlayer(db.getActor("Simon"), 0);
		engine.addPlayer(db.getActor("Sal"), 0);
		
		// Get the party id from the activity
		int pid = ((CombatActivity)this.getContext()).pid;
		
		// Get the party -- array of GameActors
		String[] enemies = db.getParty(pid);
		
		// Add the enemies to the battle
		for (int i = 0; i < enemies.length; i++) {
			engine.addPlayer(db.getActor(enemies[i]), 1);
		}
		engineThread = new Thread(engine);
							
		engineThread.start();
    }
    
    public boolean onTouchEvent(MotionEvent e){
        int action = e.getAction();
        float xTouch = e.getX();
        float yTouch = e.getY();
        

		if(action==MotionEvent.ACTION_DOWN){
			if (engine != null && engine.initialized == true) {
				for(int i=0; i<engine.all.size(); i++){
					GameActor check = engine.all.get(i);
					if (xTouch >= check.xLoc && xTouch < (check.xLoc + check.battleSprite.getWidth()) && yTouch >= check.yLoc && yTouch < (check.yLoc + check.battleSprite.getHeight())) {
	       				engine.selected=engine.all.get(i);
	       				this.xSaveTouch=xTouch;
	       				this.ySaveTouch=yTouch;
	    			}
	      		}
			}
       	}
		else if(action==MotionEvent.ACTION_UP){
			if (engine != null && engine.initialized == true) {
				if(engine.selected != null && ablSelect != -1){
					engine.select=ablSelect;
					engine.target=engine.selected;
					//try {
						//((Ability)engine.current.abilities.get(ablSelect)).anima.play(canvas, (int)engine.target.xLoc, (int)engine.target.yLoc);
					//} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						//e1.printStackTrace();
					//}
				}
				ablSelect=-1;
				engine.selected=null;
			}
		}
		else if(action==MotionEvent.ACTION_MOVE){
			if (engine != null && engine.initialized == true) {
				if(engine.selected != null){
					float deltaY = xTouch - xSaveTouch;
					float deltaX = yTouch - ySaveTouch;				
					float ang = (float) Math.abs((Math.atan(deltaX / deltaY) * 180 / Math.PI));
					if (deltaY < 0 && deltaX >= 0)ang=180-ang;
					else if (deltaY < 0 && deltaX < 0)ang=180+ang;
					else if (deltaY >= 0 && deltaX < 0)ang=360-ang;
					double dist = Math.sqrt(deltaY*deltaY + deltaX*deltaX);
					
					if(dist > engine.selected.battleSprite.getWidth()*2)engine.selected=null;
					else{
						if(ang>210 && ang<=330)ablSelect=0;
						else if (ang>330 ||ang<=90)ablSelect=1;
						else if (ang>90 && ang<=210)ablSelect=2;				
					}
				}
			}
		}

       	
    	return true; 
    }
}
