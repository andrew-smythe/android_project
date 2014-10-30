package com.example.surfacetest;

import java.io.IOException;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.IBinder;
import android.util.Log;

public class BGMusicService extends Service {
	private static final String TAG = "MyService";
	MediaPlayer player;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		Log.d(TAG, "onCreate");
	}

	@Override
	public void onDestroy() {
		Log.d(TAG, "onDestroy");
		player.stop();
	}
	
	@Override
	public void onStart(Intent intent, int startid) {
		Log.d(TAG, "onStart");
		
		if (intent.hasExtra("song")) {
			if (intent.getExtras().getInt("song") == 1) {
				player = MediaPlayer.create(this, R.raw.lostwoods);				
			}
			else if (intent.getExtras().getInt("song") == 2) {
				player = MediaPlayer.create(this, R.raw.battle);
			}
			player.setLooping(true); // Set looping
			player.start();
		}
	}
}