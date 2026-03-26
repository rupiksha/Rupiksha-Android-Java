package com.app.rupiksha.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.app.rupiksha.R;
import com.app.rupiksha.apipresenter.PinOtpVerifyPresenter;
import com.app.rupiksha.databinding.ActivityPinVerifyBinding;
import com.app.rupiksha.extra.CustomToastNotification;
import com.app.rupiksha.extra.NetworkAlertUtility;
import com.app.rupiksha.interfaces.IPinOtpView;
import com.app.rupiksha.models.BaseResponse;
import com.app.rupiksha.otpview.OnOtpCompletionListener;
import com.app.rupiksha.storage.StorageUtil;
import com.app.rupiksha.utils.Utils;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class PinVerifyActivity extends AppCompatActivity implements View.OnClickListener , IPinOtpView {

    ActivityPinVerifyBinding binding;
    Activity activity;
    String logvalue="";
    String PinValue="";
    PinOtpVerifyPresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_pin_verify);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_pin_verify);
        activity=PinVerifyActivity.this;
        presenter=new PinOtpVerifyPresenter();
        presenter.setView(this);
        if (getIntent().hasExtra("logkey"))
            logvalue = getIntent().getStringExtra("logkey");
        binding.includeLayout.toolBar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });
        binding.btnVerifyPin.setOnClickListener(this);

        binding.otpView.requestFocus();
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        binding.otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {
                Utils.showProgressDialog(activity,"");
                PinValue = otp;
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(binding.getRoot().getWindowToken(), 0);
                Utils.hideSofthKeyboaard(activity, binding.getRoot());
                pinVerify();

            }
        });
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();
//        switch (id) {
//            case R.id.btnVerifyPin:
//                    checkValidation();
//                break;
//        }
        if (id == R.id.btnVerifyPin) {
            checkValidation();
        }
    }

    private void checkValidation()
    {
        if (PinValue.isEmpty())
        {
            new CustomToastNotification(activity,getString(R.string.please_enter_pin));
        } else if (!PinValue.isEmpty()) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(binding.getRoot().getWindowToken(), 0);
            Utils.hideSofthKeyboaard(activity, binding.getRoot());
            setResult(RESULT_OK);
            pinVerify();

        }
    }


    private void pinVerify()
    {
        if (NetworkAlertUtility.isInternetConnection2(activity))
        {
            HashMap<String, RequestBody> map = new HashMap<>();
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("log_key", logvalue)
                    .addFormDataPart("otp", PinValue)
                    .build();
            //=======================================
            presenter.getotppinVerfy(activity,requestBody,true);

        } else
        {
            NetworkAlertUtility.showNetworkFailureAlert(activity);
        }
    }


    @Override
    public void onPinOtpSuccess(BaseResponse body)
    {
        if(body!=null){
            new StorageUtil(activity).setAccessToken(body.getHeaderToken());
            new StorageUtil(activity).setApiKey(body.getHeaderKey());
            if(body.getData().getAccountkycstatus().getKyc().equals("YES")){
                Intent intent1 = new Intent(this, HomeActivity.class);
                startActivity(intent1);
                finish();

            }else if(body.getData().getAccountkycstatus().getKyc().equals("NO"))
            {
                Intent intent1 = new Intent(this, KycUserActivity.class);
                startActivity(intent1);
                finish();

            }else if(body.getData().getAccountkycstatus().getKyc().equals("PENDING")){
                Intent intent1 = new Intent(this, PendingKycActivity.class)
                        .putExtra("email",body.getData().getAccountkycstatus().getSupportmail())
                        .putExtra("phone",body.getData().getAccountkycstatus().getSupportphone());
                startActivity(intent1);
                finish();
            }else{
                Intent intent1 = new Intent(this, KycUserActivity.class);
                startActivity(intent1);
                finish();
            }
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