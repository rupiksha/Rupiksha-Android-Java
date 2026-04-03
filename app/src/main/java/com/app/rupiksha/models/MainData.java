//package com.app.rupiksha.models;
//
//import com.google.gson.annotations.Expose;
//import com.google.gson.annotations.SerializedName;
//
//import java.util.List;
//
//public class MainData
//{
//    @SerializedName("roles")
//    @Expose
//    private List<RoleModel> roles;
//
//    @SerializedName("rdname")
//    @Expose
//    private List<DeviceModel> rdname;
//
//    @SerializedName("bank")
//    @Expose
//    private List<BankModel> bank;
//
//    @SerializedName("state")
//    @Expose
//    private List<StateModel> state;
//
//    @SerializedName("address_proof_list")
//    @Expose
//    private List<AddressProof> addressProofList;
//
//    public List<AddressProof> getAddressProofList() {
//        return addressProofList;
//    }
//
//    public void setAddressProofList(List<AddressProof> addressProofList) {
//        this.addressProofList = addressProofList;
//    }
//
//    @SerializedName("accountkycstatus")
//    @Expose
//    private AccountKycStatus accountkycstatus;
//
//    @SerializedName("kyc")
//    @Expose
//    private KycModel kyc;
//
//    @SerializedName("profile")
//    @Expose
//    private ModelUserInfo profile;
//
//    @SerializedName("support")
//    @Expose
//    private SupportModel support;
//
//    @SerializedName("aeps")
//    @Expose
//    private List<AepsServiceModel> aeps;
//    @SerializedName("bbps")
//    @Expose
//    private List<BbpsServiceModel> bbps;
//    @SerializedName("utility")
//    @Expose
//    private List<MoneyTransferServiceModel> utility;
//
//    @SerializedName("other")
//    @Expose
//    private List<OtherServiceModel> other;
//
//    @SerializedName("banners")
//    @Expose
//    private List<BannerModel> banners;
//
//    @SerializedName("billers")
//    @Expose
//    private List<BillerModel> billers;
//
//    @SerializedName("fetchdata")
//    @Expose
//    private FetchBillModel fetchdata;
//
//    @SerializedName("wallet")
//    @Expose
//    private String wallet;
//
//    @SerializedName("addfundBanks")
//    @Expose
//    private List<AddfundBankModel> addfundBanks;
//
//    @SerializedName("bbpsrecent")
//    @Expose
//    private List<BbpsFetchBillModel> bbpsrecent;
//
//    @SerializedName("receiptData")
//    @Expose
//    private AepsReceiptData receiptData;
//
//    @SerializedName("operatorList")
//    @Expose
//    private List<RechargeOperatorModel> operatorList;
//
//    @SerializedName("planTitle")
//    @Expose
//    private List<PlanDataModel> planTitle;
//
//    @SerializedName("allPlans")
//    @Expose
//    private List<AllPlans> allPlans;
//
//
//    @SerializedName("payoutAccounts")
//    @Expose
//    private List<PayoutAccountModel> payoutAccounts;
//
//    @SerializedName("qtAccounts")
//    @Expose
//    private List<QtAccountModel> qtAccounts;
//
//    @SerializedName("qtData")
//    @Expose
//    private QtDataModel qtData;
//
//    @SerializedName("payoutData")
//    @Expose
//    private PayoutDataModel payoutData;
//
//    @SerializedName("recentQtTxn")
//    @Expose
//    private List<QuickFetchBillModel> recentQtTxn;
//
//
//    @SerializedName("globalList")
//    @Expose
//    private List<GlobalBankModel> globalList;
//
//    @SerializedName("reportItems")
//    @Expose
//    private List<ReportTypeModel> reportItems;
//
//    @SerializedName("aepsReport")
//    @Expose
//    private List<AEPSReportDetailModel> aepsReport;
//
//    @SerializedName("dmtReport")
//    @Expose
//    private List<AEPSReportDetailModel> dmtReport;
//
//    @SerializedName("qtransferReport")
//    @Expose
//    private List<AEPSReportDetailModel> qtransferReport;
//
//    @SerializedName("payoutReport")
//    @Expose
//    private List<AEPSReportDetailModel> payoutReport;
//
//    @SerializedName("rechargeReport")
//    @Expose
//    private List<AEPSReportDetailModel> rechargeReport;
//
//    @SerializedName("bbpsReport")
//    @Expose
//    private List<AEPSReportDetailModel> bbpsReport;
//
//    @SerializedName("cmsReport")
//    @Expose
//    private List<CMSReportModel> cmsReport;
//    @SerializedName("utiReport")
//    @Expose
//    private List<AEPSReportDetailModel> utiReport;
//
//    @SerializedName("walletReport")
//    @Expose
//    private List<AEPSReportDetailModel> walletReport;
//
//    @SerializedName("commssionSlab")
//    @Expose
//    private List<FaqModel> commssionSlab;
//
//    @SerializedName("walletToWalletReport")
//    @Expose
//    private List<AEPSReportDetailModel> walletToWalletReport;
//
//    @SerializedName("WalletFetchUser")
//    @Expose
//    private WalletFetchUser walletFetchUser;
//
//    @SerializedName("walletToWalletData")
//    @Expose
//    private WalletToWalletData walletToWalletData;
//
//    @SerializedName("walletToWalletRecentTxn")
//    @Expose
//    private List<WalletToWalletReportModel> walletToWalletRecentTxn;
//    @SerializedName("dmtData")
//    @Expose
//    private List<DMTBankdetailListModel> bankdetaillist;
//    @SerializedName("dmtBankList")
//    @Expose
//    private List<BankModel> dmtBankList;
//    @SerializedName("dmtProfileData")
//    @Expose
//    private DmtProfileDataModel dmtProfileData;
//
//    @SerializedName("dmtReceiptData")
//    @Expose
//    private DmtDataModel dmtReceiptData;
//
//    @SerializedName("utiSupportData")
//    @Expose
//    private UtiSupportDataModel utiSupportData;
//    @SerializedName("utiReceipt")
//    @Expose
//    private UtiReceiptModel utiReceipt;
//
//    @SerializedName("pan_details")
//    @Expose
//    private PanDetailsModel panDetailsModel;
//
//    @SerializedName("rechargeData")
//    @Expose
//    private RechargeDataRecipt rechargeData;
//    @SerializedName("recentTxn")
//    @Expose
//    private List<RechargeTransDetailModel> recentTxn;
//
//    @SerializedName("supportTypes")
//    @Expose
//    private List<SupportType> supportTypes;
//    @SerializedName("supportTickets")
//    @Expose
//    private List<SupportTicket> supportTickets;
//
//    @SerializedName("subscription_billers")
//    @Expose
//    private List<ModelSubscriptionBiller> subscriptionBillers;
//
//    @SerializedName("biller_details")
//    @Expose
//    private ModelBillerDetails billerDetails;
//
//    @SerializedName("subscription_fetch_bill")
//    @Expose
//    private ModelSubscriptionFetchBill subscriptionFetchBill;
//
//    @SerializedName("subscription_pay_bill")
//    @Expose
//    private ModelSubscriptionPayBill subscriptionPayBill;
//
//    @SerializedName("file_name")
//    @Expose
//    public String fileName;
//
//    @SerializedName("mobile")
//    @Expose
//    public String mobile;
//    @SerializedName("otpid")
//    @Expose
//    public String otpid;
//    @SerializedName("aadhar")
//    @Expose
//    public String aadhar;
//
//    public String getOtpid() {
//        return otpid;
//    }
//
//    public String getMobile() {
//        return mobile;
//    }
//
//    public String getAadhar() {
//        return aadhar;
//    }
//    public String getFileName() {
//        return fileName;
//    }
//
//    public ModelSubscriptionPayBill getSubscriptionPayBill() {
//        return subscriptionPayBill;
//    }
//
//    public ModelSubscriptionFetchBill getSubscriptionFetchBill() {
//        return subscriptionFetchBill;
//    }
//
//    public ModelBillerDetails getBillerDetails() {
//        return billerDetails;
//    }
//
//    public List<ModelSubscriptionBiller> getSubscriptionBillers() {
//        return subscriptionBillers;
//    }
//
//    public PanDetailsModel getPanDetailsModel() {
//        return panDetailsModel;
//    }
//
//    public List<RoleModel> getRoles() {
//        return roles;
//    }
//
//    public List<DeviceModel> getRdname() {
//        return rdname;
//    }
//
//    public List<BankModel> getBank() {
//        return bank;
//    }
//
//    public AccountKycStatus getAccountkycstatus() {
//        return accountkycstatus;
//    }
//
//    public List<StateModel> getState() {return state;}
//
//    public KycModel getKyc() {
//        return kyc;
//    }
//
//    public void setKyc(KycModel kyc) {
//        this.kyc = kyc;
//    }
//
//    public ModelUserInfo getProfile() {
//        return profile;
//    }
//
//    public void setProfile(ModelUserInfo profile) {
//        this.profile = profile;
//    }
//
//    public SupportModel getSupport() {
//        return support;
//    }
//
//    public List<AepsServiceModel> getAeps() {
//        return aeps;
//    }
//
//    public List<BbpsServiceModel> getBbps() {return bbps;}
//
//    public List<OtherServiceModel> getOther() {return other;}
//
//    public List<MoneyTransferServiceModel> getUtility() {
//        return utility;
//    }
//
//    public List<BannerModel> getBanners() {
//        return banners;
//    }
//
//    public List<BillerModel> getBillers() {
//        return billers;
//    }
//
//    public FetchBillModel getFetchdata() {
//        return fetchdata;
//    }
//
//    public String getWallet() {
//        return wallet;
//    }
//
//    public List<AddfundBankModel> getAddfundBanks() {
//        return addfundBanks;
//    }
//
//    public List<BbpsFetchBillModel> getBbpsrecent() {
//        return bbpsrecent;
//    }
//
//    public AepsReceiptData getReceiptData() {
//        return receiptData;
//    }
//
//    public void setReceiptData(AepsReceiptData receiptData) {
//        this.receiptData = receiptData;
//    }
//
//    public List<RechargeOperatorModel> getOperatorList() {
//        return operatorList;
//    }
//
//    public List<AllPlans> getAllPlans() {
//        return allPlans;
//    }
//
//    public List<PlanDataModel> getPlanTitle() {
//        return planTitle;
//    }
//
//    public List<PayoutAccountModel> getPayoutAccounts() {
//        return payoutAccounts;
//    }
//
//    public List<QtAccountModel> getQtAccounts() {
//        return qtAccounts;
//    }
//
//    public List<GlobalBankModel> getGlobalList() {
//        return globalList;
//    }
//
//    public QtDataModel getQtData() {
//        return qtData;
//    }
//
//    public List<QuickFetchBillModel> getRecentQtTxn() {
//        return recentQtTxn;
//    }
//
//    public PayoutDataModel getPayoutData() {
//        return payoutData;
//    }
//
//    public List<ReportTypeModel> getReportItems() {
//        return reportItems;
//    }
//
//    public List<AEPSReportDetailModel> getAepsReport() {
//        return aepsReport;
//    }
//
//    public List<AEPSReportDetailModel> getDmtReport() {
//        return dmtReport;
//    }
//
//    public List<AEPSReportDetailModel> getQtransferReport() {
//        return qtransferReport;
//    }
//
//    public List<AEPSReportDetailModel> getPayoutReport() {
//        return payoutReport;
//    }
//
//    public List<AEPSReportDetailModel> getRechargeReport() {
//        return rechargeReport;
//    }
//
//    public List<AEPSReportDetailModel> getBbpsReport() {
//        return bbpsReport;
//    }
//
//    public List<AEPSReportDetailModel> getUtiReport() {
//        return utiReport;
//    }
//
//    public List<AEPSReportDetailModel> getWalletReport() {
//        return walletReport;
//    }
//
//    public List<FaqModel> getCommssionSlab() {
//        return commssionSlab;
//    }
//
//    public List<AEPSReportDetailModel> getWalletToWalletReport() {return walletToWalletReport;}
//
//    public WalletFetchUser getWalletFetchUser() {
//        return walletFetchUser;
//    }
//
//    public WalletToWalletData getWalletToWalletData() {
//        return walletToWalletData;
//    }
//
//    public List<WalletToWalletReportModel> getWalletToWalletRecentTxn() {
//        return walletToWalletRecentTxn;
//    }
//    public List<DMTBankdetailListModel> getBankdetaillist() {
//        return bankdetaillist;
//    }
//
//    public List<BankModel> getDmtBankList() {
//        return dmtBankList;
//    }
//
//    public DmtProfileDataModel getDmtProfileData() {
//        return dmtProfileData;
//    }
//
//    public DmtDataModel getDmtReceiptData() {
//        return dmtReceiptData;
//    }
//
//    public UtiSupportDataModel getUtiSupportData() {
//        return utiSupportData;
//    }
//
//    public UtiReceiptModel getUtiReceipt() {
//        return utiReceipt;
//    }
//
//    public RechargeDataRecipt getRechargeData() {
//        return rechargeData;
//    }
//
//    public List<RechargeTransDetailModel> getRecentTxn() {
//        return recentTxn;
//    }
//
//    public List<SupportType> getSupportTypes() {
//        return supportTypes;
//    }
//
//    public List<SupportTicket> getSupportTickets() {
//        return supportTickets;
//    }
//
//    public List<CMSReportModel> getCmsReport() {
//        return cmsReport;
//    }
//
//    public void setCmsReport(List<CMSReportModel> cmsReport) {
//        this.cmsReport = cmsReport;
//    }
//}
