package com.veever.main.manager;

import android.content.Context;

import com.veever.main.VeeverMigration;
import com.veever.main.datamodel.BeaconModel;
import com.veever.main.datamodel.Spot;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Andrews on 17,May,2019
 */


public class DatabaseManager {

    private static final String TAG = "API MANAGER";
    private static DatabaseManager ourInstance;

    private Realm realm;

    public static DatabaseManager getInstance() {
        return ourInstance;
    }

    private DatabaseManager(Context context) {
        Realm.init(context);

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name("veever")
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .migration(new VeeverMigration())
                .build();

        Realm.setDefaultConfiguration(realmConfiguration);
        realm = Realm.getDefaultInstance();
    }

    public static void initialize(Context context) {
        ourInstance = new DatabaseManager(context);
    }

    public void saveBeacons(List<BeaconModel> beaconList) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(beaconList);
        realm.commitTransaction();
    }

    public void saveSpots(List<Spot> spotList) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(spotList);
        realm.commitTransaction();
    }

    public void getSpotOrients() {

        Spot spot = realm.where(Spot.class).findFirst();

    }

    public List<BeaconModel> getBeaconList() {
        List<BeaconModel> beaconList = realm.where(BeaconModel.class).findAll();
        return realm.copyFromRealm(beaconList);
    }

    public BeaconModel getBeacon(String uuid, int major, int minor) {

        BeaconModel beacon = realm.where(BeaconModel.class)
                .equalTo("uuid",uuid.toUpperCase())
                .equalTo("major", major)
                .equalTo("minor", minor)
                .findFirst();

        if (beacon == null) {
            return null;
        }

        return realm.copyFromRealm(beacon);
    }

    public Spot getSpot(String spotid) {

        Spot spot = realm.where(Spot.class).equalTo("id",spotid).findFirst();

        if (spot == null) {
            return null;
        }

        return realm.copyFromRealm(spot);
    }

    public BeaconModel getBeaconByShortCode(String shortCode) {

        BeaconModel beacon = realm.where(BeaconModel.class).equalTo("shortCode",shortCode).findFirst();

        if (beacon == null) {
            return null;
        }

        return realm.copyFromRealm(beacon);
    }

}
