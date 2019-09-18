package me.custodio.Veever.manager;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.firestore.FirebaseFirestore;
import org.greenrobot.eventbus.EventBus;

import me.custodio.Veever.Events.UserSignUpFailureEvent;
import me.custodio.Veever.Events.UserSignUpSuccesEvent;
import me.custodio.Veever.datamodel.User;

/**
 * Created by Andrews on 18,September,2019
 */
public class FirestoreManager {

    public static final String TAG = "FirestoreManager";

    public static final String DB_USER = "users";

    private static FirestoreManager ourInstance;

    public String userId;

    FirebaseFirestore firestore;

    public static FirestoreManager getInstance() {
        return ourInstance;
    }

    private FirestoreManager(Context context) {
        firestore =  FirebaseFirestore.getInstance();
        userId = null;
    }

    public static void intialize(Context context) {
        ourInstance = new FirestoreManager(context);
    }

    public void createNewUser(User user) {
        firestore.collection(DB_USER).document()
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        EventBus.getDefault().post(new UserSignUpSuccesEvent());
                        Log.d(TAG, "DocumentSnapshot : Account successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        EventBus.getDefault().post(new UserSignUpFailureEvent());
                        Log.w(TAG, "Error Creating document", e);
                    }
                });
    }

}
