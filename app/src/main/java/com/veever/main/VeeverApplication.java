package com.veever.main;

import android.app.Application;

import com.veever.main.manager.DatabaseManager;

public class VeeverApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        DatabaseManager.initialize(this);

    }

    // api key nearby = AIzaSyCrnrHAF6Z87vHhwVAV60muPjeoKaV7Yhw
}
