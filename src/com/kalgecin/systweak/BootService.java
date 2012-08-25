package com.kalgecin.systweak;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class BootService extends Service{
	private final long mDelay = 0;
	private final long mPeriod = 5000;
	private final String LOGTAG = "SysTweakBoot";
	private Timer mTimer;
	private class LogTask extends TimerTask {
		public void run(){
			Log.i(LOGTAG,"scheduled run");
		}
	}
	private LogTask mLogTask;
	
	@Override
	public IBinder onBind(final Intent intent){
		return null;
	}
	
	@Override
	public void onCreate(){
		super.onCreate();
		Log.i(LOGTAG,"created");
		mTimer = new Timer();
		mLogTask = new LogTask();
	}
	
	@Override
	public void onStart(final Intent intent, final int startId){
		super.onStart(intent, startId);
		Log.i(LOGTAG,"started");
		settingsDB dataSrc = new settingsDB(this);
		dataSrc.open();
		Log.i(LOGTAG,"opened DB");
		if(dataSrc.getSetting("on_boot").equalsIgnoreCase("true")){
				//run disabler
			 ProcessBuilder cmd;
	         Process process;
	         //Enable or disable services/apps
	         try{
	        	String[] args = {"su","-c","pm","enable",""};
	        	String[] checks = MainActivity.checks;
	        	String[] CHKnames = MainActivity.CHKnames;
				for(int i=0;i<checks.length;i++){
					args[4] = CHKnames[i];
	        		 if(dataSrc.getSetting(checks[i]).equalsIgnoreCase("true")){//CBchecks[i].isChecked()){
	        			 args[3] = "enable";
	        		 }else{
	        			 args[3] = "disable";
	        		 }
	        		 Log.i("sysTweak_BOOT",args[4]+":"+args[3]);
	        		 cmd = new ProcessBuilder(args);
	            	 process = cmd.start();
				}
	         }catch(IOException e){
	        	 e.printStackTrace();
	         }
		}else{
			Log.i("sysTweak_BOOT","on_boot is disabled");
		}
		//mTimer.schedule(mLogTask, mDelay, mPeriod);
	}
}
