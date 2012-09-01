package com.kalgecin.systweak;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

public class SwitchManager extends Activity{
	static PackageManager pm;
	Context context;
	public SwitchManager(Context c) {
		context = c;
		pm = context.getPackageManager();
	}
	public boolean checkState(String packageName){
		List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
		for(ApplicationInfo packageInfo : packages){
			if(packageInfo.packageName.equalsIgnoreCase(packageName)){
				if(packageInfo.enabled){
					return true;
				}else{
					return false;
				}
			}
		}
		return false;
	}
	public List<ApplicationInfo> getAllPackages(){
		pm = getPackageManager();
		
		return pm.getInstalledApplications(PackageManager.GET_META_DATA);
		
	}
	public void toggleState(String packageName, boolean state){
		String tag = "sysTweak_SMtoggleState";
		Log.i(tag,"Toggle "+ packageName+" to "+state);
		Process process;
		ProcessBuilder cmd;
		cmd = new ProcessBuilder("su");
		try {
			process = cmd.start();
			cmd.redirectErrorStream();
			String comm = "";
       	 	OutputStream bw = process.getOutputStream();
       	 	if(state){
       	 		comm = "pm enable " + packageName + ";";
       	 		Log.i(tag,"enabling "+packageName);
       	 	}else{
       	 		comm = "pm disable " + packageName + ";";
       	 		Log.i(tag,"disabling " + packageName);
       	 	}
       	 	bw.write(comm.getBytes());
       	 	bw.close();
       	 	int exit = process.waitFor();
       	 	Log.i(tag,"Exit status: " + exit);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
}
