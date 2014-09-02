package com.example.kwowdy3;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.agecheq.kwowdy3.R;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.SoundPool;

public class InitialActivity extends Activity {

	//sound constants
	private SoundPool soundPool;
	private int soundID;
	AudioManager audioManager;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
				
		//lock in portrait
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
		
		//set the layout
		setContentView(R.layout.activity_initial);
		
		audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		
		

		// prepare "hi!" message
		soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {

			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
	
				//play the "hi!" message as soon as it's loaded
				soundPool.play(soundID,(float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),(float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 1, 0, 1f);
			}

		});

		//load the "hi!" message
		soundID = soundPool.load(this, R.raw.hiimkwowdy, 1);

		
	}
	
	
	public void onGetStarted(View view) {
		startActivity(new Intent(InitialActivity.this, HomeActivity.class));
		finish();
	}
	
	
	
}
