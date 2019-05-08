package com.veever.main;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements BootstrapNotifier {

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
    private boolean haveDetectedBeaconsSinceBoot = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        BeaconManager beaconManager = org.altbeacon.beacon.BeaconManager.getInstanceForApplication(this);
        BeaconManager.setDebug(true);

        // Uncomment the code below to use a foreground service to scan for beacons. This unlocks
        // the ability to continually scan for long periods of time in the background on Andorid 8+
        // in exchange for showing an icon at the top of the screen and a always-on notification to
        // communicate to users that your app is using resources in the background.
        //


        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setContentTitle("Scanning for Beacons");
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT
        );
        builder.setContentIntent(pendingIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("My Notification Channel ID",
                    "My Notification Name", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("My Notification Channel Description");
            NotificationManager notificationManager = (NotificationManager) getSystemService(
                    Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId(channel.getId());
        }
        beaconManager.enableForegroundServiceScanning(builder.build(), 456);
        // For the above foreground scanning service to be useful, you need to disable
        // JobScheduler-based scans (used on Android 8+) and set a fast background scan
        // cycle that would otherwise be disallowed by the operating system.
        //
        beaconManager.setEnableScheduledScanJobs(false);
        beaconManager.setBackgroundBetweenScanPeriod(0);
        beaconManager.setBackgroundScanPeriod(1100);


        /*Log.d(TAG, "setting up background monitoring for beacons and power saving");
        // wake up the app when a beacon is seen
        Region region = new Region("backgroundRegion",
                null, null, null);
        regionBootstrap = new RegionBootstrap(this, region);

        // simply constructing this class and holding a reference to it in your custom Application
        // class will automatically cause the BeaconLibrary to save battery whenever the application
        // is not visible.  This reduces bluetooth power usage by about 60%
        backgroundPowerSaver = new BackgroundPowerSaver(this);

        // If you wish to test beacon detection in the Android Emulator, you can use code like this:
        // BeaconManager.setBeaconSimulator(new TimedBeaconSimulator() );
        // ((TimedBeaconSimulator) BeaconManager.getBeaconSimulator()).createTimedSimulatedBeacons();*/


    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
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

    @Override
    public void didEnterRegion(Region arg0) {
        // In this example, this class sends a notification to the user whenever a Beacon
        // matching a Region (defined above) are first seen.
        Log.d(TAG, "did enter region.");
        if (!haveDetectedBeaconsSinceBoot) {
            Log.d(TAG, "auto launching MainActivity");

            // The very first time since boot that we detect an beacon, we launch the
            // MainActivity
           // Intent intent = new Intent(this, MonitoringActivity.class);
         //   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Log.d(TAG, "didEnterRegion() called with: arg0 = [" + arg0 + "]");
            // Important:  make sure to add android:launchMode="singleInstance" in the manifest
            // to keep multiple copies of this activity from getting created if the user has
            // already manually launched the app.
            //this.startActivity(intent);
            haveDetectedBeaconsSinceBoot = true;
        } else {
          /*  if (monitoringActivity != null) {
                // If the Monitoring Activity is visible, we log info about the beacons we have
                // seen on its display
                logToDisplay("I see a beacon again" );
            } else {
                // If we have already seen beacons before, but the monitoring activity is not in
                // the foreground, we send a notification to the user on subsequent detections.
                Log.d(TAG, "Sending notification.");
                sendNotification();
            }*/
        }


    }

    @Override
    public void didExitRegion(Region region) {
        Log.e(TAG, "didExitRegion: I no longer see a beacon.");
    }

    @Override
    public void didDetermineStateForRegion(int state, Region region) {
        Log.d(TAG, "didDetermineStateForRegion() called with: state = [" + state + "], region = [" + region + "]");
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

        isActivated = true;
    }

    public void deactivateVeever() {

        textViewUserDirection.setTextColor(getResources().getColor(R.color.veeverwhite));
        textViewVeever.setTextColor(getResources().getColor(R.color.veeverwhite));
        textViewVeeverStatus.setTextColor(getResources().getColor(R.color.veeverwhite));
        imageButtonActivate.setImageResource(R.drawable.button_eye_off);


        isActivated = false;
    }
}
