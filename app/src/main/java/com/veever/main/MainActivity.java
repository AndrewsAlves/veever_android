package com.veever.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;
import com.google.android.gms.nearby.messages.Strategy;
import com.google.android.gms.nearby.messages.SubscribeOptions;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    MessageListener beaconListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        beaconListener = new MessageListener() {
            @Override
            public void onFound(Message message) {
                super.onFound(message);
                Log.d(TAG, "Found message: " + new String(message.getContent()));

            }

            @Override
            public void onLost(Message message) {
                super.onLost(message);
            }
        };
    }

    // Subscribe to receive messages.
    private void subscribe() {
        Log.i(TAG, "Subscribing.");
        SubscribeOptions options = new SubscribeOptions.Builder()
                .setStrategy(Strategy.BLE_ONLY)
                .build();
        Nearby.getMessagesClient(this).subscribe(beaconListener, options);
    }

    private void unSubscribe() {
        Nearby.getMessagesClient(this).unsubscribe(beaconListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        subscribe();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unSubscribe();
    }

}
