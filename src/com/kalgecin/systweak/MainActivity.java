package com.kalgecin.systweak;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

public class MainActivity extends Activity {
	private settingsDB dataSrc;
	ProgressDialog progressBar;
	private int progressBarStatus = 0;
	private static int cnt;
	private static boolean bnt;
	private static String str="";
	private Handler progressBarHandler = new Handler();
	
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
	Boolean[] CBStatuses = {false,false,false,false,false,true,false,true,true,
							false,false,false,true,true,true,true};
	int[] CBchecksID = {R.id.swRomManager,R.id.swLiveWallpapers,R.id.swCMWallpapers,R.id.swGTTS,R.id.swMovieStudio,
						R.id.swGmail,R.id.swTvOut,R.id.swPhone,R.id.swApollo,R.id.swDSPManager,R.id.swEmail,R.id.swNewsAndWeather,
						R.id.swGTalk,R.id.swTerminalEmulator,R.id.swTorch,R.id.swMediaScanner};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnSet 		= (Button) findViewById(R.id.btnSetOnBoot);
        
        
        dataSrc = new settingsDB(this);
        dataSrc.open();
        progressBar = new ProgressDialog(this);
    	progressBar.setCancelable(false);
    	progressBar.setTitle("Initializing....");
    	progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    	progressBar.setProgress(0);
    	progressBar.setMax(checks.length);
    	progressBar.show();
    	progressBarStatus = 0;
    	new Thread(new Runnable() {
			@Override
			public void run() {
				loadChecks();
			}
		}).start();
		new Thread(new Runnable() {
		    		
			@Override
			public void run() {
				Log.i("sysTweaks_loadChecks","Entering thread");
				while(progressBarStatus < checks.length){
					Log.i("sysTweaks_loadChecks",progressBarStatus+":"+checks.length+":"+progressBar.getProgress());
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					//progressBar.setProgress(progressBarStatus);
					progressBarHandler.post(new Runnable() {
						@Override
						public void run() {
							progressBar.setProgress(progressBarStatus);
							progressBar.setMessage(str);
						}
					});
				}
				if(progressBarStatus>=checks.length){
					progressBar.dismiss();
					progressBarStatus=0;
				}
			}
		}).start();
        btnSet.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				progressBar.setProgress(0);
				progressBar.setTitle("Setting....");
				progressBar.show();
				new Thread(new Runnable() {
					@Override
					public void run() {
						Log.i("sysTweaks_setChecks","Entering thread");
						while(progressBarStatus < checks.length){
							Log.i("sysTweaks_setChecks",progressBarStatus+":"+checks.length+":"+progressBar.getProgress());
							try {
								Thread.sleep(200);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							//progressBar.setProgress(progressBarStatus);
							progressBarHandler.post(new Runnable() {
								@Override
								public void run() {
									progressBar.setProgress(progressBarStatus);
									progressBar.setMessage(str);
								}
							});
						}
						if(progressBarStatus>=checks.length){
							progressBar.dismiss();
							progressBarStatus=0;
						}
					}
				}).start();
				new Thread(new Runnable() {
					@Override
					public void run() {
						SetChecks();
					}
				}).start();
				
			}
		});
    }
    public static boolean check_exists(String package_name){
    	String fTag = "sysTweak_checkExists";
    	if(package_name.equalsIgnoreCase(CHKnames[15])){
    		Log.i(fTag,"skipping media scanner");
    		return true;
    	}
    	Log.i(fTag,"Entered");
    	Process process;
    	try{
    		process = Runtime.getRuntime().exec("pm list packages "+package_name);
    		BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
    		String result = br.readLine();
    		br.close();
    		int res = process.waitFor();
    		Log.i(fTag,"Exit status: "+res);
    		if(result != null && result.contains(package_name)){
    			Log.i(fTag,package_name+" is installed");
    			return true;
    		}else{
    			Log.i(fTag,package_name+" is not installed");
    			return false;
    		}
    	}catch(IOException e){
    		e.printStackTrace();
    	} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	return false;
    }
    public static boolean check_status(String package_name){
    	Log.i("sysTweak_check_status","Entered");
    	Process process;
    	try {
			process =Runtime.getRuntime().exec("su");
			OutputStream bw = process.getOutputStream();
			bw.write(new String("pm list packages -e "+package_name).getBytes());
			bw.close();
			String result = "";
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			Log.i("sysTweak_check_status","reading");
			result=br.readLine();
			Log.i("sysTweak_check_status","res: "+result);
			br.close();
			bw.close();
			int res = process.waitFor();
			Log.i("sysTweak_check_status","Exit status: "+res);
			if(result != null && result.contains(package_name)){
				Log.i("sysTweak_check_status",package_name+" is enabled");
				return true;
			}else{
				Log.i("sysTweak_check_status",package_name+" is disabled");
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	Log.i("sysTweak_check_status","Exited");
    	return false;
    }
    public void loadChecks(){
    	for(int i=0;i<checks.length;i++){
        	CBchecks[i] = (Switch) findViewById(CBchecksID[i]);
        }
    	String tag = "sysTweaks_loadChecks";
		for(int i=0;i<checks.length;i++){
			progressBarStatus = i+1;
			
    		Boolean b;
    		if(check_exists(CHKnames[i])){
    			CBchecks[i].setEnabled(true);
    			Log.i(tag,"enabled "+checks[i]);
    			if(dataSrc.getSetting(checks[i]).equalsIgnoreCase("true")){
        			b=true;
        			Log.i(tag,"on");
        		}else{
        			b=false;
        			Log.i(tag,"off");
        		}
        		Log.i(tag, checks[i]+","+i+","+Boolean.toString(b));
        		cnt=i;
        		bnt=b;
        		runOnUiThread(new Runnable() {
					@Override
					public void run() {
						CBchecks[cnt].setChecked(bnt);
						str=CBchecks[cnt].getText().toString();
					}
				});
        		//CBchecks[i].setChecked(b);
        		CBStatuses[i]=b;
    		}else{
    			CBchecks[i].setEnabled(false);
    			Log.i(tag,"disabled "+checks[i]);
    		}
    		
    	}
    }
    public void SetChecks(){
    	 String fTag = "sysTweak_setChecks";
         ProcessBuilder cmd;
         //@SuppressWarnings("unused")
    	Process process;
		 try {
			//process = Runtime.getRuntime().exec("su");
			 cmd = new ProcessBuilder("su");
			process = cmd.start();
			cmd.redirectErrorStream();
	         //Enable or disable services/apps
        	 String[] args = {"su","-c","pm","enable",""};
        	 String comm = "";
        	 OutputStream bw = process.getOutputStream();
        	 BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
        	 for(int i=0;i<checks.length;i++){
        		 progressBarStatus = i+1;
        		 args[4] = CHKnames[i];
        		 cnt=i;
        		 runOnUiThread(new Runnable() {
					@Override
					public void run() {
						str=CBchecks[cnt].getText().toString();
					}
				});
        		 if(CBchecks[i].isChecked()){
        			 if(!check_status(args[4])){
        				 comm = "pm enable "+args[4]+";";
            			 Log.i(fTag,"enabling "+args[4]);
        			 }else{
        				 Log.i(fTag,args[4]+" is already enabled");
        			 }
        		 }else{
        			 if(check_status(args[4])){
	        			 comm = "pm disable "+args[4]+";";
	        			 Log.i(fTag,"disabling "+args[4]);
        			 }else{
        				 Log.i(fTag,args[4]+" is already disabled");
        			 }
        		 }
        		 Log.i(fTag,comm);
        		 //bw = process.getOutputStream();
        		 bw.write(comm.getBytes());
        		 //bw.close();
        		 //Log.i("Toggle_service","res: "+br.readLine());
        	 }
        	 bw.close();
        	 br.close();
        	 process.waitFor();
         }catch(IOException e){
        	 e.printStackTrace();
         } catch (InterruptedException e) {
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
