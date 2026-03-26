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
import com.app.rupiksha.databinding.ActivityPsapendingBinding;

public class PSAPendingActivity extends AppCompatActivity  implements View.OnClickListener {

    ActivityPsapendingBinding binding;
    Activity activity;
    String email;
    String phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_psapending);
        activity=PSAPendingActivity.this;
        if (getIntent().hasExtra("email"))
            email = getIntent().getStringExtra("email");

        if (getIntent().hasExtra("phone"))
            phone = getIntent().getStringExtra("phone");

        binding.contact.setOnClickListener(this);
        binding.btnsignin.setOnClickListener(this);
        binding.contact.setText(phone);
        binding.email.setText("Support Email: "+email);
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
//        switch (id) {
//            case R.id.btnsignin:
//                onBackPressed();
//                break;
//
//            case R.id.contact:
//               if(phone!=null) {
//                   Uri u = Uri.parse("tel:" + phone);
//                   Intent i = new Intent(Intent.ACTION_DIAL, u);
//                   try {
//                       startActivity(i);
//                   } catch (SecurityException s) {
//                       Toast.makeText(this, "An error occurred", Toast.LENGTH_LONG)
//                               .show();
//                   }
//               }
//                break;
//        }

        if (id == R.id.btnsignin) {
            onBackPressed();
        } else if (id == R.id.contact) {
            if (phone != null) {
                Uri u = Uri.parse("tel:" + phone);
                Intent i = new Intent(Intent.ACTION_DIAL, u);
                try {
                    startActivity(i);
                } catch (SecurityException s) {
                    Toast.makeText(this, "An error occurred", Toast.LENGTH_LONG);
                }
            }
        }
    }
}