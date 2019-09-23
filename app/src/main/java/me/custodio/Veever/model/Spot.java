package me.custodio.Veever.model;

import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.List;
import java.util.Map;

import me.custodio.Veever.manager.Settings;

/**
 * Created by Andrews on 21,September,2019
 */
public class Spot {

    @ServerTimestamp
    Date createdAt;

    String createdBy;

    String defaultLanguage;

    boolean deleted;

    SpotInfo enUS;

    GeoPoint geoLocation;

    List<String> pictures;

    SpotInfo ptBR;

    String shortCode;

    @ServerTimestamp
    Date updatedAt;

    String updatedBy;

    public Spot() {
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

    public String getDefaultLanguage() {
        return defaultLanguage;
    }

    public void setDefaultLanguage(String defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public GeoPoint getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(GeoPoint geoLocation) {
        this.geoLocation = geoLocation;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public SpotInfo getEnUS() {
        return enUS;
    }

    public void setEnUS(SpotInfo enUS) {
        this.enUS = enUS;
    }

    public SpotInfo getPtBR() {
        return ptBR;
    }

    public void setPtBR(SpotInfo ptBR) {
        this.ptBR = ptBR;
    }

    public SpotInfo getSpotInfo() {
        if (defaultLanguage.equals(Settings.PORTUGUESE)) {
            return ptBR;
        } else {
            return enUS;
        }
    }

    public LanguageType getDefaultLanguageType() {
        if (defaultLanguage.equals("ptBR")) {
            return LanguageType.PORTUGUESE;
        } else {
            return LanguageType.ENGLISH;
        }
    }
}
