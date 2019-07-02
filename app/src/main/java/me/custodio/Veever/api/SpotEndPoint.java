package me.custodio.Veever.api;

import me.custodio.Veever.datamodel.Spot;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Andrews on 17,May,2019
 */

public interface SpotEndPoint {

    @GET("spots")
    Call<List<Spot>> fetchSpots();

}
