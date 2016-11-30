package com.springer.patryk.tas_android;

import android.app.Application;

import net.danlew.android.joda.JodaTimeAndroid;

/**
 * Created by Patryk on 2016-11-30.
 */

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
    }
}
