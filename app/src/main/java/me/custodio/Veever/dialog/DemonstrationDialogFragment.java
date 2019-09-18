package me.custodio.Veever.dialog;


import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import butterknife.ButterKnife;
import me.custodio.Veever.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DemonstrationDialogFragment extends DialogFragment {

    public DemonstrationDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_demonstration_dialog, container, false);
        ButterKnife.bind(this,v);

        // set background transparent
       // if (getDialog() != null && getDialog().getWindow() != null) {
       //     getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
       //     getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
       // }

        // Inflate the layout for this fragment
        return v;
    }


}
