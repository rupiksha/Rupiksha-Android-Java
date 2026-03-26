package com.app.rupiksha.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.app.rupiksha.R;
import com.app.rupiksha.databinding.ActivityMyProfileBinding;
import com.app.rupiksha.models.KycModel;
import com.app.rupiksha.models.ModelUserInfo;
import com.app.rupiksha.storage.StorageUtil;

public class  MyProfileActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityMyProfileBinding binding;
    Activity activity;
    ModelUserInfo modelUserInfo;
    KycModel kycModel;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_my_profile);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_my_profile);
        activity=MyProfileActivity.this;
        binding.includeLayout.toolBar.setTitle(getResources().getString(R.string.title_my_profile));
        binding.includeLayout.toolBar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });
        binding.btnedit1.setOnClickListener(this);
        binding.civUserImage.setOnClickListener(this);
        setData();
    }

    private void setData()
    {
        kycModel=new StorageUtil(activity).getKYCInfo();
        modelUserInfo=new StorageUtil(activity).getUserInfo();

        if(modelUserInfo!=null){
            if(!modelUserInfo.getProfile().isEmpty())
            {
                Glide.with(activity).load(modelUserInfo.getProfile()).placeholder(R.color.white_60).into(binding.civUserImage);
            }
            binding.name.setText(modelUserInfo.getName());
            binding.mobile.setText(modelUserInfo.getMobile());
            binding.email.setText(modelUserInfo.getEmail());
        }
        if(kycModel!=null){
            binding.aadhaar.setText(" "+kycModel.getAdhaar());
            binding.pan.setText(kycModel.getPan());
            binding.address.setText(kycModel.getAddress());
        }
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();
//        switch (id) {
//            case R.id.btnedit1:
//                Intent intent1 = new Intent(this, EditProfileActivity.class);
//                startActivity(intent1);
//                break;
//        }
        if (id==R.id.btnedit1){
            Intent intent1 = new Intent(this, EditProfileActivity.class);
            startActivity(intent1);
        }
    }


}