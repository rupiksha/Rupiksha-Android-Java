package com.app.rupiksha.activities;

import static com.tapits.ubercms_bc_sdk.utils.Globals.imei;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;

import com.app.rupiksha.R;
import com.app.rupiksha.constant.AppConstants;
import com.app.rupiksha.databinding.ActivityCms2ServiceBinding;
import com.app.rupiksha.extra.CustomToastNotification;
import com.app.rupiksha.models.ModelUserInfo;
import com.app.rupiksha.storage.StorageUtil;
import com.app.rupiksha.utils.Utils;

import com.tapits.ubercms_bc_sdk.LoginScreen;
import com.tapits.ubercms_bc_sdk.utils.Constants;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class CMS2ServiceActivity extends BaseActivity implements View.OnClickListener {

    ActivityCms2ServiceBinding binding;

    Activity activity;
    String message,amount;

    String device_id;
    private Double stringLatitude = Double.valueOf("0.0");
    private Double stringLongitude = Double.valueOf("0.0");
    private ModelUserInfo userInfo = null;
    private static final int CODE = 1001;
    String referenceId = "TXN" + System.currentTimeMillis();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        //setContentView(R.layout.activity_cms2_service);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_cms2_service);
        activity=CMS2ServiceActivity.this;

        binding.includeLayout.toolBar.setTitle(getResources().getString(R.string.title_aeps_service));
        binding.includeLayout.toolBar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });

        device_id = Utils.getAndroidId(this);

        stringLatitude = Double.valueOf(StorageUtil.getString(this, AppConstants.KEY_LAST_LAT));
        stringLongitude = Double.valueOf(StorageUtil.getString(this, AppConstants.KEY_LAST_LNG));
        binding.btnsubmit.setOnClickListener(this);

        CmsService();

    }

    @Override
    public void onClick(View view) {
        checkvalidation();
    }

    private void checkvalidation()
    {

        amount = Objects.requireNonNull(binding.etAmount.getText()).toString();
        message = Objects.requireNonNull(binding.etmessage.getText()).toString();

        if (TextUtils.isEmpty(amount)) {
            binding.etAmount.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_amount));
        }else if (TextUtils.isEmpty(message)) {
            binding.etmessage.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_message));
        }else{

           // CmsService();


        }
    }

    public static String generateClientRefID() {
        String prefix = "TXN"; // 3 characters

        // 10-digit timestamp (yyMMddHHmm)
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmm", Locale.getDefault());
        String timestamp = sdf.format(new Date());

        // Allowed characters: A-Z and 0-9
        String allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom secureRandom = new SecureRandom();

        // Generate 5 random characters
        StringBuilder randomPart = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            int index = secureRandom.nextInt(allowedChars.length());
            randomPart.append(allowedChars.charAt(index));
        }

        // Combine all parts to form the 18-character reference ID
        return prefix + timestamp + randomPart.toString();
    }

    private void CmsService() {
        userInfo = new StorageUtil(this).getUserInfo();

        Intent intent = new Intent(this, LoginScreen.class);
        intent.putExtra(Constants.MERCHANT_ID, userInfo.getOutlet());
        intent.putExtra(Constants.SECRET_KEY, "adb9af1d9d8c573d8600d73754c248c9343ce074d68af0722af434bc922061fa");
       // intent.putExtra(Constants.TYPE_REF, Constants.REF_ID);
        intent.putExtra(Constants.TYPE_REF,Constants.BILLERS);
        intent.putExtra(Constants.AMOUNT, "");
        intent.putExtra(Constants.REMARKS, "");
        intent.putExtra(Constants.MOBILE_NUMBER, userInfo.getMobile());
        intent.putExtra(Constants.SUPER_MERCHANTID, "1407");
        intent.putExtra(Constants.IMEI, device_id);
        intent.putExtra(Constants.LATITUDE, stringLatitude);
        intent.putExtra(Constants.LONGITUDE, stringLongitude);

        intent.putExtra(Constants.NAME, userInfo.getName());

        intent.putExtra(Constants.REFERENCE_ID, "");
        startActivityForResult(intent, CODE);

        Log.d("CMS_REQUEST",
                "MERCHANT_ID: " + userInfo.getOutlet() +
                        "\nSECRET_KEY: " + "adb9af1d9d8c573d8600d73754c248c9343ce074d68af0722af434bc922061fa" +
                        "\nTYPE_REF: " + Constants.REF_ID +
                        "\nAMOUNT: " + amount +
                        "\nREMARKS: " + message +
                        "\nMOBILE: " + userInfo.getMobile() +
                        "\nSUPER_MERCHANTID: " + 1407 +
                        "\nIMEI: " + device_id +
                        "\nLAT: " + stringLatitude +
                        "\nLNG: " + stringLongitude +
                        "\nREFERENCE_ID: " + generateClientRefID()
        );







    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == CODE) {
            String response = data.getStringExtra(Constants.MESSAGE);
            boolean status = data.getBooleanExtra(Constants.TRANS_STATUS, false);
            String txnId = data.getStringExtra(Constants.TXN_ID);
            String timestamp = data.getStringExtra(Constants.TRANS_TIMESTAMP);
            com.tapits.ubercms_bc_sdk.utils.Utils.logD("onactivityresult :" + response + " " + status);
            Toast.makeText(this, "" + response, Toast.LENGTH_SHORT).show();
        }
    }



    }