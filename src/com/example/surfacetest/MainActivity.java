package com.example.surfacetest;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {

	private BGMusicService music;
	private boolean firstTime = true;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		if (!this.firstTime) {
			Intent i = new Intent(this, BGMusicService.class);
			i.putExtra("song", 1);
		    startService(i);	
		}
		else {
		    this.firstTime = false;			
		}
	}
	
	protected void onPause() {
		super.onPause();
		stopService(new Intent(this, BGMusicService.class));
	}
}
