package me.custodio.Veever;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.os.Handler;
import android.os.RemoteException;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.franmontiel.localechanger.LocaleChanger;
import com.franmontiel.localechanger.utils.ActivityRecreationHelper;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.CompositeMultiplePermissionsListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.multi.SnackbarOnAnyDeniedMultiplePermissionsListener;

import com.kuassivi.component.RipplePulseRelativeLayout;
import me.custodio.Veever.datamodel.BeaconModel;
import me.custodio.Veever.datamodel.OrientationInfo;
import me.custodio.Veever.datamodel.Spot;
import me.custodio.Veever.manager.DatabaseManager;
import me.custodio.Veever.manager.Settings;
import me.custodio.Veever.manager.TextToSpeechManager;
import me.custodio.Veever.manager.VeeverSensorManager;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.fabric.sdk.android.Fabric;

/**
 * Created by Andrews on 17,May,2019
 */

public class MainActivity extends AppCompatActivity implements BeaconConsumer {

    private static final String TAG = "MainActivity";

    private static final double IMMEDIATE = 0.2000;
    private static final double NEAR = 3.000;
    private static final double FAR = 10.0000;

    @BindView(R.id.tv_veever)
    ImageView textViewVeever;

    @BindView(R.id.tb_activate)
    ImageButton imageButtonActivate;

    @BindView(R.id.ib_settings)
    ImageButton imageButtonSettings;

    @BindView(R.id.pulseLayout)
    RipplePulseRelativeLayout pulsatorLayout;
    @BindView(R.id.pulseLayout1)
    RipplePulseRelativeLayout pulsatorLayout1;

    @BindView(R.id.tv_user_direction)
    TextView textViewUserDirection;

    @BindView(R.id.tv_veever_status)
    TextView textViewVeeverStatus;

    boolean isActivated = false;

    public Handler handleDialog;
    public Handler updateBeaconsHandler;
    public Handler updateOrirentationHander;

   // private RegionBootstrap regionBootstrap;
    private BeaconManager beaconManager;

    public List<Beacon> stableBeaconList;
    public Collection<Beacon> beaconCollection;

    private String lastGeoDirection = " ";
    private String lastBeaconId = " ";

    private Resources res;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        res = getResources();

        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.setEnableScheduledScanJobs(false);

        handleDialog = new Handler();
        updateOrirentationHander = new Handler();

        stableBeaconList = new ArrayList<>();

        TextToSpeechManager.getInstance().speak(res.getString(R.string.app_main_speak_veeverinitialised));

        //textToSpeak.speak("Tap on the upper area of the screen to activate ");
       // beaconManager.bind(this);
        // setting beacon regions
        //Log.d(TAG, "setting up background monitoring for beacons and power saving");
        // wake up the app when a beacon is seen
        //Region region = new Region("backgroundRegion",
        //        null, null, null);
        //regionBootstrap = new RegionBootstrap(this, region);
        //backgroundPowerSaver = new BackgroundPowerSaver(this);

        // update randing beacons for every 3 seconds
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        newBase = LocaleChanger.configureBaseContext(newBase);
        super.attachBaseContext(newBase);
    }

    @Override
    protected void onStart() {
        super.onStart();
        VeeverSensorManager.getInstance().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ActivityRecreationHelper.onResume(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        disableMonitoring();
        TextToSpeechManager.getInstance().stopSpeech();
    }

    @Override
    protected void onDestroy() {
        ActivityRecreationHelper.onDestroy(this);
        VeeverSensorManager.getInstance().unRegister();
        super.onDestroy();
    }

    public void enableMonitoring() {
        setupUIEnabled();
        beaconManager.bind(this);
        startUpdatingBeacons();
    }

    public void disableMonitoring() {
        handleDialog.removeCallbacksAndMessages(null);
        setupUIDisabled();
        beaconManager.unbind(this);
        stopUpdatingBeacons();
    }

    public void startUpdatingBeacons() {
        updateBeaconsHandler = new Handler();
        updateBeaconsHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateBeaconsList();
            }
        }, 3000);
    }

    public void stopUpdatingBeacons() {
        if (updateBeaconsHandler == null) {
            return;
        }

        updateBeaconsHandler.removeCallbacksAndMessages(null);
    }

    public void updateBeaconsList() {

        if (!isActivated) {
            return;
        }

        startUpdatingBeacons();

        Log.e(TAG, "updateBeaconsList() called");

        if (beaconCollection == null) {
            return;
        }

        stableBeaconList.clear();
        stableBeaconList.addAll(beaconCollection);
    }

    public void setupUIEnabled() {
        pulsatorLayout1.startPulse();
        handleDialog.postDelayed(new Runnable() {
            @Override
            public void run() {
                pulsatorLayout.startPulse();
            }
        },1250);

        textViewUserDirection.setTextColor(getResources().getColor(R.color.lime2));
        textViewVeever.setImageResource(R.drawable.veever_on);
        textViewVeeverStatus.setTextColor(getResources().getColor(R.color.lime2));
        textViewVeeverStatus.setText(res.getString(R.string.app_main_activated));
        imageButtonActivate.setImageResource(R.drawable.button_eye_on);
        imageButtonSettings.setImageResource(R.drawable.setting_on);
        isActivated = true;
    }

    public void setupUIDisabled() {
        pulsatorLayout.stopPulse();
        pulsatorLayout1.stopPulse();
        textViewUserDirection.setTextColor(getResources().getColor(R.color.veeverwhite));
        textViewVeever.setImageResource(R.drawable.veever_off);
        textViewVeeverStatus.setTextColor(getResources().getColor(R.color.veeverwhite));
        textViewVeeverStatus.setText(res.getString(R.string.app_main_initialised));
        imageButtonActivate.setImageResource(R.drawable.button_eye_off);
        imageButtonSettings.setImageResource(R.drawable.setting_off);
        isActivated = false;
        removeDialog();
    }

    public void enableBluetooth() {
        final BluetoothAdapter bAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bAdapter == null) {
            Log.e(TAG, "onPermissionsChecked: bluetooth service not available");
        } else {
            if (!bAdapter.isEnabled()) {
                startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), 1);
                Log.e(TAG, "onPermissionsChecked: bluetooth turned ON");
            }
        }
    }

    ////////////////////
    ///// OnClick
    //////////////////

    @OnClick(R.id.pulseLayout1)
    public void activate() {
        if (isActivated) {
            disableMonitoring();
            return;
        }
        startBeaconMonitoring();
    }

    @OnClick(R.id.ib_settings)
    public void clickSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void startBeaconMonitoring() {

        MultiplePermissionsListener snackbarListener =
                SnackbarOnAnyDeniedMultiplePermissionsListener.Builder
                        .with(findViewById(android.R.id.content), R.string.app_permission_location)
                        .withOpenSettingsButton(R.string.app_permission_settings)
                        .build();

        MultiplePermissionsListener multiplePermissionsListener = new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {

                Log.e(TAG, "onPermissionsChecked() called with: report = [" + report + "]");

                if (report.areAllPermissionsGranted()) {
                    enableBluetooth();
                    enableMonitoring();
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) { }
        };

        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.BLUETOOTH_ADMIN)
                .withListener(new CompositeMultiplePermissionsListener(multiplePermissionsListener, snackbarListener))
                .check();
    }

    //////////////////
    /// BEACON OVERRIDE
    //////////////////

    @Override
    public void onBeaconServiceConnect() {

        Log.e(TAG, "onBeaconServiceConnect() called");

        TextToSpeechManager.getInstance().speak(res.getString(R.string.app_main_speak_veeveractivated));

        beaconManager.removeAllRangeNotifiers();
        RangeNotifier rangeNotifier = new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {

                Log.d(TAG, "didRangeBeaconsInRegion() called with: beacons = [" + beacons + "], region = [" + region + "]");

                //for (BeaconModel beacon : beacons) {
                  //  Log.e(TAG, "didRangeBeaconsInRegion: id: " + beacon.getId1() + " distance: " + beacon.getDistance());
                //}

               beaconCollection = beacons;
            }
        };

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("veever", null, null, null));
            beaconManager.addRangeNotifier(rangeNotifier);
            beaconManager.startRangingBeaconsInRegion(new Region("veever", null, null, null));
            beaconManager.addRangeNotifier(rangeNotifier);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        super.unbindService(conn);
        Log.e(TAG, "unbindService() called with: conn = [" + conn + "]");
        TextToSpeechManager.getInstance().speak(res.getString(R.string.app_main_speak_deactivated));
    }

    public void showDialog() {

        if (!isActivated) {
            return;
        }

        Log.d(TAG, "showDialog() called");

        if (stableBeaconList.size() == 0) {
            return;
        }

        Beacon beacon = getDetectedBeacons();

        if (beacon == null) {
            Log.e(TAG, "showDialog: no beacon is eligible for showing");
            return;
        }

        BeaconModel beaconModel = DatabaseManager.getInstance().getBeacon(
                beacon.getId1().toString(),
                beacon.getId2().toInt(),
                beacon.getId3().toInt());

        if (beaconModel == null) {
            Log.e(TAG, "showDialog: beacon null");
            return;
        }

        Spot spot = Settings.getSpotBasedOnLanguage(this,beaconModel.spotInfo);

        if (spot == null) {
            Log.e(TAG, "showDialog: spot null");
            return;
        }

        String title = spot.spotName;
        String description = getString(R.string.app_dialog_description_center);
        String direction = VeeverSensorManager.getInstance().getDirectionText(getBaseContext());
        GeoDirections geoDirection = VeeverSensorManager.getInstance().getGeoDirection();

        if (lastGeoDirection.equals(direction)) {
            return;
        }

        if (spot.getDirectionInfo(geoDirection) != null) {
            OrientationInfo orientationInfo = spot.getDirectionInfo(geoDirection);
            description = orientationInfo.description;
        }

        loadFragment(title, description, direction);
        lastGeoDirection = direction;
        updateOrientationInfo();

        if (!lastBeaconId.equals(beaconModel.id)) {
            lastBeaconId = beaconModel.id;
            String speakDescription = spot.spotDescription + description;
            TextToSpeechManager.getInstance().speak(spot.spotTitle + speakDescription + direction);
            return;
        }

        TextToSpeechManager.getInstance().speak(spot.spotTitle + description + direction);
    }

    private void loadFragment(String title, String description, String direction) {
        handleDialog.removeCallbacksAndMessages(null);
        BeaconDialogFragment newFragment = BeaconDialogFragment.newInstance(title, description, direction);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(0,0);
        fragmentTransaction.replace(R.id.frame_dialog_fragment, newFragment);
        fragmentTransaction.commitAllowingStateLoss(); // save the changes
        handleDialog.postDelayed(new Runnable() {
            @Override
            public void run() {
               removeDialog();
            }
        },3000);
    }

    public void removeDialog() {
        try {
            if (getSupportFragmentManager()
                    .findFragmentById(R.id.frame_dialog_fragment) != null) {
                getSupportFragmentManager().beginTransaction().
                        remove(getSupportFragmentManager()
                                .findFragmentById(R.id.frame_dialog_fragment))
                        .commitAllowingStateLoss();
            }

        } catch (NullPointerException ex) {

        }
    }

    public void updateOrientationInfo() {
        updateOrirentationHander.postDelayed(new Runnable() {
            @Override
            public void run() {
                lastGeoDirection = " ";
            }
        }, 8000);
    }

    public Beacon getDetectedBeacons() {
        List<Beacon> showBeaconList = new ArrayList<>();

        for (Beacon beacon : stableBeaconList) {

            BeaconModel beaconModel = DatabaseManager.getInstance().getBeacon(
                    beacon.getId1().toString(),
                    beacon.getId2().toInt(),
                    beacon.getId3().toInt());

            if (beaconModel == null) {
                continue;
            }

            if (getBeaconRanging(beacon.getDistance()).equals(beaconModel.rangingDistance)) {
                showBeaconList.add(beacon);
            }
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

        if (distance < IMMEDIATE) {
            return Settings.IMMEDIATE;
        } else if (distance < NEAR && distance > IMMEDIATE) {
            return Settings.NEAR;
        } else if (distance > NEAR) {
            return Settings.FAR;
        }

        return null;
    }
}
