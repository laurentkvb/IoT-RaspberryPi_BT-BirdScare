package com.laurent.budgiescare.application;

import android.app.Application;
import android.content.Context;

import com.google.android.gms.analytics.Tracker;

public class Budgiescare extends Application{

    private static Context sContext;
    public final String TAG = getClass().getSimpleName();

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     * @return tracker
     */

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    public static Context getContext() {
        return sContext;
    }
}
