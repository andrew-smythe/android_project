package com.example.surfacetest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import android.graphics.Canvas;
import android.os.MessageQueue;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.Toast;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;

public class TestSurface extends SurfaceView implements SurfaceHolder.Callback {
	
	// world settings
	private WorldMap world;
	private int worldSizeX;
	private int worldSizeY;
	
	private PanelThread _thread;
	private Paint paint = new Paint();
	private Paint textPaint;
	private int _y = 0;
	private SpriteSheet sheet;
	private boolean toasted = false;
	private int numTiles;
	private int tileSize;
	private int offset;
	private int oldOffset;
	private int screenSize;
	private String charSprite;
	
	private Tile[][] view;
	private int viewX;
	private int viewY;
	private int upMove = 0;
	
	private Boolean movingLeft = false;
	private Boolean movingRight = false;
	private Boolean movingUp = false;
	private Boolean movingDown = false;
	
	private int moveX;
	private int moveY;
	
	private Pathfinder pathfinder;
	private Stack<Integer> moveStack;
	public final static int REQUEST_CODE = 10;
	
	// whether the first draw has happened or not
	private Boolean firstDraw = true;
	
	// Database handling class
	private DBHandler db;
	
	// Player sprite
	private PlayerSprite player;
	private int shift = 0;
	
	// Message queue
	LinkedList<String> messageQueue;
	
	public TestSurface(Context context) {
		super(context);
    	Log.d("ANDREW", "was here.");
		getHolder().addCallback(this);
	}
	
	public TestSurface (Context context, AttributeSet attrs) {
		super(context, attrs);
    	Log.d("ANDREW", "was here.");
		getHolder().addCallback(this);
	}
	
	@Override 
    public void onDraw(Canvas canvas) { 
        
		// on the first draw, setup the world
		if (firstDraw) {
			initialize(canvas);
			firstDraw = false;
		}
		
		// determine what move we shall make (if there is one)
		if (moveStack.size() > 0) {
			// get the direction, but do not take it off of the stack yet
			int direction = moveStack.peek();
					
			// moving up
			if (direction == 1)
				movingUp = true;
			// moving right
			else if (direction == 2)
				movingRight = true;
			// moving down
			else if (direction == 3)
				movingDown = true;
			// moving left
			else if (direction == 4)
				movingLeft = true;
		}
		// move the screen left
		if (movingLeft) {
			player.faceLeft();
			offset+= 3;
			if (shift%5 == 0) {
				player.iterate();
			}
			shift++;
			if (offset >= oldOffset+tileSize) {
				offset = oldOffset;
				world.shiftMap(2);
				movingLeft = false;
				if (moveStack.size() == 0)
					Log.d("Weird times.", "Are happening.");
				moveStack.pop();
				startCombat();
			}
		}
		
		// move the screen right
		if (movingRight) {
			player.faceRight();
			offset-= 3;
			if (shift%5 == 0) {
				player.iterate();
			}
			shift++;
			if (offset <= oldOffset-tileSize) {
				offset = oldOffset;
				world.shiftMap(0);
				movingRight = false;
				if (moveStack.size() == 0)
					Log.d("Weird times.", "Are happening.");
				moveStack.pop();
				startCombat();
			}
		}
		
		// move the screen up
		if (movingUp) {
			player.faceUp();
			upMove += 3;
			if (shift%5 == 0) {
				player.iterate();
			}
			shift++;
			if (upMove >= tileSize) {
				upMove = 0;
				world.shiftMap(3);
				movingUp = false;
				if (moveStack.size() == 0)
					Log.d("Weird times.", "Are happening.");
				moveStack.pop();
				startCombat();
			}
		}
		
		// move the screen down
		if (movingDown) {
			player.faceDown();
			upMove -= 3;
			if (shift%5 == 0) {
				player.iterate();
			}
			shift++;
			if (upMove <= -1*tileSize) {
				upMove = 0;
				world.shiftMap(1);
				movingDown = false;
				if (moveStack.size() == 0)
					Log.d("Weird times.", "Are happening.");
				moveStack.pop();
				startCombat();
			}
		}
		
		// get our current "window" of the world map (with two rows/columns outside
		// of the viewing area)
		view = world.getMap();
		
		if (view == null)
			Log.d("This is", "BAD.");
		
		// draw each tile to the screen
		for (int i = -1; i < numTiles+1; i++) {
			for (int j = -1; j < numTiles+1; j++) {
				String msg = view[i+1][j+1].toString();
				Bitmap toDraw = view[i+1][j+1].getBitmap();
				canvas.drawBitmap(toDraw, 1+offset+(tileSize*i), 1+(tileSize*j)+upMove, null);
			}
		}
		
		// draw each tile's flat object to the screen
		for (int i = -1; i < numTiles+1; i++) {
			
			for (int j = -1; j < numTiles+1; j++) {
				// draw the object if it exists
				if (view[j+1][i+1].hasFlatObject()) {
					drawObject(j, i, view[j+1][i+1].getFlatObject(), canvas);
				}
				
			}
		}
		
		// should be replaced with some character object at some point
		canvas.drawBitmap(player.getBitmap(), 1+oldOffset+(tileSize*(numTiles/2)), 1+(tileSize*(numTiles/2)), null);
		
		// draw each tile's object to the screen
		for (int i = -1; i < numTiles+1; i++) {
			
			for (int j = -1; j < numTiles+1; j++) {
				// draw the object if it exists
				if (view[j+1][i+1].hasObject()) {
					drawObject(j, i, view[j+1][i+1].getObject(), canvas);
				}
				
			}
		}
		paint.setColor(Color.rgb(32, 32, 32));
		paint.setStrokeWidth(10);
		canvas.drawRect(0, 0, oldOffset, screenSize, paint);
		canvas.drawRect(oldOffset+(tileSize*numTiles), 0, screenSize+(oldOffset*2), screenSize, paint);
		
		// if there is a message, draw a message box
		if (messageQueue.size() > 0) {
			String text = messageQueue.peek();
			Rect bounds = new Rect(offset-50, screenSize-70, screenSize+offset+50, screenSize-30);
							
			textPaint.setColor(Color.BLACK);
			textPaint.setStyle(Style.FILL);
			canvas.drawRect(bounds, textPaint);
				    
			textPaint.setColor(Color.WHITE);
			textPaint.setStyle(Style.STROKE);
			textPaint.setStrokeWidth(3);
			canvas.drawRect(bounds, textPaint);
				    
			textPaint.setStrokeWidth(1);
			textPaint.setStyle(Style.FILL);
			canvas.drawText(text, offset-40, screenSize-40, textPaint);
		}
	}
	
	// Draw an object to the screen
	public void drawObject(int x, int y, String type, Canvas canvas) {
		
		// draw a tree
		if (type.equals("tree")) {
			
			// draw the tree trunk
			Bitmap toDraw = SpriteSheet.getTileBitmap("tree2");
			canvas.drawBitmap(toDraw, 1+offset+(tileSize*x), 1+(tileSize*y)+upMove, null);
			
			// continue to draw the tree
			toDraw = SpriteSheet.getTileBitmap("tree1");
			canvas.drawBitmap(toDraw, 1+offset+(tileSize*x), 1+(tileSize*(y-1))+upMove, null);
			
		}
		else {
			Bitmap toDraw = SpriteSheet.getTileBitmap(type);
			canvas.drawBitmap(toDraw, 1+offset+(tileSize*x), 1+(tileSize*y)+upMove, null);
			
		}
	}
	

	@Override 
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
        int height) { 


    }
    class PanelThread extends Thread {
        private SurfaceHolder _surfaceHolder;
        private TestSurface _panel;
        private boolean _run = false;

        public PanelThread(SurfaceHolder surfaceHolder, TestSurface panel) {
            _surfaceHolder = surfaceHolder;
            _panel = panel;
        }

        public void setRunning(boolean run) { //Allow us to stop the thread
            _run = run;
        }

        @Override
        public void run() {
            Canvas c;
            while (_run) {     //When setRunning(false) occurs, _run is 
                c = null;      //set to false and loop ends, stopping thread

                try {
                    c = _surfaceHolder.lockCanvas(null);
                    synchronized (_surfaceHolder) {

                     //Insert methods to modify positions of items in onDraw()
                     _y++;
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
    
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
		
    	setWillNotDraw(false); //Allows us to use invalidate() to call onDraw()
     	_thread = new PanelThread(getHolder(), this); //Start the thread that
        _thread.setRunning(true);                     //will make calls to 
        _thread.start();                              //onDraw()
    }
    
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    	try {
    		_thread.setRunning(false);                //Tells thread to stop
    		_thread.join();                           //Removes thread from mem.
    	} 
    	catch (InterruptedException e) {}
    }
    
    // create items on the world map
    public void createWorld(WorldMap world) {
    	// create some number of trees (up to 800)
		int randX, randY;
    	for (int i = 0; i < 800; i++) {
    		randX = (int)(Math.random() * (worldSizeX + 1));
    		randY = (int)(Math.random() * (worldSizeY + 1));
    		
    		// attempt to draw the tree
    		world.drawTree(randX, randY);
    	}
    }
    
    public void initialize(Canvas canvas) {
    	// create a canvas to get the relevant information from it
		// number of tiles on the screen
		numTiles = 9;
		
		// get the screen size
		screenSize = canvas.getHeight();
		
		// get the size of the tiles
		tileSize = canvas.getHeight() / numTiles;
		
		// set the sprite name
		charSprite = "char_left1";
		
		// offset -- for centering the tiles on screen
		offset = (canvas.getWidth() - canvas.getHeight()) / 2;
		oldOffset = offset;
    	Log.d("Initializing", "tileSize = "+tileSize+", Width = "+canvas.getWidth()+", Height = "+canvas.getHeight());
    	
    	// create a pathfinder
    	pathfinder = new Pathfinder();
    	
    	// setup an empty stack
    	moveStack = new Stack<Integer>();
    	
    	// add a touch listener -- for shifting left and right
		this.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch (View v, MotionEvent e) {
				// register event on release
		    	// get our current location
		    	int xLoc = world.getXOffset()+(numTiles/2);
		    	int yLoc = world.getYOffset()+(numTiles/2);
				if (e.getAction() == android.view.MotionEvent.ACTION_UP) {
					
					if (messageQueue.size() > 0) {
						messageQueue.removeFirst();
					}
					else {
						Log.d("stuff", ((int)((e.getX()-offset)/tileSize)+1+world.getXOffset())+"");
						Log.d("stuff", ((int)((e.getY())/tileSize)+1+world.getYOffset())+"");
						Log.d("xloc", ""+xLoc);
						Log.d("yloc", ""+yLoc);
						if (((xLoc == 105 && yLoc == 127) || (xLoc == 103 && yLoc == 127) ||
							(xLoc == 104 && yLoc == 126) || (xLoc == 104 && yLoc == 128)) && 
							((int)((e.getX()-offset)/tileSize)+1+world.getXOffset()) == 104 && 
							((int)((e.getY())/tileSize)+1+world.getYOffset()) == 127) {
							displayMessage("Greetings, young traveller. Welcome to my village. It is a mostly peaceful place," +
											" however you should stay away from the desert to the East. It is crawling with" +
											" nasty creatures. There are some more houses North of here. Rumour has it that there is a legendary sword deep in the forest" +
											" to the West.");
						}
						if (e.getX() > offset && e.getX() < offset+screenSize) {
							
							// clear moves
							movingUp = false;
							movingDown = false;
							movingLeft = false;
							movingRight = false;
							
							// find the path
							moveStack = pathfinder.findPath(view, numTiles, numTiles/2, numTiles/2, 
													(int)((e.getX()-offset)/tileSize), (int)((e.getY())/tileSize));
						}
					}
				}			
				return true;
			}			
		});
		// initialize the sprite sheet
		SpriteSheet.createSheet(getResources(), tileSize);
		
		// create the world map
		worldSizeX = 200;
		worldSizeY = 200;

		// set the current viewing window
		viewX = 97;
		viewY = 130;
		
		world = new WorldMap(worldSizeX, worldSizeY, numTiles, viewX, viewY, this.getContext());
		createWorld(world);
		
		// start the music service
		Intent i = new Intent(this.getContext(), BGMusicService.class);
		i.putExtra("song", 1);
	    this.getContext().startService(i);	
	    
	    // Create some GameActors
	    db = new DBHandler(this.getContext());	   
	    if (db.hasEmptyActors()) {
	    	db.createPlayers();
	    	Log.d("DB", "There wasn't no darn actors.");
	    }
	    if (db.hasEmptyAbilities()) {
	    	db.createAbilities();
	    	Log.d("DB", "There wasn't no darn abilities.");
	    }
    	db.createParties();
	    
	    db.showPartyList();
	    player = new PlayerSprite(viewX+((numTiles+2)/2), viewY+((numTiles+2)/2));
	    
	    // initialize the font for message boxes
	    textPaint = new Paint();
	    textPaint.setStyle(Style.FILL);
		textPaint.setTextSize(canvas.getHeight()/18);
		textPaint.setTextAlign(Paint.Align.LEFT);
		
		messageQueue = new LinkedList<String>();
    }
    
    // shifts the screen to the left
    public void shiftLeft() {
    	((Activity) this.getContext()).finish();
    }
    
    // shifts the screen to the right
    public void shiftRight() {
    	if (viewX < worldSizeX-numTiles-1)
    		movingRight = true;
    }
    
    // shifts the screen up
    public void shiftUp() {
    	if (viewY > 1)
    		movingUp = true;
    }
    
    // shifts the screen down
    public void shiftDown() {
    	if (viewY < worldSizeY-numTiles-1)
    		movingDown = true;
    }
    
    public void startCombat() {
    	double r = Math.random();
    	// get our current location
    	int xLoc = world.getXOffset()+(numTiles/2);
    	int yLoc = world.getYOffset()+(numTiles/2);
    	int zone = 0;
    	
    	if (xLoc < 70) {
    		// in the forest -- zone 1
    		zone = 1;
    	}
    	else if (xLoc > 120 && yLoc > 90) {
    		// in the desert -- zone 2
    		zone = 2;
    	}
    	// If we are in a hostile area -- start a combat
   		if (zone > 0) {
   			if (r < 0.025) {
	   			// Get an enemy party from the zone
	   			int pid = db.getPartyFromZone(zone);
	   			
	   			// Create an intent to send the game to combat -- include the id
	   			// of the enemy party
	    		Intent intent = new Intent(this.getContext(), CombatActivity.class);
	    		intent.putExtra("pid", pid);
	    	    ((Activity)this.getContext()).startActivityForResult(intent, REQUEST_CODE);
	    	    
	    	    // 
	    		Intent i = new Intent(this.getContext(), BGMusicService.class);
	    		i.putExtra("song", 1);
	    	    this.getContext().startService(i);	
   			}
   		}   		
    }
    
    public void displayMessage(String s) {
    	Message m = new Message(s);
    	String[] arr = m.getParts();
    	
    	for (int i = 0; i < arr.length; i++) {
    		Log.d("MSG", arr[i]);
    		messageQueue.add(arr[i]);
    	}
    }
}
