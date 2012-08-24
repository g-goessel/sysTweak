package com.kalgecin.systweak;

import java.io.IOException;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class MainActivity extends Activity {
	private settingsDB dataSrc;
	CheckBox chkRomManager;
	CheckBox chkLiveWallpapers;
	CheckBox chkCMWallpapers;
	CheckBox chkGTTS;
	CheckBox chkMovie;
	CheckBox chkGmail;
	CheckBox chkTvOut;
	String[] checks = {"chkRomManager","chkLiveWallpapers","chkCMWallpapers","chkGTTS","chkMovie","chkGmail","chkTvOut"};
	String[] CHKnames = {"com.koushikdutta.rommanager","com.android.wallpaper.livepicker","com.cyanogenmod.CMWallpapers",
						"com.google.android.tts","com.movie","com.google.android.gm","com.teamhacksung.tvout"};
	CheckBox[] CBchecks = {chkRomManager,chkLiveWallpapers,chkCMWallpapers,chkGTTS,chkMovie,chkGmail,chkTvOut};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnSet 		= (Button) findViewById(R.id.btnSetOnBoot);
        chkRomManager 		= (CheckBox) findViewById(R.id.chkRomManager);
        chkLiveWallpapers 	= (CheckBox) findViewById(R.id.chkLiveWallpapers);
        chkCMWallpapers 	= (CheckBox) findViewById(R.id.chkCMWallpapers);
        chkGTTS 			= (CheckBox) findViewById(R.id.chkGTTS);
        chkMovie 			= (CheckBox) findViewById(R.id.chkMovieStudio);
        chkGmail 			= (CheckBox) findViewById(R.id.chkGmail);
        chkTvOut			= (CheckBox) findViewById(R.id.chkTvOut);
        
        dataSrc = new settingsDB(this);
        dataSrc.open();
        loadChecks();
        
        btnSet.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SetChecks();
			}
		});
    }
    public void loadChecks(){
    	for(int i=0;i<checks.length;i++){
    		CBchecks[i].setChecked(Boolean.getBoolean(dataSrc.getSetting(checks[i])));
    	}
    	
    }
    public void SetChecks(){
    	 
         ProcessBuilder cmd;
         Process process;
         try{
        	 String[] args = {"su","-c","pm","enable",""};
        	 for(int i=0;i<checks.length;i++){
        		 args[4] = CHKnames[i];
        		 if(CBchecks[i].isChecked()){
        			 args[3] = "enabled";
        		 }else{
        			 args[3] = "disabled";
        		 }
        		 cmd = new ProcessBuilder(args);
            	 process = cmd.start();
        	 }
         }catch(IOException e){}
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
    	switch(item.getItemId()){
	    	case R.id.menu_settings:
	    		Intent intent_settings = new Intent(getApplicationContext(),menu_settings.class);
	    		startActivity(intent_settings);
	    		return true;
	    	case R.id.menu_about:
	    		Intent intent_about = new Intent(getApplicationContext(),menu_about.class);
	    		startActivity(intent_about);
	    		return true;
	    	default:
	    		return super.onOptionsItemSelected(item);
    		
    	}
    }
}
