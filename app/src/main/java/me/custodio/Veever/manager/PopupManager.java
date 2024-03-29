package me.custodio.Veever.manager;

import android.os.Handler;
import android.util.Log;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.altbeacon.beacon.Beacon;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import me.custodio.Veever.R;
import me.custodio.Veever.activity.MainActivity;
import me.custodio.Veever.enums.GeoDirections;
import me.custodio.Veever.fragment.dialog.PopUpFragment;
import me.custodio.Veever.model.BeaconModel;
import me.custodio.Veever.model.OrientationInfo;
import me.custodio.Veever.model.Spot;
import me.custodio.Veever.model.SpotInfo;

public class PopupManager {

    private static final String TAG = "PopupManager";

    private static final double IMMEDIATE = 0.2000;
    private static final double NEAR = 3.000;
    private static final double FAR = 10.0000;

    MainActivity mainActivity;
    PopUpFragment popUpFragment;
    BeaconModel lastbeaconModel;
    List<Beacon> beaconList;

    private boolean isActivated;
    private boolean isPopupShown;

    private Handler handleDialog;

    public PopupManager(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void enablePopup() {
        this.isActivated = true;
    }

    public void disable() {
        this.isActivated = false;
        removeDialog();
    }

    public void updatePopup(List<Beacon> beaconsList, boolean updateForOrientation) {

        this.beaconList = beaconsList;

        if (!isActivated) {
            return;
        }

        if (beaconsList.size() == 0) {
            return;
        }

        Beacon beacon = getDetectedBeacons();

        GeoDirections geoDirection = VeeverSensorManager.getInstance().getGeoDirection();

        // beacon in null because no beacon in range
        if (beacon == null) {
            removeDialog();
            Log.e(TAG, "showDialog: no beacon is eligible for showing");
            return;
        }

        // if not update last beacon
        BeaconModel beaconModel = FirestoreManager.getInstance().getBeaconModel(beacon);

        if (beaconModel == null) {
            Log.e(TAG, "showDialog: beacon null");
            return;
        }

        Spot spot = FirestoreManager.getInstance().getSpot(beaconModel);

        if (spot == null) {
            Log.e(TAG, "showDialog: spot null");
            return;
        }

        SpotInfo spotInfo = spot.getSpotInfo();

        if (spotInfo == null) {
            Log.e(TAG, "showDialog: spot info null");
            return;
        }

        if (lastbeaconModel != null){
            if (lastbeaconModel.getUuid().equals(beaconModel.getUuid()) && !updateForOrientation && isPopupShown) {
                return;
            }
        }

        String popupTitle = spotInfo.name;
        String popUpDescription = mainActivity.getString(R.string.app_dialog_description_center);

        String speakTop = spotInfo.voiceName + spotInfo.voiceDescription;
        String speakMiddle = mainActivity.getString(R.string.app_dialog_description_center);
        String speakBottom = VeeverSensorManager.getInstance().getDirectionText(mainActivity.getBaseContext());

        String voiceTitle = " ";

        OrientationInfo orientationInfo = spot.getSpotInfo().getDirectionInfo(geoDirection);

        if (orientationInfo != null) {
            popUpDescription = orientationInfo.title;
            voiceTitle = orientationInfo.voiceTitle;
            speakMiddle = orientationInfo.voiceTitle;
        }

        // update fragment
        updatePopupFragment(popupTitle, popUpDescription, speakTop, speakMiddle, speakBottom);
        FirestoreManager.getInstance().writeHeats(beaconModel, spot, spot.getGeoLocation());

        //SPEAK

        switch (spot.getDefaultLanguageType()) {
            case ENGLISH:
                TextToSpeechManager.getInstance().setLanguage(Settings.LOCALE_ENGLISH);
                break;
            case PORTUGUESE:
                TextToSpeechManager.getInstance().setLanguage(Settings.LOCALE_PORTUGUESE);
                break;
        }

        String speakSentence = voiceTitle;

        // when new beacon is detected we should speak this
        if (lastbeaconModel == null) {
            speakSentence = spotInfo.voiceName + spotInfo.voiceDescription;

            lastbeaconModel = beaconModel;
            TextToSpeechManager.getInstance().speak(speakSentence, true);

            return;

        } else if (!lastbeaconModel.getUuid().equals(beaconModel.getUuid())) {
            speakSentence = spotInfo.voiceName + spotInfo.voiceDescription;

            lastbeaconModel = beaconModel;
            TextToSpeechManager.getInstance().speak(speakSentence, true);

            return;
        }

        // when changing orientation we speak this
        lastbeaconModel = beaconModel;
        TextToSpeechManager.getInstance().speak(speakSentence);
    }

    public Beacon getDetectedBeacons() {
        List<Beacon> showBeaconList = new ArrayList<>();

        for (Beacon beacon : beaconList) {

            BeaconModel beaconModel = FirestoreManager.getInstance().getBeaconModel(beacon);

            if (beaconModel == null) {
                Log.e(TAG, "getDetectedBeacons: beacon model is null");
                continue;
            }

            if (beaconModel.getRangingDistance().equals(Settings.FAR)) {
                showBeaconList.add(beacon);
            } else if (beaconModel.getRangingDistance().equals(Settings.NEAR)
                    && !getBeaconRanging(beacon.getDistance()).equals(Settings.FAR)) {
                showBeaconList.add(beacon);
            } else if (beaconModel.getRangingDistance().equals(Settings.IMMEDIATE)
                    && getBeaconRanging(beacon.getDistance()).equals(Settings.IMMEDIATE)) {
                showBeaconList.add(beacon);
            }

            /*if (getBeaconRanging(beacon.getDistance()).equals(beaconModel.getRangingDistance())) {
                Log.e(TAG, "getDetectedBeacons: added beacon as per distance");
                showBeaconList.add(beacon);
            }*/
        }

        return getClosestBeacon(showBeaconList);
    }

    public Beacon getClosestBeacon(List<Beacon> beaconList) {

        Beacon closetBeacon = null;

        if (beaconList.size() == 1) {
            return beaconList.get(0);
        }

        for (Beacon beacon : beaconList) {
            for (Beacon beacon1 : beaconList) {
                if (beacon.getDistance() < beacon1.getDistance()) {
                    closetBeacon = beacon;
                }
            }
        }

        return closetBeacon;
    }

    public String getBeaconRanging(double distance) {

        Log.e(TAG, "getBeaconRanging: beacon distance: " + distance);

        if (distance < IMMEDIATE) {
            return Settings.IMMEDIATE;
        } else if (distance < NEAR && distance > IMMEDIATE) {
            return Settings.NEAR;
        } else if (distance > NEAR) {
            return Settings.FAR;
        }

        return null;
    }

    /////////////////////////////
    ///// SHOW & HIDE DIALOG
    /////////////////////////////

    private void updatePopupFragment(String title, String description, String speakTop, String speakMiddle, String speakBottom) {

        if (popUpFragment != null) {
            popUpFragment.title = title;
            popUpFragment.description = description;
            popUpFragment.direction = VeeverSensorManager.getInstance().getDirectionText(mainActivity.getBaseContext());
            popUpFragment.speakTop = speakTop;
            popUpFragment.speakMiddle = speakMiddle;
            popUpFragment.speakBottom = speakBottom;
            popUpFragment.updateView();
            return;
        }

        popUpFragment = PopUpFragment.newInstance(title, description, VeeverSensorManager.getInstance().getDirectionText(mainActivity.getBaseContext()));
        FragmentTransaction fragmentTransaction = mainActivity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(0,0);
        fragmentTransaction.replace(R.id.frame_dialog_fragment, popUpFragment);
        fragmentTransaction.commitAllowingStateLoss(); // save the changes

        isPopupShown = true;
    }

    private void removeDialog() {
        try {
            FragmentManager fragmentManager = mainActivity.getSupportFragmentManager();

            if (fragmentManager.findFragmentById(R.id.frame_dialog_fragment) != null) {

                fragmentManager.beginTransaction().
                        remove(fragmentManager.findFragmentById(R.id.frame_dialog_fragment))
                        .commitAllowingStateLoss();

                isPopupShown = false;
                popUpFragment = null;

            }

        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

}
