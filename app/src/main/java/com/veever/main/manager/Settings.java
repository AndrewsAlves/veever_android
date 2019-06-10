package com.veever.main.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.veever.main.datamodel.Spot;
import com.veever.main.datamodel.SpotInfo;

/**
 * Created by Andrews on 07,June,2019
 */
public class Settings {

    public static final String PREFS_VEEVER = "com.veever.main";
    public static final String PREFS_LANGUAGE = "DefaultLanguage";
    public static final String PREFS_SPEECHRATE = "SpeechRate";
    public static final String PREFS_DEMONSTRATION_MODE = "DemonstrationMode";

    public static final String ENGLISH = "enUS";
    public static final String PORTUGUESE = "ptBR";

    public static final String WEB_PORTAL = "enUS";
    public static final String PRIVACY_POLICY = "enUS";
    public static final String TERMS_OF_USE = "enUS";



    public static Spot getSpotBasedOnLanguage(Context context,SpotInfo spotInfo) {
        if (getSettings(context, PREFS_LANGUAGE).equals(ENGLISH)) {
            return spotInfo.english;
        } else {
            return spotInfo.portuguese;
        }
    }

    public static void saveLanguage(Context context,String language) {
        SharedPreferences sp = context.getSharedPreferences(PREFS_VEEVER , Context.MODE_PRIVATE);
        sp.edit().putString(PREFS_LANGUAGE,language).apply();
    }

    public static void saveSpeechRate(Context context,String speechRate) {
        SharedPreferences sp = context.getSharedPreferences(PREFS_VEEVER , Context.MODE_PRIVATE);
        sp.edit().putString(PREFS_SPEECHRATE,speechRate).apply();
    }

    public static void saveDemostrationMode(Context context,String demostrationMode) {
        SharedPreferences sp = context.getSharedPreferences(PREFS_VEEVER , Context.MODE_PRIVATE);
        sp.edit().putString(PREFS_DEMONSTRATION_MODE,demostrationMode).apply();
    }

    public static String getSettings(Context context, String PREF_TYPE) {
        SharedPreferences sp = context.getSharedPreferences(PREFS_VEEVER ,Context.MODE_PRIVATE);

        String defaultValue = "default";

        switch (PREF_TYPE) {
            case PREFS_LANGUAGE:
                defaultValue = PORTUGUESE;
                break;
            case PREFS_SPEECHRATE:
                defaultValue = "0.5";
                break;
        }

        return sp.getString(PREF_TYPE,defaultValue);
    }

}
