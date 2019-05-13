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
import android.support.v4.app.Fragment;
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
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.veever.main.datamodel.Spot;
import com.veever.main.manager.DatabaseManager;

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
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements BootstrapNotifier , BeaconConsumer {

    private static final String TAG = "MainActivity";

    @BindView(R.id.tv_veever)
    ImageView textViewVeever;

    @BindView(R.id.tb_activate)
    ImageButton imageButtonActivate;

    @BindView(R.id.tv_user_direction)
    TextView textViewUserDirection;

    @BindView(R.id.tv_veever_status)
    TextView textViewVeeverStatus;

    boolean isActivated = false;

    public static String lastShownBeacon = " ";

   // private RegionBootstrap regionBootstrap;
    private BackgroundPowerSaver backgroundPowerSaver;
    private BeaconManager beaconManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        beaconManager = BeaconManager.getInstanceForApplication(this);

       // beaconManager.bind(this);
        // setting beacon regions
        //Log.d(TAG, "setting up background monitoring for beacons and power saving");
        // wake up the app when a beacon is seen
        //Region region = new Region("backgroundRegion",
        //        null, null, null);
        //regionBootstrap = new RegionBootstrap(this, region);
        //backgroundPowerSaver = new BackgroundPowerSaver(this);

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

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

        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.BLUETOOTH_ADMIN)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        Log.e(TAG, "onPermissionsChecked() called with: report = [" + report + "]");
                        enableBluetooth();
                        enableMonitoring();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) { }
                })
                .check();
    }

    public void disableMonitoring() {
  //      if (regionBootstrap != null) {
  //         regionBootstrap.disable();
   //         regionBootstrap = null;
 //       }
        setupUIDisabled();
        beaconManager.unbind(this);
        //beaconManager = null;
        lastShownBeacon = " ";
    }

    public void enableMonitoring() {
    //    Region region = new Region("backgroundRegion",
   ///             null, null, null);
     //   regionBootstrap = new RegionBootstrap(this, region);
        setupUIEnabled();
        lastShownBeacon = " ";
        beaconManager.bind(this);
    }

    public void setupUIEnabled() {

        textViewUserDirection.setTextColor(getResources().getColor(R.color.lime2));
        textViewVeever.setImageResource(R.drawable.veever_on);
        textViewVeeverStatus.setTextColor(getResources().getColor(R.color.lime2));
        textViewVeeverStatus.setText("Activated");
        imageButtonActivate.setImageResource(R.drawable.button_eye_on);
        isActivated = true;
    }

    public void setupUIDisabled() {

        textViewUserDirection.setTextColor(getResources().getColor(R.color.veeverwhite));
        textViewVeever.setImageResource(R.drawable.veever_off);
        textViewVeeverStatus.setTextColor(getResources().getColor(R.color.veeverwhite));
        textViewVeeverStatus.setText("Initialised");
        imageButtonActivate.setImageResource(R.drawable.button_eye_off);
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

        beaconManager.removeAllRangeNotifiers();
        RangeNotifier rangeNotifier = new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    Log.d(TAG, "detected beacon count: "+beacons.size());

                    Beacon firstBeacon = beacons.iterator().next();

                    if (lastShownBeacon.equals(firstBeacon.toString())) {
                        return;
                    }

                    showDialog(firstBeacon);
                    lastShownBeacon = firstBeacon.toString();

                    Log.e(TAG, "didRangeBeaconsInRegion: " + firstBeacon.toString());
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

    public void showDialog(Beacon beacon) {
        if (!isActivated) {
            return;
        }

        Log.e(TAG, "showDialog: " + beacon.getId1().toString() + " " + beacon.getId2().toInt() + " " + beacon.getId3().toInt());

        com.veever.main.datamodel.Beacon beacon1 = DatabaseManager.getInstance().getBeacon(
                beacon.getId1().toString(),
                beacon.getId2().toInt(),
                beacon.getId3().toInt());

        for (com.veever.main.datamodel.Beacon beacon2 : DatabaseManager.getInstance().getBeaconList()) {
           // Log.e(TAG, "showDialog: " + beacon2.uuid + " " + beacon2.major + " " + beacon2.minor);
        }

        if (beacon1 == null) {
            Log.e(TAG, "showDialog: beacon null");
            return;
        }

        Spot spot = DatabaseManager.getInstance().getSpot(beacon1.spotid);

        if (spot == null) {
            Log.e(TAG, "showDialog: spot null");
            return;
        }

        loadFragment(spot);
        Log.e(TAG, "showDialog: " + beacon.getId1().toString() + " " + beacon.getId2().toInt() + " " + beacon.getId3().toInt());

    }

    private void loadFragment(Spot spot) {
        BeaconDialogFragment newFragment = BeaconDialogFragment.newInstance(spot.spotName,spot.spotDescription,spot.zoneLocation);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_dialog_fragment, newFragment);
        fragmentTransaction.commit(); // save the changes
    }

}
