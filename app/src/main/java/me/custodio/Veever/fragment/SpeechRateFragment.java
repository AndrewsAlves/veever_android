package me.custodio.Veever.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import me.custodio.Veever.R;
import me.custodio.Veever.manager.Settings;
import me.custodio.Veever.manager.TextToSpeechManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class SpeechRateFragment extends Fragment {

    @BindView(R.id.seekBar)
    SeekBar seekBar;

    @BindView(R.id.tv_speechrate)
    TextView textView;

    float speechRate;

    public SpeechRateFragment() {
        // Required empty public constructor
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_speech_rate, container, false);
        ButterKnife.bind(this,v);

        speechRate = Float.valueOf(Settings.getSettings(getContext(), Settings.PREFS_SPEECHRATE));
        seekBar.setProgress(Math.round(speechRate * 50f));
        setText();
        speak();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                speechRate = progress / 50f;
                setText();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Settings.saveSpeechRate(getContext(), Float.toString(speechRate));
                setText();
                speak();
            }
        });

        return v;
    }

    public void setText() {
        float percent = speechRate * 50f;
        textView.setText(String.valueOf((int)percent));
    }

    public void speak() {
        float percent = speechRate * 50f;
        TextToSpeechManager.getInstance().setSpeechRate(speechRate);
        TextToSpeechManager.getInstance().speak(
                getString(R.string.app_settings_speech_speechrate)
                        + (int)percent
                        + getString(R.string.app_settings_speechrate_percent));
    }

    @OnClick(R.id.ib_back__speech)
    public void back() {
        TextToSpeechManager.getInstance().stopSpeech();
        getActivity().getSupportFragmentManager().popBackStack();
    }
}
