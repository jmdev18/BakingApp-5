package com.debdroid.bakingapp.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.debdroid.bakingapp.R;

import timber.log.Timber;

public class NetworkUtility {
    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/";

    /**
     * This method returns the base url of the recipe
     * @return The recipe base url
     */
    public static String getBaseUrl() {
        return BASE_URL;
    }

    /**
     * This method checks if the device is connected to a network to perform network operation.
     * @param ctx Application Context
     * @return True if WiFi or Mobile network is available otherwise returns False
     */
    public static boolean isOnline(final Context ctx) {
        final ConnectivityManager connMgr = (ConnectivityManager)
                ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        return activeInfo != null && activeInfo.isConnected();
    }

    /**
     * This method shows a Toast message if the device is not online.
     * @param context Application Context
     */
    public static void checkInternetConnection(Context context) {
        boolean networkStatus = NetworkUtility.isOnline(context);
        if(!networkStatus) {
            Timber.d(context.getString(R.string.no_network_error_msg));
            Toast.makeText(context, context.getString(R.string.no_network_error_msg),Toast.LENGTH_SHORT).show();
        } else {
            Timber.d("Internet connection available");
        }
    }
}
