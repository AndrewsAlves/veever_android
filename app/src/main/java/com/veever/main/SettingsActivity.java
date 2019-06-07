package com.veever.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingsActivity extends AppCompatActivity {

    @BindView(R.id.ll_speech)
    LinearLayout llSpeech;
    @BindView(R.id.ll_demostration)
    LinearLayout llDemostration;
    @BindView(R.id.ll_email)
    LinearLayout llEmail;
    @BindView(R.id.ll_interface)
    LinearLayout llInterface;
    @BindView(R.id.ll_terms)
    LinearLayout llTerms;
    @BindView(R.id.ll_web)
    LinearLayout llWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

}
