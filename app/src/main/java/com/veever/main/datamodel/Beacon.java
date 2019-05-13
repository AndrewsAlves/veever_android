package com.veever.main.datamodel;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Beacon extends RealmObject {

    @PrimaryKey
    @SerializedName("id")
    public String id;

    @SerializedName("uuid")
    public String uuid;

    @SerializedName("major")
    public int major;

    @SerializedName("minor")
    public int minor;

    @SerializedName("spotId")
    public String spotid;

    @SerializedName("active")
    public boolean isActive;

    @SerializedName("rangingDistance")
    public String rangingDistance;

    @SerializedName("immediate")
    public String createdAt;

    @SerializedName("updatedAt")
    public String updatedAt;
}
