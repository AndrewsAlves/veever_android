package com.veever.main.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.veever.main.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class SpeechRateFragment extends Fragment {


    public SpeechRateFragment() {
        // Required empty public constructor
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_speech_rate, container, false);
        ButterKnife.bind(this,v);


        return v;
    }


    @OnClick(R.id.ib_back__speech)
    public void back() {

    }
}
