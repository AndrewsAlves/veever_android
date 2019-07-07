package me.custodio.Veever.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.franmontiel.localechanger.LocaleChanger;
import me.custodio.Veever.Events.ChangeLanguageEvent;
import me.custodio.Veever.R;
import me.custodio.Veever.SettingsActivity;
import me.custodio.Veever.manager.Settings;

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

        if (LocaleChanger.getLocale().getLanguage().equals(Settings.LOCALE_PORTUGUESE.getLanguage())) {
            setUiPortuguese();
        } else {
            setUiEnglish();
        }

        return v;
    }

    @OnClick(R.id.ib_back__interface_language)
    public void back() {
        getActivity().getSupportFragmentManager().popBackStack();
    }

    public void setUiEnglish() {
        btnPortugese.setBackgroundResource(0);
        btnEnglish.setBackgroundResource(R.drawable.bg_lime_gradient_border);
    }

    public void setUiPortuguese() {
        btnPortugese.setBackgroundResource(R.drawable.bg_lime_gradient_border);
        btnEnglish.setBackgroundResource(0);
    }

    @OnClick(R.id.ll_portuguese__btn)
    public void clickPortugese() {
        setUiPortuguese();
        Settings.saveLanguage(getContext(), Settings.PORTUGUESE);
        EventBus.getDefault().post(new ChangeLanguageEvent(Settings.LOCALE_PORTUGUESE));
    }

    @OnClick(R.id.ll_english__btn)
    public void clickEnglish() {
        setUiEnglish();
        Settings.saveLanguage(getContext(), Settings.ENGLISH);
        EventBus.getDefault().post(new ChangeLanguageEvent(Locale.US));
    }
}
