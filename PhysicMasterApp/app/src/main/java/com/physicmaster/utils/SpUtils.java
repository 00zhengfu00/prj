package com.physicmaster.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 专门访问和设置SharePreference的工具类, 保存和配置一些设置信息
 *
 * @author Kevin
 */
public class SpUtils {

    private static final String SHARE_PREFS_NAME = "config";
    public static final String MI_PUSH_ID = "MiPushId";
    public static final String LASTDAY_NUMBER = "lastday-number";
    public static final String NONE_WIFI_PROMPT = "none_wifi_prompt";
    public static final String SELECT_CHAPTER_ID = "select_chapter_id";
    public static final String SELECT_CHAPTER_NAME = "select_chapter_name";
    public static final String ADVERTISE_SHOWN = "advertise_shown";
    public static final String ADVERTISE_ID = "advertise_id";
    public static final String SHOW_NOTEBOOK_GUIDE = "show_notebook_guide";
    public static final String NOTEBOOK_GUIDE = "notebook_guide";

    public static void putBoolean(Context ctx, String key, boolean value) {
        SharedPreferences pref = ctx.getSharedPreferences(SHARE_PREFS_NAME,
                Context.MODE_PRIVATE);

        pref.edit().putBoolean(key, value).commit();
    }

    public static boolean getBoolean(Context ctx, String key,
                                     boolean defaultValue) {
        SharedPreferences pref = ctx.getSharedPreferences(SHARE_PREFS_NAME,
                Context.MODE_PRIVATE);

        return pref.getBoolean(key, defaultValue);
    }

    public static void putString(Context ctx, String key, String value) {
        SharedPreferences pref = ctx.getSharedPreferences(SHARE_PREFS_NAME,
                Context.MODE_PRIVATE);

        pref.edit().putString(key, value).commit();
    }

    public static String getString(Context ctx, String key, String defaultValue) {
        SharedPreferences pref = ctx.getSharedPreferences(SHARE_PREFS_NAME,
                Context.MODE_PRIVATE);

        return pref.getString(key, defaultValue);
    }

    public static void putInt(Context ctx, String key, int value) {
        SharedPreferences pref = ctx.getSharedPreferences(SHARE_PREFS_NAME,
                Context.MODE_PRIVATE);

        pref.edit().putInt(key, value).commit();
    }

    public static int getInt(Context ctx, String key, int defaultValue) {
        SharedPreferences pref = ctx.getSharedPreferences(SHARE_PREFS_NAME,
                Context.MODE_PRIVATE);

        return pref.getInt(key, defaultValue);
    }

    public static void putLong(Context ctx, String key, long value) {
        SharedPreferences pref = ctx.getSharedPreferences(SHARE_PREFS_NAME,
                Context.MODE_PRIVATE);
        pref.edit().putLong(key, value).commit();
    }

    public static long getLong(Context ctx, String key, int defaultValue) {
        SharedPreferences pref = ctx.getSharedPreferences(SHARE_PREFS_NAME,
                Context.MODE_PRIVATE);
        return pref.getLong(key, defaultValue);
    }


    /**
     * 移除某个key值对应的值
     */
    public static void remove(Context ctx, String key) {
        SharedPreferences pref = ctx.getSharedPreferences(SHARE_PREFS_NAME,
                Context.MODE_PRIVATE);
        pref.edit().remove(key).commit();
    }

    /**
     * 清除所有数据
     */
    public static void clear(Context ctx) {
        SharedPreferences pref = ctx.getSharedPreferences(SHARE_PREFS_NAME,
                Context.MODE_PRIVATE);
        pref.edit().clear().commit();
    }

}
