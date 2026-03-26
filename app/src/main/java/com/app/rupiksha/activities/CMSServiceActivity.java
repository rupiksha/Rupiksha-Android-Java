package com.app.rupiksha.activities;

import static com.app.rupiksha.constant.AppConstants.REQUEST_LOCATION_PERMISSION;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import com.app.rupiksha.R;
import com.app.rupiksha.apipresenter.AEPSServicePresenter;
import com.app.rupiksha.apipresenter.CMSServicePresenter;
import com.app.rupiksha.constant.AppConstants;
import com.app.rupiksha.databinding.ActivityCmsserviceBinding;
import com.app.rupiksha.extra.CustomToastNotification;
import com.app.rupiksha.extra.NetworkAlertUtility;
import com.app.rupiksha.interfaces.ICMSserviceView;
import com.app.rupiksha.models.BaseResponse;
import com.app.rupiksha.storage.StorageUtil;
import com.app.rupiksha.utils.GPSTracker;
import com.app.rupiksha.utils.Utils;

import java.util.HashMap;
import java.util.Objects;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class CMSServiceActivity extends AppCompatActivity implements ICMSserviceView , View.OnClickListener, GPSTracker.GPSTrackerInterface {

    ActivityCmsserviceBinding binding;
    CMSServicePresenter presenter;
    Activity activity;
    Context context;
    private GPSTracker gps;
    private String stringLatitude = "0.0";
    private String stringLongitude = "0.0";
    private Location mLastLocation = null;
    String title="";
    boolean type=false;
    String mobilenumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_cmsservice);
        activity=CMSServiceActivity.this;
        context=CMSServiceActivity.this;
        presenter= new CMSServicePresenter();
        presenter.setView(this);
        binding.includeLayout.toolBar.setTitle("CMS");
        binding.includeLayout.toolBar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });
        if (getIntent().hasExtra("title"))
            title = getIntent().getStringExtra("title");

        if (getIntent().hasExtra("type"))
            type = getIntent().getBooleanExtra("type",false);

        binding.tvtitle.setText(""+title);
        binding.includeLayout.toolBar.setTitle(""+title);


        if (checkLocationPermission()) {
            if (gps == null)
                gps = new GPSTracker(activity, this);
            mLastLocation = gps.getLocation();
            if (gps.canGetLocation() && mLastLocation != null) {
                StorageUtil.getSharedPref(this).edit()
                        .putString(AppConstants.KEY_LAST_LAT, "" + mLastLocation.getLatitude())
                        .putString(AppConstants.KEY_LAST_LNG, "" + mLastLocation.getLongitude())
                        .apply();
                // getUserStatus();
            } else {
                gps.showSettingsAlert();
            }
        }
        binding.btnsubmit.setOnClickListener(this);

    }



    private void checkValidation()
    {

        mobilenumber = Objects.requireNonNull(binding.etmobilenumber.getText()).toString();

        if(TextUtils.isEmpty(mobilenumber)){
            binding.etmobilenumber.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_mobile_number));
        }else{
            cmsServices();
        }
    }

    private void cmsServices()
    {
        stringLatitude = StorageUtil.getString(this, AppConstants.KEY_LAST_LAT);
        stringLongitude = StorageUtil.getString(this, AppConstants.KEY_LAST_LNG);
        mobilenumber = Objects.requireNonNull(binding.etmobilenumber.getText()).toString();

        if (NetworkAlertUtility.isInternetConnection2(activity)) {
            String accessToken = new StorageUtil(activity).getAccessToken();
            String apikey = new StorageUtil(activity).getApiKey();
            //======================================
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("mobile", mobilenumber)
                    .addFormDataPart("lat", String.valueOf(stringLatitude))
                    .addFormDataPart("log", String.valueOf(stringLongitude))
                    .build();
            //=======================================
            HashMap<String, String> headers = new HashMap<>();
            headers.put("headerToken", accessToken);
            headers.put("headerKey", apikey);
            presenter.getCMSService(activity, headers, requestBody,true);

        } else {
            NetworkAlertUtility.showNetworkFailureAlert(activity);
        }

    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onCMSserviceSuccess(BaseResponse body) {
        if(body!=null){
            new CustomToastNotification(activity,""+body.getMessage());
            String url=body.getUrl();
            if(url!=null){
                Log.e("url1",""+url);
                openMedia(url);
            }else{
                Log.e("url2",""+url);
            }

        }
    }
    private void openMedia(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
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

    @Override
    public void onClick(View view) {
        int id = view.getId();
//        switch (id) {
//            case R.id.btnverify:
//                checkValidation("verify");
//
//                break;
//            case R.id.btnpaynow:
//                checkValidation("paynow");
//                break;
//        }
        if (id == R.id.btnsubmit)
        {
           checkValidation();
        }
    }

    @Override
    public void onUserLocationChanged(Location location) {

    }
}