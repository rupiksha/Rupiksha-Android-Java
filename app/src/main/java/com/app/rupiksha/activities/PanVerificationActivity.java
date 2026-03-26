package com.app.rupiksha.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.app.rupiksha.R;
import com.app.rupiksha.apipresenter.PanVerificationPresenter;
import com.app.rupiksha.constant.AppConstants;
import com.app.rupiksha.databinding.ActivityPanVerificationBinding;
import com.app.rupiksha.extra.CustomToastNotification;
import com.app.rupiksha.extra.NetworkAlertUtility;
import com.app.rupiksha.interfaces.IPanVerificationView;
import com.app.rupiksha.models.BaseResponse;
import com.app.rupiksha.models.PanDetailsModel;
import com.app.rupiksha.storage.StorageUtil;
import com.app.rupiksha.utils.GPSTracker;

import java.util.HashMap;
import java.util.Objects;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class PanVerificationActivity extends AppCompatActivity implements View.OnClickListener, IPanVerificationView,GPSTracker.GPSTrackerInterface {
    ActivityPanVerificationBinding binding;
    Activity activity;
    String accountnumber;
    private GPSTracker gps;
    private String stringLatitude = "0.0";
    private String stringLongitude = "0.0";
    private Location mLastLocation = null;
    private PanDetailsModel panDetailsModel ;

    PanVerificationPresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pan_verification);
        activity = PanVerificationActivity.this;
        presenter=new PanVerificationPresenter();
        presenter.setView(this);
        binding.includeLayout.toolBar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });
        binding.submit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
//        switch (id) {
//            case R.id.submit:
//                checkvalidation();
//                break;
//        }
        if (id==R.id.submit){
            checkvalidation();
        }
    }

    private void checkvalidation()
    {
        accountnumber = Objects.requireNonNull(binding.etaccountnumber.getText()).toString();

        if(TextUtils.isEmpty(accountnumber)) {
            binding.etaccountnumber.requestFocus();
            new CustomToastNotification(activity, getResources().getString(R.string.please_enter_pancard));
        }else{
            callPanVerificationApi();
        }
    }

    private void callPanVerificationApi() {
        {
            stringLatitude = StorageUtil.getString(this, AppConstants.KEY_LAST_LAT);
            stringLongitude = StorageUtil.getString(this, AppConstants.KEY_LAST_LNG);

            if (NetworkAlertUtility.isInternetConnection2(activity))
            {
                String accessToken = new StorageUtil(activity).getAccessToken();
                String apikey = new StorageUtil(activity).getApiKey();

                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("pan", accountnumber)
                        .addFormDataPart("lat", String.valueOf(stringLatitude))
                        .addFormDataPart("log", String.valueOf(stringLongitude))
                        .build();

                HashMap<String, String> headers = new HashMap<>();
                headers.put("headerToken", accessToken);
                headers.put("headerKey", apikey);
                presenter.getpanverication(activity,headers,requestBody,true);

            } else
            {
                NetworkAlertUtility.showNetworkFailureAlert(activity);
            }
        }
    }

    @Override
    public void onPanVerificationSuccess(BaseResponse body) {
        if(body!=null) {
            panDetailsModel = body.getData().getPanDetailsModel();
            setPanDetailsData(panDetailsModel);
        }
    }

    private void setPanDetailsData(PanDetailsModel panDetailsModel) {
        if(panDetailsModel != null) {
            binding.materialcard1.setVisibility(View.VISIBLE);
            binding.cName.setText(panDetailsModel.getName());
            binding.status.setText(panDetailsModel.getPanStatus());
            binding.gender.setText(panDetailsModel.getGender());
            binding.date.setText(panDetailsModel.getDob());
            binding.constitution.setText(panDetailsModel.getConstitution());
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
        new CustomToastNotification(this, reason);
    }
    @Override
    public void onUserLocationChanged(Location location) {
        mLastLocation = location;
        if (gps.canGetLocation()) {
            StorageUtil.getSharedPref(this).edit()
                    .putString(AppConstants.KEY_LAST_LAT, "" + mLastLocation.getLatitude())
                    .putString(AppConstants.KEY_LAST_LNG, "" + mLastLocation.getLongitude())
                    .apply();

            Log.e("lat", String.valueOf(mLastLocation.getLatitude()));
            Log.e("log", String.valueOf(mLastLocation.getLongitude()));

        } else {
            gps.showSettingsAlert();
        }
    }
}