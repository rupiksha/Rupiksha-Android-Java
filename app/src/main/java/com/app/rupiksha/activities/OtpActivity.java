package com.app.rupiksha.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.app.rupiksha.R;
import com.app.rupiksha.apipresenter.PinOtpVerifyPresenter;
import com.app.rupiksha.databinding.ActivityOtpBinding;
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

public class OtpActivity extends AppCompatActivity implements View.OnClickListener , IPinOtpView {

    ActivityOtpBinding binding;
    Activity activity;
    boolean flag=false;
    private int otpTime = 30;
    private Handler otpHandler;
    private Runnable otpRunnable;
    private String OtpValue = "";
    String logvalue="";
    PinOtpVerifyPresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_otp);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_otp);
        activity=OtpActivity.this;
        presenter=new PinOtpVerifyPresenter();
        presenter.setView(this);
        binding.includeLayout.toolBar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });
        if (getIntent().hasExtra("flag"))
            flag = getIntent().getBooleanExtra("flag",false);

        if (getIntent().hasExtra("logkey"))
            logvalue = getIntent().getStringExtra("logkey");

        binding.btnVerifyOTP.setOnClickListener(this);

        otpHandler = new Handler(Looper.getMainLooper());
        otpTimer();

        binding.otpView.requestFocus();
        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        binding.otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {
                Utils.showProgressDialog(activity,"");
                OtpValue = otp;
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(binding.getRoot().getWindowToken(), 0);
                Utils.hideSofthKeyboaard(activity, binding.getRoot());
                if(flag) {
                    onBackPressed();
                }else{
                   otpVerify();
                }
            }
        });


    }
    private void checkValidation()
    {
        if (OtpValue.isEmpty())
        {
            new CustomToastNotification(activity,getString(R.string.please_enter_otp));
        } else if (!OtpValue.isEmpty()) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(binding.getRoot().getWindowToken(), 0);
            Utils.hideSofthKeyboaard(activity, binding.getRoot());
            setResult(RESULT_OK);
            otpVerify();

        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
//        switch (id) {
//            case R.id.btnVerifyOTP:
//
//               checkValidation();
//                break;
//        }
        if (id==R.id.btnVerifyOTP){
            checkValidation();
        }
    }

    private void otpVerify()
    {
        if (NetworkAlertUtility.isInternetConnection2(activity))
        {
            HashMap<String, RequestBody> map = new HashMap<>();
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("log_key", logvalue)
                    .addFormDataPart("otp", OtpValue)
                    .build();
            //=======================================
            presenter.getotppinVerfy(activity,requestBody,true);

        } else
        {
            NetworkAlertUtility.showNetworkFailureAlert(activity);
        }
    }

    private void otpTimer() {
        binding.tvOptTime.setText("(" + otpTime + " sec)");
        binding.tvOptTime.setVisibility(View.VISIBLE);
        binding.btnResendOtp.setVisibility(View.GONE);
        binding.otpmsg.setVisibility(View.VISIBLE);
        binding.btnResendOtp.setTextColor(getResources().getColor(R.color.white_60));
        if (otpTime == 0)
        {
            otpHandler.removeCallbacks(otpRunnable);
            binding.btnResendOtp.setVisibility(View.VISIBLE);
            binding.tvOptTime.setVisibility(View.GONE);
            binding.otpmsg.setVisibility(View.GONE);
            binding.btnResendOtp.setTextColor(getResources().getColor(R.color.blue_app));
            binding.btnResendOtp.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                   // resendVerificationCode();
                    otpTime = 30;
                    otpTimer();
                }
            });
            return;
        }
        otpRunnable = new Runnable() {
            @Override
            public void run() {
                --otpTime;
                binding.tvOptTime.setText("(" + otpTime + " sec)");
                otpTimer();
            }
        };
        otpHandler.postDelayed(otpRunnable, 1000);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("flag", flag);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

    @Override
    public void onPinOtpSuccess(BaseResponse body)
    {
        if(body!=null)
        {
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
                        .putExtra("email",body.getData() .getAccountkycstatus().getSupportmail())
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