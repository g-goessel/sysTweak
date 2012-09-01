package com.kalgecin.systweak;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {
    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
    public static View rlMain = null;
	private static settingsDB dataSrc;
	private static int cnt;
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
	
	public static Switch[] CBchecks = {swRomManager,swLiveWallpapers,swCMWallpapers,swGTTS,swMovie,swGmail,swTvOut,swPhone,swApollo,
						swDSPManager,swEmail,swNewsAndWeather,swGTalk,swTerminalEmulator,swTorch,swMediaScanner};
	static Boolean[] CBStatuses = {false,false,false,false,false,true,false,true,true,
							false,false,false,true,true,true,true};
	static int[] CBchecksID = {R.id.swRomManager,R.id.swLiveWallpapers,R.id.swCMWallpapers,R.id.swGTTS,R.id.swMovieStudio,
						R.id.swGmail,R.id.swTvOut,R.id.swPhone,R.id.swApollo,R.id.swDSPManager,R.id.swEmail,R.id.swNewsAndWeather,
						R.id.swGTalk,R.id.swTerminalEmulator,R.id.swTorch,R.id.swMediaScanner};
	static List<String> allPackages = new ArrayList<String>();
	static List<Boolean> allEnabled = new ArrayList<Boolean>();
	static List<String> allNames = new ArrayList<String>();
	static List<Switch> allSwitches = new ArrayList<Switch>();
    public static Activity activity;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.main);
        activity=this;
        
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),activity);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
			
       
        Button btnSet 		= (Button) activity.findViewById(R.id.btnSetOnBoot);
   	 	btnSet.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				SetChecks(v);	
			}
		});
        dataSrc = new settingsDB(this);
        dataSrc.open();
    	loadChecks();
    }
    public static void setupMain(Activity v,View vi){
    	AlertDialog.Builder builder = new AlertDialog.Builder(vi.getContext());
        builder.setMessage(R.string.hello_world)
               .setCancelable(false)
               .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
        Log.i("sysTweak_setupMain","Showing alert");
    	AlertDialog alert = builder.create();
    	alert.show();
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
    		CBchecks[i] = (Switch) activity.findViewById(CBchecksID[i]);
         }
    	String tag = "sysTweaks_loadChecks";
    	boolean[] btr = check_exists(CHKnames);
    	Log.i(tag,"Entered");
		for(int i=0;i<checks.length;i++){
			
    		Boolean b;
    		Log.i(tag,"btr["+i+"] = "+Boolean.toString(btr[i]));
    		if(btr[i]){
    			CBchecks[i].setEnabled(true);
    			Log.i(tag,"enabled "+checks[i]+" ->"+CBchecks[i].isEnabled());
    			if(dataSrc.getSetting(checks[i]).equalsIgnoreCase("true")){
        			b=true;
        			Log.i(tag,"on");
        		}else{
        			b=false;
        			Log.i(tag,"off");
        		}
        		Log.i(tag, checks[i]+","+i+","+Boolean.toString(b));
        		
        		CBchecks[i].setChecked(b);
        		CBStatuses[i]=b;
    		}else{
				CBchecks[i].setEnabled(false);
    			Log.i(tag,"disabled "+checks[i]+" ->"+CBchecks[i].isEnabled());
    		}
    		
    	}

		for(int i=0;i<checks.length;i++){
			CBchecks[i].setChecked(CBStatuses[i]);
		}

    }
    public static void SetChecks(View v){
    	 String fTag = "sysTweak_setChecks";
        
         //Enable or disable services/apps
    	 String[] args = {"su","-c","pm","enable",""};
    	 for(int i=0;i<checks.length;i++){
    		 args[4] = CHKnames[i];
    		 cnt=i;
    		 if(CBchecks[i].isChecked()){
    			 if(!check_status(new String[] {args[4]})[0]){
        			 Log.i(fTag,"enabling "+args[4]);
        			 SwitchManager.toggleState(args[4], true);
    			 }else{
    				 Log.i(fTag,args[4]+" is already enabled");
    				 continue;
    			 }
    		 }else{
    			 if(check_status(new String[] {args[4]})[0]){
        			 Log.i(fTag,"disabling "+args[4]);
        			 SwitchManager.toggleState(args[4], false);
    			 }else{
    				 Log.i(fTag,args[4]+" is already disabled");
    				 continue;
    			 }
    		 }
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
    	public Activity activity;
        public SectionsPagerAdapter(FragmentManager fm,Activity act) {
            super(fm);
            activity = act;
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = new DummySectionFragment(getApplicationContext(),activity);
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
   
}
