package com.app.rupiksha.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.app.rupiksha.R;
import com.app.rupiksha.apipresenter.EditProfilePresenter;
import com.app.rupiksha.databinding.ActivityEditProfileBinding;
import com.app.rupiksha.extra.CustomToastNotification;
import com.app.rupiksha.extra.NetworkAlertUtility;
import com.app.rupiksha.interfaces.IEditProfileView;
import com.app.rupiksha.models.BaseResponse;
import com.app.rupiksha.models.ModelUserInfo;
import com.app.rupiksha.storage.StorageUtil;
import com.app.rupiksha.utils.Utils;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener, IEditProfileView {

    ActivityEditProfileBinding binding;
    Activity activity;
    ModelUserInfo modelUserInfo;
    String name,phoneNumber,email,role;
    EditProfilePresenter presenter;
    File panImageFile;
    MultipartBody.Part uploadedPan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_edit_profile);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_edit_profile);
        activity=EditProfileActivity.this;
        presenter=new EditProfilePresenter();
        presenter.setView(this);
        binding.includeLayout.toolBar.setTitle(getResources().getString(R.string.title_edit_profile));
        binding.includeLayout.toolBar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });

        binding.btnsubmit.setOnClickListener(this);
        binding.civUserImage.setOnClickListener(this);
        setData();
    }


    private void setData()
    {
        modelUserInfo=new StorageUtil(activity).getUserInfo();
        if(modelUserInfo!=null)
        {
            binding.etname.setText(modelUserInfo.getName());
            binding.etmobilenumber.setText(modelUserInfo.getMobile());
            binding.etemail.setText(modelUserInfo.getEmail());
            if(!modelUserInfo.getProfile().isEmpty())
            {
                Glide.with(activity).load(modelUserInfo.getProfile()).placeholder(R.color.white_60).into(binding.civUserImage);
            }
        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
//        switch (id) {
//            case R.id.btnsubmit:
//                checkValidation();
//                break;
//
//            case R.id.civUserImage:
//                requestCameraPermission();
//                break;
//        }
        if (id == R.id.btnsubmit){
            checkValidation();
        }else if (id == R.id.civUserImage){
            requestCameraPermission();
        }
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
        }else{
            Edituser();
        }
    }
    private void requestCameraPermission()
    {
        Utils.hideSofthKeyboaard(activity, binding.getRoot());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        {
            Dexter.withActivity(activity)
                    .withPermissions(Manifest.permission.READ_MEDIA_IMAGES,
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_MEDIA_VIDEO,
                            Manifest.permission.READ_MEDIA_AUDIO)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                activity.startActivityForResult(Utils.getPickImageChooserIntent (activity), 280);
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).withErrorListener(error -> {/*Toast.makeText(context, "Error occurred! ", Toast.LENGTH_SHORT).show()*/})
                    .onSameThread()
                    .check();
        }else
        {
            Dexter.withActivity(activity)
                    .withPermissions(Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                activity.startActivityForResult(Utils.getPickImageChooserIntent(activity), 280);
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).withErrorListener(error -> {/*Toast.makeText(context, "Error occurred! ", Toast.LENGTH_SHORT).show()*/})
                    .onSameThread()
                    .check();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (requestCode == UCrop.REQUEST_CROP) {
            Uri resultUri = UCrop.getOutput(data);
            binding.civUserImage.setImageURI(resultUri);
        }

        if (requestCode == 280 && resultCode == Activity.RESULT_OK) {
            if (Utils.getPickImageResultUri(activity, data) != null)
            {
                Uri picUri = Utils.getPickImageResultUri(activity, data);
                String destinationFileName = "profile" + System.currentTimeMillis() + ".png";
                panImageFile = new File(activity.getCacheDir(), destinationFileName);
                Log.e("imagefile", String.valueOf(panImageFile));
                UCrop.of(picUri, Uri.fromFile(panImageFile)).withAspectRatio(1, 1).withOptions(Utils.getOptions(activity)).start((AppCompatActivity) activity);

            } else {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                Uri contentURI = Utils.getImageUri(activity, bitmap);
                String destinationFileName = "profile" + System.currentTimeMillis() + ".png";
                panImageFile = new File(activity.getCacheDir(), destinationFileName);
                // upimage = BitmapFactory.decodeFile(profileImageFile);
                Log.e("imagefile", String.valueOf(panImageFile));
                UCrop.of(contentURI, Uri.fromFile(panImageFile)).withAspectRatio(1, 1).withOptions(Utils.getOptions(activity)).start((AppCompatActivity) activity);

            }
        }
    }


    private void Edituser()
    {

        if (NetworkAlertUtility.isInternetConnection2(activity)) {
            String accessToken = new StorageUtil(activity).getAccessToken();
            String apikey = new StorageUtil(activity).getApiKey();
            HashMap<String, RequestBody> map = new HashMap<>();
            map.put("name", RequestBody.create(String.valueOf(name), MediaType.parse("multipart/form-data")));
            map.put("mobile", RequestBody.create(phoneNumber, MediaType.parse("multipart/form-data")));
            map.put("email", RequestBody.create(email, MediaType.parse("multipart/form-data")));

            //=======================================
            if(!(panImageFile == null))
            {
                Log.e("imagefile", String.valueOf(panImageFile));
                uploadedPan = MultipartBody.Part.createFormData("profile",
                        Utils.getFileName(this, Uri.fromFile(panImageFile)),
                        RequestBody.create(panImageFile, MediaType.parse("image/*")));
            }else{}
            //======================================
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("name", String.valueOf(name))
                    .addFormDataPart("mobile", String.valueOf(phoneNumber))
                    .addFormDataPart("email", String.valueOf(email))
                    .build();
            //=======================================
            HashMap<String, String> headers = new HashMap<>();
            headers.put("headerToken", accessToken);
            headers.put("headerKey", apikey);
            presenter.getEditProfile(activity, headers, map,uploadedPan,true);

        } else {
            NetworkAlertUtility.showNetworkFailureAlert(activity);
        }
    }

    @Override
    public void onEditProfileSuccess(BaseResponse body) {
        if(body!=null)
        {
            new CustomToastNotification(activity,""+body.getMessage());
            new StorageUtil(activity).saveUserInfo(body.getData().getProfile());
            onBackPressed();
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
}