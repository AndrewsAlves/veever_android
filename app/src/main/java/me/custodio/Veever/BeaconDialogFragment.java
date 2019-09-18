package me.custodio.Veever;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Andrews on 17,May,2019
 */

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

        }

        return v;
    }

}
