package com.app.rupiksha.extra;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.app.rupiksha.R;
import com.app.rupiksha.databinding.CustomToastNotificationBinding;


public class CustomToastNotification {

    public CustomToastNotification(Context context, String message) {
        if (context != null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            CustomToastNotificationBinding binding = DataBindingUtil.inflate(inflater,
                    R.layout.custom_toast_notification, null, false);
            binding.tvMessage.setText(message);

            Toast toast = new Toast(context);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(binding.getRoot());
            toast.show();
        }
    }

}
