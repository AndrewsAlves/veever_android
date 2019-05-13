package com.veever.main;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.veever.main.manager.DatabaseManager;

import java.util.StringTokenizer;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class BeaconDialogFragment extends Fragment {

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

    public static BeaconDialogFragment newInstance(String title, String subtitle, String direction) {
        BeaconDialogFragment dialogType = new BeaconDialogFragment();

        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("subtitle", subtitle);
        args.putString("direction", direction);
        dialogType.setArguments(args);

        return dialogType;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dialog, container, false);
        ButterKnife.bind(this,v);

        if (getArguments() != null) {
            String title = getArguments().getString("title");
            String subtitle = getArguments().getString("subtitle");
            String direction = getArguments().getString("direction");

            textViewDirection.setText(direction);
            textViewMainTitle.setText(title);
            textViewSubtitle.setText(subtitle);

           // Log.e(TAG, "onCreateView: uuid: " + uuid + " major: " + major + " minor: " + minor);
            //DatabaseManager.getInstance().getSpotFromRealm(uuid,major,minor);
        }

        return v;
    }

}
