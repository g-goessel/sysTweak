package com.kalgecin.systweak;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

//TODO: needs permission to get the on boot signal
public class menu_settings extends MainActivity{
	private settingsDB dataSrc;
	private CheckBox chkOnBoot;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		
		dataSrc = new settingsDB(this);
		dataSrc.open();
		
		chkOnBoot = (CheckBox) findViewById(R.id.chkSettings_onboot);
		Button btnSet = (Button) findViewById(R.id.btnSettingsSave);
		loadSettings();
		btnSet.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				saveSettings();
			}
		});
	}
	private void saveSettings(){
		dataSrc.addSetting("on_boot", Boolean.toString(chkOnBoot.isChecked()));
	}
	private void loadSettings(){
		String onB = dataSrc.getSetting("on_boot");
		boolean onboot = Boolean.parseBoolean(onB);
		chkOnBoot.setChecked(onboot);
	}
}
