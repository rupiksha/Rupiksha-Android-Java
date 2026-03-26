package com.app.rupiksha.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.app.rupiksha.R;
import com.app.rupiksha.apipresenter.RemitterLoginPresenter;
import com.app.rupiksha.databinding.ActivityDmtloginBinding;
import com.app.rupiksha.extra.CustomToastNotification;
import com.app.rupiksha.extra.NetworkAlertUtility;
import com.app.rupiksha.interfaces.IRemitterLoginView;
import com.app.rupiksha.models.BaseResponse;
import com.app.rupiksha.storage.StorageUtil;

import java.util.HashMap;
import java.util.Objects;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class DMTLoginActivity extends AppCompatActivity implements View.OnClickListener, IRemitterLoginView {
   ActivityDmtloginBinding binding;
    Activity activity;
    RemitterLoginPresenter presenter;
    String phoneNumber;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_dmtlogin);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_dmtlogin);
        activity=DMTLoginActivity.this;
        presenter=new RemitterLoginPresenter();
        presenter.setView(this);
        binding.includeLayout.toolBar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });

       binding.btnsignin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();
        if (id == R.id.btnsignin){
            checkValidation();
        }
    }

    private void checkValidation() {
        phoneNumber = Objects.requireNonNull(binding.etmobilenumber.getText()).toString();
        if (TextUtils.isEmpty(phoneNumber)) {
            binding.etmobilenumber.requestFocus();
            new CustomToastNotification(activity, getResources().getString(R.string.please_enter_mobile_number));
        }
        else{
            Signinremitter();
        }
    }

    private void Signinremitter() {
        if (NetworkAlertUtility.isInternetConnection2(activity))
        {
            String accessToken = new StorageUtil(activity).getAccessToken();
            String apikey = new StorageUtil(activity).getApiKey();
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("phone", phoneNumber)
                    .build();
            //=======================================
            HashMap<String, String> headers = new HashMap<>();
            headers.put("headerToken", accessToken);
            headers.put("headerKey", apikey);
            presenter.getRemitterLogin(activity,headers, requestBody,true);

        } else
        {
            NetworkAlertUtility.showNetworkFailureAlert(activity);
        }

    }

    @Override
    public void onRemitterLoginSuccess(BaseResponse body) {
        if(body!=null){
            new CustomToastNotification(activity,""+body.getMessage());
            Intent intent = null;
            switch (body.getActivity()) {
//                case "kyc":
//                    intent = new Intent(activity, DMTKycActivity.class)
//                    .putExtra("dmtKey", body.getDmtKey())
//                    .putExtra("phoneNumber", phoneNumber);
//                    startActivity(intent);
//                    finish();
//                    break;
                case "dashboard":
                    new StorageUtil(activity).setDMTkey(body.getDmtKey());
                    new StorageUtil(activity).saveDMTInfo(body.getData().getDmtProfileData());
                    intent = new Intent(activity, DMTActivity.class)
                            .putExtra("dmtKey", body.getDmtKey())
                            .putExtra("phoneNumber", phoneNumber);

                    startActivity(intent);
                    finish();
                    break;
                case "register":
                    intent = new Intent(activity, DMTRegisterActivity.class)
                            .putExtra("dmtKey", body.getDmtKey())
                            .putExtra("phoneNumber", phoneNumber);


                    startActivity(intent);
                    finish();
                    break;
                case "vaadhar":
                    intent = new Intent(activity, VAadharActivity.class)
                            .putExtra("phoneNumber", phoneNumber);

                    startActivity(intent);
                    finish();
                    break;
            }
        }

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