package me.custodio.Veever.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.value.LottieFrameInfo;
import com.airbnb.lottie.value.SimpleLottieValueCallback;
import com.google.firebase.firestore.GeoPoint;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.CompositeMultiplePermissionsListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.multi.SnackbarOnAnyDeniedMultiplePermissionsListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.w3c.dom.Text;

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

    @BindView(R.id.lottie_request_assistance)
    ColorLottieView lottieAnimationView;

    @BindView(R.id.tv_random_word)
    TextView tvRandomWord;

    private LocationManager locationManager;

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

        // Get the location manager
        getUserLocation();
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
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @OnClick(R.id.btn_cancel_request)
    public void clickCancelRequest() {
        FirestoreManager.getInstance().askHelpAndUpdateLocation(null, null, false);
        finish();
    }

    public void getUserLocation() {

        MultiplePermissionsListener snackbarListener = SnackbarOnAnyDeniedMultiplePermissionsListener.Builder
                        .with(findViewById(android.R.id.content), R.string.app_permission_location)
                        .withOpenSettingsButton(R.string.app_permission_settings)
                        .build();

        MultiplePermissionsListener singlePermissionListener = new MultiplePermissionsListener() {

            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                Location lastKnownLocation = getLastKnownLocation();
                if (lastKnownLocation != null) {
                    GeoPoint geoPoint = new GeoPoint(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                    FirestoreManager.getInstance().askHelpAndUpdateLocation(safeWord, geoPoint, true);
                } else {
                    Log.e(TAG, "onPermissionsChecked: location null");
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) { }
        };

        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                .withListener(new CompositeMultiplePermissionsListener(singlePermissionListener, snackbarListener))
                .check();
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
        TextToSpeechManager.getInstance().speak("Random word is " + safeWord + " Our Assistant will reach you in a moment Please stay where you are!");
    }
}
