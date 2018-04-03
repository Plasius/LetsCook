package com.plasius.letscook.utils;

import android.content.Context;

public class PersistenceUtils {
    private static final String SAVE_NAME = "com.plasius.letscook.saves";

    //bool
    public static void setSharedPrefBool(Context context, String key, boolean value){
        context.getSharedPreferences(SAVE_NAME, Context.MODE_PRIVATE).edit().putBoolean(key, value).commit();
    }

    public static boolean getSharedPrefBool(Context context, String key, boolean defValue){
        return context.getSharedPreferences(SAVE_NAME, Context.MODE_PRIVATE).getBoolean(key, defValue);
    }

    //string
    public static void setSharedPrefString(Context context, String key, String value){
        context.getSharedPreferences(SAVE_NAME, Context.MODE_PRIVATE).edit().putString(key, value).commit();
    }

    public static String getSharedPrefString(Context context, String key, String defValue) {
        return context.getSharedPreferences(SAVE_NAME, Context.MODE_PRIVATE).getString(key, defValue);
    }

    //int
    public static void setSharedPrefInt(Context context, String key, int value){
        context.getSharedPreferences(SAVE_NAME, Context.MODE_PRIVATE).edit().putInt(key, value).commit();
    }

    public static int getSharedPrefInt(Context context, String key, int defValue) {
        return context.getSharedPreferences(SAVE_NAME, Context.MODE_PRIVATE).getInt(key, defValue);
    }


}
