package me.custodio.Veever.manager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.widget.Toast;

/**
 * Created by Andrews on 19,September,2019
 */
public class Utils {

    public static boolean isNetworkConnected(Context context) {

        boolean hasNetwork = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        hasNetwork = cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();

        if (!hasNetwork) {
            makeToast(context, "No internet connection");
        }

        return hasNetwork;
    }

    public static void makeToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

}
