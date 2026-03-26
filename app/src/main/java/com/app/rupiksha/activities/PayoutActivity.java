package com.app.rupiksha.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;

import com.app.rupiksha.R;
import com.app.rupiksha.adapters.BankListAdapter;
import com.app.rupiksha.adapters.PayoutBankAdapter;
import com.app.rupiksha.adapters.ViewPrintAdapter;
import com.app.rupiksha.apipresenter.PayoutPresenter;
import com.app.rupiksha.constant.AppConstants;
import com.app.rupiksha.databinding.ActivityPayoutBinding;
import com.app.rupiksha.databinding.SendOtpLayoutBinding;
import com.app.rupiksha.databinding.SendPinLayoutBinding;
import com.app.rupiksha.databinding.SuccessquicktransferbsheetBinding;
import com.app.rupiksha.extra.CustomToastNotification;
import com.app.rupiksha.extra.NetworkAlertUtility;
import com.app.rupiksha.interfaces.IPayoutView;
import com.app.rupiksha.models.BaseResponse;
import com.app.rupiksha.models.PayoutAccountModel;
import com.app.rupiksha.models.PayoutDataModel;
import com.app.rupiksha.otpview.OnOtpCompletionListener;
import com.app.rupiksha.storage.StorageUtil;
import com.app.rupiksha.utils.Utils;
import com.fingpay.microatmsdk.utils.Constants;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class PayoutActivity extends AppCompatActivity implements View.OnClickListener, IPayoutView {

    ActivityPayoutBinding binding;
    Activity activity;
    String title="";
    boolean type=false;
    boolean flag=false;
    private List<PayoutAccountModel> banklist=new ArrayList<>();
    PayoutBankAdapter bankAdapter;
    BankListAdapter adapter;
    ActivityResultLauncher<Intent> launch;
    PayoutPresenter presenter;
    int bankid=0,accountType=1;
    String mode="IMPS",mobile,amount,senderName;
    PayoutDataModel qtDataModel;
    String OtpValue="",PinValue="";
    boolean pinflag=false;
    boolean otpflag=false;
    SendOtpLayoutBinding otpBinding;
    BottomSheetDialog otpBottomSheet;

    SendPinLayoutBinding pinBinding;
    BottomSheetDialog pinBottomSheet;

    SuccessquicktransferbsheetBinding qtBinding;
    BottomSheetDialog qtBottomSheet;
    private String stringLatitude = "";
    private String stringLongitude = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_payout);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_payout);
        activity=PayoutActivity.this;
        presenter=new PayoutPresenter();
        presenter.setView(this);
        binding.includeLayout.toolBar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });

        if (getIntent().hasExtra("title"))
            title = getIntent().getStringExtra("title");

        if (getIntent().hasExtra("type"))
            type = getIntent().getBooleanExtra("type",false);

        stringLatitude = StorageUtil.getString(this, AppConstants.KEY_LAST_LAT);
        stringLongitude = StorageUtil.getString(this, AppConstants.KEY_LAST_LNG);

        binding.btnsendotp.setOnClickListener(this);
        binding.tvtitle.setText(""+title);
        binding.includeLayout.toolBar.setTitle(""+title);
        binding.lltransactionLayout.setVisibility(View.GONE);
        getBankList(true);
        setAccountType();

        launch = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK)
                        {
                            flag=result.getData().getBooleanExtra("flag",false);
                            if(flag)
                            {
                               // binding.lltransactionLayout.setVisibility(View.VISIBLE);
                                getBankList(false);

                            }else{


                            }
                        }
                    }
                });


        binding.fabCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=   new Intent(activity, AddBenificiaryActivity.class)
                        .putExtra("flag",true);
                launch.launch(intent);
            }
        });

        binding.toggle.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {

                if(checkedId==R.id.radioButtonsupplier){
                    mode="IMPS";
                }else if(checkedId==R.id.radioButtoncategory){
                    mode="NEFT";
                }else if(checkedId == R.id.radioButtonRTGS){
                    mode="RTGS";
                }
            }
        });

        binding.etAccountType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedtype = (String) parent.getItemAtPosition(position);
                Log.d("selectedType", selectedtype);

                if (selectedtype.equals("Savings Account")) {
                    accountType = 1;
                } else if (selectedtype.equals("Current Account")) {
                    accountType = 2;
                }
            }
        });
    }

    private void getBankList(boolean b)
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
            presenter.getBankList(activity,headers,b);

        } else
        {
            NetworkAlertUtility.showNetworkFailureAlert(activity);
        }
    }

    private void setUpBanklist()
    {
        binding.etbank.setThreshold(100);
        bankAdapter = new PayoutBankAdapter(this, R.layout.row_price_selector_item_dropdown, R.id.tvItemName, banklist, Gravity.CENTER);
        binding.etbank.setAdapter(bankAdapter);
        binding.etbank.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String name = banklist.get(position).getAccount();
                String usename = banklist.get(position).getName();
                binding.etSenderName.setText(usename);
                bankid=banklist.get(position).getId();
                binding.etbank.setText(name);
                binding.etbank.clearFocus();

            }
        });

    }

    private void setAccountType() {
        String[] list = getResources().getStringArray(R.array.Select_account_type);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                list
        );
        binding.etAccountType.setAdapter(adapter);
    }

    private void setbankdataList()
    {
        adapter = new BankListAdapter(activity,banklist, new BankListAdapter.OnItemClick() {
            @Override
            public void onClick(int position) {
                //  startActivity(new Intent(activity, WebViewActivity.class));

                deletebankapi(banklist.get(position).getId());
            }
        });
        binding.fetchbillrecycler.setLayoutManager(new LinearLayoutManager(activity));
        binding.fetchbillrecycler.setItemAnimator(new DefaultItemAnimator());
        binding.fetchbillrecycler.setAdapter(adapter);
        binding.fetchbillrecycler.setNestedScrollingEnabled(false);
    }

    private void deletebankapi(int id)
    {
        if (NetworkAlertUtility.isInternetConnection2(activity))
        {
            String accessToken = new StorageUtil(activity).getAccessToken();
            String apikey = new StorageUtil(activity).getApiKey();
            HashMap<String, RequestBody> map = new HashMap<>();
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("id", String.valueOf(id))
                    .build();
            //=======================================
            HashMap<String, String> headers = new HashMap<>();
            headers.put("headerToken", accessToken);
            headers.put("headerKey", apikey);
            presenter.deleteBanificiary(activity,headers,requestBody,true);

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
            presenter.doPayoutTransaction(activity,headers,requestBody,true);

        } else
        {
            NetworkAlertUtility.showNetworkFailureAlert(activity);
        }
    }
    @Override
    public void onClick(View v)
    {
        int id = v.getId();
//        switch (id) {
//            case R.id.btnsendotp:
//                  checkvalidation();
//                break;
//        }
        if (id==R.id.btnsendotp){
            checkvalidation();
        }
    }

    private void checkvalidation()
    {

        mobile = Objects.requireNonNull(binding.etmobilenumber.getText()).toString();
        amount = Objects.requireNonNull(binding.etamount.getText()).toString();
        senderName= Objects.requireNonNull(binding.etSenderName.getText()).toString();

        if(TextUtils.isEmpty(mobile)){
            binding.etmobilenumber.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_mobile_number));
        }else if (bankid==0) {
            binding.etbank.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_bank));
        }else if (TextUtils.isEmpty(amount)) {
            binding.etamount.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_amount));
        }else if (TextUtils.isEmpty(senderName)) {
            binding.etSenderName.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_sender_name));
        } else{
            doInitiateTransaction();
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
                    .addFormDataPart("bid", String.valueOf(bankid))
                    .addFormDataPart("amount", amount)
                    .addFormDataPart("mode", "IMPS")
                    .addFormDataPart("sname", senderName)
                    .addFormDataPart("lat", stringLatitude)
                    .addFormDataPart("log", stringLongitude)
                    .addFormDataPart("account_type", String.valueOf(accountType))
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

    @Override
    public void onBankListSuccess(BaseResponse body) {
        if(body!=null){
            if(banklist.size()>0)
                banklist.clear();

            banklist.addAll(body.getData().getPayoutAccounts());
            if(banklist.size()>0)
                 binding.lltransactionLayout.setVisibility(View.VISIBLE);
                else
                     binding.lltransactionLayout.setVisibility(View.GONE);

            setbankdataList();
            setUpBanklist();

        }
    }

    @Override
    public void onDeleteBankSuccess(BaseResponse body) {
        if(body!=null){
            new CustomToastNotification(activity,body.getMessage());
            bankAdapter.notifyDataSetChanged();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onInitiateTransactionSuccess(BaseResponse body) {
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
    public void onPayoutTransactionSuccess(BaseResponse body) {
        if(body!=null){
            new CustomToastNotification(activity,body.getMessage());
            qtDataModel=body.getData().getPayoutData();

            if(pinflag){
                pinBottomSheet.dismiss();}
            if(otpflag){
                otpBottomSheet.dismiss();}

            if(qtDataModel!=null){
                setQtRecieptBottomSheet();
            }


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

        qtBinding.lblQt.setText(getResources().getString(R.string.lbl_payout));
        qtBinding.lblCharge.setText(getResources().getString(R.string.lbl_payout_charge));


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