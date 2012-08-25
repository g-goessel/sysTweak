package com.kalgecin.systweak;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver{
	@Override
	public void onReceive(final Context context, final Intent bootintent){
		Intent mServiceIntent = new Intent();
		mServiceIntent.setAction("com.kalgecin.systweak.BootService");
		context.startService(mServiceIntent);
	}
}
