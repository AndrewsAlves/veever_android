package me.custodio.Veever;

import android.app.Application;
import android.content.res.Configuration;

import com.franmontiel.localechanger.LocaleChanger;
import com.onesignal.OneSignal;

import me.custodio.Veever.manager.APIManager;
import me.custodio.Veever.manager.DatabaseManager;
import me.custodio.Veever.manager.FirestoreManager;
import me.custodio.Veever.manager.TextToSpeechManager;
import me.custodio.Veever.manager.VeeverSensorManager;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Created by Andrews on 17,May,2019
 */

public class VeeverApplication extends Application {

    public static final List<Locale> SUPPORTED_LOCALES =
            Arrays.asList(
                    new Locale("en", "US"),
                    new Locale("pt", "BR")
            );

    @Override
    public void onCreate() {
        super.onCreate();

        LocaleChanger.initialize(getApplicationContext(), SUPPORTED_LOCALES);

        //DatabaseManager.initialize(this);
        FirestoreManager.intialize(this);
        APIManager.initialize(this);
        VeeverSensorManager.initialise(this);
        TextToSpeechManager.initialise(this);

        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);

        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25")); //Altbeacon layout
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24")); // ibeacon layout
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("x,s:0-1=feaa,m:2-2=20,d:3-3,d:4-5,d:6-7,d:8-11,d:12-15")); // EDDYSTONE TLM
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("s:0-1=feaa,m:2-2=00,p:3-3:-41,i:4-13,i:14-19")); // EDDYSTONE UID
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("s:0-1=feaa,m:2-2=10,p:3-3:-41,i:4-20v")); // EDDYSTONE URL

        beaconManager.setDebug(true);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LocaleChanger.onConfigurationChanged();
    }
}
