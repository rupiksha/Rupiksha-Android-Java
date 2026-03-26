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
import com.app.rupiksha.adapters.FundBankAdapter;
import com.app.rupiksha.apipresenter.AddMoneyPresenter;
import com.app.rupiksha.databinding.ActivityAddMoneyBinding;
import com.app.rupiksha.extra.CustomToastNotification;
import com.app.rupiksha.extra.NetworkAlertUtility;
import com.app.rupiksha.interfaces.IAddMoneyView;
import com.app.rupiksha.models.AddfundBankModel;
import com.app.rupiksha.models.BaseResponse;
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

public class AddMoneyActivity extends AppCompatActivity implements View.OnClickListener , IAddMoneyView {

    ActivityAddMoneyBinding binding;
    Activity activity;
    String date="",accountnumber,ifsccode,amount,utrnumber;
    File docImageFile;
    private List<AddfundBankModel> banklist=new ArrayList<>();
    FundBankAdapter bankAdapter;
    AddMoneyPresenter presenter;
    int bankid=0;
    MultipartBody.Part uploadedDoc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_add_money);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_add_money);
        activity=AddMoneyActivity.this;
        presenter=new AddMoneyPresenter();
        presenter.setView(this);
        binding.includeLayout.toolBar.setTitle(getResources().getString(R.string.lbl_add_money));
        binding.includeLayout.toolBar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });
        binding.btnsubmit.setOnClickListener(this);
        binding.etdate.setOnClickListener(this);
        binding.uploaddoc.setOnClickListener(this);

        getBankList();
        setUpBanklist();
    }

    private void getBankList()
    {
        if (NetworkAlertUtility.isInternetConnection2(activity))
        {
            String accessToken = new StorageUtil(activity).getAccessToken();
            String apikey = new StorageUtil(activity).getApiKey();
            //=======================================
            HashMap<String, String> headers = new HashMap<>();
            headers.put("headerToken", accessToken);
            headers.put("headerKey", apikey);
            presenter.getBankList(activity,headers,true);

        } else
        {
            NetworkAlertUtility.showNetworkFailureAlert(activity);
        }
    }

    private void setUpBanklist()
    {
        binding.etbank.setThreshold(100);
        bankAdapter = new FundBankAdapter(this, R.layout.row_price_selector_item_dropdown, R.id.tvItemName, banklist, Gravity.CENTER);
        binding.etbank.setAdapter(bankAdapter);
        binding.etbank.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String name = banklist.get(position).getName();
                String accountnumber = banklist.get(position).getAccount();
                String ifsc = banklist.get(position).getIfsc();
                bankid=banklist.get(position).getId();

                binding.etbank.setText(name+" "+accountnumber+" "+ifsc);
                binding.etbank.clearFocus();

            }
        });

    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();
//        switch (id) {
//            case R.id.btnsubmit:
//                 checkvaildation();
//                break;
//            case R.id.etdate:
//                showDatePickerDOBDialog();
//                break;
//            case R.id.uploaddoc:
//                requestCameraPermission();
//                break;
//        }

        if (id == R.id.btnsubmit)
        {
            checkvaildation();
        } else if (id == R.id.etdate)
        {
            showDatePickerDOBDialog();
        } else if (id == R.id.uploaddoc)
        {
            requestCameraPermission();
        }
    }

    private void checkvaildation()
    {
        accountnumber= Objects.requireNonNull(binding.etaccountnumber.getText()).toString();
        ifsccode = Objects.requireNonNull(binding.etifsc.getText()).toString();
        utrnumber = Objects.requireNonNull(binding.etutr.getText()).toString();
        amount=Objects.requireNonNull(binding.etamount.getText()).toString();

        if(bankid==0){
            binding.etbank.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_bank));
        }/*else if(TextUtils.isEmpty(accountnumber)){
            binding.etaccountnumber.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_account_number));
        }else if(TextUtils.isEmpty(ifsccode)){
            binding.etifsc.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_ifsc_code));
        }*/else if(TextUtils.isEmpty(amount)){
            binding.etamount.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_amount));
        }else if(TextUtils.isEmpty(date)){
            binding.etdate.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_date));
        }else if(docImageFile==null){
            new CustomToastNotification(activity,getResources().getString(R.string.please_upload_doc_proof));
        }else{
            addFund();
        }



    }

    private void addFund()
    {
        if (NetworkAlertUtility.isInternetConnection2(activity))
        {
            String accessToken = new StorageUtil(activity).getAccessToken();
            String apikey = new StorageUtil(activity).getApiKey();
            HashMap<String, RequestBody> map = new HashMap<>();
            map.put("amount", RequestBody.create(amount, MediaType.parse("multipart/form-data")));
            map.put("bank", RequestBody.create(String.valueOf(bankid), MediaType.parse("multipart/form-data")));
            map.put("rrn", RequestBody.create(utrnumber, MediaType.parse("multipart/form-data")));
            map.put("txn_date", RequestBody.create(date, MediaType.parse("multipart/form-data")));


            if(!(docImageFile == null))
            {
                uploadedDoc = MultipartBody.Part.createFormData("proof",
                        Utils.getFileName(this, Uri.fromFile(docImageFile)),
                        RequestBody.create(docImageFile,MediaType.parse("image/*")));
            }else{}



            HashMap<String, String> headers = new HashMap<>();
            headers.put("headerToken", accessToken);
            headers.put("headerKey", apikey);
            presenter.getAddMoney(activity, headers, map,true,uploadedDoc);
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

    private void showDatePickerDOBDialog()
    {
        Locale.setDefault(Locale.getDefault());
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        c.add(Calendar.YEAR,0);
        DatePickerDialog datePickerDialog = new DatePickerDialog(activity, R.style.MyDialogTheme, (view, year, monthOfYear, dayOfMonth) -> {
            String d = dayOfMonth < 10 ? "0" + dayOfMonth : "" + dayOfMonth;
            String m = (monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : "" + (monthOfYear + 1);
            date = String.valueOf(year).concat("-").concat(m).concat("-").concat(d);
            binding.etdate.setText(parseDate(d + "-" + m + "-" + year));
        }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
        datePickerDialog.show();
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

                binding.screenshot.setImageURI(resultUri);

        }

        if (requestCode == 280 && resultCode == Activity.RESULT_OK)
        {
            if (Utils.getPickImageResultUri(activity,data) != null) {

                Uri picUri = Utils.getPickImageResultUri(activity, data);
                String destinationFileName = "profile" + System.currentTimeMillis() + ".png";
                docImageFile = new File(activity.getCacheDir(), destinationFileName);
                Log.d("imagefile", String.valueOf(docImageFile));
                UCrop.of(picUri, Uri.fromFile(docImageFile)).withAspectRatio(1, 1).withOptions(Utils.getOptions(activity)).start((AppCompatActivity) activity);

            } else {

                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                Uri contentURI = Utils.getImageUri(activity, bitmap);
                String destinationFileName = "profile" + System.currentTimeMillis() + ".png";
                docImageFile = new File(activity.getCacheDir(), destinationFileName);
                // upimage = BitmapFactory.decodeFile(profileImageFile);
                Log.d("imagefile", String.valueOf(docImageFile));
                UCrop.of(contentURI, Uri.fromFile(docImageFile)).withAspectRatio(1, 1).withOptions(Utils.getOptions(activity)).start((AppCompatActivity) activity);

            }
        }


    }

    @Override
    public void onBankListSuccess(BaseResponse body) {
        if(body!=null){
            if(banklist.size()>0)
                banklist.clear();

            banklist.addAll(body.getData().getAddfundBanks());

        }
    }

    @Override
    public void onAddMoneySuccess(BaseResponse body)
    {
        if(body!=null){
             new CustomToastNotification(activity,""+body.getMessage());
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