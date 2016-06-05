package com.leon.jinanbus.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SaveHelper extends SQLiteOpenHelper {
    public SaveHelper(Context context) {
        super(context, "collect.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE collect (_id INTEGER PRIMARY KEY AUTOINCREMENT, busLineId VARCHAR, busNum TEXT, orientation TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
