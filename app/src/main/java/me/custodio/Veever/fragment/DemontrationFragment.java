package me.custodio.Veever.fragment;


import android.app.Activity;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import me.custodio.Veever.Events.UpdateDemoBeaconEvent;
import me.custodio.Veever.enums.GeoDirections;
import me.custodio.Veever.R;
import me.custodio.Veever.manager.FirestoreManager;
import me.custodio.Veever.model.OrientationInfo;
import me.custodio.Veever.model.Spot;
import me.custodio.Veever.manager.Settings;
import me.custodio.Veever.manager.TextToSpeechManager;
import me.custodio.Veever.manager.VeeverSensorManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.custodio.Veever.model.SpotInfo;

import static android.view.View.GONE;

/**
 * A simple {@link Fragment} subclass.
 */
public class DemontrationFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {

    private static final String TAG = "Demonstration fragment";
    @BindView(R.id.switch_demo)
    SwitchCompat switchDemo;

    @BindView(R.id.tv_text_center)
    TextView textViewCenter;

    @BindView(R.id.et_shortcode)
    EditText EditTextShortCode;

    @BindView(R.id.cl_demo_dialog)
    ConstraintLayout dialogView;

    ///// BEACON DIALOG /////

    @BindView(R.id.dialog_beacon)
    ConstraintLayout dialogBeacon;

    @BindView(R.id.tv_user_direction)
    TextView textViewDirection;

    @BindView(R.id.tv_main_title)
    TextView textViewMainTitle;

    @BindView(R.id.tv_subtitle)
    TextView textViewSubtitle;

    Spot spot;

    public DemontrationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_demontration, container, false);

        ButterKnife.bind(this,v);
        switchDemo.setOnCheckedChangeListener(this);
        switchDemo.setChecked(false);

        EventBus.getDefault().register(this);

        return v;
    }

    @Override
    public void onDestroyView() {
        TextToSpeechManager.getInstance().stopSpeech();
        VeeverSensorManager.getInstance().setDemo(false);
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(EditTextShortCode.getWindowToken(), 0);
    }

    public void updateBeaconDialog(boolean readSpotDetails) {
        GeoDirections geoDirections = VeeverSensorManager.getInstance().getGeoDirection();
        SpotInfo spotInfo = spot.getSpotInfo();

        if (spotInfo == null) {
            return;
        }

        String title = spotInfo.name;
        String description = getString(R.string.app_dialog_description_center);
        String direction = VeeverSensorManager.getInstance().getDirectionText(getContext());

        OrientationInfo orientationInfo = spotInfo.getDirectionInfo(geoDirections);

        if (orientationInfo != null) {
            description = orientationInfo.title;
        }

        textViewMainTitle.setText(title);
        textViewSubtitle.setText(description);
        textViewDirection.setText(direction);

        switch (spot.getDefaultLanguageType()) {
            case ENGLISH:
                TextToSpeechManager.getInstance().setLanguage(Settings.LOCALE_ENGLISH);
            case PORTUGUESE:
                TextToSpeechManager.getInstance().setLanguage(Settings.LOCALE_PORTUGUESE);
        }

        String speechText = spotInfo.getDirectionInfo(geoDirections).voiceTitle;

        if (readSpotDetails) {
            speechText = spotInfo.voiceName + spotInfo.voiceDescription;
            TextToSpeechManager.getInstance().speak(speechText, true);
            return;
        }

        TextToSpeechManager.getInstance().speak(speechText);
    }

    @OnClick(R.id.ib_back__demonstration)
    public void back() {
        getActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (isChecked) {
            dialogView.setVisibility(View.VISIBLE);
            textViewCenter.setVisibility(GONE);
        } else {
            VeeverSensorManager.getInstance().setDemo(false);
            dialogBeacon.setVisibility(GONE);
            textViewCenter.setVisibility(View.VISIBLE);
            TextToSpeechManager.getInstance().stopSpeech();
        }
    }

    @OnClick(R.id.tv_cancel__btn)
    public void clickCancel() {
        dialogView.setVisibility(GONE);
        switchDemo.setChecked(false);
        hideKeyboard();
    }

    @OnClick(R.id.tv_continue__btn)
    public void clickContinue() {

        dialogView.setVisibility(GONE);
        dialogBeacon.setVisibility(View.VISIBLE);

        hideKeyboard(); // hide keyboard so view wont get squised

        String shortCode = "VEEVER";

        if (!EditTextShortCode.getText().toString().isEmpty()) {
            shortCode = EditTextShortCode.getText().toString().toUpperCase();
        }

        spot = FirestoreManager.getInstance().getSpotByShortId(shortCode);

        if (spot == null) {
            spot = FirestoreManager.getInstance().getSpotByShortId("VEEVER");
        }

        if (spot != null) {
            VeeverSensorManager.getInstance().setDemo(true);
            updateBeaconDialog(true);
        }
    }

    @OnClick(R.id.topview)
    public void clickTopGreen() {
        if (spot == null) {
            return;
        }

        String speechText = spot.getSpotInfo().voiceName + spot.getSpotInfo().voiceDescription;
        TextToSpeechManager.getInstance().speak(speechText);
    }

    @OnClick(R.id.tv_subtitle)
    public void clickMiddleWhite() {
        if (spot.getSpotInfo() == null) {
            return;
        }

        String speechText = spot.getSpotInfo().getDirectionInfo(VeeverSensorManager.getInstance().getGeoDirection()).voiceTitle;
        TextToSpeechManager.getInstance().speak(speechText);
    }

    @OnClick(R.id.bottomView)
    public void clickBottomGreen() {
        String speechText = VeeverSensorManager.getInstance().getDirectionText(getContext());
        TextToSpeechManager.getInstance().speak(speechText);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UpdateDemoBeaconEvent event) {
        updateBeaconDialog(false);
    }
}
