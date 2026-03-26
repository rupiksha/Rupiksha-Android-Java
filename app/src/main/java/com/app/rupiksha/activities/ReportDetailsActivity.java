package com.app.rupiksha.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.DatePicker;
import android.widget.Toast;

import com.app.rupiksha.R;
import com.app.rupiksha.adapters.AEPSReportDetailAdapter;
import com.app.rupiksha.adapters.BBPSReportDetailAdapter;
import com.app.rupiksha.adapters.CMSReportDetailAdapter;
import com.app.rupiksha.adapters.DMTReportDetailAdapter;
import com.app.rupiksha.adapters.PayoutReportDetailAdapter;
import com.app.rupiksha.adapters.QTReportDetailAdapter;
import com.app.rupiksha.adapters.RechargeReportDetailAdapter;
import com.app.rupiksha.adapters.UTIReportDetailAdapter;
import com.app.rupiksha.adapters.WalletReportDetailAdapter;
import com.app.rupiksha.adapters.WalletToWalletReportDetailNewAdapter;
import com.app.rupiksha.apipresenter.ReportDetailPresenter;
import com.app.rupiksha.databinding.ActivityReportDetailsBinding;
import com.app.rupiksha.extra.CustomToastNotification;
import com.app.rupiksha.extra.NetworkAlertUtility;
import com.app.rupiksha.interfaces.IReportView;
import com.app.rupiksha.models.BaseResponse;
import com.app.rupiksha.models.AEPSReportDetailModel;
import com.app.rupiksha.models.BbpsReportModel;
import com.app.rupiksha.models.CMSReportModel;
import com.app.rupiksha.models.DmtReportModel;
import com.app.rupiksha.models.PayoutReportModel;
import com.app.rupiksha.models.QtransferReportModel;
import com.app.rupiksha.models.RechargeReportModel;
import com.app.rupiksha.models.UtiReportModel;
import com.app.rupiksha.models.WalletReportModel;
import com.app.rupiksha.models.WalletToWalletReportModel;
import com.app.rupiksha.storage.StorageUtil;
import com.app.rupiksha.utils.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class   ReportDetailsActivity extends AppCompatActivity  implements View.OnClickListener, IReportView {

    ActivityReportDetailsBinding binding;
    Activity activity;
    Context context;
    private List<AEPSReportDetailModel> aepslist=new ArrayList<>();
    private List<DmtReportModel> dmtlist=new ArrayList<>();
    private List<QtransferReportModel> qtlist=new ArrayList<>();
    private List<PayoutReportModel> payoutlist=new ArrayList<>();
    private List<RechargeReportModel> rechargelist=new ArrayList<>();
    private List<BbpsReportModel> bbpslist=new ArrayList<>();
    private List<UtiReportModel> utilist=new ArrayList<>();
    private List<WalletReportModel> walletlist=new ArrayList<>();
    private List<WalletToWalletReportModel> wtowlist=new ArrayList<>();
    private List<AEPSReportDetailModel> allDataList=new ArrayList<>();
    private List<CMSReportModel> cmsDataList=new ArrayList<>();
    private List<CMSReportModel> cmsList=new ArrayList<>();

    String title="";
    String type="";
    String date_from = "", date_to = "";
    String currentDateandTime;
    ReportDetailPresenter presenter;
    int totalPageCount=1;
    int pagecount=0;
    int totalCount=0;
    int pageLimit=10;
    int lastPositionTravel=0;
    boolean isLoading=false;

    AEPSReportDetailAdapter aepsReportDetailAdapter = null;
    QTReportDetailAdapter qtReportDetailAdapter = null;
    DMTReportDetailAdapter dmtReportDetailAdapter = null;
    PayoutReportDetailAdapter payoutReportDetailAdapter  = null;
    RechargeReportDetailAdapter rechargeReportDetailAdapter  = null;
    BBPSReportDetailAdapter bbpsReportDetailAdapter  = null;

    CMSReportDetailAdapter cmssReportDetailAdapter  = null;

    UTIReportDetailAdapter utiReportDetailAdapter  = null;
    WalletReportDetailAdapter walletReportDetailAdapter  = null;
    WalletToWalletReportDetailNewAdapter walletToWalletReportDetailAdapter = null;

    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_report_details);
        binding= DataBindingUtil.setContentView(ReportDetailsActivity.this,R.layout.activity_report_details);
        activity=ReportDetailsActivity.this;
        context=ReportDetailsActivity.this;
        presenter=new ReportDetailPresenter();
        presenter.setView(this);
        binding.includeLayout.toolBar.setTitle(getResources().getString(R.string.label_bbps_report));
        binding.includeLayout.toolBar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });
        if (getIntent().hasExtra("title"))
            title = getIntent().getStringExtra("title");

        if (getIntent().hasExtra("type"))
            type = getIntent().getStringExtra("type");

        binding.includeLayout.toolBar.setTitle(""+title);


        binding.llDateFrom.setOnClickListener(this);
        binding.llDateTo.setOnClickListener(this);

        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        currentDateandTime = dateformat.format(new Date());

        binding.tvDateFrom.setText("Start date \n" + currentDateandTime);
        binding.tvDateTo.setText("End date \n" + currentDateandTime);

        date_from = currentDateandTime;
        date_to = currentDateandTime;

        setAEPSReportList();
        setDMTReportList();
        setQTReportList();
        setPayoutReportList();
        setRechargeReportList();
        setBBPSReportList();
        setUTIReportList();
        setWalletReportList();
        setWallettowalletReportList();
        setCMSReportList();

        linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        binding.bbpsrecycler.setItemAnimator(new DefaultItemAnimator());
        binding.bbpsrecycler.setLayoutManager(linearLayoutManager);

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
    }

    private void getReportApi(boolean isProgress)
    {

        if (NetworkAlertUtility.isInternetConnection2(context)) {
            isLoading =true;
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
            presenter.getReport(activity, headers, requestBody,isProgress);

        } else {
            NetworkAlertUtility.showNetworkFailureAlert(context);
        }

    }

    private void setAEPSReportList()
    {
        aepsReportDetailAdapter = new AEPSReportDetailAdapter(activity,allDataList, new AEPSReportDetailAdapter.OnItemClick() {
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
                        .putExtra("txid",allDataList.get(position).getTxnid())
                        .putExtra("type",type));
            }
        });
        binding.bbpsrecycler.setAdapter(aepsReportDetailAdapter);
        binding.bbpsrecycler.setNestedScrollingEnabled(false);
    }
    private void setDMTReportList()
    {
        dmtReportDetailAdapter = new DMTReportDetailAdapter(activity,allDataList, new DMTReportDetailAdapter.OnItemClick() {
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
                        .putExtra("txid",allDataList.get(position).getTxnid())
                        .putExtra("type",type));
            }

        });
        binding.bbpsrecycler.setAdapter(dmtReportDetailAdapter);
        binding.bbpsrecycler.setNestedScrollingEnabled(false);
    }
    private void setQTReportList()
    {
        qtReportDetailAdapter = new QTReportDetailAdapter(activity,allDataList, new QTReportDetailAdapter.OnItemClick() {
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
                        .putExtra("txid",allDataList.get(position).getTxnid())
                        .putExtra("type",type));
            }
        });
        binding.bbpsrecycler.setAdapter(qtReportDetailAdapter);
        binding.bbpsrecycler.setNestedScrollingEnabled(false);
    }
    private void setPayoutReportList()
    {
        payoutReportDetailAdapter = new PayoutReportDetailAdapter(activity,allDataList, new PayoutReportDetailAdapter.OnItemClick() {
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
                        .putExtra("txid",allDataList.get(position).getTxnid())
                        .putExtra("type",type));
            }
        });
        binding.bbpsrecycler.setAdapter(payoutReportDetailAdapter);
        binding.bbpsrecycler.setNestedScrollingEnabled(false);
    }
    private void setRechargeReportList()
    {
        rechargeReportDetailAdapter = new RechargeReportDetailAdapter(activity,allDataList, new RechargeReportDetailAdapter.OnItemClick() {
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
                        .putExtra("txid",allDataList.get(position).getTxnid())
                        .putExtra("type",type));
            }
        });
        binding.bbpsrecycler.setAdapter(rechargeReportDetailAdapter);
        binding.bbpsrecycler.setNestedScrollingEnabled(false);
    }
    private void setBBPSReportList()
    {
       bbpsReportDetailAdapter = new BBPSReportDetailAdapter(activity,allDataList, new BBPSReportDetailAdapter.OnItemClick() {
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
                        .putExtra("txid",allDataList.get(position).getTxnid())
                        .putExtra("type",type));
            }
        });
        binding.bbpsrecycler.setAdapter(bbpsReportDetailAdapter);
        binding.bbpsrecycler.setNestedScrollingEnabled(false);
    }

    private void setCMSReportList()
    {
        cmssReportDetailAdapter = new CMSReportDetailAdapter(activity,cmsDataList, new CMSReportDetailAdapter.OnItemClick() {
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
                        .putExtra("txid",cmsDataList.get(position).getTxnid())
                        .putExtra("type",type));
            }
        });
        binding.bbpsrecycler.setAdapter(cmssReportDetailAdapter);
        binding.bbpsrecycler.setNestedScrollingEnabled(false);
    }
    private void setUTIReportList()
    {
        utiReportDetailAdapter = new UTIReportDetailAdapter(activity,allDataList, new UTIReportDetailAdapter.OnItemClick() {
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
                        .putExtra("txid",allDataList.get(position).getTxnid())
                        .putExtra("type",type));
            }
        });
        binding.bbpsrecycler.setAdapter(utiReportDetailAdapter);
        binding.bbpsrecycler.setNestedScrollingEnabled(false);
    }
    private void setWalletReportList()
    {
        walletReportDetailAdapter = new WalletReportDetailAdapter(activity,allDataList, new WalletReportDetailAdapter.OnItemClick() {
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
                        .putExtra("txid",allDataList.get(position).getTxnid())
                        .putExtra("type",type));
            }
        });
        binding.bbpsrecycler.setAdapter(walletReportDetailAdapter);
        binding.bbpsrecycler.setNestedScrollingEnabled(false);
    }
    private void setWallettowalletReportList()
    {
        walletToWalletReportDetailAdapter = new WalletToWalletReportDetailNewAdapter(activity,allDataList, new WalletToWalletReportDetailNewAdapter.OnItemClick() {
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
                        .putExtra("txid",allDataList.get(position).getTxnid())
                        .putExtra("type",type));
            }
        });
        binding.bbpsrecycler.setAdapter(walletToWalletReportDetailAdapter);
        binding.bbpsrecycler.setNestedScrollingEnabled(false);
    }
    private void setEndDate()
    {
        if (!binding.tvDateFrom.getText().toString().isEmpty()) {
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {

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
            Toast.makeText(activity, ""+getResources().getString(R.string.title_selectdate), Toast.LENGTH_SHORT).show();
        }
    }
    private void setStartDate()
    {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
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
//
//        }
        if (id == R.id.ll_date_from) {
            setStartDate();
        } else if (id == R.id.ll_date_to) {
            setEndDate();
        }
    }

    @Override
    public void onReportListSuccess(BaseResponse body)
    {
        if(body!=null) {


            if (allDataList.size() > 0 && totalPageCount == 1)
                allDataList.clear();

            switch (type) {
                case "aeps":
                    //setProgressDisplay
                    if(totalPageCount == 1)
                        binding.bbpsrecycler.setAdapter(aepsReportDetailAdapter);

                    if (aepsReportDetailAdapter != null && lastPositionTravel > 0 && allDataList.size() > 0) {
                        allDataList.get(lastPositionTravel).setProgressDelay(false);
                        aepsReportDetailAdapter.notifyItemChanged(lastPositionTravel);
                    }
                    allDataList.addAll(body.getData().getAepsReport());

                    if (allDataList.size() > 0) {
                        binding.linearLayout.setVisibility(View.GONE);
                        binding.bbpsrecycler.setVisibility(View.VISIBLE);

                        totalCount = body.getTotalRecord();

                        Log.e("TotalSize",""+ allDataList.size());
                        Log.e("TotalSizeCount",""+ totalCount);


                        if (aepsReportDetailAdapter != null && totalPageCount == 1)
                            aepsReportDetailAdapter.notifyDataSetChanged();
                        else
                            aepsReportDetailAdapter.notifyItemRangeInserted(lastPositionTravel, body.getData().getAepsReport().size());

                        lastPositionTravel = allDataList.size() - 1;

                        if (totalPageCount == getpageCount(totalCount)) {
                            allDataList.get(lastPositionTravel).setProgressDelay(false);
                            aepsReportDetailAdapter.notifyItemChanged(lastPositionTravel);
                        } else {
                            allDataList.get(lastPositionTravel).setProgressDelay(true);
                            aepsReportDetailAdapter.notifyItemChanged(lastPositionTravel);
                        }

                        isLoading = false;
                    } else {
                        if (aepsReportDetailAdapter != null && totalPageCount == 1)
                            aepsReportDetailAdapter.notifyDataSetChanged();

                        binding.linearLayout.setVisibility(View.VISIBLE);
                        binding.bbpsrecycler.setVisibility(View.GONE);
                        isLoading = false;
                    }

                       /* if (aepslist.size() > 0) {
                            aepslist.clear();
                        }


                        if (aepslist.size() == 0) {
                            binding.linearLayout.setVisibility(View.VISIBLE);
                            binding.bbpsrecycler.setVisibility(View.GONE);

                        } else {
                            if (aepsReportDetailAdapter != null) {
                                binding.bbpsrecycler.setAdapter(aepsReportDetailAdapter);
                                aepsReportDetailAdapter.notifyDataSetChanged();
                            } else
                                setAEPSReportList();
                        }*/
                    break;
                case "money-transfer":

                    if(totalPageCount == 1)
                        binding.bbpsrecycler.setAdapter(dmtReportDetailAdapter);

                    if (dmtReportDetailAdapter != null && lastPositionTravel > 0 && allDataList.size() > 0) {
                        allDataList.get(lastPositionTravel).setProgressDelay(false);
                        dmtReportDetailAdapter.notifyItemChanged(lastPositionTravel);
                    }
                    allDataList.    addAll(body.getData().getDmtReport());

                    if (allDataList.size() > 0) {
                        binding.linearLayout.setVisibility(View.GONE);
                        binding.bbpsrecycler.setVisibility(View.VISIBLE);

                        totalCount = body.getTotalRecord();

                        Log.e("TotalSize",""+ allDataList.size());
                        Log.e("TotalSizeCount",""+ totalCount);


                        if (dmtReportDetailAdapter != null && totalPageCount == 1)
                            dmtReportDetailAdapter.notifyDataSetChanged();
                        else
                            dmtReportDetailAdapter.notifyItemRangeInserted(lastPositionTravel, body.getData().getDmtReport().size());


                        lastPositionTravel = allDataList.size() - 1;

                        if (totalPageCount == getpageCount(totalCount)) {
                            allDataList.get(lastPositionTravel).setProgressDelay(false);
                            dmtReportDetailAdapter.notifyItemChanged(lastPositionTravel);
                        } else {
                            allDataList.get(lastPositionTravel).setProgressDelay(true);
                            dmtReportDetailAdapter.notifyItemChanged(lastPositionTravel);
                        }

                        isLoading = false;
                    } else {
                        if (dmtReportDetailAdapter != null && totalPageCount == 1)
                            dmtReportDetailAdapter.notifyDataSetChanged();

                        binding.linearLayout.setVisibility(View.VISIBLE);
                        binding.bbpsrecycler.setVisibility(View.GONE);
                        isLoading = false;
                    }
                      /*  if (dmtlist.size() > 0) {
                            dmtlist.clear();
                        }
                        dmtlist.addAll(body.getData().getDmtReport());

                        if (dmtlist.size() == 0) {
                            binding.linearLayout.setVisibility(View.VISIBLE);
                            binding.bbpsrecycler.setVisibility(View.GONE);

                        } else {
                            binding.linearLayout.setVisibility(View.GONE);
                            binding.bbpsrecycler.setVisibility(View.VISIBLE);
                            if (dmtReportDetailAdapter != null) {
                                binding.bbpsrecycler.setAdapter(dmtReportDetailAdapter);
                                dmtReportDetailAdapter.notifyDataSetChanged();
                            } else
                                setDMTReportList();
                        }*/
                    break;
                case "qtransfer":

                    if(totalPageCount == 1)
                        binding.bbpsrecycler.setAdapter(qtReportDetailAdapter);


                    if (qtReportDetailAdapter != null && lastPositionTravel > 0 && allDataList.size() > 0) {
                        allDataList.get(lastPositionTravel).setProgressDelay(false);
                        qtReportDetailAdapter.notifyItemChanged(lastPositionTravel);
                    }
                    allDataList.addAll(body.getData().getQtransferReport());

                    if (allDataList.size() > 0) {
                        binding.linearLayout.setVisibility(View.GONE);
                        binding.bbpsrecycler.setVisibility(View.VISIBLE);

                        totalCount = body.getTotalRecord();

                        Log.e("TotalSize",""+ allDataList.size());
                        Log.e("TotalSizeCount",""+ totalCount);


                        if (qtReportDetailAdapter != null && totalPageCount == 1)
                            qtReportDetailAdapter.notifyDataSetChanged();
                        else
                            qtReportDetailAdapter.notifyItemRangeInserted(lastPositionTravel, body.getData().getQtransferReport().size());

                        lastPositionTravel = allDataList.size() - 1;

                        if (totalPageCount == getpageCount(totalCount)) {
                            allDataList.get(lastPositionTravel).setProgressDelay(false);
                            qtReportDetailAdapter.notifyItemChanged(lastPositionTravel);
                        } else {
                            allDataList.get(lastPositionTravel).setProgressDelay(true);
                            qtReportDetailAdapter.notifyItemChanged(lastPositionTravel);
                        }

                        isLoading = false;
                    } else {
                        if (qtReportDetailAdapter != null && totalPageCount == 1)
                            qtReportDetailAdapter.notifyDataSetChanged();

                        binding.linearLayout.setVisibility(View.VISIBLE);
                        binding.bbpsrecycler.setVisibility(View.GONE);
                        isLoading = false;
                    }


                        /*if (qtlist.size() > 0) {
                            qtlist.clear();
                        }
                        qtlist.addAll(body.getData().getQtransferReport());

                        if (qtlist.size() == 0) {
                            binding.linearLayout.setVisibility(View.VISIBLE);
                            binding.bbpsrecycler.setVisibility(View.GONE);

                        } else {
                            binding.linearLayout.setVisibility(View.GONE);
                            binding.bbpsrecycler.setVisibility(View.VISIBLE);
                            if (qtReportDetailAdapter != null) {
                                binding.bbpsrecycler.setAdapter(qtReportDetailAdapter);
                                qtReportDetailAdapter.notifyDataSetChanged();
                            } else
                                setQTReportList();
                        }*/
                    break;

                case "payout":

                    if(totalPageCount == 1)
                        binding.bbpsrecycler.setAdapter(payoutReportDetailAdapter);

                    if (payoutReportDetailAdapter != null && lastPositionTravel > 0 && allDataList.size() > 0) {
                        allDataList.get(lastPositionTravel).setProgressDelay(false);
                        payoutReportDetailAdapter.notifyItemChanged(lastPositionTravel);
                    }
                    allDataList.addAll(body.getData().getPayoutReport());

                    if (allDataList.size() > 0) {
                        binding.linearLayout.setVisibility(View.GONE);
                        binding.bbpsrecycler.setVisibility(View.VISIBLE);

                        totalCount = body.getTotalRecord();

                        Log.e("TotalSize",""+ allDataList.size());
                        Log.e("TotalSizeCount",""+ totalCount);


                        if (payoutReportDetailAdapter != null && totalPageCount == 1)
                            payoutReportDetailAdapter.notifyDataSetChanged();
                        else
                            payoutReportDetailAdapter.notifyItemRangeInserted(lastPositionTravel, body.getData().getPayoutReport().size());


                        lastPositionTravel = allDataList.size() - 1;

                        if (totalPageCount == getpageCount(totalCount)) {
                            allDataList.get(lastPositionTravel).setProgressDelay(false);
                            payoutReportDetailAdapter.notifyItemChanged(lastPositionTravel);
                        } else {
                            allDataList.get(lastPositionTravel).setProgressDelay(true);
                            payoutReportDetailAdapter.notifyItemChanged(lastPositionTravel);
                        }

                        isLoading = false;
                    } else {
                        if (payoutReportDetailAdapter != null && totalPageCount == 1)
                            payoutReportDetailAdapter.notifyDataSetChanged();

                        binding.linearLayout.setVisibility(View.VISIBLE);
                        binding.bbpsrecycler.setVisibility(View.GONE);
                        isLoading = false;
                    }


                       /* if (payoutlist.size() > 0) {
                            payoutlist.clear();
                        }
                        payoutlist.addAll(body.getData().getPayoutReport());

                        if (payoutlist.size() == 0) {
                            binding.linearLayout.setVisibility(View.VISIBLE);
                            binding.bbpsrecycler.setVisibility(View.GONE);

                        } else {
                            binding.linearLayout.setVisibility(View.GONE);
                            binding.bbpsrecycler.setVisibility(View.VISIBLE);
                            if (payoutReportDetailAdapter != null) {
                                binding.bbpsrecycler.setAdapter(payoutReportDetailAdapter);
                                payoutReportDetailAdapter.notifyDataSetChanged();
                            } else
                                setPayoutReportList();
                        }*/
                    break;

                case "recharge":

                    if(totalPageCount == 1)
                        binding.bbpsrecycler.setAdapter(rechargeReportDetailAdapter);

                    if (rechargeReportDetailAdapter != null && lastPositionTravel > 0 && allDataList.size() > 0) {
                        allDataList.get(lastPositionTravel).setProgressDelay(false);
                        rechargeReportDetailAdapter.notifyItemChanged(lastPositionTravel);
                    }
                    allDataList.addAll(body.getData().getRechargeReport());

                    if (allDataList.size() > 0) {
                        binding.linearLayout.setVisibility(View.GONE);
                        binding.bbpsrecycler.setVisibility(View.VISIBLE);

                        totalCount = body.getTotalRecord();

                        Log.e("TotalSize",""+ allDataList.size());
                        Log.e("TotalSizeCount",""+ totalCount);


                        if (rechargeReportDetailAdapter != null && totalPageCount == 1)
                            rechargeReportDetailAdapter.notifyDataSetChanged();
                        else
                            rechargeReportDetailAdapter.notifyItemRangeInserted(lastPositionTravel, body.getData().getRechargeReport().size());


                        lastPositionTravel = allDataList.size() - 1;

                        if (totalPageCount == getpageCount(totalCount)) {
                            allDataList.get(lastPositionTravel).setProgressDelay(false);
                            rechargeReportDetailAdapter.notifyItemChanged(lastPositionTravel);
                        } else {
                            allDataList.get(lastPositionTravel).setProgressDelay(true);
                            rechargeReportDetailAdapter.notifyItemChanged(lastPositionTravel);
                        }

                        isLoading = false;
                    } else {
                        if (rechargeReportDetailAdapter != null && totalPageCount == 1)
                            rechargeReportDetailAdapter.notifyDataSetChanged();

                        binding.linearLayout.setVisibility(View.VISIBLE);
                        binding.bbpsrecycler.setVisibility(View.GONE);
                        isLoading = false;
                    }


                      /*
                        if (rechargelist.size() > 0) {
                            rechargelist.clear();
                        }
                        rechargelist.addAll(body.getData().getRechargeReport());

                        if (rechargelist.size() == 0) {
                            binding.linearLayout.setVisibility(View.VISIBLE);
                            binding.bbpsrecycler.setVisibility(View.GONE);

                        } else {
                            binding.linearLayout.setVisibility(View.GONE);
                            binding.bbpsrecycler.setVisibility(View.VISIBLE);
                            if (rechargeReportDetailAdapter != null) {
                                binding.bbpsrecycler.setAdapter(rechargeReportDetailAdapter);
                                rechargeReportDetailAdapter.notifyDataSetChanged();
                            } else
                                setRechargeReportList();
                        }*/
                    break;

                case "bbps":

                    if(totalPageCount == 1)
                        binding.bbpsrecycler.setAdapter(bbpsReportDetailAdapter);

                    if (bbpsReportDetailAdapter != null && lastPositionTravel > 0 && allDataList.size() > 0) {
                        allDataList.get(lastPositionTravel).setProgressDelay(false);
                        bbpsReportDetailAdapter.notifyItemChanged(lastPositionTravel);
                    }
                    allDataList.addAll(body.getData().getBbpsReport());

                    if (allDataList.size() > 0) {
                        binding.linearLayout.setVisibility(View.GONE);
                        binding.bbpsrecycler.setVisibility(View.VISIBLE);

                        totalCount = body.getTotalRecord();

                        if (bbpsReportDetailAdapter != null && totalPageCount == 1)
                            bbpsReportDetailAdapter.notifyDataSetChanged();
                        else
                            bbpsReportDetailAdapter.notifyItemRangeInserted(lastPositionTravel, body.getData().getBbpsReport().size());


                        lastPositionTravel = allDataList.size() - 1;

                        if (totalPageCount == getpageCount(totalCount)) {
                            allDataList.get(lastPositionTravel).setProgressDelay(false);
                            bbpsReportDetailAdapter.notifyItemChanged(lastPositionTravel);
                        } else {
                            allDataList.get(lastPositionTravel).setProgressDelay(true);
                            bbpsReportDetailAdapter.notifyItemChanged(lastPositionTravel);
                        }

                        isLoading = false;
                    } else {
                        if (bbpsReportDetailAdapter != null && totalPageCount == 1)
                            bbpsReportDetailAdapter.notifyDataSetChanged();

                        binding.linearLayout.setVisibility(View.VISIBLE);
                        binding.bbpsrecycler.setVisibility(View.GONE);
                        isLoading = false;
                    }



                       /* if (bbpslist.size() > 0) {
                            bbpslist.clear();
                        }
                        bbpslist.addAll(body.getData().getBbpsReport());

                        if (bbpslist.size() == 0) {
                            binding.linearLayout.setVisibility(View.VISIBLE);
                            binding.bbpsrecycler.setVisibility(View.GONE);

                        } else {
                            binding.linearLayout.setVisibility(View.GONE);
                            binding.bbpsrecycler.setVisibility(View.VISIBLE);
                            if (bbpsReportDetailAdapter != null) {
                                binding.bbpsrecycler.setAdapter(bbpsReportDetailAdapter);
                                bbpsReportDetailAdapter.notifyDataSetChanged();
                            } else
                                setBBPSReportList();
                        }*/
                    break;

                case "uti-coupon":

                    if(totalPageCount == 1)
                        binding.bbpsrecycler.setAdapter(utiReportDetailAdapter);

                    if (utiReportDetailAdapter != null && lastPositionTravel > 0 && allDataList.size() > 0) {
                        allDataList.get(lastPositionTravel).setProgressDelay(false);
                        utiReportDetailAdapter.notifyItemChanged(lastPositionTravel);
                    }
                    allDataList.addAll(body.getData().getUtiReport());

                    if (allDataList.size() > 0) {
                        binding.linearLayout.setVisibility(View.GONE);
                        binding.bbpsrecycler.setVisibility(View.VISIBLE);

                        totalCount = body.getTotalRecord();

                        if (utiReportDetailAdapter != null && totalPageCount == 1)
                            utiReportDetailAdapter.notifyDataSetChanged();
                        else
                            utiReportDetailAdapter.notifyItemRangeInserted(lastPositionTravel, body.getData().getUtiReport().size());


                        lastPositionTravel = allDataList.size() - 1;

                        if (totalPageCount == getpageCount(totalCount)) {
                            allDataList.get(lastPositionTravel).setProgressDelay(false);
                            utiReportDetailAdapter.notifyItemChanged(lastPositionTravel);
                        } else {
                            allDataList.get(lastPositionTravel).setProgressDelay(true);
                            utiReportDetailAdapter.notifyItemChanged(lastPositionTravel);
                        }

                        isLoading = false;
                    } else {
                        if (utiReportDetailAdapter != null && totalPageCount == 1)
                            utiReportDetailAdapter.notifyDataSetChanged();

                        binding.linearLayout.setVisibility(View.VISIBLE);
                        binding.bbpsrecycler.setVisibility(View.GONE);
                        isLoading = false;
                    }

                        /*if (utilist.size() > 0) {
                            utilist.clear();
                        }
                        utilist.addAll(body.getData().getUtiReport());

                        if (utilist.size() == 0) {
                            binding.linearLayout.setVisibility(View.VISIBLE);
                            binding.bbpsrecycler.setVisibility(View.GONE);

                        } else {
                            binding.linearLayout.setVisibility(View.GONE);
                            binding.bbpsrecycler.setVisibility(View.VISIBLE);
                            if (utiReportDetailAdapter != null) {
                                binding.bbpsrecycleutiReportDetailAdapterr.setAdapter(utiReportDetailAdapter);
                                .notifyDataSetChanged();
                            } else
                                setUTIReportList();
                        }*/
                    break;

                case "wallet":
                    if(totalPageCount == 1)
                        binding.bbpsrecycler.setAdapter(walletReportDetailAdapter);

                    if (walletReportDetailAdapter != null && lastPositionTravel > 0 && allDataList.size() > 0) {
                        allDataList.get(lastPositionTravel).setProgressDelay(false);
                        walletReportDetailAdapter.notifyItemChanged(lastPositionTravel);
                    }
                    allDataList.addAll(body.getData().getWalletReport());

                    if (allDataList.size() > 0) {
                        binding.linearLayout.setVisibility(View.GONE);
                        binding.bbpsrecycler.setVisibility(View.VISIBLE);

                        totalCount = body.getTotalRecord();

                        if (walletReportDetailAdapter != null && totalPageCount == 1)
                            walletReportDetailAdapter.notifyDataSetChanged();
                        else
                            walletReportDetailAdapter.notifyItemRangeInserted(lastPositionTravel, body.getData().getWalletReport().size());


                        lastPositionTravel = allDataList.size() - 1;

                        if (totalPageCount == getpageCount(totalCount)) {
                            allDataList.get(lastPositionTravel).setProgressDelay(false);
                            walletReportDetailAdapter.notifyItemChanged(lastPositionTravel);
                        } else {
                            allDataList.get(lastPositionTravel).setProgressDelay(true);
                            walletReportDetailAdapter.notifyItemChanged(lastPositionTravel);
                        }

                        isLoading = false;
                    } else {
                        if (walletReportDetailAdapter != null && totalPageCount == 1)
                            walletReportDetailAdapter.notifyDataSetChanged();

                        binding.linearLayout.setVisibility(View.VISIBLE);
                        binding.bbpsrecycler.setVisibility(View.GONE);
                        isLoading = false;
                    }
                       /* if (walletlist.size() > 0) {
                            walletlist.clear();
                        }
                        walletlist.addAll(body.getData().getWalletReport());

                        if (walletlist.size() == 0) {
                            binding.linearLayout.setVisibility(View.VISIBLE);
                            binding.bbpsrecycler.setVisibility(View.GONE);

                        } else {
                            binding.linearLayout.setVisibility(View.GONE);
                            binding.bbpsrecycler.setVisibility(View.VISIBLE);
                            if (walletReportDetailAdapter != null) {
                                binding.bbpsrecycler.setAdapter(walletReportDetailAdapter);
                                walletReportDetailAdapter.notifyDataSetChanged();
                            } else
                                setWalletReportList();
                        }*/
                    break;

                case "wallet-to-wallet":
                    if(totalPageCount == 1)
                        binding.bbpsrecycler.setAdapter(walletToWalletReportDetailAdapter);

                    if (walletToWalletReportDetailAdapter != null && lastPositionTravel > 0 && allDataList.size() > 0) {
                        allDataList.get(lastPositionTravel).setProgressDelay(false);
                        walletToWalletReportDetailAdapter.notifyItemChanged(lastPositionTravel);
                    }
                    allDataList.addAll(body.getData().getWalletToWalletReport());

                    if (allDataList.size() > 0) {
                        binding.linearLayout.setVisibility(View.GONE);
                        binding.bbpsrecycler.setVisibility(View.VISIBLE);

                        totalCount = body.getTotalRecord();

                        Log.e("TotalSize",""+ allDataList.size());
                        Log.e("TotalSizeCount",""+ totalCount);


                        if (walletToWalletReportDetailAdapter != null && totalPageCount == 1)
                            walletToWalletReportDetailAdapter.notifyDataSetChanged();
                        else
                            walletToWalletReportDetailAdapter.notifyItemRangeInserted(lastPositionTravel, body.getData().getWalletToWalletReport().size());


                        lastPositionTravel = allDataList.size() - 1;

                        if (totalPageCount == getpageCount(totalCount)) {
                            allDataList.get(lastPositionTravel).setProgressDelay(false);
                            walletToWalletReportDetailAdapter.notifyItemChanged(lastPositionTravel);
                        } else {
                            allDataList.get(lastPositionTravel).setProgressDelay(true);
                            walletToWalletReportDetailAdapter.notifyItemChanged(lastPositionTravel);
                        }

                        isLoading = false;
                    } else {
                        if (walletToWalletReportDetailAdapter != null && totalPageCount == 1)
                            walletToWalletReportDetailAdapter.notifyDataSetChanged();

                        binding.linearLayout.setVisibility(View.VISIBLE);
                        binding.bbpsrecycler.setVisibility(View.GONE);
                        isLoading = false;
                    }


                       /* if (wtowlist.size() > 0) {
                            wtowlist.clear();
                        }
                        wtowlist.addAll(body.getData().getWalletToWalletReport());

                        if (wtowlist.size() == 0) {
                            binding.linearLayout.setVisibility(View.VISIBLE);
                            binding.bbpsrecycler.setVisibility(View.GONE);

                        } else {
                            binding.linearLayout.setVisibility(View.GONE);
                            binding.bbpsrecycler.setVisibility(View.VISIBLE);
                            if (walletToWalletReportDetailAdapter != null) {
                                binding.bbpsrecycler.setAdapter(walletToWalletReportDetailAdapter);
                                walletToWalletReportDetailAdapter.notifyDataSetChanged();
                            } else
                                setWallettowalletReportList();
                        }*/
                    break;

                case "cms":

                    if(totalPageCount == 1)
                        binding.bbpsrecycler.setAdapter(cmssReportDetailAdapter);

                    if (cmssReportDetailAdapter != null && lastPositionTravel > 0 && cmsDataList.size() > 0) {
                        cmsDataList.get(lastPositionTravel).setProgressDelay(false);
                        cmssReportDetailAdapter.notifyItemChanged(lastPositionTravel);
                    }
                    cmsDataList.addAll(body.getData().getCmsReport());

                    if (cmsDataList.size() > 0) {
                        binding.linearLayout.setVisibility(View.GONE);
                        binding.bbpsrecycler.setVisibility(View.VISIBLE);

                        totalCount = body.getTotalRecord();

                        if (cmssReportDetailAdapter != null && totalPageCount == 1)
                            cmssReportDetailAdapter.notifyDataSetChanged();
                        else
                            cmssReportDetailAdapter.notifyItemRangeInserted(lastPositionTravel, body.getData().getCmsReport().size());


                        lastPositionTravel = cmsDataList.size() - 1;

                        if (totalPageCount == getpageCount(totalCount)) {
                            cmsDataList.get(lastPositionTravel).setProgressDelay(false);
                            cmssReportDetailAdapter.notifyItemChanged(lastPositionTravel);
                        } else {
                            cmsDataList.get(lastPositionTravel).setProgressDelay(true);
                            cmssReportDetailAdapter.notifyItemChanged(lastPositionTravel);
                        }

                        isLoading = false;
                    } else {
                        if (cmssReportDetailAdapter != null && totalPageCount == 1)
                            cmssReportDetailAdapter.notifyDataSetChanged();

                        binding.linearLayout.setVisibility(View.VISIBLE);
                        binding.bbpsrecycler.setVisibility(View.GONE);
                        isLoading = false;
                    }
                    break;


            }

                /*  if(type.equalsIgnoreCase("aeps"))
                    {
                        if(aepslist.size()>0){
                            aepslist.clear();
                        }
                        aepslist.addAll(body.getData().getAepsReport());

                        if(aepslist.size()==0){
                            binding.linearLayout.setVisibility(View.VISIBLE);
                            binding.bbpsrecycler.setVisibility(View.GONE);

                        }else{
                            binding.linearLayout.setVisibility(View.GONE);
                            binding.bbpsrecycler.setVisibility(View.VISIBLE);
                            setAEPSReportList();
                        }


                    }else if(type.equalsIgnoreCase("money-transfer"))
                    {
                        if(dmtlist.size()>0){
                            dmtlist.clear();
                        }
                        dmtlist.addAll(body.getData().getDmtReport());

                        if(dmtlist.size()==0){
                            binding.linearLayout.setVisibility(View.VISIBLE);
                            binding.bbpsrecycler.setVisibility(View.GONE);

                        }else{
                            binding.linearLayout.setVisibility(View.GONE);
                            binding.bbpsrecycler.setVisibility(View.VISIBLE);
                            setDMTReportList();
                        }
                    }else if(type.equalsIgnoreCase("qtransfer"))
                    {
                        if(qtlist.size()>0){
                            qtlist.clear();
                        }
                        qtlist.addAll(body.getData().getQtransferReport());

                        if(qtlist.size()==0){
                            binding.linearLayout.setVisibility(View.VISIBLE);
                            binding.bbpsrecycler.setVisibility(View.GONE);

                        }else{
                            binding.linearLayout.setVisibility(View.GONE);
                            binding.bbpsrecycler.setVisibility(View.VISIBLE);
                            setQTReportList();
                        }
                    }else if(type.equalsIgnoreCase("payout"))
                    {
                        if(payoutlist.size()>0){
                            payoutlist.clear();
                        }
                        payoutlist.addAll(body.getData().getPayoutReport());

                        if(payoutlist.size()==0){
                            binding.linearLayout.setVisibility(View.VISIBLE);
                            binding.bbpsrecycler.setVisibility(View.GONE);

                        }else{
                            binding.linearLayout.setVisibility(View.GONE);
                            binding.bbpsrecycler.setVisibility(View.VISIBLE);
                            setPayoutReportList();
                        }
                    }else if(type.equalsIgnoreCase("recharge"))
                    {
                        if(rechargelist.size()>0){
                            rechargelist.clear();
                        }
                        rechargelist.addAll(body.getData().getRechargeReport());

                        if(rechargelist.size()==0){
                            binding.linearLayout.setVisibility(View.VISIBLE);
                            binding.bbpsrecycler.setVisibility(View.GONE);

                        }else{
                            binding.linearLayout.setVisibility(View.GONE);
                            binding.bbpsrecycler.setVisibility(View.VISIBLE);
                            setRechargeReportList();
                        }
                    }else if(type.equalsIgnoreCase("bbps"))
                    {
                        if(bbpslist.size()>0){
                            bbpslist.clear();
                        }
                        bbpslist.addAll(body.getData().getBbpsReport());

                        if(bbpslist.size()==0){
                            binding.linearLayout.setVisibility(View.VISIBLE);
                            binding.bbpsrecycler.setVisibility(View.GONE);

                        }else{
                            binding.linearLayout.setVisibility(View.GONE);
                            binding.bbpsrecycler.setVisibility(View.VISIBLE);
                            setBBPSReportList();
                        }
                    }else if(type.equalsIgnoreCase("uti-coupon"))
                    {
                        if(utilist.size()>0){
                            utilist.clear();
                        }
                        utilist.addAll(body.getData().getUtiReport());

                        if(utilist.size()==0){
                            binding.linearLayout.setVisibility(View.VISIBLE);
                            binding.bbpsrecycler.setVisibility(View.GONE);

                        }else{
                            binding.linearLayout.setVisibility(View.GONE);
                            binding.bbpsrecycler.setVisibility(View.VISIBLE);
                            setUTIReportList();
                        }
                    }else if(type.equalsIgnoreCase("wallet"))
                    {
                        if(walletlist.size()>0){
                            walletlist.clear();
                        }
                        walletlist.addAll(body.getData().getWalletReport());

                        if(walletlist.size()==0){
                            binding.linearLayout.setVisibility(View.VISIBLE);
                            binding.bbpsrecycler.setVisibility(View.GONE);

                        }else{
                            binding.linearLayout.setVisibility(View.GONE);
                            binding.bbpsrecycler.setVisibility(View.VISIBLE);
                            setWalletReportList();
                        }
                    }else if(type.equalsIgnoreCase("wallet-to-wallet"))
                    {
                        if(wtowlist.size()>0){
                            wtowlist.clear();
                        }
                        wtowlist.addAll(body.getData().getWalletToWalletReport());

                        if(wtowlist.size()==0){
                            binding.linearLayout.setVisibility(View.VISIBLE);
                            binding.bbpsrecycler.setVisibility(View.GONE);

                        }else{
                            binding.linearLayout.setVisibility(View.GONE);
                            binding.bbpsrecycler.setVisibility(View.VISIBLE);
                            setWallettowalletReportList();
                        }
                    }*/


        }

    }

    public  void setPagination(){

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

    public int getpageCount(int totalCount)
    {
        pagecount = totalCount / pageLimit;
        int modeofcount = totalCount % pageLimit;

        if (modeofcount > 0)
            pagecount += 1;

        Log.e("pagecoount", "" + pagecount);
        return pagecount;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.animate_slide_in_left, R.anim.animate_slide_out_right);
    }
}