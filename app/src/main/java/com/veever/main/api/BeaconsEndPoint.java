package com.veever.main.api;

import android.util.Size;

import com.veever.main.datamodel.Beacon;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface BeaconsEndPoint {

    @GET("beacons")
    Call<List<Beacon>> fetchBeacons();

}
