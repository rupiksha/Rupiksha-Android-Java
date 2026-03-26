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
import com.app.rupiksha.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityLoginBinding binding;
    Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_login);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_login);
        activity=LoginActivity.this;
        binding.btnLogin.setOnClickListener(this);
        binding.btnsignup.setOnClickListener(this);
        binding.tvcontact.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();
        Intent intent;
//        switch (id) {
//            case R.id.btnLogin:
//                Intent  intent1 = new Intent(this, SignInActivity.class);
//                startActivity(intent1);
//                break;
//
//            case R.id.btnsignup:
//                intent = new Intent(this, SignUpActivity.class);
//                startActivity(intent);
//                break;
//        }
        if (id == R.id.btnLogin) {
            intent = new Intent(this, SignInActivity.class);
            overridePendingTransition(R.anim.animate_slide_in_left, R.anim.animate_slide_out_right);
            startActivity(intent);

        } else if (id == R.id.btnsignup) {
            intent = new Intent(this, SignUpActivity.class);
            overridePendingTransition(R.anim.animate_slide_in_left, R.anim.animate_slide_out_right);
            startActivity(intent);

        }else if (id == R.id.tvcontact) {

        Uri u = Uri.parse("tel:" + "+91 7004128310");
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