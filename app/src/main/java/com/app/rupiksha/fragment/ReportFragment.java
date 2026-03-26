package com.app.rupiksha.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.rupiksha.R;
import com.app.rupiksha.activities.ReportDetailsActivity;
import com.app.rupiksha.adapters.AllReportAdapter;
import com.app.rupiksha.adapters.GridSpacingItemDecoration;
import com.app.rupiksha.apipresenter.ReportFragmentPresenter;
import com.app.rupiksha.databinding.FragmentReportBinding;
import com.app.rupiksha.extra.CustomToastNotification;
import com.app.rupiksha.extra.NetworkAlertUtility;
import com.app.rupiksha.interfaces.IReportFragmentView;
import com.app.rupiksha.models.BaseResponse;
import com.app.rupiksha.models.ReportTypeModel;
import com.app.rupiksha.storage.StorageUtil;
import com.app.rupiksha.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ReportFragment extends Fragment implements IReportFragmentView {

    FragmentReportBinding binding;
    Context context;
    Activity activity;
    private List<ReportTypeModel> reportlist=new ArrayList<>();
    public ReportFragment() {
        // Required empty public constructor
    }

    ReportFragmentPresenter presenter;
    public static ReportFragment newInstance() {
        ReportFragment fragment = new ReportFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.bind(inflater.inflate(R.layout.fragment_report, container, false));
        context = getActivity();
        activity = getActivity();

        presenter=new ReportFragmentPresenter();
        presenter.setView(this);
        callReportApi();

        return binding.getRoot();
    }

    private void callReportApi()
    {
        if (NetworkAlertUtility.isInternetConnection2(activity))
        {
            binding.sflMyTrips.startShimmer();
            binding.llSimmer.setVisibility(View.VISIBLE);
            binding.sflMyTrips.setVisibility(View.VISIBLE);
            binding.swipeToRefresh.setVisibility(View.GONE);

            String accessToken = new StorageUtil(activity).getAccessToken();
            String apikey = new StorageUtil(activity).getApiKey();
            //=======================================
            HashMap<String, String> headers = new HashMap<>();
            headers.put("headerToken", accessToken);
            headers.put("headerKey", apikey);
            presenter.getAllReportType(activity,headers,false);

        } else
        {
            NetworkAlertUtility.showNetworkFailureAlert(activity);
        }
    }


    private void setBbpsServiceList()
    {
        AllReportAdapter adapter = new AllReportAdapter(context,reportlist, new AllReportAdapter.OnItemClick() {
            @Override
            public void onClick(int position) {
                   startActivity(new Intent(context, ReportDetailsActivity.class)
                            .putExtra("title",reportlist.get(position).getName())
                            .putExtra("type",reportlist.get(position).getType()));

                if (context instanceof Activity) {
                    ((Activity) context).overridePendingTransition(R.anim.animate_slide_in_left, R.anim.animate_slide_out_right);
                }
            }

        });
        int count=3;
        if(count>=3)
        {
            count=3;
        }else{
            count=3;
        }

        binding.bbpsrecycler.setLayoutManager(new GridLayoutManager(context,count));
        binding.bbpsrecycler.setItemAnimator(new DefaultItemAnimator());
        int spanCount = 3; // 3 columns
        int spacing = 10; // 50px
        boolean includeEdge = true;
        binding.bbpsrecycler.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        binding.bbpsrecycler.setAdapter(adapter);
    }

    @Override
    public void onReportListSuccess(BaseResponse body) {
        binding.sflMyTrips.stopShimmer();
        binding.llSimmer.setVisibility(View.GONE);
        binding.sflMyTrips.setVisibility(View.GONE);
        binding.swipeToRefresh.setVisibility(View.VISIBLE);
        if(body!=null) {
            if (reportlist.size() > 0)
                reportlist.clear();

            reportlist.addAll(body.getData().getReportItems());
            setBbpsServiceList();
        }
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