package com.veever.main.manager;

import android.content.Context;
import android.util.Log;

import com.veever.main.BuildConfig;
import com.veever.main.api.BeaconResponse;
import com.veever.main.api.BeaconsEndPoint;
import com.veever.main.api.SpotEndPoint;
import com.veever.main.api.SpotResponse;
import com.veever.main.datamodel.Beacon;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
        final Call<List<Beacon>> beaconList = beaconsEndPoint.fetchBeacons();
        beaconList.enqueue(new Callback<List<Beacon>>() {
            @Override
            public void onResponse(Call<List<Beacon>> call, Response<List<Beacon>> response) {
                for(Beacon beacon: response.body()) {
                    Log.e(TAG, "onResponse: list - " + beacon.toString());
                }
            }

            @Override
            public void onFailure(Call<List<Beacon>> call, Throwable t) {

            }
        });
    }

    public void fetchSpots(final Callback<SpotResponse> callback) {
        final Call<SpotResponse> beaconList = spotEndPoint.fetchSpots();
        beaconList.enqueue(callback);
    }

}
