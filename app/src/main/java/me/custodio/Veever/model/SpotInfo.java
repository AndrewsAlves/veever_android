package me.custodio.Veever.model;

import me.custodio.Veever.enums.GeoDirections;

/**
 * Created by Andrews on 17,May,2019
 */

public class SpotInfo {

    public String name;

    public Orientation orientation;

    public String voiceDescription;

    public String voiceName;

    public String zoneNotification;

    public String zoneLocation;

    public SpotInfo() {
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public OrientationInfo getDirectionInfo(GeoDirections geoDirections) {

        switch (geoDirections) {
            case NORTH:
                return orientation.north;
            case NORTH_EAST:
                return orientation.northEast;
            case EAST:
                return orientation.east;
            case SOUTH_EAST:
                return orientation.southEast;
            case SOUTH:
                return orientation.south;
            case SOUTH_WEST:
                return orientation.southWest;
            case WEST:
                return orientation.west;
            case NORTH_WEST:
                return orientation.northWest;
           default:
               return null;
        }
    }
}
