package com.veever.main.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.veever.main.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class InterfaceLanguageFragment extends Fragment {


    public InterfaceLanguageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_interface_language, container, false);
        ButterKnife.bind(this,v);


        return v;
    }

    @OnClick(R.id.ib_back__interface_language)
    public void back() {

    }
}
