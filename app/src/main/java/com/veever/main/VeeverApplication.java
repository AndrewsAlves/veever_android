package com.veever.main;

import android.app.Application;

import com.veever.main.manager.APIManager;
import com.veever.main.manager.DatabaseManager;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;

public class VeeverApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        DatabaseManager.initialize(this);
        APIManager.initialize(this);

        BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);

        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25")); //Altbeacon layout
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24")); // ibeacon layout

        beaconManager.setDebug(true);
    }

    // api key nearby = AIzaSyCrnrHAF6Z87vHhwVAV60muPjeoKaV7Yhw
}
