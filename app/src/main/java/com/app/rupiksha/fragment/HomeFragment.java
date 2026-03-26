package com.app.rupiksha.fragment;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.rupiksha.R;
import com.app.rupiksha.activities.APTwoFactorAuthenticationActivity;
import com.app.rupiksha.activities.AadharPayActivity;
import com.app.rupiksha.activities.AddMoneyActivity;
import com.app.rupiksha.activities.AepsCashWithdrowlActivity;
import com.app.rupiksha.activities.AepsKycActivity;
import com.app.rupiksha.activities.AepsKycVerifyActivity;
import com.app.rupiksha.activities.AepsServicesActivity;
import com.app.rupiksha.activities.AllBbpsServiceActivity;
import com.app.rupiksha.activities.BbpsServiceActivity;
import com.app.rupiksha.activities.CMS2ServiceActivity;
import com.app.rupiksha.activities.CMSServiceActivity;
import com.app.rupiksha.activities.DMTActivity;
import com.app.rupiksha.activities.DMTLoginActivity;
import com.app.rupiksha.activities.MatmActivity;
import com.app.rupiksha.activities.MobilePrepaidActivity;
import com.app.rupiksha.activities.OtherServicesActivity;
import com.app.rupiksha.activities.PSAPendingActivity;
import com.app.rupiksha.activities.PSARegistrationActivity;
import com.app.rupiksha.activities.PSAandUTIActivity;
import com.app.rupiksha.activities.PanVerificationActivity;
import com.app.rupiksha.activities.PayoutActivity;
import com.app.rupiksha.activities.PendingKycActivity;
import com.app.rupiksha.activities.QuickTransferActivity;
import com.app.rupiksha.activities.TaxFormActivity;
import com.app.rupiksha.activities.TwoFactorAuthenticationActivity;
import com.app.rupiksha.adapters.AepsServiceAdapter;
import com.app.rupiksha.adapters.BannerAdapter;
import com.app.rupiksha.adapters.BbpsServiceAdapter;
import com.app.rupiksha.adapters.MoneyTranferServiceAdapter;
import com.app.rupiksha.adapters.OtherServiceAdapter;
import com.app.rupiksha.apipresenter.HomeFragmentPresenter;
import com.app.rupiksha.constant.AppConstants;
import com.app.rupiksha.databinding.FragmentHomeBinding;
import com.app.rupiksha.extra.CustomToastNotification;
import com.app.rupiksha.extra.NetworkAlertUtility;
import com.app.rupiksha.interfaces.IHomeFragmentView;
import com.app.rupiksha.models.AepsServiceModel;
import com.app.rupiksha.models.BannerModel;
import com.app.rupiksha.models.BaseResponse;
import com.app.rupiksha.models.BbpsServiceModel;
import com.app.rupiksha.models.ModelUserInfo;
import com.app.rupiksha.models.MoneyTransferServiceModel;
import com.app.rupiksha.models.OtherServiceModel;
import com.app.rupiksha.storage.StorageUtil;
import com.app.rupiksha.utils.StartSnapHelper;
import com.app.rupiksha.utils.Utils;
import com.tapits.ubercms_bc_sdk.LoginScreen;
import com.tapits.ubercms_bc_sdk.utils.Constants;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class HomeFragment extends Fragment  implements IHomeFragmentView {


    public FragmentHomeBinding binding;
    Context  context;
    Activity activity;
    private List<BannerModel> bannerlist=new ArrayList<>();
    private List<AepsServiceModel> aepslist=new ArrayList<>();
    private List<BbpsServiceModel> bbpslist=new ArrayList<>();
    private List<MoneyTransferServiceModel> moneytlist=new ArrayList<>();
    private List<OtherServiceModel> otherlist=new ArrayList<>();
    BannerAdapter bannerAdapter;
    private Handler handler;
    private Runnable Update;
    private Timer swipeTimer;
    private int currentPage = 0;
    private int totalPage = 0;
    LinearLayoutManager linearLayoutManager;
    private static HomeFragment instance;
    HomeFragmentPresenter presenter;
    String title="";
    boolean type=false;
    boolean isFirst =false;

    String device_id;
    private Double stringLatitude = Double.valueOf("0.0");
    private Double stringLongitude = Double.valueOf("0.0");
    private ModelUserInfo userInfo = null;
    private static final int CODE = 1001;

    public HomeFragment() {
        // Required empty public constructor
    }


    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
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
        binding = DataBindingUtil.bind(inflater.inflate(R.layout.fragment_home, container, false));
        context = getActivity();
        activity=getActivity();
        presenter=new HomeFragmentPresenter();
        presenter.setView(this);
        instance=this;
        stringLatitude = Double.valueOf(StorageUtil.getString(context, AppConstants.KEY_LAST_LAT));
        stringLongitude = Double.valueOf(StorageUtil.getString(context, AppConstants.KEY_LAST_LNG));
        device_id = Utils.getAndroidId(context);


        callServiceApi(true);


        binding.swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh() {
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        callServiceApi(false);
                        binding.swipeToRefresh.setRefreshing(false);


                    }
                }, 500);
            }
        });

        binding.viewall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  startActivity(new Intent(context, AllBbpsServiceActivity.class));
                  Utils.animateSlideLeft(context);
            }
        });

        binding.tvAddMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, AddMoneyActivity.class));
            }
        });

        binding.tvRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callWalletBalanceApi();
            }
        });


        return binding.getRoot();
    }

    private void callServiceApi(boolean b)
    {
        if (NetworkAlertUtility.isInternetConnection2(activity))
        {
            binding.swipeToRefresh.setRefreshing(false);
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
            presenter.getService(activity,headers,false);

        } else
        {
            NetworkAlertUtility.showNetworkFailureAlert(activity);
        }
    }

    private void checkKycApi()
    {
        if (NetworkAlertUtility.isInternetConnection2(activity))
        {
            String accessToken = new StorageUtil(activity).getAccessToken();
            String apikey = new StorageUtil(activity).getApiKey();
            //=======================================
            HashMap<String, String> headers = new HashMap<>();
            headers.put("headerToken", accessToken);
            headers.put("headerKey", apikey);
            presenter.getCheckKyc(activity,headers,true);

        } else
        {
            NetworkAlertUtility.showNetworkFailureAlert(activity);
        }
    }

    private void checkPSAApi()
    {
        if (NetworkAlertUtility.isInternetConnection2(activity))
        {
            String accessToken = new StorageUtil(activity).getAccessToken();
            String apikey = new StorageUtil(activity).getApiKey();
            //=======================================
            HashMap<String, String> headers = new HashMap<>();
            headers.put("headerToken", accessToken);
            headers.put("headerKey", apikey);
            presenter.getCheckPSA(activity,headers,true);

        } else
        {
            NetworkAlertUtility.showNetworkFailureAlert(activity);
        }
    }

    private void callWalletBalanceApi()
    {
        if (NetworkAlertUtility.isInternetConnection2(activity))
        {
            String accessToken = new StorageUtil(activity).getAccessToken();
            String apikey = new StorageUtil(activity).getApiKey();
            //=======================================
            HashMap<String, String> headers = new HashMap<>();
            headers.put("headerToken", accessToken);
            headers.put("headerKey", apikey);
            presenter.getwallet(activity,headers,true);

        } else
        {
            NetworkAlertUtility.showNetworkFailureAlert(activity);
        }
    }

    public static HomeFragment getInstance()
    {
        return instance;
    }

    private void  setAepsServiceList()
    {
        AepsServiceAdapter adapter = new AepsServiceAdapter(context,aepslist, new AepsServiceAdapter.OnItemClick() {
            @Override
            public void onClick(int position) {
                if(position==0){
                    title=context.getResources().getString(R.string.text_withdrawal_title);
                    type=true;
                    checkKycApi();
                }else if(position==1){
                    title=context.getResources().getString(R.string.text_aadhar_pay_title);
                    type=true;
                    checkKycApi();
                }else if(position==2){
                    title=context.getResources().getString(R.string.text_balance_enquiry_title);
                    type=false;
                    checkKycApi();
                }else if(position==3){
                    title=context.getResources().getString(R.string.text_mini_statement_title);
                    type=false;
                    checkKycApi();
                }else if(position==4){
                    /*title=context.getResources().getString(R.string.text_mini_statement_title);
                    type=false;
                    checkKycApi();*/
                    new CustomToastNotification(context, "Service Coming Soon");

                }else if(position==5){
                    /*title=context.getResources().getString(R.string.text_mini_statement_title);
                    type=false;
                    checkKycApi();*/
                    new CustomToastNotification(context, "Service Coming Soon");
                }
            }

        });
        int count=4;
        if(count>=4)
        {
            count=4;
        }else{
            count=4;
        }

        binding.aepsrecycler.setLayoutManager(new GridLayoutManager(context,count));
        binding.aepsrecycler.setItemAnimator(new DefaultItemAnimator());
       /* int spanCount = 4; // 3 columns
        int spacing = 10; //
        boolean includeEdge = true;
        binding.aepsrecycler.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));*/
        binding.aepsrecycler.setAdapter(adapter);
    }

    private void setBbpsServiceList()
    {
        BbpsServiceAdapter adapter = new BbpsServiceAdapter(context,bbpslist, new BbpsServiceAdapter.OnItemClick() {
            @Override
            public void onClick(int position) {
               //  startActivity(new Intent(activity, WebViewActivity.class));
                startActivity(new Intent(context, BbpsServiceActivity.class)
                        .putExtra("title",bbpslist.get(position).getName())
                        .putExtra("type",bbpslist.get(position).getType()));
                activity.overridePendingTransition(R.anim.animate_slide_in_left, R.anim.animate_slide_out_right);
              /*  if(position==0) {
                    startActivity(new Intent(context, BbpsServiceActivity.class)
                            .putExtra("title","Electricity")
                            .putExtra("type",false));
                }else if(position==1){
                    startActivity(new Intent(context, BbpsServiceActivity.class)
                            .putExtra("title","Broadband")
                            .putExtra("type",false));

                }else if(position==2){
                    startActivity(new Intent(context, BbpsServiceActivity.class)
                            .putExtra("title","Municipality")
                            .putExtra("type",false));

                }else if(position==3){

                    startActivity(new Intent(context, BbpsServiceActivity.class)
                            .putExtra("title","Education")
                            .putExtra("type",false));

                }else if(position==4){

                    startActivity(new Intent(context, BbpsServiceActivity.class)
                            .putExtra("title","Water")
                            .putExtra("type",false));

                }else if(position==5){
                    startActivity(new Intent(context, BbpsServiceActivity.class)
                            .putExtra("title","Mobile Postpaid")
                            .putExtra("type",false));

                }else if(position==6){
                    startActivity(new Intent(context, BbpsServiceActivity.class)
                            .putExtra("title","Insurance")
                            .putExtra("type",false));

                }else if(position==7){
                    startActivity(new Intent(context, BbpsServiceActivity.class)
                            .putExtra("title","Traffic Chalan")
                            .putExtra("type",false));
                }*/


            }

        });
        int count=8;
        if(count>=4)
        {
            count=4;
        }else{
            count=8;
        }

        binding.bbpsrecycler.setLayoutManager(new GridLayoutManager(context,count));
        binding.bbpsrecycler.setItemAnimator(new DefaultItemAnimator());
      /*  int spanCount = 4; // 3 columns
        int spacing = 10; // 50px
        boolean includeEdge = true;
        binding.bbpsrecycler.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));*/
        binding.bbpsrecycler.setAdapter(adapter);
    }

    private void setMoneyServiceList()
     {
        MoneyTranferServiceAdapter adapter = new MoneyTranferServiceAdapter(context,moneytlist, new MoneyTranferServiceAdapter.OnItemClick() {
            @Override
            public void onClick(int position) {
                //  startActivity(new Intent(activity, WebViewActivity.class));
                if(moneytlist.get(position).getType().equalsIgnoreCase("cms")) {
                    startActivity(new Intent(context, CMSServiceActivity.class)
                            .putExtra("title",moneytlist.get(position).getName())
                            .putExtra("type",moneytlist.get(position).getType()));
                } else if(moneytlist.get(position).getType().equalsIgnoreCase("cms2")) {
                   /* startActivity(new Intent(context, CMS2ServiceActivity.class)
                            .putExtra("title",moneytlist.get(position).getName())
                            .putExtra("type",moneytlist.get(position).getType()));*/

                    CmsService();
                }else if(moneytlist.get(position).getType().equalsIgnoreCase("qt")){

                    startActivity(new Intent(context, QuickTransferActivity.class)
                            .putExtra("title",moneytlist.get(position).getName())
                            .putExtra("type",moneytlist.get(position).getType()));
                }else if(moneytlist.get(position).getType().equalsIgnoreCase("dmt")){
                    String dmtvalue=new StorageUtil(context).getDmtKey();
                    if(dmtvalue.equalsIgnoreCase("")||dmtvalue.equals(null)){
                        startActivity(new Intent(context, DMTLoginActivity.class)
                                .putExtra("title",moneytlist.get(position).getName())
                                .putExtra("type",moneytlist.get(position).getType()));
                    }else{
                        startActivity(new Intent(context, DMTActivity.class)
                                .putExtra("title",moneytlist.get(position).getName())
                                .putExtra("type",moneytlist.get(position).getType()));
                    }


                }else if(moneytlist.get(position).getType().equalsIgnoreCase("payout")){
                    startActivity(new Intent(context, PayoutActivity.class)
                            .putExtra("title",moneytlist.get(position).getName())
                            .putExtra("type",moneytlist.get(position).getType()));

                }else if(moneytlist.get(position).getType().equalsIgnoreCase("uti"))
                {
                   checkPSAApi();

                }else if(moneytlist.get(position).getType().equalsIgnoreCase("matm"))
                {
                    startActivity(new Intent(context, MatmActivity.class)
//                            .putExtra("title",moneytlist.get(position).getName())
//                            .putExtra("type",moneytlist.get(position).getType())
                    );

                }
            }

        });
        int count=moneytlist.size();
        if(count>=4)
        {
            count=4;
        }else{
            count=4;
        }

        binding.moneyrecycler.setLayoutManager(new GridLayoutManager(context,count));
        binding.moneyrecycler.setItemAnimator(new DefaultItemAnimator());
     /*   int spanCount = 4; // 3 columns
        int spacing = 5  ; // 50px
        boolean includeEdge = true;
        binding.moneyrecycler.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));*/
        binding.moneyrecycler.setAdapter(adapter);
    }

    private void setOtherServiceList() {
        {
            OtherServiceAdapter adapter = new OtherServiceAdapter(context,otherlist, new OtherServiceAdapter.OnItemClick() {
                @Override
                public void onClick(int position) {

                    new CustomToastNotification(context, "Service Coming Soon");
                   /* //  startActivity(new Intent(activity, WebViewActivity.class));
                    if(otherlist.get(position).getType().equalsIgnoreCase("pan")){
                        startActivity(new Intent(context, PanVerificationActivity.class)
                                .putExtra("title",otherlist.get(position).getName())
                                .putExtra("type",otherlist.get(position).getType()));
                    }else if(otherlist.get(position).getType().equalsIgnoreCase("FT")) {
                        startActivity(new Intent(context, OtherServicesActivity.class)
                                .putExtra("title", otherlist.get(position).getName())
                                .putExtra("type", otherlist.get(position).getType()));
                    }else if(otherlist.get(position).getType().equalsIgnoreCase("CD")){
                        startActivity(new Intent(context, OtherServicesActivity.class)
                                .putExtra("title", otherlist.get(position).getName())
                                .putExtra("type", otherlist.get(position).getType()));
                    }else if(otherlist.get(position).getType().equalsIgnoreCase("TF")){
                        startActivity(new Intent(context, TaxFormActivity.class)
                                .putExtra("title", otherlist.get(position).getName())
                                .putExtra("type", otherlist.get(position).getType()));
                    }
                    *//*else if(moneytlist.get(position).getType().equalsIgnoreCase("qt")){

                        startActivity(new Intent(context, QuickTransferActivity.class)
                                .putExtra("title",moneytlist.get(position).getName())
                                .putExtra("type",moneytlist.get(position).getType()));
                    }else if(moneytlist.get(position).getType().equalsIgnoreCase("dmt")){
                        String dmtvalue=new StorageUtil(context).getDmtKey();
                        if(dmtvalue.equalsIgnoreCase("")||dmtvalue.equals(null)){
                            startActivity(new Intent(context, DMTLoginActivity.class)
                                    .putExtra("title",moneytlist.get(position).getName())
                                    .putExtra("type",moneytlist.get(position).getType()));
                        }else{
                            startActivity(new Intent(context, DMTActivity.class)
                                    .putExtra("title",moneytlist.get(position).getName())
                                    .putExtra("type",moneytlist.get(position).getType()));
                        }


                    }else if(moneytlist.get(position).getType().equalsIgnoreCase("payout")){
                        startActivity(new Intent(context, PayoutActivity.class)
                                .putExtra("title",moneytlist.get(position).getName())
                                .putExtra("type",moneytlist.get(position).getType()));

                    }else if(moneytlist.get(position).getType().equalsIgnoreCase("uti"))
                    {
                        checkPSAApi();

                    }*/
                }

            });
            int count=otherlist.size();
            if(count>=4)
            {
                count=4;
            }else{
                count=4;
            }

            binding.otherrecycler.setLayoutManager(new GridLayoutManager(context,count));
            binding.otherrecycler.setItemAnimator(new DefaultItemAnimator());
     /*   int spanCount = 4; // 3 columns
        int spacing = 5  ; // 50px
        boolean includeEdge = true;
        binding.moneyrecycler.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));*/
            binding.otherrecycler.setAdapter(adapter);
        }
    }

    private void setBannerdata()
    {
        new StartSnapHelper(context).attachToRecyclerView(binding.recycleviewAnnonce);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        bannerAdapter = new BannerAdapter(context, bannerlist, new BannerAdapter.OnItemClick() {
            @Override
            public void onClick(int position, int proUserId, String link) {

            }
        });
        binding.recycleviewAnnonce.setLayoutManager(linearLayoutManager);
        binding.recycleviewAnnonce.setAdapter(bannerAdapter);
        totalPage = bannerlist.size();
    }



    @Override
    public void onResume() {
        setTimer();
        super.onResume();
    }

    public void setTimer(){
        handler =new Handler();
        swipeTimer = new Timer();

        Update = new Runnable() {
            public void run () {
                if(currentPage < totalPage)
                    currentPage++;
                else
                    currentPage =0;
                binding.recycleviewAnnonce.smoothScrollToPosition(currentPage);
            }
        };

        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run () {
                handler.post(Update);

            }
        },3000,3000);
    }

    @Override
    public void onServiceListSuccess(BaseResponse body)
    {
        binding.sflMyTrips.stopShimmer();
        binding.llSimmer.setVisibility(View.GONE);
        binding.sflMyTrips.setVisibility(View.GONE);
        binding.swipeToRefresh.setVisibility(View.VISIBLE);
       if(body!=null)
       {
           if(bannerlist.size()>0)
               bannerlist.clear();

           if(aepslist.size()>0)
               aepslist.clear();

           if(bbpslist.size()>0)
               bbpslist.clear();

           if(moneytlist.size()>0)
               moneytlist.clear();

           if(otherlist.size()>0)
               otherlist.clear();

           bannerlist.addAll(body.getData().getBanners());
           aepslist.addAll(body.getData().getAeps());
           bbpslist.addAll(body.getData().getBbps());
           moneytlist.addAll(body.getData().getUtility());

           if(body.getData().getOther()!= null && body.getData().getOther().size()>0) {
               otherlist.addAll(body.getData().getOther());

               binding.othermaterialcard.setVisibility(View.VISIBLE);
           }else
               binding.othermaterialcard.setVisibility(View.GONE);


           if(aepslist.size()==0){
               binding.materialcard.setVisibility(View.GONE);
           }else{
               binding.materialcard.setVisibility(View.VISIBLE);
           }

           if(bbpslist.size()==0){
               binding.bbpsmaterialcard.setVisibility(View.GONE);
           }else{
               binding.bbpsmaterialcard.setVisibility(View.VISIBLE);
           }

           if(moneytlist.size()==0){
               binding.moneymaterialcard.setVisibility(View.GONE);
           }else{
               binding.moneymaterialcard.setVisibility(View.VISIBLE);
           }

           String wallet= String.valueOf(body.getWalletBalance());
           if(wallet==null || wallet.equals("")){
               binding.tvBalance.setText(getResources().getString(R.string.rupees) + " " +wallet);
           }else{
//              double famount= body.getWalletBalance();
//               binding.tvBalance.setText(getResources().getString(R.string.rupees) + " " +(new DecimalFormat("##.##").format(famount)));
               if (isAdded() && binding != null) {
                   double famount = body.getWalletBalance();
                   String rupee = getResources().getString(R.string.rupees);
                   String formatted = new DecimalFormat("##.##").format(famount);
                   binding.tvBalance.setText(rupee + " " + formatted);
               } else {
                   Log.w("HomeFragment", "Fragment not attached or binding is null. Skipping UI update.");
               }
           }
           if(body.getNews()==null || body.getNews().equals(""))
           {
               binding.layoutNews.setVisibility(View.GONE);
           }else{
               binding.layoutNews.setVisibility(View.VISIBLE);
               binding.newstext.setText(body.getNews());
               binding.newstext.setSelected(true);
           }
           setBannerdata();
           setAepsServiceList();
           setBbpsServiceList();
           setMoneyServiceList();
           setOtherServiceList();
       }
    }



    @Override
    public void onWalletBalanceSuccess(BaseResponse body)
    {
      if(body!=null){
          String wallet= String.valueOf(body.getWalletBalance());
          if(wallet==null || wallet.equals("")){
              binding.tvBalance.setText(getResources().getString(R.string.rupees) + " " +wallet);
          }else{
              double famount= body.getWalletBalance();
              binding.tvBalance.setText(getResources().getString(R.string.rupees) + " " +(new DecimalFormat("##.##").format(famount)));
          }
      }
    }

    @Override
    public void onCheckKycSuccess(BaseResponse body)
    {
        if(body!=null){
            if(body.getAepsStatus().equalsIgnoreCase("No"))
            {
                startActivity(new Intent(context, AepsKycActivity.class));
            }else if(body.getAepsStatus().equalsIgnoreCase("YES"))
            {

                if(title.equalsIgnoreCase(context.getResources().getString(R.string.text_aadhar_pay_title)))
                {
                    if (body.isAptfa()) {
                        startActivity(new Intent(context, AadharPayActivity.class)
                                .putExtra("title", title)
                                .putExtra("type", type));
                    } else {
                        isFirst = true;
                        startActivity(new Intent(context, APTwoFactorAuthenticationActivity.class)
                                .putExtra("title", title)
                                .putExtra("type", type)
                                .putExtra("isFirst",isFirst));
                    }
                }else if(title.equalsIgnoreCase(context.getResources().getString(R.string.text_withdrawal_title))){
                    if(body.isTfa()){
                       /* startActivity(new Intent(context, AepsCashWithdrowlActivity.class)
                                .putExtra("title", title)
                                .putExtra("type", type));*/
                        startActivity(new Intent(context, AepsServicesActivity.class)
                                .putExtra("title", title)
                                .putExtra("type", type));
                    }else{
                        startActivity(new Intent(context, TwoFactorAuthenticationActivity.class)
                                .putExtra("title", title)
                                .putExtra("type", type));
                    }

                }else{
                    if (body.isTfa()) {
                        startActivity(new Intent(context, AepsServicesActivity.class)
                                .putExtra("title", title)
                                .putExtra("type", type));
                    } else {
                        startActivity(new Intent(context, TwoFactorAuthenticationActivity.class)
                                .putExtra("title", title)
                                .putExtra("type", type));
                    }
                }
            }else if(body.getAepsStatus().equalsIgnoreCase("PENDING"))
            {
              /*  startActivity(new Intent(context, AepsKycActivity.class)
                        .putExtra("title",title)
                        .putExtra("type",type));*/
                   startActivity(new Intent(context, AepsKycVerifyActivity.class)
                        .putExtra("title",title)
                        .putExtra("type",type));
            }else{
                startActivity(new Intent(context, AepsKycActivity.class)
                        .putExtra("title",title)
                        .putExtra("type",type));
              /*  startActivity(new Intent(context, PendingKycActivity.class)
                        .putExtra("title",title)
                        .putExtra("type",type));*/
            }
        }
    }

    private void CmsService() {
        userInfo = new StorageUtil(context).getUserInfo();

        Intent intent = new Intent(context, LoginScreen.class);
        intent.putExtra(Constants.MERCHANT_ID, userInfo.getOutlet());
        intent.putExtra(Constants.SECRET_KEY, "adb9af1d9d8c573d8600d73754c248c9343ce074d68af0722af434bc922061fa");
        // intent.putExtra(Constants.TYPE_REF, Constants.REF_ID);
        intent.putExtra(Constants.TYPE_REF,Constants.BILLERS);
        intent.putExtra(Constants.AMOUNT, "");
        intent.putExtra(Constants.REMARKS, "");
        intent.putExtra(Constants.MOBILE_NUMBER, userInfo.getMobile());
        intent.putExtra(Constants.SUPER_MERCHANTID, "1407");
        intent.putExtra(Constants.IMEI, device_id);
        intent.putExtra(Constants.LATITUDE, stringLatitude);
        intent.putExtra(Constants.LONGITUDE, stringLongitude);

        intent.putExtra(Constants.NAME, userInfo.getName());

        intent.putExtra(Constants.REFERENCE_ID, "");
        startActivityForResult(intent, CODE);

        Log.d("CMS_REQUEST",
                "MERCHANT_ID: " + userInfo.getOutlet() +
                        "\nSECRET_KEY: " + "adb9af1d9d8c573d8600d73754c248c9343ce074d68af0722af434bc922061fa" +
                        "\nTYPE_REF: " + Constants.REF_ID +
                        "\nAMOUNT: " + "" +
                        "\nREMARKS: " + "" +
                        "\nMOBILE: " + userInfo.getMobile() +
                        "\nSUPER_MERCHANTID: " + 1407 +
                        "\nIMEI: " + device_id +
                        "\nLAT: " + stringLatitude +
                        "\nLNG: " + stringLongitude +
                        "\nREFERENCE_ID: " + " "
        );







    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == CODE) {
            String response = data.getStringExtra(Constants.MESSAGE);
            boolean status = data.getBooleanExtra(Constants.TRANS_STATUS, false);
            String txnId = data.getStringExtra(Constants.TXN_ID);
            String timestamp = data.getStringExtra(Constants.TRANS_TIMESTAMP);
            com.tapits.ubercms_bc_sdk.utils.Utils.logD("onactivityresult :" + response + " " + status);
            Toast.makeText(context, "" + response, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCheckPSASuccess(BaseResponse body)
    {
        if(body!=null)
        {
            if(body.getUtiStatus().equalsIgnoreCase("No"))
            {
                startActivity(new Intent(context, PSARegistrationActivity.class) .putExtra("email",body.getData().getUtiSupportData().getSupportmail())
                        .putExtra("phone",body.getData().getUtiSupportData().getSupportphone()));
            }else if(body.getUtiStatus().equalsIgnoreCase("YES"))
            {
                double  amount=body.getCouponCharge();
                startActivity(new Intent(context, PSAandUTIActivity.class).putExtra("amount",amount ));

            }else if(body.getUtiStatus().equalsIgnoreCase("PENDING"))
            {
                startActivity(new Intent(context, PSAPendingActivity.class).putExtra("email",body.getData().getUtiSupportData().getSupportmail())
                        .putExtra("phone",body.getData().getUtiSupportData().getSupportphone()));
            }
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