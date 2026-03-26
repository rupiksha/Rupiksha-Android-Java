package com.app.rupiksha.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelUserInfo {

    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("mobile")
    @Expose
    private String mobile;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("profile")
    @Expose
    private String profile;

    @SerializedName("itr_register")
    @Expose
    public String itrRegister;
    @SerializedName("itr_login")
    @Expose
    public String itrLogin;

    @SerializedName("outlet")
    @Expose
    public String outlet;

    public String getOutlet() {
        return outlet;
    }

    public String getItrRegister() {
        return itrRegister;
    }

    public void setItrRegister(String itrRegister) {
        this.itrRegister = itrRegister;
    }

    public String getItrLogin() {
        return itrLogin;
    }

    public void setItrLogin(String itrLogin) {
        this.itrLogin = itrLogin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
