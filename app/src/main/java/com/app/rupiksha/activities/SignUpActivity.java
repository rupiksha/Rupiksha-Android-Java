package com.app.rupiksha.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.rupiksha.R;
import com.app.rupiksha.adapters.RoleAdapter;
import com.app.rupiksha.apipresenter.RegistrationPresenter;
import com.app.rupiksha.databinding.ActivitySignUpBinding;
import com.app.rupiksha.extra.CustomToastNotification;
import com.app.rupiksha.extra.NetworkAlertUtility;
import com.app.rupiksha.interfaces.IRegisterView;
import com.app.rupiksha.models.BaseResponse;
import com.app.rupiksha.models.RoleModel;
import com.app.rupiksha.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener , IRegisterView {

    ActivitySignUpBinding binding;
    Activity activity;
    String name,phoneNumber,email,role;
    RegistrationPresenter presenter;
    private List<RoleModel> rolelist=new ArrayList<>();
    RoleAdapter roleAdapter;
    int role_id=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_sign_up);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_sign_up);
        activity=SignUpActivity.this;
        binding.includeLayout.toolBar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });
        presenter=new RegistrationPresenter();
        presenter.setView(this);
        getRoleList();
        setUpRoleList();
        binding.etrole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpRoleList();
            }
        });

        binding.btnsignin.setOnClickListener(this);
        binding.tvcontact.setOnClickListener(this);
        setSpannableText1(activity, binding.etsignin, getString(R.string.text_account), getString(R.string.text_login));
    }
    public void setSpannableText1(Activity context, TextView textView, String mainText, String s1) {
        SpannableString spannableString = new SpannableString(mainText);
        int startPositionS1 = mainText.indexOf(s1);
        int endPositionS1 = mainText.lastIndexOf(s1) + s1.length();
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent intent = new Intent(activity, SignInActivity.class);
                startActivity(intent);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                int linkColor = ContextCompat.getColor(context, R.color.color_F72F20);
                ds.setColor(linkColor);
                ds.setTypeface(ResourcesCompat.getFont(context, R.font.dmsans_medium));
                ds.setUnderlineText(false);
            }
        }, startPositionS1, endPositionS1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        textView.setText(spannableString);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void setUpRoleList()
    {
        roleAdapter = new RoleAdapter(this, R.layout.row_price_selector_item_dropdown, R.id.tvItemName, rolelist, Gravity.CENTER);
        binding.etrole.setAdapter(roleAdapter);
        binding.etrole.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = rolelist.get(position).getName();
                role_id=rolelist.get(position).getId();
                Log.e("name",name);
                Log.e("id", String.valueOf(role_id));
                binding.etrole.setText(name);
                binding.etrole.clearFocus();
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
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_email));

        }else if (!Utils.validateEmail(email)) {
            binding.etemail.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_valid_email));
        }else if (role_id==0) {
            binding.etrole.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_select_user_role));
        }else{
              userRegistration();
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
        if (id == R.id.btnsignin) {
            checkValidation();

        }
        if (id == R.id.tvcontact) {

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


    private void getRoleList()
    {
        if (NetworkAlertUtility.isInternetConnection2(activity))
        {
            HashMap<String, RequestBody> map = new HashMap<>();
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("demo", "")
                    .build();
            //=======================================
            presenter.getRole(activity,true);

        } else
        {
            NetworkAlertUtility.showNetworkFailureAlert(activity);
        }
    }

    private void userRegistration()
    {
        if (NetworkAlertUtility.isInternetConnection2(activity))
        {
            HashMap<String, RequestBody> map = new HashMap<>();
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("phone", phoneNumber)
                    .addFormDataPart("name", name)
                    .addFormDataPart("email", email)
                    .addFormDataPart("role", String.valueOf(role_id))
                    .build();
            //=======================================
            presenter.getRegistration(activity,requestBody,true);

        } else
        {
            NetworkAlertUtility.showNetworkFailureAlert(activity);
        }
    }

    @Override
    public void onRoleListSuccess(BaseResponse body)
    {
        if(body!=null){
           if(rolelist.size()>0)
               rolelist.clear();

           rolelist.addAll(body.getData().getRoles());
          // setUpRoleList();
        }


    }

    @Override
    public void onRegisterSuccess(BaseResponse body)
    {
        if(body!=null) {
            new CustomToastNotification(this, body.getMessage());
            Intent intent1 = new Intent(this, SignInActivity.class);
            startActivity(intent1);
            overridePendingTransition(R.anim.animate_slide_in_left, R.anim.animate_slide_out_right);
            finish();
        }
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.animate_slide_in_left, R.anim.animate_slide_out_right);
    }
}