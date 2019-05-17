package com.veever.main.api;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;

/**
 * Created by Andrews on 17,May,2019
 */

public class SpotResponse {

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
