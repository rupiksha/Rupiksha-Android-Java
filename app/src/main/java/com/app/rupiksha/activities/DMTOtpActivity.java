package com.app.rupiksha.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;

import com.app.rupiksha.R;
import com.app.rupiksha.apipresenter.RemitterRegistrationPresenter;
import com.app.rupiksha.constant.AppConstants;
import com.app.rupiksha.databinding.ActivityDmtotpBinding;
import com.app.rupiksha.databinding.ActivityVaadharBinding;
import com.app.rupiksha.extra.CustomToastNotification;
import com.app.rupiksha.extra.NetworkAlertUtility;
import com.app.rupiksha.interfaces.IRemitterRegistrationView;
import com.app.rupiksha.models.BaseResponse;
import com.app.rupiksha.otpview.OnOtpCompletionListener;
import com.app.rupiksha.storage.StorageUtil;
import com.app.rupiksha.utils.Utils;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class DMTOtpActivity extends AppCompatActivity implements View.OnClickListener, IRemitterRegistrationView {
    ActivityDmtotpBinding binding;
    Activity activity;
    Context context;
    String PinValue="",mobile="",optId="",aadhar="";
    RemitterRegistrationPresenter presenter;
    private String stringLatitude = "";
    private String stringLongitude = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_dmtotp);
        activity = DMTOtpActivity.this;
        context = DMTOtpActivity.this;
        presenter=new RemitterRegistrationPresenter();
        presenter.setView(this);
        binding.includeLayout.toolBar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });
        stringLatitude = StorageUtil.getString(this, AppConstants.KEY_LAST_LAT);
        stringLongitude = StorageUtil.getString(this, AppConstants.KEY_LAST_LNG);
        if(getIntent().hasExtra("mobile"))
            mobile = getIntent().getStringExtra("mobile");

        if(getIntent().hasExtra("otpid"))
            optId = getIntent().getStringExtra("otpid");

        if(getIntent().hasExtra("aadhar"))
            aadhar = getIntent().getStringExtra("aadhar");

        binding.otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {
                Utils.showProgressDialog(activity,"");
                PinValue = otp;
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(binding.getRoot().getWindowToken(), 0);
                Utils.hideSofthKeyboaard(activity, binding.getRoot());
                otpVerify();

            }
        });
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
            otpVerify();

        }
    }
    private void otpVerify()
    {
        if (NetworkAlertUtility.isInternetConnection2(activity))
        {
            String accessToken = new StorageUtil(activity).getAccessToken();
            String apikey = new StorageUtil(activity).getApiKey();
            //======================================
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("otp", PinValue)
                    .addFormDataPart("otpid",optId)
                    .addFormDataPart("aadhar",aadhar)
                    .addFormDataPart("lat",stringLatitude)
                    .addFormDataPart("log",stringLongitude)
                    .addFormDataPart("mobile",mobile)
                    .build();
            //=======================================
            HashMap<String, String> headers = new HashMap<>();
            headers.put("headerToken", accessToken);
            headers.put("headerKey", apikey);
            Log.e("1234",""+requestBody);
            presenter.getValidateOtp(activity,headers, requestBody,true);

        } else
        {
            NetworkAlertUtility.showNetworkFailureAlert(activity);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnVerifyPin) {
            checkValidation();
        }
    }

    @Override
    public void onRemitterRegisterSuccess(BaseResponse body) {

    }

    @Override
    public void vAadharSuccess(BaseResponse body) {

    }

    @Override
    public void onValidateOtpSuccess(BaseResponse body) {
        if(body!=null) {
            new CustomToastNotification(activity, "" + body.getMessage());
            Intent intent = new Intent(activity,DMTKycActivity.class)
                    .putExtra("aadhar",body.getData().getAadhar())
                    .putExtra("otp_id",body.getData().getOtpid())
                    .putExtra("mobile",body.getData().getMobile());
            startActivity(intent);
            finish();
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