package me.custodio.Veever.activity;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.os.RemoteException;

import androidx.annotation.Nullable;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.franmontiel.localechanger.LocaleChanger;
import com.franmontiel.localechanger.utils.ActivityRecreationHelper;
import com.google.firebase.firestore.GeoPoint;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.CompositeMultiplePermissionsListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.multi.SnackbarOnAnyDeniedMultiplePermissionsListener;

import me.custodio.Veever.Events.FetchBeaconSuccessEvent;
import me.custodio.Veever.R;
import me.custodio.Veever.manager.GPSManager;
import me.custodio.Veever.manager.PopupManager;
import me.custodio.Veever.manager.FirestoreManager;
import me.custodio.Veever.manager.Settings;
import me.custodio.Veever.manager.TextToSpeechManager;
import me.custodio.Veever.manager.VeeverSensorManager;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.fabric.sdk.android.Fabric;
import me.custodio.Veever.views.ColorLottieView;

/**
 * Created by Andrews on 17,May,2019
 */

public class MainActivity extends AppCompatActivity implements BeaconConsumer {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_ENABLE_BLUETOOTH = 2;

    private static String BASE_URL = "https://api.veever.experio.com.br/";

    @BindView(R.id.tv_veever)
    ImageView textViewVeever;

    @BindView(R.id.tb_activate)
    ImageButton imageButtonActivate;

    @BindView(R.id.ib_settings)
    ImageButton imageButtonSettings;

    @BindView(R.id.animation_view)
    ColorLottieView lottieView;

    @BindView(R.id.tv_user_direction)
    TextView textViewUserDirection;

    @BindView(R.id.tv_veever_status)
    TextView textViewVeeverStatus;

    boolean isActivated = false;

    public PopupManager popupManager;

    public Handler updateBeaconsHandler;
    public Handler updateOrirentationHander;

   // private RegionBootstrap regionBootstrap;
    private BeaconManager beaconManager;
    private LocationManager locationManager;

    public List<Beacon> stableBeaconList;
    public Collection<Beacon> beaconCollection;

    private Location lastUserLocation;

    private boolean isGPSon;
    private boolean shownDialog;

    private Resources res;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);

        res = getResources();
        fetchFromFirestore();
        setUpLottie();

        locationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.setEnableScheduledScanJobs(false);

        updateOrirentationHander = new Handler();
        popupManager = new PopupManager(this);

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

    public void fetchFromFirestore() {
        FirestoreManager.getInstance().fetchConfigs();
        FirestoreManager.getInstance().fetchBeaconsAndSpots();
    }

    public void setUpLottie() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            Log.e(TAG, "onCreate: Device under low performance");
            lottieView.setAnimation("veever_animation_lowspec.json");
        } else {
            Log.e(TAG, "onCreate: Device under high performance");
            lottieView.setAnimation("veever_animation.json");
        }

        lottieView.setSpeed(0.5f);
        lottieView.updateColor(res.getColor(R.color.veeverwhite));
        lottieView.playAnimation();
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
        TextToSpeechManager.getInstance().setLanguage(Settings.DEFAULT_LOCALE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //enableUserLocationUpdate();
        ActivityRecreationHelper.onResume(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        disableMonitoring();
        //disableLocationUpdate();
        TextToSpeechManager.getInstance().stopSpeech();
    }

    @Override
    protected void onDestroy() {
        ActivityRecreationHelper.onDestroy(this);
        VeeverSensorManager.getInstance().unRegister();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GPSManager.GPS_REQUEST) {
            if (resultCode == RESULT_OK) {
                isGPSon = true;
            }
        } else if (requestCode == REQUEST_ENABLE_BLUETOOTH) {
            if (resultCode == RESULT_OK) {
                //enableGPS();
            }
        }
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
        }, 2000);
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

        popupManager.updatePopup(stableBeaconList, false);
    }

    public void setupUIEnabled() {
        lottieView.setSpeed(1.0f);
        lottieView.updateColor(res.getColor(R.color.lime1));
        textViewUserDirection.setTextColor(getResources().getColor(R.color.lime2));
        textViewVeever.setImageResource(R.drawable.rir_veever_on);
        textViewVeeverStatus.setTextColor(getResources().getColor(R.color.lime2));
        textViewVeeverStatus.setText(res.getString(R.string.app_main_activated));
        imageButtonActivate.setImageResource(R.drawable.rir_button_eye_on);
        imageButtonSettings.setImageResource(R.drawable.setting_on);
        isActivated = true;
        popupManager.enablePopup();
    }

    public void setupUIDisabled() {
        lottieView.setSpeed(0.5f);
        lottieView.updateColor(res.getColor(R.color.veeverwhite));
        textViewUserDirection.setTextColor(getResources().getColor(R.color.veeverwhite));
        textViewVeever.setImageResource(R.drawable.rir_veever_off);
        textViewVeeverStatus.setTextColor(getResources().getColor(R.color.veeverwhite));
        textViewVeeverStatus.setText(res.getString(R.string.app_main_initialised));
        imageButtonActivate.setImageResource(R.drawable.rir_button_eye_off);
        imageButtonSettings.setImageResource(R.drawable.setting_off);
        isActivated = false;
        popupManager.disable();
    }

    public void enableBluetoothAndGps() {
        final BluetoothAdapter bAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bAdapter == null) {
            Log.e(TAG, "onPermissionsChecked: bluetooth service not available");
        } else {
            if (!bAdapter.isEnabled()) {
                startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), REQUEST_ENABLE_BLUETOOTH);
                Log.e(TAG, "onPermissionsChecked: bluetooth turned ON");
            } else {
                //enableGPS();
            }
        }
    }

    ////////////////////
    ///// OnClick
    //////////////////

    @OnClick(R.id.animation_view)
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

        TextToSpeechManager.getInstance().setLanguage(Settings.DEFAULT_LOCALE);

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
                    enableMonitoring();
                    enableBluetoothAndGps();

                    //BeaconModel beaconModel = FirestoreManager.getInstance().getBeaconModel("554AE4E9-546B-44CC-B9BB-A43BE1EA9F81", 44714, 36182);

                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) { }
        };

        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.BLUETOOTH_ADMIN)
                .withListener(new CompositeMultiplePermissionsListener(multiplePermissionsListener, snackbarListener))
                .check();
    }

    //////////////////////////////
    /////// EVENTS
    //////////////////////////////

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(FetchBeaconSuccessEvent event) {
        Log.d(TAG, "onMessageEvent() called with: event = [" + event + "]");
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

    public GeoPoint getUserGeoLocation() {

        if (lastUserLocation == null) {
            return new GeoPoint(0.0,0.0);
        }

        return new GeoPoint(lastUserLocation.getLatitude(), lastUserLocation.getLongitude());
    }

    public void enableUserLocationUpdate() {
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    10000,
                    10, locationListenerGPS);
        } catch (SecurityException ev) {
            ev.printStackTrace();
        }
    }

    public void disableLocationUpdate() {
        locationManager.removeUpdates(locationListenerGPS);
    }

    public void enableGPS() {
        GPSManager gpsManager = new GPSManager(this);
        gpsManager.turnGPSOn(new GPSManager.OnGpsListener() {
            @Override
            public void gpsStatus(boolean isGPSEnable) {
                isGPSon = isGPSEnable;
            }
        });
    }

    LocationListener locationListenerGPS = new LocationListener() {
        @Override
        public void onLocationChanged(android.location.Location location) {
            if (location == null) {
                Log.e(TAG, "onLocationChanged: location null");
                return;
            }

            lastUserLocation = location;
            Toast.makeText(MainActivity.this, "updated user location", Toast.LENGTH_SHORT).show();
            //Log.e(TAG, "onLocationChanged: updated user location latitude: " + location.getLatitude());
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) { }
        @Override
        public void onProviderEnabled(String provider) { }
        @Override
        public void onProviderDisabled(String provider) { }

    };
}
