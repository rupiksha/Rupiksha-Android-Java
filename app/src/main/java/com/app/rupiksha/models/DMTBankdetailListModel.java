package com.app.rupiksha.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DMTBankdetailListModel
{
    @SerializedName("bank_recipient_id")
    @Expose
    private String beneId;
    @SerializedName("bankid")
    @Expose
    private String bankid;
    @SerializedName("bank")
    @Expose
    private String bankname;
    @SerializedName("recipient_name")
    @Expose
    private String name;
    @SerializedName("recipient_mobile")
    @Expose
    private String recipient_mobile;
    @SerializedName("account")
    @Expose
    private String accno;
    @SerializedName("ifsc")
    @Expose
    private String ifsc;
    @SerializedName("verified")
    @Expose
    private String verified;
    @SerializedName("banktype")
    @Expose
    private String banktype;
    @SerializedName("paytm")
    @Expose
    private Boolean paytm;

    public String getBeneId() {
        return beneId;
    }

    public void setBeneId(String beneId) {
        this.beneId = beneId;
    }

    public String getBankid() {
        return bankid;
    }

    public void setBankid(String bankid) {
        this.bankid = bankid;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccno() {
        return accno;
    }

    public void setAccno(String accno) {
        this.accno = accno;
    }

    public String getIfsc() {
        return ifsc;
    }

    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    public String getBanktype() {
        return banktype;
    }

    public void setBanktype(String banktype) {
        this.banktype = banktype;
    }

    public Boolean getPaytm() {
        return paytm;
    }

    public void setPaytm(Boolean paytm) {
        this.paytm = paytm;
    }
}
