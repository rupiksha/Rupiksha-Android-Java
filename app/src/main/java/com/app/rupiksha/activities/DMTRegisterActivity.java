package com.app.rupiksha.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.app.rupiksha.R;
import com.app.rupiksha.apipresenter.RemitterRegistrationPresenter;
import com.app.rupiksha.databinding.ActivityDmtregisterBinding;
import com.app.rupiksha.extra.CustomToastNotification;
import com.app.rupiksha.extra.NetworkAlertUtility;
import com.app.rupiksha.interfaces.IRemitterRegistrationView;
import com.app.rupiksha.models.BaseResponse;
import com.app.rupiksha.storage.StorageUtil;

import java.util.HashMap;
import java.util.Objects;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class DMTRegisterActivity extends AppCompatActivity implements View.OnClickListener,IRemitterRegistrationView {
    ActivityDmtregisterBinding binding;
    Activity activity;
    RemitterRegistrationPresenter presenter;
    String firstname,city,state,district,area,pincode,address,dob;
    private String OtpValue = "",dmtKeyValue="";
    private String dmtKey = "";
    String phoneNumber="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_dmtregister);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_dmtregister);
        activity=DMTRegisterActivity.this;
        presenter=new RemitterRegistrationPresenter();
        presenter.setView(this);
        binding.includeLayout.toolBar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });

        if(getIntent().hasExtra("dmtKey"))
            dmtKeyValue = getIntent().getStringExtra("dmtKey");

        if(getIntent().hasExtra("phoneNumber"))
            phoneNumber = getIntent().getStringExtra("phoneNumber");

        binding.btnsignin.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
//        switch (id) {
//            case R.id.btnsignin:
//                checkValidation();
//                break;
//
//        }
        if (id == R.id.btnsignin){
            checkValidation();
        }
    }

    private void checkValidation()
    {
        firstname = Objects.requireNonNull(binding.etname.getText()).toString();
        city = Objects.requireNonNull(binding.etCity.getText()).toString();
        state = Objects.requireNonNull(binding.etState.getText()).toString();
        district = Objects.requireNonNull(binding.etDistrict.getText()).toString();
        pincode = Objects.requireNonNull(binding.etpincode.getText()).toString();
        area = Objects.requireNonNull(binding.etaddress.getText()).toString();
        dob = Objects.requireNonNull(binding.etdob.getText()).toString();
        //OtpValue = Objects.requireNonNull(binding.etotp.getText()).toString();


        if (TextUtils.isEmpty(firstname)) {
            binding.etname.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_firstname));
        }else if(TextUtils.isEmpty(city)){
            binding.etCity.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_city));
        }else if(TextUtils.isEmpty(pincode)){
            binding.etpincode.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_pincode));
        }
        else if(TextUtils.isEmpty(state)){
        binding.etState.requestFocus();
        new CustomToastNotification(activity,getResources().getString(R.string.please_enter_state));
    }
        else if(TextUtils.isEmpty(area)){
            binding.etaddress.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_area));
        }
        else if(TextUtils.isEmpty(dob)){
            binding.etdob.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_dob));
        }
        else if(TextUtils.isEmpty(district)){
            binding.etDistrict.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_district));
        }
         signupremitter();
    }

    private void signupremitter() {
        if (NetworkAlertUtility.isInternetConnection2(activity))
        {
            String accessToken = new StorageUtil(activity).getAccessToken();
            String apikey = new StorageUtil(activity).getApiKey();
            //======================================
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("name", firstname)
                    .addFormDataPart("city", city)
                    .addFormDataPart("state", state)
                    .addFormDataPart("pin", pincode)
                    .addFormDataPart("district", district)
                    .addFormDataPart("area", area)
                    .addFormDataPart("dob",dob)
                    .addFormDataPart("mobile",phoneNumber)
                    .addFormDataPart("dmtKey",dmtKey)
                    .build();
            //=======================================
            HashMap<String, String> headers = new HashMap<>();
            headers.put("headerToken", accessToken);
            headers.put("headerKey", apikey);
            Log.e("1234",""+requestBody);
            presenter.getRemitterRegister(activity,headers, requestBody,true);

        } else
        {
            NetworkAlertUtility.showNetworkFailureAlert(activity);
        }
    }


    @Override
    public void onRemitterRegisterSuccess(BaseResponse body) {
        if(body!=null) {
            new CustomToastNotification(activity, "" + body.getMessage());
            Intent intent = new Intent(activity,VAadharActivity.class)
                    .putExtra("phoneNumber",phoneNumber);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void vAadharSuccess(BaseResponse body) {

    }

    @Override
    public void onValidateOtpSuccess(BaseResponse body) {

    }

    @Override
    public Context getContext() {
        return null;
    }

    @Override
    public void enableLoadingBar(boolean enable, String s) {

    }

    @Override
    public void onError(String reason) {

    }

    @Override
    public void dialogAccountDeactivate(String reason) {

    }

    @Override
    public void onErrorToast(String reason) {

    }
}