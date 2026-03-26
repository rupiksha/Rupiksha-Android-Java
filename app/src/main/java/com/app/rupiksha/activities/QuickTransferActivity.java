package com.app.rupiksha.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;

import com.app.rupiksha.R;
import com.app.rupiksha.adapters.BankIFSCAdapter;
import com.app.rupiksha.adapters.QTAccountAdapter;
import com.app.rupiksha.adapters.QuickFetchBillAdapter;
import com.app.rupiksha.apipresenter.QuickTranferPresenter;
import com.app.rupiksha.constant.AppConstants;
import com.app.rupiksha.databinding.ActivityQuickTransferBinding;
import com.app.rupiksha.databinding.FetchaccountdialogbsheetBinding;
import com.app.rupiksha.databinding.SendOtpLayoutBinding;
import com.app.rupiksha.databinding.SendPinLayoutBinding;

import com.app.rupiksha.databinding.SuccessquicktransferbsheetBinding;
import com.app.rupiksha.extra.CustomToastNotification;
import com.app.rupiksha.extra.NetworkAlertUtility;
import com.app.rupiksha.interfaces.IQuickTransferView;
import com.app.rupiksha.models.BaseResponse;
import com.app.rupiksha.models.GlobalBankModel;
import com.app.rupiksha.models.QtAccountModel;
import com.app.rupiksha.models.QtDataModel;
import com.app.rupiksha.models.QuickFetchBillModel;
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

public class QuickTransferActivity extends AppCompatActivity  implements View.OnClickListener, IQuickTransferView {

    ActivityQuickTransferBinding binding;
    Activity activity;
    String title="";
    String OtpValue="",PinValue="";
    boolean type=false;
    boolean pinflag=false;
    boolean otpflag=false;
    int fetch_acc=0;
    private List<QuickFetchBillModel> quicktransactionlist=new ArrayList<>();
    private List<QtAccountModel> accountlist=new ArrayList<>();
    private List<GlobalBankModel> banklist=new ArrayList<>();
    BankIFSCAdapter bankAdapter;
    ActivityResultLauncher<Intent> launch;
    QuickTranferPresenter presenter;
    QtDataModel qtDataModel;
    int bankid=0;
    private String stringLatitude = "0.0";
    private String stringLongitude = "0.0";
    String mobile,name,ifsccode,accountnumber,amount;
    FetchaccountdialogbsheetBinding myBottomBinding;
    BottomSheetDialog userBottomSheet;

    SendOtpLayoutBinding otpBinding;
    BottomSheetDialog otpBottomSheet;

    SendPinLayoutBinding pinBinding;
    BottomSheetDialog pinBottomSheet;

    SuccessquicktransferbsheetBinding qtBinding;
    BottomSheetDialog qtBottomSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_quick_transfer);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_quick_transfer);
        activity=QuickTransferActivity.this;
        presenter=new QuickTranferPresenter();
        presenter.setView(this);
        binding.includeLayout.toolBar.setTitle(getResources().getString(R.string.title_aeps_service));
        binding.includeLayout.toolBar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });

        if (getIntent().hasExtra("title"))
            title = getIntent().getStringExtra("title");

        if (getIntent().hasExtra("type"))
            type = getIntent().getBooleanExtra("type",false);

        binding.tvtitle.setText(""+title);
        binding.includeLayout.toolBar.setTitle(""+title);
        binding.btnverify.setOnClickListener(this);
        binding.btnpaynow.setOnClickListener(this);
        binding.lltransactionLayout.setVisibility(View.GONE);

        binding.etmobilenumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if(s.length()==10){
                    mobile= String.valueOf(s);
                    getFetchAccount(mobile);
                }else{
                    binding.etbank.setText(null);
                    binding.etaccount.setText("");
                    binding.etifsc.setText("");
                    binding.etname.setText("");
                    binding.etamount.setText("");
                    fetch_acc=0;
                }
            }
        });

        getBankList();
        setUpBanklist();

    }

    private void getFetchAccount(String mobile)
    {
        if (NetworkAlertUtility.isInternetConnection2(activity)) {
            String accessToken = new StorageUtil(activity).getAccessToken();
            String apikey = new StorageUtil(activity).getApiKey();
            //======================================
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("mobile", String.valueOf(mobile))
                    .build();
            //=======================================
            HashMap<String, String> headers = new HashMap<>();
            headers.put("headerToken", accessToken);
            headers.put("headerKey", apikey);
            presenter.fetchaccounts(activity, headers, requestBody,true);

        } else {
            NetworkAlertUtility.showNetworkFailureAlert(activity);
        }
    }

    private void setquicktransactionList()
    {
        QuickFetchBillAdapter adapter = new QuickFetchBillAdapter(activity,quicktransactionlist, new QuickFetchBillAdapter.OnItemClick() {
            @Override
            public void onClick(int position) {
                //  startActivity(new Intent(activity, WebViewActivity.class));
            }
        });
        binding.fetchbillrecycler.setLayoutManager(new LinearLayoutManager(activity));
        binding.fetchbillrecycler.setItemAnimator(new DefaultItemAnimator());
        binding.fetchbillrecycler.setAdapter(adapter);
    }

    private void getBankList()
    {
        if (NetworkAlertUtility.isInternetConnection2(activity))
        {
            String accessToken = new StorageUtil(activity).getAccessToken();
            String apikey = new StorageUtil(activity).getApiKey();
            HashMap<String, RequestBody> map = new HashMap<>();
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("demo", "")
                    .build();
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
        bankAdapter = new BankIFSCAdapter(this, R.layout.row_price_selector_item_dropdown, R.id.tvItemName, banklist, Gravity.CENTER);
        binding.etbank.setAdapter(bankAdapter);
        binding.etbank.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String name = banklist.get(position).getName();
                binding.etbank.setText(name);
                binding.etbank.clearFocus();

            }
        });

    }


    @Override
    public void onClick(View v)
    {
        int id = v.getId();
//        switch (id) {
//            case R.id.btnverify:
//                checkValidation("verify");
//
//                break;
//            case R.id.btnpaynow:
//                checkValidation("paynow");
//                break;
//        }
        if (id == R.id.btnverify) {
            checkValidation("verify");
        } else if (id == R.id.btnpaynow) {
            checkValidation("paynow");
        }
    }

    private void checkValidation(String type)
    {
        accountnumber = Objects.requireNonNull(binding.etaccount.getText()).toString();
        mobile = Objects.requireNonNull(binding.etmobilenumber.getText()).toString();
        amount = Objects.requireNonNull(binding.etamount.getText()).toString();
        ifsccode = Objects.requireNonNull(binding.etifsc.getText()).toString();
        name = Objects.requireNonNull(binding.etname.getText()).toString();
        if(TextUtils.isEmpty(mobile)){
            binding.etmobilenumber.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_mobile_number));
        }else if(TextUtils.isEmpty(accountnumber)) {
            binding.etaccount.requestFocus();
            new CustomToastNotification(activity, getResources().getString(R.string.please_enter_account_number));
        }else if(TextUtils.isEmpty(ifsccode)) {
            binding.etifsc.requestFocus();
            new CustomToastNotification(activity, getResources().getString(R.string.please_enter_ifsc_code));
        }else if (TextUtils.isEmpty(name)) {
            binding.etname.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_name));
        }else if (TextUtils.isEmpty(amount) && type.equalsIgnoreCase("paynow")) {
            binding.etamount.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_amount));
        }else if(fetch_acc==0){
            doVerifyAccount();
        }else{
            doInitiateTransaction();
        }
    }

    private void doVerifyAccount() {
        if (NetworkAlertUtility.isInternetConnection2(activity))
        {
            String accessToken = new StorageUtil(activity).getAccessToken();
            String apikey = new StorageUtil(activity).getApiKey();
            stringLatitude = StorageUtil.getString(this, AppConstants.KEY_LAST_LAT);
            stringLongitude = StorageUtil.getString(this, AppConstants.KEY_LAST_LNG);
            HashMap<String, RequestBody> map = new HashMap<>();
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("account", accountnumber)
                    .addFormDataPart("ifsc", ifsccode)
                    .addFormDataPart("mobile", mobile)
                    .addFormDataPart("lat", stringLatitude)
                    .addFormDataPart("log", stringLongitude)
                    .build();
            //=======================================
            HashMap<String, String> headers = new HashMap<>();
            headers.put("headerToken", accessToken);
            headers.put("headerKey", apikey);
            presenter.qtaccountverify(activity,headers,requestBody,true);

        } else
        {
            NetworkAlertUtility.showNetworkFailureAlert(activity);
        }
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
                    .addFormDataPart("mobile", mobile)
                    .addFormDataPart("amount", amount)
                    .addFormDataPart("account", accountnumber)
                    .addFormDataPart("ifsc", ifsccode)
                    .addFormDataPart("name", name)
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

    private void setOtpBottomSheet(String txnKey)
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
                otpVerify(OtpValue,txnKey);
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
                    otpVerify(OtpValue,txnKey);

                }
            }
        });
        otpBottomSheet.show();
    }

    private void setPinBottomSheet(String txnKey)
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
                otpVerify(PinValue,txnKey);
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
                    otpVerify(PinValue,txnKey);

                }
            }
        });
        pinBottomSheet.show();
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
       /* qtBinding.btnscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.sharepdf(activity,qtBinding.recieptlayout);
            }
        });*/
        qtBottomSheet.show();
    }

    private void otpVerify(String value, String txnKey)
    {
        if (NetworkAlertUtility.isInternetConnection2(activity))
        {
            String accessToken = new StorageUtil(activity).getAccessToken();
            String apikey = new StorageUtil(activity).getApiKey();
            HashMap<String, RequestBody> map = new HashMap<>();
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("otp", value)
                    .addFormDataPart("txn_key", txnKey)
                    .build();
            //=======================================
            HashMap<String, String> headers = new HashMap<>();
            headers.put("headerToken", accessToken);
            headers.put("headerKey", apikey);
            presenter.doqtTransaction(activity,headers,requestBody,true);

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

            banklist.addAll(body.getData().getGlobalList());

        }
    }

    @Override
    public void onFetchAccountSuccess(BaseResponse body)
    {
        if(body!=null){

            if(accountlist.size()>0){
                accountlist.clear();
            }
            accountlist.addAll(body.getData().getQtAccounts());

            if(accountlist.size()==0){
                new CustomToastNotification(activity,""+body.getMessage());
                binding.btnverify.setVisibility(View.VISIBLE);
                binding.btnpaynow.setVisibility(View.GONE);
            }else {
                binding.btnpaynow.setVisibility(View.VISIBLE);
                setbootomsheet();
            }


        }
    }

    @Override
    public void onInitiateTransactionSuccess(BaseResponse body)
    {
        if(body!=null){
            new CustomToastNotification(activity,body.getMessage());
            if(body.getMode().equalsIgnoreCase("otp")){
               setOtpBottomSheet(body.getTxnKey());
            }else{
                setPinBottomSheet(body.getTxnKey());
            }
        }
    }

    @Override
    public void onQTTransactionSuccess(BaseResponse body)
    {
         if(body!=null){
             new CustomToastNotification(activity,body.getMessage());
             qtDataModel=body.getData().getQtData();

             if(pinflag){
             pinBottomSheet.dismiss();}
             if(otpflag){
             otpBottomSheet.dismiss();}

             if(qtDataModel!=null){
                 setQtRecieptBottomSheet();
             }

             if(quicktransactionlist.size()>0){
                 quicktransactionlist.clear();
             }

             quicktransactionlist.addAll(body.getData().getRecentQtTxn());
             setquicktransactionList();
             binding.lltransactionLayout.setVisibility(View.VISIBLE);

         }
    }

    @Override
    public void onAccountVerifySuccess(BaseResponse body)
    {
        if(body!=null){
            new CustomToastNotification(activity,body.getMessage());
            binding.btnverify.setVisibility(View.GONE);
            fetch_acc=1;
            binding.btnpaynow.setVisibility(View.VISIBLE);
            if(body.getName()!= null)
                 binding.etname.setText(body.getName());
        }
    }

    private void setbootomsheet()
    {
        userBottomSheet = new BottomSheetDialog(activity, R.style.CustomBottomSheetDialogThemeWithDim);
        myBottomBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.fetchaccountdialogbsheet, null, false);
        userBottomSheet.setContentView(myBottomBinding.getRoot());
        userBottomSheet.setCancelable(true);
        userBottomSheet.setCanceledOnTouchOutside(false);
        userBottomSheet.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);

        QTAccountAdapter adapter = new QTAccountAdapter(activity,accountlist, new QTAccountAdapter.OnItemClick() {
            @Override
            public void onClick(int position)
            {
                for(int i=0;i<banklist.size();i++){
                    if(accountlist.get(position).getId()==banklist.get(i).getId())
                    {
                        binding.etbank.setText(accountlist.get(position).getBname());
                    }
                }
                binding.etaccount.setText(accountlist.get(position).getAccount());
                binding.etifsc.setText(accountlist.get(position).getIfsc());
                binding.etname.setText(accountlist.get(position).getName());
                fetch_acc=accountlist.get(position).getFetchAcc();
                if (fetch_acc==0){
                    binding.btnverify.setVisibility(View.VISIBLE);
                }else{
                    binding.btnverify.setVisibility(View.GONE);
                }
                userBottomSheet.dismiss();
            }
        });
        myBottomBinding.fetchbillrecycler.setLayoutManager(new LinearLayoutManager(activity));
        myBottomBinding.fetchbillrecycler.setItemAnimator(new DefaultItemAnimator());
        myBottomBinding.fetchbillrecycler.setAdapter(adapter);
        myBottomBinding.fetchbillrecycler.setNestedScrollingEnabled(false);

        myBottomBinding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userBottomSheet.dismiss();
            }
        });
        userBottomSheet.show();
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