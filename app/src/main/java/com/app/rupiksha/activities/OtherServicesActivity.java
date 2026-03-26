package com.app.rupiksha.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.app.rupiksha.R;
import com.app.rupiksha.adapters.SearchSubscriptionBillerAdapter;
import com.app.rupiksha.apipresenter.OtherServiceImpPresenter;
import com.app.rupiksha.databinding.ActivityOtherServicesBinding;
import com.app.rupiksha.databinding.OperatorsearchbottomsheetBinding;
import com.app.rupiksha.extra.CustomToastNotification;
import com.app.rupiksha.extra.NetworkAlertUtility;
import com.app.rupiksha.interfaces.IOtherServiceImpView;
import com.app.rupiksha.models.BaseResponse;
import com.app.rupiksha.models.ModelBillerDetails;
import com.app.rupiksha.models.ModelSubscriptionBiller;
import com.app.rupiksha.models.ModelSubscriptionFetchBill;
import com.app.rupiksha.models.ModelSubscriptionPayBill;
import com.app.rupiksha.storage.StorageUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class OtherServicesActivity extends AppCompatActivity implements IOtherServiceImpView, View.OnClickListener {
    ActivityOtherServicesBinding binding;
    Activity activity;
    String title="",skey="",fetchamount="";
    String type="";
    String name="";
    Integer id;
    Context context;
    Dialog operatorBottomSheet;
    LinearLayoutManager linearLayoutManager;
    OperatorsearchbottomsheetBinding myCustomDialog;
    SearchSubscriptionBillerAdapter searchSubscriptionBillerAdapter;
    private List<ModelSubscriptionBiller> billerList=new ArrayList<>();
    private List<ModelSubscriptionBiller> storecategories = new ArrayList<>();
    private List<ModelSubscriptionBiller> search = new ArrayList<>();
    String customerid,ad1value,ad2value,ad3value,amount;
    private ModelSubscriptionFetchBill modelSubscriptionFetchBill;
    private ModelBillerDetails billerDetails;
    private ModelSubscriptionPayBill modelSubscriptionPayBill;
    OtherServiceImpPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      /*  setContentView(R.layout.activity_other_services);*/
        binding = DataBindingUtil.setContentView(this, R.layout.activity_other_services);

        activity = OtherServicesActivity.this;
        presenter=new OtherServiceImpPresenter();
        presenter.setView(this);

        if (getIntent().hasExtra("title"))
            title = getIntent().getStringExtra("title");

        if (getIntent().hasExtra("type"))
            type = getIntent().getStringExtra("type");

        title = title.replaceAll("\n","");
        binding.includeLayout.toolBar.setTitle(""+title);
        binding.tvtitle.setText(title);

        switch(type){
            case "fastag" :
                getBiller(type);
                break;
            case "credit-card" :
                getBiller(type);
                break;
        }
        binding.etbank.setOnClickListener(this);
        binding.btnfetchbill.setOnClickListener(this);
        binding.btnpaynow1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
            int id = v.getId();
//            switch (id) {
//                case R.id.etbank:
//                    setUpBanklist();
//                    break;
//                case R.id.btnfetchbill:
//                    checkValidation();
//                    break;
//                case R.id.btnpaynow1:
//                    checkValidation1();
//                  /*  directpaybill();*/
//                    break;
//            }
            if (id==R.id.etbank){
                setUpBanklist();
            } else if (id==R.id.btnfetchbill){
                checkValidation();
            }else if (id==R.id.btnpaynow1){
                checkValidation1();
            }

        }

    private void checkValidation1() {
        customerid = Objects.requireNonNull(binding.etservicenumber.getText()).toString();
        ad1value = Objects.requireNonNull(binding.etad1.getText()).toString();
        ad2value=Objects.requireNonNull(binding.etad2.getText()).toString();
        ad3value=Objects.requireNonNull(binding.etad3.getText()).toString();
        amount=Objects.requireNonNull(binding.etamount.getText()).toString();

        if ((binding.tilservicenumber.getVisibility()==View.VISIBLE)&&(TextUtils.isEmpty(customerid))) {
            binding.etservicenumber.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_mandatory_field));

        }
        else if((binding.tilad1.getVisibility()==View.VISIBLE)&&(TextUtils.isEmpty(ad1value))){
            binding.etad1.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_parm1));

        }
        else if((binding.tilad2.getVisibility()==View.VISIBLE)&&(TextUtils.isEmpty(ad2value))){
            binding.etad2.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_parm2));

        }
        else if((binding.tilad3.getVisibility()==View.VISIBLE)&&(TextUtils.isEmpty(ad3value))){
            binding.etad3.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_parm3));

        }
        else{
            directpaybill();
        }
    }

    private void checkValidation()
    {
        customerid = Objects.requireNonNull(binding.etservicenumber.getText()).toString();
        ad1value = Objects.requireNonNull(binding.etad1.getText()).toString();
        ad2value=Objects.requireNonNull(binding.etad2.getText()).toString();
        ad3value=Objects.requireNonNull(binding.etad3.getText()).toString();
        amount=Objects.requireNonNull(binding.etamount.getText()).toString();

        if ((binding.tilservicenumber.getVisibility()==View.VISIBLE)&&(TextUtils.isEmpty(customerid))) {
            binding.etservicenumber.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_mandatory_field));

        }
        else if((binding.tilad1.getVisibility()==View.VISIBLE)&&(TextUtils.isEmpty(ad1value))){
            binding.etad1.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_parm1));

        }
        else if((binding.tilad2.getVisibility()==View.VISIBLE)&&(TextUtils.isEmpty(ad2value))){
            binding.etad2.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_parm2));

        }
        else if((binding.tilad3.getVisibility()==View.VISIBLE)&&(TextUtils.isEmpty(ad3value))){
            binding.etad3.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_parm3));

        }
        else{
            fetchBill();
        }
    }

    private void fetchBill() {

        if (NetworkAlertUtility.isInternetConnection2(activity))
            {
                String accessToken = new StorageUtil(activity).getAccessToken();
                String apikey = new StorageUtil(activity).getApiKey();
                HashMap<String, RequestBody> map = new HashMap<>();
                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("billerId", String.valueOf(id))
                        .addFormDataPart("param1", ad1value)
                        .addFormDataPart("param2", ad2value)
                        .build();
                //==============================================================
                HashMap<String, String> headers = new HashMap<>();
                headers.put("headerToken", accessToken);
                headers.put("headerKey", apikey);
                presenter.getFetchbilllist(activity,headers,requestBody,true);

            } else
            {
                NetworkAlertUtility.showNetworkFailureAlert(activity);
            }
        }

    private void setUpBanklist() {
        operatorBottomSheet = new Dialog(activity, R.style.MyDialogTheme);
        myCustomDialog = DataBindingUtil.inflate(getLayoutInflater(), R.layout.operatorsearchbottomsheet, null, false);
        operatorBottomSheet.setContentView(myCustomDialog.getRoot());
        operatorBottomSheet.setCancelable(true);
        operatorBottomSheet.setCanceledOnTouchOutside(true);
        operatorBottomSheet.show();

        storecategories = new StorageUtil(activity).getSubscriptionBiller(activity);
        billerList.addAll(storecategories);
        myCustomDialog.etname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int i1, int i2) {
                if (s.length() == 0) {
                    // binding.cancel.setVisibility(View.GONE);
                    if (billerList.size() > 0) {
                        billerList.clear();
                    }
                    billerList.addAll(storecategories);
                    searchSubscriptionBillerAdapter.notifyDataSetChanged();
                }
                if (s.length() > 0) {
                    // binding.cancel.setVisibility(View.VISIBLE);
                    if (search.size() > 0)
                        search.clear();
                    for (int i = 0; i < storecategories.size(); i++) {
                        if ((storecategories.get(i).getBiller().toLowerCase(Locale.ROOT).contains(s.toString().toLowerCase()) == true)) {
                            if (!search.contains(storecategories.get(i)))
                                search.add(storecategories.get(i));
                        }
                    }
                    if (billerList.size() > 0)
                        billerList.clear();

                    billerList.addAll(search);
                    searchSubscriptionBillerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        searchSubscriptionBillerAdapter = new SearchSubscriptionBillerAdapter(activity, this.billerList, new SearchSubscriptionBillerAdapter.OnItemClick() {
            @Override
            public void onClick(int position) {
                {
                     name = billerList.get(position).getBiller();
                        id = billerList.get(position).getId();
                  /*  billerid = billerList.get(position).getId();*/
                    binding.etbank.setText(name);
                    operatorBottomSheet.dismiss();

                    getBillerDetails(type,id,name);
                    /*binding.tilad1.setVisibility(View.GONE);
                    binding.tilad2.setVisibility(View.GONE);
                    binding.tilad3.setVisibility(View.GONE);
                    binding.btnpaynow1.setVisibility(View.VISIBLE);
                    binding.tilamount.setVisibility(View.VISIBLE);
                    binding.btnfetchbill.setVisibility(View.GONE);
                    binding.tilservicenumber.setVisibility(View.GONE);
                    binding.llfetchbillLayout.setVisibility(View.GONE);
                    binding.lltransactionLayout.setVisibility(View.GONE);*/


                   /* if (billerList.get(position).getViewbill().isEmpty() || billerList.get(position).getViewbill().equalsIgnoreCase("0")) {
                        binding.btnpaynow1.setVisibility(View.VISIBLE);
                        binding.tilamount.setVisibility(View.VISIBLE);
                        binding.btnfetchbill.setVisibility(View.GONE);
                    }
                    else {
                        binding.btnpaynow1.setVisibility(View.GONE);
                        binding.tilamount.setVisibility(View.GONE);
                        binding.btnfetchbill.setVisibility(View.VISIBLE);
                    }

                    if ((billerList.get(position).getDisplayType().equals(""))) {

                        binding.tilservicenumber.setVisibility(View.GONE);
                    } else {

                        binding.tilservicenumber.setVisibility(View.VISIBLE);
                        binding.tilservicenumber.setHint(billerList.get(position).getDisplayname());
                        binding.tilservicenumber.setPlaceholderText(billerList.get(position).getDisplayname());
                    }

                    if ((billerList.get(position).getAd1Type().equals(""))) {
                        binding.tilad1.setVisibility(View.GONE);
                    } else {
                        binding.tilad1.setVisibility(View.VISIBLE);
                        binding.tilad1.setHint(billerList.get(position).getAd1Name());
                        binding.tilad1.setPlaceholderText(billerList.get(position).getAd1Name());

                        if (billerList.get(position).getAd1Type().equalsIgnoreCase("mobile")) {

                            binding.etad1.setInputType(InputType.TYPE_CLASS_PHONE);
                        }
                        else if (billerList.get(position).getAd1Type().equalsIgnoreCase("date")) {
                            binding.etad1.setInputType(InputType.TYPE_CLASS_TEXT);
                        }
                        else if (billerList.get(position).getAd1Type().equalsIgnoreCase("email")) {
                            binding.etad1.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                        }
                        else if (billerList.get(position).getAd1Type().equalsIgnoreCase("text")) {
                            binding.etad1.setInputType(InputType.TYPE_CLASS_TEXT);
                        }
                    }

                    if ((billerList.get(position).getAd2Type().equals(""))) {

                    } else {
                        binding.tilad2.setVisibility(View.VISIBLE);
                        binding.tilad2.setHint(billerList.get(position).getAd2Name());
                        binding.tilad2.setPlaceholderText(billerList.get(position).getAd2Name());
                        if (billerList.get(position).getAd2Type().equalsIgnoreCase("mobile")) {

                            binding.etad2.setInputType(InputType.TYPE_CLASS_PHONE);
                        }
                        else if (billerList.get(position).getAd2Type().equalsIgnoreCase("date")) {

                            binding.etad2.setInputType(InputType.TYPE_CLASS_TEXT);
                        }
                        else if (billerList.get(position).getAd2Type().equalsIgnoreCase("email")) {
                            binding.etad2.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                        }
                        else if (billerList.get(position).getAd2Type().equalsIgnoreCase("text")) {
                            binding.etad2.setInputType(InputType.TYPE_CLASS_TEXT);
                        }
                    }

                    if ((billerList.get(position).getAd3Type().equals(""))) {

                    } else {
                        binding.tilad3.setVisibility(View.VISIBLE);
                        binding.tilad3.setHint(billerList.get(position).getAd3Name());
                        binding.tilad3.setPlaceholderText(billerList.get(position).getAd3Name());
                        if (billerList.get(position).getAd3Type().equalsIgnoreCase("mobile")) {
                            binding.etad3.setInputType(InputType.TYPE_CLASS_PHONE);
                        }
                        else if (billerList.get(position).getAd3Type().equalsIgnoreCase("date")) {

                            binding.etad3.setInputType(InputType.TYPE_CLASS_TEXT);
                        }
                        else if (billerList.get(position).getAd3Type().equalsIgnoreCase("email")) {
                            binding.etad3.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                        }
                        else if (billerList.get(position).getAd3Type().equalsIgnoreCase("text")) {
                            binding.etad3.setInputType(InputType.TYPE_CLASS_TEXT);
                        }
                    }*/
                }
            }


        });
        myCustomDialog.opteratorrecycler.setLayoutManager(linearLayoutManager);
        myCustomDialog.opteratorrecycler.setAdapter(searchSubscriptionBillerAdapter);

        /*binding.etbank.setThreshold(100);
        billersAdapter = new BillersAdapter(this, R.layout.row_price_selector_item_dropdown, R.id.tvItemName, billerList, Gravity.CENTER);
        binding.etbank.setAdapter(billersAdapter);
        binding.etbank.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String name = billerList.get(position).getName();
                billerid=billerList.get(position).getId();
                binding.etbank.setText(name);
                binding.etbank.clearFocus();

                binding.tilad1.setVisibility(View.GONE);
                binding.tilad2.setVisibility(View.GONE);
                binding.tilad3.setVisibility(View.GONE);
                binding.btnpaynow1.setVisibility(View.VISIBLE);
                binding.tilamount.setVisibility(View.VISIBLE);
                binding.btnfetchbill.setVisibility(View.GONE);
                binding.tilservicenumber.setVisibility(View.GONE);
                binding.llfetchbillLayout.setVisibility(View.GONE);
                binding.lltransactionLayout.setVisibility(View.GONE);


                if(billerList.get(position).getViewbill().isEmpty() || billerList.get(position).getViewbill().equalsIgnoreCase("0")){
                    binding.btnpaynow1.setVisibility(View.VISIBLE);
                    binding.tilamount.setVisibility(View.VISIBLE);
                    binding.btnfetchbill.setVisibility(View.GONE);
                }else{
                    binding.btnpaynow1.setVisibility(View.GONE);
                    binding.tilamount.setVisibility(View.GONE);
                    binding.btnfetchbill.setVisibility(View.VISIBLE);
                }

                if ((billerList.get(position).getDisplayType().equals(""))) {

                    binding.tilservicenumber.setVisibility(View.GONE);
                }else{

                    binding.tilservicenumber.setVisibility(View.VISIBLE);
                    binding.tilservicenumber.setHint(billerList.get(position).getDisplayname());
                    binding.tilservicenumber.setPlaceholderText(billerList.get(position).getDisplayname());
                }

                if((billerList.get(position).getAd1Type().equals("")))
                {
                    binding.tilad1.setVisibility(View.GONE);
                }else{
                    binding.tilad1.setVisibility(View.VISIBLE);
                    binding.tilad1.setHint(billerList.get(position).getAd1Name());
                    binding.tilad1.setPlaceholderText(billerList.get(position).getAd1Name());

                    if (billerList.get(position).getAd1Type().equalsIgnoreCase("mobile")) {

                        binding.etad1.setInputType(InputType.TYPE_CLASS_PHONE);
                    } else if (billerList.get(position).getAd1Type().equalsIgnoreCase("date")) {
                        binding.etad1.setInputType(InputType.TYPE_CLASS_TEXT);
                    } else if (billerList.get(position).getAd1Type().equalsIgnoreCase("email")) {
                        binding.etad1.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                    } else if (billerList.get(position).getAd1Type().equalsIgnoreCase("text")) {
                        binding.etad1.setInputType(InputType.TYPE_CLASS_TEXT);
                    }
                }

                if((billerList.get(position).getAd2Type().equals(""))) {

                }else{
                    binding.tilad2.setVisibility(View.VISIBLE);
                    binding.tilad2.setHint(billerList.get(position).getAd1Name());
                    binding.tilad2.setPlaceholderText(billerList.get(position).getAd1Name());
                    if (billerList.get(position).getAd2Type().equalsIgnoreCase("mobile")) {

                        binding.etad2.setInputType(InputType.TYPE_CLASS_PHONE);
                    } else if (billerList.get(position).getAd2Type().equalsIgnoreCase("date")) {

                        binding.etad2.setInputType(InputType.TYPE_CLASS_TEXT);
                    } else if (billerList.get(position).getAd2Type().equalsIgnoreCase("email")) {
                        binding.etad2.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                    } else if (billerList.get(position).getAd2Type().equalsIgnoreCase("text")) {
                        binding.etad2.setInputType(InputType.TYPE_CLASS_TEXT);
                    }
                }

                if((billerList.get(position).getAd3Type().equals(""))) {

                }else{
                    binding.tilad3.setVisibility(View.VISIBLE);
                    binding.tilad3.setHint(billerList.get(position).getAd1Name());
                    binding.tilad3.setPlaceholderText(billerList.get(position).getAd1Name());
                    if (billerList.get(position).getAd3Type().equalsIgnoreCase("mobile")) {
                        binding.etad3.setInputType(InputType.TYPE_CLASS_PHONE);
                    } else if (billerList.get(position).getAd3Type().equalsIgnoreCase("date")) {

                        binding.etad3.setInputType(InputType.TYPE_CLASS_TEXT);
                    } else if (billerList.get(position).getAd3Type().equalsIgnoreCase("email")) {
                        binding.etad3.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                    }else if (billerList.get(position).getAd3Type().equalsIgnoreCase("text")) {
                        binding.etad3.setInputType(InputType.TYPE_CLASS_TEXT);
                    }
                }
*/
    }

    private void getBillerDetails(String type, Integer id, String name) {
        if (NetworkAlertUtility.isInternetConnection2(activity)) {
            String accessToken = new StorageUtil(activity).getAccessToken();
            String apikey = new StorageUtil(activity).getApiKey();
            //======================================

            //=======================================
            HashMap<String, String> headers = new HashMap<>();
            headers.put("headerToken", accessToken);
            headers.put("headerKey", apikey);
            presenter.getbillerdetails(activity, headers,type,id,name,true);

        } else {
            NetworkAlertUtility.showNetworkFailureAlert(activity);
        }
    }

    private void getBiller(String type) {
        if (NetworkAlertUtility.isInternetConnection2(activity)) {
            String accessToken = new StorageUtil(activity).getAccessToken();
            String apikey = new StorageUtil(activity).getApiKey();
            //======================================

            //=======================================
            HashMap<String, String> headers = new HashMap<>();
            headers.put("headerToken", accessToken);
            headers.put("headerKey", apikey);
            presenter.getbillerlist(activity, headers,type,true);

        } else {
            NetworkAlertUtility.showNetworkFailureAlert(activity);
        }
    }

    private void directpaybill()
    {
        if (NetworkAlertUtility.isInternetConnection2(activity)) {
            String accessToken = new StorageUtil(activity).getAccessToken();
            String apikey = new StorageUtil(activity).getApiKey();

            //======================================
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("billerId", String.valueOf(id))
                    .addFormDataPart("param1", String.valueOf(customerid))
                    .addFormDataPart("param2", String.valueOf(ad1value))
                    .addFormDataPart("amount", String.valueOf(amount))
                    .addFormDataPart("skey", skey)
                    .build();
            //=======================================
            HashMap<String, String> headers = new HashMap<>();
            headers.put("headerToken", accessToken);
            headers.put("headerKey", apikey);

            presenter.getPayBillList(activity, headers, requestBody,true);

        } else {
            NetworkAlertUtility.showNetworkFailureAlert(activity);
        }
    }

    @Override
    public void onBillerListSuccess(BaseResponse body) {
        if(body!=null){
            if(billerList.size()>0)
                billerList.clear();

            billerList.addAll(body.getData().getSubscriptionBillers());

            new StorageUtil(activity).saveSubscriptionBiller((ArrayList<ModelSubscriptionBiller>) billerList);

        }
    }

    @Override
    public void onBillerDetailsSuccess(BaseResponse body) {
        if(body!=null) {
            billerDetails = body.getData().getBillerDetails();
            if (billerDetails != null) {
                binding.tilad1.setVisibility(View.GONE);
                binding.tilad2.setVisibility(View.GONE);
                binding.tilad3.setVisibility(View.GONE);
                binding.btnpaynow1.setVisibility(View.VISIBLE);
                binding.tilamount.setVisibility(View.VISIBLE);
                binding.btnfetchbill.setVisibility(View.GONE);
                binding.tilservicenumber.setVisibility(View.GONE);
                binding.llfetchbillLayout.setVisibility(View.GONE);
                binding.lltransactionLayout.setVisibility(View.GONE);


                if ( billerDetails.getFetchBill()==1) {
                    binding.btnpaynow1.setVisibility(View.VISIBLE);
                    binding.tilamount.setVisibility(View.VISIBLE);
                    binding.btnfetchbill.setVisibility(View.GONE);
                } else {
                    binding.btnpaynow1.setVisibility(View.GONE);
                    binding.tilamount.setVisibility(View.GONE);
                    binding.btnfetchbill.setVisibility(View.VISIBLE);
                }
                if (billerDetails.getParam1().equals("")) {
                    binding.tilservicenumber.setVisibility(View.GONE);
                }else {
                    binding.tilservicenumber.setVisibility(View.VISIBLE);
                    binding.tilservicenumber.setHint(billerDetails.getParam1());
                }
                if (billerDetails.getParam2().equals("")) {
                    binding.tilad1.setVisibility(View.GONE);
                }else {
                    binding.tilad1.setVisibility(View.VISIBLE);
                    binding.tilad1.setHint(billerDetails.getParam2());
                }
                if ( billerDetails.getParam3().equals("")) {
                    binding.tilad2.setVisibility(View.GONE);
                }else {
                    binding.tilad2.setVisibility(View.VISIBLE);
                    binding.tilad2.setHint(billerDetails.getParam3());
                }

                if (billerDetails.getParam4().equals("")) {
                    binding.tilad3.setVisibility(View.GONE);
                }else {
                    binding.tilad3.setVisibility(View.VISIBLE);
                    binding.tilad3.setHint(billerDetails.getParam4());
                }
            }
        }
    }

    @Override
    public void onFetchBillSuccess(BaseResponse body) {
        if (body != null) {
            skey = body.getSkey();
            fetchamount = body.getData().getSubscriptionFetchBill().getItem3value();
            modelSubscriptionFetchBill = body.getData().getSubscriptionFetchBill();
            binding.llfetchbillLayout.setVisibility(View.VISIBLE);
            setFetchbillData();
        }
    }

    private void setFetchbillData()
    {
        binding.billtitle.setText(""+modelSubscriptionFetchBill.getMessage());

        binding.lblValue1.setText(modelSubscriptionFetchBill.getName());
        binding.lblValue2.setText(modelSubscriptionFetchBill.getBillNumber());

       /* binding.lblName3.setText(modelSubscriptionFetchBill.getParamname());
        binding.lblValue3.setText(modelSubscriptionFetchBill.getParamvalue());*/

        binding.lblName4.setText(modelSubscriptionFetchBill.getItem1());
        binding.lblValue4.setText(modelSubscriptionFetchBill.getItem1value());

        binding.lblName5.setText(modelSubscriptionFetchBill.getItem2());
        binding.lblValue5.setText(modelSubscriptionFetchBill.getItem2value());
        binding.lblName6.setText(modelSubscriptionFetchBill.getItem3());
        binding.lblValue6.setText(modelSubscriptionFetchBill.getItem3value());
    }

    @Override
    public void onPayBillSuccess(BaseResponse body) {
        if(body!=null){
            new CustomToastNotification(activity,body.getMessage());
          /*  setBbpstransactionList();*/
            binding.lltransactionLayout.setVisibility(View.VISIBLE);
            binding.llfetchbillLayout.setVisibility(View.GONE);
            binding.etamount.setText("");
            binding.etservicenumber.setText("");
            binding.etad1.setText("");
            binding.etad2.setText("");
            binding.etad3.setText("");
            binding.etbank.clearListSelection();
        }
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