package com.app.rupiksha.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.rupiksha.R;
import com.app.rupiksha.apipresenter.SupportFragmentPresenter;
import com.app.rupiksha.databinding.FragmentSupportBinding;
import com.app.rupiksha.extra.CustomToastNotification;
import com.app.rupiksha.extra.NetworkAlertUtility;
import com.app.rupiksha.interfaces.ISupportView;
import com.app.rupiksha.models.BaseResponse;
import com.app.rupiksha.models.SupportModel;
import com.app.rupiksha.storage.StorageUtil;
import com.app.rupiksha.utils.Utils;

import java.util.HashMap;

public class SupportFragment extends Fragment implements ISupportView , View.OnClickListener {


    FragmentSupportBinding binding;
    Context context;
    Activity activity;
    SupportModel supportModel;
    SupportFragmentPresenter presenter;

    public SupportFragment() {
        // Required empty public constructor
    }


    public static SupportFragment newInstance() {
        SupportFragment fragment = new SupportFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.bind(inflater.inflate(R.layout.fragment_support, container, false));
        context = getActivity();
        activity = getActivity();
        presenter=new SupportFragmentPresenter();
        presenter.setView(this);
        binding.watsapp.setOnClickListener(this);
        binding.instagram.setOnClickListener(this);
        binding.facebook.setOnClickListener(this);
        binding.twitter.setOnClickListener(this);
        binding.linkedin.setOnClickListener(this);
        userSupport();
        return binding.getRoot();

    }

    private void userSupport()
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
            presenter.getSupportData(activity,headers,false);

        } else {
            NetworkAlertUtility.showNetworkFailureAlert(activity);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        Intent intent;
//        switch (id) {
//            case R.id.watsapp:
//                if (supportModel.getCnumber() != null) {
//                    String url="https://api.whatsapp.com/send?phone="+supportModel.getCnumber();
//                    openMedia(url);
//                }
//                break;
//            case R.id.instagram:
//                if (supportModel.getInstagram() != null) {
//                    openMedia(supportModel.getInstagram());
//                }
//                break;
//            case R.id.facebook:
//                if (supportModel.getFacebook() != null) {
//                    openMedia(supportModel.getFacebook());
//                }
//                break;
//            case R.id.twitter:
//                if (supportModel.getTwitter() != null) {
//                    openMedia(supportModel.getTwitter());
//                }
//                break;
//            case R.id.linkedin:
//                if (supportModel.getLinkedin() != null) {
//                    openMedia(supportModel.getLinkedin());
//                }
//                break;
//        }

        if(id==R.id.watsapp){
            if (supportModel.getCnumber() != null) {
                String url="https://api.whatsapp.com/send?phone="+supportModel.getCnumber();
                openMedia(url);
            } else if (id == R.id.instagram) {
                 if (supportModel.getInstagram() != null) {
                     openMedia(supportModel.getInstagram());
                 }
            } else if (id == R.id.facebook) {
                 if (supportModel.getFacebook() != null) {
                     openMedia(supportModel.getFacebook());
                 }
            } else if (id == R.id.twitter) {
                 if (supportModel.getTwitter() != null) {
                     openMedia(supportModel.getTwitter());
                 }
            } else if (id == R.id.linkedin) {
                 if (supportModel.getLinkedin() != null) {
                     openMedia(supportModel.getLinkedin());
                 }
            }
        }
    }


    @Override
    public void onSupportSuccess(BaseResponse body) {
        binding.sflMyTrips.stopShimmer();
        binding.llSimmer.setVisibility(View.GONE);
        binding.sflMyTrips.setVisibility(View.GONE);
        binding.swipeToRefresh.setVisibility(View.VISIBLE);
        if (body != null) {

            supportModel = body.getData().getSupport();

            setData(supportModel);

        }
    }

    private void setData(SupportModel supportModel) {
        binding.mobile.setText("" + supportModel.getCnumber());
        binding.email.setText("" + supportModel.getCemail());
        binding.address.setText("" + supportModel.getAddress());
    }

    private void openMedia(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
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

