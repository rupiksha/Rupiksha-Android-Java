package com.app.rupiksha.activities;


import static com.app.rupiksha.utils.Utils.parseDate;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.rupiksha.R;
import com.app.rupiksha.adapters.BankDetailAdapter;
import com.app.rupiksha.adapters.ITRAdapter;
import com.app.rupiksha.adapters.OtherExpensesAdapter;
import com.app.rupiksha.adapters.OtherIncomeAdapter;
import com.app.rupiksha.apipresenter.ITRPresenter;
import com.app.rupiksha.databinding.ActivityTaxFormBinding;
import com.app.rupiksha.extra.CustomToastNotification;
import com.app.rupiksha.extra.NetworkAlertUtility;
import com.app.rupiksha.interfaces.CallBack;
import com.app.rupiksha.interfaces.IITRFormView;
import com.app.rupiksha.interfaces.ItemClickListener;
import com.app.rupiksha.models.AccountTypeModel;
import com.app.rupiksha.models.BankDetailModel;
import com.app.rupiksha.models.BaseResponse;
import com.app.rupiksha.models.ModelUserInfo;
import com.app.rupiksha.models.TaxFormDetailModel;
import com.app.rupiksha.otpview.OtpView;
import com.app.rupiksha.storage.StorageUtil;
import com.app.rupiksha.utils.FileUtil;
import com.app.rupiksha.utils.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class TaxFormActivity extends BaseActivity implements View.OnClickListener, IITRFormView {

    private ActivityTaxFormBinding binding;
    private TaxFormActivity mActivity;
    private ArrayList<TaxFormDetailModel> investmentDetailList = new ArrayList<>();
    private ArrayList<TaxFormDetailModel> otherDetailList = new ArrayList<>();
    private ArrayList<TaxFormDetailModel> otherExpensesList = new ArrayList<>();
    private ArrayList<AccountTypeModel> accountTypeList = new ArrayList<>();
    private ArrayList<BankDetailModel> bankDetailArraylist = new ArrayList<>();

    private LinearLayoutManager linearLayoutManager;
    private ITRAdapter itrAdapter;
    private OtherIncomeAdapter otherIncomeAdapter;
    private OtherExpensesAdapter otherExpensesAdapter;
    private BankDetailAdapter bankDetailAdapter;
    private Uri imageUri = null;
    private ITRPresenter itrPresenter;
    private String fileType = "Image";

    private Uri imgFrontAdhar= null;
    private Uri imgBackAdhar= null;
    private Uri imgUploadPan= null;
    private Uri imgUploadForm16= null;
    private Uri imgUploadBankStatement= null;
    private Uri imgUploadMiscellaneousItem= null;
    private Uri imgUploadSelfDeclarationClient= null;
    private Uri imgChoseSchool= null;
    private Uri imgChoseLic= null;
    private Uri imgChoseMFound= null;
    private Uri imgChoseSukanya= null;
    private Uri imgHousingLone= null;
    private Uri imgHealth= null;
    private Uri imgSaleDeed= null;
    private Uri imgPurchaseDeed= null;
    private Uri imgShareDeed= null;

    private Uri otherImageUri = null;

    private String imgFrontAdharName= "";
    private String imgBackAdharrName= "";
    private String imgUploadPanrName= "";
    private String imgUploadForm16rName= "";
    private String imgUploadBankStatementrName= "";
    private String imgUploadMiscellaneousItemrName= "";
    private String imgChoseSchoolrName= "";
    private String imgChoseLicrName= "";
    private String imgChoseMFoundrName= "";
    private String imgChoseSukanyarName= "";
    private String imgHousingLonerName= "";
    private String imgHealthrName= "";
    private String imgSelfDeclarationClientName= "";
    private String imgSaleDeedName= null;
    private String imgPurchaseDeedName= null;
    private String imgShareDeedName = null;


    private String imgChoseSchoolrAmount= "";
    private String imgChoseLicrAmount= "";
    private String imgChoseMFoundrNameAmount= "";
    private String imgChoseSukanyarNameAmount= "";
    private String imgHousingLonerNameAmount= "";
    private String imgHealthrNameAmount= "";


    private String incomeTaxUserName= "";
    private String incomeTaxPassword= "";
    private String phoneNumber= "";
    private String whatsappPhoneNumber= "";
    private String emailId= "";
    private String firmName= "";


    private String aadharNumber= "";
    private String aadharAddress= "";
    private String pancardNumber= "";
    private String pancardName= "";
    private String pancardFatherName= "";
    private String pancardDob= "";
    private String bankAccountNumber= "";
    private String bankIfscCode= "";
    private String dob= "";
    private ModelUserInfo settingData = null;

    private String houseNumber= "";
    private String roadStreetName= "";
    private String localAreaName= "";
    private String cityStateName= "";
    private String areaPinCode= "";
    private String bankArrayListData= "";
    private String returnType= "";
    private String gender= "";
    private String rentIncome= "";
    private String intrestFromFDR= "";
    private String sBAccountIncome= "";
    private String otherIncome= "";


    private String businessFirmName= "";
    private String gstNo= "";
    private String cashSale= "";
    private String bankSale= "";
    private String debtors= "";
    private String creditors= "";
    private String cash= "";
    private String stock= "";
    private String isSaleLand = "1";
    private String isSaleShare= "0";


    private int itemPosition = 0;
    private String selectType = "";
    int ServiceId = 0;
    boolean isChecked = false;
    boolean isVerified = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_tax_form);
        blockScreenShot();
       // changeStatusBarColor(getResources().getColor(R.color.app_color,null));
     //   changeNavigationBarColor(getResources().getColor(R.color.white,null));
        setLightStatusBar();
        mActivity = TaxFormActivity.this;

        binding.includeLayout.toolBar.setTitle(getResources().getString(R.string.txt_form_rtr));
        binding.includeLayout.toolBar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });

        itrPresenter = new ITRPresenter();
        itrPresenter.setView(this);

        if(getIntent().hasExtra("ServiceId"))
            ServiceId = getIntent().getIntExtra("ServiceId",0);


        settingData = new StorageUtil(mActivity).getUserInfo();

        binding.imgFrontAdhar.setOnClickListener(this);
        binding.imgBackAdhar.setOnClickListener(this);

        binding.imgUploadPan.setOnClickListener(this);
        binding.imgUploadForm16.setOnClickListener(this);
       // binding.imgUploadBankStatement.setOnClickListener(this);
        binding.imgUploadMiscellaneousItem.setOnClickListener(this);

        binding.btnChoseSchool.setOnClickListener(this);
        binding.btnChoseLic.setOnClickListener(this);
        binding.btnChoseMFound.setOnClickListener(this);
        binding.btnChoseSukanya.setOnClickListener(this);
        binding.btnHousingLone.setOnClickListener(this);
        binding.btnHealth.setOnClickListener(this);
        binding.btnAddMore.setOnClickListener(this);
        binding.btnAddMoreBAnk.setOnClickListener(this);
        binding.btnRegisterITR.setOnClickListener(this);
        binding.btnNewUser.setOnClickListener(this);
        binding.btnFPassword.setOnClickListener(this);
        binding.btnSignup.setOnClickListener(this);
        binding.btnDownloadForm.setOnClickListener(this);
        binding.etPanCArdDob.setOnClickListener(this);
        binding.btnRegisterNow.setOnClickListener(this);
        binding.btnResetPassword.setOnClickListener(this);
        binding.imgUploadSaleDeed.setOnClickListener(this);
        binding.imgUploadPurchaseDeed.setOnClickListener(this);
        binding.imgUploadSaleShareFile.setOnClickListener(this);
        binding.llPropertySale.setOnClickListener(this);
        binding.llShareSale.setOnClickListener(this);
        binding.btnAddMoreOtherIncome.setOnClickListener(this);
        binding.btnAddMoreExpenses.setOnClickListener(this);
        binding.llMale.setOnClickListener(this);
        binding.llFemale.setOnClickListener(this);


        binding.etPanCardNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                    binding.etUserName.setText(s.toString());
            }
        });

        binding.tabLayout2.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        returnType = "Business";
                        binding.llForBusinessDetail.setVisibility(View.VISIBLE);
                        binding.txtForBusiness.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        returnType = "Salary";
                        binding.llForBusinessDetail.setVisibility(View.GONE);
                        binding.txtForBusiness.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        accountTypeList.add(new AccountTypeModel(1,getResources().getString(R.string.txt_saving_account)));
        accountTypeList.add(new AccountTypeModel(1,getResources().getString(R.string.txt_current_account)));

        addOtherTransaction();
        addOtherIncome();
        addOtherExpenses();
        addBAnkDetail();
    }

    private void setItrAdapter()
    {
        linearLayoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        binding.recycleviewInvestmetDetail.setLayoutManager(linearLayoutManager);
        binding.recycleviewInvestmetDetail.getItemAnimator().setChangeDuration(0);
        itrAdapter = new ITRAdapter(mActivity, investmentDetailList, itemClickListener);
        binding.recycleviewInvestmetDetail.setAdapter(itrAdapter);
        binding.recycleviewInvestmetDetail.setNestedScrollingEnabled(false);
        binding.recycleviewInvestmetDetail.setHasFixedSize(true);

    }

    private void setOtherIncomeAdapter()
    {
        linearLayoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        binding.rvAddOtherIncome.setLayoutManager(linearLayoutManager);
        binding.rvAddOtherIncome.getItemAnimator().setChangeDuration(0);
        otherIncomeAdapter = new OtherIncomeAdapter(mActivity, otherDetailList, itemClickListener);
        binding.rvAddOtherIncome.setAdapter(otherIncomeAdapter);
        binding.rvAddOtherIncome.setNestedScrollingEnabled(false);
        binding.rvAddOtherIncome.setHasFixedSize(true);

    }

    private void setOtherExpensesAdapter()
    {
        linearLayoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        binding.rvAddOtherExpenses.setLayoutManager(linearLayoutManager);
        binding.rvAddOtherExpenses.getItemAnimator().setChangeDuration(0);
        otherExpensesAdapter = new OtherExpensesAdapter(mActivity, otherExpensesList, itemClickListener);
        binding.rvAddOtherExpenses.setAdapter(otherExpensesAdapter);
        binding.rvAddOtherExpenses.setNestedScrollingEnabled(false);
        binding.rvAddOtherExpenses.setHasFixedSize(true);

    }

    private void setBankDetailAdapter()
    {
        linearLayoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        binding.recycleviewBankDetail.setLayoutManager(linearLayoutManager);
        binding.recycleviewBankDetail.getItemAnimator().setChangeDuration(0);
        bankDetailAdapter = new BankDetailAdapter(mActivity, bankDetailArraylist,accountTypeList, itemClickListener);
        binding.recycleviewBankDetail.setAdapter(bankDetailAdapter);
        binding.recycleviewBankDetail.setNestedScrollingEnabled(false);
        binding.recycleviewBankDetail.setHasFixedSize(true);

    }

    ItemClickListener itemClickListener = new ItemClickListener() {
        @Override
        public void itemClick(Object object) {

        }

        @Override
        public void itemClick(Object object, int position, String type) {
            switch (type) {
                case "AddImage":
                    selectImage(mActivity, "Vertical", new CallBack() {
                        @Override
                        public void setPhoto(Uri s) {
                            otherImageUri = s;
                            TaxFormDetailModel taxFormDetailModel = (TaxFormDetailModel) object;
                            String fileName = Utils.getFileName(mActivity, s);
                            taxFormDetailModel.setImageUri(otherImageUri);
                            taxFormDetailModel.setImageName(fileName);
                            if (itrAdapter != null)
                                itrAdapter.notifyItemChanged(position);
                        }
                    });
                    break;
                case "AddData":
                    TaxFormDetailModel taxFormDetailModel = (TaxFormDetailModel) object;
                    investmentDetailList.get(position).setOtherDocument(true);
                    investmentDetailList.set(position, taxFormDetailModel);
                    selectType = "Other";
                    itemPosition = position;

                    if (taxFormDetailModel.getImageUri() != null)
                        callUploadImageApi(taxFormDetailModel.getImageUri());
                    break;

                case "AddBankStatementImage":
                    selectPdf(mActivity, "Vertical", new CallBack() {
                        @Override
                        public void setPhoto(Uri s) {
                            itemPosition = 5;
                            imgUploadBankStatement = s;
                            BankDetailModel bankDetailModel = (BankDetailModel) object;
                            String fileName = Utils.getFileName(mActivity, s);
                            bankDetailModel.setImageUri(imgUploadBankStatement);
                            bankDetailModel.setImageName(fileName);
                            if (bankDetailAdapter != null)
                                bankDetailAdapter.notifyItemChanged(position);

                            // callUploadImageApi(imgUploadBankStatement);
                        }
                    });
                    break;
                case "AddBankDetail":
                    BankDetailModel bankDetailModel = (BankDetailModel) object;
                    bankDetailArraylist.get(position).setOtherDocument(true);
                    bankDetailArraylist.set(position, bankDetailModel);
                    selectType = "BankDetail";
                    itemPosition = position;

                    if (bankDetailModel.getImageUri() != null)
                        callUploadImageApi(bankDetailModel.getImageUri());

                    break;
                case "AddOtherIncomeData":
                    TaxFormDetailModel taxFormDetailModel1 = (TaxFormDetailModel) object;
                    otherDetailList.get(position).setOtherDocument(true);
                    otherDetailList.set(position, taxFormDetailModel1);
                    selectType = "OtherIncome";
                    itemPosition = position;

                    break;
                case "AddOtherExpensesData":
                    TaxFormDetailModel taxFormDetailModel2 = (TaxFormDetailModel) object;
                    otherExpensesList.get(position).setOtherDocument(true);
                    otherExpensesList.set(position, taxFormDetailModel2);
                    selectType = "OtherExpenses";
                    itemPosition = position;

                    break;
            }
        }
    };

    public void checkValidation() {

        imgChoseSchoolrAmount = binding.txtAmountSchool.getText().toString();
        imgChoseLicrAmount = binding.txtAmountLic.getText().toString();
        imgChoseMFoundrNameAmount = binding.txtAmountMFound.getText().toString();
        imgChoseSukanyarNameAmount = binding.txtAmountSukanya.getText().toString();
        imgHousingLonerNameAmount = binding.txtAmountHousingLone.getText().toString();
        imgHealthrNameAmount = binding.txtAmountHealth.getText().toString();

        firmName = binding.etFirmName.getText().toString();
        incomeTaxUserName = binding.etUserName.getText().toString();
        incomeTaxPassword = binding.etPassword.getText().toString();
        phoneNumber = binding.etMobileNumber.getText().toString();
        whatsappPhoneNumber = binding.etWhatsappNumber.getText().toString();
        emailId = binding.etEmailId.getText().toString();

        aadharNumber= binding.etadhar.getText().toString();
        aadharAddress= binding.etadharAddress.getText().toString();
        pancardNumber= binding.etPanCardNumber.getText().toString();
        pancardName= binding.etPanCardHolderName.getText().toString();
        pancardFatherName= binding.etFatherName.getText().toString();
        pancardDob= binding.etPanCArdDob.getText().toString();

        houseNumber= binding.etadharAddress.getText().toString();
        roadStreetName= binding.etRoadStreet.getText().toString();
        localAreaName= binding.etLocalityArea.getText().toString();
        cityStateName= binding.etCityTown.getText().toString();
        areaPinCode= binding.etPinCode.getText().toString();
        bankArrayListData = getJsonBankArraylist().toString();

        rentIncome = binding.etRentIncome.getText().toString();
        intrestFromFDR = binding.etInvestFromFDR.getText().toString();
        sBAccountIncome = binding.etSBIncome.getText().toString();
      //otherIncome = binding.etOtherIncome.getText().toString();

        businessFirmName = binding.etBusinessFirmName.getText().toString();
        gstNo = binding.etGstNo.getText().toString();
        cashSale = binding.etCashSale.getText().toString();
        bankSale = binding.etBankSale.getText().toString();
        debtors = binding.etDebtors.getText().toString();
        creditors = binding.etCreditors.getText().toString();
        cash = binding.etCash.getText().toString();
        stock = binding.etStock.getText().toString();

        /*  returnType;*/

        if (houseNumber.isEmpty())
            new CustomToastNotification(mActivity, getResources().getString(R.string.txt_alert_residence_number));
        else if (roadStreetName.isEmpty())
            new CustomToastNotification(mActivity, getResources().getString(R.string.txt_alert_road_name));
        else if (localAreaName.isEmpty())
            new CustomToastNotification(mActivity, getResources().getString(R.string.txt_alert_locality_area));
        else if (cityStateName.isEmpty())
            new CustomToastNotification(mActivity, getResources().getString(R.string.txt_alert_locality_area));
        else if (areaPinCode.isEmpty())
            new CustomToastNotification(mActivity, getResources().getString(R.string.txt_alert_pincode_number));
        else if (aadharNumber.isEmpty())
            new CustomToastNotification(mActivity, getResources().getString(R.string.txt_aadhar_number));
        else if (aadharNumber.length() != 12)
            new CustomToastNotification(mActivity, getResources().getString(R.string.txt_aadhar_number_validation));
        else if (imgFrontAdharName.isEmpty())
            new CustomToastNotification(mActivity, getResources().getString(R.string.txt_empty_front_aadhar_img));
        else if (imgBackAdharrName.isEmpty())
            new CustomToastNotification(mActivity, getResources().getString(R.string.txt_empty_back_aadhar_img));
        else if (pancardName.isEmpty())
            new CustomToastNotification(mActivity, getResources().getString(R.string.txt_pan_card_name));
        else if (pancardFatherName.isEmpty())
            new CustomToastNotification(mActivity, getResources().getString(R.string.txt_pan_card_fname));
        else if (pancardDob.isEmpty())
            new CustomToastNotification(mActivity, getResources().getString(R.string.txt_pan_card_dob));
        else if (gender.isEmpty())
            new CustomToastNotification(mActivity, getResources().getString(R.string.txt_select_gender));
        else if (pancardNumber.isEmpty())
            new CustomToastNotification(mActivity, getResources().getString(R.string.txt_pan_card_number));
        else if (!Utils.panCardValidation(pancardNumber))
            new CustomToastNotification(mActivity, getResources().getString(R.string.txt_pan_card_not_valid));
        else if (imgUploadPanrName.isEmpty())
            new CustomToastNotification(mActivity, getResources().getString(R.string.txt_empty_pancard_img));
        else if (returnType.equalsIgnoreCase("Salary") && imgUploadForm16rName.isEmpty())
            new CustomToastNotification(mActivity, getResources().getString(R.string.txt_empty_form_sis_img));
         else if (bankArrayListData.isEmpty())
            new CustomToastNotification(mActivity, getResources().getString(R.string.txt_alert_bank_detail));
        else
            userTaxSelfDetail();

            /*if (!imgChoseSchoolrName.isEmpty()) {
                schoolFeeValidation();
        }else if (!imgChoseLicrName.isEmpty()) {
                LicValidation();
        }else if (!imgChoseMFoundrName.isEmpty()) {
                MFoundValidation();
        }else if (!imgChoseSukanyarName.isEmpty()) {
            if (imgChoseSukanyarNameAmount.isEmpty()) {
                new CustomToastNotification(mActivity, getResources().getString(R.string.txt_forget_password));
            } else {
                hosingLoneValidation();
            }
        }else if(!imgHousingLonerName.isEmpty()) {
            hosingLoneValidation();
        }else if (!imgHealthrName.isEmpty()) {
            hosingLoneValidation();
        } else {
            userTaxSelfDetail();
        }*/
    }

    public void schoolFeeValidation(){
        if (imgChoseSchoolrAmount.isEmpty()) {
            new CustomToastNotification(mActivity, getResources().getString(R.string.txt_alert_school_amount));
        } else if (!imgChoseLicrName.isEmpty()) {
            LicValidation();
        } else {
            if (!imgChoseMFoundrName.isEmpty()) {
                MFoundValidation();
            } else {
                if (!imgChoseSukanyarName.isEmpty()) {
                    if (imgChoseSukanyarNameAmount.isEmpty()) {
                        new CustomToastNotification(mActivity, getResources().getString(R.string.txt_alert_sukanya_yojna_amount));
                    } else {
                        hosingLoneValidation();
                    }
                } else {
                    hosingLoneValidation();
                }
            }
        }
    }

    public void LicValidation(){

        if (imgChoseLicrAmount.isEmpty()) {
            new CustomToastNotification(mActivity, getResources().getString(R.string.txt_alert_lic_amount));
        } else if (!imgChoseMFoundrName.isEmpty()) {
            MFoundValidation();
        } else {
            if (!imgChoseSukanyarName.isEmpty()) {
                if (imgChoseSukanyarNameAmount.isEmpty()) {
                    new CustomToastNotification(mActivity, getResources().getString(R.string.txt_alert_sukanya_yojna_amount));
                } else {
                    hosingLoneValidation();
                }
            } else {
                hosingLoneValidation();
            }
        }
    }

    public void MFoundValidation(){
        if (imgChoseMFoundrNameAmount.isEmpty()) {
            new CustomToastNotification(mActivity, getResources().getString(R.string.txt_alert_mutual_found_amount));
        } else if (!imgChoseSukanyarName.isEmpty()) {
            if (imgChoseSukanyarNameAmount.isEmpty()) {
                new CustomToastNotification(mActivity, getResources().getString(R.string.txt_alert_sukanya_yojna_amount));
            } else {
                hosingLoneValidation();
            }
        } else hosingLoneValidation();
    }

    public void hosingLoneValidation(){
        if (!imgHousingLonerName.isEmpty()) {
            if (imgHousingLonerNameAmount.isEmpty()) {
                new CustomToastNotification(mActivity, getResources().getString(R.string.txt_alert_house_lone_amount));
            } else if (!imgHealthrName.isEmpty()) {
                if (imgHealthrNameAmount.isEmpty()) {
                    new CustomToastNotification(mActivity, getResources().getString(R.string.txt_alert_health_insurance_amount));
                } else userTaxSelfDetail();
            }
        }else{
            if (!imgHealthrName.isEmpty()) {
                if (imgHealthrNameAmount.isEmpty()) {
                    new CustomToastNotification(mActivity, getResources().getString(R.string.txt_alert_health_insurance_amount));
                } else userTaxSelfDetail();
            }else userTaxSelfDetail();
        }
    }

    public void userTaxSelfDetail(){

        if(returnType.equalsIgnoreCase("Business")){
            /*if(businessFirmName.isEmpty())
                new CustomToastNotification(mActivity, getResources().getString(R.string.txt_alert_firm_name));
            else if(cashSale.isEmpty())
                new CustomToastNotification(mActivity, getResources().getString(R.string.txt_alert_case_sale));
            else if(bankSale.isEmpty())
                new CustomToastNotification(mActivity, getResources().getString(R.string.txt_alert_bank_sale));
            else if(debtors.isEmpty())
                new CustomToastNotification(mActivity, getResources().getString(R.string.txt_alert_bank_sale));
            else if(creditors.isEmpty())
                new CustomToastNotification(mActivity, getResources().getString(R.string.txt_alert_creditors_sale));
            else if(cash.isEmpty())
                new CustomToastNotification(mActivity, getResources().getString(R.string.txt_case));
            else if(stock.isEmpty())
                new CustomToastNotification(mActivity, getResources().getString(R.string.txt_stock));
            else if (firmName.isEmpty())
                new CustomToastNotification(mActivity, getResources().getString(R.string.txt_alert_firm_name));
            else if (!isVerified)
                new CustomToastNotification(mActivity, getResources().getString(R.string.txt_alert_verify));
            else */if (incomeTaxUserName.isEmpty())
                new CustomToastNotification(mActivity, getResources().getString(R.string.txt_alert_firm_username));
            else if (incomeTaxPassword.isEmpty())
                new CustomToastNotification(mActivity, getResources().getString(R.string.txt_alert_firm_password));
            else if (phoneNumber.isEmpty())
                new CustomToastNotification(mActivity, getResources().getString(R.string.txt_alert_mobilenumber));
            else if (whatsappPhoneNumber.isEmpty())
                new CustomToastNotification(mActivity, getResources().getString(R.string.txt_alert_whatsapp_mobile_number));
            else if (emailId.isEmpty())
                new CustomToastNotification(mActivity, getResources().getString(R.string.txt_alert_regoster_emailid));
            else if(!Utils.validateEmail(emailId)){
                binding.etEmailId.requestFocus();
                new CustomToastNotification(this, getResources().getString(R.string.txt_valid_email));
            }else{
                callSubmitFormApi();
            }
        }else{

           /* if (firmName.isEmpty())
                new CustomToastNotification(mActivity, getResources().getString(R.string.txt_alert_firm_name));
            else if (!isVerified)
                new CustomToastNotification(mActivity, getResources().getString(R.string.txt_alert_verify));
            else*/ if (incomeTaxUserName.isEmpty())
                new CustomToastNotification(mActivity, getResources().getString(R.string.txt_alert_firm_username));
            else if (incomeTaxPassword.isEmpty())
                new CustomToastNotification(mActivity, getResources().getString(R.string.txt_alert_firm_password));
            else if (phoneNumber.isEmpty())
                new CustomToastNotification(mActivity, getResources().getString(R.string.txt_alert_mobilenumber));
            else if (whatsappPhoneNumber.isEmpty())
                new CustomToastNotification(mActivity, getResources().getString(R.string.txt_alert_whatsapp_mobile_number));
            else if (emailId.isEmpty())
                new CustomToastNotification(mActivity, getResources().getString(R.string.txt_alert_regoster_emailid));
            else if(!Utils.validateEmail(emailId)){
                binding.etEmailId.requestFocus();
                new CustomToastNotification(this, getResources().getString(R.string.txt_valid_email));
            }else{
                callSubmitFormApi();
            }
        }
    }

    public void callUploadImageApi(Uri uploadImage){
        if (NetworkAlertUtility.isInternetConnection2(mActivity)) {
            HashMap map = new HashMap<String, RequestBody>();

            map.put("file_type", RequestBody.create(fileType, MediaType.parse("multipart/form-data")));
            MultipartBody.Part uploadedFile = null;
            if (uploadImage != null) {
                try {
                    File file = FileUtil.from(mActivity, uploadImage);

                    uploadedFile = MultipartBody.Part.createFormData("file_name",
                            Utils.getFileName(mActivity, Uri.fromFile(file)),
                            RequestBody.create(MediaType.parse("image/*"), file));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            String accessToken = new StorageUtil(mActivity).getAccessToken();
            String apikey = new StorageUtil(mActivity).getApiKey();
            String versionCOde = Utils.getAppVersionName(this);

            HashMap<String, String> headers = new HashMap<>();
            headers.put("api-key", accessToken);
            headers.put("api-secret", apikey);
            headers.put("appversion",versionCOde);


           itrPresenter.sendItrImages(this, map, headers, uploadedFile);
        } else {
            NetworkAlertUtility.showNetworkFailureAlert(this);
        }
    }


    public void callSubmitFormApi(){
        if (NetworkAlertUtility.isInternetConnection2(mActivity)) {
            HashMap map = new HashMap<String, RequestBody>();

            map.put("residence_number", RequestBody.create(houseNumber, MediaType.parse("multipart/form-data")));
            map.put("road_or_street", RequestBody.create(roadStreetName, MediaType.parse("multipart/form-data")));
            map.put("locality_or_area", RequestBody.create(localAreaName, MediaType.parse("multipart/form-data")));
            map.put("city_or_town_or_district", RequestBody.create(cityStateName, MediaType.parse("multipart/form-data")));
            map.put("pin_code", RequestBody.create(areaPinCode, MediaType.parse("multipart/form-data")));
            map.put("aadhar_number", RequestBody.create(aadharNumber, MediaType.parse("multipart/form-data")));

            map.put("pan_card_number", RequestBody.create(pancardNumber, MediaType.parse("multipart/form-data")));
            map.put("pan_card_name", RequestBody.create(pancardName, MediaType.parse("multipart/form-data")));
            map.put("pan_card_father_name", RequestBody.create(pancardFatherName, MediaType.parse("multipart/form-data")));
            map.put("pan_card_dob", RequestBody.create(dob, MediaType.parse("multipart/form-data")));
            map.put("gender", RequestBody.create(gender, MediaType.parse("multipart/form-data")));
            map.put("bank_name", RequestBody.create(bankAccountNumber, MediaType.parse("multipart/form-data")));
            map.put("bank_ifsc_code", RequestBody.create(bankIfscCode, MediaType.parse("multipart/form-data")));

            map.put("front_side_aadhar_file", RequestBody.create(imgFrontAdharName, MediaType.parse("multipart/form-data")));
            map.put("back_side_aadhar_file", RequestBody.create(imgBackAdharrName, MediaType.parse("multipart/form-data")));
            map.put("pan_card_file", RequestBody.create(imgUploadPanrName, MediaType.parse("multipart/form-data")));
            map.put("form_16_file", RequestBody.create(imgUploadForm16rName, MediaType.parse("multipart/form-data")));
            map.put("miscellaneous_file", RequestBody.create(imgUploadMiscellaneousItemrName, MediaType.parse("multipart/form-data")));
            map.put("bank_statement", RequestBody.create(imgUploadBankStatementrName, MediaType.parse("multipart/form-data")));

            map.put("school_fees_file", RequestBody.create(imgChoseSchoolrName, MediaType.parse("multipart/form-data")));
            map.put("lic_file", RequestBody.create(imgChoseLicrName, MediaType.parse("multipart/form-data")));
            map.put("mutual_fund_file", RequestBody.create(imgChoseMFoundrName, MediaType.parse("multipart/form-data")));
            map.put("sukanya_yohna_file", RequestBody.create(imgChoseSukanyarName, MediaType.parse("multipart/form-data")));
            map.put("housing_loan_file", RequestBody.create(imgHousingLonerName, MediaType.parse("multipart/form-data")));
            map.put("health_insurance_file", RequestBody.create(imgHealthrName, MediaType.parse("multipart/form-data")));

            map.put("sf_amount", RequestBody.create(imgChoseSchoolrAmount, MediaType.parse("multipart/form-data")));
            map.put("lic_amount", RequestBody.create(imgChoseLicrAmount, MediaType.parse("multipart/form-data")));
            map.put("mf_amount", RequestBody.create(imgChoseMFoundrNameAmount, MediaType.parse("multipart/form-data")));
            map.put("sy_amount", RequestBody.create(imgChoseSukanyarNameAmount, MediaType.parse("multipart/form-data")));
            map.put("hl_amount", RequestBody.create(imgHousingLonerNameAmount, MediaType.parse("multipart/form-data")));
            map.put("hi_amount", RequestBody.create(imgHealthrNameAmount, MediaType.parse("multipart/form-data")));


            map.put("firm_name", RequestBody.create(firmName, MediaType.parse("multipart/form-data")));
            map.put("declaration_file", RequestBody.create(imgSelfDeclarationClientName, MediaType.parse("multipart/form-data")));
            map.put("itr_user_name", RequestBody.create(incomeTaxUserName, MediaType.parse("multipart/form-data")));
            map.put("itr_password", RequestBody.create(incomeTaxPassword, MediaType.parse("multipart/form-data")));
            map.put("itr_mobile", RequestBody.create(phoneNumber, MediaType.parse("multipart/form-data")));
            map.put("whatsapp_number", RequestBody.create(whatsappPhoneNumber, MediaType.parse("multipart/form-data")));
            map.put("itr_email", RequestBody.create(emailId, MediaType.parse("multipart/form-data")));
            map.put("service_id", RequestBody.create(String.valueOf(ServiceId), MediaType.parse("multipart/form-data")));

            map.put("cash", RequestBody.create(String.valueOf(cash), MediaType.parse("multipart/form-data")));
            map.put("stock", RequestBody.create(String.valueOf(stock), MediaType.parse("multipart/form-data")));
            map.put("debtors", RequestBody.create(String.valueOf(debtors), MediaType.parse("multipart/form-data")));
            map.put("creditors", RequestBody.create(String.valueOf(creditors), MediaType.parse("multipart/form-data")));
            map.put("cash_sale", RequestBody.create(String.valueOf(cashSale), MediaType.parse("multipart/form-data")));
            map.put("bank_sale", RequestBody.create(String.valueOf(bankSale), MediaType.parse("multipart/form-data")));
            map.put("business_firm_name", RequestBody.create(String.valueOf(businessFirmName), MediaType.parse("multipart/form-data")));
            map.put("gst_number", RequestBody.create(String.valueOf(gstNo), MediaType.parse("multipart/form-data")));

            map.put("fdr_intrest", RequestBody.create(String.valueOf(intrestFromFDR), MediaType.parse("multipart/form-data")));
            map.put("sbi_income", RequestBody.create(String.valueOf(sBAccountIncome), MediaType.parse("multipart/form-data")));
            map.put("rent_income", RequestBody.create(String.valueOf(rentIncome), MediaType.parse("multipart/form-data")));
            map.put("other_expenses", RequestBody.create(String.valueOf(getJsonOtherExpensesArraylist()), MediaType.parse("multipart/form-data")));

            map.put("is_sale_land", RequestBody.create(String.valueOf(isSaleLand), MediaType.parse("multipart/form-data")));
            map.put("is_sale_share", RequestBody.create(String.valueOf(isSaleShare), MediaType.parse("multipart/form-data")));
            map.put("other_income_json", RequestBody.create(String.valueOf(getJsonOtherIncomeArraylist()), MediaType.parse("multipart/form-data")));
            map.put("sale_deed_file_name", RequestBody.create(String.valueOf(imgSaleDeedName), MediaType.parse("multipart/form-data")));
            map.put("purchase_deed_file_name", RequestBody.create(String.valueOf(imgPurchaseDeedName), MediaType.parse("multipart/form-data")));
            map.put("sale_share_file_name", RequestBody.create(String.valueOf(imgShareDeedName), MediaType.parse("multipart/form-data")));
            map.put("itr_form_type", RequestBody.create(String.valueOf(returnType), MediaType.parse("multipart/form-data")));

            String listString =  getJesonArraylist();
            Log.e("listString = ",""+listString);

            map.put("invest_data", RequestBody.create(listString, MediaType.parse("multipart/form-data")));
            map.put("bank_data", RequestBody.create(bankArrayListData, MediaType.parse("multipart/form-data")));


            String accessToken = new StorageUtil(mActivity).getAccessToken();
            String apikey = new StorageUtil(mActivity).getApiKey();
            String versionCOde = Utils.getAppVersionName(this);

            HashMap<String, String> headers = new HashMap<>();
            headers.put("api-key", accessToken);
            headers.put("api-secret", apikey);
            headers.put("appversion",versionCOde);

            itrPresenter.submitITRForm(this, map, headers);
            } else {
                NetworkAlertUtility.showNetworkFailureAlert(this);
            }
        }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.imgFrontAdhar) {
            selectImage(mActivity, "Change", new CallBack() {
                @Override
                public void setPhoto(Uri s) {
                    itemPosition = 1;
                    imgFrontAdhar = s;
                    selectType = "Fix";
                    callUploadImageApi(imgFrontAdhar);
                    Utils.setDocuments(mActivity, binding.imgAdhar, binding.txtAdhar, s);
                }
            });
        } else if (id == R.id.imgBackAdhar) {
            selectImage(mActivity, "Change", new CallBack() {
                @Override
                public void setPhoto(Uri s) {
                    itemPosition = 2;
                    imgBackAdhar = s;
                    selectType = "Fix";
                    callUploadImageApi(imgBackAdhar);
                    Utils.setDocuments(mActivity, binding.adBack, binding.txtBack, s);
                }
            });
        } else if (id == R.id.imgUploadPan) {
            selectImage(mActivity, "Change", new CallBack() {
                @Override
                public void setPhoto(Uri s) {
                    itemPosition = 3;
                    imgUploadPan = s;
                    selectType = "Fix";
                    callUploadImageApi(imgUploadPan);
                    Utils.setDocuments(mActivity, binding.panimg, binding.txtPan, s);
                }
            });
        } else if (id == R.id.imgUploadForm16) {
            selectImage(mActivity, "Vertical", new CallBack() {
                @Override
                public void setPhoto(Uri s) {
                    itemPosition = 4;
                    imgUploadForm16 = s;
                    selectType = "Fix";
                    callUploadImageApi(imgUploadForm16);
                    Utils.setDocuments(mActivity, binding.imgForm16, binding.txtForm16, s);
                }
            });
        } else if (id == R.id.imgUploadBankStatement) {
            selectPdf(mActivity, "Vertical", new CallBack() {
                @Override
                public void setPhoto(Uri s) {
                    itemPosition = 5;
                    imgUploadBankStatement = s;
                    selectType = "Fix";
                    callUploadImageApi(imgUploadBankStatement);
                    Utils.setDocuments(mActivity, binding.imgForm16, binding.txtForm16, s);
                }
            });
        } else if (id == R.id.imgUploadMiscellaneousItem) {
            selectPdf(mActivity, "Vertical", new CallBack() {
                @Override
                public void setPhoto(Uri s) {
                    itemPosition = 6;
                    imgUploadMiscellaneousItem = s;
                    selectType = "Fix";
                    callUploadImageApi(imgUploadMiscellaneousItem);
                    Utils.setDocuments(mActivity, binding.imgMiscellaneousItem, binding.txtMiscellaneousItem, s);
                }
            });
        } else if (id == R.id.btnChoseSchool) {
            selectPdf(mActivity, "Vertical", new CallBack() {
                @Override
                public void setPhoto(Uri s) {
                    itemPosition = 7;
                    imgChoseSchool = s;
                    selectType = "Fix";
                    callUploadImageApi(imgChoseSchool);
                }
            });
        } else if (id == R.id.btnChoseLic) {
            selectPdf(mActivity, "Vertical", new CallBack() {
                @Override
                public void setPhoto(Uri s) {
                    itemPosition = 8;
                    imgChoseLic = s;
                    selectType = "Fix";
                    callUploadImageApi(imgChoseLic);
                    setSingleImageURI(binding.imgAdhar, s);
                }
            });
        } else if (id == R.id.btnChoseMFound) {
            selectPdf(mActivity, "Vertical", new CallBack() {
                @Override
                public void setPhoto(Uri s) {
                    itemPosition = 9;
                    imgChoseMFound = s;
                    selectType = "Fix";
                    callUploadImageApi(imgChoseMFound);
                }
            });
        } else if (id == R.id.btnChoseSukanya) {
            selectPdf(mActivity, "Vertical", new CallBack() {
                @Override
                public void setPhoto(Uri s) {
                    itemPosition = 10;
                    imgChoseSukanya = s;
                    selectType = "Fix";
                    callUploadImageApi(imgChoseSukanya);
                }
            });
        } else if (id == R.id.btnHousingLone) {
            selectPdf(mActivity, "Vertical", new CallBack() {
                @Override
                public void setPhoto(Uri s) {
                    itemPosition = 11;
                    imgHousingLone = s;
                    selectType = "Fix";
                    callUploadImageApi(imgHousingLone);
                }
            });
        } else if (id == R.id.btnHealth) {
            selectPdf(mActivity, "Vertical", new CallBack() {
                @Override
                public void setPhoto(Uri s) {
                    itemPosition = 12;
                    imgHealth = s;
                    selectType = "Fix";
                    callUploadImageApi(imgHealth);
                }
            });
        } else if (id == R.id.btnDownloadForm) {
            showVerificationDialog(mActivity);
        } else if (id == R.id.imgUploadSaleDeed) {
            selectPdf(mActivity, "Vertical", new CallBack() {
                @Override
                public void setPhoto(Uri s) {
                    itemPosition = 14;
                    imgSaleDeed = s;
                    selectType = "Fix";
                    callUploadImageApi(imgSaleDeed);
                    Utils.setDocuments(mActivity, binding.imgSaleDeed, binding.txtSaleDeed, s);
                }
            });
        } else if (id == R.id.imgUploadPurchaseDeed) {
            selectPdf(mActivity, "Vertical", new CallBack() {
                @Override
                public void setPhoto(Uri s) {
                    itemPosition = 15;
                    imgPurchaseDeed = s;
                    selectType = "Fix";
                    callUploadImageApi(imgPurchaseDeed);
                    Utils.setDocuments(mActivity, binding.imgPurchaseDeed, binding.txtPurchaseDeed, s);
                }
            });
        } else if (id == R.id.imgUploadSaleShareFile) {
            selectPdf(mActivity, "Vertical", new CallBack() {
                @Override
                public void setPhoto(Uri s) {
                    itemPosition = 16;
                    imgShareDeed = s;
                    selectType = "Fix";
                    callUploadImageApi(imgShareDeed);
                    Utils.setDocuments(mActivity, binding.imgSaleShareFile, binding.txtSaleShareFile, s);
                }
            });
        } else if (id == R.id.btnAddMoreExpenses) {
            addOtherExpenses();
        } else if (id == R.id.btnAddMore) {
            addOtherTransaction();
        } else if (id == R.id.btnAddMoreBAnk) {
            addBAnkDetail();
        } else if (id == R.id.btnRegisterITR) {
            setHideVisibleContent(0);
            setClickBackground(0);
        } else if (id == R.id.btnNewUser) {
            setHideVisibleContent(1);
            setClickBackground(1);
        } else if (id == R.id.btnRegisterNow) {
            if (settingData != null)
                callUrl(settingData.getItrRegister());
        } else if (id == R.id.btnResetPassword) {
            if (settingData != null)
                callUrl(settingData.getItrLogin());
        } else if (id == R.id.btnFPassword) {
            setHideVisibleContent(2);
            setClickBackground(2);
        } else if (id == R.id.btnSignup) {
            checkValidation();
        } else if (id == R.id.llPropertySale) {
            setCheckBoxSelected(0);
            binding.llSaleProperty.setVisibility(View.VISIBLE);
            binding.llSaleShare.setVisibility(View.GONE);
        } else if (id == R.id.llShareSale) {
            setCheckBoxSelected(1);
            binding.llSaleProperty.setVisibility(View.GONE);
            binding.llSaleShare.setVisibility(View.VISIBLE);
        } else if (id == R.id.llMale) {
            setCheckBoxSelectedGender(0);
        } else if (id == R.id.llFemale) {
            setCheckBoxSelectedGender(1);
        } else if (id == R.id.btnAddMoreOtherIncome) {
            addOtherIncome();
        } else if (id == R.id.etPanCArdDob) {
            showDatePickerDOBDialog();
        }
//        switch (v.getId()){
//
//            case R.id.imgFrontAdhar:
//                selectImage(mActivity,"Change",new CallBack() {
//                    @Override
//                    public void setPhoto(Uri s) {
//                        itemPosition = 1;
//                        imgFrontAdhar = s;
//                        selectType = "Fix";
//                        callUploadImageApi(imgFrontAdhar);
//                        Utils.setDocuments(mActivity,binding.imgAdhar,binding.txtAdhar,s);
//                    }
//                });
//                break;
//            case R.id.imgBackAdhar:
//                selectImage(mActivity,"Change",new CallBack() {
//                    @Override
//                    public void setPhoto(Uri s) {
//                        itemPosition = 2;
//                        imgBackAdhar = s;
//                        selectType = "Fix";
//                        callUploadImageApi(imgBackAdhar);
//                        Utils.setDocuments(mActivity,binding.adBack,binding.txtBack,s);
//
//                    }
//                });
//                break;
//            case R.id.imgUploadPan:
//                selectImage(mActivity,"Change",new CallBack() {
//                    @Override
//                    public void setPhoto(Uri s) {
//                        itemPosition = 3;
//                        imgUploadPan = s;
//                        selectType = "Fix";
//                        callUploadImageApi(imgUploadPan);
//                        Utils.setDocuments(mActivity,binding.panimg,binding.txtPan,s);
//                    }
//                });
//                break;
//            case R.id.imgUploadForm16:
//                selectImage(mActivity,"Vertical",new CallBack() {
//                    @Override
//                    public void setPhoto(Uri s) {
//                        itemPosition = 4;
//                        imgUploadForm16 = s;
//                        selectType = "Fix";
//                        callUploadImageApi(imgUploadForm16);
//                        Utils.setDocuments(mActivity,binding.imgForm16,binding.txtForm16,s);
//                    }
//                });
//                break;
//            case R.id.imgUploadBankStatement:
//                selectPdf(mActivity,"Vertical",new CallBack() {
//                    @Override
//                    public void setPhoto(Uri s) {
//                        itemPosition = 5;
//                        imgUploadBankStatement = s;
//                        selectType = "Fix";
//                        callUploadImageApi(imgUploadBankStatement);
//                        Utils.setDocuments(mActivity,binding.imgForm16,binding.txtForm16,s);
//
//                        //binding.imgBankStatement.setImageDrawable(getResources().getDrawable(R.drawable.pdfab,null));
//                        //setSingleImageURI(binding.imgBankStatement, s);
//                    }
//                });
//                break;
//            case R.id.imgUploadMiscellaneousItem:
//                selectPdf(mActivity,"Vertical",new CallBack() {
//                    @Override
//                    public void setPhoto(Uri s) {
//                        itemPosition = 6;
//                        imgUploadMiscellaneousItem = s;
//                        selectType = "Fix";
//                        callUploadImageApi(imgUploadMiscellaneousItem);
//                        Utils.setDocuments(mActivity,binding.imgMiscellaneousItem,binding.txtMiscellaneousItem,s);
//                    }
//                });
//                break;
//            case R.id.btnChoseSchool:
//                selectPdf(mActivity,"Vertical",new CallBack() {
//                    @Override
//                    public void setPhoto(Uri s) {
//                        itemPosition = 7;
//                        imgChoseSchool = s;
//                        selectType = "Fix";
//                        callUploadImageApi(imgChoseSchool);
//                    }
//                });
//
//                break;
//            case R.id.btnChoseLic:
//                selectPdf(mActivity,"Vertical",new CallBack() {
//                    @Override
//                    public void setPhoto(Uri s) {
//                        itemPosition = 8;
//                        imgChoseLic = s;
//                        selectType = "Fix";
//                        callUploadImageApi(imgChoseLic);
//                        setSingleImageURI(binding.imgAdhar, s);
//                    }
//                });
//                break;
//            case R.id.btnChoseMFound:
//                selectPdf(mActivity,"Vertical",new CallBack() {
//                    @Override
//                    public void setPhoto(Uri s) {
//                        itemPosition = 9;
//                        imgChoseMFound = s;
//                        selectType = "Fix";
//                        callUploadImageApi(imgChoseMFound);
//                    }
//                });
//                break;
//            case R.id.btnChoseSukanya:
//                selectPdf(mActivity,"Vertical",new CallBack() {
//                    @Override
//                    public void setPhoto(Uri s) {
//                        itemPosition = 10;
//                        imgChoseSukanya = s;
//                        selectType = "Fix";
//                        callUploadImageApi(imgChoseSukanya);
//                    }
//                });
//                break;
//            case R.id.btnHousingLone:
//                selectPdf(mActivity,"Vertical",new CallBack() {
//                    @Override
//                    public void setPhoto(Uri s) {
//                        itemPosition = 11;
//                        imgHousingLone = s;
//                        selectType = "Fix";
//                        callUploadImageApi(imgHousingLone);
//                    }
//                });
//                break;
//            case R.id.btnHealth:
//                selectPdf(mActivity,"Vertical",new CallBack() {
//                    @Override
//                    public void setPhoto(Uri s) {
//                        itemPosition = 12;
//                        imgHealth = s;
//                        selectType = "Fix";
//
//
//                        callUploadImageApi(imgHealth);
//                    }
//                });
//                break;
//            case R.id.btnDownloadForm:
//                showVerificationDialog(mActivity);
//                break;
//            case R.id.imgUploadSaleDeed:
//                selectPdf(mActivity,"Vertical",new CallBack() {
//                    @Override
//                    public void setPhoto(Uri s) {
//                        itemPosition = 14;
//                        imgSaleDeed = s;
//                        selectType = "Fix";
//                        callUploadImageApi(imgSaleDeed);
//                        Utils.setDocuments(mActivity,binding.imgSaleDeed,binding.txtSaleDeed,s);
//                    }
//                });
//                break;
//            case R.id.imgUploadPurchaseDeed:
//                selectPdf(mActivity,"Vertical",new CallBack() {
//                    @Override
//                    public void setPhoto(Uri s) {
//                        itemPosition = 15;
//                        imgPurchaseDeed = s;
//                        selectType = "Fix";
//                        callUploadImageApi(imgPurchaseDeed);
//                        Utils.setDocuments(mActivity,binding.imgPurchaseDeed,binding.txtPurchaseDeed,s);
//                    }
//                });
//                break;
//            case R.id.imgUploadSaleShareFile:
//                selectPdf(mActivity,"Vertical",new CallBack() {
//                    @Override
//                    public void setPhoto(Uri s) {
//                        itemPosition = 16;
//                        imgShareDeed = s;
//                        selectType = "Fix";
//                        callUploadImageApi(imgShareDeed);
//                        Utils.setDocuments(mActivity,binding.imgSaleShareFile,binding.txtSaleShareFile,s);
//                    }
//                });
//                break;
//
//            case R.id.btnAddMoreExpenses:
//                    addOtherExpenses();
//                break;
//
//            case R.id.btnAddMore:
//                addOtherTransaction();
//                break;
//            case R.id.btnAddMoreBAnk:
//                addBAnkDetail();
//                break;
//            case R.id.btnRegisterITR:
//                setHideVisibleContent(0);
//                setClickBackground(0);
//                break;
//            case R.id.btnNewUser:
//                setHideVisibleContent(1);
//                setClickBackground(1);
//                break;
//            case R.id.btnRegisterNow:
//                if(settingData != null)
//                    callUrl(settingData.getItrRegister());
//                break;
//            case R.id.btnResetPassword:
//                if(settingData != null)
//                    callUrl(settingData.getItrLogin());
//                break;
//            case R.id.btnFPassword:
//                setHideVisibleContent(2);
//                setClickBackground(2);
//                break;
//            case R.id.btnSignup:
//                 checkValidation();
//                break;
//            case R.id.llPropertySale:
//                setCheckBoxSelected(0);
//                binding.llSaleProperty.setVisibility(View.VISIBLE);
//                binding.llSaleShare.setVisibility(View.GONE);
//                break;
//            case R.id.llShareSale:
//                setCheckBoxSelected(1);
//                binding.llSaleProperty.setVisibility(View.GONE);
//                binding.llSaleShare.setVisibility(View.VISIBLE);
//                break;
//            case R.id.llMale:
//                setCheckBoxSelectedGender(0);
//                break;
//            case R.id.llFemale:
//                setCheckBoxSelectedGender(1);
//                break;
//            case R.id.btnAddMoreOtherIncome:
//                    addOtherIncome();
//                break;
//            case R.id.etPanCArdDob:
//                showDatePickerDOBDialog();
//                break;
//        }
    }

    public void setCheckBoxSelected(int position){

        switch (position){
            case 0:
                binding.imgCheckBoxMonthly.setImageDrawable(getResources().getDrawable(R.drawable.ic_checkbox_icon,null));
                binding.imgCheckBoxQuarterly.setImageDrawable(getResources().getDrawable(R.drawable.ic_checked_icon,null));
                returnType = "SaleProperty";
                isSaleLand = "1";
                isSaleShare = "0";
                break;
            case 1:
                binding.imgCheckBoxMonthly.setImageDrawable(getResources().getDrawable(R.drawable.ic_checked_icon,null));
                binding.imgCheckBoxQuarterly.setImageDrawable(getResources().getDrawable(R.drawable.ic_checkbox_icon,null));
                returnType="SaleShare";
                isSaleLand = "0";
                isSaleShare = "1";
                break;
        }
    }

    public void setCheckBoxSelectedGender(int position){

        switch (position){
            case 0:
                binding.imgCheckBoxMale.setImageDrawable(getResources().getDrawable(R.drawable.ic_checkbox_icon,null));
                binding.imgCheckBoxFemale.setImageDrawable(getResources().getDrawable(R.drawable.ic_checked_icon,null));
                gender = "Male";
                break;
            case 1:
                binding.imgCheckBoxMale.setImageDrawable(getResources().getDrawable(R.drawable.ic_checked_icon,null));
                binding.imgCheckBoxFemale.setImageDrawable(getResources().getDrawable(R.drawable.ic_checkbox_icon,null));
                gender="Female";
                break;
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(mActivity, R.style.MyDialogTheme, (view, year, monthOfYear, dayOfMonth) -> {
            String d = dayOfMonth < 10 ? "0" + dayOfMonth : "" + dayOfMonth;
            String m = (monthOfYear + 1) < 10 ? "0" + (monthOfYear + 1) : "" + (monthOfYear + 1);
            dob = String.valueOf(year).concat("-").concat(m).concat("-").concat(d);
            binding.etPanCArdDob.setText(parseDate(d + "-" + m + "-" + year));
        }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
        datePickerDialog.show();
    }

    public void callUrl(String url){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    public void uploadImages(int itemPosition,String itemValue,String fileName){
        switch (itemValue){
            case "Fix":{
                switch (itemPosition){
                    case 1:{
                        imgFrontAdharName = fileName;
                        break;
                    }
                    case 2:{
                        imgBackAdharrName = fileName;
                        break;
                    }
                    case 3:{
                        imgUploadPanrName = fileName;
                        break;
                    }
                    case 4:{
                        imgUploadForm16rName = fileName;
                        break;
                    }
                    case 5:{
                        imgUploadBankStatementrName = fileName;
                        break;
                    }
                    case 6:{
                        imgUploadMiscellaneousItemrName = fileName;
                        break;
                    }
                    case 7:{
                        imgChoseSchoolrName = fileName;
                        binding.txtNoFileSchool.setText(imgChoseSchoolrName);
                        break;
                    }
                    case 8:{
                        imgChoseLicrName = fileName;
                        binding.txtNoFileLic.setText(imgChoseLicrName);
                        break;
                    }
                    case 9:{
                        imgChoseMFoundrName = fileName;
                        binding.txtNoFileMFound.setText(imgChoseMFoundrName);
                        break;
                    }
                    case 10:{
                        imgChoseSukanyarName = fileName;
                        binding.txtNoFileSukanya.setText(imgChoseSukanyarName);
                        break;
                    }
                    case 11:{
                        imgHousingLonerName = fileName;
                        binding.txtNoFileHousingLone.setText(imgHousingLonerName);
                        break;
                    }
                    case 12:{
                        imgHealthrName = fileName;
                        binding.txtNoFileHealth.setText(imgHealthrName);
                        break;
                    }
                    case 13:{
                        imgSelfDeclarationClientName = fileName;
                        break;
                    }
                    case 14:{
                        imgSaleDeedName = fileName;
                        break;
                    } case 15:{
                        imgPurchaseDeedName = fileName;
                        break;
                    } case 16:{
                        imgShareDeedName = fileName;
                        break;
                    }
                }
                break;
            }
            case "Other":
            {
                investmentDetailList.get(itemPosition).setImageName(fileName);
                if(itrAdapter!= null)
                    itrAdapter.notifyItemChanged(itemPosition);
                break;
            }
            case "OtherExpenses":
            {
                otherExpensesList.get(itemPosition).setImageName(fileName);
                if(otherExpensesAdapter!= null)
                    otherExpensesAdapter.notifyItemChanged(itemPosition);
                break;
            }
            case "OtherIncome":
            {
                otherDetailList.get(itemPosition).setImageName(fileName);
                if(otherIncomeAdapter!= null)
                    otherIncomeAdapter.notifyItemChanged(itemPosition);
                break;
            }
            case "BankDetail":
            {
                bankDetailArraylist.get(itemPosition).setImageName(fileName);
                if(bankDetailAdapter!= null)
                    bankDetailAdapter.notifyItemChanged(itemPosition);
                break;
            }
        }
    }

    public void setSingleImageURI(ImageView view,Uri sourceUri) {
        if (sourceUri != null) {
            Glide.with(mActivity)
                    .load(sourceUri)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ic_profile_placeholder)
                    .error(R.drawable.ic_profile_placeholder)
                    .into(view);
        }
    }

    public void addOtherTransaction(){
        investmentDetailList.add(new TaxFormDetailModel(1,"",null,"","",false));
        if(itrAdapter!= null)
            itrAdapter.notifyDataSetChanged();
        else
            setItrAdapter();
    }

    public void addOtherIncome(){
        otherDetailList.add(new TaxFormDetailModel(1,"",null,"","",false));
        if(otherIncomeAdapter!= null)
            otherIncomeAdapter.notifyDataSetChanged();
        else
            setOtherIncomeAdapter();
    }


    public void addOtherExpenses(){
        otherExpensesList.add(new TaxFormDetailModel(1,"",null,"","",false));
        if(otherExpensesAdapter!= null)
            otherExpensesAdapter.notifyDataSetChanged();
        else
            setOtherExpensesAdapter();
    }

    public void addBAnkDetail(){
        bankDetailArraylist.add(new BankDetailModel(1,"","",null,"","","",false));
        if(bankDetailAdapter!= null)
            bankDetailAdapter.notifyDataSetChanged();
        else
            setBankDetailAdapter();
    }

    public void setClickBackground(int position){
        switch (position){
            case 0:
                binding.btnRegisterITR.setCardBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.color_F72F20,null)));
                binding.btnNewUser.setCardBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.white,null)));
                binding.btnFPassword.setCardBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.white,null)));
                binding.txtRegisterITR.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white,null)));
                binding.txtNewUser.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.color_F72F20,null)));
                binding.txtFPassword.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.color_F72F20,null)));
                break;
            case 1:
                binding.btnRegisterITR.setCardBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.white,null)));
                binding.btnNewUser.setCardBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.color_F72F20,null)));
                binding.btnFPassword.setCardBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.white,null)));
                binding.txtRegisterITR.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.color_F72F20,null)));
                binding.txtNewUser.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white,null)));
                binding.txtFPassword.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.color_F72F20,null)));
                break;
            case 2:
                binding.btnRegisterITR.setCardBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.white,null)));
                binding.btnNewUser.setCardBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.white,null)));
                binding.btnFPassword.setCardBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.color_F72F20,null)));
                binding.txtRegisterITR.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.color_F72F20,null)));
                binding.txtNewUser.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.color_F72F20,null)));
                binding.txtFPassword.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white,null)));
                break;
        }
    }

    public void setHideVisibleContent(int position){
        switch (position){
            case 0:
                binding.btnRegisterNow.setVisibility(View.GONE);
                binding.btnResetPassword.setVisibility(View.GONE);
                binding.llRegisterInfo.setVisibility(View.VISIBLE);
                break;
            case 1:
                binding.btnRegisterNow.setVisibility(View.VISIBLE);
                binding.btnResetPassword.setVisibility(View.GONE);
                binding.llRegisterInfo.setVisibility(View.GONE);
                break;
            case 2:
                binding.btnRegisterNow.setVisibility(View.GONE);
                binding.btnResetPassword.setVisibility(View.VISIBLE);
                binding.llRegisterInfo.setVisibility(View.GONE);
                break;
        }
    }

    public String getJesonArraylist(){

        ArrayList<TaxFormDetailModel> tempInvestmentDetailList = new ArrayList<>();

        for(int i= 0;i<investmentDetailList.size();i++){
            if(investmentDetailList.get(i).isOtherDocument()){
                tempInvestmentDetailList.add(investmentDetailList.get(i));
            }
        }

        Gson gson = new Gson();
        String listString = gson.toJson(
                tempInvestmentDetailList,
                new TypeToken<ArrayList<TaxFormDetailModel>>() {}.getType());


        return listString;
    }

    public String getJsonBankArraylist(){

        ArrayList<BankDetailModel> tempBankDetailArraylist = new ArrayList<>();

        for(int i= 0;i<bankDetailArraylist.size();i++){
            if(bankDetailArraylist.get(i).isOtherDocument()){
                tempBankDetailArraylist.add(bankDetailArraylist.get(i));
            }
        }

        String listBankString = "";
        if(tempBankDetailArraylist.size()>0) {
            Gson gson = new Gson();
            listBankString = gson.toJson(
                    tempBankDetailArraylist,
                    new TypeToken<ArrayList<BankDetailModel>>() {
                    }.getType());
        }


        return listBankString;
    }

 public String getJsonOtherIncomeArraylist(){

        ArrayList<TaxFormDetailModel> tempBankDetailArraylist = new ArrayList<>();

        for(int i= 0;i<otherDetailList.size();i++){
            if(otherDetailList.get(i).isOtherDocument()){
                tempBankDetailArraylist.add(otherDetailList.get(i));
            }
        }

        String listBankString = "";
        if(tempBankDetailArraylist.size()>0) {
            Gson gson = new Gson();
            listBankString = gson.toJson(
                    tempBankDetailArraylist,
                    new TypeToken<ArrayList<TaxFormDetailModel>>() {
                    }.getType());
        }


        return listBankString;
    }

    public String getJsonOtherExpensesArraylist(){

        ArrayList<TaxFormDetailModel> tempBankDetailArraylist = new ArrayList<>();

        for(int i= 0;i<otherExpensesList.size();i++){
            if(otherExpensesList.get(i).isOtherDocument()){
                tempBankDetailArraylist.add(otherExpensesList.get(i));
            }
        }

        String listBankString = "";
        if(tempBankDetailArraylist.size()>0) {
            Gson gson = new Gson();
            listBankString = gson.toJson(
                    tempBankDetailArraylist,
                    new TypeToken<ArrayList<TaxFormDetailModel>>() {
                    }.getType());
        }


        return listBankString;
    }

    @Override
    public void onUploadImageSuccess(BaseResponse body) {
        if(body!= null){
            String fileName = body.getData().getFileName();
            uploadImages(itemPosition,selectType,fileName);
        }
    }

    @Override
    public void onFormSubmitSuccess(BaseResponse body) {
        if(body!= null){
            new CustomToastNotification(mActivity,body.getMsg());
            getOnBackPressedDispatcher().onBackPressed();
        }
    }

    @Override
    public Context getContext() {
        return null;
    }

    @Override
    public void enableLoadingBar(boolean enable, String s) {
        if (enable)
            loadProgressBar(mActivity);
        else
            dismissProgressBar();
    }

    @Override
    public void onError(String reason) {
        try {
            JSONObject jObjError  = new  JSONObject(reason);
            // val title = if (jObjError.has("title")) jObjError.getString("title") else ""
            String reason1 = jObjError.has("message") ? jObjError.getString("message") : "";
            new CustomToastNotification(this, reason1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dialogAccountDeactivate(String reason) {
        Utils.userLogoutDeleteAccount(mActivity);
    }

    @Override
    public void onErrorToast(String reason) {
        new CustomToastNotification(this, reason);
    }

        public void showVerificationDialog(Activity activity){

            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.custom_dialogbox_varification);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            LinearLayout llSelfDeclaration = (LinearLayout) dialog.findViewById(R.id.llSelfDeclaration);
            ShapeableImageView checkedImage = (ShapeableImageView) dialog.findViewById(R.id.imgCheckBoxSelfProperty);
            MaterialButton btnVerification = (MaterialButton) dialog.findViewById(R.id.btnVerification);
            TextView tvNote = (TextView) dialog.findViewById(R.id.tvNote);
            OtpView otpView = (OtpView) dialog.findViewById(R.id.otp_view);

            otpView.setOtpCompletionListener(otp -> {
                if(otp.equalsIgnoreCase("1234")){
                    isVerified = true;
                    dialog.dismiss();
                }else
                    new CustomToastNotification(mActivity,"Otp value not match");

            });


            llSelfDeclaration.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(!isChecked){
                        isChecked = true;
                        checkedImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_checkbox_icon,null));
                        tvNote.setVisibility(View.GONE);
                        otpView.setVisibility(View.GONE);

                    }else{
                        isChecked = false;
                        btnVerification.setText(getResources().getString(R.string.txt_btn_verify));
                        checkedImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_checked_icon,null));
                        tvNote.setVisibility(View.GONE);
                        otpView.setVisibility(View.GONE);
                    }

                }
            });
            btnVerification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(btnVerification.getText().toString().equalsIgnoreCase(getResources().getString(R.string.txt_btn_verify))){
                        btnVerification.setText(getResources().getString(R.string.txt_btn_verify_otp));
                        tvNote.setVisibility(View.VISIBLE);
                        otpView.setVisibility(View.VISIBLE);
                    }else{
                      dialog.dismiss();
                    }
                }
            });

            dialog.setOnDismissListener(dialog1 -> {
            });
            dialog.show();
        }
}