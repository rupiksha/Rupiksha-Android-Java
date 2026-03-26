package com.app.rupiksha.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BbpsFetchBillModel
{
    @SerializedName("txnid")
    @Expose
    private String txnid;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("amount")
    @Expose
    private int amount;
    @SerializedName("canumber")
    @Expose
    private String canumber;
    @SerializedName("boperator")
    @Expose
    private String boperator;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("operatorName")
    @Expose
    private String operatorName;
    @SerializedName("displayname")
    @Expose
    private String displayname;


    public String getTxnid() {
        return txnid;
    }

    public String getStatus() {
        return status;
    }

    public int getAmount() {
        return amount;
    }

    public String getCanumber() {
        return canumber;
    }

    public String getBoperator() {
        return boperator;
    }

    public String getDate() {
        return date;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public String getDisplayname() {
        return displayname;
    }
}
