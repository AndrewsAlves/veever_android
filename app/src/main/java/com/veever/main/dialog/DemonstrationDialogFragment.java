package com.veever.main.dialog;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;

import com.veever.main.Events.OnDemoContinueEvent;
import com.veever.main.R;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
