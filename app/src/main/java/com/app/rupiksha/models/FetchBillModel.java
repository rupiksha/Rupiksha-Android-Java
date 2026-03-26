package com.app.rupiksha.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FetchBillModel
{
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("paramname")
    @Expose
    private String paramname;
    @SerializedName("baseparam")
    @Expose
    private String paramvalue;
    @SerializedName("item1")
    @Expose
    private String item1;
    @SerializedName("item1value")
    @Expose
    private String item1value;
    @SerializedName("item2")
    @Expose
    private String item2;
    @SerializedName("item2value")
    @Expose
    private String item2value;
    @SerializedName("item3")
    @Expose
    private String item3;
    @SerializedName("item3value")
    @Expose
    private String item3value;
    @SerializedName("support")
    @Expose
    private String support;
    @SerializedName("skey")
    @Expose
    private String skey;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParamname() {
        return paramname;
    }

    public void setParamname(String paramname) {
        this.paramname = paramname;
    }

    public String getParamvalue() {
        return paramvalue;
    }

    public void setParamvalue(String paramvalue) {
        this.paramvalue = paramvalue;
    }

    public String getItem1() {
        return item1;
    }

    public void setItem1(String item1) {
        this.item1 = item1;
    }

    public String getItem1value() {
        return item1value;
    }

    public void setItem1value(String item1value) {
        this.item1value = item1value;
    }

    public String getItem2() {
        return item2;
    }

    public void setItem2(String item2) {
        this.item2 = item2;
    }

    public String getItem2value() {
        return item2value;
    }

    public void setItem2value(String item2value) {
        this.item2value = item2value;
    }

    public String getItem3() {
        return item3;
    }

    public void setItem3(String item3) {
        this.item3 = item3;
    }

    public String getItem3value() {
        return item3value;
    }

    public void setItem3value(String item3value) {
        this.item3value = item3value;
    }

    public String getSupport() {
        return support;
    }

    public void setSupport(String support) {
        this.support = support;
    }
}
