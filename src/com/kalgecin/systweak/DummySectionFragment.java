package com.kalgecin.systweak;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
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
    public DummySectionFragment(Context c,Activity act) {
    	context = c;
    	activity = act;
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
    			MainActivity.setupMain(activity,rlMain);
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
								SwitchManager.toggleState(allPackages.get(i), true);
							}else if(!swCur.isChecked() && allEnabled.get(i)){
								SwitchManager.toggleState(allPackages.get(i), false);
							}
							i++;
						}
					}
				});
    			return rlMain;
    	}
        return rlMain;
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