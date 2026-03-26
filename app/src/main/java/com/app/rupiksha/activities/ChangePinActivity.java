package com.app.rupiksha.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.app.rupiksha.R;
import com.app.rupiksha.apipresenter.ChangePinPresenter;
import com.app.rupiksha.databinding.ActivityChangePinBinding;
import com.app.rupiksha.extra.CustomToastNotification;
import com.app.rupiksha.extra.NetworkAlertUtility;
import com.app.rupiksha.interfaces.IChangePinView;
import com.app.rupiksha.models.BaseResponse;
import com.app.rupiksha.storage.StorageUtil;
import com.app.rupiksha.utils.Utils;

import java.util.HashMap;
import java.util.Objects;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ChangePinActivity extends AppCompatActivity implements View.OnClickListener , IChangePinView {

    ActivityChangePinBinding binding;
    Activity activity;
    String oldpin,newpin,confirmpin;
    ChangePinPresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_change_pin);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_pin);
        activity = ChangePinActivity.this;
        presenter=new ChangePinPresenter();
        presenter.setView(this);
        binding.includeLayout.toolBar.setTitle(getResources().getString(R.string.title_change_pin));
        binding.includeLayout.toolBar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });

        binding.btnsubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
//        switch (id) {
//            case R.id.btnsubmit:
//                checkValidation();
//                break;
//
//        }
        if (id == R.id.btnsubmit){
            checkValidation();
        }
    }

    private void checkValidation()
    {
        oldpin = Objects.requireNonNull(binding.etoldpin.getText()).toString();
        newpin = Objects.requireNonNull(binding.etnewpin.getText()).toString();
        confirmpin = Objects.requireNonNull(binding.etconfirmpin.getText()).toString();
        if (TextUtils.isEmpty(oldpin)) {
            binding.etoldpin.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_old_pin));
        }else if(TextUtils.isEmpty(newpin)){
            binding.etnewpin.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_new_pin));
        }else if(TextUtils.isEmpty(confirmpin)){
            binding.etconfirmpin.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_confirm_pin));
        }else if (!newpin.equals(confirmpin)) {
            binding.etconfirmpin.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_confirm_pin_match));
        }else{
            changePinApi();
        }
    }

    private void changePinApi()
    {
        if (NetworkAlertUtility.isInternetConnection2(activity)) {
            String accessToken = new StorageUtil(activity).getAccessToken();
            String apikey = new StorageUtil(activity).getApiKey();
            //======================================
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("current_pin", oldpin)
                    .addFormDataPart("new_pin", newpin)
                    .build();
            //=======================================
            HashMap<String, String> headers = new HashMap<>();
            headers.put("headerToken", accessToken);
            headers.put("headerKey", apikey);
            presenter.getchangepin(activity, headers, requestBody,true);

        } else {
            NetworkAlertUtility.showNetworkFailureAlert(activity);
        }
    }

    @Override
    public void onChangePinSuccess(BaseResponse body) {
        if(body!=null){
            new CustomToastNotification(this, body.getMessage());
            onBackPressed();
        }
    }

    @Override
    public Context getContext() {
        return null;
    }

    @Override
    public void enableLoadingBar(boolean enable, String s) {
        if (enable)
            Utils.showProgressDialog(activity,"");
        else
            Utils.hideProgressDialog();
    }

    @Override
    public void onError(String reason) {
        new CustomToastNotification(this, reason);
    }

    @Override
    public void dialogAccountDeactivate(String reason) {

    }

    @Override
    public void onErrorToast(String reason) {
        new CustomToastNotification(this, reason);
    }
}