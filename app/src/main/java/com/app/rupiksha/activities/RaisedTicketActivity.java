package com.app.rupiksha.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;

import com.app.rupiksha.R;
import com.app.rupiksha.adapters.ServiceTypeAdapter;
import com.app.rupiksha.adapters.TicketSupportAdapter;
import com.app.rupiksha.apipresenter.RaiseComplainedPresenter;
import com.app.rupiksha.databinding.ActivityRaisedTicketBinding;
import com.app.rupiksha.extra.CustomToastNotification;
import com.app.rupiksha.extra.NetworkAlertUtility;
import com.app.rupiksha.interfaces.IRaisedComplainedView;
import com.app.rupiksha.models.BaseResponse;
import com.app.rupiksha.models.SupportTicket;
import com.app.rupiksha.models.SupportType;
import com.app.rupiksha.storage.StorageUtil;
import com.app.rupiksha.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class RaisedTicketActivity extends AppCompatActivity implements View.OnClickListener , IRaisedComplainedView {
    ActivityRaisedTicketBinding binding;
    Activity activity;
    String type,message,txid;
    RaiseComplainedPresenter presenter;
    ServiceTypeAdapter adapter;
    private List<SupportType> banklist=new ArrayList<>();
    private List<SupportTicket> ticketlist=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_raise_complained);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_raised_ticket);
        activity=RaisedTicketActivity.this;
        presenter=new RaiseComplainedPresenter();
        presenter.setView(this);
        binding.includeLayout.toolBar.setTitle(getResources().getString(R.string.label_raised_ticket));
        binding.includeLayout.toolBar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });

        binding.btnsubmit.setOnClickListener(this);
        getBankList(true);
        setUpBanklist();

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
        adapter = new ServiceTypeAdapter(this, R.layout.row_price_selector_item_dropdown, R.id.tvItemName, banklist, Gravity.CENTER);
        binding.etbank.setAdapter(adapter);
        binding.etbank.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                 type = banklist.get(position).getName();
                binding.etbank.setText(type);
                binding.etbank.clearFocus();

            }
        });

    }

    private void setBbpstransactionList()
    {
        TicketSupportAdapter adapter = new TicketSupportAdapter(activity,ticketlist, new TicketSupportAdapter.OnItemClick() {
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
       // type = Objects.requireNonNull(binding.etbank.getText()).toString();
        txid = Objects.requireNonNull(binding.ettxid.getText()).toString();
        message = Objects.requireNonNull(binding.etmessage.getText()).toString();
        if(TextUtils.isEmpty(type))
        {
            binding.etbank.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_type));
        }else if (TextUtils.isEmpty(txid)) {
            binding.ettxid.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_txid));
        }else if (TextUtils.isEmpty(message)) {
            binding.etmessage.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_message));
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
                    .addFormDataPart("message", message)
                    .addFormDataPart("txnid", txid)
                    .addFormDataPart("service", type)
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
    public void onRaisedComplainedSuccess(BaseResponse body)
    {
        if(body!=null){
            new CustomToastNotification(activity,body.getMessage());
            binding.ettxid.setText("");
            binding.etmessage.setText("");
            binding.etbank.setText("");
            getBankList(false);
        }
    }

    @Override
    public void onBankListSuccess(BaseResponse body) {
        if(body!=null){
            if(banklist.size()>0)
                banklist.clear();

            if(ticketlist.size()>0)
                ticketlist.clear();

            banklist.addAll(body.getData().getSupportTypes());
            ticketlist.addAll(body.getData().getSupportTickets());

            if(ticketlist.size()==0)
            {
                binding.lltransactionLayout.setVisibility(View.GONE);
            }else{
                binding.lltransactionLayout.setVisibility(View.VISIBLE);
                setBbpstransactionList();
            }

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