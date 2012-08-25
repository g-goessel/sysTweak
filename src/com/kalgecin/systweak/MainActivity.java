package com.kalgecin.systweak;

import java.io.IOException;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

public class MainActivity extends Activity {
	private settingsDB dataSrc;
	Switch swRomManager,swLiveWallpapers,swCMWallpapers,swGTTS,swMovie,swGmail,swTvOut,swPhone,swApollo,swDSPManager;
	Switch swEmail,swNewsAndWeather,swGTalk,swTerminalEmulator,swTorch,swMediaScanner;
	
	public static String[] checks = {"swRomManager","swLiveWallpapers","swCMWallpapers","swGTTS","swMovie","swGmail","swTvOut","swPhone",
						"swApollo","swDSPManager","swEmail","swNewsAndWeather","swGTalk","swTerminalEmulator","swTorch",
						"swMediaScanner"};
	
	public static String[] CHKnames = {"com.koushikdutta.rommanager","com.android.wallpaper.livepicker","com.cyanogenmod.CMWallpapers",
						"com.google.android.tts","com.android.videoeditor","com.google.android.gm","com.teamhacksung.tvout",
						"com.android.phone","com.andrew.apollo","com.bel.android.dspmanager","com.android.email",
						"com.google.android.apps.genie.geniewidget","com.google.android.talk","jackpal.androidterm",
						"net.cactii.flash2","com.android.providers.media/com.android.providers.media.MediaScannerReceiver"};
	
	Switch[] CBchecks = {swRomManager,swLiveWallpapers,swCMWallpapers,swGTTS,swMovie,swGmail,swTvOut,swPhone,swApollo,
						swDSPManager,swEmail,swNewsAndWeather,swGTalk,swTerminalEmulator,swTorch,swMediaScanner};
	
	int[] CBchecksID = {R.id.swRomManager,R.id.swLiveWallpapers,R.id.swCMWallpapers,R.id.swGTTS,R.id.swMovieStudio,
						R.id.swGmail,R.id.swTvOut,R.id.swPhone,R.id.swApollo,R.id.swDSPManager,R.id.swEmail,R.id.swNewsAndWeather,
						R.id.swGTalk,R.id.swTerminalEmulator,R.id.swTorch,R.id.swMediaScanner};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnSet 		= (Button) findViewById(R.id.btnSetOnBoot);
        
        for(int i=0;i<checks.length;i++){
        	CBchecks[i] = (Switch) findViewById(CBchecksID[i]);
        }
        
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
    		Boolean b;
    		if(dataSrc.getSetting(checks[i]).equalsIgnoreCase("true")){
    			b=true;
    			Log.i("loadChecks","IT'S TRUE");
    		}else{
    			b=false;
    			Log.i("loadChecks","false alarm");
    		}
    		Log.i(this.getPackageName(), checks[i]+","+i+","+Boolean.toString(b));
    		CBchecks[i].setChecked(b);
    	}
    	
    }
    public void SetChecks(){
    	 
         ProcessBuilder cmd;
         Process process;
         //Enable or disable services/apps
         try{
        	 String[] args = {"su","-c","pm","enable",""};
        	 for(int i=0;i<checks.length;i++){
        		 args[4] = CHKnames[i];
        		 if(CBchecks[i].isChecked()){
        			 args[3] = "enable";
        		 }else{
        			 args[3] = "disable";
        		 }
        		 Log.i("Toggle_service",args[4]+":"+args[3]);
        		 cmd = new ProcessBuilder(args);
            	 process = cmd.start();
        	 }
         }catch(IOException e){
        	 e.printStackTrace();
         }
         //Save current state of checks to DB
         for(int i=0;i<checks.length;i++){
        	 dataSrc.addSetting(checks[i], Boolean.toString(CBchecks[i].isChecked()));
         }
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
