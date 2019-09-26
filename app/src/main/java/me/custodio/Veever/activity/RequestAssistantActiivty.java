package me.custodio.Veever.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.franmontiel.localechanger.LocaleChanger;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.GeoPoint;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.custodio.Veever.Events.AskHelpSuccessEvent;
import me.custodio.Veever.R;
import me.custodio.Veever.manager.GPSManager;
import me.custodio.Veever.manager.TextToSpeechManager;
import me.custodio.Veever.manager.FirestoreManager;
import me.custodio.Veever.views.ColorLottieView;

public class RequestAssistantActiivty extends AppCompatActivity {

    public static final String TAG = "RequestAssistance";
    private static final int LOCATION_SETTINGS_REQUEST = 1;

    @BindView(R.id.lottie_request_assistance)
    ColorLottieView lottieAnimationView;

    @BindView(R.id.tv_random_word)
    TextView tvRandomWord;

    private LocationManager locationManager;
    private FusedLocationProviderClient fusedLocationClient;

    String safeWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_assistant_actiivty);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        lottieAnimationView.updateColor(getResources().getColor(R.color.veeverblack));

        if (getIntent() != null) {
            safeWord = getIntent().getExtras().getString("safe_word");
            tvRandomWord.setText(safeWord);
        }

        locationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        checkGPSAndGetLocation();

        TextToSpeechManager.getInstance().speak(getString(R.string.voice_requestassistance));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(locationListenerGPS);
        EventBus.getDefault().unregister(this);
        FirestoreManager.getInstance().askHelpAndUpdateLocation(null, null, false);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        newBase = LocaleChanger.configureBaseContext(newBase);
        super.attachBaseContext(newBase);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    5000,
                    0, locationListenerGPS);
        } catch (SecurityException ev) {
            ev.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GPSManager.GPS_REQUEST) {
            if (resultCode == RESULT_OK) {
                // Call find location in 3 seconds when GPS is turned on
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        RequestAssistantActiivty.this.getLocationThroughClient();
                    }
                }, 6000);

                Log.e(TAG, "onActivityResult: GPS enabled true");
            }
        }
    }

    LocationListener locationListenerGPS = new LocationListener() {
        @Override
        public void onLocationChanged(android.location.Location location) {
            if (location == null) {
                Log.e(TAG, "onLocationChanged: location null");
                return;
            }

            GeoPoint geoPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
            FirestoreManager.getInstance().askHelpAndUpdateLocation(safeWord, geoPoint, true);
            Toast.makeText(RequestAssistantActiivty.this, " Updated User location",Toast.LENGTH_SHORT).show();
            //Log.e(TAG, "onLocationChanged: updated user location latitude: " + location.getLatitude());
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) { }
        @Override
        public void onProviderEnabled(String provider) { }
        @Override
        public void onProviderDisabled(String provider) { }

    };

    public void checkGPSAndGetLocation() {
        locationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            requestGPS();
            Log.e(TAG, "is GPS enabled: false");
        } else {
            getLocationThroughClient();
            Log.e(TAG, "is GPS enabled: true");
        }
    }

    public void requestGPS() {
        GPSManager gpsManager = new GPSManager(this);
        gpsManager.turnGPSOn(new GPSManager.OnGpsListener() {
            @Override
            public void gpsStatus(boolean isGPSEnable) {
                if (isGPSEnable) {
                    getLocationThroughClient();
                }
            }
        });
    }

    public void getLocationThroughClient() {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            GeoPoint geoPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
                            Toast.makeText(RequestAssistantActiivty.this, " Fetched User location",Toast.LENGTH_SHORT).show();
                            FirestoreManager.getInstance().askHelpAndUpdateLocation(safeWord, geoPoint, true);
                            Log.e(TAG, "onSuccess: Location is not null");
                        } else {
                            Log.e(TAG, "onSuccess: Location is null");
                        }
                    }
                });
    }

    @OnClick(R.id.btn_cancel_request)
    public void clickCancelRequest() {
        FirestoreManager.getInstance().askHelpAndUpdateLocation(null, null, false);
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(AskHelpSuccessEvent event) {
        Log.d(TAG, "onMessageEvent() called with: event = [" + event + "]");
    }

    //////////
    /// DISABLED FUNCTION
    /////////

     /*public Location getLocation() {

        Location location = null;

        try {
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        } catch (SecurityException ec) {
            ec.printStackTrace();
        }

        return location;
    }

    private Location getLastKnownLocation() {
        locationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = null;

            try {
                l = locationManager.getLastKnownLocation(provider);
            } catch (SecurityException ex) {
                ex.printStackTrace();
            }

            if (l == null) {
                continue;
            }

            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }

        return bestLocation;
    }*/
}
