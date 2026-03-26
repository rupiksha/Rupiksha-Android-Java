package com.app.rupiksha.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.app.rupiksha.R;
import com.app.rupiksha.adapters.BankAdapter;
import com.app.rupiksha.adapters.SearchBankAdapter;
import com.app.rupiksha.apipresenter.AddBanificiaryPresenter;
import com.app.rupiksha.databinding.ActivityAddBenificiaryBinding;
import com.app.rupiksha.databinding.OperatorsearchbottomsheetBinding;
import com.app.rupiksha.extra.CustomToastNotification;
import com.app.rupiksha.extra.NetworkAlertUtility;
import com.app.rupiksha.interfaces.IAddBenificiaryView;
import com.app.rupiksha.models.BankModel;
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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class AddBenificiaryActivity extends AppCompatActivity implements View.OnClickListener, IAddBenificiaryView {

    ActivityAddBenificiaryBinding binding;
    Activity activity;
    Dialog operatorBottomSheet;
    LinearLayoutManager linearLayoutManager;
    OperatorsearchbottomsheetBinding myCustomDialog;
    SearchBankAdapter searchBankAdapter;
    private List<BankModel> banklist=new ArrayList<>();
    private List<BankModel> storecategories = new ArrayList<>();
    private List<BankModel> search = new ArrayList<>();
    BankAdapter bankAdapter;
    ActivityResultLauncher<Intent> launch;
    AddBanificiaryPresenter presenter;
    int bankid=0;
    boolean flag=false;
    String accountnumber,ifsccode,name;
    File panImageFile;
    MultipartBody.Part uploadedPan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_add_benificiary);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_add_benificiary);
        activity=AddBenificiaryActivity.this;
        presenter=new AddBanificiaryPresenter();
        presenter.setView(this);
        if (getIntent().hasExtra("flag"))
            flag = getIntent().getBooleanExtra("flag",false);

        binding.includeLayout.toolBar.setTitle(getResources().getString(R.string.label_add_benificiary));
        binding.includeLayout.toolBar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });
        binding.btnsubmit.setOnClickListener(this);
        binding.uploadpan.setOnClickListener(this);
        binding.etbank.setOnClickListener(this);
        getBankList();
//        setUpBanklist();

    }

    private void getBankList()
    {
        if (NetworkAlertUtility.isInternetConnection2(activity))
        {
            HashMap<String, RequestBody> map = new HashMap<>();
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("demo", "")
                    .build();
            //=======================================
            presenter.getBankList(activity, true);

        } else
        {
            NetworkAlertUtility.showNetworkFailureAlert(activity);
        }
    }

    private void getAccountverify()
    {
        if (NetworkAlertUtility.isInternetConnection2(activity))
        {
            String accessToken = new StorageUtil(activity).getAccessToken();
            String apikey = new StorageUtil(activity).getApiKey();
            //======================================
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("account", accountnumber)
                    .addFormDataPart("ifsc",ifsccode)
                    .build();
            //=======================================
            HashMap<String, String> headers = new HashMap<>();
            headers.put("headerToken", accessToken);
            headers.put("headerKey", apikey);
            Log.e("1234",""+requestBody);
            presenter.verifyAccount(activity,headers, requestBody,true);

        } else
        {
            NetworkAlertUtility.showNetworkFailureAlert(activity);
        }
    }

    private void setUpBanklist()
    {
        operatorBottomSheet = new Dialog(activity, R.style.MyDialogTheme);
        myCustomDialog = DataBindingUtil.inflate(getLayoutInflater(), R.layout.operatorsearchbottomsheet, null, false);
        operatorBottomSheet.setContentView(myCustomDialog.getRoot());
        operatorBottomSheet.setCancelable(true);
        operatorBottomSheet.setCanceledOnTouchOutside(true);
        operatorBottomSheet.show();

        storecategories = new StorageUtil(activity).getBankList(activity);
        banklist.addAll(storecategories);
        myCustomDialog.etname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int i1, int i2) {
                if (s.length() == 0) {
                    // binding.cancel.setVisibility(View.GONE);
                    if (banklist.size() > 0) {
                        banklist.clear();
                    }
                    banklist.addAll(storecategories);
                    searchBankAdapter.notifyDataSetChanged();
                }
                if (s.length() > 0) {
                    // binding.cancel.setVisibility(View.VISIBLE);
                    if (search.size() > 0)
                        search.clear();
                    for (int i = 0; i < storecategories.size(); i++) {
                        if ((storecategories.get(i).getName().toLowerCase(Locale.ROOT).contains(s.toString().toLowerCase()) == true)) {
                            if (!search.contains(storecategories.get(i)))
                                search.add(storecategories.get(i));
                        }
                    }
                    if (banklist.size() > 0)
                        banklist.clear();

                    banklist.addAll(search);
                    searchBankAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        searchBankAdapter = new SearchBankAdapter(activity, this.banklist, new SearchBankAdapter.OnItemClick() {
            @Override
            public void onClick(int position) {
                String name = banklist.get(position).getName();
                bankid=banklist.get(position).getId();

                binding.etbank.setText(name);
                binding.etbank.clearFocus();
                if(operatorBottomSheet!=null){
                    operatorBottomSheet.dismiss();
                }
            }
        });
        myCustomDialog.opteratorrecycler.setLayoutManager(linearLayoutManager);
        myCustomDialog.opteratorrecycler.setAdapter(searchBankAdapter);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("flag", flag);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
//        switch (id) {
//            case R.id.btnsubmit:
//                 checkvalidation();
//                break;
//            case R.id.uploadpan:
//                requestCameraPermission();
//                break;
//            case R.id.etbank:
//                setUpBanklist();
//                break;
//        }
        if (id == R.id.btnsubmit)
        {
            checkvalidation();
        } else if (id == R.id.uploadpan) {
            requestCameraPermission();
        }else if (id == R.id.etbank) {
            setUpBanklist();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (requestCode == UCrop.REQUEST_CROP) {
            Uri resultUri = UCrop.getOutput(data);
                binding.panimg.setImageURI(resultUri);
        }

        if (requestCode == 280 && resultCode == Activity.RESULT_OK) {
            if (Utils.getPickImageResultUri(activity, data) != null)
            {
                Uri picUri = Utils.getPickImageResultUri(activity, data);
                String destinationFileName = "profile" + System.currentTimeMillis() + ".png";
                panImageFile = new File(activity.getCacheDir(), destinationFileName);
                Log.d("imagefile", String.valueOf(panImageFile));
                UCrop.of(picUri, Uri.fromFile(panImageFile)).withAspectRatio(1, 1).withOptions(Utils.getOptions(activity)).start((AppCompatActivity) activity);

            } else {
                if (data.getExtras().get("data") != null) {
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    Uri contentURI = Utils.getImageUri(activity, bitmap);
                    String destinationFileName = "profile" + System.currentTimeMillis() + ".png";
                    panImageFile = new File(activity.getCacheDir(), destinationFileName);
                    // upimage = BitmapFactory.decodeFile(profileImageFile);
                    Log.d("imagefile", String.valueOf(panImageFile));
                    UCrop.of(contentURI, Uri.fromFile(panImageFile)).withAspectRatio(1, 1).withOptions(Utils.getOptions(activity)).start((AppCompatActivity) activity);
                }
            }
        }
    }

    private void checkvalidation()
    {
        accountnumber = Objects.requireNonNull(binding.etaccountnumber.getText()).toString();
        ifsccode = Objects.requireNonNull(binding.etifsc.getText()).toString();

        if(TextUtils.isEmpty(accountnumber)) {
            binding.etaccountnumber.requestFocus();
            new CustomToastNotification(activity, getResources().getString(R.string.please_enter_account_number));
        }else if(TextUtils.isEmpty(ifsccode)) {
            binding.etifsc.requestFocus();
            new CustomToastNotification(activity, getResources().getString(R.string.please_enter_ifsc_code));
        }
//        else if (TextUtils.isEmpty(name)) {
//            binding.etname.requestFocus();
//            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_name));
//        }else if (bankid==0) {
//            binding.etbank.requestFocus();
//            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_bank));
//        }else if (panImageFile==null){
//            new CustomToastNotification(activity,getResources().getString(R.string.please_upload_doc_image));
//        }
        else{
            //addBenificary();
            getAccountverify();
        }
    }

    private void addBenificary()
    {
        if (NetworkAlertUtility.isInternetConnection2(activity))
        {
            String accessToken = new StorageUtil(activity).getAccessToken();
            String apikey = new StorageUtil(activity).getApiKey();
            HashMap<String, RequestBody> map = new HashMap<>();
           // map.put("bank", RequestBody.create(String.valueOf(bankid), MediaType.parse("multipart/form-data")));
            map.put("account", RequestBody.create(accountnumber, MediaType.parse("multipart/form-data")));
            map.put("ifsc", RequestBody.create(ifsccode, MediaType.parse("multipart/form-data")));
            map.put("name", RequestBody.create(name, MediaType.parse("multipart/form-data")));

            //=======================================
//            if(!(panImageFile == null))
//            {
//                uploadedPan = MultipartBody.Part.createFormData("passbook",
//                        Utils.getFileName(this, Uri.fromFile(panImageFile)),
//                        RequestBody.create(panImageFile, MediaType.parse("image/*")));
//            }else{}

            HashMap<String, String> headers = new HashMap<>();
            headers.put("headerToken", accessToken);
            headers.put("headerKey", apikey);
            presenter.addBenificiary(activity,headers,map,uploadedPan,true);

        } else
        {
            NetworkAlertUtility.showNetworkFailureAlert(activity);
        }
    }

    @Override
    public void onBankListSuccess(BaseResponse body) {
        if(body!=null){
            if(banklist.size()>0)
                banklist.clear();

            banklist.addAll(body.getData().getBank());
            new StorageUtil(activity).saveBankList((ArrayList<BankModel>) banklist);
        }
    }

    @Override
    public void onAddBankSuccess(BaseResponse body)
    {
         if(body!=null){
             new CustomToastNotification(activity,body.getMessage());
             onBackPressed();
         }
    }

    @Override
    public void onAccountVerifySuccess(BaseResponse body) {
        if(body!=null){
            new CustomToastNotification(activity,body.getMessage());
             binding.tilname.setVisibility(View.VISIBLE);
             binding.etname.setText(body.getName());
             name = Objects.requireNonNull(binding.etname.getText()).toString();
             Log.d("name",name);
             binding.btnsubmit.setVisibility(View.GONE);
             binding.btnAddAccount.setVisibility(View.VISIBLE);
             binding.btnAddAccount.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {
                     addBenificary();
                 }
             });
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