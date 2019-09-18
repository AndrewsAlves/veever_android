package me.custodio.Veever;

import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import me.custodio.Veever.manager.FirestoreManager;
import me.custodio.Veever.manager.SharedPrefsManager;


/**
 * Created by Andrews on 17,May,2019
 */

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler openMain = new Handler();

        openMain.postDelayed(new Runnable() {
            @Override
            public void run() {
                openDesiredActivity();
            }
        }, 2000);

    }

    public void openDesiredActivity() {

        Intent intent = new Intent(Splash.this, LoginActivity.class);

        String userId = SharedPrefsManager.getUserId(this);
        if (userId != null) {
            FirestoreManager.getInstance().userId = userId;
            intent =  new Intent(Splash.this, MainActivity.class);
        }

        startActivity(intent);
    }
}
