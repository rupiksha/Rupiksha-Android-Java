package com.app.rupiksha.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UtiSupportDataModel
{
    @SerializedName("supportmail")
    @Expose
    private String supportmail;
    @SerializedName("supportphone")
    @Expose
    private String supportphone;


    public String getSupportmail() {
        return supportmail;
    }

    public void setSupportmail(String supportmail) {
        this.supportmail = supportmail;
    }

    public String getSupportphone() {
        return supportphone;
    }

    public void setSupportphone(String supportphone) {
        this.supportphone = supportphone;
    }
}
