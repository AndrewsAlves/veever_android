package com.veever.main.manager;

import android.content.Context;

public class APIManager {
    private static final APIManager ourInstance = new APIManager();

    public static APIManager getInstance() {
        return ourInstance;
    }

    private APIManager() {
    }

    public static void initialize(Context context) {
        ourInstance = new DatabaseManager(context);
    }
}
