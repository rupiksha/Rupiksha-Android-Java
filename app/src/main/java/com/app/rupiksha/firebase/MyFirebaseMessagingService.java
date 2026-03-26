package com.app.rupiksha.firebase;

import android.content.Intent;
import android.util.Log;

import com.app.rupiksha.utils.Utils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.tapits.ubercms_bc_sdk.ConfirmationScreen;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FINGPAY_PUSH";

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);

        Log.d(TAG, "Firebase Token: " + token);

        // Agar server pe token bhejna hai to yaha bhej sakte ho
        // new StorageUtil(getApplicationContext()).setDeviceToken(token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage == null) return;

        if (remoteMessage.getData() != null && !remoteMessage.getData().isEmpty()) {

            Map<String, String> data = remoteMessage.getData();

            Log.d(TAG, "Push Data: " + data.toString());

            String msg = data.get("msg :");
            String ref = data.get("ref :");
            String push = data.get("pushnotification");

            if (push != null) {

                openSdkConfirmation(msg, ref);

            }
        }
    }

    private void openSdkConfirmation(String msg, String ref) {

        Intent intent = new Intent(this, ConfirmationScreen.class);
        intent.putExtra("msg :", msg);
        intent.putExtra("ref :", ref);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
    }
}
