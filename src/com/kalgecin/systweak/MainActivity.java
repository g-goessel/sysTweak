package com.kalgecin.systweak;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import android.os.Bundle;
import android.os.Handler;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;

public class MainActivity extends FragmentActivity {
    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;

	private static settingsDB dataSrc;
	static ProgressDialog progressBar;
	private static int progressBarStatus = 0;
	private static int cnt;
	private static boolean bnt;
	private static String str="";
	private static Handler progressBarHandler = new Handler();
	
	static Switch swRomManager,swLiveWallpapers,swCMWallpapers,swGTTS,swMovie,swGmail,swTvOut,swPhone,swApollo,swDSPManager;
	static Switch swEmail,swNewsAndWeather,swGTalk,swTerminalEmulator,swTorch,swMediaScanner;
	
	public static String[] checks = {"swRomManager","swLiveWallpapers","swCMWallpapers","swGTTS","swMovie","swGmail","swTvOut","swPhone",
						"swApollo","swDSPManager","swEmail","swNewsAndWeather","swGTalk","swTerminalEmulator","swTorch",
						"swMediaScanner"};
	
	public static String[] CHKnames = {"com.koushikdutta.rommanager","com.android.wallpaper.livepicker","com.cyanogenmod.CMWallpapers",
						"com.google.android.tts","com.android.videoeditor","com.google.android.gm","com.teamhacksung.tvout",
						"com.android.phone","com.andrew.apollo","com.bel.android.dspmanager","com.android.email",
						"com.google.android.apps.genie.geniewidget","com.google.android.talk","jackpal.androidterm",
						"net.cactii.flash2","com.android.providers.media/com.android.providers.media.MediaScannerReceiver"};
	
	static Switch[] CBchecks = {swRomManager,swLiveWallpapers,swCMWallpapers,swGTTS,swMovie,swGmail,swTvOut,swPhone,swApollo,
						swDSPManager,swEmail,swNewsAndWeather,swGTalk,swTerminalEmulator,swTorch,swMediaScanner};
	Boolean[] CBStatuses = {false,false,false,false,false,true,false,true,true,
							false,false,false,true,true,true,true};
	int[] CBchecksID = {R.id.swRomManager,R.id.swLiveWallpapers,R.id.swCMWallpapers,R.id.swGTTS,R.id.swMovieStudio,
						R.id.swGmail,R.id.swTvOut,R.id.swPhone,R.id.swApollo,R.id.swDSPManager,R.id.swEmail,R.id.swNewsAndWeather,
						R.id.swGTalk,R.id.swTerminalEmulator,R.id.swTorch,R.id.swMediaScanner};
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.main);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());


        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        
       
        
        
        dataSrc = new settingsDB(this);
        dataSrc.open();
        progressBar = new ProgressDialog(this);
    	progressBar.setCancelable(false);
    	progressBar.setTitle("Initializing....");
    	progressBar.setMessage("");
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
					//Log.i("sysTweaks_loadChecks",progressBarStatus+":"+checks.length+":"+progressBar.getProgress());
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
							//Log.i("sysTweaks_loadChecks",str);
						}
					});
				}
				if(progressBarStatus>=checks.length){
					progressBar.dismiss();
					progressBarStatus=0;
				}
			}
		}).start();
       
    }
    public static void setupMain(View v){
    	 Button btnSet 		= (Button) v.findViewById(R.id.btnSetOnBoot);
    	 btnSet.setOnClickListener(new View.OnClickListener() {
 			@Override
 			public void onClick(View v) {
 				progressBar.setProgress(0);
 				progressBar.setTitle("Setting....");
 				progressBar.setMessage("");
 				progressBar.show();
 				new Thread(new Runnable() {
 					@Override
 					public void run() {
 						Log.i("sysTweaks_setChecks","Entering thread");
 						while(progressBarStatus < checks.length){
 							//Log.i("sysTweaks_setChecks",progressBarStatus+":"+checks.length+":"+progressBar.getProgress());
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
    public static boolean[] check_exists(String[] package_name){
    	String fTag = "sysTweak_checkExists";
    	boolean[] btr = new boolean[package_name.length];
    	Log.i(fTag,"Entered");
    	Process process;
    	try {
			process = Runtime.getRuntime().exec("pm list packages");
		
	    	BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
	    	String brstr = "";
	    	while((brstr = br.readLine())!=null){
	    		for(int i=0;i<package_name.length;i++){
	    			if(brstr.contains(package_name[i])){
	    				btr[i]=true;
	    			}
	    		}
	    	}
	    	for(int i=0;i<package_name.length;i++){
	    		if(btr[i]!=true){
	    			btr[i]=false;
	    		}
	    	}
	    	btr[15]=true; //Media Scanner
    	} catch (IOException e) {
			e.printStackTrace();
		}
    	return btr;
    }
    public static boolean[] check_status(String[] package_name){
    	Log.i("sysTweak_check_status","Entered");
    	boolean[] btr = new boolean[package_name.length];
    	
    	Process process;
    	try {
			process =Runtime.getRuntime().exec("pm list packages -e");
			String result = "";
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			Log.i("sysTweak_check_status","reading");
			while((result=br.readLine())!=null){
				for(int i=0;i<package_name.length;i++){
					if(result.contains(package_name[i])){
						btr[i]=true;
					}
				}
			}
			for(int i=0;i<package_name.length;i++){
				if(btr[i]!=true){
					btr[i]=false;
				}
			}
			br.close();
			int res = process.waitFor();
			Log.i("sysTweak_check_status","Exit status: "+res);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	Log.i("sysTweak_check_status","Exited");
    	return btr;
    }
    public void loadChecks(){
    	for(int i=0;i<checks.length;i++){
        	CBchecks[i] = (Switch) findViewById(CBchecksID[i]);
        }
    	String tag = "sysTweaks_loadChecks";
    	boolean[] btr = check_exists(CHKnames);
    	
		for(int i=0;i<checks.length;i++){
			progressBarStatus = i+1;
			
    		Boolean b;
    		if(btr[i]){
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
    public static void SetChecks(){
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
        		 /*runOnUiThread(new Runnable() {
					@Override
					public void run() {
						str=CBchecks[cnt].getText().toString();
					}
				});*/
        		 str=CBchecks[cnt].getText().toString();
        		 if(CBchecks[i].isChecked()){
        			 if(!check_status(new String[] {args[4]})[0]){
        				 comm = "pm enable "+args[4]+";";
            			 Log.i(fTag,"enabling "+args[4]);
        			 }else{
        				 Log.i(fTag,args[4]+" is already enabled");
        				 continue;
        			 }
        		 }else{
        			 if(check_status(new String[] {args[4]})[0]){
	        			 comm = "pm disable "+args[4]+";";
	        			 Log.i(fTag,"disabling "+args[4]);
        			 }else{
        				 Log.i(fTag,args[4]+" is already disabled");
        				 continue;
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
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = new DummySectionFragment();
            Bundle args = new Bundle();
            args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, i + 1);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0: return getString(R.string.title_section1).toUpperCase();
                case 1: return getString(R.string.title_section2).toUpperCase();
            }
            return null;
        }
    }
    public static class DummySectionFragment extends Fragment {
        public DummySectionFragment() {
        }

        public static final String ARG_SECTION_NUMBER = "section_number";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
        	Bundle args = getArguments();
        	View rlMain = null;
        	Log.i("Creating",Integer.toString(args.getInt(ARG_SECTION_NUMBER)));
        	switch (args.getInt(ARG_SECTION_NUMBER)){
        		case 1: 
        			rlMain = inflater.inflate(R.layout.activity_main, container,false);
        			MainActivity.setupMain(rlMain);
        			return rlMain;
        		case 2: return inflater.inflate(R.layout.all, container,false);
        	}
            return rlMain;
        }
    }
}
