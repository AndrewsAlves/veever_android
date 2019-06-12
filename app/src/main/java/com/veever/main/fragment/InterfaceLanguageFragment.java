package com.veever.main.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.veever.main.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class InterfaceLanguageFragment extends Fragment {

    @BindView(R.id.ll_portuguese__btn)
    LinearLayout btnPortugese;

    @BindView(R.id.ll_english__btn)
    LinearLayout btnEnglish;

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
        getActivity().getSupportFragmentManager().popBackStack();
    }

    @OnClick(R.id.ll_portuguese__btn)
    public void clickPortugese() {
        btnPortugese.setBackgroundResource(R.drawable.bg_lime_gradient_border);
        btnEnglish.setBackgroundResource(0);
    }

    @OnClick(R.id.ll_english__btn)
    public void clickEnglish() {
        btnPortugese.setBackgroundResource(0);
        btnEnglish.setBackgroundResource(R.drawable.bg_lime_gradient_border);
    }
}
