package me.custodio.Veever.model;

import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

/**
 * Created by Andrews on 18,September,2019
 */

public class User {

    @ServerTimestamp
    Date createAt;

    String createdBy;

    String email;
    String firstName;
    GeoPoint geoLocation;
    String lastName;
    String userId;
    String safeWord;

    boolean wantsHelp;

    //boolean isTesting = true;

    public User() {
    }

    public User(String userId) {
        this.userId = userId;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserId() {
        return userId;
    }

    public boolean isWantsHelp() {
        return wantsHelp;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setGeoLocation(GeoPoint geoLocation) {
        this.geoLocation = geoLocation;
    }

    public String getSafeWord() {
        return safeWord;
    }

    public void setSafeWord(String safeWord) {
        this.safeWord = safeWord;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setWantsHelp(boolean wantsHelp) {
        this.wantsHelp = wantsHelp;
    }

    public GeoPoint getGeoLocation() {
        return geoLocation;
    }

   // public boolean isTesting() {
   //     return isTesting;
   // }
}
