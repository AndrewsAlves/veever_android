package me.custodio.Veever;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookRequestError;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
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

public class LoginActivity extends AppCompatActivity implements FacebookCallback<LoginResult> {

    private static final String TAG = "LoginActivity";

    private static final String EMAIL = "email";

    @BindView(R.id.progressBar_guest)
    IndeterminantProgressBar progressBarGuest;

    @BindView(R.id.ll_guest_btn_content)
    LinearLayout parentGuestContent;

    @BindView(R.id.progressBar_facebook)
    IndeterminantProgressBar progressBarFacebook;

    @BindView(R.id.ll_faceboook_btn_content)
    LinearLayout parentFacebookContent;

    @BindView(R.id.facebook_login_button)
    LoginButton fbGhostBotton;

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        callbackManager = CallbackManager.Factory.create();
        fbGhostBotton.setReadPermissions(Arrays.asList(EMAIL));

        LoginManager.getInstance().registerCallback(callbackManager, this);

    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSuccess(LoginResult o) {
        requestUserProfile(o);
        Log.d(TAG, "onSuccess() called with: o = [" + o + "]");
    }

    @Override
    public void onCancel() {
        Log.d(TAG, "onCancel() called");
    }

    @Override
    public void onError(FacebookException error) {
        Log.d(TAG, "onError() called with: error = [" + error + "]");
    }

    public void requestUserProfile(LoginResult result) {

        GraphRequest request = GraphRequest.newMeRequest(
                result.getAccessToken(),
                (object, response) -> {

                    Log.e(TAG, "onCompleted: graphResponce: " + response.toString());

                    try {

                        progressBarFacebook.setVisibility(View.VISIBLE);
                        parentFacebookContent.setVisibility(View.GONE);

                        String email = object.getString("email");
                        String id = object.getString("id");
                        String firstName = object.getString("first_name");
                        String lastName = object.getString("last_name");

                        User user = new User(object.getString("id"));
                        user.setEmail(email);
                        user.setFirstName(firstName);
                        user.setLastName(lastName);
                        user.setUserId(id);

                        SharedPrefsManager.saveUserId(this, id);
                        FirestoreManager.getInstance().createNewUser(user);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,first_name,last_name,email");
        request.setParameters(parameters);
        request.executeAsync();
        request.executeAsync();

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
        fbGhostBotton.performClick();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UserSignUpSuccesEvent event) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UserSignUpFailureEvent event) {
        progressBarGuest.setVisibility(View.GONE);
        parentGuestContent.setVisibility(View.VISIBLE);
        progressBarFacebook.setVisibility(View.GONE);
        parentFacebookContent.setVisibility(View.VISIBLE);
    }
}
