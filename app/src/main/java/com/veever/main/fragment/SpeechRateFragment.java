package com.veever.main.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.veever.main.R;
import com.veever.main.manager.Settings;
import com.veever.main.manager.TextToSpeechManager;

import org.w3c.dom.Text;

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
        textView.setText(String.valueOf(speechRate));

        TextToSpeechManager.getInstance().speak("Speech rate is " + speechRate);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                speechRate = progress / 50f;
                textView.setText(String.valueOf(speechRate));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Settings.saveSpeechRate(getContext(), Float.toString(speechRate));
                updateUi();
            }
        });

        return v;
    }

    public void updateUi() {
        textView.setText(String.valueOf(speechRate));
        TextToSpeechManager.getInstance().setSpeechRate(speechRate);
        TextToSpeechManager.getInstance().speak("Speech rate is " + speechRate);
    }
    @OnClick(R.id.ib_back__speech)
    public void back() {
        getActivity().getSupportFragmentManager().popBackStack();
    }
}
