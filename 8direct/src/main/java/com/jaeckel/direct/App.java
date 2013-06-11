package com.jaeckel.direct;

import android.app.Application;


/**
 * @author flashmop
 * @date 02.03.13 12:06
 */
public class App extends Application {

    private static App instance;
    public final static String TAG = "8DIRECT";


    @Override
    public void onCreate() {
        super.onCreate();    //To change body of overridden methods use File | Settings | File Templates.
        instance = this;
    }

    public static App getInstance() {
        return instance;
    }
}
