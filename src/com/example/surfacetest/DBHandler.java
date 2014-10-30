package com.example.surfacetest;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.*;
import android.graphics.Color;
import android.util.Log;

public class DBHandler extends SQLiteOpenHelper {

	// static constants
	private static final String DB_NAME = "csci485game";
	private static final int DB_VERSION = 1;
	
	// table names
	private static final String TABLE_MAP = "map";
	private static final String TABLE_OBJ = "obj";
	private static final String TABLE_ACTOR = "actor";
	private static final String TABLE_ABILITY = "ability";
	private static final String TABLE_ALIST = "ability_list";
	private static final String TABLE_PARTY = "enemy_parties";
	private static final String TABLE_PLIST = "enemy_party_list";
	private static final String TABLE_EVENT = "events";
	
	// Map table column names
	private static final String COL_X = "x";
	private static final String COL_Y = "y";
	private static final String COL_IMG = "imgName";
	private static final String COL_WALKABLE = "walkable";
	private static final String COL_OBJ = "objectID";
	private static final String COL_FOBJ = "flat_objectID";
	private static final String COL_EVENT = "eventID";
	
	// Object table column names
	private static final String COL_OID = "oID";
	private static final String COL_OTYPE = "type";
	
	// Actor table column names
	private static final String COL_HP = "hp";
	private static final String COL_MP = "mp";
	private static final String COL_PATK = "pATK";
	private static final String COL_MATK = "mATK";
	private static final String COL_MDEF = "mDEF";
	private static final String COL_PDEF = "pDEF";
	private static final String COL_SPD = "spd";
	
	// Ability table column names
	private static final String COL_AID = "abilityID";
	private static final String COL_NAME = "name";
	private static final String COL_BASE = "base";
	private static final String COL_COST = "cost";
	private static final String COL_AMOD = "aMod";
	private static final String COL_DMOD = "dMod";
	private static final String COL_TARGET = "target";
	private static final String COL_DTYPE = "dType";
	private static final String COL_COLOUR = "colour";
	
	// Actor ability table column names
	private static final String COL_NAME_ALIST = "name_alist";
	
	// Enemy party table column names
	private static final String COL_PID = "party_id";
	private static final String COL_PSIZE = "size";
	private static final String COL_ZONE = "zone";
	
	// Event table column names
	private static final String COL_EID = "event_id";
	private static final String COL_TYPE = "type";
	private static final String COL_ARGS = "args";
	
	// constructor -- calls superclass constructor
	public DBHandler (Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		// setup table creation queries
		// map table
		String CREATE_TABLE_MAP = "CREATE TABLE " + TABLE_MAP + "("
				+ COL_X + " INTEGER, " 
				+ COL_Y + " INTEGER, "
				+ COL_IMG + " TEXT, "
				+ COL_WALKABLE + " INTEGER, "
				+ COL_OBJ + " INTEGER, "
				+ COL_FOBJ + " INTEGER, "
				+ COL_EVENT + " INTEGER, "
				+ "PRIMARY KEY("+COL_X+", "+COL_Y+")"
				+ ")";
		
		// object table
		String CREATE_TABLE_OBJ = "CREATE TABLE " + TABLE_OBJ + "("
				+ COL_OID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ COL_OTYPE + " TEXT"
				+ ")";
		
		// actor table
		String CREATE_TABLE_ACTOR = "CREATE TABLE " + TABLE_ACTOR + "("
				+ COL_NAME + " TEXT PRIMARY KEY, "
				+ COL_IMG + " TEXT, "
				+ COL_HP + " INTEGER, "
				+ COL_MP + " INTEGER, "
				+ COL_PATK + " INTEGER, "
				+ COL_MATK + " INTEGER, "
				+ COL_MDEF + " INTEGER, "
				+ COL_PDEF + " INTEGER, "
				+ COL_SPD + " INTEGER"
				+ ")";
		
		String CREATE_TABLE_ABILITY = "CREATE TABLE " + TABLE_ABILITY + "("
				+ COL_AID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ COL_NAME + " TEXT, "
				+ COL_BASE + " INTEGER, "
				+ COL_COST + " INTEGER, "
				+ COL_AMOD + " INTEGER, "
				+ COL_DMOD + " INTEGER, "
				+ COL_TARGET + " TEXT, "
				+ COL_DTYPE + " TEXT, "
				+ COL_COLOUR + " INTEGER"
				+ ")";
		
		String CREATE_TABLE_ALIST = "CREATE TABLE " + TABLE_ALIST + "("
				+ COL_NAME_ALIST + " TEXT, "
				+ COL_AID + " INTEGER"
				+ ")";
		
		String CREATE_TABLE_PARTY = "CREATE TABLE " + TABLE_PARTY + "("
				+ COL_PID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ COL_PSIZE + " INTEGER, "
				+ COL_ZONE + " INTEGER"
				+ ")";
		
		String CREATE_TABLE_PARTY_LIST = "CREATE TABLE " + TABLE_PLIST + "("
				+ COL_PID + " INTEGER, "
				+ COL_NAME + " TEXT"
				+ ")";
		
		String CREATE_TABLE_EVENT = "CREATE TABLE " + TABLE_EVENT + "("
				+ COL_EID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ COL_TYPE + " TEXT, "
				+ COL_ARGS + " TEXT"
				+ ")";
		
		// create tables
		db.execSQL(CREATE_TABLE_MAP);
		db.execSQL(CREATE_TABLE_OBJ);
		db.execSQL(CREATE_TABLE_ACTOR);
		db.execSQL(CREATE_TABLE_ABILITY);
		db.execSQL(CREATE_TABLE_ALIST);
		db.execSQL(CREATE_TABLE_PARTY);
		db.execSQL(CREATE_TABLE_PARTY_LIST);
		db.execSQL(CREATE_TABLE_EVENT);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// drop older version of table if it exists
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_MAP);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_OBJ);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTOR);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ABILITY);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALIST);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARTY);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLIST);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENT);
		
		// recreate tables
		onCreate(db);
	}
	
	public void addObject(String type) {
		// create or open database
		SQLiteDatabase db = this.getWritableDatabase();
		
		// perform insert
		ContentValues values = new ContentValues();

		// add values to insert
		values.putNull(COL_OID);
		values.put(COL_OTYPE, type);
		
		// insert the rows
		db.insert(TABLE_OBJ, null, values);			
	}
	
	public void addTiles() {
		// create or open database
		SQLiteDatabase db = this.getWritableDatabase();
		
		// start a transaction
		db.beginTransaction();
		
		// Insert statement -- SQL
		String insertQuery = "INSERT INTO " + TABLE_MAP + " (" + COL_X + ", " + COL_Y + ", "
				+ COL_IMG + ", " + COL_WALKABLE + ", " + COL_OBJ + ", " + COL_FOBJ + ", " + COL_EVENT + ") "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?)";
		
		// will perform multiple inserts transaction
		SQLiteStatement stmt = db.compileStatement(insertQuery);
		
		// perform insert
		//ContentValues values = new ContentValues();
		
		for (int i = 0; i < 200; i++) {
			for (int j = 0; j < 200; j++) {
				//Log.d("Inserting a tile:", i+","+j);
				double r = Math.random();
				int walkable = 1;
				String image;
				if (r < 0.5)
					//map[i][j] = new Tile("grass", true);
					image = "grass";
				else
					//map[i][j] = new Tile("grass2", true);
					image = "grass2";
				
				// decide if the tile will have a tree object or not
				int objID = -1;
				int fobjID = -1;
				
				// randomly place a couple trees in the main world -- not many
				if (r < 0.02) {
					objID = 1;
					walkable = 0;
				}
				else if (r <= 0.03) {
					objID = 2;
					walkable = 0;
				}
				else if (r <= 0.06) {
					fobjID = 5;
				}
				else if (r <= 0.08) {
					objID = 7;
					walkable = 0;
				}
				else if (r <= 0.1) {
					fobjID = 4;
				}
				
				// create a forest
				if (i < 70) {
					image = "grass3";
					if (r < 0.3) {
						objID = 1;
						walkable = 0;
					}
				}
				
				// create a desert
				if (i > 120 && j > 90) {
					// desert has its own rules for objects!
					objID = -1;
					if (r < 0.1) {
						objID = 6; 
						walkable = 0;
					}
					else if (r <= 0.13) {
						objID = 7;
						walkable = 0;
					}
					
					if (i == 152 && j == 102) {
						objID = 8;
						walkable = 0;
					}
					else if (i == 152 && j == 103) {
						objID = 9;
						walkable = 0;
					}
					image = "desert";
				}
				
				// surround the border in trees
				if (i < 20 || i > 180 || j < 30 || j > 170) {
					objID = 1;
					walkable = 0;
				}
				
				// a large portion of the bottom will be covered in trees, save for
				// a small path
				if (j > 130 && (i < 99 || i > 105)) {
					objID = 1;
					walkable = 0;
				}
				
				// draw a dirt path where the player starts
				if (j < 170 && j > 30) {
					if (i == 100) {
						objID = -1;
						fobjID = -1;
						image = "path_dirt0";
					}
					if (i == 101) {
						objID = -1;
						fobjID = -1;
						image = "path_dirt1";
					}
					if (i == 102) {
						objID = -1;
						fobjID = -1;
						image = "path_dirt2";
					}
					if (i == 103) {
						objID = -1;
						fobjID = -1;
						image = "path_dirt3";
					}
				}
				
				// build some houses
				if ((i == 105 && j == 81) || (i == 105 && j == 41) || (i == 142 && j == 66)) {
					objID = 10;
					walkable = 0;
				}
				if ((i == 106 && j == 81) || (i == 106 && j == 41) || (i == 143 && j == 66)) {
					objID = 11;
					walkable = 0;
				}
				if ((i == 107 && j == 81) || (i == 107 && j == 41) || (i == 144 && j == 66)) {
					objID = 12;
					walkable = 0;
				}
				if ((i == 108 && j == 81) || (i == 108 && j == 41) || (i == 145 && j == 66)) {
					objID = 13;
					walkable = 0;
				}
				if ((i == 109 && j == 81) || (i == 109 && j == 41) || (i == 146 && j == 66)) {
					objID = 14;
					walkable = 0;
				}
				if ((i == 110 && j == 81) || (i == 110 && j == 41) || (i == 147 && j == 66)) {
					objID = 15;
					walkable = 0;
				}
				if ((i == 105 && j == 80) || (i == 105 && j == 40) || (i == 142 && j == 65)) {
					objID = 16;
					walkable = 0;
				}
				if ((i == 106 && j == 80) || (i == 106 && j == 40) || (i == 143 && j == 65)) {
					objID = 17;
					walkable = 0;
				}
				if ((i == 107 && j == 80) || (i == 107 && j == 40) || (i == 144 && j == 65)) {
					objID = 18;
					walkable = 0;
				}
				if ((i == 108 && j == 80) || (i == 108 && j == 40) || (i == 145 && j == 65)) {
					objID = 19;
					walkable = 0;
				}
				if ((i == 109 && j == 80) || (i == 109 && j == 40) || (i == 146 && j == 65)) {
					objID = 20;
					walkable = 0;
				}
				if ((i == 110 && j == 80) || (i == 110 && j == 40) || (i == 147 && j == 65)) {
					objID = 21;
					walkable = 0;
				}
				if ((i == 105 && j == 79) || (i == 105 && j == 39) || (i == 142 && j == 64)) {
					objID = 22;
					walkable = 0;
				}
				if ((i == 106 && j == 79) || (i == 106 && j == 39) || (i == 143 && j == 64)) {
					objID = 23;
					walkable = 0;
				}
				if ((i == 107 && j == 79) || (i == 107 && j == 39) || (i == 144 && j == 64)) {
					objID = 24;
					walkable = 0;
				}
				if ((i == 108 && j == 79) || (i == 108 && j == 39) || (i == 145 && j == 64)) {
					objID = 25;
					walkable = 0;
				}
				if ((i == 109 && j == 79) || (i == 109 && j == 39) || (i == 146 && j == 64)) {
					objID = 26;
					walkable = 0;
				}
				if ((i == 110 && j == 79) || (i == 110 && j == 39) || (i == 147 && j == 64)) {
					objID = 27;
					walkable = 0;
				}
				if ((i == 105 && j == 78) || (i == 105 && j == 38) || (i == 142 && j == 63)) {
					objID = 28;
					walkable = 0;
				}
				if ((i == 106 && j == 78) || (i == 106 && j == 38) || (i == 143 && j == 63)) {
					objID = 29;
					walkable = 0;
				}
				if ((i == 107 && j == 78) || (i == 107 && j == 38) || (i == 144 && j == 63)) {
					objID = 30;
					walkable = 0;
				}
				if ((i == 108 && j == 78) || (i == 108 && j == 38) || (i == 145 && j == 63)) {
					objID = 31;
					walkable = 0;
				}
				if ((i == 109 && j == 78) || (i == 109 && j == 38) || (i == 146 && j == 63)) {
					objID = 32;
					walkable = 0;
				}
				if ((i == 110 && j == 78) || (i == 110 && j == 38) || (i == 147 && j == 63)) {
					objID = 33;
					walkable = 0;
				}
				if ((i == 107 && j == 77) || (i == 107 && j == 37) || (i == 144 && j == 62)) {
					objID = 34;
					walkable = 0;
				}
				if ((i == 108 && j == 76) || (i == 108 && j == 37) || (i == 145 && j == 62)) {
					objID = 35;
					walkable = 0;
				}
				
				// more different houses
				if ((i == 80 && j == 111) || (i == 80 && j == 41) || (i == 130 && j == 100)) {
					objID = 36;
					walkable = 0;
				}
				if ((i == 81 && j == 111) || (i == 81 && j == 41) || (i == 131 && j == 100)) {
					objID = 37;
					walkable = 0;
				}
				if ((i == 82 && j == 111) || (i == 82 && j == 41) || (i == 132 && j == 100)) {
					objID = 38;
					walkable = 0;
				}
				if ((i == 83 && j == 111) || (i == 83 && j == 41) || (i == 13 && j == 100)) {
					objID = 39;
					walkable = 0;
				}
				if ((i == 80 && j == 110) || (i == 80 && j == 40) || (i == 130 && j == 99)) {
					objID = 40;
					walkable = 0;
				}
				if ((i == 81 && j == 110) || (i == 81 && j == 40) || (i == 131 && j == 99)) {
					objID = 41;
					walkable = 0;
				}
				if ((i == 82 && j == 110) || (i == 82 && j == 40) || (i == 132 && j == 99)) {
					objID = 42;
					walkable = 0;
				}
				if ((i == 83 && j == 110) || (i == 83 && j == 40) || (i == 13 && j == 99)) {
					objID = 43;
					walkable = 0;
				}
				if ((i == 80 && j == 109) || (i == 80 && j == 39) || (i == 130 && j == 98)) {
					objID = 44;
					walkable = 0;
				}
				if ((i == 81 && j == 109) || (i == 81 && j == 39) || (i == 131 && j == 98)) {
					objID = 45;
					walkable = 0;
				}
				if ((i == 82 && j == 109) || (i == 82 && j == 39) || (i == 132 && j == 98)) {
					objID = 46;
					walkable = 0;
				}
				if ((i == 83 && j == 109) || (i == 83 && j == 39) || (i == 133 && j == 98)) {
					objID = 47;
					walkable = 0;
				}
				if ((i == 80 && j == 108) || (i == 80 && j == 38) || (i == 130 && j == 97)) {
					objID = 48;
					walkable = 0;
				}
				if ((i == 81 && j == 108) || (i == 81 && j == 38) || (i == 131 && j == 97)) {
					objID = 49;
					walkable = 0;
				}
				if ((i == 82 && j == 108) || (i == 82 && j == 38) || (i == 132 && j == 97)) {
					objID = 50;
					walkable = 0;
				}
				if ((i == 83 && j == 108) || (i == 83 && j == 38) || (i == 133 && j == 97)) {
					objID = 51;
					walkable = 0;
				}
				if ((i == 80 && j == 107) || (i == 80 && j == 37) || (i == 130 && j == 96)) {
					objID = 52;
					walkable = 0;
				}
				if ((i == 81 && j == 107) || (i == 81 && j == 37) || (i == 131 && j == 96)) {
					objID = 53;
					walkable = 0;
				}
				if ((i == 82 && j == 107) || (i == 82 && j == 37) || (i == 132 && j == 96)) {
					objID = 54;
					walkable = 0;
				}
				if ((i == 83 && j == 107) || (i == 83 && j == 37) || (i == 133 && j == 96)) {
					objID = 55;
					walkable = 0;
				}
				if ((i == 80 && j == 106) || (i == 80 && j == 36) || (i == 130 && j == 95)) {
					objID = 56;
				}
				if ((i == 81 && j == 106) || (i == 81 && j == 36) || (i == 131 && j == 95)) {
					objID = 57;
				}
				if ((i == 82 && j == 106) || (i == 82 && j == 36) || (i == 132 && j == 95)) {
					objID = 58;
				}
				if ((i == 83 && j == 106) || (i == 83 && j == 36) || (i == 133 && j == 95)) {
					objID = 59;
				}
				
				// add an NPC to the map
				if (i == 104 && j == 127) {
					objID = 60;
					walkable = 0;
				}
				if (i == 104 && j == 128) {
					objID = -1;
					walkable = 1;
				}
				
				if (i == 24 && j == 100) {
					objID = 61;
					walkable = 1;
				}
				
				// add values to insert -- bind strings
				stmt.bindLong(1, i);
				stmt.bindLong(2, j);
				stmt.bindString(3, image);
				stmt.bindLong(4, walkable);
				stmt.bindLong(5, objID);
				stmt.bindLong(6, fobjID);
				stmt.bindLong(7, -1);
				
				// execute the statement
				stmt.execute();
				stmt.clearBindings();
				
				// insert the rows
				//db.insert(TABLE_MAP, null, values);	
				
				Log.d("New tile", i+", "+j);
				
			}
		}
		
		// perform transaction
		db.setTransactionSuccessful();
		db.endTransaction();
	}
	
	// add an actor object to the database
	public void addActor(String name, String imgName, int hp, int mp, int pATK, int mATK,
							int mDEF, int pDEF, int spd, ArrayList<Integer> abilities)
	{
		// open database for writing
		SQLiteDatabase db = this.getWritableDatabase();
		
		
		// perform insert
		ContentValues values = new ContentValues();

		// add values to insert
		values.put(COL_NAME, name);
		values.put(COL_IMG, imgName);
		values.put(COL_HP, hp);
		values.put(COL_MP, mp);
		values.put(COL_PATK, pATK);
		values.put(COL_MATK, mATK);
		values.put(COL_MDEF, mDEF);
		values.put(COL_PDEF, pDEF);
		values.put(COL_SPD, spd);
		
		// insert the rows
		db.insert(TABLE_ACTOR, null, values);	
		
		// insert into the ability row
		for (int i = 0; i < abilities.size(); i++) {
			// get the values for the insert
			ContentValues aValues = new ContentValues();
			aValues.put(COL_NAME_ALIST, name);
			aValues.put(COL_AID, abilities.get(i));
			
			// insert the row
			db.insert(TABLE_ALIST, null, aValues);
		}
	}
	
	// add an ability object to the database
	public void addAbility(String name, int base, int cost, String aMod, String dMod, String target,
							String dType, int colour)
	{
		// open database
		SQLiteDatabase db = this.getWritableDatabase();
		
		// perform insert
		ContentValues values = new ContentValues();

		// add values to insert
		values.putNull(COL_AID);
		values.put(COL_NAME, name);
		values.put(COL_BASE, base);
		values.put(COL_COST, cost);
		values.put(COL_AMOD, aMod);
		values.put(COL_DMOD, dMod);
		values.put(COL_TARGET, target);
		values.put(COL_DTYPE, dType);
		values.put(COL_COLOUR, colour);
		
		// insert the rows
		db.insert(TABLE_ABILITY, null, values);

	}
	
	public Tile getTile(int x, int y) {

		// selecting query
		String selectQuery = "SELECT * FROM " + TABLE_MAP + " WHERE "
				+ COL_X +"=" + x + " AND " + COL_Y + "=" + y;
		
		//Log.d("Query", selectQuery);
		
		// get the database, and perform query
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		//Log.d("DB cursor num", ""+cursor.getCount());
		
		// Tile object that we will return
		cursor.moveToFirst();
		Tile t = new Tile(cursor.getString(2), (cursor.getInt(3) == 1), cursor.getInt(4), cursor.getInt(5), this);
		
		// close cursor and db
		cursor.close();
		
		// return the tile object
		return t;
	}
	
	public void createAbilities() {
		this.addAbility("ATTACK", -10, 0, "pATK", "pDEF", "1E", "Slashing", Color.argb(255,137,137,150));
		this.addAbility("FIRE", -50, 10, "mATK", "mDEF", "1E", "Fire", Color.argb(255,255,0,0));
		this.addAbility("NUKE", -30, 10, "mATK", "mDEF", "AE", "Fire", Color.argb(255,255,0,0));
	}
	
	public void createPlayers() {
		
		// List of integers -- define which abilities each actor gets
		ArrayList<Integer> hAbilities = new ArrayList<Integer>();
		ArrayList<Integer> eAbilities = new ArrayList<Integer>();
		
		// Hero abilities -- Attack, Fire, Nuke
		hAbilities.add(1);
		hAbilities.add(2);
		hAbilities.add(3);

		// Enemy abilities -- just attack
		eAbilities.add(1);
		
		// Hero actors
		this.addActor("Bob", "hero1_1", 100, 50, 30, 40, 30, 10, 15, hAbilities);
		this.addActor("Edric", "hero1_1", 100, 50, 30, 40, 30, 10, 15, hAbilities);
		this.addActor("Simon", "hero1_1", 100, 50, 30, 40, 30, 10, 15, hAbilities);
		this.addActor("Sal", "hero1_1", 100, 50, 30, 40, 30, 10, 15, hAbilities);
		
		// Enemy actors
		this.addActor("Gerkamo", "villain1", 100, 20, 30, 30, 30, 30, 13, eAbilities);
		this.addActor("Lilith", "villain1", 100, 20, 30, 30, 30, 30, 13, eAbilities);
		this.addActor("Sil", "villain1", 100, 20, 30, 30, 30, 30, 13, eAbilities);
		this.addActor("Ogopogo", "villain1", 100, 20, 30, 30, 30, 30, 13, eAbilities);
		this.addActor("Wurmy", "sandWurm", 200, 40, 60, 60, 60, 60, 13, eAbilities);
		this.addActor("Greeny", "goblin", 50, 20, 30, 30, 30, 30, 20, eAbilities);
		this.addActor("Fly Eye", "flyingEye", 120, 25, 28, 27, 33, 30, 16, eAbilities);
		this.addActor("Kitty", "fireCat", 140, 30, 30, 35, 30, 30, 23, eAbilities);
		this.addActor("Lizard", "lizard", 150, 40, 30, 35, 20, 40, 10, eAbilities);
	}
	
	/*
	 * THE CODE BELOW NEEDS TO BE FIXED!
	 * 
	 */
	
	public void createParty(String[] enemies, int zone) {
		// open database
		SQLiteDatabase db = this.getWritableDatabase();
				
		// perform insert
		ContentValues values = new ContentValues();

		// add values to insert
		values.putNull(COL_PID);
		values.put(COL_PSIZE, enemies.length);
		values.put(COL_ZONE, zone);
			
		// insert the rows
		db.insert(TABLE_PARTY, null, values);
		
		// find the party id we just added
		SQLiteDatabase db2 = this.getReadableDatabase();
		
		// Selection query -- get that id
		String selectQuery = "SELECT "+COL_PID+" FROM " + TABLE_PARTY + " ORDER BY " + COL_PID + " DESC";
		Cursor cursor = db2.rawQuery(selectQuery, null);
		cursor.moveToFirst();
		int partyid = cursor.getInt(0);
				
		// Start a transaction to create the party lists
		db.beginTransaction();
				
		// Insert statement -- SQL
		String insertQuery = "INSERT INTO " + TABLE_PLIST + " (" + COL_PID + ", " + COL_NAME + ") "
				+ "VALUES (?, ?)";
		
		// will perform multiple inserts transaction
		SQLiteStatement stmt = db.compileStatement(insertQuery);
		
		// get each enemy name
		for (int i = 0; i < enemies.length; i++) {
			// add values to insert -- bind strings
			stmt.bindLong(1, partyid);
			stmt.bindString(2, enemies[i]);
			
			// execute the statement
			stmt.execute();
			stmt.clearBindings();
		}
		
		// perform transaction
		db.setTransactionSuccessful();
		db.endTransaction();

	}
	
	// create the enemy parties
	public void createParties() {

		SQLiteDatabase db = this.getWritableDatabase();
		
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARTY);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLIST);
		
		String CREATE_TABLE_PARTY = "CREATE TABLE " + TABLE_PARTY + "("
				+ COL_PID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ COL_PSIZE + " INTEGER, "
				+ COL_ZONE + " INTEGER"
				+ ")";
		
		String CREATE_TABLE_PARTY_LIST = "CREATE TABLE " + TABLE_PLIST + "("
				+ COL_PID + " INTEGER, "
				+ COL_NAME + " TEXT"
				+ ")";
		
		db.execSQL(CREATE_TABLE_PARTY);
		db.execSQL(CREATE_TABLE_PARTY_LIST);
		
		
		// FOREST ZONE
		createParty(new String[]{"Greeny", "Greeny", "Greeny", "Greeny"}, 1);
		createParty(new String[]{"Greeny", "Fly Eye"}, 1);
		createParty(new String[]{"Fly Eye", "Fly Eye", "Fly Eye"}, 1);
		createParty(new String[]{"Gerkamo", "Greeny", "Gerkamo"}, 1);
		
		// DESERT ZONE
		createParty(new String[]{"Wurmy"}, 2);
		createParty(new String[]{"Kitty", "Kitty", "Kitty"}, 2);
		createParty(new String[]{"Lizard", "Kitty", "Lizard"}, 2);
	}
	
	public void showPartyList() {
		// get each item in the party list
		String selectQuery = "SELECT * FROM " + TABLE_PLIST + " NATURAL JOIN " + TABLE_PARTY;
		SQLiteDatabase db = this.getReadableDatabase();
		
		// Fetch results
		Cursor cursor = db.rawQuery(selectQuery, null);
		cursor.moveToFirst();
		
		for (int i =0; i < cursor.getCount(); i++) {
			Log.d("Name", cursor.getString(cursor.getColumnIndex(COL_NAME)));
			Log.d("PID", ""+cursor.getInt(cursor.getColumnIndex(COL_PID)));
			Log.d("Zone", cursor.getString(cursor.getColumnIndex(COL_ZONE)));
			cursor.moveToNext();
		}
	}
	
	// Find a random party
	public int getPartyFromZone(int zone) {
		
		// party id for return
		int pid = 0;
		
		// get all of the party groups
		String selectQuery = "SELECT * FROM " + TABLE_PARTY + " WHERE " + COL_ZONE + "=" + zone;
		SQLiteDatabase db = getReadableDatabase();
		
		// Fetch the results
		Cursor cursor = db.rawQuery(selectQuery, null);
		cursor.moveToFirst();
		
		// create a random number for the party id
		if (cursor.getCount() > 0)
			pid = ((int) ((int)(Math.random()*100))%cursor.getCount());
		
		for (int i = 0; i < pid; i++) {
			cursor.moveToNext();
		}
		
		return cursor.getInt(0);
	}
	
	public String[] getParty(int pid) {
		
		// get all of the party groups
		String selectQuery = "SELECT * FROM " + TABLE_PLIST + " WHERE " + COL_PID + "=" + pid;
		SQLiteDatabase db = getReadableDatabase();
		
		// Get the results
		Cursor cursor = db.rawQuery(selectQuery, null);
		cursor.moveToFirst();
		String[] enemies = new String[cursor.getCount()];
		
		for (int i = 0; i < enemies.length; i++) {
			enemies[i] = cursor.getString(1);
			cursor.moveToNext();
		}
		return enemies;
	}
	
	
	public String getObj(int oID) {

		// selecting query
		String selectQuery = "SELECT * FROM " + TABLE_OBJ + " WHERE "
				+ COL_OID + "=" + oID;
		
		//Log.d("Obj query", selectQuery);
		
		// get the database, and perform query
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		// Get the object type from the database
		cursor.moveToFirst();
		return (cursor.getString(cursor.getColumnIndex(COL_OTYPE)));
	}
	
	public Tile[] getCol(int xCoord, int yOff, int size) {
		
		// selecting query
		String selectQuery = "SELECT * FROM " + TABLE_MAP + " WHERE "
				+ COL_X +"=" + xCoord + " AND " + COL_Y + ">=" + yOff + " AND " +
				COL_Y + "<" + (yOff+size);
		
		//Log.d("Query", selectQuery);
		
		// get the database, and perform query
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		//Log.d("DB cursor num", ""+cursor.getCount());
		
		// Create an array of tiles to return
		Tile[] col = new Tile[cursor.getCount()];
		cursor.moveToFirst();
		int it = 0;
		
		do {
			col[it] = new Tile(cursor.getString(2), (cursor.getInt(3) == 1), cursor.getInt(4), cursor.getInt(5), this);
			it++;
		} while (cursor.moveToNext());
		
		// close cursor and db
		cursor.close();
		
		// return the column of tiles
		return col;
		
	}
	
	public Tile[] getRow(int yCoord, int xOff, int size) {
		
		// selecting query
		String selectQuery = "SELECT * FROM " + TABLE_MAP + " WHERE "
				+ COL_X +">=" + xOff + " AND " + COL_X + "<" + (xOff+size) + " AND " + COL_Y
				+ "=" + yCoord;
		
		//Log.d("Query", selectQuery);
		
		// get the database, and perform query
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		//Log.d("DB cursor num", ""+cursor.getCount());
		
		// Create an array of tiles to return
		Tile[] row = new Tile[cursor.getCount()];
		cursor.moveToFirst();
		int it = 0;
		
		do {
			row[it] = new Tile(cursor.getString(2), (cursor.getInt(3) == 1), cursor.getInt(4), cursor.getInt(5), this);
			it++;
		} while (cursor.moveToNext());
		
		// close cursor and db
		//cursor.close();
		
		// return the column of tiles
		return row;
		
	}
	
	// get a GameActor from the database
	public GameActor getActor(String name) {
		// selecting query
		String selectQuery = "SELECT * FROM " + TABLE_ACTOR + " WHERE "
				+ COL_NAME + "=\"" + name +"\"";
		
		//Log.d("Obj query", selectQuery);
		
		// get the database, and perform query
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		// Move cursor to the first result, and then build the GameActor object
		cursor.moveToFirst();
		String cName = cursor.getString(cursor.getColumnIndex(COL_NAME));
		String imgName = cursor.getString(cursor.getColumnIndex(COL_IMG));
		int hp = cursor.getInt(cursor.getColumnIndex(COL_HP));
		int mp = cursor.getInt(cursor.getColumnIndex(COL_MP));
		int pATK = cursor.getInt(cursor.getColumnIndex(COL_PATK));
		int mATK = cursor.getInt(cursor.getColumnIndex(COL_MATK));
		int mDEF = cursor.getInt(cursor.getColumnIndex(COL_MDEF));
		int pDEF = cursor.getInt(cursor.getColumnIndex(COL_PDEF));
		int spd = cursor.getInt(cursor.getColumnIndex(COL_SPD));
		
		// get the abilities for the actor
		ArrayList<Ability> abilities = getAbilities(cName);
		
		// build the GameActor object
		GameActor newActor = new GameActor(cName, SpriteSheet.getTileBitmap(imgName), hp, mp, pATK,
											mATK, mDEF, pDEF, spd, abilities);
		
		cursor.close();
		return newActor;
	}
	
	// Get an array that contains each of the actor's abilities
	public ArrayList<Ability> getAbilities(String name) {
		
		// get the list of abilities -- inner join of ability table and player ability list table
		// to get the specific abilities of a player
		String selectQuery = "SELECT * FROM " + TABLE_ABILITY + " NATURAL JOIN " + TABLE_ALIST + " WHERE "
				+ COL_NAME_ALIST + "=\"" + name + "\"";

		// get the database, and perform query
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		// create a list to put abilities in
		ArrayList<Ability> abilities = new ArrayList<Ability>();
		
		// move to the first item in the cursor
		cursor.moveToFirst();
		
		// get each item
		for (int i = 0; i < cursor.getCount(); i++) {
			String aName = cursor.getString(cursor.getColumnIndex(COL_NAME));
			int base = cursor.getInt(cursor.getColumnIndex(COL_BASE));
			int cost = cursor.getInt(cursor.getColumnIndex(COL_COST));
			String aMod = cursor.getString(cursor.getColumnIndex(COL_AMOD));
			String dMod = cursor.getString(cursor.getColumnIndex(COL_DMOD));
			String target = cursor.getString(cursor.getColumnIndex(COL_TARGET));
			String dType = cursor.getString(cursor.getColumnIndex(COL_DTYPE));
			int colour = cursor.getInt(cursor.getColumnIndex(COL_COLOUR));
			
			// go to the next item
			cursor.moveToNext();
			
			// create the ability, add it to the list
			abilities.add(new Ability(aName, base, cost, aMod, dMod, target, dType, colour));
		}
		
		cursor.close();
		// return the full list of abilities
		return abilities;
	}
	
	public boolean hasEmptyMap() {
		
		// selecting query
		String selectQuery = "SELECT * FROM " + TABLE_MAP;
		
		// get the database, and perform query
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		//Log.d("Num items in map: ", ""+cursor.getCount());
		
		// check how many rows were returned by the query
		if (cursor.getCount() == 0) {
			// close cursor and db
			cursor.close();
			
			return true;
		}
		
		// close cursor and db
		cursor.close();
		
		return false;
	}	
	
	public boolean hasEmptyActors() {
		// selecting query
		String selectQuery = "SELECT * FROM " + TABLE_ACTOR;
			
		// get the database, and perform query
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		//Log.d("Num items in map: ", ""+cursor.getCount());
		
		// check how many rows were returned by the query
		if (cursor.getCount() == 0) {
			// close cursor and db
			cursor.close();
			
			return true;
		}
		
		// close cursor and db
		cursor.close();
		
		return false;
	}
	
	public boolean hasEmptyAbilities() {
		// selecting query
		String selectQuery = "SELECT * FROM " + TABLE_ABILITY;
			
		// get the database, and perform query
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		//Log.d("Num items in map: ", ""+cursor.getCount());
		
		// check how many rows were returned by the query
		if (cursor.getCount() == 0) {
			// close cursor and db
			cursor.close();
			
			return true;
		}
		
		// close cursor and db
		cursor.close();
		
		return false;
	}
	
	public boolean noActorAbilities() {
		String selectQuery = "SELECT * FROM " + TABLE_ABILITY + " NATURAL JOIN " + TABLE_ALIST;
			
		// get the database, and perform query
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		//Log.d("Num items in map: ", ""+cursor.getCount());
		
		// check how many rows were returned by the query
		if (cursor.getCount() == 0) {
			// close cursor and db
			cursor.close();
			
			return true;
		}
		
		cursor.moveToFirst();
		for (int i = 0; i < cursor.getCount(); i++) {
			Log.d("ALIST #"+i, cursor.getString(cursor.getColumnIndex(COL_NAME_ALIST)));
			Log.d("ALIST #"+i, cursor.getString(cursor.getColumnIndex(COL_AID)));
			Log.d("ALIST #"+i, cursor.getString(cursor.getColumnIndex(COL_NAME)));
			cursor.moveToNext();
		}
		
		// close cursor and db
		cursor.close();
			
		return false;
	}
}