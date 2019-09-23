package me.custodio.Veever.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.GeoPoint;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.CompositePermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.karumi.dexter.listener.single.SnackbarOnDeniedPermissionListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.security.Provider;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.custodio.Veever.Events.AskHelpSuccessEvent;
import me.custodio.Veever.R;
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

        TextToSpeechManager.getInstance().speak(" Our Assistant will reach you in a moment Please stay where you are!");

        lottieAnimationView.updateColor(getResources().getColor(R.color.veeverblack));
        locationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);

        if (getIntent() != null) {
            safeWord = getIntent().getExtras().getString("safe_word");
            tvRandomWord.setText(safeWord);
        }

        checkGPSAndGetLocation();
    }

    public void checkGPSAndGetLocation() {
        locationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            requestGPS();
            Log.e(TAG, "is GPS enabled: false");
        } else {
            getUserLocation();
            Log.e(TAG, "is GPS enabled: true");
        }
    }

    public void requestGPS() {

        LocationRequest mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)
                .setFastestInterval(1 * 1000);

        LocationSettingsRequest.Builder settingsBuilder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        settingsBuilder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(this)
                .checkLocationSettings(settingsBuilder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response =
                            task.getResult(ApiException.class);
                } catch (ApiException ex) {
                    switch (ex.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                ResolvableApiException resolvableApiException =
                                        (ResolvableApiException) ex;
                                resolvableApiException
                                        .startResolutionForResult(RequestAssistantActiivty.this,
                                                LOCATION_SETTINGS_REQUEST);
                            } catch (IntentSender.SendIntentException e) {

                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:

                            break;
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        FirestoreManager.getInstance().askHelpAndUpdateLocation(null, null, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    2000,
                    10, locationListenerGPS);
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
        if (requestCode == LOCATION_SETTINGS_REQUEST) {
            if (resultCode == RESULT_OK) {
                // Call find location in 3 seconds when GPS is turned on
                new Handler().postDelayed(() -> getUserLocation(), 6000);
                Log.e(TAG, "onActivityResult: GPS enabled true");
            }
        }
    }

    @OnClick(R.id.btn_cancel_request)
    public void clickCancelRequest() {
        FirestoreManager.getInstance().askHelpAndUpdateLocation(null, null, false);
        finish();
    }

    public void getUserLocation() {
        Location lastKnownLocation = getLocation();
        if (lastKnownLocation != null) {
            GeoPoint geoPoint = new GeoPoint(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
            Toast.makeText(RequestAssistantActiivty.this, " Fetched User location",Toast.LENGTH_SHORT).show();
            FirestoreManager.getInstance().askHelpAndUpdateLocation(safeWord, geoPoint, true);
        } else {
            Log.e(TAG, "onPermissionsChecked: location null");
        }
    }

    public Location getLocation() {

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
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(AskHelpSuccessEvent event) {
        Log.d(TAG, "onMessageEvent() called with: event = [" + event + "]");
    }

    LocationListener locationListenerGPS =new LocationListener() {
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
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };
}
