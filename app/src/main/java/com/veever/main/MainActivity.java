package com.veever.main;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;

import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements BootstrapNotifier , BeaconConsumer {

    private static final String TAG = "MainActivity";

    @BindView(R.id.tv_veever)
    TextView textViewVeever;

    @BindView(R.id.tb_activate)
    ImageButton imageButtonActivate;

    @BindView(R.id.tv_user_direction)
    TextView textViewUserDirection;

    @BindView(R.id.tv_veever_status)
    TextView textViewVeeverStatus;

    boolean isActivated = false;

    private RegionBootstrap regionBootstrap;
    private BackgroundPowerSaver backgroundPowerSaver;
    private BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        // setting beacon regions
        Log.d(TAG, "setting up background monitoring for beacons and power saving");
        // wake up the app when a beacon is seen
        Region region = new Region("backgroundRegion",
                null, null, null);
        regionBootstrap = new RegionBootstrap(this, region);
        backgroundPowerSaver = new BackgroundPowerSaver(this);
        beaconManager.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    ////////////////////
    ///// OnClick
    //////////////////

    @OnClick(R.id.tb_activate)
    public void activate() {
        if (isActivated) {
            deactivateVeever();
            return;
        }

        activateVeever();
    }

    public void activateVeever() {

        textViewUserDirection.setTextColor(getResources().getColor(R.color.lime2));
        textViewVeever.setTextColor(getResources().getColor(R.color.lime2));
        textViewVeeverStatus.setTextColor(getResources().getColor(R.color.lime2));
        imageButtonActivate.setImageResource(R.drawable.button_eye_on);

        startBeaconMonitoring();

        isActivated = true;
    }

    public void deactivateVeever() {

        textViewUserDirection.setTextColor(getResources().getColor(R.color.veeverwhite));
        textViewVeever.setTextColor(getResources().getColor(R.color.veeverwhite));
        textViewVeeverStatus.setTextColor(getResources().getColor(R.color.veeverwhite));
        imageButtonActivate.setImageResource(R.drawable.button_eye_off);

        disableMonitoring();

        isActivated = false;
    }

    public void startBeaconMonitoring() {

        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.BLUETOOTH_ADMIN)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        Log.d(TAG, "onPermissionsChecked() called with: report = [" + report + "]");
                        enableBluetooth();
                        enableMonitoring();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                    }
                })
                .check();
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

    public void disableMonitoring() {
        if (regionBootstrap != null) {
            regionBootstrap.disable();
            regionBootstrap = null;
        }
    }

    public void enableMonitoring() {
        Region region = new Region("backgroundRegion",
                null, null, null);
        regionBootstrap = new RegionBootstrap(this, region);
    }

    //////////////////
    /// BEACON OVERRIDE FUNC
    //////////////////

    @Override
    public void didEnterRegion(Region region) {

    }

    @Override
    public void didExitRegion(Region region) {

    }

    @Override
    public void didDetermineStateForRegion(int i, Region region) {

    }

    @Override
    public void onBeaconServiceConnect() {
        RangeNotifier rangeNotifier = new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    Log.d(TAG, "didRangeBeaconsInRegion called with beacon count:  "+beacons.size());
                    Beacon firstBeacon = beacons.iterator().next();
                    Log.e(TAG, "didRangeBeaconsInRegion: " + firstBeacon.toString());
                   // logToDisplay("The first beacon " + firstBeacon.toString() + " is about " + firstBeacon.getDistance() + " meters away.");
                }
            }

        };
        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
            beaconManager.addRangeNotifier(rangeNotifier);
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
            beaconManager.addRangeNotifier(rangeNotifier);
        } catch (RemoteException e) {   }
    }
}
