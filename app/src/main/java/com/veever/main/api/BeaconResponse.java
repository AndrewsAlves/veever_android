package com.veever.main.api;

import com.google.gson.annotations.SerializedName;

public class BeaconResponse {

    @SerializedName("id")
    public String id;

    @SerializedName("uuid")
    public String uuid;

    @SerializedName("major")
    public int major;

    @SerializedName("minor")
    public int minor;

    @SerializedName("spotid")
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
