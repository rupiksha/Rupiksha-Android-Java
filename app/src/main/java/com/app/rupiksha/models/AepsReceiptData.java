package com.app.rupiksha.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AepsReceiptData
{
    @SerializedName("uid")
    @Expose
    private int uid;
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
    private String txnamount;

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
    @SerializedName("response")
    @Expose
    private String response;
    @SerializedName("ministatement")
    @Expose
    private List<Ministatement> ministatement;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("support")
    @Expose
    private String support;


    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
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

    public List<Ministatement> getMinistatement() {
        return ministatement;
    }

    public void setMinistatement(List<Ministatement> ministatement) {
        this.ministatement = ministatement;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSupport() {
        return support;
    }

    public void setSupport(String support) {
        this.support = support;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTxnamount() {
        return txnamount;
    }

    public void setTxnamount(String txnamount) {
        this.txnamount = txnamount;
    }
}
