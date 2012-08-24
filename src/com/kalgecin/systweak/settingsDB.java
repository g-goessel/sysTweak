package com.kalgecin.systweak;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class settingsDB {
	private SQLiteDatabase database;
	private dbHelped dbHelper;
	private String[] collumns = {dbHelped.dbIdCol,dbHelped.dbColSetting,dbHelped.dbColValue};
	public settingsDB(Context context){
		dbHelper = new dbHelped(context);
	}
	
	public void open() throws SQLException{
		database = dbHelper.getWritableDatabase();
	}
	
	public void close() {
		dbHelper.close();
	}
	
	public void addSetting(String name, String value){
		ContentValues cv = new ContentValues();
		cv.put(dbHelped.dbColSetting, name);
		cv.put(dbHelped.dbColValue, value);
		//TODO: check if setting exists to update it instead
		database.insert(dbHelped.dbSettingsName, null, cv);
	}
	public void editSetting(String name, String value){
		ContentValues cv = new ContentValues();
		cv.put(dbHelped.dbColValue, value);
		//TODO: if name doesn't exist add it
		database.update(dbHelped.dbSettingsName, cv, dbHelped.dbColSetting+"+"+name,null);
	}
	public String getSetting(String name){
		Cursor cursor = database.query(dbHelped.dbSettingsName, collumns, null, null, null, null, null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast() && cursor.getString(1) != name){
			return cursor.getString(2);
		}
		return "ST_FAIL";
	}
}
