package com.leon.jinanbus.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.leon.jinanbus.domain.CollectItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 收藏列表数据库存储
 */
public class CollectDao {
    private static final String TABLE_NAME = "collect";
    private static CollectDao mCollectDao;
    private SaveHelper mHelper;

    private CollectDao(Context context) {
        mHelper = new SaveHelper(context);
    }

    public static CollectDao getInstance(Context context) {
        if (mCollectDao == null) {
            synchronized (CollectDao.class) {
                if (mCollectDao == null) {
                    mCollectDao = new CollectDao(context);
                }
            }
        }

        return mCollectDao;
    }

    public void add(String id, String busNum, String orientation) {
        ContentValues values = new ContentValues();
        values.put("busLineId", id);
        values.put("busNum", busNum);
        values.put("orientation", orientation);

        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void delete(String id) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.delete(TABLE_NAME, "busLineId = ?", new String[]{id});
        db.close();
    }

    public boolean query(String id) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{"busLineId"}, "busLineId = ?", new String[]{id},
                null, null, null, null);

        boolean exist = cursor.moveToFirst();

        db.close();
        cursor.close();

        return exist;
    }

    public List<CollectItem> queryAll() {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{"busLineId", "busNum", "orientation"},
                null, null, null, null, null);
        List<CollectItem> list = new ArrayList<>();

        while (cursor.moveToNext()) {
            CollectItem item = new CollectItem();
            item.id = cursor.getString(cursor.getColumnIndex("busLineId"));
            item.busNum = cursor.getString(cursor.getColumnIndex("busNum"));
            item.orientation = cursor.getString(cursor.getColumnIndex("orientation"));

            list.add(item);
        }

        db.close();
        cursor.close();

        return list;
    }
}