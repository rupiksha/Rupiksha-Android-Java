package com.app.rupiksha.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.app.rupiksha.R;
import com.app.rupiksha.apipresenter.RaiseComplainedPresenter;
import com.app.rupiksha.databinding.ActivityRaiseComplainedBinding;
import com.app.rupiksha.extra.CustomToastNotification;
import com.app.rupiksha.extra.NetworkAlertUtility;
import com.app.rupiksha.interfaces.IRaisedComplainedView;
import com.app.rupiksha.models.BaseResponse;
import com.app.rupiksha.storage.StorageUtil;
import com.app.rupiksha.utils.Utils;

import java.util.HashMap;
import java.util.Objects;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class RaiseComplainedActivity extends AppCompatActivity implements View.OnClickListener , IRaisedComplainedView {
    ActivityRaiseComplainedBinding binding;
    Activity activity;
    String type,message,txid;
    RaiseComplainedPresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_raise_complained);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_raise_complained);
        activity=RaiseComplainedActivity.this;
        presenter=new RaiseComplainedPresenter();
        presenter.setView(this);
        if (getIntent().hasExtra("type"))
            type = getIntent().getStringExtra("type");
        if (getIntent().hasExtra("txid"))
            txid = getIntent().getStringExtra("txid");
        binding.includeLayout.toolBar.setTitle(getResources().getString(R.string.label_raised_complaint));
        binding.includeLayout.toolBar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });
        binding.ettxid.setText(""+txid);
        binding.ettype.setText(""+type);
        binding.btnsubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();
//        switch (id) {
//            case R.id.btnsubmit:
//                checkvalidation();
//                break;
//        }
        if (id == R.id.btnsubmit)
        {
            checkvalidation();
        }
    }
    private void checkvalidation()
    {
        type = Objects.requireNonNull(binding.ettype.getText()).toString();
        txid = Objects.requireNonNull(binding.ettxid.getText()).toString();
        message = Objects.requireNonNull(binding.etmessage.getText()).toString();
        if(TextUtils.isEmpty(type))
        {
            binding.ettype.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_type));
        }else if (TextUtils.isEmpty(txid)) {
            binding.ettxid.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_txid));
        }else if (TextUtils.isEmpty(message)) {
            binding.etmessage.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_message));
        }else{
            doInitiateTransaction();
        }
    }

    private void doInitiateTransaction()
    {
        if (NetworkAlertUtility.isInternetConnection2(activity))
        {
            String accessToken = new StorageUtil(activity).getAccessToken();
            String apikey = new StorageUtil(activity).getApiKey();
            HashMap<String, RequestBody> map = new HashMap<>();
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("message", message)
                    .addFormDataPart("txnid", txid)
                    .addFormDataPart("service", type)
                    .build();
            //==============================================================
            HashMap<String, String> headers = new HashMap<>();
            headers.put("headerToken", accessToken);
            headers.put("headerKey", apikey);
            presenter.doPayoutTransaction(activity,headers,requestBody,true);

        } else
        {
            NetworkAlertUtility.showNetworkFailureAlert(activity);
        }
    }

    @Override
    public void onRaisedComplainedSuccess(BaseResponse body)
    {
         if(body!=null){
             new CustomToastNotification(activity,body.getMessage());
         }
    }

    @Override
    public void onBankListSuccess(BaseResponse body) {

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