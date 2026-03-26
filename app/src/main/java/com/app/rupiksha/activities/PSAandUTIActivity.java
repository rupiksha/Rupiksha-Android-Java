package com.app.rupiksha.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.app.rupiksha.R;
import com.app.rupiksha.apipresenter.PanUTIServicePresenter;
import com.app.rupiksha.databinding.ActivityPsaandUtiactivityBinding;
import com.app.rupiksha.databinding.SuccespansheetBinding;
import com.app.rupiksha.extra.CustomToastNotification;
import com.app.rupiksha.extra.NetworkAlertUtility;
import com.app.rupiksha.interfaces.IPANserviceView;
import com.app.rupiksha.models.BaseResponse;
import com.app.rupiksha.models.UtiReceiptModel;
import com.app.rupiksha.storage.StorageUtil;
import com.app.rupiksha.utils.Utils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.HashMap;
import java.util.Objects;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class PSAandUTIActivity extends AppCompatActivity implements IPANserviceView ,View.OnClickListener{
    ActivityPsaandUtiactivityBinding binding;
    Activity activity;
    PanUTIServicePresenter presenter;
    String name,qty,amount;
    SuccespansheetBinding qtBinding;
    BottomSheetDialog qtBottomSheet;
    UtiReceiptModel qtDataModel;
    double value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_psaand_utiactivity);
        activity=PSAandUTIActivity.this;
        presenter=new PanUTIServicePresenter();
        presenter.setView(this);
        if (getIntent().hasExtra("amount"))
            value = getIntent().getDoubleExtra("amount",0.0);
        binding.includeLayout.toolBar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });

            binding.etamount.setText(""+value);


        binding.btnsubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();
//        switch (id) {
//            case R.id.btnsubmit:
//                checkvalidation();
//                break;
//        }
        if (id == R.id.btnsubmit) {
            checkvalidation();
        }
    }

    private void checkvalidation()
    {
        name = Objects.requireNonNull(binding.etname.getText()).toString();
        amount = Objects.requireNonNull(binding.etamount.getText()).toString();
        qty = Objects.requireNonNull(binding.etqty.getText()).toString();
        if(TextUtils.isEmpty(name))
        {
            binding.etname.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_name));
        }else if (TextUtils.isEmpty(qty)) {
            binding.etqty.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_qty));
        }else if (TextUtils.isEmpty(amount)) {
            binding.etamount.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_amount));
        }else{
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
                    .addFormDataPart("qty", qty)
                    .build();
            //==============================================================
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
    public void onPANUTISuccess(BaseResponse body)
    {
       if(body!=null){
           new CustomToastNotification(activity,body.getMessage());
           qtDataModel=body.getData().getUtiReceipt();

           if(qtDataModel!=null) {
               setQtRecieptBottomSheet();
           }
       }
    }

    private void setQtRecieptBottomSheet()
    {
        qtBottomSheet = new BottomSheetDialog(activity, R.style.CustomBottomSheetDialogThemeWithDim);
        qtBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.succespansheet, null, false);
        qtBottomSheet.setContentView(qtBinding.getRoot());
        qtBottomSheet.setCancelable(true);
        qtBottomSheet.setCanceledOnTouchOutside(false);
        qtBottomSheet.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);

        qtBinding.date.setText(qtDataModel.getDate());
        qtBinding.txId.setText(qtDataModel.getTxnid());
        qtBinding.status.setText(qtDataModel.getStatus());
        if(qtDataModel.getStatus().equalsIgnoreCase("FAILED")){
            qtBinding.status.setTextColor(activity.getResources().getColor(R.color.color_red));
        }else if(qtDataModel.getStatus().equalsIgnoreCase("FAILED")){
        qtBinding.status.setTextColor(activity.getResources().getColor(R.color.color_yellow));
        }else{
            qtBinding.status.setTextColor(activity.getResources().getColor(R.color.color_green));

        }

        qtBinding.lblQty.setText(qtDataModel.getItem1());
        qtBinding.lblTotal.setText(qtDataModel.getItem2());
        qtBinding.qty.setText(""+qtDataModel.getAmount1());
        qtBinding.total.setText(getResources().getString(R.string.rupees) + " " +qtDataModel.getAmount2());

        qtBinding.btnConfirm.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                qtBottomSheet.dismiss();
            }
        });
        qtBinding.btnscreen.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Utils.sharepdf(activity,qtBinding.recptlayout);
            }
        });
        qtBottomSheet.show();
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