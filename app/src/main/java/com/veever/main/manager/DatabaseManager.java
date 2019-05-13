package com.veever.main.manager;

import android.content.Context;
import android.util.Log;

import com.veever.main.VeeverMigration;
import com.veever.main.datamodel.Beacon;
import com.veever.main.datamodel.Spot;

import java.util.List;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;

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
                .migration(new VeeverMigration())
                .build();

        Realm.setDefaultConfiguration(realmConfiguration);
        realm = Realm.getDefaultInstance();
    }

    public static void initialize(Context context) {
        ourInstance = new DatabaseManager(context);
    }

    public void saveBeacons(List<Beacon> beaconList) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(beaconList);
        realm.commitTransaction();
    }

    public void saveSpots(List<Spot> spotList) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(spotList);
        realm.commitTransaction();
    }

    public List<Beacon> getBeaconList() {

        List<Beacon> beaconList = realm.where(Beacon.class).findAll();

        return realm.copyFromRealm(beaconList);
    }

    public Spot getSpotFromRealm(String uuid, String major, String minor) {

        Spot spot = null;

        Beacon beacon = realm.where(Beacon.class)
                .equalTo("uuid",uuid)
                .equalTo("major", major)
                .equalTo("minor", minor)
                .findFirst();

        if (beacon != null) {
          spot = realm.where(Spot.class).equalTo("id",beacon.spotid).findFirst();
        }

        return spot;
    }

    public Beacon getBeacon(String uuid, int major, int minor) {

        Log.e(TAG, "getBeacon: uuid: " + uuid);
        Log.e(TAG, "getBeacon: major: " + major);
        Log.e(TAG, "getBeacon: minor: " + minor);

        Beacon beacon = realm.where(Beacon.class)
                .equalTo("uuid",uuid.toUpperCase())
                .equalTo("major", major)
                .equalTo("minor", minor)
                .findFirst();

        if (beacon == null) {
            Log.e(TAG, "getBeacon: beacon null");
            return null;
        }

        return realm.copyFromRealm(beacon);
    }

    public Spot getSpot(String spotid) {

        Log.e(TAG, "getSpot: id: " + spotid);

        Spot spot = realm.where(Spot.class).equalTo("id",spotid).findFirst();

        return realm.copyFromRealm(spot);
    }

}
