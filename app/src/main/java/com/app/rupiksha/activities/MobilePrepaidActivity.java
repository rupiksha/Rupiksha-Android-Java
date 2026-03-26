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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;

import com.app.rupiksha.R;
import com.app.rupiksha.adapters.OperatorAdapter;
import com.app.rupiksha.adapters.RechargeTransactionDetailAdapter;
import com.app.rupiksha.apipresenter.PrepaidRechargePresenter;
import com.app.rupiksha.databinding.ActivityMobilePrepaidBinding;
import com.app.rupiksha.databinding.MobilerechargebsheetBinding;
import com.app.rupiksha.extra.CustomToastNotification;
import com.app.rupiksha.extra.NetworkAlertUtility;
import com.app.rupiksha.interfaces.IPrepaidRechargeView;
import com.app.rupiksha.models.AllPlans;
import com.app.rupiksha.models.BaseResponse;
import com.app.rupiksha.models.PlanDataModel;
import com.app.rupiksha.models.RechargeDataRecipt;
import com.app.rupiksha.models.RechargeOperatorModel;
import com.app.rupiksha.models.RechargeTransDetailModel;
import com.app.rupiksha.storage.StorageUtil;
import com.app.rupiksha.utils.Utils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class MobilePrepaidActivity extends AppCompatActivity implements View.OnClickListener , IPrepaidRechargeView {
    ActivityMobilePrepaidBinding binding;
    Activity activity;
    private List<RechargeTransDetailModel> bbpslist=new ArrayList<>();
    private List<RechargeOperatorModel> operatorlist=new ArrayList<>();
    String title="";
    String type="";
    String mobile="";
    String finalamount="";
    boolean flag=false;
    int operatorid=0;
    MobilerechargebsheetBinding myBottomBinding;
    BottomSheetDialog userBottomSheet;
    PrepaidRechargePresenter presenter;
    OperatorAdapter operatorAdapter;
    private List<AllPlans> planModel=new ArrayList<>();
    private List<PlanDataModel> catModel=new ArrayList<>();
    ActivityResultLauncher<Intent> launch;
    int amount;
     RechargeDataRecipt qtDataModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_mobile_prepaid);
        binding= DataBindingUtil.setContentView(MobilePrepaidActivity.this,R.layout.activity_mobile_prepaid);
        activity=MobilePrepaidActivity.this;
        presenter=new PrepaidRechargePresenter();
        presenter.setView(this);
       // binding.includeLayout.toolBar.setTitle(getResources().getString(R.string.label_bbps_report));
        binding.includeLayout.toolBar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });
        if (getIntent().hasExtra("title"))
            title = getIntent().getStringExtra("title");

        if (getIntent().hasExtra("type"))
            type = getIntent().getStringExtra("type");
        binding.includeLayout.toolBar.setTitle(""+title);

        binding.btnpaynow.setOnClickListener(this);
        binding.btnvieplan.setOnClickListener(this);

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
                                                                //  getFetchOperator(mobile);
                                                              }else{
                                                                  binding.etbank.setText("");
                                                                  binding.etamount.setText("");
                                                                  binding.btnvieplan.setVisibility(View.GONE);
                                                                  binding.btnpaynow.setVisibility(View.VISIBLE);
                                                                  operatorid=0;
                                                              }
                                                          }
                                                      });

        launch = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK)
                        {
                            flag=result.getData().getBooleanExtra("flag",false);
                            amount=result.getData().getIntExtra("amount",0);
                            if(flag)
                            {
                               binding.etamount.setText(""+amount);
                               binding.btnpaynow.setVisibility(View.VISIBLE);
                            }else{

                            }
                        }
                    }
                });

        getOperatorList();
    }

    private void getOperatorList()
    {
        if (NetworkAlertUtility.isInternetConnection2(activity))
        {
            String accessToken = new StorageUtil(activity).getAccessToken();
            String apikey = new StorageUtil(activity).getApiKey();
            //=======================================
            HashMap<String, String> headers = new HashMap<>();
            headers.put("headerToken", accessToken);
            headers.put("headerKey", apikey);
            presenter.getoperator(activity,headers,true);

        } else
        {
            NetworkAlertUtility.showNetworkFailureAlert(activity);
        }
    }

    private void getFetchOperator(String mobile)
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
            presenter.fetchoperator(activity, headers, requestBody,true);

        } else {
            NetworkAlertUtility.showNetworkFailureAlert(activity);
        }

    }

    private void setUpOperatorList()
    {
        binding.etbank.setThreshold(100);
        operatorAdapter = new OperatorAdapter(this, R.layout.row_price_selector_item_dropdown, R.id.tvItemName, operatorlist, Gravity.CENTER);
        binding.etbank.setAdapter(operatorAdapter);
        binding.etbank.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String name = operatorlist.get(position).getName();
                operatorid=operatorlist.get(position).getId();
                Log.e("12",""+String.valueOf(operatorid));

                binding.etbank.setText(name);
                binding.etbank.clearFocus();

            }
        });
    }


    private void settransactionList()
    {
        RechargeTransactionDetailAdapter adapter = new RechargeTransactionDetailAdapter(activity,bbpslist, new RechargeTransactionDetailAdapter.OnItemClick() {
            @Override
            public void onClick(int position)
            {
                binding.fetchbillrecycler.getAdapter().notifyDataSetChanged();
            }
        });
        binding.fetchbillrecycler.setLayoutManager(new LinearLayoutManager(activity));
        binding.fetchbillrecycler.setItemAnimator(new DefaultItemAnimator());
        binding.fetchbillrecycler.setAdapter(adapter);
        binding.fetchbillrecycler.setNestedScrollingEnabled(false);
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();
        Intent intent;
//        switch (id) {
//            case R.id.btnpaynow:
//                checkvalidation();
//                break;
//
//            case R.id.btnvieplan:
//
//                if(mobile.length()==10){
//                    intent = new Intent(this, ViewRechargePlanActivity.class).putExtra("data", (Serializable) planModel)
//                            .putExtra("title", (Serializable) catModel).putExtra("flag",true);
//                    launch.launch(intent);
//                }else{
//                    new CustomToastNotification(activity,getResources().getString(R.string.please_enter_digit_mobile_number));
//                }
//
//                break;
//        }

        if (id== R.id.btnpaynow){
           // checkvalidation();
        }else if (id == R.id.btnvieplan){
            if(mobile.length()==10){
                    intent = new Intent(this, ViewRechargePlanActivity.class).putExtra("data", (Serializable) planModel)
                            .putExtra("title", (Serializable) catModel).putExtra("flag",true);
                    launch.launch(intent);
                }else{
                    new CustomToastNotification(activity,getResources().getString(R.string.please_enter_digit_mobile_number));
                }
        }
    }

    private void checkvalidation()
    {
         finalamount=binding.etamount.getText().toString();

        if(TextUtils.isEmpty(finalamount))
        {
            binding.etmobilenumber.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_digit_mobile_number));
        }else if ((operatorid==0)) {
            binding.etbank.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_select_operator));
        }else if (TextUtils.isEmpty(finalamount)) {
            binding.etamount.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_amount));
        }else{
            if(!finalamount.isEmpty())
                amount = Integer.parseInt(finalamount);
          //  prepaidrechargeApi();
        }
    }

    private void prepaidrechargeApi() {
        if (NetworkAlertUtility.isInternetConnection2(activity)) {
            String accessToken = new StorageUtil(activity).getAccessToken();
            String apikey = new StorageUtil(activity).getApiKey();
            //======================================
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("mobile",String.valueOf(mobile) )
                    .addFormDataPart("operator",String.valueOf(operatorid) )
                    .addFormDataPart("amount", String.valueOf(amount))
                    .build();
            //=======================================
            HashMap<String, String> headers = new HashMap<>();
            headers.put("headerToken", accessToken);
            headers.put("headerKey", apikey);
            presenter.doRecharge(activity,headers,requestBody,true);

        } else {
            NetworkAlertUtility.showNetworkFailureAlert(activity);
        }
    }

    private void setbootomsheet()
    {
        userBottomSheet = new BottomSheetDialog(activity, R.style.CustomBottomSheetDialogThemeWithDim);
        myBottomBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.mobilerechargebsheet, null, false);
        userBottomSheet.setContentView(myBottomBinding.getRoot());
        userBottomSheet.setCancelable(true);
        userBottomSheet.setCanceledOnTouchOutside(false);
        userBottomSheet.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);
        userBottomSheet.show();

        myBottomBinding.date.setText(qtDataModel.getDate());
        myBottomBinding.txId.setText(qtDataModel.getTxnid());
        myBottomBinding.status.setText(qtDataModel.getStatus());
        if(qtDataModel.getStatus().equalsIgnoreCase("FAILED")){
            myBottomBinding.status.setTextColor(activity.getResources().getColor(R.color.color_red));
        }else  if(qtDataModel.getStatus().equalsIgnoreCase("SUCCESS")){
            myBottomBinding.status.setTextColor(activity.getResources().getColor(R.color.color_green));
        }else{
            myBottomBinding.status.setTextColor(activity.getResources().getColor(R.color.color_yellow));

        }

        myBottomBinding.lblQt.setText(qtDataModel.getItem1());
        myBottomBinding.lblCharge.setText(qtDataModel.getItem2());


        String wallet= String.valueOf(qtDataModel.getAmount1());
        if(wallet==null || wallet.equals("")){
            myBottomBinding.qtamount.setText(getResources().getString(R.string.rupees) + " " +wallet);
        }else{
            double famount= qtDataModel.getAmount1();
            myBottomBinding.qtamount.setText(getResources().getString(R.string.rupees) + " " +(new DecimalFormat("##.##").format(famount)));
        }

        String charge= String.valueOf(qtDataModel.getAmount2());
        if(charge==null || charge.equals("")){
            myBottomBinding.qtcharge.setText(getResources().getString(R.string.rupees) + " " +charge);
        }else{
            double famount= qtDataModel.getAmount2();
            myBottomBinding.qtcharge.setText(getResources().getString(R.string.rupees) + " " +(new DecimalFormat("##.##").format(famount)));
        }

        String total= String.valueOf(qtDataModel.getTotal());
        if(total==null || total.equals("")){
            myBottomBinding.total.setText(getResources().getString(R.string.rupees) + " " +total);
        }else{
            double famount= qtDataModel.getTotal();
            myBottomBinding.total.setText(getResources().getString(R.string.rupees) + " " +(new DecimalFormat("##.##").format(famount)));
        }



        myBottomBinding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userBottomSheet.dismiss();
            }
        });
        myBottomBinding.btnscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.sharepdf(activity,myBottomBinding.recieptlayout);
            }
        });
    }

    @Override
    public void onGetOperatorSuccess(BaseResponse body) {
        if(body!=null){
            if(operatorlist.size()>0)
                operatorlist.clear();

            operatorlist.addAll(body.getData().getOperatorList());
            setUpOperatorList();

        }
    }

    @Override
    public void onFetchOperatorSuccess(BaseResponse body)
    {
        if(body!=null)
        {

            if(body.getData().getAllPlans()==null || body.getData().getAllPlans().isEmpty() &&
                   body.getData().getPlanTitle().isEmpty() || body.getData().getPlanTitle() == null){
               new CustomToastNotification(this.activity,getResources().getString(R.string.text_no_plan_fetched));
               binding.btnvieplan.setVisibility(View.GONE);
               binding.btnpaynow.setVisibility(View.VISIBLE);
           }

           if(planModel.size()>0)
               planModel.clear();

            planModel.addAll(body.getData().getAllPlans());

            if(catModel.size()>0)
                catModel.clear();

            catModel.addAll(body.getData().getPlanTitle());

            setOperator(body.getId());
        }
    }

    private void setOperator(int id)
     {
        for(int i=0;i<operatorlist.size();i++){
            if(id==operatorlist.get(i).getId())
            {
                binding.etbank.setText(operatorlist.get(i).getName());
                operatorid=operatorlist.get(i).getId();
            }
        }
    }

    @Override
    public void onDoRechargeSuccess(BaseResponse body)
    {
        if(body!=null)
        {
            qtDataModel=body.getData().getRechargeData();
            if(bbpslist.size()>0){
                bbpslist.clear();
            }
            bbpslist.addAll(body.getData().getRecentTxn());

            if (bbpslist.size()==0){
                binding.lltransactionLayout.setVisibility(View.GONE);
            }else{
                binding.lltransactionLayout.setVisibility(View.VISIBLE);
                settransactionList();
            }

            setbootomsheet();
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