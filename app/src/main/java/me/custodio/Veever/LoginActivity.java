package me.custodio.Veever;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.custodio.Veever.Events.UserSignUpFailureEvent;
import me.custodio.Veever.Events.UserSignUpSuccesEvent;
import me.custodio.Veever.datamodel.User;
import me.custodio.Veever.manager.FirestoreManager;
import me.custodio.Veever.manager.SharedPrefsManager;
import me.custodio.Veever.views.IndeterminantProgressBar;

public class LoginActivity extends AppCompatActivity {


    @BindView(R.id.progressBar_guest)
    IndeterminantProgressBar progressBarGuest;

    @BindView(R.id.ll_guest_btn_content)
    LinearLayout parentGuestContent;

    @BindView(R.id.progressBar_facebook)
    IndeterminantProgressBar progressBarFacebook;

    @BindView(R.id.ll_faceboook_btn_content)
    LinearLayout parentFacebookContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @OnClick(R.id.ll_btn_login_guest)
    public void clickGuest() {

        progressBarGuest.setVisibility(View.VISIBLE);
        parentGuestContent.setVisibility(View.GONE);

        String newUserId = UUID.randomUUID().toString();
        SharedPrefsManager.saveUserId(this, newUserId);

        User user = new User(newUserId);
        user.setCreatedBy(newUserId);

        if(BuildConfig.DEBUG) {
            user.setFirstName("Andy Developer");
        }

        FirestoreManager.getInstance().createNewUser(user);
    }

    @OnClick(R.id.ll_btn_login_facebook)
    public void clickFacebook() {

        progressBarFacebook.setVisibility(View.VISIBLE);
        parentFacebookContent.setVisibility(View.GONE);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UserSignUpSuccesEvent event) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UserSignUpFailureEvent event) {
        progressBarGuest.setVisibility(View.GONE);
        parentGuestContent.setVisibility(View.VISIBLE);
        progressBarFacebook.setVisibility(View.GONE);
        parentFacebookContent.setVisibility(View.VISIBLE);
    }
}
