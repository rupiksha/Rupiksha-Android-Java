package com.app.rupiksha.activities;

import static com.app.rupiksha.constant.AppConstants.REQUEST_LOCATION_PERMISSION;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;

import com.app.rupiksha.R;
import com.app.rupiksha.adapters.SearchBankAdapter;
import com.app.rupiksha.adapters.StateAdapter;
import com.app.rupiksha.apipresenter.KYCBankPresenter;
import com.app.rupiksha.constant.AppConstants;
import com.app.rupiksha.databinding.ActivityAepsKycBinding;
import com.app.rupiksha.databinding.OperatorsearchbottomsheetBinding;
import com.app.rupiksha.extra.CustomToastNotification;
import com.app.rupiksha.extra.NetworkAlertUtility;
import com.app.rupiksha.interfaces.CallBack;
import com.app.rupiksha.interfaces.IBankKycView;
import com.app.rupiksha.models.BankModel;
import com.app.rupiksha.models.BaseResponse;
import com.app.rupiksha.models.KycModel;
import com.app.rupiksha.models.ModelUserInfo;
import com.app.rupiksha.models.StateModel;
import com.app.rupiksha.storage.StorageUtil;
import com.app.rupiksha.utils.FileUtil;
import com.app.rupiksha.utils.GPSTracker;
import com.app.rupiksha.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class AepsKycActivity extends BaseActivity implements View.OnClickListener , IBankKycView, GPSTracker.GPSTrackerInterface {

    ActivityAepsKycBinding binding;
    Activity activity;
    KYCBankPresenter presenter;
    StateAdapter stateAdapter;
    int stateid;
    private List<StateModel> statelist=new ArrayList<>();
    private Uri imgShopName= null;
    File shopImageFile;

    private List<BankModel> banklist=new ArrayList<>();
    private List<BankModel> storecategories = new ArrayList<>();
    private List<BankModel> search = new ArrayList<>();
    private GPSTracker gps;
    private String stringLatitude = "0.0";
    private String stringLongitude = "0.0";
    private Location mLastLocation = null;

    Dialog operatorBottomSheet;
    LinearLayoutManager linearLayoutManager;
    OperatorsearchbottomsheetBinding myCustomDialog;
    SearchBankAdapter searchBankAdapter;

    String firstname,lastname,phoneNumber,email,shopname,address,pincode,city,state,pannumber,adharnumber,bankHolderName,accountNumber,ifscCode,dob,shopaddress,bankName;
    ModelUserInfo modelUserInfo;
    KycModel kycModel;
    String title="";
    String outlet="";
    String aepsStatus="";
    boolean type=false;
    MultipartBody.Part uploadedshopImage;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_aeps_kyc);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_aeps_kyc);
        activity=AepsKycActivity.this;
        presenter=new KYCBankPresenter();
        presenter.setView(this);
        binding.includeLayout.toolBar.setTitle(getResources().getString(R.string.label_user_kyc));
        binding.includeLayout.toolBar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });

        if (getIntent().hasExtra("title"))
            title = getIntent().getStringExtra("title");


        if (getIntent().hasExtra("aepsStatus"))
            aepsStatus = getIntent().getStringExtra("aepsStatus");

        if (getIntent().hasExtra("type"))
            type = getIntent().getBooleanExtra("type",false);

        if (checkLocationPermission()) {
            if (gps == null)
                gps = new GPSTracker(activity, this);
            mLastLocation = gps.getLocation();
            if (gps.canGetLocation() && mLastLocation != null) {
                StorageUtil.getSharedPref(this).edit()
                        .putString(AppConstants.KEY_LAST_LAT, "" + mLastLocation.getLatitude())
                        .putString(AppConstants.KEY_LAST_LNG, "" + mLastLocation.getLongitude())
                        .apply();
                // getUserStatus();
            } else {
                gps.showSettingsAlert();
            }
        }
        modelUserInfo=new StorageUtil(activity).getUserInfo();

        if(modelUserInfo!= null){
            if(modelUserInfo.getOutlet()!= null && !modelUserInfo.getOutlet().isEmpty()){
                outlet = modelUserInfo.getOutlet();
            }
        }

        kycModel=new StorageUtil(activity).getKYCInfo();
        getBankList();
        getStateList();
        setUpStatelist();

        if(!outlet.isEmpty() || aepsStatus.equalsIgnoreCase("YES")){
            Log.e("aepsStatus",""+aepsStatus);
            binding.tiloutlet.setVisibility(View.VISIBLE);
        }
        else{
            binding.tiloutlet.setVisibility(View.GONE);
        }

        binding.btnsubmit.setOnClickListener(this);
        binding.uploadshop.setOnClickListener(this);
        binding.etbank.setOnClickListener(this);

    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
            return false;
        } else {
            return true;
        }
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
            presenter.getBankList(activity,true);

        } else
        {
            NetworkAlertUtility.showNetworkFailureAlert(activity);
        }
    }
    private void setuserData()
    {
        if(modelUserInfo!=null) {
            binding.etname.setText(modelUserInfo.getName());
            binding.etmobilenumber.setText(modelUserInfo.getMobile());
            binding.etemail.setText(modelUserInfo.getEmail());
            binding.etoutlet.setText(modelUserInfo.getOutlet());
        }

        if(kycModel!=null){
            binding.etadhar.setText(kycModel.getAdhaar());
            binding.etpan.setText(kycModel.getPan());
            binding.etaddress.setText(kycModel.getAddress());
            binding.etcity.setText(kycModel.getDistrict());
            binding.etpincode.setText(kycModel.getPincode());
            binding.etshop.setText(kycModel.getShopname());

            if(kycModel.getState().equalsIgnoreCase("")||kycModel.getState()==null){

            }else{
                stateid= Integer.parseInt(kycModel.getState());

                for(int i=0;i<statelist.size();i++){
                    if(stateid==statelist.get(i).getId()){
                        binding.etstate.setText(""+statelist.get(i).getName());
                    }
                }
            }

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
    private void setUpStatelist()
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


    @Override
    public void onClick(View v) {
        int id = v.getId();
//        switch (id) {
//            case R.id.btnsubmit:
//                 checkValidation();
//                break;
//            case R.id.uploadshop:
//                selectImage(activity,"Change",new CallBack() {
//                    @Override
//                    public void setPhoto(Uri s) {
//                        imgShopName = s;
//                        Utils.setImageDocument(activity,binding.shopImage,s);
//                    }
//                });
//                break;
//               /* requestCameraPermission();*/
//
//            case R.id.etbank:
//                setUpBanklist();
//                break;
//        }
        if (id == R.id.btnsubmit){
            checkValidation();

        }else if(id == R.id.uploadshop){
            selectImage(activity,"Change",new CallBack() {
                    @Override
                    public void setPhoto(Uri s) {
                        imgShopName = s;
                        Utils.setImageDocument(activity,binding.shopImage,s);
                    }
                });
        }/*else if (id == R.id.etbank){
            setUpBanklist();
        }*/
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
               // bankName=banklist.get(position).getName();


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


    private void checkValidation()
    {
        firstname = Objects.requireNonNull(binding.etname.getText()).toString();
        lastname = Objects.requireNonNull(binding.etlastname.getText()).toString();
        bankHolderName= Objects.requireNonNull(binding.etholdername.getText()).toString();
        accountNumber= Objects.requireNonNull(binding.etBankAccountName.getText()).toString();
        ifscCode= Objects.requireNonNull(binding.etIfscCode.getText()).toString();
        phoneNumber = Objects.requireNonNull(binding.etmobilenumber.getText()).toString();
        email = Objects.requireNonNull(binding.etemail.getText()).toString();
        shopname = Objects.requireNonNull(binding.etshop.getText()).toString();
        address = Objects.requireNonNull(binding.etaddress.getText()).toString();
        pincode = Objects.requireNonNull(binding.etpincode.getText()).toString();
        city= Objects.requireNonNull(binding.etcity.getText()).toString();
        state = Objects.requireNonNull(binding.etstate.getText()).toString();
        pannumber = Objects.requireNonNull(binding.etpan.getText()).toString();
        adharnumber = Objects.requireNonNull(binding.etadhar.getText()).toString();
        bankName=Objects.requireNonNull(binding.etbank.getText()).toString();

        if (TextUtils.isEmpty(firstname)) {
            binding.etname.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_firstname));
        } else if (TextUtils.isEmpty(lastname)) {
            binding.etlastname.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_lastname));
        }/*else if (!TextUtils.isEmpty(dob)) {
            binding.etdob.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_dob));
        }*/else if(TextUtils.isEmpty(phoneNumber)){
            binding.etmobilenumber.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_mobile_number));
        }else if(TextUtils.isEmpty(email)){
            binding.etemail.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_email));
        }else if (!Utils.validateEmail(email)) {
            binding.etemail.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_valid_email));
        }/*else if (TextUtils.isEmpty(shopaddress)){
            binding.etshopaddress.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_shopaddress));
        }*/else if (TextUtils.isEmpty(address)){
            binding.etaddress.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_address));
        }else if (TextUtils.isEmpty(shopname)){
            binding.etshop.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_shop_name));
        }/*else if(!(shopImageFile == null)){
            new CustomToastNotification(activity,getResources().getString(R.string.please_select_shop_image));
        }*/else if (TextUtils.isEmpty(pincode)){
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
        }else if (TextUtils.isEmpty(adharnumber)){
            binding.etadhar.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_aadhar));
        }else if (TextUtils.isEmpty(bankHolderName)) {
            binding.etholdername.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_holder_bank_name));
        } else if (bankName.equalsIgnoreCase("")) {
            binding.etbank.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter1_bank));
        } else if (TextUtils.isEmpty(accountNumber))
        {
            binding.etBankAccountName.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_account_number));
        } else{

            callUserKyc();

        }
    }

    private void callUserKyc()
    {
        stringLatitude = StorageUtil.getString(this, AppConstants.KEY_LAST_LAT);
        stringLongitude = StorageUtil.getString(this, AppConstants.KEY_LAST_LNG);

        if (NetworkAlertUtility.isInternetConnection2(activity))
        {

            String accessToken = new StorageUtil(activity).getAccessToken();
            String apikey = new StorageUtil(activity).getApiKey();

            HashMap<String, RequestBody> map = new HashMap<>();
            map.put("first_name", RequestBody.create(firstname, MediaType.parse("multipart/form-data")));
            map.put("last_name", RequestBody.create(lastname, MediaType.parse("multipart/form-data")));
            map.put("companyBankAccountNumber", RequestBody.create(accountNumber, MediaType.parse("multipart/form-data")));
             map.put("companyBankName", RequestBody.create(bankName, MediaType.parse("multipart/form-data")));
            map.put("bankAccountName", RequestBody.create(bankHolderName, MediaType.parse("multipart/form-data")));
            map.put("bankIfscCode", RequestBody.create(ifscCode, MediaType.parse("multipart/form-data")));
            map.put("aadhar", RequestBody.create(adharnumber, MediaType.parse("multipart/form-data")));
            map.put("email", RequestBody.create(email, MediaType.parse("multipart/form-data")));
            map.put("phone", RequestBody.create(phoneNumber, MediaType.parse("multipart/form-data")));
            map.put("pan", RequestBody.create(pannumber, MediaType.parse("multipart/form-data")));
            map.put("address", RequestBody.create(address, MediaType.parse("multipart/form-data")));
            map.put("shopname", RequestBody.create(shopname , MediaType.parse("multipart/form-data")));
            map.put("pincode", RequestBody.create(pincode, MediaType.parse("multipart/form-data")));
            map.put("city", RequestBody.create(city, MediaType.parse("multipart/form-data")));
            map.put("state", RequestBody.create(String.valueOf(stateid), MediaType.parse("multipart/form-data")));
            map.put("lat", RequestBody.create(stringLatitude, MediaType.parse("multipart/form-data")));
            map.put("log", RequestBody.create(stringLongitude, MediaType.parse("multipart/form-data")));
            map.put("outlet", RequestBody.create(outlet, MediaType.parse("multipart/form-data")));


         /*
            try {
                shopImageFile = FileUtil.from(activity, imgShopName);
                if(!(shopImageFile == null))
                {
                    uploadedshopImage  = MultipartBody.Part.createFormData("shop_photo",
                            Utils.getFileName(activity, Uri.fromFile(shopImageFile)),
                            RequestBody.create(MediaType.parse("image/*"), shopImageFile));
                }else{}
            }catch (Exception e) {
                e.printStackTrace();
            }
*/
            HashMap<String, String> headers = new HashMap<>();
            headers.put("headerToken", accessToken);
            headers.put("headerKey", apikey);
         /*   if(!outlet.isEmpty() || aepsStatus.equalsIgnoreCase("YES"))
                presenter.getUpdateAepsKyc(activity, headers, map,uploadedshopImage,true);
            else*/
                presenter.getAepsKyc(activity, headers, map,uploadedshopImage,true);
        }
        else {
            NetworkAlertUtility.showNetworkFailureAlert(activity);
        }

        }





    @Override
    public void onBankKycSuccess(BaseResponse body)
    {

        if(body!=null){
            new CustomToastNotification(activity,body.getMessage());
            if(body.getKey()==1){
                startActivity(new Intent(activity, AepsKycVerifyActivity.class)
                        .putExtra("title",title)
                        .putExtra("type",type));
                finish();
            }else{
               // new CustomToastNotification(activity,getResources().getString(R.string.msg_something_went_wrong));
                startActivity(new Intent(activity, TwoFactorAuthenticationActivity.class)
                        .putExtra("title",title)
                        .putExtra("type",type)
                        .putExtra("aepsStatus",body.getAepsStatus()));
                finish();
            }
        }
    }

    @Override
    public void onStateListSuccess(BaseResponse body) {
        if(body!=null){
            if(statelist.size()>0)
                statelist.clear();

            statelist.addAll(body.getData().getState());
            setuserData();
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

    @Override
    public void onUserLocationChanged(Location location) {
        mLastLocation = location;
        if (gps.canGetLocation()) {
            StorageUtil.getSharedPref(this).edit()
                    .putString(AppConstants.KEY_LAST_LAT, "" + mLastLocation.getLatitude())
                    .putString(AppConstants.KEY_LAST_LNG, "" + mLastLocation.getLongitude())
                    .apply();

            Log.e("lat", String.valueOf(mLastLocation.getLatitude()));
            Log.e("log", String.valueOf(mLastLocation.getLongitude()));

        } else {
            gps.showSettingsAlert();
        }
    }
}