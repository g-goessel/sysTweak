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
	public SwitchManager(Context c) throws NullPointerException{
		if(c == null){
			context = MainActivity.activity;
			Log.i("sysTweak_SwitchManager","null context on constructor");
			//throw new NullPointerException("NullContext");
		}else{
			context = c;
		}
		
		pm = context.getPackageManager();
	}
	/**
	 * Check to see if packageName exists
	 * @param packageName name of package to check
	 * @return true if the package exists false if otherwise
	 */
	public boolean checkExists(String packageName){
		if(packageName.contains("/")){
			packageName = packageName.substring(0, packageName.indexOf("/"));
		}
		Log.i("sysTweak_SMcheckExists",packageName);
		List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
		for(ApplicationInfo packageInfo : packages){
			if(packageInfo.packageName.equalsIgnoreCase(packageName)){
				return true;
			}
		}
		return false;
	}
	/**
	 * Check if packageName is enabled on system 
	 * @param packageName package name to be checked
	 * @return true if packageName is enabled, false otherwise
	 */
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
	/**
	 * get all packages installed on system
	 * @return list of all packages
	 */
	public List<ApplicationInfo> getAllPackages(){
		pm = context.getPackageManager();
		
		return pm.getInstalledApplications(PackageManager.GET_META_DATA);
		
	}
	/**
	 * sync the status of a switch between COMMON and ALL sections
	 * @param name	packageName of the switch
	 * @param state true for on, false for off
	 */
	public void syncSwitch(String name,boolean state){
		String tag = "sysTweak_SMsyncSwitch";
		int a = DummySectionFragment.allPackages.lastIndexOf(name);
		Log.i(tag,"name: "+name+" state: "+state+" a: "+a);
		if(a>-1)
			DummySectionFragment.allSwitches.get(a).setChecked(state);
		
		for(int i=0;i<MainActivity.CHKnames.length;i++){
			if(MainActivity.CHKnames[i].contains(name)){
				MainActivity.CBchecks[i].setChecked(state);
				Log.i(tag,"name: "+MainActivity.CHKnames[i]+" state: "+state+" i: "+i);
				settingsDB datasrc = new settingsDB(context);
				datasrc.open();
				datasrc.addSetting(name, Boolean.toString(state));
				datasrc.close();
			}
		}
	}
	/**
	 * toggle the state of the switch
	 * @param packageName name of the package to toggle
	 * @param state true for enabled, false otherwise
	 */
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
