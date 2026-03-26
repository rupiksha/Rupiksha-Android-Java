package com.app.rupiksha.extra;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.app.rupiksha.constant.AppConstants;


public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        new Handler(Looper.getMainLooper()).post(() -> {
            try {
                boolean IsConnected = NetworkAlertUtility.isInternetConnection(context);
                Log.e("NetworkChangeReceiver", "IsConnected: " + IsConnected);

                Intent intent2 = new Intent(AppConstants.BroadcastNotifyAction.BROADCAST_CONNECTION_CHANGE);
                intent2.putExtra("notify_type", AppConstants.NOTIFY_CONNECTION_CHANGE);
                intent2.putExtra("IsConnected", IsConnected);
                intent2.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                context.sendBroadcast(intent2);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}