package com.app.rupiksha.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;

import com.app.rupiksha.R;
import com.app.rupiksha.apipresenter.RemitterRegistrationPresenter;
import com.app.rupiksha.databinding.ActivityAepsServicesBinding;
import com.app.rupiksha.databinding.ActivityVaadharBinding;
import com.app.rupiksha.extra.CustomToastNotification;
import com.app.rupiksha.extra.NetworkAlertUtility;
import com.app.rupiksha.interfaces.IRemitterRegistrationView;
import com.app.rupiksha.models.BaseResponse;
import com.app.rupiksha.storage.StorageUtil;

import java.util.HashMap;
import java.util.Objects;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class VAadharActivity extends AppCompatActivity implements View.OnClickListener, IRemitterRegistrationView {
    ActivityVaadharBinding binding;
    Activity activity;
    Context context;
    String phoneNumber="";
    String aadhar_number;
    RemitterRegistrationPresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_vaadhar);
        activity = VAadharActivity.this;
        context = VAadharActivity.this;
        binding.includeLayout.toolBar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });
        presenter=new RemitterRegistrationPresenter();
        presenter.setView(this);
        if(getIntent().hasExtra("phoneNumber"))
            phoneNumber = getIntent().getStringExtra("phoneNumber");
        Log.d("phone",phoneNumber);

        binding.btnsubmit.setOnClickListener(this);
    }

    private void checkValidation() {
        aadhar_number = Objects.requireNonNull(binding.etadhar.getText()).toString();
        if (TextUtils.isEmpty(aadhar_number)) {
            binding.etadhar.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.placeholder_aadhar_card));
        }else{
            vAadharApi();
        }

    }

    private void vAadharApi() {
        if (NetworkAlertUtility.isInternetConnection2(activity))
        {
            String accessToken = new StorageUtil(activity).getAccessToken();
            String apikey = new StorageUtil(activity).getApiKey();
            //======================================
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("aadhar", aadhar_number)
                    .addFormDataPart("mobile",phoneNumber)
                    .build();
            //=======================================
            HashMap<String, String> headers = new HashMap<>();
            headers.put("headerToken", accessToken);
            headers.put("headerKey", apikey);
            Log.e("1234",""+requestBody);
            presenter.getVAadhar(activity,headers, requestBody,true);

        } else
        {
            NetworkAlertUtility.showNetworkFailureAlert(activity);
        }
    }
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnsubmit){
            checkValidation();
        }
    }

    @Override
    public void onRemitterRegisterSuccess(BaseResponse body) {

    }

    @Override
    public void vAadharSuccess(BaseResponse body) {
        if(body!=null) {
            new CustomToastNotification(activity, "" + body.getMessage());
            Intent intent = new Intent(activity,DMTOtpActivity.class)
                    .putExtra("mobile",body.getData().getMobile())
                    .putExtra("otpid",body.getData().getOtpid())
                    .putExtra("aadhar",body.getData().getAadhar());
            startActivity(intent);
            finish();
        }

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