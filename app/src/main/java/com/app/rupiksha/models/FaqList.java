package com.app.rupiksha.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FaqList
{
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("package")
    @Expose
    private String _package;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("percent")
    @Expose
    private String percent;
    @SerializedName("froma")
    @Expose
    private String froma;
    @SerializedName("toa")
    @Expose
    private String toa;
    @SerializedName("commission")
    @Expose
    private String commission;
    @SerializedName("operator")
    @Expose
    private String operator;
    @SerializedName("opid")
    @Expose
    private String opid;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String get_package() {
        return _package;
    }

    public void set_package(String _package) {
        this._package = _package;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public String getFroma() {
        return froma;
    }

    public void setFroma(String froma) {
        this.froma = froma;
    }

    public String getToa() {
        return toa;
    }

    public void setToa(String toa) {
        this.toa = toa;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOpid() {
        return opid;
    }

    public void setOpid(String opid) {
        this.opid = opid;
    }
}
