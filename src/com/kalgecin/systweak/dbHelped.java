package com.kalgecin.systweak;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class dbHelped extends SQLiteOpenHelper{
	public static String dbName = "settings.db";
	public static String dbSettingsName = "settings";
	public static String dbColSetting = "setting";
	public static String dbColValue = "value";
	public static String dbIdCol = "_id";
	public static int dbVersion = 1;
	public dbHelped(Context context) {
		super(context, dbName, null, dbVersion);
	}
	@Override
	public void onCreate(SQLiteDatabase database){
		database.execSQL("create table "+dbSettingsName+" ("+dbIdCol+" integer primary key autoincrement, "+dbColSetting+" text not null, "+dbColValue+" text not null);");
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		Log.w(dbHelped.class.getName(), "upgrading from "+oldVersion+" to "+newVersion);
		db.execSQL("DROP TABLE IF EXIST "+dbSettingsName);
		onCreate(db);
	}
}
