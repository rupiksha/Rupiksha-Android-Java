package com.app.rupiksha.extra;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.app.rupiksha.R;


public class NetworkAlertUtility {

    private static AlertDialog alert;

    public static boolean isConnectingToInternet(Context context) {
        if (context != null) {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null)
                    for (NetworkInfo anInfo : info) {
                        if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                    }
            }
        }

        return false;
    }

    public static boolean isConnectingToInternet2(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = null;
        if (cm != null) {
            activeNetwork = cm.getActiveNetworkInfo();
        }

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static boolean isInternetConnection(Context mContext) {
        final ConnectivityManager connMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = connMgr.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnected();
    }

    public static boolean isInternetConnection2(Context mContext)
    {

        final ConnectivityManager connMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connMgr != null) {
            final NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            final NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            return wifi.isAvailable() && wifi.getState() == NetworkInfo.State.CONNECTED || mobile.isAvailable() && mobile.getState() == NetworkInfo.State.CONNECTED;
        }
        return false;
    }

    public static void showNetworkFailureAlert(Context context) {
        if (context != null && !((Activity) context).isFinishing() && (alert == null || !alert.isShowing())) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(context.getResources().getString(R.string.msg_no_network)).setTitle(context.getResources().getString(R.string.no_internet))
                    .setCancelable(false)
                    .setNegativeButton(context.getString(R.string.btn_ok), (dialog, id) -> {
                    });
            alert = builder.create();
            alert.show();
        }
    }


}
