package com.app.rupiksha.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.app.rupiksha.R;
import com.app.rupiksha.databinding.ActivityAddMemberBinding;
import com.app.rupiksha.extra.CustomToastNotification;
import com.app.rupiksha.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddMemberActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityAddMemberBinding binding;
    Activity activity;
    String name,phoneNumber,email,role;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_add_member);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_add_member);
        activity=AddMemberActivity.this;
        binding.includeLayout.toolBar.setTitle(getResources().getString(R.string.text_add_member));
        binding.includeLayout.toolBar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });
        binding.btnsignin.setOnClickListener(this);
        setUpCompanyEmployee();
    }

    private void setUpCompanyEmployee()
    {
        List<String> companyEmployee = new ArrayList<>();
        companyEmployee.add("Role 1");
        companyEmployee.add("Role 2");
        companyEmployee.add("Role 3");

        /*for (int i = 0; i < eventTypeList.size(); i++) {
            events.add(eventTypeList.get(i).getTitle());
        }*/

        ArrayAdapter aa = new ArrayAdapter(activity, android.R.layout.simple_spinner_item, companyEmployee);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.etrole.setAdapter(aa);
        binding.etrole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                binding.etrole.setText(companyEmployee.get(position));
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void checkValidation()
    {
        name = Objects.requireNonNull(binding.etname.getText()).toString();
        phoneNumber = Objects.requireNonNull(binding.etmobilenumber.getText()).toString();
        email = Objects.requireNonNull(binding.etemail.getText()).toString();
        if (TextUtils.isEmpty(name)) {
            binding.etname.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_name));
        }else if(TextUtils.isEmpty(phoneNumber)){
            binding.etmobilenumber.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_mobile_number));
        }else if(TextUtils.isEmpty(email)){
            binding.etemail.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_mobile_number));

        }else if (!Utils.validateEmail(email)) {
            binding.etemail.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_valid_email));
        }else{
            Intent intent1 = new Intent(this, LoginActivity.class);
            startActivity(intent1);
        }
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();
        Intent intent;
//        switch (id) {
//            case R.id.btnsignin:
//                checkValidation();
//                break;
//        }
        if (id == R.id.btnsignin)
        {
            checkValidation();
        }

    }
}