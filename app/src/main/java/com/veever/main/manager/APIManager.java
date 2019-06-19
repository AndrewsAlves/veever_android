package com.veever.main.manager;

import android.content.Context;
import android.util.Log;

import com.veever.main.api.BeaconsEndPoint;
import com.veever.main.api.SpotEndPoint;
import com.veever.main.datamodel.BeaconModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Andrews on 17,May,2019
 */

public class APIManager {
    private static final String TAG = "API manager";
    private static APIManager ourInstance;

    private static String BASE_URL = "https://api.veever.experio.com.br/";

    private BeaconsEndPoint beaconsEndPoint;
    private SpotEndPoint spotEndPoint;

    public static APIManager getInstance() {
        return ourInstance;
    }

    private APIManager() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        beaconsEndPoint = retrofit.create(BeaconsEndPoint.class);
        spotEndPoint = retrofit.create(SpotEndPoint.class);

        fetchBeacons();
    }

    public static void initialize(Context context) {
        ourInstance = new APIManager();
    }

    public void fetchBeacons() {
        final Call<List<BeaconModel>> beaconList = beaconsEndPoint.fetchBeacons();
        beaconList.enqueue(new Callback<List<BeaconModel>>() {
            @Override
            public void onResponse(Call<List<BeaconModel>> call, Response<List<BeaconModel>> response) {

                if (response.body() == null && !response.isSuccessful()) {
                    Log.e(TAG, "onResponse: failed to fetch beacons");
                    return;
                }

                for (BeaconModel beacon : response.body()) {
                    Log.e(TAG, "onResponse: beacon uuid " + beacon.uuid + " " + beacon.major);
                    Log.e(TAG, "onResponse: beacon default language " + beacon.spotInfo.defaultLanguage  );
                }

                DatabaseManager.getInstance().saveBeacons(response.body());
            }

            @Override
            public void onFailure(Call<List<BeaconModel>> call, Throwable t) {

            }
        });
    }
}
