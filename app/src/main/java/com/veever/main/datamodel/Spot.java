package com.veever.main.datamodel;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Spot extends RealmObject {

    @PrimaryKey
    @SerializedName("id")
    public String id;

    @SerializedName("spotName")
    public String spotName;

    @SerializedName("zoneLocation")
    public String zoneLocation;

    @SerializedName("active")
    public boolean isActive;

    @SerializedName("spotTitle")
    public String spotTitle;

    @SerializedName("spotDescription")
    public String spotDescription;

    @SerializedName("beaconId")
    public String beaconId;

    @SerializedName("createdAt")
    public String createdAt;

    @SerializedName("updatedAt")
    public String updatedAt;

}
