package me.custodio.Veever.datamodel;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by Andrews on 17,May,2019
 */

public class Orientation extends RealmObject {

    @SerializedName("north")
    public OrientationInfo northInfo;

    @SerializedName("east")
    public OrientationInfo eastInfo;

    @SerializedName("south")
    public OrientationInfo southInfo;

    @SerializedName("west")
    public OrientationInfo westInfo;

    @SerializedName("northEast")
    public OrientationInfo northEastInfo;

    @SerializedName("southEast")
    public OrientationInfo southEastInfo;

    @SerializedName("northWest")
    public OrientationInfo northWestInfo;

    @SerializedName("southWest")
    public OrientationInfo southWestInfo;

}
