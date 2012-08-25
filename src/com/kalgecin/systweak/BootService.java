package com.kalgecin.systweak;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class BootService extends Service{
	private final long mDelay = 0;
	private final long mPeriod = 500;
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
		mTimer.schedule(mLogTask, mDelay, mPeriod);
	}
}
