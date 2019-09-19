package me.custodio.Veever.datamodel;

import com.google.gson.annotations.SerializedName;
import me.custodio.Veever.enums.GeoDirections;

import io.realm.RealmObject;

/**
 * Created by Andrews on 17,May,2019
 */

public class Spot extends RealmObject {

    @SerializedName("spotName")
    public String spotName;

    @SerializedName("spotTitle")
    public String spotTitle;

    @SerializedName("spotDescription")
    public String spotDescription;

    @SerializedName("zoneNotification")
    public String zoneNotification;

    @SerializedName("zoneLocation")
    public String zoneLocation;

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
