package com.veever.main.datamodel;

import com.google.gson.annotations.SerializedName;
import com.veever.main.GeoDirections;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.LinkingObjects;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Andrews on 17,May,2019
 */

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

    @SerializedName("orientation")
    public Orientation orientation;

    public Orientation getOrientation() {
        return orientation;
    }

    public OrientationInfo getDirectionInfo(GeoDirections geoDirections) {

        switch (geoDirections) {
            case NORTH:
                return orientation.northInfo;
            case NORTH_EAST:
                return orientation.northEastInfo;
            case EAST:
                return orientation.eastInfo;
            case SOUTH_EAST:
                return orientation.southEastInfo;
            case SOUTH:
                return orientation.southInfo;
            case SOUTH_WEST:
                return orientation.southWestInfo;
            case WEST:
                return orientation.westInfo;
            case NORTH_WEST:
                return orientation.northWestInfo;
           default:
               return null;
        }
    }
}
