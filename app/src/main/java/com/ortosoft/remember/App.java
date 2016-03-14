package com.ortosoft.remember;

import android.app.Application;
import android.content.Context;

/**
 * Created by dima on 03.03.2016.
 */
public class App extends Application {
    private static App _instance;
    private static Context _context;

    public App() {_instance = this;}


    public static App getInstance() {return _instance;}
    public static Context getContext() {return _context;}

    @Override
    public void onCreate() {
        super.onCreate();
        App._context = getApplicationContext();
    }
}
