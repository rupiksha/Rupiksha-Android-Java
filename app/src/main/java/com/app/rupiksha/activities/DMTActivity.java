package com.app.rupiksha.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.app.rupiksha.R;
import com.app.rupiksha.adapters.DMTBankListAdapter;
import com.app.rupiksha.adapters.ViewPrintAdapter;
import com.app.rupiksha.apipresenter.DMTPresenter;
import com.app.rupiksha.constant.AppConstants;
import com.app.rupiksha.databinding.ActivityDmtactivityBinding;
import com.app.rupiksha.databinding.DmttransferbsheetBinding;
import com.app.rupiksha.databinding.SendOtpLayoutBinding;
import com.app.rupiksha.databinding.SendPinLayoutBinding;
import com.app.rupiksha.databinding.SuccessquicktransferbsheetBinding;
import com.app.rupiksha.extra.CustomToastNotification;
import com.app.rupiksha.extra.NetworkAlertUtility;
import com.app.rupiksha.interfaces.IDMTView;
import com.app.rupiksha.models.BaseResponse;
import com.app.rupiksha.models.DMTBankdetailListModel;
import com.app.rupiksha.models.DmtDataModel;
import com.app.rupiksha.models.DmtProfileDataModel;
import com.app.rupiksha.otpview.OnOtpCompletionListener;
import com.app.rupiksha.storage.StorageUtil;
import com.app.rupiksha.utils.Utils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class DMTActivity extends AppCompatActivity implements View.OnClickListener, IDMTView {

    ActivityDmtactivityBinding binding;
    Activity activity;
    DMTPresenter presenter;
    String name="",remainingLimit = "",mobile="",dmtKey = "",title,amount;
    boolean type=false;
    boolean flag=false;
    String baneid,paymentmode,ifsc,mode,mobileNo;
    String transferbankid;
    DmtDataModel qtDataModel;
    DmtProfileDataModel dmtProfileDataModel;
    ActivityResultLauncher<Intent> launch;
    private List<DMTBankdetailListModel> bankdetaillist=new ArrayList<>();
    String OtpValue="" , PinValue="";
    boolean pinflag=false;
    boolean otpflag=false;
    private Menu menu;
    DMTBankListAdapter adapter;

    SendOtpLayoutBinding otpBinding;
    BottomSheetDialog otpBottomSheet;

    SendPinLayoutBinding pinBinding;
    BottomSheetDialog pinBottomSheet;

    DmttransferbsheetBinding myBottomBinding;
    BottomSheetDialog userBottomSheet;

    SuccessquicktransferbsheetBinding qtBinding;
    BottomSheetDialog qtBottomSheet;
    private String stringLatitude = "";
    private String stringLongitude = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_dmtactivity);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_dmtactivity);
        activity=DMTActivity.this;
        presenter=new DMTPresenter();
        presenter.setView(this);
        binding.includeLayout.toolBar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });
        setSupportActionBar(binding.includeLayout.toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        stringLatitude = StorageUtil.getString(this, AppConstants.KEY_LAST_LAT);
        stringLongitude = StorageUtil.getString(this, AppConstants.KEY_LAST_LNG);

        if (getIntent().hasExtra("type"))
            type = getIntent().getBooleanExtra("type",false);

        if (getIntent().hasExtra("title"))
            title = getIntent().getStringExtra("title");

        if (getIntent().hasExtra("phoneNumber"))
            mobile = getIntent().getStringExtra("phoneNumber");
//        if (getIntent().hasExtra("dmtKey"))
//            dmtKey = getIntent().getStringExtra("dmtKey");

        binding.tvtitle.setText(getResources().getString(R.string.title_dmt_transfer));
        binding.includeLayout.toolBar.setTitle(title);
        dmtProfileDataModel=new StorageUtil(activity).getDMTInfo();
        dmtKey= new StorageUtil(activity).getDmtKey();

        if(dmtProfileDataModel!=null)
        {
            binding.name.setText("" + dmtProfileDataModel.getName());
            Log.d("dmtProfileDataModel",dmtProfileDataModel.getMobile());
            binding.mobile.setText("" + dmtProfileDataModel.getMobile());
            Log.d("dmtProfileDataModel",String.valueOf(dmtProfileDataModel.getRemainingLimit()));
            binding.remlimit.setText(activity.getResources().getString(R.string.rupees) + "  " + dmtProfileDataModel.getRemainingLimit());
        }

        callaccountListApi();

        launch = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK)
                        {
                            flag=result.getData().getBooleanExtra("flag",false);
                            if(flag)
                            {
                                callaccountListApi();

                            }else{


                            }
                        }
                    }
                });


        binding.fabCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=   new Intent(activity, AddAccountActivity.class)
                        .putExtra("flag",true);
                launch.launch(intent);
            }
        });
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
       if (item.getItemId() == R.id.nav_like)
        {
               logoutApi();
        }
        return super.onOptionsItemSelected(item);
    }

    private void logoutApi()
    {
        if (NetworkAlertUtility.isInternetConnection2(activity))
        {
            String accessToken = new StorageUtil(activity).getAccessToken();
            String apikey = new StorageUtil(activity).getApiKey();
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("dmtKey",dmtKey)
                    .build();

            //=======================================
            HashMap<String, String> headers = new HashMap<>();
            headers.put("headerToken", accessToken);
            headers.put("headerKey", apikey);
            presenter.logoutDmt(activity,headers,requestBody,true);

        } else
        {
            NetworkAlertUtility.showNetworkFailureAlert(activity);
        }
    }

    private void callaccountListApi() {
        if (NetworkAlertUtility.isInternetConnection2(activity))
        {
            String accessToken = new StorageUtil(activity).getAccessToken();
            String apikey = new StorageUtil(activity).getApiKey();
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("dmtKey",dmtKey)
                    .build();

            //=======================================
            HashMap<String, String> headers = new HashMap<>();
            headers.put("headerToken", accessToken);
            headers.put("headerKey", apikey);
            presenter.getdmtAccountList(activity,headers,requestBody,true);

        } else
        {
            NetworkAlertUtility.showNetworkFailureAlert(activity);
        }
    }

    private void deleteAccountApi() {
        if (NetworkAlertUtility.isInternetConnection2(activity))
        {
            String accessToken = new StorageUtil(activity).getAccessToken();
            String apikey = new StorageUtil(activity).getApiKey();
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("dmtKey",dmtKey)
                    .addFormDataPart("beneId",baneid)
                    .build();

            //=======================================
            HashMap<String, String> headers = new HashMap<>();
            headers.put("headerToken", accessToken);
            headers.put("headerKey", apikey);
            presenter.deleteAccountList(activity,headers,requestBody,true);

        } else
        {
            NetworkAlertUtility.showNetworkFailureAlert(activity);
        }
    }

    private void setbankdataList()
    {
        adapter = new DMTBankListAdapter(activity,bankdetaillist, new DMTBankListAdapter.OnItemClick() {
            @Override
            public void onClick(int position) {
                baneid=bankdetaillist.get(position).getBeneId();
                deleteAccountApi();
            }

            @Override
            public void onIMPSClick(String type, int position) {
                paymentmode=type;
                baneid=bankdetaillist.get(position).getBeneId();
                ifsc=bankdetaillist.get(position).getIfsc();
                setbootomsheet();
            }

            @Override
            public void onNEFTClick(String type, int position) {
                paymentmode=type;
                baneid=bankdetaillist.get(position).getBeneId();
                ifsc=bankdetaillist.get(position).getIfsc();
                setbootomsheet();
            }
        });
        binding.fetchbillrecycler.setLayoutManager(new LinearLayoutManager(activity));
        binding.fetchbillrecycler.setItemAnimator(new DefaultItemAnimator());
        binding.fetchbillrecycler.setAdapter(adapter);
        binding.fetchbillrecycler.setNestedScrollingEnabled(false);
    }


    private void doInitiateTransaction()
    {
        if (NetworkAlertUtility.isInternetConnection2(activity))
        {
            String accessToken = new StorageUtil(activity).getAccessToken();
            String apikey = new StorageUtil(activity).getApiKey();
            HashMap<String, RequestBody> map = new HashMap<>();
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("dmtKey", dmtKey)
                    .addFormDataPart("bene_id", String.valueOf(baneid))
                    .addFormDataPart("amount", amount)
                    .addFormDataPart("mode", paymentmode)
                    .addFormDataPart("ifsc",ifsc)
                    .addFormDataPart("lat",stringLatitude)
                    .addFormDataPart("log",stringLongitude)
//                    .addFormDataPart("mobile",mobile)
                    .build();
            //=======================================
            HashMap<String, String> headers = new HashMap<>();
            headers.put("headerToken", accessToken);
            headers.put("headerKey", apikey);
            presenter.initiateTransaction(activity,headers,requestBody,true);

        } else
        {
            NetworkAlertUtility.showNetworkFailureAlert(activity);
        }
    }

    private void setOtpBottomSheet()
    {
        otpBottomSheet = new BottomSheetDialog(activity, R.style.CustomBottomSheetDialogThemeWithDim);
        otpBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.send_otp_layout, null, false);
        otpBottomSheet.setContentView(otpBinding.getRoot());
        otpBottomSheet.setCancelable(true);
        otpBottomSheet.setCanceledOnTouchOutside(false);
        otpBottomSheet.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);
        otpflag=true;
        otpBinding.otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {
                Utils.showProgressDialog(activity,"");
                OtpValue = otp;
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(binding.getRoot().getWindowToken(), 0);
                Utils.hideSofthKeyboaard(activity, binding.getRoot());
                otpVerify(OtpValue);
            }
        });
        otpBinding.btnVerifyOTP.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                otpBottomSheet.dismiss();
                if (OtpValue.isEmpty())
                {
                    new CustomToastNotification(activity,getString(R.string.please_enter_otp));
                } else if (!OtpValue.isEmpty()) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(binding.getRoot().getWindowToken(), 0);
                    Utils.hideSofthKeyboaard(activity, binding.getRoot());
                    setResult(RESULT_OK);
                    otpVerify(OtpValue);

                }
            }
        });
        otpBottomSheet.show();
    }

    private void otpVerify(String value)
    {
        if (NetworkAlertUtility.isInternetConnection2(activity))
        {
            String accessToken = new StorageUtil(activity).getAccessToken();
            String apikey = new StorageUtil(activity).getApiKey();
            HashMap<String, RequestBody> map = new HashMap<>();
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("otp", value)
                    .addFormDataPart("dmtKey", dmtKey)
                    .build();
            //=======================================
            HashMap<String, String> headers = new HashMap<>();
            headers.put("headerToken", accessToken);
            headers.put("headerKey", apikey);
            presenter.doDMTTransaction(activity,headers,requestBody,true);

        } else
        {
            NetworkAlertUtility.showNetworkFailureAlert(activity);
        }
    }

    private void setPinBottomSheet()
    {
        pinBottomSheet = new BottomSheetDialog(activity, R.style.CustomBottomSheetDialogThemeWithDim);
        pinBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.send_pin_layout, null, false);
        pinBottomSheet.setContentView(pinBinding.getRoot());
        pinBottomSheet.setCancelable(true);
        pinBottomSheet.setCanceledOnTouchOutside(false);
        pinBottomSheet.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);
        pinflag=true;
        pinBinding.otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {
                Utils.showProgressDialog(activity,"");
                PinValue = otp;
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(binding.getRoot().getWindowToken(), 0);
                Utils.hideSofthKeyboaard(activity, binding.getRoot());
                otpVerify(PinValue);
            }
        });

        pinBinding.btnVerifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pinBottomSheet.dismiss();
                if (PinValue.isEmpty())
                {
                    new CustomToastNotification(activity,getString(R.string.please_enter_pin));
                } else if (!PinValue.isEmpty()) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(binding.getRoot().getWindowToken(), 0);
                    Utils.hideSofthKeyboaard(activity, binding.getRoot());
                    setResult(RESULT_OK);
                    otpVerify(PinValue);

                }
            }
        });
        pinBottomSheet.show();
    }

    private void setbootomsheet()
    {
        userBottomSheet = new BottomSheetDialog(activity, R.style.CustomBottomSheetDialogThemeWithDim);
        myBottomBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.dmttransferbsheet, null, false);
        userBottomSheet.setContentView(myBottomBinding.getRoot());
        userBottomSheet.setCancelable(true);
        userBottomSheet.setCanceledOnTouchOutside(true);
        userBottomSheet.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);


        myBottomBinding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                amount = Objects.requireNonNull(myBottomBinding.etamount.getText()).toString();
                if(TextUtils.isEmpty(amount)){
                    myBottomBinding.etamount.requestFocus();
                    new CustomToastNotification(activity,getResources().getString(R.string.please_enter_amount));
                }else{
                    doInitiateTransaction();
                }
            }
        });

        userBottomSheet.show();
    }



    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {

        }
    }


    @Override
    public void onDmtAccountListSuccess(BaseResponse body) {
        if(body!=null) {
            if (bankdetaillist.size() > 0)
                bankdetaillist.clear();
            bankdetaillist.addAll(body.getData().getBankdetaillist());

            if(bankdetaillist.size()==0){
                binding.transactiontitle.setVisibility(View.GONE);
                binding.lltransactionLayout.setVisibility(View.GONE);
            }else{
                binding.transactiontitle.setVisibility(View.VISIBLE);
                binding.lltransactionLayout.setVisibility(View.VISIBLE);
                setbankdataList();
            }
          }
        }

    @Override
    public void onDeleteAccountSuccess(BaseResponse body) {
        if(body!=null){
            new CustomToastNotification(activity,body.getMessage());
            adapter.notifyDataSetChanged();

            if(bankdetaillist.size()==0){
                binding.transactiontitle.setVisibility(View.GONE);
                binding.lltransactionLayout.setVisibility(View.GONE);
            }


        }
    }

    @Override
    public void onInitiateTransactionSuccess(BaseResponse body) {
        if(body!=null){
            new CustomToastNotification(activity,body.getMessage());
            userBottomSheet.dismiss();
            setOtpBottomSheet();

        }
    }

    @Override
    public void onDmtTransactionSuccess(BaseResponse body) {
        if(body!=null){
            new CustomToastNotification(activity,body.getMessage());
            qtDataModel=body.getData().getDmtReceiptData();

            if(pinflag){
                pinBottomSheet.dismiss();}
            if(otpflag){
                otpBottomSheet.dismiss();}

            if(qtDataModel!=null){
                setQtRecieptBottomSheet();
            }
        }
    }

    @Override
    public void onDmtLogoutSuccess(BaseResponse body) {
        if(body!=null)
        {
            new StorageUtil(activity).setDMTkey("");
            startActivity(new Intent(activity,HomeActivity.class));
            finishAffinity();
        }
    }

    private void setQtRecieptBottomSheet()
    {
        qtBottomSheet = new BottomSheetDialog(activity, R.style.CustomBottomSheetDialogThemeWithDim);
        qtBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.successquicktransferbsheet, null, false);
        qtBottomSheet.setContentView(qtBinding.getRoot());
        qtBottomSheet.setCancelable(true);
        qtBottomSheet.setCanceledOnTouchOutside(false);
        qtBottomSheet.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);

        qtBinding.date.setText(qtDataModel.getDate());
        qtBinding.txId.setText(qtDataModel.getTxnid());
        qtBinding.status.setText(qtDataModel.getStatus());
        if(qtDataModel.getStatus().equalsIgnoreCase("FAILED")){
            qtBinding.status.setTextColor(activity.getResources().getColor(R.color.color_red));
        }else{
            qtBinding.status.setTextColor(activity.getResources().getColor(R.color.color_green));

        }

        qtBinding.lblQt.setText(getResources().getString(R.string.lbl_dmt));
        qtBinding.lblCharge.setText(getResources().getString(R.string.lbl_dmt_charge));


        String wallet= String.valueOf(qtDataModel.getAmount1());
        if(wallet==null || wallet.equals("")){
            qtBinding.qtamount.setText(getResources().getString(R.string.rupees) + " " +wallet);
        }else{
            double famount= qtDataModel.getAmount1();
            qtBinding.qtamount.setText(getResources().getString(R.string.rupees) + " " +(new DecimalFormat("##.##").format(famount)));
        }

        String charge= String.valueOf(qtDataModel.getAmount2());
        if(charge==null || charge.equals("")){
            qtBinding.qtcharge.setText(getResources().getString(R.string.rupees) + " " +charge);
        }else{
            double famount= qtDataModel.getAmount2();
            qtBinding.qtcharge.setText(getResources().getString(R.string.rupees) + " " +(new DecimalFormat("##.##").format(famount)));
        }

        String total= String.valueOf(qtDataModel.getTotal());
        if(total==null || total.equals("")){
            qtBinding.total.setText(getResources().getString(R.string.rupees) + " " +total);
        }else{
            double famount= qtDataModel.getTotal();
            qtBinding.total.setText(getResources().getString(R.string.rupees) + " " +(new DecimalFormat("##.##").format(famount)));
        }

        qtBinding.txName.setText(qtDataModel.getName());
        qtBinding.accountnumber.setText(qtDataModel.getAccount());
        qtBinding.ifsc.setText(qtDataModel.getIfsc());
        qtBinding.rrn.setText(qtDataModel.getRrn());

        qtBinding.btnConfirm.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                qtBottomSheet.dismiss();
            }
        });
        /*qtBinding.btnscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.sharepdf(activity,qtBinding.recieptlayout);
            }
        });*/

        qtBinding.btnprint.setOnClickListener(v ->
                printReceipt(qtBinding.recieptlayout)
        );
        qtBottomSheet.show();
    }

    private void printReceipt(View view) {

        PrintManager printManager =
                (PrintManager) getSystemService(Context.PRINT_SERVICE);

        PrintDocumentAdapter printAdapter =
                new ViewPrintAdapter(this, view);

        printManager.print("Payout Receipt", printAdapter, null);
    }
    @Override
    public Context getContext() {
        return null;
    }

    @Override
    public void enableLoadingBar(boolean enable, String s) {

    }

    @Override
    public void onError(String reason) {

    }

    @Override
    public void dialogAccountDeactivate(String reason) {

    }

    @Override
    public void onErrorToast(String reason) {

    }
}