package com.app.rupiksha.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.app.rupiksha.R;
import com.app.rupiksha.adapters.BbpsFetchBillAdapter;
import com.app.rupiksha.adapters.BillersAdapter;
import com.app.rupiksha.adapters.SearchBbpsOperatorAdapter;
import com.app.rupiksha.apipresenter.BBPSServiceImpPresenter;
import com.app.rupiksha.constant.AppConstants;
import com.app.rupiksha.databinding.ActivityBbpsServiceBinding;
import com.app.rupiksha.databinding.OperatorsearchbottomsheetBinding;
import com.app.rupiksha.extra.CustomToastNotification;
import com.app.rupiksha.extra.NetworkAlertUtility;
import com.app.rupiksha.interfaces.IBBPSServiceImpView;
import com.app.rupiksha.models.BaseResponse;
import com.app.rupiksha.models.BbpsFetchBillModel;
import com.app.rupiksha.models.BillerModel;
import com.app.rupiksha.models.FetchBillModel;
import com.app.rupiksha.storage.StorageUtil;
import com.app.rupiksha.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class   BbpsServiceActivity extends AppCompatActivity  implements View.OnClickListener , IBBPSServiceImpView {

    ActivityBbpsServiceBinding binding;
    Activity activity;
    Context context;
    String title="";
    String type;
    Dialog operatorBottomSheet;
    LinearLayoutManager linearLayoutManager;
    OperatorsearchbottomsheetBinding myCustomDialog;
    SearchBbpsOperatorAdapter searchBbpsOperatorAdapter;
    private List<BillerModel>billerList=new ArrayList<>();
    private List<BbpsFetchBillModel> bbpstransactionlist=new ArrayList<>();
    private List<BillerModel> storecategories = new ArrayList<>();
    private List<BillerModel> search = new ArrayList<>();
    BBPSServiceImpPresenter presenter;
    BillersAdapter billersAdapter;
    int billerid;
    String customerid,ad1value,ad2value,ad3value,amount;
    FetchBillModel fetchBillModel;
    String skey="";
    String fetchamount="";
    private String stringLatitude = "";
    private String stringLongitude = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_bbps_service);
        binding= DataBindingUtil.setContentView(BbpsServiceActivity.this,R.layout.activity_bbps_service);
        activity=BbpsServiceActivity.this;
        context=BbpsServiceActivity.this;
        presenter=new BBPSServiceImpPresenter();
        presenter.setView(this);
        binding.includeLayout.toolBar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });
        if (getIntent().hasExtra("title"))
            title = getIntent().getStringExtra("title");

        if (getIntent().hasExtra("type"))
            type = getIntent().getStringExtra("type");

        binding.includeLayout.toolBar.setTitle(""+title);
       // binding.tvtitle.setText(""+title);

        stringLatitude = StorageUtil.getString(this, AppConstants.KEY_LAST_LAT);
        stringLongitude = StorageUtil.getString(this, AppConstants.KEY_LAST_LNG);

        binding.btnfetchbill.setOnClickListener(this);
        binding.btnpaynow.setOnClickListener(this);
        binding.btnpaynow1.setOnClickListener(this);
        binding.etbank.setOnClickListener(this);
        binding.llfetchbillLayout.setVisibility(View.GONE);
        binding.lltransactionLayout.setVisibility(View.GONE);

        binding.timinduration.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUpBanklist();
            }
        });

        getBilllerList();


    }

    private void getBilllerList()
    {
        if (NetworkAlertUtility.isInternetConnection2(context)) {
            String accessToken = new StorageUtil(activity).getAccessToken();
            String apikey = new StorageUtil(activity).getApiKey();
            //======================================
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("type", String.valueOf(type))
                    .build();
            //=======================================
            HashMap<String, String> headers = new HashMap<>();
            headers.put("headerToken", accessToken);
            headers.put("headerKey", apikey);
            presenter.getbillerlist(activity, headers, requestBody,true);

        } else {
            NetworkAlertUtility.showNetworkFailureAlert(context);
        }

    }


    private void setUpBanklist() {
        operatorBottomSheet = new Dialog(activity, R.style.MyDialogTheme);
        myCustomDialog = DataBindingUtil.inflate(getLayoutInflater(), R.layout.operatorsearchbottomsheet, null, false);
        operatorBottomSheet.setContentView(myCustomDialog.getRoot());
        operatorBottomSheet.setCancelable(true);
        operatorBottomSheet.setCanceledOnTouchOutside(true);
        operatorBottomSheet.show();

        storecategories = new StorageUtil(activity).getOperatorList(activity);
        if (billerList.size() > 0)
            billerList.clear();
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
                    searchBbpsOperatorAdapter.notifyDataSetChanged();
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
                    if (billerList.size() > 0)
                        billerList.clear();

                    billerList.addAll(search);
                    searchBbpsOperatorAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        searchBbpsOperatorAdapter = new SearchBbpsOperatorAdapter(activity, this.billerList, new SearchBbpsOperatorAdapter.OnItemClick() {
            @Override
            public void onClick(int position) {
                {
                    Log.e("position", "on click");
                    String name = billerList.get(position).getName();
//                    billerid = billerList.get(position).getId();
                    billerid = Integer.parseInt(billerList.get(position).getOperator_id());
                    binding.etbank.setText(name);
                    operatorBottomSheet.dismiss();

                    binding.tilad1.setVisibility(View.GONE);
                    binding.tilad2.setVisibility(View.GONE);
                    binding.tilad3.setVisibility(View.GONE);
                    binding.btnpaynow1.setVisibility(View.VISIBLE);
                    binding.tilamount.setVisibility(View.VISIBLE);
                    binding.btnfetchbill.setVisibility(View.GONE);
                    binding.tilservicenumber.setVisibility(View.GONE);
                    binding.llfetchbillLayout.setVisibility(View.GONE);
                    binding.lltransactionLayout.setVisibility(View.GONE);


                    if (billerList.get(position).getFetchBill().isEmpty() || billerList.get(position).getFetchBill().equalsIgnoreCase("0")) {
                        Log.d("position", "on click");
                        binding.btnpaynow1.setVisibility(View.VISIBLE);
                        binding.tilamount.setVisibility(View.VISIBLE);
                        binding.btnfetchbill.setVisibility(View.GONE);
                    }
                    else {
                        Log.d("position", "on clickbbbbbb");
                        binding.btnpaynow1.setVisibility(View.GONE);
                        binding.tilamount.setVisibility(View.GONE);
//                        binding.tilservicenumber.setVisibility(View.VISIBLE);
//                        binding.tilservicenumber.setHint(billerList.get(position).getDisplayname());
                        binding.btnfetchbill.setVisibility(View.VISIBLE);
                    }

                    String servicename = billerList.get(position).getParam1();
                    if (servicename == null || servicename.equals("")) {

                        binding.tilservicenumber.setVisibility(View.GONE);
                    } else {

                        binding.tilservicenumber.setVisibility(View.VISIBLE);
                        binding.tilservicenumber.setHint(billerList.get(position).getParam1());
                        binding.tilservicenumber.setPlaceholderText(billerList.get(position).getParam1());
                    }


                    String ad1DName = billerList.get(position).getParam2();
                    if (ad1DName == null || ad1DName.equals("")) {
                        binding.tilad1.setVisibility(View.GONE);
                    } else {
                        binding.tilad1.setVisibility(View.VISIBLE);
                        binding.tilad1.setHint(billerList.get(position).getParam2());
                        binding.tilad1.setPlaceholderText(billerList.get(position).getParam2());


//                        if (billerList.get(position).getAd1Type().equalsIgnoreCase("mobile")) {
//
//                            binding.etad1.setInputType(InputType.TYPE_CLASS_PHONE);
//                        }
//                        else if (billerList.get(position).getAd1Type().equalsIgnoreCase("date")) {
//                            binding.etad1.setInputType(InputType.TYPE_CLASS_TEXT);
//                        }
//                        else if (billerList.get(position).getAd1Type().equalsIgnoreCase("email")) {
//                            binding.etad1.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
//                        }
//                        else if (billerList.get(position).getAd1Type().equalsIgnoreCase("text")) {
//                            binding.etad1.setInputType(InputType.TYPE_CLASS_TEXT);
//                        }
                    }

//                    String ad2DName = billerList.get(position).getAd2DName();
//                    if (ad2DName == null || ad2DName.equals("")) {
//
//                    }
//                    else {
//                        binding.tilad2.setVisibility(View.VISIBLE);
//                        binding.tilad2.setHint(billerList.get(position).getAd2Name());
//                        binding.tilad2.setPlaceholderText(billerList.get(position).getAd2Name());
////                        if (billerList.get(position).getAd2Type().equalsIgnoreCase("mobile")) {
////
////                            binding.etad2.setInputType(InputType.TYPE_CLASS_PHONE);
////                        }
////                        else if (billerList.get(position).getAd2Type().equalsIgnoreCase("date")) {
////
////                            binding.etad2.setInputType(InputType.TYPE_CLASS_TEXT);
////                        }
////                        else if (billerList.get(position).getAd2Type().equalsIgnoreCase("email")) {
////                            binding.etad2.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
////                        }
////                        else if (billerList.get(position).getAd2Type().equalsIgnoreCase("text")) {
////                            binding.etad2.setInputType(InputType.TYPE_CLASS_TEXT);
////                        }
//                    }

//                    String ad3DName = billerList.get(position).getAd3DName();
//                    if (ad3DName == null || ad3DName.equals("")) {
//
//                    } else {
//                        binding.tilad3.setVisibility(View.VISIBLE);
//                        binding.tilad3.setHint(billerList.get(position).getAd3Name());
//                        binding.tilad3.setPlaceholderText(billerList.get(position).getAd3Name());
////                        if (billerList.get(position).getAd3Type().equalsIgnoreCase("mobile")) {
////                            binding.etad3.setInputType(InputType.TYPE_CLASS_PHONE);
////                        }
////                        else if (billerList.get(position).getAd3Type().equalsIgnoreCase("date")) {
////
////                            binding.etad3.setInputType(InputType.TYPE_CLASS_TEXT);
////                        }
////                        else if (billerList.get(position).getAd3Type().equalsIgnoreCase("email")) {
////                            binding.etad3.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
////                        }
////                        else if (billerList.get(position).getAd3Type().equalsIgnoreCase("text")) {
////                            binding.etad3.setInputType(InputType.TYPE_CLASS_TEXT);
////                        }
//                    }
                }
            }
        });
        myCustomDialog.opteratorrecycler.setLayoutManager(linearLayoutManager);
        myCustomDialog.opteratorrecycler.setAdapter(searchBbpsOperatorAdapter);

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

    private void setBbpstransactionList()
    {
        BbpsFetchBillAdapter adapter = new BbpsFetchBillAdapter(activity,bbpstransactionlist, new BbpsFetchBillAdapter.OnItemClick() {
            @Override
            public void onClick(int position) {
                //  startActivity(new Intent(activity, WebViewActivity.class));
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
//        switch (id) {
//            case R.id.btnfetchbill:
//
//                  checkValidation();
//                // binding.llfetchbillLayout.setVisibility(View.VISIBLE);
//                // binding.lltransactionLayout.setVisibility(View.GONE);
//                break;
//
//           case R.id.btnpaynow:
//
//                paybill();
//
//                break;
//
//            case R.id.btnpaynow1:
//                checkValidationpay();
//                break;
//            case R.id.etbank:
//                setUpBanklist();
//                break;
//        }

        if (id == R.id.btnfetchbill) {
            checkValidation();
        } else if (id == R.id.btnpaynow) {

            paybill();
        } else if (id == R.id.btnpaynow1) {


            checkValidationpay();
        } else if (id == R.id.etbank) {
            setUpBanklist();

        }
    }

    private void paybill()
    {
        if (NetworkAlertUtility.isInternetConnection2(context)) {
            String accessToken = new StorageUtil(activity).getAccessToken();
            String apikey = new StorageUtil(activity).getApiKey();
            //======================================
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("billerId", String.valueOf(billerid))
                    .addFormDataPart("lat", stringLatitude)
                    .addFormDataPart("log", stringLongitude)
                    .addFormDataPart("param1", String.valueOf(customerid))
                    .addFormDataPart("param2", String.valueOf(ad1value))
                    .addFormDataPart("param3", String.valueOf(ad2value))
                    .addFormDataPart("param4", String.valueOf(ad3value))
                    .addFormDataPart("amount", String.valueOf(fetchamount))
                    .addFormDataPart("skey", String.valueOf(skey))
                    .build();

            //=======================================
            HashMap<String, String> headers = new HashMap<>();
            headers.put("headerToken", accessToken);
            headers.put("headerKey", apikey);
            presenter.getPayBillList(activity, headers, requestBody,true);

        } else {
            NetworkAlertUtility.showNetworkFailureAlert(context);
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

        }else if((binding.tilad1.getVisibility()==View.VISIBLE)&&(TextUtils.isEmpty(ad1value))){
            binding.etad1.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_parm1));

        }else if((binding.tilad2.getVisibility()==View.VISIBLE)&&(TextUtils.isEmpty(ad2value))){
            binding.etad2.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_parm2));

        }else if((binding.tilad3.getVisibility()==View.VISIBLE)&&(TextUtils.isEmpty(ad3value))){
            binding.etad3.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_parm3));

        }else{
            fetchBill();
        }
    }


    private void checkValidationpay()
    {

        customerid = Objects.requireNonNull(binding.etservicenumber.getText()).toString();
        ad1value = Objects.requireNonNull(binding.etad1.getText()).toString();
        ad2value=Objects.requireNonNull(binding.etad2.getText()).toString();
        ad3value=Objects.requireNonNull(binding.etad3.getText()).toString();
        amount=Objects.requireNonNull(binding.etamount.getText()).toString();

        if ((binding.tilservicenumber.getVisibility()==View.VISIBLE)&&(TextUtils.isEmpty(customerid))) {
            binding.etservicenumber.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_mandatory_field));

        }else if((binding.tilad1.getVisibility()==View.VISIBLE)&&(TextUtils.isEmpty(ad1value))){
            binding.etad1.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_parm1));

        }else if((binding.tilad2.getVisibility()==View.VISIBLE)&&(TextUtils.isEmpty(ad2value))){
            binding.etad2.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_parm2));

        }else if((binding.tilad3.getVisibility()==View.VISIBLE)&&(TextUtils.isEmpty(ad3value))){
            binding.etad3.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_parm3));

        }else if((binding.etamount.getVisibility()==View.VISIBLE)&&(TextUtils.isEmpty(amount))){
            binding.etamount.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_amount));

        }else{
           directpaybill();
        }
    }

    private void directpaybill()
    {
        if (NetworkAlertUtility.isInternetConnection2(context)) {
            String accessToken = new StorageUtil(activity).getAccessToken();
            String apikey = new StorageUtil(activity).getApiKey();
            //======================================
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("biller_id", String.valueOf(billerid))
                    .addFormDataPart("param1", String.valueOf(customerid))
                    .addFormDataPart("param2", String.valueOf(ad1value))
                    .addFormDataPart("param3", String.valueOf(ad2value))
                    .addFormDataPart("param4", String.valueOf(ad3value))
                    .addFormDataPart("amount", String.valueOf(amount))
                    .build();
            //=======================================
            HashMap<String, String> headers = new HashMap<>();
            headers.put("headerToken", accessToken);
            headers.put("headerKey", apikey);
            presenter.getPayBillList(activity, headers, requestBody,true);

        } else {
            NetworkAlertUtility.showNetworkFailureAlert(context);
        }
    }

    private void fetchBill()
    {
        if (NetworkAlertUtility.isInternetConnection2(context)) {
            String accessToken = new StorageUtil(activity).getAccessToken();
            String apikey = new StorageUtil(activity).getApiKey();
            //======================================
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("billerId", String.valueOf(billerid))
                    .addFormDataPart("lat", stringLatitude)
                    .addFormDataPart("log", stringLongitude)
                    .addFormDataPart("param1",  String.valueOf(customerid))
                    .addFormDataPart("param2", String.valueOf(ad1value))
                    .addFormDataPart("param3", String.valueOf(ad2value))
                    .addFormDataPart("param4", String.valueOf(ad3value))
                    .build();
            //=======================================
            HashMap<String, String> headers = new HashMap<>();
            headers.put("headerToken", accessToken);
            headers.put("headerKey", apikey);
            presenter.getFetchbilllist(activity, headers, requestBody,true);

        } else {
            NetworkAlertUtility.showNetworkFailureAlert(context);
        }
    }

    private void setFetchbillData()
    {
        binding.billtitle.setText(""+fetchBillModel.getMessage());
        binding.lblName1.setText(fetchBillModel.getParamname());
        binding.lblValue1.setText(fetchBillModel.getParamvalue());

        binding.lblName2.setText(fetchBillModel.getItem1());
        binding.lblValue2.setText(fetchBillModel.getItem1value());

        binding.lblName3.setText(fetchBillModel.getItem2());
        String date = fetchBillModel.getItem2value();
        Log.d("date format",date);
        String updatedate =Utils.getDateFormatExample(date);
        Log.d("updateformat format",updatedate);
        binding.lblValue3.setText(updatedate);

        binding.lblName4.setText(fetchBillModel.getItem3());
        binding.lblValue4.setText(fetchBillModel.getItem3value());
    }

    @Override
    public void onBillerListSuccess(BaseResponse body) {
     if(body!=null){
         if(billerList.size()>0)
             billerList.clear();

         billerList.addAll(body.getData().getBillers());

         new StorageUtil(activity).saveOperatorList((ArrayList<BillerModel>) billerList);

     }
    }

    @Override
    public void onFetchBillSuccess(BaseResponse body)
    {
       if(body!=null){

//            skey=body.getSkey();
//            fetchamount=body.getData().getFetchdata().getItem3value();
//            fetchBillModel=body.getData().getFetchdata();
//            binding.llfetchbillLayout.setVisibility(View.VISIBLE);
//            setFetchbillData();
           if ("SUCCESS".equalsIgnoreCase(body.getStatus())
                   && body.getData() != null
                   && body.getData().getFetchdata() != null) {

               skey = body.getSkey();
               fetchBillModel = body.getData().getFetchdata();
               fetchamount = fetchBillModel.getItem3value();

               binding.llfetchbillLayout.setVisibility(View.VISIBLE);
               setFetchbillData();

           } else {
//               Toast.makeText(this,
//                       body.getMessage() != null ? body.getMessage() : "Failed to fetch bill",
//                       Toast.LENGTH_SHORT).show();
               new CustomToastNotification(this,body.getMessage());
               binding.llfetchbillLayout.setVisibility(View.GONE);
           }
       }
    }



    @Override
    public void onPayBillSuccess(BaseResponse body)
    {
        if(body!=null){
            new CustomToastNotification(activity,body.getMessage());
            if(bbpstransactionlist.size()>0)
                bbpstransactionlist.clear();

            bbpstransactionlist.addAll(body.getData().getBbpsrecent());

           setBbpstransactionList();
            binding.llfetchbillLayout.setVisibility(View.GONE);
           binding.lltransactionLayout.setVisibility(View.VISIBLE);
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
        if (enable)
            Utils.showProgressDialog(context, "");
        else
            Utils.hideProgressDialog();
    }

    @Override
    public void onError(String reason) {
        new CustomToastNotification(context, reason);
    }

    @Override
    public void dialogAccountDeactivate(String reason) {

    }

    @Override
    public void onErrorToast(String reason) {
        new CustomToastNotification(context, reason);
    }


}