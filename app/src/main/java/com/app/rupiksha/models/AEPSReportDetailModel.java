package com.app.rupiksha.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AEPSReportDetailModel
{

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("uid")
    @Expose
    private String uid;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("txnid")
    @Expose
    private String txnid;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("txnamount")
    @Expose
    private Object txnamount;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("aadhar")
    @Expose
    private String aadhar;
    @SerializedName("rrn")
    @Expose
    private String rrn;
    @SerializedName("bank")
    @Expose
    private String bank;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("operator")
    @Expose
    private  String operator;
    @SerializedName("response")
    @Expose
    private String response;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("tempid")
    @Expose
    private String tempid;
    @SerializedName("account")
    @Expose
    private String account;
    @SerializedName("ifsc")
    @Expose
    private String ifsc;
    @SerializedName("bname")
    @Expose
    private String bname;
    @SerializedName("mode")
    @Expose
    private String mode;
    @SerializedName("utr")
    @Expose
    private String utr;
    @SerializedName("req")
    @Expose
    private String req;
    @SerializedName("canumber")
    @Expose
    private String canumber;
    @SerializedName("boperator")
    @Expose
    private String boperator;
    @SerializedName("displayName")
    @Expose
    private String displayName;
    @SerializedName("qty")
    @Expose
    private String qty;
    @SerializedName("txn_uid")
    @Expose
    private Object txnUid;
    @SerializedName("txntype")
    @Expose
    private String txntype;
    @SerializedName("remark")
    @Expose
    private String remark;
    @SerializedName("opening")
    @Expose
    private String opening;
    @SerializedName("closing")
    @Expose
    private String closing;
    @SerializedName("senderName")
    @Expose
    private String senderName;
    @SerializedName("senderPhone")
    @Expose
    private String senderPhone;
    @SerializedName("receiverName")
    @Expose
    private String receiverName;
    @SerializedName("receiverphone")
    @Expose
    private String receiverphone;

    private boolean progressDelay=false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTxnid() {
        return txnid;
    }

    public void setTxnid(String txnid) {
        this.txnid = txnid;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Object getTxnamount() {
        return txnamount;
    }

    public void setTxnamount(Object txnamount) {
        this.txnamount = txnamount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAadhar() {
        return aadhar;
    }

    public void setAadhar(String aadhar) {
        this.aadhar = aadhar;
    }

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getTempid() {
        return tempid;
    }

    public void setTempid(String tempid) {
        this.tempid = tempid;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getIfsc() {
        return ifsc;
    }

    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getUtr() {
        return utr;
    }

    public void setUtr(String utr) {
        this.utr = utr;
    }

    public String getReq() {
        return req;
    }

    public void setReq(String req) {
        this.req = req;
    }

    public String getCanumber() {
        return canumber;
    }

    public void setCanumber(String canumber) {
        this.canumber = canumber;
    }

    public String getBoperator() {
        return boperator;
    }

    public void setBoperator(String boperator) {
        this.boperator = boperator;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public Object getTxnUid() {
        return txnUid;
    }

    public void setTxnUid(Object txnUid) {
        this.txnUid = txnUid;
    }

    public String getTxntype() {
        return txntype;
    }

    public void setTxntype(String txntype) {
        this.txntype = txntype;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOpening() {
        return opening;
    }

    public void setOpening(String opening) {
        this.opening = opening;
    }

    public String getClosing() {
        return closing;
    }

    public void setClosing(String closing) {
        this.closing = closing;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderPhone() {
        return senderPhone;
    }

    public void setSenderPhone(String senderPhone) {
        this.senderPhone = senderPhone;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverphone() {
        return receiverphone;
    }

    public void setReceiverphone(String receiverphone) {
        this.receiverphone = receiverphone;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public boolean isProgressDelay() {
        return progressDelay;
    }

    public void setProgressDelay(boolean progressDelay) {
        this.progressDelay = progressDelay;
    }
}
