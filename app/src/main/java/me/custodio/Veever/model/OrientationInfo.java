package me.custodio.Veever.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by Andrews on 17,May,2019
 */

public class OrientationInfo  extends RealmObject {

    @SerializedName("orientationTitle")
    public String title;

    @SerializedName("orientationDescription")
    public String description;

}
