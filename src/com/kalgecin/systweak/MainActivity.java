package com.kalgecin.systweak;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;

public class MainActivity extends FragmentActivity {
    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
    public static View rlMain = null;
	private static settingsDB dataSrc;
	
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
			
       
        
        dataSrc = new settingsDB(this);
        dataSrc.open();
    	loadChecks();
    }
    public void loadChecks(){
    	for(int i=0;i<checks.length;i++){
    		CBchecks[i] = (Switch) activity.findViewById(CBchecksID[i]);
         }
    	String tag = "sysTweaks_loadChecks";
    	boolean[] btr = new boolean[CHKnames.length];
    	SwitchManager swm = new SwitchManager(getApplicationContext());
    	for(int i=0;i<CHKnames.length;i++){
    		btr[i]=swm.checkState(CHKnames[i]);
    	}
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
            Fragment fragment = new DummySectionFragment();
            ((DummySectionFragment) fragment).setUP(getApplicationContext(),activity);
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
