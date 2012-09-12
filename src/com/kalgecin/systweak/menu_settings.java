package com.kalgecin.systweak;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

//TODO: needs permission to get the on boot signal
public class menu_settings extends MainActivity{
	private settingsDB dataSrc;
	private CheckBox chkOnBoot;
	private CheckBox chkShowAdv;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		
		dataSrc = new settingsDB(this);
		dataSrc.open();
		
		chkOnBoot = (CheckBox) findViewById(R.id.chkSettings_onboot);
		chkShowAdv = (CheckBox) findViewById(R.id.chkShowAdv);
		Button btnSet = (Button) findViewById(R.id.btnSettingsSave);
		loadSettings();
		btnSet.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				saveSettings();
			}
		});
	}
	private void saveSettings(){
		dataSrc.addSetting("on_boot", Boolean.toString(chkOnBoot.isChecked()));
		dataSrc.addSetting("showadv", Boolean.toString(chkShowAdv.isChecked()));
	}
	private void loadSettings(){
		String onB = dataSrc.getSetting("on_boot");
		boolean onboot = Boolean.parseBoolean(onB);
		chkOnBoot.setChecked(onboot);
		chkShowAdv.setChecked(Boolean.parseBoolean(dataSrc.getSetting("showadv")));
	}
}
