package com.veever.main;

import com.google.gson.annotations.SerializedName;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmSchema;
import io.realm.annotations.PrimaryKey;

public class VeeverMigration implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {

        RealmSchema schema = realm.getSchema();

        if (oldVersion == 0) {

            schema.create("Beacon")
                    .addField("id", String.class)
                    .addPrimaryKey("id")
                    .addField("uuid", String.class)
                    .addField("major", int.class)
                    .addField("minor", int.class)
                    .addField("spotid", String.class)
                    .addField("isActive", boolean.class)
                    .addField("rangingDistance", String.class)
                    .addField("createdAt", String.class)
                    .addField("updatedAt", String.class);

            schema.create("Spot")
                    .addField("id", String.class)
                    .addPrimaryKey("id")
                    .addField("spotName", String.class)
                    .addField("zoneLocation", String.class)
                    .addField("isActive", boolean.class)
                    .addField("spotTitle", String.class)
                    .addField("spotDescription", String.class)
                    .addField("beaconId", String.class)
                    .addField("createdAt", String.class)
                    .addField("updatedAt", String.class);

            oldVersion++;
        }

    }
}
