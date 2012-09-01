package com.kalgecin.systweak;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;

public class DummySectionFragment extends Fragment {
	static List<String> allPackages = new ArrayList<String>();
	static List<Boolean> allEnabled = new ArrayList<Boolean>();
	static List<String> allNames = new ArrayList<String>();
	static List<Switch> allSwitches = new ArrayList<Switch>();
	Context context;
	public Activity activity;
	private SwitchManager swm;
    public static final String ARG_SECTION_NUMBER = "section_number";
    
    public DummySectionFragment() {
    }
    public void setUP(Context c,Activity act){
    	context = c;
    	activity = act;
    	swm = new SwitchManager(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	Bundle args = getArguments();
    	View rlMain = null;
    	Log.i("Creating",Integer.toString(args.getInt(ARG_SECTION_NUMBER)));
    	switch (args.getInt(ARG_SECTION_NUMBER)){
    		case 1: 
    			rlMain = inflater.inflate(R.layout.activity_main, container,false);
    			Button btnSetA 		= (Button) rlMain.findViewById(R.id.btnSetOnBoot);
    	   	 	btnSetA.setOnClickListener(new View.OnClickListener() {
    				@Override
    				public void onClick(final View v) {
    					SetChecks(v);	
    				}
    			});
    			setupMain(rlMain);
    			loadChecks(rlMain);
    			return rlMain;
    		case 2: 
    			rlMain = inflater.inflate(R.layout.all, container,false); 
    			
    			LinearLayout rlAll = (LinearLayout) rlMain.findViewById(R.id.rlAll);
    			//ScrollView rlSV = (ScrollView) rlMain.findViewById(R.id.rlAllSV);
    			//((ViewGroup)rlSV.getParent()).removeView(rlSV);
    			allPackages = getAllPackages();
    			for(int i=0;i<allPackages.size();i++){
    				if(allSwitches.size()>i){
    					allSwitches.set(i,new Switch(getActivity()));
    				}else{
    					allSwitches.add(new Switch(getActivity()));
    				}
					allSwitches.get(i).setText(allNames.get(i));
					allSwitches.get(i).setChecked(allEnabled.get(i));
					rlAll.addView(allSwitches.get(i));
					
    			}
    			Button btnSet = (Button) rlMain.findViewById(R.id.btnAllApply);
    			btnSet.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						allPackages = getAllPackages();
						int i=0;
						for(Switch swCur : allSwitches){
							if(swCur.isChecked() && !allEnabled.get(i)){
								swm.toggleState(allPackages.get(i), true);
							}else if(!swCur.isChecked() && allEnabled.get(i)){
								swm.toggleState(allPackages.get(i), false);
							}
							i++;
						}
					}
				});
    			return rlMain;
    	}
        return rlMain;
    }
    public void SetChecks(View v){
   	 String fTag = "sysTweak_setChecks";
   	 String[] checks = MainActivity.checks,CHKnames = MainActivity.CHKnames;
   	 Switch[] CBchecks = MainActivity.CBchecks;
     settingsDB dataSrc = new settingsDB(context);
     dataSrc.open();
   	 for(int i=0;i<checks.length;i++){
   		 if(CBchecks[i].isChecked()){
   			 if(!swm.checkState(CHKnames[i])){
   				 if(CBchecks[i].isEnabled()){
	       			 Log.i(fTag,"enabling "+CHKnames[i]);
	       			 swm.toggleState(CHKnames[i], true);
   				 }else{
   					Log.i(fTag,CHKnames[i]+"is not installed");
   				 }
   			 }else{
   				 Log.i(fTag,CHKnames[i]+" is already enabled");
   				 continue;
   			 }
   		 }else{
   			 if(swm.checkState(CHKnames[i])){
   				if(!CBchecks[i].isEnabled()){
	       			 Log.i(fTag,"disabling "+CHKnames[i]);
	       			 swm.toggleState(CHKnames[i], false);
   				}else{
   					Log.i(fTag,CHKnames[i]+"is not installed");
   				}
   			 }else{
   				 Log.i(fTag,CHKnames[i]+" is already disabled");
   				 continue;
   			 }
   		 }
   	 }
   	 
        //Save current state of checks to DB
        for(int i=0;i<checks.length;i++){
        	dataSrc.addSetting(checks[i], Boolean.toString(CBchecks[i].isChecked()));
        }
        dataSrc.close();
   }
    public void setupMain(View vi){
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
    public void loadChecks(View v){
    	String[] checks = MainActivity.checks,CHKnames = MainActivity.CHKnames;
    	Switch[] CBchecks = MainActivity.CBchecks;
    	int[] CBchecksID = MainActivity.CBchecksID;
    	Boolean[] CBStatuses = MainActivity.CBStatuses;
    	settingsDB dataSrc = new settingsDB(context);
        dataSrc.open();
        
    	for(int i=0;i<checks.length;i++){
    		CBchecks[i] = (Switch) v.findViewById(CBchecksID[i]);
         }
    	String tag = "sysTweaks_loadChecks";
    	boolean[] btr = new boolean[CHKnames.length];
    	SwitchManager swm = new SwitchManager(context);
    	for(int i=0;i<CHKnames.length;i++){
    		btr[i]=swm.checkExists(CHKnames[i]);
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
		dataSrc.close();
    }
    public List<String> getAllPackages(){
    	List<String> out = new ArrayList<String>();
    	final PackageManager pm = context.getPackageManager();
    	List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
    	allNames.clear();
    	allEnabled.clear();
    	for(ApplicationInfo packageInfo : packages){
    		out.add(packageInfo.packageName);
    		allNames.add(pm.getApplicationLabel(packageInfo).toString());
    		allEnabled.add(packageInfo.enabled);
    	}
    	return out;
    }
}