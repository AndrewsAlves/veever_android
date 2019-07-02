package me.custodio.Veever;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.custodio.Veever.R;
import com.franmontiel.localechanger.LocaleChanger;
import com.franmontiel.localechanger.utils.ActivityRecreationHelper;
import me.custodio.Veever.Events.ChangeLanguageEvent;
import me.custodio.Veever.fragment.DemontrationFragment;
import me.custodio.Veever.fragment.InterfaceLanguageFragment;
import me.custodio.Veever.fragment.SpeechRateFragment;
import me.custodio.Veever.manager.Settings;
import me.custodio.Veever.manager.TextToSpeechManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends AppCompatActivity {

    @BindView(R.id.ll_speech)
    LinearLayout llSpeech;
    @BindView(R.id.ll_demostration)
    LinearLayout llDemostration;
    @BindView(R.id.ll_email)
    LinearLayout llEmail;
    @BindView(R.id.ll_interface)
    LinearLayout llInterface;
    @BindView(R.id.ll_terms)
    LinearLayout llTerms;
    @BindView(R.id.ll_web)
    LinearLayout llWeb;

    @BindView(R.id.rl_terms)
    RelativeLayout rlTerms;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        newBase = LocaleChanger.configureBaseContext(newBase);
        super.attachBaseContext(newBase);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ActivityRecreationHelper.onResume(this);
    }

    @Override
    protected void onDestroy() {
        ActivityRecreationHelper.onDestroy(this);
        EventBus.getDefault().unregister(this);
        super.onDestroy();

    }

    @Override
    public void onBackPressed() {

        if (rlTerms.getVisibility() == View.VISIBLE) {
            rlTerms.setVisibility(View.GONE);
            return;
        }

        super.onBackPressed();
        TextToSpeechManager.getInstance().stopSpeech();
    }

    @OnClick(R.id.ll_speech)
    public void clickSpeech() {
        openFragment(new SpeechRateFragment());
    }

    @OnClick(R.id.ll_demostration)
    public void clickDemonstration() {
        openFragment(new DemontrationFragment());
    }

    @OnClick(R.id.ll_email)
    public void clickEmail() {

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setDataAndType(Uri.parse("contato@veever.com.br"), "text/html");
        final PackageManager pm = this.getPackageManager();
        final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
        String className = null;
        for (final ResolveInfo info : matches) {
            if (info.activityInfo.packageName.equals("com.google.android.gm")) {
                className = info.activityInfo.name;

                if(className != null && !className.isEmpty()){
                    break;
                }
            }
        }

        if (className == null) {
            return;
        }

        emailIntent.setClassName("com.google.android.gm", className);

        try{
            startActivity(emailIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this,"Gmail not fount!",Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.ll_interface)
    public void clickInterface() {
        openFragment(new InterfaceLanguageFragment());
    }

    @OnClick(R.id.ll_terms)
    public void clickTerms() {
        rlTerms.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.fl_bg)
    public void exitLayout() {
        rlTerms.setVisibility(View.GONE);
    }

    @OnClick(R.id.tv_privacy_policy__btn)
    public void clickPrivacy() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Settings.PRIVACY_POLICY)));
    }

    @OnClick(R.id.tv_terms_of_use__btn)
    public void clickTermsUse() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Settings.TERMS_OF_USE)));
    }

    @OnClick(R.id.ll_web)
    public void clickWeb() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Settings.WEB_PORTAL)));
    }

    @OnClick(R.id.ib_quit_settings__btn)
    public void exit() {
        onBackPressed();
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_settings, fragment);
        fragmentTransaction.addToBackStack("settings");
        fragmentTransaction.commit(); // save the changes
    }

    @Subscribe
    public void onMessageEvent(ChangeLanguageEvent event) {
        LocaleChanger.setLocale(event.locale);
        //setLanguage(event.locale);
        TextToSpeechManager.getInstance().setLanguage(event.locale);
        ActivityRecreationHelper.recreate(this, true);
    }
}
