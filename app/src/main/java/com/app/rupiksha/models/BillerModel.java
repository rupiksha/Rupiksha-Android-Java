package com.app.rupiksha.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BillerModel
{
    @SerializedName("id")
    @Expose
    private int id;
//    @SerializedName("biller_name")
   @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("category")
    @Expose
    private String category;
//    @SerializedName("fetch_bill")
    @SerializedName("viewbill")
    @Expose
    private String viewbill;
    @SerializedName("regex")
    @Expose
    private String regex;
    @SerializedName("displayname")
    @Expose
    private String displayname;
    @SerializedName("ad1_d_name")
    @Expose
    private String ad1DName;
    @SerializedName("ad1_name")
    @Expose
    private String ad1Name;
    @SerializedName("ad1_regex")
    @Expose
    private String ad1Regex;
    @SerializedName("ad2_d_name")
    @Expose
    private String ad2DName;
    @SerializedName("ad2_name")
    @Expose
    private String ad2Name;
    @SerializedName("ad2_regex")
    @Expose
    private String ad2Regex;
    @SerializedName("ad3_d_name")
    @Expose
    private String ad3DName;
    @SerializedName("ad3_name")
    @Expose
    private String ad3Name;
    @SerializedName("ad3_regex")
    @Expose
    private String ad3Regex;
    @SerializedName("catkey")
    @Expose
    private String catkey;
    @SerializedName("type")
    @Expose
    private String type;
//
@SerializedName("param1")
@Expose
private String param1;
    @SerializedName("param1_name")
    @Expose
    private String param1_name;
    @SerializedName("param1_type")
    @Expose
    private String param1_type;

    @SerializedName("param2")
    @Expose
    private String param2;

    public String getOperator_id() {
        return operator_id;
    }

    public void setOperator_id(String operator_id) {
        this.operator_id = operator_id;
    }

    @SerializedName("operator_id")
    @Expose
    private String operator_id;

    public String getParam2_name() {
        return param2_name;
    }

    public void setParam2_name(String param2_name) {
        this.param2_name = param2_name;
    }

    public String getParam2() {
        return param2;
    }

    public void setParam2(String param2) {
        this.param2 = param2;
    }

    public String getParam2_type() {
        return param2_type;
    }

    public void setParam2_type(String param2_type) {
        this.param2_type = param2_type;
    }

    @SerializedName("param2_name")
    @Expose
    private String param2_name;
    @SerializedName("param2_type")
    @Expose
    private String param2_type;

    public String getFetchBill() {
        return fetchBill;
    }

    public void setFetchBill(String fetchBill) {
        this.fetchBill = fetchBill;
    }

    @SerializedName("fetchBill")
    @Expose
    private String fetchBill;
    public String getParam2_regex() {
        return param2_regex;
    }

    public void setParam2_regex(String param2_regex) {
        this.param2_regex = param2_regex;
    }

    @SerializedName("param2_regex")
    @Expose
    private String param2_regex;
    public String getParam1_regex() {
        return param1_regex;
    }

    public void setParam1_regex(String param1_regex) {
        this.param1_regex = param1_regex;
    }

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }

    public String getParam1_name() {
        return param1_name;
    }

    public void setParam1_name(String param1_name) {
        this.param1_name = param1_name;
    }

    public String getParam1_type() {
        return param1_type;
    }

    public void setParam1_type(String param1_type) {
        this.param1_type = param1_type;
    }

    @SerializedName("param1_regex")
    @Expose
    private String param1_regex;

//    @SerializedName("param1_type")
//    @Expose
//    private String displayType;
//    @SerializedName("param2_type")
//    @Expose
//    private String ad1Type;
//    @SerializedName("param3_type")
//    @Expose
//    private String ad2Type;
//    @SerializedName("param4_type")
//    @Expose
//    private String ad3Type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getViewbill() {
        return viewbill;
    }

    public void setViewbill(String viewbill) {
        this.viewbill = viewbill;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public String getAd1DName() {
        return ad1DName;
    }

    public void setAd1DName(String ad1DName) {
        this.ad1DName = ad1DName;
    }

    public String getAd1Name() {
        return ad1Name;
    }

    public void setAd1Name(String ad1Name) {
        this.ad1Name = ad1Name;
    }

    public String getAd1Regex() {
        return ad1Regex;
    }

    public void setAd1Regex(String ad1Regex) {
        this.ad1Regex = ad1Regex;
    }

    public String getAd2DName() {
        return ad2DName;
    }

    public void setAd2DName(String ad2DName) {
        this.ad2DName = ad2DName;
    }

    public String getAd2Name() {
        return ad2Name;
    }

    public void setAd2Name(String ad2Name) {
        this.ad2Name = ad2Name;
    }

    public String getAd2Regex() {
        return ad2Regex;
    }

    public void setAd2Regex(String ad2Regex) {
        this.ad2Regex = ad2Regex;
    }

    public String getAd3DName() {
        return ad3DName;
    }

    public void setAd3DName(String ad3DName) {
        this.ad3DName = ad3DName;
    }

    public String getAd3Name() {
        return ad3Name;
    }

    public void setAd3Name(String ad3Name) {
        this.ad3Name = ad3Name;
    }

    public String getAd3Regex() {
        return ad3Regex;
    }

    public void setAd3Regex(String ad3Regex) {
        this.ad3Regex = ad3Regex;
    }

    public String getCatkey() {
        return catkey;
    }

    public void setCatkey(String catkey) {
        this.catkey = catkey;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

//    public String getDisplayType() {
//        return displayType;
//    }

//    public void setDisplayType(String displayType) {
//        this.displayType = displayType;
//    }
//
//    public String getAd1Type() {
//        return ad1Type;
//    }
//
//    public void setAd1Type(String ad1Type) {
//        this.ad1Type = ad1Type;
//    }
//
//    public String getAd2Type() {
//        return ad2Type;
//    }
//
//    public void setAd2Type(String ad2Type) {
//        this.ad2Type = ad2Type;
//    }
//
//    public String getAd3Type() {
//        return ad3Type;
//    }
//
//    public void setAd3Type(String ad3Type) {
//        this.ad3Type = ad3Type;
//    }
}
