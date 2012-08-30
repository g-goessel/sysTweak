package com.kalgecin.systweak;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import android.util.Log;

public class SwitchManager {
	public static void toggleState(String packageName, boolean state){
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
       	 	BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
       	 	if(state){
       	 		comm = "pm enable " + packageName + ";";
       	 		Log.i(tag,"enabling "+packageName);
       	 	}else{
       	 		comm = "pm disable " + packageName + ";";
       	 		Log.i(tag,"disabling " + packageName);
       	 	}
       	 	bw.write(comm.getBytes());
       	 	bw.close();
       	 	br.close();
       	 	int exit = process.waitFor();
       	 	Log.i(tag,"Exit status: " + exit);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
}
