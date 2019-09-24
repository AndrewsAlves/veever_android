package me.custodio.Veever.fragment.dialog;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.custodio.Veever.R;

/**
 * Created by Andrews on 17,May,2019
 */

/**
 * A simple {@link Fragment} subclass.
 */
public class PopUpFragment extends Fragment {

    private static final String TAG = "BeaconDialog";

    @BindView(R.id.tv_user_direction)
    TextView textViewDirection;

    @BindView(R.id.tv_main_title)
    TextView textViewMainTitle;

    @BindView(R.id.tv_subtitle)
    TextView textViewSubtitle;

    public String title;
    public String description;
    public String direction;

    public PopUpFragment() {
        // Required empty public constructor
    }

    public static PopUpFragment newInstance(String title, String subtitle, String direction) {
        PopUpFragment dialogType = new PopUpFragment();

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
             title = getArguments().getString("title");
             description = getArguments().getString("subtitle");
             direction = getArguments().getString("direction");

             updateView();
        }

        return v;
    }

    public void updateView() {
        textViewDirection.setText(direction);
        textViewMainTitle.setText(title);
        textViewSubtitle.setText(description);
    }

}
