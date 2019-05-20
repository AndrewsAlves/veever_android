package com.veever.main;

import android.app.Application;
import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import java.util.Locale;

/**
 * Created by Admin on 20,May,2019
 */
public class TextToSpeechManager {

    private static  TextToSpeechManager ourInstance;

    public TextToSpeech textToSpeech;

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
                    int ttsLang = textToSpeech.setLanguage(Locale.US);

                    if (ttsLang == TextToSpeech.LANG_MISSING_DATA
                            || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "The Language is not supported!");
                    } else {
                        Log.i("TTS", "Language Supported.");
                    }
                    Log.i("TTS", "Initialization success.");
                } else {
                    Toast.makeText(application.getApplicationContext(), "TTS Initialization failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void speak(String string) {
        int speechStatus = textToSpeech.speak(string, TextToSpeech.QUEUE_FLUSH, null);

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

}
