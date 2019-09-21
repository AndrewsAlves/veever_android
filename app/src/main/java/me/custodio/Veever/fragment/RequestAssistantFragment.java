package me.custodio.Veever.fragment;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        Intent intent  = new Intent(getActivity(), RequestAssistantActiivty.class);
        intent.putExtra("safe_word", random);
        startActivity(intent);
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
