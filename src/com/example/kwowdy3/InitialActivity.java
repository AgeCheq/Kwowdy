package com.example.kwowdy3;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.agecheq.kwowdy3.R;
import com.example.kwowdy3.ForecastActivity.LongRunningGetIO;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;


public class InitialActivity extends Activity {

	//sound constants
	private SoundPool soundPool;
	private int soundID;
	AudioManager audioManager;

	//webview for Privacy Policy viewer
	private WebView wv;
	
	//unique identifier
	String userID = "";
	
	//PrivacyCheq App ID
	String appID = "9bed1ddf-fcbe-46ab-8af7-80bf16f947bf";
	
	

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
				
		//lock in portrait
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
		
		//set the layout
		setContentView(R.layout.activity_initial);
		
		//Do the CliqWrap code immediately
		showSplash();
	
	}
	
	public void onGetStarted(View view) {
		startActivity(new Intent(InitialActivity.this, HomeActivity.class));
		finish();
	}
	
	public void showSplash() {
		
		//start the audio manager
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
	

	
	
}
