package com.veever.main.manager;

import android.content.Context;

import com.veever.main.VeeverMigration;
import com.veever.main.datamodel.Beacon;
import com.veever.main.datamodel.Spot;

import java.util.List;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;

public class DatabaseManager {

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

    public Spot getSpotFromRealm(String uuid, String major, String minor) {

        Spot spot = null;

        realm.beginTransaction();
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

}
