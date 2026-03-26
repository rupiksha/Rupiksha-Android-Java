package com.app.rupiksha.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BankListModel
{
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("catkey")
    @Expose
    private String catkey;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("viewbill")
    @Expose
    private String viewbill;
    @SerializedName("displayname")
    @Expose
    private String displayname;
    @SerializedName("ad1dname")
    @Expose
    private Object ad1dname;
    @SerializedName("ad1name")
    @Expose
    private Object ad1name;
    @SerializedName("ad2dname")
    @Expose
    private Object ad2dname;
    @SerializedName("ad2name")
    @Expose
    private Object ad2name;
    @SerializedName("ad3dname")
    @Expose
    private Object ad3dname;
    @SerializedName("ad3name")
    @Expose
    private Object ad3name;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCatkey() {
        return catkey;
    }

    public void setCatkey(String catkey) {
        this.catkey = catkey;
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

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public Object getAd1dname() {
        return ad1dname;
    }

    public void setAd1dname(Object ad1dname) {
        this.ad1dname = ad1dname;
    }

    public Object getAd1name() {
        return ad1name;
    }

    public void setAd1name(Object ad1name) {
        this.ad1name = ad1name;
    }

    public Object getAd2dname() {
        return ad2dname;
    }

    public void setAd2dname(Object ad2dname) {
        this.ad2dname = ad2dname;
    }

    public Object getAd2name() {
        return ad2name;
    }

    public void setAd2name(Object ad2name) {
        this.ad2name = ad2name;
    }

    public Object getAd3dname() {
        return ad3dname;
    }

    public void setAd3dname(Object ad3dname) {
        this.ad3dname = ad3dname;
    }

    public Object getAd3name() {
        return ad3name;
    }

    public void setAd3name(Object ad3name) {
        this.ad3name = ad3name;
    }
}
