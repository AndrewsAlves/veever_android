package com.veever.main.datamodel;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class OrientationInfo  extends RealmObject {

    @SerializedName("title")
    public String title;

    @SerializedName("description")
    public String description;

}
