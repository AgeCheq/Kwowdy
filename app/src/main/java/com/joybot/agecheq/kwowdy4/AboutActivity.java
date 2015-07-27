package com.joybot.agecheq.kwowdy4;


import android.os.Bundle;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.widget.TextView;


public class AboutActivity extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //lock in portrait
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //set the layout
        setContentView(R.layout.activity_about);

        //set the font on the question text views
        TextView textView1 =(TextView)findViewById(R.id.textView1);
        Typeface typeFace=Typeface.createFromAsset(getAssets(),"playtime.ttf");
        textView1.setTypeface(typeFace);


    }

}
