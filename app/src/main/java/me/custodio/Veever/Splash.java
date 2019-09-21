package me.custodio.Veever;

import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import me.custodio.Veever.Events.FetchUserFailureEvent;
import me.custodio.Veever.Events.FetchUserSuccessEvent;
import me.custodio.Veever.activity.LoginActivity;
import me.custodio.Veever.activity.MainActivity;
import me.custodio.Veever.manager.FirestoreManager;
import me.custodio.Veever.manager.SharedPrefsManager;
import me.custodio.Veever.manager.Utils;


/**
 * Created by Andrews on 17,May,2019
 */

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        EventBus.getDefault().register(this);

        Handler openMain = new Handler();

        openMain.postDelayed(new Runnable() {
            @Override
            public void run() {
                openDesiredActivity();
            }
        }, 2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void openDesiredActivity() {

        String userId = SharedPrefsManager.getUserId(this);

        if (userId != null) {
            FirestoreManager.getInstance().userId = userId;
            FirestoreManager.getInstance().documentID = SharedPrefsManager.getUserDocumentId(this);
            FirestoreManager.getInstance().fetchUser();
            return;
        }

        Intent intent = new Intent(Splash.this, LoginActivity.class);
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(FetchUserSuccessEvent event) {
        Intent intent =  new Intent(Splash.this, MainActivity.class);
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(FetchUserFailureEvent event) {
        if (!Utils.isNetworkConnected(this)) {
           finish();
        }
    }
}
