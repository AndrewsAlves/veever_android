package com.veever.main;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.RemoteException;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.CompositeMultiplePermissionsListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.multi.SnackbarOnAnyDeniedMultiplePermissionsListener;

import com.kuassivi.component.RipplePulseRelativeLayout;
import com.veever.main.datamodel.OrientationInfo;
import com.veever.main.datamodel.Spot;
import com.veever.main.manager.DatabaseManager;
import com.veever.main.manager.VeeverSensorManager;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Andrews on 17,May,2019
 */

public class MainActivity extends AppCompatActivity implements BeaconConsumer {

    private static final String TAG = "MainActivity";

    @BindView(R.id.tv_veever)
    ImageView textViewVeever;

    @BindView(R.id.tb_activate)
    ImageButton imageButtonActivate;

   // @BindView(R.id.ib_settings)
   // ImageButton imageButtonSettings;

    @BindView(R.id.pulseLayout)
    RipplePulseRelativeLayout pulsatorLayout;
    @BindView(R.id.pulseLayout1)
    RipplePulseRelativeLayout pulsatorLayout1;

    @BindView(R.id.tv_user_direction)
    TextView textViewUserDirection;

    @BindView(R.id.tv_veever_status)
    TextView textViewVeeverStatus;

    boolean isActivated = false;

    public static String lastShownBeacon = " ";

    public Handler handleDialog;
    public Handler updateBeaconsHandler;
    public Handler updateOrirentationHander;

   // private RegionBootstrap regionBootstrap;
    private BackgroundPowerSaver backgroundPowerSaver;
    private BeaconManager beaconManager;

    public List<Beacon> stableBeaconList;
    public Collection<Beacon> beaconCollection;

    private String lastGeoDirection = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.setEnableScheduledScanJobs(false);

        handleDialog = new Handler();
        updateOrirentationHander = new Handler();

        stableBeaconList = new ArrayList<>();

        TextToSpeechManager.getInstance().speak("Veever Initialised. Tap on the upper area of the screen to activate");

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
    protected void onStart() {
        super.onStart();
        VeeverSensorManager.getInstance().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        VeeverSensorManager.getInstance().unRegister();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void enableMonitoring() {
        setupUIEnabled();
        beaconManager.bind(this);
        startUpdatingBeacons();
    }

    public void disableMonitoring() {
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
        pulsatorLayout.startPulse();
        pulsatorLayout1.startPulse();
        textViewUserDirection.setTextColor(getResources().getColor(R.color.lime2));
        textViewVeever.setImageResource(R.drawable.veever_on);
        textViewVeeverStatus.setTextColor(getResources().getColor(R.color.lime2));
        textViewVeeverStatus.setText("ACTIVATED");
        imageButtonActivate.setImageResource(R.drawable.button_eye_on);
       // imageButtonSettings.setImageResource(R.drawable.setting_on);
        isActivated = true;
    }

    public void setupUIDisabled() {
        pulsatorLayout.stopPulse();
        pulsatorLayout1.stopPulse();
        textViewUserDirection.setTextColor(getResources().getColor(R.color.veeverwhite));
        textViewVeever.setImageResource(R.drawable.veever_off);
        textViewVeeverStatus.setTextColor(getResources().getColor(R.color.veeverwhite));
        textViewVeeverStatus.setText("INITIALISED");
        imageButtonActivate.setImageResource(R.drawable.button_eye_off);
      //  imageButtonSettings.setImageResource(R.drawable.setting_off);
        isActivated = false;
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

    @OnClick(R.id.tb_activate)
    public void activate() {
        if (isActivated) {
            disableMonitoring();
            return;
        }
        startBeaconMonitoring();
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

        TextToSpeechManager.getInstance().speak("Veever Activated. Follow the directions");

        beaconManager.removeAllRangeNotifiers();
        RangeNotifier rangeNotifier = new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {

                Log.d(TAG, "didRangeBeaconsInRegion() called with: beacons = [" + beacons + "], region = [" + region + "]");

                //for (Beacon beacon : beacons) {
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
        TextToSpeechManager.getInstance().speak("Veever Deactivated");
    }

    public void showDialog() {

        if (!isActivated) {
            return;
        }

        Log.d(TAG, "showDialog() called");

        if (stableBeaconList.size() == 0) {
            return;
        }

        Beacon beacon = getClosestBeacon();
        com.veever.main.datamodel.Beacon beacon1 = DatabaseManager.getInstance().getBeacon(
                beacon.getId1().toString(),
                beacon.getId2().toInt(),
                beacon.getId3().toInt());

        if (beacon1 == null) {
            Log.e(TAG, "showDialog: beacon null");
            return;
        }

        Spot spot = DatabaseManager.getInstance().getSpot(beacon1.spotid);

        if (spot == null) {
            Log.e(TAG, "showDialog: spot null");
            return;
        }

        String title = "LEWLARA/TBWA";
        String description = "There is no point of interests mapped in this direction";
        String direction = VeeverSensorManager.getInstance().getDirectionText();
        GeoDirections geoDirection = VeeverSensorManager.getInstance().getGeoDirection();

        if (lastGeoDirection.equals(direction)) {
            return;
        }

        if (spot.getDirectionInfo(geoDirection) != null) {
            OrientationInfo directionInfo = spot.getDirectionInfo(geoDirection);
            title = spot.spotName;
            description = directionInfo.description;
        }

        loadFragment(title, description, direction);
        lastGeoDirection = direction;
        updateOrientationInfo();

        TextToSpeechManager.getInstance().speak(spot.spotTitle + description + direction);
    }

    private void loadFragment(String title, String description, String direction) {
        handleDialog.removeCallbacksAndMessages(null);
        BeaconDialogFragment newFragment = BeaconDialogFragment.newInstance(title, description, direction);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.fade_in,R.anim.fade_out);
        fragmentTransaction.replace(R.id.frame_dialog_fragment, newFragment);
        fragmentTransaction.commit(); // save the changes
        removeDialog();
    }

    public void removeDialog() {
        handleDialog.postDelayed(new Runnable() {
            @Override
            public void run() {
                getSupportFragmentManager().beginTransaction().
                        remove(getSupportFragmentManager()
                                .findFragmentById(R.id.frame_dialog_fragment))
                        .setCustomAnimations(R.anim.fade_out,R.anim.fade_in)
                        .commit();
            }
        },3000);
    }

    public void updateOrientationInfo() {
        updateOrirentationHander.postDelayed(new Runnable() {
            @Override
            public void run() {
                lastGeoDirection = " ";
            }
        }, 8000);
    }

    public Beacon getClosestBeacon() {

        Beacon closetBeacon = null;

        if (stableBeaconList.size() == 1) {
            return stableBeaconList.get(0);
        }

        for (Beacon beacon : stableBeaconList) {
            for (Beacon beacon1 : stableBeaconList) {
                if (beacon.getDistance() < beacon1.getDistance()) {
                    closetBeacon = beacon;
                }
            }
        }

        return closetBeacon;
    }
}
