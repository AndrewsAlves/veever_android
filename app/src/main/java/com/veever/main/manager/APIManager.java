package com.veever.main.manager;

import android.content.Context;

import com.veever.main.BuildConfig;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIManager {
    private static APIManager ourInstance;

    private static String BASE_URL = "https://api.veever.experio.com.br/";

    public static APIManager getInstance() {
        return ourInstance;
    }

    private APIManager() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static void initialize(Context context) {
        ourInstance = new APIManager();
    }
}
