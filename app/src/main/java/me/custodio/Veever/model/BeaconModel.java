package me.custodio.Veever.model;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

/**
 * Created by Andrews on 21,September,2019
 */

// beacon model 2.0
public class BeaconModel {

    String brand;

    @ServerTimestamp
    Date createdAt;

    String createdBy;

    boolean deleted;

    int major;

    int minor;

    String model;

    String rangingDistance;

    DocumentReference spot;

    @ServerTimestamp
    Date updatedAt;

    String updatedBy;

    String uuid;

    public BeaconModel() {
    }


    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getRangingDistance() {
        return rangingDistance;
    }

    public DocumentReference getSpot() {
        return spot;
    }

    public String getUuid() {
        return uuid;
    }

    public String getBrand() {
        return brand;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public void setMajor(int major) {
        this.major = major;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }

    public void setRangingDistance(String rangingDistance) {
        this.rangingDistance = rangingDistance;
    }

    public void setSpot(DocumentReference spot) {
        this.spot = spot;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
