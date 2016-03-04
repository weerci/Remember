package com.ortosoft.remember;

import android.app.Application;

/**
 * Created by dima on 03.03.2016.
 */
public class App extends Application {
    private static App instance;

    public App() {instance = this;}
    public static App getInstance() {return instance;}

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
