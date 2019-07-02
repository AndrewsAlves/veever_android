package me.custodio.Veever.api;

import me.custodio.Veever.datamodel.BeaconModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Andrews on 17,May,2019
 */

public interface BeaconsEndPoint {

    @GET("beacons")
    Call<List<BeaconModel>> fetchBeacons();

}
