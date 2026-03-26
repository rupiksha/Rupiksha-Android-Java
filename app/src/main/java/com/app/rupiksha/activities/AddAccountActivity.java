package com.app.rupiksha.activities;

import static com.app.rupiksha.utils.Utils.parseDate;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.app.rupiksha.R;
import com.app.rupiksha.adapters.BankAdapter;
import com.app.rupiksha.adapters.SearchBankAdapter;
import com.app.rupiksha.apipresenter.AddAccountPresenter;
import com.app.rupiksha.databinding.ActivityAddAccountBinding;
import com.app.rupiksha.databinding.OperatorsearchbottomsheetBinding;
import com.app.rupiksha.extra.CustomToastNotification;
import com.app.rupiksha.extra.NetworkAlertUtility;
import com.app.rupiksha.interfaces.IAddAccountView;
import com.app.rupiksha.models.BankModel;
import com.app.rupiksha.models.BaseResponse;
import com.app.rupiksha.storage.StorageUtil;
import com.app.rupiksha.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class AddAccountActivity extends AppCompatActivity implements View.OnClickListener, IAddAccountView {

    ActivityAddAccountBinding binding;
    Activity activity;
    Dialog operatorBottomSheet;
    LinearLayoutManager linearLayoutManager;
    OperatorsearchbottomsheetBinding myCustomDialog;
    SearchBankAdapter searchBankAdapter;

    private List<BankModel> banklist=new ArrayList<>();
    private List<BankModel> storecategories = new ArrayList<>();
    private List<BankModel> search = new ArrayList<>();
    BankAdapter bankAdapter;
    ActivityResultLauncher<Intent> launch;
    AddAccountPresenter presenter;
    int bankid=0;
    boolean flag=false;
    String accountnumber,ifsccode,name,mobile,dob,dmtkey;
    File panImageFile;
    MultipartBody.Part uploadedPan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_add_benificiary);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_add_account);
        activity=AddAccountActivity.this;
        presenter=new AddAccountPresenter();
        presenter.setView(this);
        if (getIntent().hasExtra("flag"))
            flag = getIntent().getBooleanExtra("flag",false);

        binding.includeLayout.toolBar.setTitle(getResources().getString(R.string.label_add_account));
        binding.includeLayout.toolBar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });

        dmtkey=new StorageUtil(activity).getDmtKey();

        binding.submit.setOnClickListener(this);
        binding.etdob.setOnClickListener(this);
        binding.etbank.setOnClickListener(this);
        getBankList();
//        setUpBanklist();

    }

    private void getBankList()
    {
        if (NetworkAlertUtility.isInternetConnection2(activity))
        {
            String accessToken = new StorageUtil(activity).getAccessToken();
            String apikey = new StorageUtil(activity).getApiKey();

            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("demo", "")
                    .build();
            //=======================================
            HashMap<String, String> headers = new HashMap<>();
            headers.put("headerToken", accessToken);
            headers.put("headerKey", apikey);
            presenter.getBankList(activity,headers,true);

        } else
        {
            NetworkAlertUtility.showNetworkFailureAlert(activity);
        }
    }

    private void setUpBanklist()
    {
        operatorBottomSheet = new Dialog(activity, R.style.MyDialogTheme);
        myCustomDialog = DataBindingUtil.inflate(getLayoutInflater(), R.layout.operatorsearchbottomsheet, null, false);
        operatorBottomSheet.setContentView(myCustomDialog.getRoot());
        operatorBottomSheet.setCancelable(true);
        operatorBottomSheet.setCanceledOnTouchOutside(true);
        operatorBottomSheet.show();

        storecategories = new StorageUtil(activity).getBankList(activity);
        banklist.addAll(storecategories);
        myCustomDialog.etname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int i1, int i2) {
                if (s.length() == 0) {
                    // binding.cancel.setVisibility(View.GONE);
                    if (banklist.size() > 0) {
                        banklist.clear();
                    }
                    banklist.addAll(storecategories);
                    searchBankAdapter.notifyDataSetChanged();
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
                    if (banklist.size() > 0)
                        banklist.clear();

                    banklist.addAll(search);
                    searchBankAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        searchBankAdapter = new SearchBankAdapter(activity, this.banklist, new SearchBankAdapter.OnItemClick() {
            @Override
            public void onClick(int position) {
                String name = banklist.get(position).getName();
                bankid=banklist.get(position).getId();

                binding.etbank.setText(name);
                binding.etbank.clearFocus();
                if(operatorBottomSheet!=null){
                    operatorBottomSheet.dismiss();
                }

            }
        });
        myCustomDialog.opteratorrecycler.setLayoutManager(linearLayoutManager);
        myCustomDialog.opteratorrecycler.setAdapter(searchBankAdapter);

       /* binding.etbank.setThreshold(100);
        bankAdapter = new BankAdapter(this, R.layout.row_price_selector_item_dropdown, R.id.tvItemName, banklist, Gravity.CENTER);
        binding.etbank.setAdapter(bankAdapter);
        binding.etbank.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String name = banklist.get(position).getName();
                bankid=banklist.get(position).getId();

                binding.etbank.setText(name);
                binding.etbank.clearFocus();

            }
        });*/

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("flag", flag);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.submit)
        {
            checkvalidation();
        } else if (id == R.id.etdob) {
            showDatePickerDOBDialog();
        } else if (id == R.id.etbank) {
            setUpBanklist();
        }
    }

    private void checkvalidation()
    {
        accountnumber = Objects.requireNonNull(binding.etaccountnumber.getText()).toString();
        ifsccode = Objects.requireNonNull(binding.etifsc.getText()).toString();
        name = Objects.requireNonNull(binding.etname.getText()).toString();
        mobile = Objects.requireNonNull(binding.etpincode.getText()).toString();

        if(TextUtils.isEmpty(accountnumber)) {
            binding.etaccountnumber.requestFocus();
            new CustomToastNotification(activity, getResources().getString(R.string.please_enter_account_number));
        }else if(TextUtils.isEmpty(ifsccode)) {
            binding.etifsc.requestFocus();
            new CustomToastNotification(activity, getResources().getString(R.string.please_enter_ifsc_code));
        }else if (TextUtils.isEmpty(name)) {
            binding.etname.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_name));
        }else if (bankid==0) {
            binding.etbank.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_bank));
        }else if (TextUtils.isEmpty(mobile)){
            binding.etpincode.requestFocus();
            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_mobile_number));
        }
//        else if (TextUtils.isEmpty(dob)) {
//            binding.etdob.requestFocus();
//            new CustomToastNotification(activity,getResources().getString(R.string.please_enter_dob));
//        }
        else{
           addBenificary();
        }
    }


    private void showDatePickerDOBDialog()
    {
        Locale.setDefault(Locale.getDefault());
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        c.add(Calendar.YEAR, -18);
        DatePickerDialog datePickerDialog = new DatePickerDialog(activity, R.style.MyDialogTheme, (view, year, monthOfYear, dayOfMonth) -> {
            String d = dayOfMonth < 10 ? "0" + dayOfMonth : "" + dayOfMonth;
            String m = (monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : "" + (monthOfYear + 1);
            dob = String.valueOf(year).concat("-").concat(m).concat("-").concat(d);
            binding.etdob.setText(parseDate(d + "-" + m + "-" + year));
        }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
        datePickerDialog.show();
    }
    private void addBenificary()
    {
        if (NetworkAlertUtility.isInternetConnection2(activity))
        {
            String accessToken = new StorageUtil(activity).getAccessToken();
            String apikey = new StorageUtil(activity).getApiKey();

            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("name", name)
                    .addFormDataPart("account", accountnumber)
                    .addFormDataPart("ifsc", ifsccode)
                    .addFormDataPart("bid", String.valueOf(bankid))
//                    .addFormDataPart("dob", dob)
                    .addFormDataPart("mobile", mobile)
                    .addFormDataPart("dmtKey", dmtkey)
                    .build();

            HashMap<String, String> headers = new HashMap<>();
            headers.put("headerToken", accessToken);
            headers.put("headerKey", apikey);
            presenter.addAccount(activity,headers,requestBody,true);

        } else
        {
            NetworkAlertUtility.showNetworkFailureAlert(activity);
        }
    }


    @Override
    public void onBankListSuccess(BaseResponse body) {
        if(body!=null){
            if(banklist.size()>0)
                banklist.clear();

            banklist.addAll(body.getData().getDmtBankList());
            new StorageUtil(activity).saveBankList((ArrayList<BankModel>) banklist);
        }
    }

    @Override
    public void onAddBankSuccess(BaseResponse body)
    {
        if(body!=null){
            new CustomToastNotification(activity,body.getMessage());
            onBackPressed();
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