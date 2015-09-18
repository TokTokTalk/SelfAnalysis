package com.toktoktalk.selfanalysis.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.toktoktalk.selfanalysis.common.GsonConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by seogangmin on 2015. 8. 16..
 */
public class ComPreference {
    private final String PREF_NAME = "com.toktoktalk.pref";


    static Context mContext;

    public ComPreference(Context c) {
        mContext = c;
    }

    public void put(String key, String value) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString(key, value);
        editor.commit();
    }

    public void put(String key, boolean value) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putBoolean(key, value);
        editor.commit();
    }

    public void put(String key, int value) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putInt(key, value);
        editor.commit();
    }

    public void put(String key, Object value) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        String jsonValue = GsonConverter.toJson(value);
        editor.putString(key, jsonValue);
        editor.commit();
    }

    public String getValue(String key, String dftValue) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);

        try {
            return pref.getString(key, dftValue);
        } catch (Exception e) {
            return dftValue;
        }

    }

    public int getValue(String key, int dftValue) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);

        try {
            return pref.getInt(key, dftValue);
        } catch (Exception e) {
            return dftValue;
        }

    }

    public boolean getValue(String key, boolean dftValue) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);

        try {
            return pref.getBoolean(key, dftValue);
        } catch (Exception e) {
            return dftValue;
        }
    }

    public Object getValue(String key, Class castCls, String dftValue) {
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);

        try {
            String jsonObj =pref.getString(key, dftValue);
            return GsonConverter.fromJson(jsonObj, castCls);
        } catch (Exception e) {
            return dftValue;
        }
    }

    public List getValues(String key, Class castCls, String dftValue){
        SharedPreferences pref = mContext.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);

        try {
            String jsonArray =pref.getString(key, dftValue);
            return (List)GsonConverter.fromJsonArray(jsonArray, castCls);
        } catch (Exception e) {
            return null;
        }
    }
}
