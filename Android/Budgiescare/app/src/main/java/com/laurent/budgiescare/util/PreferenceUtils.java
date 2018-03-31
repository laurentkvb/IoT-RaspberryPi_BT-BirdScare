package com.laurent.budgiescare.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.laurent.budgiescare.application.Budgiescare;


public class PreferenceUtils {

    private static final String KEY = "sharedPreferences";
    private static final String KEY_IP_ADDRESS = "KEY_IP_ADDRESS";

    private static PreferenceUtils sInstance = new PreferenceUtils();

    private SharedPreferences mSharedPreferences;

    private PreferenceUtils() {
        mSharedPreferences = Budgiescare.getContext().getSharedPreferences(KEY, Context.MODE_PRIVATE);
    }

    public static synchronized PreferenceUtils getInstance() {
        return sInstance;
    }

    public void setIP(String secretPin) {
        mSharedPreferences.edit()
                .putString(KEY_IP_ADDRESS, secretPin)
                .apply();
    }

    public String getIP() {
        return mSharedPreferences.getString(KEY_IP_ADDRESS, "");
    }



}



