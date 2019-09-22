package me.custodio.Veever.model;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by Andrews on 17,May,2019
 */

public class Orientation extends RealmObject {

    public OrientationInfo east;

    public OrientationInfo north;

    public OrientationInfo northEast;

    public OrientationInfo northWest;

    public OrientationInfo south;

    public OrientationInfo southEast;

    public OrientationInfo southWest;

    public OrientationInfo west;

}
