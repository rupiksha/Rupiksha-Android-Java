package com.app.rupiksha.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.app.rupiksha.R;
import com.app.rupiksha.databinding.ActivityPendingKycBinding;

public class PendingKycActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityPendingKycBinding binding;
    Activity activity;
    String email;
    String phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_pending_kyc);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_pending_kyc);
        activity=PendingKycActivity.this;
        if (getIntent().hasExtra("email"))
            email = getIntent().getStringExtra("email");

        if (getIntent().hasExtra("phone"))
            phone = getIntent().getStringExtra("phone");

        binding.btnsignin.setOnClickListener(this);
        binding.contact.setOnClickListener(this);
        binding.contact.setText(phone);
        binding.email.setText("Support Email: "+email);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
//        switch (id) {
//            case R.id.btnsignin:
//                  finishAffinity();
//                break;
//
//            case R.id.contact:
//
//                    Uri u = Uri.parse("tel:" + phone);
//                    Intent i = new Intent(Intent.ACTION_DIAL, u);
//                    try {
//                        startActivity(i);
//                    } catch (SecurityException s) {
//                        Toast.makeText(this, "An error occurred", Toast.LENGTH_LONG)
//                                .show();
//                    }
//                break;
//        }
        if (id == R.id.btnsignin) {
            finishAffinity();
        } else if (id == R.id.contact) {
            Uri u = Uri.parse("tel:" + phone);
            Intent i = new Intent(Intent.ACTION_DIAL, u);
            try {
                startActivity(i);
            } catch (SecurityException s) {
                Toast.makeText(this, "An error occurred", Toast.LENGTH_LONG)
                        .show();

            }
        }
    }
}