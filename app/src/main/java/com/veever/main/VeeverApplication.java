package com.veever.main;

import android.app.Application;

import com.veever.main.manager.APIManager;
import com.veever.main.manager.DatabaseManager;
import com.veever.main.manager.VeeverSensorManager;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;

public class VeeverApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        DatabaseManager.initialize(this);
        APIManager.initialize(this);
        VeeverSensorManager.initialise(this);

        BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);

        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25")); //Altbeacon layout
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24")); // ibeacon layout
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("x,s:0-1=feaa,m:2-2=20,d:3-3,d:4-5,d:6-7,d:8-11,d:12-15")); // EDDYSTONE TLM
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("s:0-1=feaa,m:2-2=00,p:3-3:-41,i:4-13,i:14-19")); // EDDYSTONE UID
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("s:0-1=feaa,m:2-2=10,p:3-3:-41,i:4-20v")); // EDDYSTONE URL

        beaconManager.setDebug(true);
    }

    // api key nearby = AIzaSyCrnrHAF6Z87vHhwVAV60muPjeoKaV7Yhw
}
