package me.custodio.Veever.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Andrews on 18,September,2019
 */
public class SharedPrefsManager {

    static final String PREF_VEEVER_USER_ID = "user_id";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static String getUserId(Context context) {
        return getSharedPreferences(context).getString(PREF_VEEVER_USER_ID, null);
    }

    public static void saveUserId(Context context, String userId) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREF_VEEVER_USER_ID, userId);
        editor.apply();
    }


}
