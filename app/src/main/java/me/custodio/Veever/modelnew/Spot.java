package me.custodio.Veever.modelnew;

import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.List;

/**
 * Created by Andrews on 21,September,2019
 */
public class Spot {

    @ServerTimestamp
    Date createdAt;

    String createdBy;

    String defaultLanguage;

    boolean deleted;

    GeoPoint geoLocation;

    List<String> pictures;

    String shortCode;

    @ServerTimestamp
    Date updatedAt;

    String updatedBy;

}
