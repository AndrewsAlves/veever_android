package com.veever.main.datamodel;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Andrews on 17,May,2019
 */

public class BeaconModel extends RealmObject {

    @PrimaryKey
    @SerializedName("id")
    public String id;

    @SerializedName("shortCode")
    public String shortCode;

    @SerializedName("uuid")
    public String uuid;

    @SerializedName("major")
    public int major;

    @SerializedName("minor")
    public int minor;

    @SerializedName("spot")
    public SpotInfo spotInfo;

    @SerializedName("rangingDistance")
    public String rangingDistance;

    @SerializedName("deleted")
    public boolean deleted;

    @SerializedName("updatedAt")
    public String updatedAt;

    @SerializedName("updatedBy")
    public String updatedBy;

    @SerializedName("createdAt")
    public String createdAt;

    @SerializedName("createdBy")
    public String createdBy;

}
