package com.app.rupiksha.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MainData(
    @SerializedName("roles")
    @Expose
    val roles: List<RoleModel>? = null,

    @SerializedName("rdname")
    @Expose
    val rdname: List<DeviceModel>? = null,

    @SerializedName("bank")
    @Expose
    val bank: List<BankModel>? = null,

    @SerializedName("state")
    @Expose
    val state: List<StateModel>? = null,

    @SerializedName("address_proof_list")
    @Expose
    val addressProofList: List<AddressProof>? = null,

    @SerializedName("accountkycstatus")
    @Expose
    val accountkycstatus: AccountKycStatus? = null,

    @SerializedName("kyc")
    @Expose
    val kyc: KycModel? = null,

    @SerializedName("profile")
    @Expose
    val profile: ModelUserInfo? = null,

    @SerializedName("support")
    @Expose
    val support: SupportModel? = null,

    @SerializedName("aeps")
    @Expose
    val aeps: List<AepsServiceModel>? = null,
    
    @SerializedName("bbps")
    @Expose
    val bbps: List<BbpsServiceModel>? = null,
    
    @SerializedName("utility")
    @Expose
    val utility: List<MoneyTransferServiceModel>? = null,

    @SerializedName("wallet")
    @Expose
    val wallet: String? = null,

    @SerializedName("receiptData")
    @Expose
    val receiptData: AepsReceiptData? = null,

    @SerializedName("payoutAccounts")
    @Expose
    val payoutAccounts: List<PayoutAccountModel>? = null,

    @SerializedName("supportTypes")
    @Expose
    val supportTypes: List<SupportType>? = null,
    
    @SerializedName("supportTickets")
    @Expose
    val supportTickets: List<SupportTicket>? = null,

    @SerializedName("file_name")
    @Expose
    val fileName: String? = null,

    @SerializedName("mobile")
    @Expose
    val mobile: String? = null,
    
    @SerializedName("otpid")
    @Expose
    val otpid: String? = null,
    
    @SerializedName("aadhar")
    @Expose
    val aadhar: String? = null,
    
    @SerializedName("pan_details")
    @Expose
    val panDetailsModel: PanDetailsModel? = null,
    
    @SerializedName("utiReceipt")
    @Expose
    val utiReceipt: UtiReceiptModel? = null,
    
    @SerializedName("utiSupportData")
    @Expose
    val utiSupportData: UtiSupportDataModel? = null,
    
    @SerializedName("addfundBanks")
    @Expose
    val addfundBanks: List<AddfundBankModel>? = null,
    
    @SerializedName("walletToWalletReport")
    @Expose
    val walletToWalletReport: List<AEPSReportDetailModel>? = null,

    @SerializedName("walletFetchUser")
    @Expose
    val walletFetchUser: WalletFetchUser? = null,

    @SerializedName("bankdetaillist")
    @Expose
    val bankdetaillist: List<DMTBankdetailListModel>? = null,

    @SerializedName("dmtReceiptData")
    @Expose
    val dmtReceiptData: DmtDataModel? = null,

    @SerializedName("operatorList")
    @Expose
    val operatorList: List<RechargeOperatorModel>? = null,

    @SerializedName("planTitle")
    @Expose
    val planTitle: List<PlanDataModel>? = null,

    @SerializedName("allPlans")
    @Expose
    val allPlans: List<AllPlans>? = null,

    @SerializedName("rechargeData")
    @Expose
    val rechargeData: RechargeDataRecipt? = null,

    @SerializedName("recentTxn")
    @Expose
    val recentTxn: List<RechargeTransDetailModel>? = null,

    @SerializedName("fetchdata")
    @Expose
    val fetchdata: FetchBillModel? = null,

    @SerializedName("bbpsrecent")
    @Expose
    val bbpsrecent: List<BbpsFetchBillModel>? = null,

    @SerializedName("billers")
    @Expose
    val billers: List<BillerModel>? = null,

    @SerializedName("bbpsreport")
    @Expose
    val bbpsreport: List<BbpsReportModel>? = null,

    @SerializedName("qtAccounts")
    @Expose
    val qtAccounts: List<QtAccountModel>? = null,

    @SerializedName("globalList")
    @Expose
    val globalList: List<GlobalBankModel>? = null,

    @SerializedName("reportItems")
    @Expose
    val reportItems: List<ReportTypeModel>? = null,

    @SerializedName("commssionSlab")
    @Expose
    val commssionSlab: List<FaqModel>? = null,

    @SerializedName("aepsReport")
    @Expose
    val aepsReport: List<AEPSReportDetailModel>? = null,

    @SerializedName("dmtReport")
    @Expose
    val dmtReport: List<AEPSReportDetailModel>? = null,

    @SerializedName("qtransferReport")
    @Expose
    val qtransferReport: List<AEPSReportDetailModel>? = null,

    @SerializedName("payoutReport")
    @Expose
    val payoutReport: List<AEPSReportDetailModel>? = null,

    @SerializedName("rechargeReport")
    @Expose
    val rechargeReport: List<AEPSReportDetailModel>? = null,

    @SerializedName("bbpsReport")
    @Expose
    val bbpsReport: List<AEPSReportDetailModel>? = null,

    @SerializedName("utiReport")
    @Expose
    val utiReport: List<AEPSReportDetailModel>? = null,

    @SerializedName("walletReport")
    @Expose
    val walletReport: List<AEPSReportDetailModel>? = null
)
