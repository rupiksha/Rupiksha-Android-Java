package com.app.rupiksha.activities;

import static com.app.rupiksha.utils.Utils.parseDate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;

import com.app.rupiksha.R;
import com.app.rupiksha.adapters.DocumentAdapter;
import com.app.rupiksha.adapters.StateAdapter;
import com.app.rupiksha.apipresenter.KYCUserPresenter;
import com.app.rupiksha.databinding.ActivityKycUserBinding;
import com.app.rupiksha.extra.CustomToastNotification;
import com.app.rupiksha.extra.NetworkAlertUtility;
import com.app.rupiksha.interfaces.IUserKycView;
import com.app.rupiksha.models.AddressProof;
import com.app.rupiksha.models.BaseResponse;
import com.app.rupiksha.models.StateModel;
import com.app.rupiksha.storage.StorageUtil;
import com.app.rupiksha.utils.Utils;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class KycUserActivity extends AppCompatActivity  implements View.OnClickListener, IUserKycView {

    ActivityKycUserBinding binding;
    Activity activity;
    File panImageFile;
    File adharfrontFile;
    File adharbackFile;
    File docFile;
    File selfieFile;
    boolean pan=false,adharfront=false,adharback=false,docimage=false,selfiimage=false;
    String name,phoneNumber,email,shopname,address,pincode,city,state,pannumber,adharnumber,dob,shopaddress;
    KYCUserPresenter presenter;
    StateAdapter stateAdapter;

    DocumentAdapter documentAdapter;
    int stateid;

    String doctype;
    private List<StateModel> statelist=new ArrayList<>();

    private List<AddressProof> documentlist=new ArrayList<>();
    MultipartBody.Part uploadedPan;
    MultipartBody.Part uploadedAadharFront;
    MultipartBody.Part uploadedAadharBack;
    MultipartBody.Part uploaddocument;
    MultipartBody.Part uploadselfie;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_kyc_user);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_kyc_user);
        activity=KycUserActivity.this;
        presenter=new KYCUserPresenter();
        presenter.setView(this);
        binding.includeLayout.toolBar.setTitle(getResources().getString(R.string.label_user_kyc));
        binding.includeLayout.toolBar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });
        binding.btnsubmit.setOnClickListener(this);
        binding.etdob.setOnClickListener(this);
        binding.uploadpan.setOnClickListener(this);
        binding.uploadfront.setOnClickListener(this);
        binding.uploadback.setOnClickListener(this);
        binding.uploaddoc.setOnClickListener(this);
        binding.uploadselfie.setOnClickListener(this);
        getStateList();
        getdocumentList();
        setUpBanklist();
        setUpDoclist();
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();
//        switch (id) {
//            case R.id.btnsubmit:
//                checkValidation();
//
//                break;
//            case R.id.uploadpan:
//                requestCameraPermission();
//                break;
//
//            case R.id.uploadfront:
//                requestCameraPermission1();
//                break;
//
//            case R.id.uploadback:
//                requestCameraPermission2();
//                break;
//
//            case R.id.etdob:
//                showDatePickerDOBDialog();
//                break;
//        }
       if (id == R.id.btnsubmit){
           checkValidation();
       }else if (id == R.id.uploadpan){
           requestCameraPermission();
       }else if (id == R.id.uploadfront){
           requestCameraPermission1();
       }else if (id == R.id.uploadback){
           requestCameraPermission2();
       }else if (id == R.id.uploaddoc){
           requestCameraPermission3();
       }else if (id == R.id.uploadselfie){
           requestCameraPermission4();
       } else if (id == R.id.etdob) {
           showDatePickerDOBDialog();
       }
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

    private void getdocumentList()
    {
        if (NetworkAlertUtility.isInternetConnection2(activity))
        {
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("demo", "")
                    .build();
            //=======================================
            presenter.getDocumentList(activity,true);

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

    private void setUpDoclist()
    {
        binding.etdocument.setThreshold(100);
        documentAdapter = new DocumentAdapter(this, R.layout.row_price_selector_item_dropdown, R.id.tvItemName, documentlist, Gravity.CENTER);
        binding.etdocument.setAdapter(documentAdapter);
        binding.etdocument.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String name = documentlist.get(position).getType();
                doctype=documentlist.get(position).getType();
                binding.etdocument.setText(name);
                binding.etdocument.clearFocus();

            }
        });

    }

    private void showDatePickerDOBDialog()
    {
        Locale.setDefault(Locale.getDefault());
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        c.add(Calendar.YEAR, -18);
        DatePickerDialog datePickerDialog = new DatePickerDialog(activity, R.style.MyDialogTheme, (view, year, monthOfYear, dayOfMonth) -> {
            String d = dayOfMonth < 10 ? "0" + dayOfMonth : "" + dayOfMonth;
            String m = (monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : "" + (monthOfYear + 1);
            dob = String.valueOf(year).concat("-").concat(m).concat("-").concat(d);
            binding.etdob.setText(parseDate(d + "-" + m + "-" + year));
        }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
        datePickerDialog.show();
    }


    private void checkValidation()
    {
        name = Objects.requireNonNull(binding.etname.getText()).toString();

        shopname = Objects.requireNonNull(binding.etshop.getText()).toString();
        shopaddress = Objects.requireNonNull(binding.etaddress.getText()).toString();
        address = Objects.requireNonNull(binding.etaddress.getText()).toString();
        pincode = Objects.requireNonNull(binding.etpincode.getText()).toString();
         city= Objects.requireNonNull(binding.etcity.getText()).toString();
        state = Objects.requireNonNull(binding.etstate.getText()).toString();
        pannumber = Objects.requireNonNull(binding.etpan.getText()).toString();
        adharnumber = Objects.requireNonNull(binding.etadhar.getText()).toString();
        if (TextUtils.isEmpty(name)) {
            binding.etname.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_fathername));
        }/*else if(TextUtils.isEmpty(phoneNumber)){
            binding.etmobilenumber.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_mobile_number));
        }else if(TextUtils.isEmpty(email)){
            binding.etemail.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_email));
        }*/else if (TextUtils.isEmpty(dob)) {
            binding.etdob.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_dob));
        }else if (TextUtils.isEmpty(shopname)){
            binding.etshop.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_shop_name));
        }else if (TextUtils.isEmpty(shopaddress)){
            binding.etshopaddress.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_shopaddress));
        }else if (TextUtils.isEmpty(address)){
            binding.etaddress.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_address));
        }else if (TextUtils.isEmpty(pincode)){
            binding.etpincode.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_pincode));
        }else if (TextUtils.isEmpty(city)){
            binding.etcity.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_city));
        }else if (TextUtils.isEmpty(state)){
            binding.etstate.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_state));
        }else if (TextUtils.isEmpty(pannumber)){
            binding.etpan.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_pancard));
        }else if (panImageFile==null){
            new CustomToastNotification(activity,getResources().getString(R.string.please_upload_pan_image));
        }else if (TextUtils.isEmpty(adharnumber)){
            binding.etadhar.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_aadhar));
        }else if (adharfrontFile==null){
            new CustomToastNotification(activity,getResources().getString(R.string.please_upload_aadhar_front));
        }else if (adharbackFile==null){
            new CustomToastNotification(activity,getResources().getString(R.string.please_upload_aadhar_back));
        }else if (TextUtils.isEmpty(doctype)){
              binding.etdocument.requestFocus();
              new CustomToastNotification(activity,getResources().getString(R.string.please_enter_document));
         }else if (docFile==null){
            new CustomToastNotification(activity,getResources().getString(R.string.please_upload_doc_file));
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
                map.put("fatherName", RequestBody.create(name, MediaType.parse("multipart/form-data")));
                map.put("aadharNumber", RequestBody.create(adharnumber, MediaType.parse("multipart/form-data")));
                map.put("panNumber", RequestBody.create(pannumber, MediaType.parse("multipart/form-data")));
                map.put("address", RequestBody.create(address, MediaType.parse("multipart/form-data")));
                map.put("shopAddress", RequestBody.create(shopaddress, MediaType.parse("multipart/form-data")));
                map.put("dob", RequestBody.create(dob, MediaType.parse("multipart/form-data")));
                map.put("shopName", RequestBody.create(shopname , MediaType.parse("multipart/form-data")));
                map.put("pinCode", RequestBody.create(pincode, MediaType.parse("multipart/form-data")));
                map.put("district", RequestBody.create(city, MediaType.parse("multipart/form-data")));
                map.put("state", RequestBody.create(String.valueOf(stateid), MediaType.parse("multipart/form-data")));
                map.put("address_proof", RequestBody.create(String.valueOf(doctype), MediaType.parse("multipart/form-data")));
                if(!(panImageFile == null))
                {
                    uploadedPan = MultipartBody.Part.createFormData("panImage",
                            Utils.getFileName(this, Uri.fromFile(panImageFile)),
                            RequestBody.create(panImageFile,MediaType.parse("image/*")));
                }else{}
                if(!(adharfrontFile == null))
                {
                    uploadedAadharFront = MultipartBody.Part.createFormData("aadharFront",
                            Utils.getFileName(this, Uri.fromFile(adharfrontFile)),
                            RequestBody.create(adharfrontFile,MediaType.parse("image/*")));
                }else{}
                if(!(adharbackFile == null))
                {
                    uploadedAadharBack = MultipartBody.Part.createFormData("aadharBack",
                            Utils.getFileName(this, Uri.fromFile(adharbackFile)),
                            RequestBody.create(adharbackFile,MediaType.parse("image/*")));
                }else{}
                if(!(docFile == null))
                {
                    uploaddocument = MultipartBody.Part.createFormData("address_image",
                            Utils.getFileName(this, Uri.fromFile(docFile)),
                            RequestBody.create(docFile,MediaType.parse("image/*")));
                }else{}
                if(!(selfieFile == null))
                {
                    uploadselfie = MultipartBody.Part.createFormData("selfie_with_employee",
                            Utils.getFileName(this, Uri.fromFile(selfieFile)),
                            RequestBody.create(selfieFile,MediaType.parse("image/*")));
                }else{}

                HashMap<String, String> headers = new HashMap<>();
                headers.put("headerToken", accessToken);
                headers.put("headerKey", apikey);
                presenter.getKyc(activity, headers, map,true,uploadedPan,uploadedAadharFront,uploadedAadharBack,uploaddocument,uploadselfie);
        } else {
            NetworkAlertUtility.showNetworkFailureAlert(activity);
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

    private void requestCameraPermission1()
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
                                activity.startActivityForResult(Utils.getPickImageChooserIntent(activity), 285);
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
                                activity.startActivityForResult(Utils.getPickImageChooserIntent(activity), 285);
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

    private void requestCameraPermission2()
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
                                activity.startActivityForResult(Utils.getPickImageChooserIntent(activity), 290);
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
                                activity.startActivityForResult(Utils.getPickImageChooserIntent(activity), 290);
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

    private void requestCameraPermission3()
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
                                activity.startActivityForResult(Utils.getPickImageChooserIntent(activity), 295);
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
                                activity.startActivityForResult(Utils.getPickImageChooserIntent(activity), 295);
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

    private void requestCameraPermission4()
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
                                activity.startActivityForResult(Utils.getPickImageChooserIntent(activity), 300);
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
                                activity.startActivityForResult(Utils.getPickImageChooserIntent(activity), 300);
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (requestCode == UCrop.REQUEST_CROP) {
            Uri resultUri = UCrop.getOutput(data);
            if(pan)
            binding.panimg.setImageURI(resultUri);
            if(adharfront)
                binding.adfront.setImageURI(resultUri);
            if(adharback)
                binding.adback.setImageURI(resultUri);
            if(docimage)
                binding.addoc.setImageURI(resultUri);
            if(selfiimage)
                binding.adselfie.setImageURI(resultUri);
        }

        if (requestCode == 280 && resultCode == Activity.RESULT_OK)
        {
            if (Utils.getPickImageResultUri(activity,data) != null) {
                pan=true;
                adharfront=false;
                adharback=false;
                docimage=false;
                selfiimage=false;
                Uri picUri = Utils.getPickImageResultUri(activity, data);
                String destinationFileName = "profile" + System.currentTimeMillis() + ".png";
                panImageFile = new File(activity.getCacheDir(), destinationFileName);
                Log.d("imagefile", String.valueOf(panImageFile));
                UCrop.of(picUri, Uri.fromFile(panImageFile)).withAspectRatio(16, 9).withOptions(Utils.getOptions(activity)).start((AppCompatActivity) activity);

            } else {
                pan=true;
                adharfront=false;
                adharback=false;
                docimage=false;
                selfiimage=false;
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                Uri contentURI = Utils.getImageUri(activity, bitmap);
                String destinationFileName = "profile" + System.currentTimeMillis() + ".png";
                panImageFile = new File(activity.getCacheDir(), destinationFileName);
                // upimage = BitmapFactory.decodeFile(profileImageFile);
                Log.d("imagefile", String.valueOf(panImageFile));
                UCrop.of(contentURI, Uri.fromFile(panImageFile)).withAspectRatio(16, 9).withOptions(Utils.getOptions(activity)).start((AppCompatActivity) activity);

            }
        }

        if (requestCode == 285 && resultCode == Activity.RESULT_OK)
        {
            if (Utils.getPickImageResultUri(activity,data) != null) {
                pan=false;
                adharfront=true;
                adharback=false;
                docimage=false;
                selfiimage=false;
                Uri picUri = Utils.getPickImageResultUri(activity, data);
                String destinationFileName = "profile" + System.currentTimeMillis() + ".png";
                adharfrontFile = new File(activity.getCacheDir(), destinationFileName);
                Log.d("imagefile", String.valueOf(adharfrontFile));
                UCrop.of(picUri, Uri.fromFile(adharfrontFile)).withAspectRatio(16, 9).withOptions(Utils.getOptions(activity)).start((AppCompatActivity) activity);
            } else {
                pan=false;
                adharfront=true;
                adharback=false;
                docimage=false;
                selfiimage=false;
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                Uri contentURI = Utils.getImageUri(activity, bitmap);
                String destinationFileName = "profile" + System.currentTimeMillis() + ".png";
                adharfrontFile = new File(activity.getCacheDir(), destinationFileName);
                // upimage = BitmapFactory.decodeFile(profileImageFile);
                Log.d("imagefile", String.valueOf(adharfrontFile));
                UCrop.of(contentURI, Uri.fromFile(adharfrontFile)).withAspectRatio(16, 9).withOptions(Utils.getOptions(activity)).start((AppCompatActivity) activity);
            }
        }

        if (requestCode == 290 && resultCode == Activity.RESULT_OK)
        {
            if (Utils.getPickImageResultUri(activity,data) != null)
            {
                pan=false;
                adharfront=false;
                adharback=true;
                docimage=false;
                selfiimage=false;
                Uri picUri = Utils.getPickImageResultUri(activity, data);
                String destinationFileName = "profile" + System.currentTimeMillis() + ".png";
                adharbackFile = new File(activity.getCacheDir(), destinationFileName);
                Log.d("imagefile", String.valueOf(adharbackFile));
                UCrop.of(picUri, Uri.fromFile(adharbackFile)).withAspectRatio(1, 1).withOptions(Utils.getOptions(activity)).start((AppCompatActivity) activity);
            } else {
                pan=false;
                adharfront=false;
                adharback=true;
                docimage=false;
                selfiimage=false;
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                Uri contentURI = Utils.getImageUri(activity, bitmap);
                String destinationFileName = "profile" + System.currentTimeMillis() + ".png";
                adharbackFile = new File(activity.getCacheDir(), destinationFileName);
                // upimage = BitmapFactory.decodeFile(profileImageFile);
                Log.d("imagefile", String.valueOf(adharbackFile));
                UCrop.of(contentURI, Uri.fromFile(adharbackFile)).withAspectRatio(1, 1).withOptions(Utils.getOptions(activity)).start((AppCompatActivity) activity);
            }
        }

        if (requestCode == 295 && resultCode == Activity.RESULT_OK)
        {
            if (Utils.getPickImageResultUri(activity,data) != null)
            {
                pan=false;
                adharfront=false;
                adharback=false;
                docimage=true;
                selfiimage=false;
                Uri picUri = Utils.getPickImageResultUri(activity, data);
                String destinationFileName = "profile" + System.currentTimeMillis() + ".png";
                docFile = new File(activity.getCacheDir(), destinationFileName);
                Log.d("imagefile", String.valueOf(docFile));
                UCrop.of(picUri, Uri.fromFile(docFile)).withAspectRatio(1, 1).withOptions(Utils.getOptions(activity)).start((AppCompatActivity) activity);
            } else {
                pan=false;
                adharfront=false;
                adharback=false;
                docimage=true;
                selfiimage=false;
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                Uri contentURI = Utils.getImageUri(activity, bitmap);
                String destinationFileName = "profile" + System.currentTimeMillis() + ".png";
                docFile = new File(activity.getCacheDir(), destinationFileName);
                // upimage = BitmapFactory.decodeFile(profileImageFile);
                Log.d("imagefile", String.valueOf(docFile));
                UCrop.of(contentURI, Uri.fromFile(docFile)).withAspectRatio(1, 1).withOptions(Utils.getOptions(activity)).start((AppCompatActivity) activity);
            }
        }
        if (requestCode == 300 && resultCode == Activity.RESULT_OK)
        {
            if (Utils.getPickImageResultUri(activity,data) != null)
            {
                pan=false;
                adharfront=false;
                adharback=false;
                docimage=false;
                selfiimage=true;
                Uri picUri = Utils.getPickImageResultUri(activity, data);
                String destinationFileName = "profile" + System.currentTimeMillis() + ".png";
                selfieFile = new File(activity.getCacheDir(), destinationFileName);
                Log.d("imagefile", String.valueOf(selfieFile));
                UCrop.of(picUri, Uri.fromFile(selfieFile)).withAspectRatio(1, 1).withOptions(Utils.getOptions(activity)).start((AppCompatActivity) activity);
            } else {
                pan=false;
                adharfront=false;
                adharback=false;
                docimage=false;
                selfiimage=true;
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                Uri contentURI = Utils.getImageUri(activity, bitmap);
                String destinationFileName = "profile" + System.currentTimeMillis() + ".png";
                selfieFile = new File(activity.getCacheDir(), destinationFileName);
                // upimage = BitmapFactory.decodeFile(profileImageFile);
                Log.d("imagefile", String.valueOf(selfieFile));
                UCrop.of(contentURI, Uri.fromFile(selfieFile)).withAspectRatio(1, 1).withOptions(Utils.getOptions(activity)).start((AppCompatActivity) activity);
            }
        }
    }


    @Override
    public void onKycSuccess(BaseResponse body) {
        if(body!=null) {
            new CustomToastNotification(activity,body.getMessage());
            Intent intent1 = new Intent(this, PendingKycActivity.class)
                    .putExtra("email",body.getData().getAccountkycstatus().getSupportmail())
                    .putExtra("phone",body.getData().getAccountkycstatus().getSupportphone());
            startActivity(intent1);
            finish();
        }
    }
    @Override
    public void onStateListSuccess(BaseResponse body) {
        if(body!=null){
            if(statelist.size()>0)
                statelist.clear();

            statelist.addAll(body.getData().getState());
        }
    }

    @Override
    public void onDocumentListSuccess(BaseResponse body) {
        if(body!=null){
            if(documentlist.size()>0)
                documentlist.clear();

            documentlist.addAll(body.getData().getAddressProofList());
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