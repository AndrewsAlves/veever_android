package com.veever.main.api;

import com.veever.main.datamodel.Spot;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface SpotEndPoint {

    @GET("spots")
    Call<List<Spot>> fetchSpots();

}
