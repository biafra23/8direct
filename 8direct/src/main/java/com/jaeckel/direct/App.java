package com.jaeckel.direct;

import android.app.Application;

/**
 * @author flashmop
 * @date 02.03.13 12:06
 */
public class App extends Application {

    public final static String TAG = "8DIRECT";

    private static App instance;
    private boolean[] activated = new boolean[8];


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static App getInstance() {
        return instance;
    }


    public boolean[] getActivated() {
        return activated;
    }

    public void setActivated(boolean[] activated) {
        this.activated = activated;
    }
}
