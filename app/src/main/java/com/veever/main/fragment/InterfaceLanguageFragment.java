package com.veever.main.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.akexorcist.localizationactivity.ui.LocalizationActivity;
import com.veever.main.Events.ChangeLanguageEvent;
import com.veever.main.R;
import com.veever.main.SettingsActivity;
import com.veever.main.manager.Settings;

import org.greenrobot.eventbus.EventBus;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class InterfaceLanguageFragment extends Fragment {

    @BindView(R.id.ll_portuguese__btn)
    LinearLayout btnPortugese;

    @BindView(R.id.ll_english__btn)
    LinearLayout btnEnglish;

    SettingsActivity settingsActivity;

    public InterfaceLanguageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_interface_language, container, false);
        ButterKnife.bind(this,v);
        settingsActivity = (SettingsActivity) getActivity();

        if (settingsActivity.getCurrentLanguage().getLanguage().equals(Settings.LOCALE_PORTUGUESE.getLanguage())) {
            clickPortugese();
        } else {
            clickEnglish();
        }

        return v;
    }

    @OnClick(R.id.ib_back__interface_language)
    public void back() {
        getActivity().getSupportFragmentManager().popBackStack();
    }

    @OnClick(R.id.ll_portuguese__btn)
    public void clickPortugese() {
        btnPortugese.setBackgroundResource(R.drawable.bg_lime_gradient_border);
        btnEnglish.setBackgroundResource(0);
        EventBus.getDefault().post(new ChangeLanguageEvent(new Locale("pt", "POR")));
    }

    @OnClick(R.id.ll_english__btn)
    public void clickEnglish() {
        btnPortugese.setBackgroundResource(0);
        btnEnglish.setBackgroundResource(R.drawable.bg_lime_gradient_border);
        EventBus.getDefault().post(new ChangeLanguageEvent(Locale.US));
    }
}
