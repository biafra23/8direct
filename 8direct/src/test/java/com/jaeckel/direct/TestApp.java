package com.jaeckel.direct;

import de.akquinet.android.androlog.Log;

/**
 * This is the App class during robolectric tests
 */
public class TestApp extends App {


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "TestApp.onCreate()");
    }


}
