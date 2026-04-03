package com.app.rupiksha.models;
//
//import com.google.gson.annotations.Expose;
//import com.google.gson.annotations.SerializedName;
//
//public class ModelBillerDetails {
//
//    @SerializedName("id")
//    @Expose
//    private Integer id;
//    @SerializedName("b_id")
//    @Expose
//    private Integer bId;
//    @SerializedName("billerId")
//    @Expose
//    private String billerId;
//    @SerializedName("biller_name")
//    @Expose
//    private String billerName;
//    @SerializedName("category_key")
//    @Expose
//    private String categoryKey;
//    @SerializedName("type")
//    @Expose
//    private String type;
//    @SerializedName("category_name")
//    @Expose
//    private String categoryName;
//    @SerializedName("coverage_city")
//    @Expose
//    private String coverageCity;
//    @SerializedName("coverage_state")
//    @Expose
//    private String coverageState;
//    @SerializedName("coverage_pincode")
//    @Expose
//    private Integer coveragePincode;
//    @SerializedName("biller_status")
//    @Expose
//    private String billerStatus;
//    @SerializedName("is_available")
//    @Expose
//    private Integer isAvailable;
//    @SerializedName("fetch_bill")
//    @Expose
//    private Integer fetchBill;
//    @SerializedName("icon_url")
//    @Expose
//    private String iconUrl;
//    @SerializedName("param1")
//    @Expose
//    private String param1;
//    @SerializedName("param1_mandatory")
//    @Expose
//    private Integer param1Mandatory;
//    @SerializedName("param1_regex")
//    @Expose
//    private String param1Regex;
//    @SerializedName("param2")
//    @Expose
//    private String param2;
//    @SerializedName("param2_mandatory")
//    @Expose
//    private Integer param2Mandatory;
//    @SerializedName("param2_regex")
//    @Expose
//    private String param2Regex;
//    @SerializedName("param3")
//    @Expose
//    private String param3;
//    @SerializedName("param3_mandatory")
//    @Expose
//    private Integer param3Mandatory;
//    @SerializedName("param3_regex")
//    @Expose
//    private String param3Regex;
//    @SerializedName("param4")
//    @Expose
//    private String param4;
//    @SerializedName("param4_mandatory")
//    @Expose
//    private Integer param4Mandatory;
//    @SerializedName("param4_regex")
//    @Expose
//    private String param4Regex;
//    @SerializedName("created_at")
//    @Expose
//    private String createdAt;
//    @SerializedName("updated_at")
//    @Expose
//    private String updatedAt;
//
//    public Integer getId() {
//        return id;
//    }
//
//    public void setId(Integer id) {
//        this.id = id;
//    }
//
//    public Integer getbId() {
//        return bId;
//    }
//
//    public void setbId(Integer bId) {
//        this.bId = bId;
//    }
//
//    public String getBillerId() {
//        return billerId;
//    }
//
//    public void setBillerId(String billerId) {
//        this.billerId = billerId;
//    }
//
//    public String getBillerName() {
//        return billerName;
//    }
//
//    public void setBillerName(String billerName) {
//        this.billerName = billerName;
//    }
//
//    public String getCategoryKey() {
//        return categoryKey;
//    }
//
//    public void setCategoryKey(String categoryKey) {
//        this.categoryKey = categoryKey;
//    }
//
//    public String getType() {
//        return type;
//    }
//
//    public void setType(String type) {
//        this.type = type;
//    }
//
//    public String getCategoryName() {
//        return categoryName;
//    }
//
//    public void setCategoryName(String categoryName) {
//        this.categoryName = categoryName;
//    }
//
//    public String getCoverageCity() {
//        return coverageCity;
//    }
//
//    public void setCoverageCity(String coverageCity) {
//        this.coverageCity = coverageCity;
//    }
//
//    public String getCoverageState() {
//        return coverageState;
//    }
//
//    public void setCoverageState(String coverageState) {
//        this.coverageState = coverageState;
//    }
//
//    public Integer getCoveragePincode() {
//        return coveragePincode;
//    }
//
//    public void setCoveragePincode(Integer coveragePincode) {
//        this.coveragePincode = coveragePincode;
//    }
//
//    public String getBillerStatus() {
//        return billerStatus;
//    }
//
//    public void setBillerStatus(String billerStatus) {
//        this.billerStatus = billerStatus;
//    }
//
//    public Integer getIsAvailable() {
//        return isAvailable;
//    }
//
//    public void setIsAvailable(Integer isAvailable) {
//        this.isAvailable = isAvailable;
//    }
//
//    public Integer getFetchBill() {
//        return fetchBill;
//    }
//
//    public void setFetchBill(Integer fetchBill) {
//        this.fetchBill = fetchBill;
//    }
//
//    public String getIconUrl() {
//        return iconUrl;
//    }
//
//    public void setIconUrl(String iconUrl) {
//        this.iconUrl = iconUrl;
//    }
//
//    public String getParam1() {
//        return param1;
//    }
//
//    public void setParam1(String param1) {
//        this.param1 = param1;
//    }
//
//    public Integer getParam1Mandatory() {
//        return param1Mandatory;
//    }
//
//    public void setParam1Mandatory(Integer param1Mandatory) {
//        this.param1Mandatory = param1Mandatory;
//    }
//
//    public String getParam1Regex() {
//        return param1Regex;
//    }
//
//    public void setParam1Regex(String param1Regex) {
//        this.param1Regex = param1Regex;
//    }
//
//    public String getParam2() {
//        return param2;
//    }
//
//    public void setParam2(String param2) {
//        this.param2 = param2;
//    }
//
//    public Integer getParam2Mandatory() {
//        return param2Mandatory;
//    }
//
//    public void setParam2Mandatory(Integer param2Mandatory) {
//        this.param2Mandatory = param2Mandatory;
//    }
//
//    public String getParam2Regex() {
//        return param2Regex;
//    }
//
//    public void setParam2Regex(String param2Regex) {
//        this.param2Regex = param2Regex;
//    }
//
//    public String getParam3() {
//        return param3;
//    }
//
//    public void setParam3(String param3) {
//        this.param3 = param3;
//    }
//
//    public Integer getParam3Mandatory() {
//        return param3Mandatory;
//    }
//
//    public void setParam3Mandatory(Integer param3Mandatory) {
//        this.param3Mandatory = param3Mandatory;
//    }
//
//    public String getParam3Regex() {
//        return param3Regex;
//    }
//
//    public void setParam3Regex(String param3Regex) {
//        this.param3Regex = param3Regex;
//    }
//
//    public String getParam4() {
//        return param4;
//    }
//
//    public void setParam4(String param4) {
//        this.param4 = param4;
//    }
//
//    public Integer getParam4Mandatory() {
//        return param4Mandatory;
//    }
//
//    public void setParam4Mandatory(Integer param4Mandatory) {
//        this.param4Mandatory = param4Mandatory;
//    }
//
//    public Object getParam4Regex() {
//        return param4Regex;
//    }
//
//    public void setParam4Regex(String param4Regex) {
//        this.param4Regex = param4Regex;
//    }
//
//    public String getCreatedAt() {
//        return createdAt;
//    }
//
//    public void setCreatedAt(String createdAt) {
//        this.createdAt = createdAt;
//    }
//
//    public String getUpdatedAt() {
//        return updatedAt;
//    }
//
//    public void setUpdatedAt(String updatedAt) {
//        this.updatedAt = updatedAt;
//    }
//}
