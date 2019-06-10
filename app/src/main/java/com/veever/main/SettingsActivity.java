package com.veever.main;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.veever.main.fragment.DemontrationFragment;
import com.veever.main.fragment.InterfaceLanguageFragment;
import com.veever.main.fragment.SpeechRateFragment;
import com.veever.main.manager.Settings;

import butterknife.BindView;
import butterknife.ButterKnife;
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

    @BindView(R.id.rl_terms)
    RelativeLayout rlTerms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.ll_speech)
    public void clickSpeech() {
        openFragment(new SpeechRateFragment());
    }

    @OnClick(R.id.ll_demostration)
    public void clickDemonstration() {
        openFragment(new DemontrationFragment());
    }

    @OnClick(R.id.ll_email)
    public void clickEmail() {
        Intent mailClient = new Intent(Intent.ACTION_VIEW);
        mailClient.setClassName("com.google.android.gm", "com.google.android.gm.ConversationListActivity");
        try{
            startActivity(mailClient);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this,"Gmail not fount!",Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.ll_interface)
    public void clickInterface() {
        openFragment(new InterfaceLanguageFragment());
    }

    @OnClick(R.id.ll_terms)
    public void clickTerms() {
        rlTerms.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.fl_bg)
    public void exitLayout() {
        rlTerms.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.tv_privacy_policy__btn)
    public void clickPrivacy() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Settings.PRIVACY_POLICY)));
    }

    @OnClick(R.id.tv_terms_of_use__btn)
    public void clickTermsUse() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Settings.TERMS_OF_USE)));
    }

    @OnClick(R.id.ll_web)
    public void clickWeb() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Settings.WEB_PORTAL)));
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_settings, fragment);
        fragmentTransaction.commit(); // save the changes
    }
}
