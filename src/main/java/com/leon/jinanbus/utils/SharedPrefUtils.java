package com.leon.jinanbus.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreference工具类
 * <p/>
 * Modify for 2016.5.28 by Leon
 */
public class SharedPrefUtils {
    private static SharedPreferences getPref(Context context) {
        return context.getSharedPreferences("setting", Context.MODE_PRIVATE);
    }

    public static boolean getBoolean(Context context, String key, boolean defValue) {
        return getPref(context).getBoolean(key, defValue);
    }


    public static void putBoolean(Context context, String key, boolean value) {
        getPref(context).edit().putBoolean(key, value).apply();
    }
}