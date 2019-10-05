package me.custodio.Veever.manager;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import org.altbeacon.beacon.Beacon;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import me.custodio.Veever.Events.AskHelpSuccessEvent;
import me.custodio.Veever.Events.FetchBeaconSuccessEvent;
import me.custodio.Veever.Events.FetchUserFailureEvent;
import me.custodio.Veever.Events.FetchUserSuccessEvent;
import me.custodio.Veever.Events.UserSignUpFailureEvent;
import me.custodio.Veever.Events.UserSignUpSuccesEvent;
import me.custodio.Veever.model.Configs;
import me.custodio.Veever.model.Spot;
import me.custodio.Veever.model.User;
import me.custodio.Veever.model.BeaconModel;
import me.custodio.Veever.model.Heats;

/**
 * Created by Andrews on 18,September,2019
 */
public class FirestoreManager {

    public static final String TAG = "FirestoreManager";

    public static final String DB_USER = "users";
    public static final String DB_CONFIGS = "configs";
    public static final String DB_BEACONS = "beacons";
    public static final String DB_HEATS = "heats";
    public static final String DB_SPOTS = "spots";

    public int iant = 50000224;

    public static final String DOC_CONFIGS = "a1uclGwKQXjOl0cP7gs2";

    private static FirestoreManager ourInstance;

    public User user;
    public String userId;
    public String documentID;
    public Configs configs;

    public List<BeaconModel> beaconModelList;
    public List<Heats> heatsList;
    public List<Spot> spotList;

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

    public BeaconModel getBeaconModel(Beacon beacon) {

        String uuid = beacon.getId1().toString();
        String major = beacon.getId2().toString();
        String minor = beacon.getId3().toString();

        if (beaconModelList == null) {
            return null;
        }

        for (BeaconModel beaconModel : beaconModelList) {
            if (beaconModel != null && beaconModel.getUuid() != null) {
                try {
                    if (beaconModel.getUuid().equals(uuid.toUpperCase())
                            && beaconModel.getMajor() == Integer.parseInt(major)
                            && beaconModel.getMinor() == Integer.parseInt(minor) ) {
                        return beaconModel;
                    }
                } catch (UnsupportedOperationException op) {
                    op.printStackTrace();
                }
            }
        }

        return null;
    }

    public Spot getSpotByShortId(String shordId) {

        if (spotList == null) {
            return null;
        }

        for (Spot spot : spotList) {
            if (spot != null && spot.getShortCode() != null) {
                if (spot.getShortCode().equals(shordId)) {
                    return spot;
                }
            }
        }

        return null;
    }

    public List<BeaconModel> getBeaconModelList() {
        return beaconModelList;
    }

    public List<Spot> getSpotList() {
        return spotList;
    }

    public Spot getSpot(BeaconModel beaconModel) {

        if (beaconModel.getSpot() == null) {
            return null;
        }

        for (Spot spot : spotList) {
            if (spot.documentId.equals(beaconModel.getSpot().getId())) {
                return spot;
            }
        }

        return null;
    }

    ////////////////////////////////
    /////////// READS
    ///////////////////////////////

    public void fetchUser() {
        firestore.collection(DB_USER).document(documentID).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if (documentSnapshot == null) {
                            return;
                        }

                        user = documentSnapshot.toObject(User.class);
                        EventBus.getDefault().post(new FetchUserSuccessEvent());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                        EventBus.getDefault().post(new FetchUserFailureEvent());
                    }
                });
    }

    public void fetchBeaconsAndSpots() {

        beaconModelList = new ArrayList<>();
        spotList = new ArrayList<>();

        firestore.collection(DB_BEACONS)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                            if (documentSnapshot == null) {
                                continue;
                            }

                            beaconModelList.add(documentSnapshot.toObject(BeaconModel.class));
                            Log.e(TAG, "onSuccess: beacon" + beaconModelList.toString());
                        }

                        EventBus.getDefault().post(new FetchBeaconSuccessEvent());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

        firestore.collection(DB_SPOTS)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for (int i = 0; i < queryDocumentSnapshots.size() ; i++) {

                            if (queryDocumentSnapshots.getDocuments().get(i) == null) {
                                continue;
                            }

                            spotList.add(queryDocumentSnapshots.getDocuments().get(i).toObject(Spot.class));
                            spotList.get(i).documentId = queryDocumentSnapshots.getDocuments().get(i).getId();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    public void fetchConfigs() {

        firestore.collection(DB_CONFIGS).document(DOC_CONFIGS)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if (documentSnapshot == null) {
                            return;
                        }

                        Log.d(TAG, "onSuccess() called with: documentSnapshot = [" + documentSnapshot + "]");

                        configs = documentSnapshot.toObject(Configs.class);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
        });
    }

    //////////////////////////////
    /////// WRITES
    //////////////////////////////

    public void createNewUser(Context context,User userModel) {

        documentID = firestore.collection(DB_USER).document().getId();

        firestore.collection(DB_USER).document(documentID)
                .set(userModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        user = userModel;

                        SharedPrefsManager.saveUserId(context, userModel.getUserId(), documentID);
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

    public void writeHeats(BeaconModel beaconModel, Spot spot, GeoPoint point){

        Heats heats = new Heats();
        heats.setCreatedBy(user.getUserId());
        heats.setGeoLocation(spot.getGeoLocation());
        heats.setSpot(beaconModel.getSpot().getId());
        heats.setUserLocation(point);

        firestore.collection(DB_HEATS).document()
                .set(heats)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess() Write heats [" + aVoid + "]");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure() Write heats: e = [" + e + "]");
                    }
                });
    }

    //////////////////////////////
    ///////// REQUEST ASSISTANCE
    //////////////////////////////

    public void askHelpAndUpdateLocation(String safeWord, GeoPoint geoPoint, boolean askHelp) {

        if (user == null ) {
            return;
        }

        user.setWantsHelp(askHelp);
        user.setGeoLocation(geoPoint);
        user.setSafeWord(safeWord);

        firestore.collection(DB_USER).document(documentID)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if (askHelp) {
                            EventBus.getDefault().post(new AskHelpSuccessEvent());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure() called with: e = [" + e + "]");
                    }
                });
    }

}
