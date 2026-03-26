package com.app.rupiksha.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;

import com.app.rupiksha.R;
import com.app.rupiksha.constant.AppConstants;
import com.app.rupiksha.databinding.ActivityMatmBinding;
import com.app.rupiksha.extra.CustomToastNotification;
import com.app.rupiksha.models.ModelUserInfo;
import com.app.rupiksha.storage.StorageUtil;
import com.app.rupiksha.utils.Utils;
import com.fingpay.microatmsdk.MicroAtmLoginScreen;
import com.fingpay.microatmsdk.utils.Constants;

import org.bouncycastle.jcajce.provider.symmetric.ARC4;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class MatmActivity extends BaseActivity implements View.OnClickListener {

    ActivityMatmBinding binding;
    Activity activity;
    String type,message,amount;
    int type1=0;
    String device_id;
    private Double stringLatitude = Double.valueOf("0.0");
    private Double stringLongitude = Double.valueOf("0.0");
    private ModelUserInfo userInfo = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_matm);
        activity=MatmActivity.this;
        binding.includeLayout.toolBar.setTitle("Matm");
        binding.includeLayout.toolBar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });
        device_id = Utils.getAndroidId(this);

        stringLatitude = Double.valueOf(StorageUtil.getString(this, AppConstants.KEY_LAST_LAT));
        stringLongitude = Double.valueOf(StorageUtil.getString(this, AppConstants.KEY_LAST_LNG));

        setType();
        binding.btnsubmit.setOnClickListener(this);
        binding.etType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedtype = (String) parent.getItemAtPosition(position);
                Log.d("selectedType", selectedtype);

                if (selectedtype.equals("cashWithdrawal")) {
                    type = String.valueOf(Constants.CASH_WITHDRAWAL);
                    type1 = Constants.CASH_WITHDRAWAL;
                    Log.d("transactionAmount", selectedtype);
                    binding.tilAmount.setVisibility(View.VISIBLE);
                    amount = binding.etAmount.getText().toString();
                    Log.d("transactionAmount11", amount);
                } else if (selectedtype.equals("balanceEnquiry")) {
                    type = String.valueOf(Constants.BALANCE_ENQUIRY);
                    type1 = Constants.BALANCE_ENQUIRY;
                    binding.tilAmount.setVisibility(View.GONE);
                    amount = "0";
                    binding.etAmount.setText("0");
                    Log.d("transactionAmount", amount);
                    Log.d("type", type);
                }
            }
        });
    }
    private void checkvalidation()
    {
        amount = Objects.requireNonNull(binding.etAmount.getText()).toString();
        message = Objects.requireNonNull(binding.etmessage.getText()).toString();
//        if(TextUtils.isEmpty(type))
//        {
//            binding.etType.requestFocus();
//            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_type));
//        }else
            if (TextUtils.isEmpty(amount)) {
            binding.etAmount.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_amount));
        }else if (TextUtils.isEmpty(message)) {
            binding.etmessage.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_message));
        }else{
                userInfo = new StorageUtil(this).getUserInfo();
                Log.d("type","type1 :"+type1+"mobile :"+ userInfo.getMobile());
                Intent intent = new Intent(this,MicroAtmLoginScreen.class);
//            startActivity(new Intent(this, MicroAtmLoginScreen.class)
                intent.putExtra(Constants.MERCHANT_USERID, "FINGPAY1234");
                intent.putExtra(Constants.MERCHANT_PASSWORD, "e6e061838856bf47e1de730719fb2609");
                intent.putExtra(Constants.AMOUNT, amount);
                intent.putExtra(Constants.REMARKS, message);
                intent.putExtra(Constants.MOBILE_NUMBER, userInfo.getMobile());
                intent.putExtra(Constants.AMOUNT_EDITABLE, false);
                intent.putExtra(Constants.TXN_ID, generateClientRefID());
                intent.putExtra(Constants.SUPER_MERCHANTID, "1407");
                intent.putExtra(Constants.IMEI, device_id);
                intent.putExtra(Constants.LATITUDE, stringLatitude);
                intent.putExtra(Constants.LONGITUDE, stringLongitude);
                intent.putExtra(Constants.TYPE,type1);
                intent.putExtra(Constants.MICROATM_MANUFACTURER, Constants.MoreFun);
                startActivity(intent);

                Log.d("MicroAtmLoginScreen", Constants.TYPE+","+Constants.MERCHANT_USERID+","+Constants.MERCHANT_PASSWORD+","
                        +Constants.AMOUNT+","+Constants.REMARKS+","+Constants.MOBILE_NUMBER+","+Constants.AMOUNT_EDITABLE+","
                        +Constants.TXN_ID+":"+generateClientRefID()+","+Constants.SUPER_MERCHANTID+","+Constants.IMEI+","+Constants.LATITUDE+","+Constants.LONGITUDE);
            Log.d("type33",type);
            Log.d("stringLatitude", String.valueOf(stringLatitude));
            Log.d("stringLongitude", String.valueOf(stringLongitude)+"device_id"+device_id);
        }
    }

    @Override
    public void onClick(View view) {
        checkvalidation();
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

    private void setType() {
        String[] list = getResources().getStringArray(R.array.transactionType);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                list
        );
        binding.etType.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 999) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {

                    boolean status = data.getBooleanExtra("status", false);
                    int response = data.getIntExtra("response", 0);
                    String message = data.getStringExtra("message");
                    if (status) {
                        try {
                            JSONObject jsonObject = new JSONObject(String.valueOf(data.getStringExtra("JSONDATA")));
                            Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_LONG).show();
                            Log.d( " RESULTS1",jsonObject.toString());
                        } catch (JSONException jsonException) {
                            Log.d( " RESULTS2",jsonException.toString());
                            jsonException.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_LONG).show();
                        Log.d( " RESULTS3",message.toString());
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "" + data.toString(), Toast.LENGTH_LONG).show();
                    Log.d( " RESULTS4",data.toString());
                }
            }
        }

//    }
}
