package com.app.rupiksha.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.app.rupiksha.R;
import com.app.rupiksha.databinding.ActivityQrcodeBinding;

public class QRCodeActivity extends AppCompatActivity  implements View.OnClickListener {

    ActivityQrcodeBinding binding;
    Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_qrcode);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_qrcode);
        activity=QRCodeActivity.this;
        binding.includeLayout.toolBar.setTitle(getResources().getString(R.string.text_accept_qr));
        binding.includeLayout.toolBar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });
        binding.btnshare.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();
        Intent intent;
//        switch (id) {
//            case R.id.btnshare:
//               onBackPressed();
//                break;
//        }
        if (id == R.id.btnshare) {
            onBackPressed();
        }
    }
}