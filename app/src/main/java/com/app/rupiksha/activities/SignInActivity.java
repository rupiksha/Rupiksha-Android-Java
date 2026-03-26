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
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.app.rupiksha.R;
import com.app.rupiksha.apipresenter.LoginPresenter;
import com.app.rupiksha.databinding.ActivitySignInBinding;
import com.app.rupiksha.databinding.ForgetpasswordbsheetBinding;
import com.app.rupiksha.databinding.ForgetpinBinding;
import com.app.rupiksha.extra.CustomToastNotification;
import com.app.rupiksha.extra.NetworkAlertUtility;
import com.app.rupiksha.interfaces.ILoginView;
import com.app.rupiksha.models.BaseResponse;
import com.app.rupiksha.utils.Utils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Objects;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener,ILoginView {

    ActivitySignInBinding binding;
    Activity activity;
    String ltype="pin";
    ForgetpasswordbsheetBinding myBottomBinding;
    BottomSheetDialog userBottomSheet;

    ForgetpinBinding myBottomBinding1;
    BottomSheetDialog userBottomSheet1;
    String phoneNumber,password,email;
    LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_sign_in);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_sign_in);
        activity=SignInActivity.this;
        presenter=new LoginPresenter();
        presenter.setView(this);

        binding.includeLayout.toolBar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });
        binding.btnLogin.setOnClickListener(this);
        binding.etforgetpassword.setOnClickListener(this);
        binding.etforgetpin.setOnClickListener(this);
        binding.tvcontact.setOnClickListener(this);
        setSpannableText1(activity, binding.etsignin, getString(R.string.text_create_account), getString(R.string.text_signup));

        binding.toggle.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
//                switch (checkedId){
//                    case R.id.radiopin:
//                        ltype="pin";
//                        break;
//                    case R.id.radiootp:
//                        ltype="otp";
//                        break;
//                }
                if (checkedId == R.id.radiopin){
                    ltype="pin";
                }else if (checkedId == R.id.radiootp){
                    ltype="otp";
                }

            }
        });

    }


    public void setSpannableText1(Activity context, TextView textView, String mainText, String s1) {
        SpannableString spannableString = new SpannableString(mainText);
        int startPositionS1 = mainText.indexOf(s1);
        int endPositionS1 = mainText.lastIndexOf(s1) + s1.length();
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent intent = new Intent(activity, SignUpActivity.class);
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
//        switch (id) {
//            case R.id.btnLogin:
//                    checkValidation();
//                    break;
//
//            case R.id.etforgetpassword:
//                    setbootomsheet();
//                    break;
//
//            case R.id.etforgetpin:
//                    setpinbootomsheet();
//                    break;
//
//        }

        if (id == R.id.btnLogin) {
            checkValidation();
        } else if (id == R.id.etforgetpassword) {
            setbootomsheet();
        } else if (id == R.id.etforgetpin) {
            setpinbootomsheet();
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

    private void checkValidation()
    {
        phoneNumber = Objects.requireNonNull(binding.etmobilenumber.getText()).toString();
        password = Objects.requireNonNull(binding.etpassword.getText()).toString();

       if (TextUtils.isEmpty(phoneNumber)) {
        binding.etmobilenumber.requestFocus();
        new CustomToastNotification(activity,getResources().getString(R.string.please_enter_mobile_number));

        }else if(TextUtils.isEmpty(password)){
           binding.etpassword.requestFocus();
           new CustomToastNotification(activity,getResources().getString(R.string.please_enter_password));

        }else{
           userLogin();
       }
    }


    private void userLogin()
    {
        if (NetworkAlertUtility.isInternetConnection2(activity))
        {
            String device_id = Utils.getAndroidId(this);
            String manufacturer = Build.MANUFACTURER;
            String model = Build.MODEL;
            String device_name = manufacturer + " " + model;
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("phone", phoneNumber)
                    .addFormDataPart("password", password)
                    .addFormDataPart("imei", device_id)
                    .addFormDataPart("device", device_name)
                    .addFormDataPart("ltype", ltype)
                    .build();
            //=======================================
            presenter.getLogin(activity,requestBody,true);

        } else
        {
            NetworkAlertUtility.showNetworkFailureAlert(activity);
        }
    }


    private void setbootomsheet()
    {
        userBottomSheet = new BottomSheetDialog(activity, R.style.CustomBottomSheetDialogThemeWithDim);
        myBottomBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.forgetpasswordbsheet, null, false);
        userBottomSheet.setContentView(myBottomBinding.getRoot());
        userBottomSheet.setCancelable(true);
        userBottomSheet.setCanceledOnTouchOutside(true);
        userBottomSheet.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);


        myBottomBinding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                    email = Objects.requireNonNull(myBottomBinding.etemail.getText()).toString();
                   if(TextUtils.isEmpty(email)){
                        myBottomBinding.etemail.requestFocus();
                        new CustomToastNotification(activity,getResources().getString(R.string.please_enter_email));

                    }else if (!Utils.validateEmail(email)) {
                        myBottomBinding.etemail.requestFocus();
                        new CustomToastNotification(activity,getResources().getString(R.string.please_enter_valid_email));
                    }else{

                       forgetPassword(email);

                    }
            }
        });

        userBottomSheet.show();
    }

    private void forgetPassword(String email)
    {
        if (NetworkAlertUtility.isInternetConnection2(activity))
        {
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("email", email)
                    .build();
            //=======================================
            presenter.getforgetpassword(activity,requestBody,true);
        } else
        {
            NetworkAlertUtility.showNetworkFailureAlert(activity);
        }
    }

    private void forgetPin(String email)
    {
        if (NetworkAlertUtility.isInternetConnection2(activity))
        {
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("email", email)
                    .build();
            //=======================================
            presenter.getforgetpin(activity,requestBody,true);
        } else
        {
            NetworkAlertUtility.showNetworkFailureAlert(activity);
        }
    }


    private void setpinbootomsheet()
    {
        userBottomSheet1 = new BottomSheetDialog(activity, R.style.CustomBottomSheetDialogThemeWithDim);
        myBottomBinding1 = DataBindingUtil.inflate(getLayoutInflater(), R.layout.forgetpin, null, false);
        userBottomSheet1.setContentView(myBottomBinding1.getRoot());
        userBottomSheet1.setCancelable(true);
        userBottomSheet1.setCanceledOnTouchOutside(true);
        userBottomSheet1.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);


        myBottomBinding1.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                email = Objects.requireNonNull(myBottomBinding1.etemail.getText()).toString();
                if(TextUtils.isEmpty(email)){
                    myBottomBinding1.etemail.requestFocus();
                    new CustomToastNotification(activity,getResources().getString(R.string.please_enter_email));

                }else if (!Utils.validateEmail(email)) {
                    myBottomBinding1.etemail.requestFocus();
                    new CustomToastNotification(activity,getResources().getString(R.string.please_enter_valid_email));
                }else{
                    forgetPin(email);

                }
            }
        });

        userBottomSheet1.show();
    }


    @Override
    public void onLoginSuccess(BaseResponse body)
    {
        if(body!=null)
        {
            new CustomToastNotification(this, body.getMessage());
            if (body.getMode().equals("otp")) {
                Intent intent1 = new Intent(this, OtpActivity.class)
                        .putExtra("logkey",body.getLogKey());
                startActivity(intent1);
                overridePendingTransition(R.anim.animate_slide_in_left, R.anim.animate_slide_out_right);
            } else {
                Intent intent1 = new Intent(this, PinVerifyActivity.class)
                        .putExtra("logkey",body.getLogKey());
                startActivity(intent1);
                overridePendingTransition(R.anim.animate_slide_in_left, R.anim.animate_slide_out_right);
            }
        }
    }

    @Override
    public void onForgetPasswordSuccess(BaseResponse body) {
        if(body!=null)
        {
            new CustomToastNotification(this, body.getMessage());
            userBottomSheet.dismiss();
        }
    }

    @Override
    public void onForgetPinSuccess(BaseResponse body) {
        if(body!=null)
        {
            new CustomToastNotification(this, body.getMessage());
            userBottomSheet1.dismiss();
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

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.animate_slide_in_left, R.anim.animate_slide_out_right);
    }
}