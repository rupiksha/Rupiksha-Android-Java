package com.app.rupiksha.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.app.rupiksha.R;
import com.app.rupiksha.adapters.AllBbpsServiceAdapter;
import com.app.rupiksha.adapters.GridSpacingItemDecoration;
import com.app.rupiksha.apipresenter.BBpsServicePresenter;
import com.app.rupiksha.databinding.ActivityAllBbpsServiceBinding;
import com.app.rupiksha.extra.CustomToastNotification;
import com.app.rupiksha.extra.NetworkAlertUtility;
import com.app.rupiksha.interfaces.IBBPSserviceView;
import com.app.rupiksha.models.BaseResponse;
import com.app.rupiksha.models.BbpsServiceModel;
import com.app.rupiksha.storage.StorageUtil;
import com.app.rupiksha.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AllBbpsServiceActivity extends AppCompatActivity implements IBBPSserviceView {

    ActivityAllBbpsServiceBinding binding;
    Activity activity;
    Context context;
    private List<BbpsServiceModel> bbpslist=new ArrayList<>();
    BBpsServicePresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_all_bbps_service);
        binding= DataBindingUtil.setContentView(AllBbpsServiceActivity.this,R.layout.activity_all_bbps_service);
        activity=AllBbpsServiceActivity.this;
        context=AllBbpsServiceActivity.this;
        presenter=new BBpsServicePresenter();
        presenter.setView(this);
        binding.includeLayout.toolBar.setTitle(getResources().getString(R.string.label_bbps_service));
        binding.includeLayout.toolBar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });

        callServiceApi();


    }

    private void callServiceApi()
    {
        if (NetworkAlertUtility.isInternetConnection2(activity))
        {
            String accessToken = new StorageUtil(activity).getAccessToken();
            String apikey = new StorageUtil(activity).getApiKey();
            //=======================================
            HashMap<String, String> headers = new HashMap<>();
            headers.put("headerToken", accessToken);
            headers.put("headerKey", apikey);
            presenter.getbbpsService(activity,headers,true);

        } else
        {
            NetworkAlertUtility.showNetworkFailureAlert(activity);
        }
    }

    private void setBbpsServiceList()
    {
        AllBbpsServiceAdapter adapter = new AllBbpsServiceAdapter(activity,bbpslist, new AllBbpsServiceAdapter.OnItemClick() {
            @Override
            public void onClick(int position) {
                startActivity(new Intent(context, BbpsServiceActivity.class)
                        .putExtra("title",bbpslist.get(position).getName())
                        .putExtra("type",bbpslist.get(position).getType()));
            }

        });
        int count=3;
        if(count>=3)
        {
            count=3;
        }else{
            count=3;
        }

        binding.bbpsrecycler.setLayoutManager(new GridLayoutManager(activity,count));
        binding.bbpsrecycler.setItemAnimator(new DefaultItemAnimator());
        int spanCount = 3; // 3 columns
        int spacing = 15; // 50px
        boolean includeEdge = true;
        binding.bbpsrecycler.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        binding.bbpsrecycler.setAdapter(adapter);
    }

    @Override
    public void onServiceListSuccess(BaseResponse body)
    {

        if(body!=null) {

            if (bbpslist.size() > 0)
                bbpslist.clear();

            bbpslist.addAll(body.getData().getBbps());

            if(bbpslist.size()>0){
                binding.bbpsrecycler.setVisibility(View.VISIBLE);
                binding.linearLayout.setVisibility(View.GONE);
            }else{
                binding.bbpsrecycler.setVisibility(View.GONE);
                binding.linearLayout.setVisibility(View.VISIBLE);
            }

            setBbpsServiceList();
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