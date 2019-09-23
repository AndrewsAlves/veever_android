package me.custodio.Veever.manager;

import android.content.Context;
import android.content.SharedPreferences;

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
    public static final Locale LOCALE_ENGLISH = Locale.ENGLISH;

    public static Locale DEFAULT_LOCALE = Locale.ENGLISH;

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
                defaultValue = "Default";
                break;
            case PREFS_SPEECHRATE:
                defaultValue = "1.0f";
                break;
        }

        return sp.getString(PREF_TYPE,defaultValue);
    }

    public static Locale getLanguageLocaleFromSettings(Context context) {
        String language = getSettings(context, PREFS_LANGUAGE);

        if (language.equals("Default")) {
            DEFAULT_LOCALE = Locale.getDefault();
            return DEFAULT_LOCALE;
        }

        if (language.equals(PORTUGUESE)) {
            DEFAULT_LOCALE = Settings.LOCALE_PORTUGUESE;
        } else {
            DEFAULT_LOCALE =  Locale.US;
        }

        return DEFAULT_LOCALE;
    }


}
