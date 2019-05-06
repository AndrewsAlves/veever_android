package com.veever.main.manager;

import android.content.Context;

import io.realm.Realm;

public class DatabaseManager {

    private static DatabaseManager ourInstance;

    public static DatabaseManager getInstance() {
        return ourInstance;
    }

    private DatabaseManager(Context context) {
        Realm.init(context);
    }

    public static void initialize(Context context) {
        ourInstance = new DatabaseManager(context);
    }

}
