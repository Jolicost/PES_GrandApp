package com.jauxim.grandapp.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import static android.content.Context.MODE_PRIVATE;

public class DataUtils {

    private static String KEY_USER_SHARED_PREFERENCES = "key_user_preferences";
    private static String KEY_LATITUDE = "user_latitude";
    private static String KEY_LONGITUDE = "user_longitude";

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(KEY_USER_SHARED_PREFERENCES, MODE_PRIVATE);
    }

    private static void setString(Context context, String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(key, value);
        editor.apply();
    }

    private static String getString(Context context, String key) {
        SharedPreferences prefs = getSharedPreferences(context);
        return prefs.getString(key, null);
    }

    private static void setLong(Context context, String key, Long value) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putLong(key, value);
        editor.apply();
    }

    private static Long getLong(Context context, String key) {
        SharedPreferences prefs = getSharedPreferences(context);
        Long res = prefs.getLong(key, Long.MAX_VALUE);
        return (res == Long.MAX_VALUE ? null : res);
    }

    private static void setFloat(Context context, String key, Float value) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    private static Float getFloat(Context context, String key) {
        SharedPreferences prefs = getSharedPreferences(context);
        Float res = prefs.getFloat(key, Float.MAX_VALUE);
        return (res == Long.MAX_VALUE ? null : res);
    }

    public static void saveLocation(Context context, SingleShotLocationProvider.GPSCoordinates coordinates) {
        if (coordinates == null) return;
        setFloat(context, KEY_LATITUDE, coordinates.latitude);
        setFloat(context, KEY_LONGITUDE, coordinates.longitude);
        RxBus.instanceOf().setLocation(new SingleShotLocationProvider.GPSCoordinates(getFloat(context, KEY_LATITUDE), getFloat(context, KEY_LONGITUDE)));
    }

    public static SingleShotLocationProvider.GPSCoordinates getLocation(Context context){
        if (getFloat(context, KEY_LATITUDE)==null || getFloat(context, KEY_LONGITUDE)==null)
            return null;
        return new SingleShotLocationProvider.GPSCoordinates(getFloat(context, KEY_LATITUDE), getFloat(context, KEY_LONGITUDE));
    }

}