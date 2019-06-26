package com.veever.main.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.veever.main.datamodel.Spot;
import com.veever.main.datamodel.SpotInfo;

import java.util.Locale;

/**
 * Created by Andrews on 07,June,2019
 */
public class Settings {

    public static final String IMMEDIATE = "immediate";
    public static final String FAR = "far";
    public static final String NEAR = "near";

    public static final String PREFS_VEEVER = "com.veever.main";
    public static final String PREFS_LANGUAGE = "DefaultLanguage";
    public static final String PREFS_SPEECHRATE = "SpeechRate";
    public static final String PREFS_DEMONSTRATION_MODE = "DemonstrationMode";

    public static final String ENGLISH = "enUS";
    public static final String PORTUGUESE = "ptBR";

    public static final String WEB_PORTAL = "https://veever.global";
    public static final String PRIVACY_POLICY = "https://veever.global/privacy";
    public static final String TERMS_OF_USE = "https://veever.global/terms";

    public static final Locale LOCALE_PORTUGUESE = new Locale("pt", "BR");

    public static Spot getSpotBasedOnLanguage(Context context,SpotInfo spotInfo) {

        if (spotInfo.defaultLanguage.equals(ENGLISH)) {
            return spotInfo.english;
        } else {
            return spotInfo.portuguese;
        }

      /*  if (getSettings(context, PREFS_LANGUAGE).equals(ENGLISH)) {
            if (spotInfo.english == null) {
                return spotInfo.portuguese;
            } else {
                return spotInfo.english;
            }
        } else {
            if (spotInfo.portuguese == null) {
                return spotInfo.english;
            } else {
                return spotInfo.portuguese;
            }
        } */
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
                defaultValue = ENGLISH;
                break;
            case PREFS_SPEECHRATE:
                defaultValue = "1.0f";
                break;
        }

        return sp.getString(PREF_TYPE,defaultValue);
    }

    public static Locale getLanguageLocaleFromSettings(Context context) {
        String language = getSettings(context, PREFS_LANGUAGE);

        if (language.equals(PORTUGUESE)) {
            return Settings.LOCALE_PORTUGUESE;
        } else {
            return Locale.US;
        }
    }

}
