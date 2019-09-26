package me.custodio.Veever.manager;

import android.app.Application;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Admin on 20,May,2019
 */
public class TextToSpeechManager {

    private static final String TAG = "TOS";
    private static  TextToSpeechManager ourInstance;

    public TextToSpeech textToSpeech;

    public boolean noSpeechIntereption = false;

    public static TextToSpeechManager getInstance() {
        return ourInstance;
    }

    public static void initialise(Application application) {
        ourInstance = new TextToSpeechManager(application);
    }

    private TextToSpeechManager(final Application application) {

        textToSpeech = new TextToSpeech(application.getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {

                    int ttsLang = textToSpeech.setLanguage(Settings.getLanguageLocaleFromSettings(application.getBaseContext()));

                    float speechRate = Float.valueOf(Settings.getSettings(
                            application.getBaseContext(),
                            Settings.PREFS_SPEECHRATE));

                    textToSpeech.setSpeechRate(speechRate);

                    if (ttsLang == TextToSpeech.LANG_MISSING_DATA
                            || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(application.getBaseContext(), "Language not supported for speech!", Toast.LENGTH_LONG).show();
                        Log.e("TTS", "The Language is not supported!");
                    } else {
                        Log.i("TTS", "Language Supported.");
                    }

                    // set speech rate

                    Log.i("TTS", "Initialization success.");
                } else {
                    Toast.makeText(application.getApplicationContext(), "TTS Initialization failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void speak(String string, boolean noSpeechInter) {

        if (string == null) {
            return;
        }

        textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) { }

            @Override
            public void onDone(String utteranceId) {
                noSpeechIntereption = false;
                Log.e(TAG, "onDone: finished speaking: " + string);
            }

            @Override
            public void onError(String utteranceId) { }
        });

        int speechStatus = 0;

        HashMap<String, String> map = new HashMap<String, String>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "OnIntereptedSpeech");

        if (!noSpeechIntereption) {
           speechStatus = textToSpeech.speak(string, TextToSpeech.QUEUE_FLUSH, map);
        }

        this.noSpeechIntereption = noSpeechInter;

        if (speechStatus == TextToSpeech.ERROR) {
            Log.e("TTS", "Error in converting Text to Speech!");
        }
    }

    public void speak(String string) {

        if (string == null) {
            return;
        }

        int speechStatus = 0;

        if (!noSpeechIntereption) {
            speechStatus = textToSpeech.speak(string, TextToSpeech.QUEUE_FLUSH, null);
        }

        if (speechStatus == TextToSpeech.ERROR) {
            Log.e("TTS", "Error in converting Text to Speech!");
        }
    }

    public void destroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }

    public void stopSpeech() {
        if (textToSpeech != null) {
            textToSpeech.stop();
        }
    }

    public void setLanguage(Locale locale) {
        textToSpeech.setLanguage(locale);
    }

    public void setSpeechRate(float rate) {
        textToSpeech.setSpeechRate(rate);
    }

}
