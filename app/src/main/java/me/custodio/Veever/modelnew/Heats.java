package me.custodio.Veever.modelnew;

import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

/**
 * Created by Admin on 21,September,2019
 */
public class Heats {

    @ServerTimestamp
    Date createdAt;

    String createdBy;

    GeoPoint geoLocation;

    String spot;

    GeoPoint userLocation;

    public Heats() {
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public GeoPoint getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(GeoPoint geoLocation) {
        this.geoLocation = geoLocation;
    }

    public String getSpot() {
        return spot;
    }

    public void setSpot(String spot) {
        this.spot = spot;
    }

    public GeoPoint getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(GeoPoint userLocation) {
        this.userLocation = userLocation;
    }

}
