package com.veever.main;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.veever.main.manager.DatabaseManager;

import java.util.StringTokenizer;
import java.util.UUID;

import butterknife.BindView;


/**
 * A simple {@link Fragment} subclass.
 */
public class BeaconDialogFragment extends DialogFragment {

    private static final String TAG = "BeaconDialog";
    @BindView(R.id.tv_user_direction)
    TextView textViewDirection;

    @BindView(R.id.tv_main_title)
    TextView textViewMainTitle;

    @BindView(R.id.tv_subtitle)
    TextView textViewSubtitle;

    public BeaconDialogFragment() {
        // Required empty public constructor
    }

    public static BeaconDialogFragment newInstance(String beaconInfo) {
        BeaconDialogFragment dialogType = new BeaconDialogFragment();

        Bundle args = new Bundle();
        args.putString("beacon_info", beaconInfo);
        dialogType.setArguments(args);

        return dialogType;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dialog, container, false);

        if (getArguments() != null) {
            String beaconInfo = getArguments().getString("beacon_info");

            String[] separated = beaconInfo.split(":");
            String uuid = separated[1];
            String major = separated[2];
            String minor = separated[3];

            Log.e(TAG, "onCreateView: uuid: " + uuid + " major: " + major + " minor: " + minor);
            //DatabaseManager.getInstance().getSpotFromRealm(uuid,major,minor);
        }

        return v;
    }

}
