package com.example.kwowdy3;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.agecheq.kwowdy3.R;
import com.agecheq.agecheqlib.*;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.ads.identifier.AdvertisingIdClient.Info;


public class AgeCheqActivity extends Activity implements AgeCheqServerInterface {
	
	private String DevKey   = "06c3a8ba-8d2e-429c-9ce6-4f86a70815d6";
	private String AppId    = "21cdc227-48ad-4cf5-a67c-92f00a3dbef7";
	private String AgeCheqPIN = "";
	public  String ZipCode  = "";
	
	private boolean boolDebug = false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);

		//test the zip code and ParentID
	    if (!HomeActivity.globalZipcode.equals("")) {
	    	ZipCode = HomeActivity.globalZipcode;
	    }
	    

	    if (!HomeActivity.globalChildID.equals("")) {
	    	s
	    	//grab the AgeCheq PIN from the home activity
	    	AgeCheqPIN = HomeActivity.globalChildID;
	    }
	    
		//lock in portrait
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 
						
		//set the layout
		setContentView(R.layout.activity_agecheq);
		
 	    //get the controls
	    TextView textHeading = (TextView) findViewById(R.id.textHeading);
	    TextView textParagraph = (TextView) findViewById(R.id.textParagraph);
		
		//set the font on the question text views
		Typeface typeFace=Typeface.createFromAsset(getAssets(),"playtime.ttf");
		textHeading.setTypeface(typeFace);
		textParagraph.setTypeface(typeFace);
	    	
	}

	
	@Override
	protected void onResume() {
		super.onResume();
		
		//clear the edit field
		EditText editText = (EditText)findViewById(R.id.editTextField);
		editText.setText("");
		
		doCheck();
	}
	
	//----------------------------------------
	// AgeCheq Commands
	//----------------------------------------
	
	private void doCheck() {
		
	
		if (!AgeCheqPIN.equals("") )
		{
			AgeCheqApi.check(this, DevKey, AppId, AgeCheqPIN);
		}
		else
		{
			showNoChildID();
		}

	}
	

	
	//----------------------------------------
	// Multi-Button Clicks
	//----------------------------------------
	public void doLeft(View view) {
		
		if (boolDebug == true) {
			AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);                      
		    dlgAlert.setMessage("IN DO LEFT");
		    dlgAlert.setTitle("DEBUG");              
		    dlgAlert.setPositiveButton("OK", null);
		    dlgAlert.setCancelable(true);
		    dlgAlert.create().show();
		}
		

		//get the text on the button on the click
		Button tempBtn = (Button)view;

		//get the AgeCheqPIN and test 
		if (tempBtn.getText().toString().equals(getResources().getString(R.string.btnRegister))) {
			
			//collect the AgeCheq PIN
			EditText editText = (EditText)findViewById(R.id.editTextField);
			AgeCheqPIN = editText.getText().toString();
			
			//check for parental approval 
			doCheck();
			
		}
		
		//check for parental approval 
		if ( tempBtn.getText().toString().equals(getResources().getString(R.string.btnCheckForApproval)))  {
			
			//We already have an AgeCheq PIN, check for parental approval again
			doCheck();
		} 
		
		//zip code submit
		if (tempBtn.getText().toString().equals(getResources().getString(R.string.enterZipCode))) {
			
			//collect the zip code
			EditText editText = (EditText)findViewById(R.id.editTextField);
			ZipCode = editText.getText().toString();
			HomeActivity.globalZipcode = ZipCode;
			
			//TODO: Make sure it is a valid zip code?
			if (ZipCode.length() == 5) {

				//show the weather at this ZIP code
				startActivity(new Intent(AgeCheqActivity.this, ForecastActivity.class));
				
				//clear the edit field
				editText.setText("");
			}
			else
			{
				//invalid zip code
				AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);                      
			    dlgAlert.setMessage("I\'m afraid this is an invalid zip code.  Please try again.");
			    dlgAlert.setTitle("Invalid Zip Code");              
			    dlgAlert.setPositiveButton("OK", null);
			    dlgAlert.setCancelable(true);
			    dlgAlert.create().show();
			    editText.setText("");
			}
			
			//TODO: Save the ZIP code if it is valid?
		} 
	}
	
	public void doRight(View view) {
		//open a browser window to the Parent Dashboard
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://parent.agecheq.com"));
		startActivity(browserIntent);
	}
	
	
	//----------------------------------------
	// Response Handlers
	//----------------------------------------
	
    @Override
    public void onCheckResponse(String rtn, String rtnmsg, int checktype, 
			  Boolean appauthorized,
			  Boolean appblocked,
			  int parentverified,	  
			  Boolean under13,
			  Boolean under18,
			  Boolean underdevage,
			  int trials)
    {
    	
    	
    	if (boolDebug) {
	    	AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);                      
		    dlgAlert.setMessage("Check Response = " + rtn + ",AppAuthorized=" + appauthorized + ",AppBlocked=" + appblocked);
		    dlgAlert.setTitle("CHECK");              
		    dlgAlert.setPositiveButton("OK", null);
		    dlgAlert.setCancelable(true);
		    dlgAlert.create().show();
    	}
    	
    	Log.d("zz", "onCheckResponse");     	
    	Log.d("zz", "rtn=" + rtn);      	
    	Log.d("zz", "rtnmsg=" + rtnmsg);   

    	if (!rtn.equals("ok") )
    	{
    		//Tell the user that the Internet has failed for whatever reason
    		
     	    //get the text paragraph
    	    TextView textParagraph = (TextView) findViewById(R.id.textParagraph);
    	    
    	    //set the text of the paragraph to say that the connection failed
    	    textParagraph.setText(R.string.connectionFailed);
    	}
    	else
    	{
    		//Save the AgeCheqPIN
    		HomeActivity.globalChildID = AgeCheqPIN;
    		SharedPreferences settings = getSharedPreferences(HomeActivity.PREFS_NAME, 0);
    		SharedPreferences.Editor editor = settings.edit();
    		editor.putString("childid", HomeActivity.globalChildID);
    		editor.commit();
	    	
	    	//check to see if the app has been authorized to collect PII
	     	if (!appauthorized) {
	     		showUnauthorizedDevice(false);
	
	     	} else {
	     		//check to see if the app has been blocked by a parent or not
	     		if (appblocked) {
	     			showBlockedDevice();
	     		} else {
	     			//we are now free to collect a ZIP code from the child
	     			showZipCodeCollection();
	     		}
	     	}
    	}
	    
    	
    	/*
     	RelativeLayout ua = (RelativeLayout) findViewById(R.id.notauthorized);
     	TextView msg = (TextView) findViewById(R.id.uaMsg);
     	*/
    } 
    
   
    
    @Override
    public void onAssociateDataResponse(String rtn, String rtnmsg) {
		AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);                      
	    dlgAlert.setMessage("Register Response = " + rtn +"-" + rtnmsg);
	    dlgAlert.setTitle("REGISTER");              
	    dlgAlert.setPositiveButton("OK", null);
	    dlgAlert.setCancelable(true);
	    dlgAlert.create().show();
    }    
    
    
    public void onAgeCheqServerError(String paramString) {
    	
    }
    

    //-----------------------------------------------
    // UI Setups
    //-----------------------------------------------    
    private void showNoChildID() {
    	
 	    //get the controls
	    TextView textHeading = (TextView) findViewById(R.id.textHeading);
	    TextView textParagraph = (TextView) findViewById(R.id.textParagraph);
	    ImageView imgCloud = (ImageView) findViewById(R.id.imageSadKwowdy);
	    Button btnLeft = (Button)findViewById(R.id.buttonLeft);
	    Button btnRight = (Button)findViewById(R.id.buttonRight);
	    EditText editText = (EditText)findViewById(R.id.editTextField);

	    //clear the edit field
	    editText.setText("");
	    
	    //set the text of the paragraph and buttons
	    textHeading.setText(R.string.giveParentalConsent);
	    textParagraph.setText(R.string.parentalVerification);
	    btnLeft.setText(R.string.btnRegister);
	    btnRight.setText(R.string.btnNewParent);
	    
	    //make the buttons visible
	    btnLeft.setVisibility(View.VISIBLE);
	    btnRight.setVisibility(View.VISIBLE);
	    
	    //TODO: Update the Kwowdy character
    	
    }
    
    private void showUnauthorizedDevice(boolean boolJustRegistered) {
	    //get the controls
	    TextView textHeading = (TextView) findViewById(R.id.textHeading);
	    TextView textParagraph = (TextView) findViewById(R.id.textParagraph);
	    ImageView imgCloud = (ImageView) findViewById(R.id.imageSadKwowdy);
	    Button btnLeft = (Button)findViewById(R.id.buttonLeft);
	    Button btnRight = (Button)findViewById(R.id.buttonRight);
	    EditText editText = (EditText)findViewById(R.id.editTextField);
	    
	    //clear the edit field
	    editText.setText("");

	    //set the text of the paragraph and buttons
	    textParagraph.setText(R.string.explainParentalConsent);
	    btnLeft.setText(R.string.btnCheckForApproval);
	    btnRight.setText(R.string.btnOpenParentDashboard);
	    	    
	    //hide the buttons and edit field
	    btnLeft.setVisibility(View.VISIBLE);
	    btnRight.setVisibility(View.VISIBLE);
	    editText.setVisibility(View.GONE);
	    
	   
	    if (boolJustRegistered) {
	    	//this device was *just* registered with an AgeCheq Parent Dashboard account
	    	 textHeading.setText(R.string.oneMoreStep);
	    } else {
	    	//device already registered, but we still aren't authorized 
	    	 textHeading.setText(R.string.giveParentalConsent);
	    }
	    
	    //TODO: Update the Kwowdy character
    	
    }
    
    private void showBlockedDevice() {
	    //get the controls
	    TextView textHeading = (TextView) findViewById(R.id.textHeading);
	    TextView textParagraph = (TextView) findViewById(R.id.textParagraph);
	    ImageView imgCloud = (ImageView) findViewById(R.id.imageSadKwowdy);
	    Button btnLeft = (Button)findViewById(R.id.buttonLeft);
	    Button btnRight = (Button)findViewById(R.id.buttonRight);
	    EditText editText = (EditText)findViewById(R.id.editTextField);

	    //clear the edit field
	    editText.setText("");
	    
	    //set the text of the paragraph and buttons
	    textHeading.setText(R.string.blocked);
	    textParagraph.setText(R.string.aboutBlocked);
	    	    
	    //hide the buttons and edit field
	    btnLeft.setVisibility(View.GONE);
	    btnRight.setVisibility(View.GONE);
	    editText.setVisibility(View.GONE);
	    
	    //TODO: Update the Kwowdy character
    	
    }
    
    private void showZipCodeCollection() {
	    //get the controls
	    TextView textHeading = (TextView) findViewById(R.id.textHeading);
	    TextView textParagraph = (TextView) findViewById(R.id.textParagraph);
	    ImageView imgCloud = (ImageView) findViewById(R.id.imageSadKwowdy);
	    Button btnLeft = (Button)findViewById(R.id.buttonLeft);
	    Button btnRight = (Button)findViewById(R.id.buttonRight);
	    EditText editText = (EditText)findViewById(R.id.editTextField);
	    
	    //clear the edit field
	    editText.setText("");
	    
		//Prime the zip code field and change the edit field to accept numbers only
	    if (!ZipCode.equals("")) {
	    	editText.setText(ZipCode);
	    }
		editText.setInputType(InputType.TYPE_CLASS_NUMBER);
	    
	    //set the text of the paragraph and buttons
	    textHeading.setText(R.string.getZipCode);
	    textParagraph.setText(R.string.explainZipCode);
	    btnLeft.setText(R.string.enterZipCode);
	    	    
	    //hide the buttons and edit field
	    btnLeft.setVisibility(View.VISIBLE);
	    btnRight.setVisibility(View.GONE);
	    editText.setVisibility(View.VISIBLE);
    	
	    //make Kwowdy happy!
	    imgCloud.setImageResource(R.drawable.kwowdyhappy);
     }
    




}
