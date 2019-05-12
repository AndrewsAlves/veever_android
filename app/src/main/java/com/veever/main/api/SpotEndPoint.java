package com.veever.main.api;

import retrofit2.Call;
import retrofit2.http.GET;

public interface SpotEndPoint {

    @GET("spots")
    Call<SpotResponse> fetchSpots();

}
