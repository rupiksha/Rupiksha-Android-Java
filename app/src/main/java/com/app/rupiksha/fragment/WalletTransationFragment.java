package com.app.rupiksha.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.app.rupiksha.R;
import com.app.rupiksha.activities.RaiseComplainedActivity;
import com.app.rupiksha.adapters.WalletToWalletRecentTxnAdapter;
import com.app.rupiksha.adapters.WalletToWalletReportDetailAdapter;
import com.app.rupiksha.apipresenter.WalletFragmentPresenter;
import com.app.rupiksha.databinding.FragmentWalletTransationBinding;
import com.app.rupiksha.databinding.WalletreciptbsheetBinding;
import com.app.rupiksha.extra.CustomToastNotification;
import com.app.rupiksha.extra.NetworkAlertUtility;
import com.app.rupiksha.interfaces.IWalletFragmentView;
import com.app.rupiksha.models.AEPSReportDetailModel;
import com.app.rupiksha.models.BaseResponse;
import com.app.rupiksha.models.WalletToWalletData;
import com.app.rupiksha.models.WalletToWalletReportModel;
import com.app.rupiksha.storage.StorageUtil;
import com.app.rupiksha.utils.Utils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class WalletTransationFragment extends Fragment implements View.OnClickListener , IWalletFragmentView {

    FragmentWalletTransationBinding binding;
    Context context;
    Activity activity;
    private List<AEPSReportDetailModel> wtowlist=new ArrayList<>();
    private List<WalletToWalletReportModel> recenttxnlist=new ArrayList<>();
    String title="";
    String type="wallet-to-wallet";
    String date_from = "", date_to = "";
    String currentDateandTime;
    WalletreciptbsheetBinding myBottomBinding;
    BottomSheetDialog userBottomSheet;
    WalletFragmentPresenter presenter;
    WalletToWalletReportDetailAdapter walletToWalletReportDetailAdapter;
    String mobile="",amount="";
    WalletToWalletData walletToWalletData;
    int totalPageCount=1;
    int pagecount=0;
    int totalCount=0;
    int pageLimit=10;
    int lastPositionTravel=0;
    boolean isLoading=false;
    PdfDocument document;
    Uri uri;
    public WalletTransationFragment() {
        // Required empty public constructor
    }


    public static WalletTransationFragment newInstance() {
        WalletTransationFragment fragment = new WalletTransationFragment();
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
        binding = DataBindingUtil.bind(inflater.inflate(R.layout.fragment_wallet_transation, container, false));
        context = getActivity();
        activity = getActivity();

        presenter=new WalletFragmentPresenter();
        presenter.setView(this);

        binding.llDateFrom.setOnClickListener(this);
        binding.llDateTo.setOnClickListener(this);
        binding.btnpaynow.setOnClickListener(this);

        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        currentDateandTime = dateformat.format(new Date());

        binding.tvDateFrom.setText("Start date \n" + currentDateandTime);
        binding.tvDateTo.setText("End date \n" + currentDateandTime);


        date_from = currentDateandTime;
        date_to = currentDateandTime;

        binding.svLogin.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener()
        {
            @Override
            public void onScrollChanged()
            {
                View view = (View)binding.svLogin.getChildAt(binding.svLogin.getChildCount() - 1);
                int diff = (view.getBottom() - (binding.svLogin.getHeight() + binding.svLogin.getScrollY()));
                if (diff == 0)
                {
                    if (!isLoading)
                    {
                        if (totalPageCount < getpageCount(totalCount) && !isLoading ) {
                            totalPageCount += 1;
                            getReportApi(false);
                        }
                    }
                }
            }
        });

        getReportApi(true);
        setWallettowalletReportList();

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
                    getFetchWalletUserApi();
                }else{
                    binding.etname.setText("");
                }
            }
        });

        binding.toggle.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
//                switch (checkedId){
//                    case R.id.radiowallethistory:
//                        binding.llwallettowallet.setVisibility(View.GONE);
//                        binding.llwhistory.setVisibility(View.VISIBLE);
//                        getReportApi(true);
//                        break;
//                    case R.id.radiowallettowallet:
//                        binding.llwallettowallet.setVisibility(View.VISIBLE);
//                        binding.llwhistory.setVisibility(View.GONE);
//                        setRecentTransactionList();
//                        break;
//                }
                if(checkedId==R.id.radiowallethistory){
                    binding.llwallettowallet.setVisibility(View.GONE);
                    binding.llwhistory.setVisibility(View.VISIBLE);
                    getReportApi(true);
                } else if (checkedId == R.id.radiowallettowallet) {
                    binding.llwallettowallet.setVisibility(View.VISIBLE);
                    binding.llwhistory.setVisibility(View.GONE);
                    binding.linearLayout.setVisibility(View.GONE);
                    setRecentTransactionList();

                }
            }
        });


        return binding.getRoot();
    }

    private void setEndDate()
    {
        if (!binding.tvDateFrom.getText().toString().isEmpty()) {
            // Get Current Date
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int years, int monthOfYear, int dayOfMonth) {


                    int day = view.getDayOfMonth();
                    int month = view.getMonth();
                    int year = view.getYear();

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, month, day);

                    DateFormat outputFormatter = new SimpleDateFormat("yyyy-MM-dd");
                    DateFormat outputFormatterprint = new SimpleDateFormat("dd-MM-yyyy");
                    date_to = (outputFormatter.format(calendar.getTime()));
                    binding.tvDateTo.setText("End date \n" + outputFormatterprint.format(calendar.getTime()));
                    getReportApi(true);

                }
            }, mYear, mMonth, mDay);
            datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
            datePickerDialog.show();
        }else{
            Toast.makeText(getActivity(), ""+getResources().getString(R.string.title_selectdate), Toast.LENGTH_SHORT).show();
        }
    }

    private void setStartDate()
    {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int years, int monthOfYear, int dayOfMonth) {

                int day = view.getDayOfMonth();
                int month = view.getMonth();
                int year = view.getYear();

                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);

                DateFormat outputFormatter = new SimpleDateFormat("yyyy-MM-dd");
                DateFormat outputFormatterprint = new SimpleDateFormat("dd-MM-yyyy");
                date_from = (outputFormatter.format(calendar.getTime()));
                binding.tvDateFrom.setText("Start date \n" + outputFormatterprint.format(calendar.getTime()));
                getReportApi(true);
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
        datePickerDialog.show();
    }

    private void getFetchWalletUserApi()
    {

        if (NetworkAlertUtility.isInternetConnection2(context)) {
            String accessToken = new StorageUtil(activity).getAccessToken();
            String apikey = new StorageUtil(activity).getApiKey();
            //======================================
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("phone", String.valueOf(mobile))

                    .build();
            //=======================================
            HashMap<String, String> headers = new HashMap<>();
            headers.put("headerToken", accessToken);
            headers.put("headerKey", apikey);
            presenter.getFetchUser(activity, headers, requestBody,true);

        } else {
            NetworkAlertUtility.showNetworkFailureAlert(context);
        }

    }

    private void getReportApi(boolean isProgress)
    {

        if (NetworkAlertUtility.isInternetConnection2(context)) {
            isLoading = true;
            if(isProgress) {
                binding.sflMyTrips.startShimmer();
                binding.llSimmer.setVisibility(View.VISIBLE);
                binding.sflMyTrips.setVisibility(View.VISIBLE);
                binding.swipeToRefresh.setVisibility(View.GONE);
            }

            String accessToken = new StorageUtil(activity).getAccessToken();
            String apikey = new StorageUtil(activity).getApiKey();
            //======================================
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("from", String.valueOf(date_from))
                    .addFormDataPart("to", String.valueOf(date_to))
                    .addFormDataPart("type", String.valueOf(type))
                    .addFormDataPart("page_number", String.valueOf(totalPageCount))
                    .addFormDataPart("limit", String.valueOf(pageLimit))
                    .build();
            //=======================================
            HashMap<String, String> headers = new HashMap<>();
            headers.put("headerToken", accessToken);
            headers.put("headerKey", apikey);
            presenter.getReport(activity, headers, requestBody,false);

        } else {
            NetworkAlertUtility.showNetworkFailureAlert(context);
        }

    }

    private void doInitiateTransaction()
    {

        if (NetworkAlertUtility.isInternetConnection2(context)) {
            String accessToken = new StorageUtil(activity).getAccessToken();
            String apikey = new StorageUtil(activity).getApiKey();
            //======================================
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("phone", String.valueOf(mobile))
                    .addFormDataPart("amount", String.valueOf(amount))
                    .build();
            //=======================================
            HashMap<String, String> headers = new HashMap<>();
            headers.put("headerToken", accessToken);
            headers.put("headerKey", apikey);
            presenter.getdotransaction(activity, headers, requestBody,true);

        } else {
            NetworkAlertUtility.showNetworkFailureAlert(context);
        }

    }


    private void setWallettowalletReportList()
    {
        walletToWalletReportDetailAdapter = new WalletToWalletReportDetailAdapter(activity,wtowlist, new WalletToWalletReportDetailAdapter.OnItemClick() {
            @Override
            public void onClick(int position)
            {
                binding.bbpsrecycler.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onShareReciept(ConstraintLayout recptlayout, int position) {
              Utils.sharepdf(activity,recptlayout);
            }

            @Override
            public void onDownloadReciept(ConstraintLayout recptlayout, int position) {
                startActivity(new Intent(context, RaiseComplainedActivity.class)
                        .putExtra("txid",wtowlist.get(position).getTxnid())
                        .putExtra("type",type));
            }
        });
        binding.bbpsrecycler.setLayoutManager(new LinearLayoutManager(activity));
        binding.bbpsrecycler.setItemAnimator(new DefaultItemAnimator());
        binding.bbpsrecycler.setAdapter(walletToWalletReportDetailAdapter);
        binding.bbpsrecycler.setNestedScrollingEnabled(false);
    }



    private void setRecentTransactionList()
    {
        WalletToWalletRecentTxnAdapter adapter = new WalletToWalletRecentTxnAdapter(context,recenttxnlist, new WalletToWalletRecentTxnAdapter.OnItemClick() {
            @Override
            public void onClick(int position)
            {
                binding.recentwallet.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onShareReciept(ConstraintLayout recptlayout, int position) {
                Utils.sharepdf(activity,recptlayout);
            }

            @Override
            public void onDownloadReciept(ConstraintLayout recptlayout, int position) {
                startActivity(new Intent(context, RaiseComplainedActivity.class)
                        .putExtra("txid",recenttxnlist.get(position).getTxnid())
                        .putExtra("type",type));
            }


        });
        binding.recentwallet.setLayoutManager(new LinearLayoutManager(context));
        binding.recentwallet.setItemAnimator(new DefaultItemAnimator());
        binding.recentwallet.setAdapter(adapter);
        binding.recentwallet.setNestedScrollingEnabled(false);
    }


    @Override
    public void onClick(View v)
    {
        int id = v.getId();
//        switch (id) {
//            case R.id.ll_date_from:
//                setStartDate();
//                break;
//
//            case R.id.ll_date_to:
//                setEndDate();
//                break;
//            case R.id.btnpaynow:
//
//                checkValidation();
//              //  setbootomsheet();
//                break;
//
//        }
        if(id==R.id.ll_date_from){
            setStartDate();
        }else if(id==R.id.ll_date_to){
            setEndDate();
        }else if(id==R.id.btnpaynow){
            checkValidation();
        }
    }

    private void checkValidation()
    {

        mobile = Objects.requireNonNull(binding.etmobilenumber.getText()).toString();
        amount = Objects.requireNonNull(binding.etamount.getText()).toString();
        if(TextUtils.isEmpty(mobile)){
            binding.etmobilenumber.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_mobile_number));
        }else if (TextUtils.isEmpty(amount)) {
            binding.etamount.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_amount));
        }else{
            doInitiateTransaction();
        }
    }

    private void setbootomsheet()
    {
        userBottomSheet = new BottomSheetDialog(getActivity(), R.style.CustomBottomSheetDialogThemeWithDim);
        myBottomBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.walletreciptbsheet, null, false);
        userBottomSheet.setContentView(myBottomBinding.getRoot());
        userBottomSheet.setCancelable(true);
        userBottomSheet.setCanceledOnTouchOutside(false);
        userBottomSheet.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);

        myBottomBinding.txnId.setText(""+walletToWalletData.getTxnid());
        myBottomBinding.date.setText(walletToWalletData.getDate());
        myBottomBinding.sndmob.setText(walletToWalletData.getSenderMobile());
        myBottomBinding.type.setText(walletToWalletData.getStatus());

        myBottomBinding.txSndname.setText(walletToWalletData.getSenderName());


        if(walletToWalletData.getStatus().equalsIgnoreCase("SUCCESS")){
            myBottomBinding.type.setTextColor(context.getResources().getColor(R.color.color_green));

        }/*else if(list.get(position).getStatus().equalsIgnoreCase("ERROR")){
             holder.binding.status.setTextColor(context.getResources().getColor(R.color.color_red));
        }else if(list.get(position).getStatus().equalsIgnoreCase("PENDING")){
          holder.binding.status.setTextColor(context.getResources().getColor(R.color.color_yellow));
       }*/else{
            myBottomBinding.type.setTextColor(context.getResources().getColor(R.color.color_red));
        }
        String wallet= String.valueOf(walletToWalletData.getAmount());
        myBottomBinding.amount.setText(context.getResources().getString(R.string.rupees) + " " +wallet);
        myBottomBinding.rcname.setText(walletToWalletData.getReceiverName());
        myBottomBinding.rcmob.setText(walletToWalletData.getReceiverMobile());

        myBottomBinding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userBottomSheet.dismiss();
            }
        });

        userBottomSheet.show();
    }

    @Override
    public void onReportListSuccess(BaseResponse body)
    {
        binding.sflMyTrips.stopShimmer();
        binding.llSimmer.setVisibility(View.GONE);
        binding.sflMyTrips.setVisibility(View.GONE);
        binding.swipeToRefresh.setVisibility(View.VISIBLE);

        if(body!=null){

            if(wtowlist.size()>0 && totalPageCount == 1){
                wtowlist.clear();
            }

            if (walletToWalletReportDetailAdapter != null && lastPositionTravel > 0 && wtowlist.size() > 0) {
                wtowlist.get(lastPositionTravel).setProgressDelay(false);
                walletToWalletReportDetailAdapter.notifyItemChanged(lastPositionTravel);
            }
            wtowlist.addAll(body.getData().getWalletToWalletReport());
            if (wtowlist.size() > 0)
            {
                binding.linearLayout.setVisibility(View.GONE);
                binding.bbpsrecycler.setVisibility(View.VISIBLE);

                totalCount = body.getTotalRecord();

                Log.e("TotalSize",""+ wtowlist.size());
                Log.e("TotalSizeCount",""+ totalCount);


                if (walletToWalletReportDetailAdapter != null && totalPageCount == 1)
                    walletToWalletReportDetailAdapter.notifyDataSetChanged();
                else
                    walletToWalletReportDetailAdapter.notifyItemRangeInserted(lastPositionTravel, body.getData().getWalletToWalletReport().size());

                lastPositionTravel = wtowlist.size() - 1;

                if (totalPageCount == getpageCount(totalCount)) {
                    wtowlist.get(lastPositionTravel).setProgressDelay(false);
                    walletToWalletReportDetailAdapter.notifyItemChanged(lastPositionTravel);
                } else {
                    wtowlist.get(lastPositionTravel).setProgressDelay(true);
                    walletToWalletReportDetailAdapter.notifyItemChanged(lastPositionTravel);
                }

                isLoading = false;
            }
            else
            {
                if (walletToWalletReportDetailAdapter != null && totalPageCount == 1)
                    walletToWalletReportDetailAdapter.notifyDataSetChanged();

                binding.linearLayout.setVisibility(View.VISIBLE);
                binding.bbpsrecycler.setVisibility(View.GONE);
                isLoading = false;
            }
        }
    }

    @Override
    public void onFetchUserSuccess(BaseResponse body)
    {
         if(body!=null){
            binding.etname.setText(""+body.getData().getWalletFetchUser().getName());
         }
    }

    @Override
    public void onDoTransactionSuccess(BaseResponse body)
    {
          if(body!=null) {
                walletToWalletData=body.getData().getWalletToWalletData();

                if(walletToWalletData!=null){
                    setbootomsheet();
                }

              if(recenttxnlist.size()>0){
                  recenttxnlist.clear();
              }
              recenttxnlist.addAll(body.getData().getWalletToWalletRecentTxn());

              if(recenttxnlist.size()==0){
                  binding.transactiontitle.setVisibility(View.GONE);
                  binding.recentwallet.setVisibility(View.GONE);

              }else{
                  binding.transactiontitle.setVisibility(View.VISIBLE);
                  binding.recentwallet.setVisibility(View.VISIBLE);
                  setRecentTransactionList();
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

    public int getpageCount(int totalCount)
    {
        pagecount = totalCount / pageLimit;
        int modeofcount = totalCount % pageLimit;

        if (modeofcount > 0)
            pagecount += 1;

        Log.e("pagecoount", "" + pagecount);
        return pagecount;
    }
}