package com.example.surfacetest;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.widget.EditText;

public class CombatActivity extends Activity {
	public int pid;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_combat);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	public void finish(boolean result) {
	    super.finish();
	    stopService(new Intent(this, BGMusicService.class));
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra("COMBAT_RESULT", result);
		setResult(RESULT_OK, intent);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		// Receive the intent from MainActivity, get the party id
		Intent intent = getIntent();
		this.pid = intent.getIntExtra("pid", 0);
		
		// restart music service
		Intent i = new Intent(this, BGMusicService.class);
		i.putExtra("song", 2);
	    startService(i);	
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		stopService(new Intent(this, BGMusicService.class));		
	}

}
