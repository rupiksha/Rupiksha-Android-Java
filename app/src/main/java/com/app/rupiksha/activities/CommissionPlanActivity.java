package com.app.rupiksha.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.app.rupiksha.R;
import com.app.rupiksha.adapters.FAQAdapter;
import com.app.rupiksha.apipresenter.CommissionPlanPresenter;
import com.app.rupiksha.databinding.ActivityCommissionPlanBinding;
import com.app.rupiksha.extra.NetworkAlertUtility;
import com.app.rupiksha.interfaces.ICommissionView;
import com.app.rupiksha.models.BaseResponse;
import com.app.rupiksha.models.FaqModel;
import com.app.rupiksha.storage.StorageUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommissionPlanActivity extends AppCompatActivity implements ICommissionView {

    ActivityCommissionPlanBinding binding;
    Activity activity;
    Context context;
    private List<FaqModel> commisionlist=new ArrayList<>();
    CommissionPlanPresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding= DataBindingUtil.setContentView(CommissionPlanActivity.this,R.layout.activity_commission_plan);
        activity=CommissionPlanActivity.this;
        context=CommissionPlanActivity.this;
        presenter=new CommissionPlanPresenter();
        presenter.setView(this);
        binding.includeLayout.toolBar.setTitle(getResources().getString(R.string.label_commission_plan));
        binding.includeLayout.toolBar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });
        getCommissionApi();
    }

    private void getCommissionApi()
    {

        if (NetworkAlertUtility.isInternetConnection2(context)) {
            String accessToken = new StorageUtil(activity).getAccessToken();
            String apikey = new StorageUtil(activity).getApiKey();

            HashMap<String, String> headers = new HashMap<>();
            headers.put("headerToken", accessToken);
            headers.put("headerKey", apikey);
            presenter.getCommissionPlan(activity, headers,true);

        } else {
            NetworkAlertUtility.showNetworkFailureAlert(context);
        }

    }

    private void setAEPSReportList()
    {
        FAQAdapter adapter = new FAQAdapter(activity,commisionlist, new FAQAdapter.OnItemClick() {
            @Override
            public void onClick(int position)
            {
                binding.bbpsrecycler.getAdapter().notifyDataSetChanged();
            }
        });
        binding.bbpsrecycler.setLayoutManager(new LinearLayoutManager(activity));
        binding.bbpsrecycler.setItemAnimator(new DefaultItemAnimator());
        binding.bbpsrecycler.setAdapter(adapter);
    }

    @Override
    public void onCommissionPlanSuccess(BaseResponse body)
    {
        if(body!=null) {


                if (commisionlist.size() > 0) {
                    commisionlist.clear();
                }
                commisionlist.addAll(body.getData().getCommssionSlab());

                if (commisionlist.size() == 0) {
                    binding.linearLayout.setVisibility(View.VISIBLE);
                    binding.bbpsrecycler.setVisibility(View.GONE);

                } else {
                    binding.linearLayout.setVisibility(View.GONE);
                    binding.bbpsrecycler.setVisibility(View.VISIBLE);
                    setAEPSReportList();
                }
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