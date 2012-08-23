package com.kalgecin.systweak;

import java.io.IOException;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnSet 				= (Button) findViewById(R.id.btnSetOnBoot);
        btnSet.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SetChecks();
			}
		});
    }
    public void SetChecks(){
    	 CheckBox chkRomManager 	= (CheckBox) findViewById(R.id.chkRomManager);
         CheckBox chkLiveWallpapers = (CheckBox) findViewById(R.id.chkLiveWallpapers);
         CheckBox chkCMWallpapers 	= (CheckBox) findViewById(R.id.chkCMWallpapers);
         CheckBox chkGTTS 			= (CheckBox) findViewById(R.id.chkGTTS);
         CheckBox chkMovie 			= (CheckBox) findViewById(R.id.chkMovieStudio);
         CheckBox chkGmail 			= (CheckBox) findViewById(R.id.chkGmail);
         CheckBox chkTvOut			= (CheckBox) findViewById(R.id.chkTvOut);
         CheckBox chkSetOnBoot 		= (CheckBox) findViewById(R.id.chkBoot);
         ProcessBuilder cmd;
         Process process;
         try{
        	 String[] args = {"su","-c","pm","enable",""};
	         if(chkRomManager.isChecked()){
	        	 //enable RomManager
	        	 args[3] = "enable";
	        	 args[4] = "com.koushikdutta.rommanager";
	         }else{
	        	 //disable RomManager
	        	 args[3] = "disable";
	        	 args[4] = "com.koushikdutta.rommanager";
	         }
	         cmd = new ProcessBuilder(args);
        	 process = cmd.start();
        	 
	         if(chkLiveWallpapers.isChecked()){
	        	 //enable LiveWallpapers
	        	 args[3] = "enable";
	        	 args[4] = "com.android.wallpaper.livepicker";
	         }else{
	        	 //disable LiveWallpapers
	        	 args[3] = "disable";
	        	 args[4] = "com.android.wallpaper.livepicker";
	         }
	         cmd = new ProcessBuilder(args);
        	 process = cmd.start();
        	 
	         if(chkCMWallpapers.isChecked()){
	        	 //enable CM Wallpapers 
	        	 args[3] = "enable";
	        	 args[4] = "com.cyanogenmod.CMWallpapers";
	         }else{
	        	 //disable CM Wallpapers
	        	 args[3] = "disable";
	        	 args[4] = "com.cyanogenmod.CMWallpapers";
	         }
	         cmd = new ProcessBuilder(args);
        	 process = cmd.start();
        	 
        	 if(chkTvOut.isChecked()){
        		 args[3] = "enabled";
        		 args[4] = "com.teamhacksung.tvout";
        	 }else{
        		 args[3] = "disabled";
        		 args[4] = "com.teamhacksung.tvout";
        	 }
        	 cmd = new ProcessBuilder(args);
        	 process = cmd.start();
        	 
	         if(chkGTTS.isChecked()){
	        	 //enable Google Text-To-Speech
	        	 args[3] = "enabled";
	        	 args[4] = "com.google.android.tts";
	         }else{
	        	 //disable Google Text-To-Speech
	        	 args[3] = "disabled";
	        	 args[4] = "com.google.android.tts";
	         }
	         cmd = new ProcessBuilder(args);
	         process = cmd.start();
	         
	         if(chkMovie.isChecked()){
	        	 //enable Movie Editor
	         }else{
	        	 //disable Movie Editor
	         }
	         if(chkGmail.isChecked()){
	        	 //enable Gmail
	         }else{
	        	 //disable GMail
	         }
	         if(chkSetOnBoot.isChecked()){
	        	 //enable applying of settings on boot
	         }else{
	        	 //disable applying of settings on boot
	         }
         }catch(IOException e){}
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
