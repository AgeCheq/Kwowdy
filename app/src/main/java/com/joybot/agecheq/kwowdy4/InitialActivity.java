package com.joybot.agecheq.kwowdy4;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.content.Intent;
import android.view.View;

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

        //show and play splash content
        showSplash();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_initial, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
