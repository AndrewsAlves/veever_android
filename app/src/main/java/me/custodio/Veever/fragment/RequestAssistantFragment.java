package me.custodio.Veever.fragment;


import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.GeoPoint;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.CompositePermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.karumi.dexter.listener.single.SnackbarOnDeniedPermissionListener;

import java.util.Locale;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.custodio.Veever.R;
import me.custodio.Veever.activity.RequestAssistantActiivty;
import me.custodio.Veever.manager.FirestoreManager;
import me.custodio.Veever.manager.Settings;
import me.custodio.Veever.manager.TextToSpeechManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestAssistantFragment extends Fragment {

    @BindView(R.id.tv_random_word)
    TextView tvRandomWord;

    String random = " ";

    public RequestAssistantFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_request_assistant_fragment, container, false);
        ButterKnife.bind(this, view);

        setRandom();

        return view;
    }

    @OnClick(R.id.btn_request_assistance)
    public void requestAssitance() {
        PermissionListener snackbarListener = SnackbarOnDeniedPermissionListener.Builder
                .with(getActivity().findViewById(android.R.id.content), R.string.app_permission_location)
                .withOpenSettingsButton(R.string.app_permission_settings)
                .build();

        PermissionListener singlePermissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                Intent intent  = new Intent(getActivity(), RequestAssistantActiivty.class);
                intent.putExtra("safe_word", random);
                startActivity(intent);
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) { }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) { }
        };

        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new CompositePermissionListener(singlePermissionListener, snackbarListener))
                .check();

    }

    @OnClick(R.id.ib_back__request_assistance)
    public void back() {
        getActivity().getSupportFragmentManager().popBackStack();
    }

    public void setRandom() {

        if (FirestoreManager.getInstance().configs == null) {
            return;
        }

        Random r = new Random();
        int listSize = FirestoreManager.getInstance().configs.safeWords.size();
        int randowItem = r.nextInt(listSize);

        if (Settings.DEFAULT_LOCALE.equals(Locale.US)) {
            random = FirestoreManager.getInstance().configs.safeWords.get("enUS").get(randowItem);
        } else {
            random = FirestoreManager.getInstance().configs.safeWords.get("ptBR").get(randowItem);
        }

        tvRandomWord.setText(random);
    }

}
