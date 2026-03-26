package com.app.rupiksha.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.app.rupiksha.R;
import com.app.rupiksha.apipresenter.ChangePasswordPresenter;
import com.app.rupiksha.databinding.ActivityChangePasswordBinding;
import com.app.rupiksha.extra.CustomToastNotification;
import com.app.rupiksha.extra.NetworkAlertUtility;
import com.app.rupiksha.interfaces.IChangePasswordView;
import com.app.rupiksha.models.BaseResponse;
import com.app.rupiksha.storage.StorageUtil;
import com.app.rupiksha.utils.Utils;

import java.util.HashMap;
import java.util.Objects;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener, IChangePasswordView {

    ActivityChangePasswordBinding binding;
    Activity activity;
    String oldpassword,newpassword,confirmpassword;
    ChangePasswordPresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_change_password);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_change_password);
        activity=ChangePasswordActivity.this;
        presenter=new ChangePasswordPresenter();
        presenter.setView(this);
        binding.includeLayout.toolBar.setTitle(getResources().getString(R.string.title_change_password));
        binding.includeLayout.toolBar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });

        binding.btnsubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();
//        switch (id) {
//            case R.id.btnsubmit:
//                 checkValidation();
//                 break;
//        }
        if (id == R.id.btnsubmit){
            checkValidation();
        }
    }

    private void checkValidation()
    {
        oldpassword = Objects.requireNonNull(binding.etoldpass.getText()).toString();
        newpassword = Objects.requireNonNull(binding.etnewpass.getText()).toString();
        confirmpassword = Objects.requireNonNull(binding.etconfirmpass.getText()).toString();
        if (TextUtils.isEmpty(oldpassword)) {
            binding.etoldpass.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_old_password));
        }else if(TextUtils.isEmpty(newpassword)){
            binding.etnewpass.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_new_password));
        }else if(TextUtils.isEmpty(confirmpassword)){
            binding.etconfirmpass.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_confirm_password));
        }else if (!newpassword.equals(confirmpassword)) {
            binding.etconfirmpass.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_confirm_password_match));
        }else{
            changePasswordApi();
        }
    }

    private void changePasswordApi()
    {
        if (NetworkAlertUtility.isInternetConnection2(activity)) {
            String accessToken = new StorageUtil(activity).getAccessToken();
            String apikey = new StorageUtil(activity).getApiKey();
            //======================================
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("current_password", oldpassword)
                    .addFormDataPart("new_password", newpassword)
                    .build();
            //=======================================
            HashMap<String, String> headers = new HashMap<>();
            headers.put("headerToken", accessToken);
            headers.put("headerKey", apikey);
            presenter.getchangepassword(activity, headers, requestBody,true);

        } else {
            NetworkAlertUtility.showNetworkFailureAlert(activity);
        }
    }

    @Override
    public void onChangePasswordSuccess(BaseResponse body) {
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