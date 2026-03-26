package com.app.rupiksha.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;

import com.app.rupiksha.R;
import com.app.rupiksha.adapters.StateAdapter;
import com.app.rupiksha.apipresenter.PSARegisterPresenter;
import com.app.rupiksha.databinding.ActivityPsaregistrationBinding;
import com.app.rupiksha.extra.CustomToastNotification;
import com.app.rupiksha.extra.NetworkAlertUtility;
import com.app.rupiksha.interfaces.IPSARegistrationView;
import com.app.rupiksha.models.BaseResponse;
import com.app.rupiksha.models.StateModel;
import com.app.rupiksha.storage.StorageUtil;
import com.app.rupiksha.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class PSARegistrationActivity extends AppCompatActivity implements View.OnClickListener,IPSARegistrationView {

    ActivityPsaregistrationBinding binding;
    Activity activity;
    PSARegisterPresenter presenter;
    StateAdapter stateAdapter;
    int stateid;
    private List<StateModel> statelist=new ArrayList<>();
    String name,phoneNumber,email,shopname,address,pincode,city,state,pannumber,adharnumber,dob,shopaddress;
    String title="",phone;
    boolean type=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_psaregistration);
        activity=PSARegistrationActivity.this;
        presenter=new PSARegisterPresenter();
        presenter.setView(this);
        if (getIntent().hasExtra("email"))
            email = getIntent().getStringExtra("email");

        if (getIntent().hasExtra("phone"))
            phone = getIntent().getStringExtra("phone");

        binding.includeLayout.toolBar.setTitle(getResources().getString(R.string.label_psa_registration));
        binding.includeLayout.toolBar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });
        getStateList();
        binding.btnsubmit.setOnClickListener(this);
    }
    private void getStateList()
    {
        if (NetworkAlertUtility.isInternetConnection2(activity))
        {
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("demo", "")
                    .build();
            //=======================================
            presenter.getStateList(activity,true);

        } else
        {
            NetworkAlertUtility.showNetworkFailureAlert(activity);
        }
    }

    private void setUpBanklist()
    {
        binding.etstate.setThreshold(100);
        stateAdapter = new StateAdapter(this, R.layout.row_price_selector_item_dropdown, R.id.tvItemName, statelist, Gravity.CENTER);
        binding.etstate.setAdapter(stateAdapter);
        binding.etstate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String name = statelist.get(position).getName();
                stateid=statelist.get(position).getId();

                binding.etstate.setText(name);
                binding.etstate.clearFocus();

            }
        });

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
//        switch (id) {
//            case R.id.btnsubmit:
//                checkValidation();
//                break;
//        }
         if (id == R.id.btnsubmit) {
             checkValidation();
         }
    }

    private void checkValidation()
    {
        name = Objects.requireNonNull(binding.etname.getText()).toString();
        phoneNumber = Objects.requireNonNull(binding.etmobilenumber.getText()).toString();
        email = Objects.requireNonNull(binding.etemail.getText()).toString();
        shopaddress = Objects.requireNonNull(binding.etshopaddress.getText()).toString();
        address = Objects.requireNonNull(binding.etaddress.getText()).toString();
        pincode = Objects.requireNonNull(binding.etpincode.getText()).toString();
        state = Objects.requireNonNull(binding.etstate.getText()).toString();
        pannumber = Objects.requireNonNull(binding.etpan.getText()).toString();
        adharnumber = Objects.requireNonNull(binding.etadhar.getText()).toString();
        if (TextUtils.isEmpty(name)) {
            binding.etname.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_name));
        }/*else if (!TextUtils.isEmpty(dob)) {
            binding.etdob.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_dob));
        }*/else if(TextUtils.isEmpty(phoneNumber)){
            binding.etmobilenumber.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_mobile_number));
        }else if(TextUtils.isEmpty(email)){
            binding.etemail.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_email));
        }else if (!Utils.validateEmail(email)) {
            binding.etemail.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_valid_email));
        }/*else if (TextUtils.isEmpty(shopname)){
            binding.etshop.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_shop_name));
        }*/else if (TextUtils.isEmpty(shopaddress)){
            binding.etshopaddress.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_shopaddress));
        }else if (TextUtils.isEmpty(address)){
            binding.etaddress.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_address));
        }else if (TextUtils.isEmpty(pincode)){
            binding.etpincode.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_pincode));
        }else if (TextUtils.isEmpty(state)){
            binding.etstate.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_state));
        }else if (TextUtils.isEmpty(pannumber)){
            binding.etpan.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_pancard));
        }else if (TextUtils.isEmpty(adharnumber)){
            binding.etadhar.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_aadhar));
        } else{

            callUserKyc();

        }
    }

    private void callUserKyc()
    {
        if (NetworkAlertUtility.isInternetConnection2(activity))
        {

            String accessToken = new StorageUtil(activity).getAccessToken();
            String apikey = new StorageUtil(activity).getApiKey();

            HashMap<String, RequestBody> map = new HashMap<>();
            map.put("name", RequestBody.create(name, MediaType.parse("multipart/form-data")));
            map.put("aadhar", RequestBody.create(adharnumber, MediaType.parse("multipart/form-data")));
            map.put("email", RequestBody.create(email, MediaType.parse("multipart/form-data")));
            map.put("phone", RequestBody.create(phoneNumber, MediaType.parse("multipart/form-data")));
            map.put("pan", RequestBody.create(pannumber, MediaType.parse("multipart/form-data")));
            map.put("address", RequestBody.create(address, MediaType.parse("multipart/form-data")));
            map.put("slocation", RequestBody.create(shopaddress , MediaType.parse("multipart/form-data")));
            map.put("pincode", RequestBody.create(pincode, MediaType.parse("multipart/form-data")));
            map.put("state", RequestBody.create(String.valueOf(stateid), MediaType.parse("multipart/form-data")));

            HashMap<String, String> headers = new HashMap<>();
            headers.put("headerToken", accessToken);
            headers.put("headerKey", apikey);
            presenter.getPSARegistration(activity, headers, map,true);
        } else {
            NetworkAlertUtility.showNetworkFailureAlert(activity);
        }
    }


    @Override
    public void onPSARegistrationSuccess(BaseResponse body)
    {
        if(body!=null){
            new CustomToastNotification(activity,body.getMessage());
            startActivity(new Intent(activity, PSAPendingActivity.class) .putExtra("email",email)
                    .putExtra("phone",phone));
            finish();
        }

    }


    @Override
    public void onStateListSuccess(BaseResponse body) {
        if(body!=null){
            if(statelist.size()>0)
                statelist.clear();

            statelist.addAll(body.getData().getState());
            setUpBanklist();
        }
    }

    @Override
    public Context getContext()
    {
        return null;
    }

    @Override
    public void enableLoadingBar(boolean enable, String s)
    {
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
    public void onErrorToast(String reason)
    {
        new CustomToastNotification(this, reason);
    }


}