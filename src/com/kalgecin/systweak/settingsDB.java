package com.kalgecin.systweak;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class settingsDB {
	private SQLiteDatabase database = null;
	private dbHelped dbHelper = null;
	private String[] collumns = {dbHelped.dbIdCol,dbHelped.dbColSetting,dbHelped.dbColValue};
	public settingsDB(Context context){
		if(context != null)
			dbHelper = new dbHelped(context);
		else{
			Log.i("sysTweak_settingsDB","null context");
			dbHelper = new dbHelped(MainActivity.activity);
		}
	}
	
	public void open() throws SQLException{
		try{
			database = dbHelper.getWritableDatabase();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	public void close() {
		dbHelper.close();
	}
	public Boolean checkSetting(String name){
		//TODO: very dirty fix
		if(database == null)
			return false;
		Cursor c = database.rawQuery("select 1 from "+dbHelped.dbSettingsName+" where "+dbHelped.dbColSetting+"=?", new String[] {name});
		Boolean exists = (c.getCount() > 0);
		c.close();
		return exists;
	}
	public void addSetting(String name, String value){
		ContentValues cv = new ContentValues();
		cv.put(dbHelped.dbColSetting, name);
		cv.put(dbHelped.dbColValue, value);
		if(!checkSetting(name)){
			database.insert(dbHelped.dbSettingsName, null, cv);
		}else{
			editSetting(name, value);
		}
	}
	
	public void editSetting(String name, String value){
		ContentValues cv = new ContentValues();
		cv.put(dbHelped.dbColValue, value);
		if(!checkSetting(name)){
			addSetting(name, value);
		}else{
			database.update(dbHelped.dbSettingsName, cv, dbHelped.dbColSetting+"=?",new String[] {name});
		}
	}
	public String getSetting(String name){
		if(checkSetting(name)){
			Cursor cursor = database.query(dbHelped.dbSettingsName, collumns, dbHelped.dbColSetting+"=?", new String[] {name}, null, null, null);
			cursor.moveToFirst();
			String value =cursor.getString(cursor.getColumnIndex(dbHelped.dbColValue));
			cursor.close();
			Log.i("getSetting", "name = "+name+", value='"+value+"'");
			return value;
		}
		return "false";
	}
}
