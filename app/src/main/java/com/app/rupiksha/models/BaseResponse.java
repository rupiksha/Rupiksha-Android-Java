package com.app.rupiksha.models;
//
//import com.google.gson.annotations.Expose;
//import com.google.gson.annotations.SerializedName;
//
//public class BaseResponse
//{
//    @SerializedName("status")
//    @Expose
//    private String status;
//    @SerializedName("error")
//    @Expose
//    private boolean error;
//    @SerializedName("message")
//    @Expose
//    private String message;
//    @SerializedName("mode")
//    @Expose
//    private String mode;
//    @SerializedName("log_key")
//    @Expose
//    private String logKey;
//    @SerializedName("data")
//    @Expose
//    private MainData data;
//    @SerializedName("header_token")
//    @Expose
//    private String headerToken;
//    @SerializedName("header_key")
//    @Expose
//    private String headerKey;
//    @SerializedName("news")
//    @Expose
//    private String news;
//    @SerializedName("walletBalance")
//    @Expose
//    private double walletBalance;
//    @SerializedName("skey")
//    @Expose
//    private String skey;
//    @SerializedName("aepsStatus")
//    @Expose
//    private String aepsStatus;
//    @SerializedName("tfa")
//    @Expose
//    private boolean tfa;
//    @SerializedName("aptfa")
//    @Expose
//    private boolean aptfa;
//    @SerializedName("id")
//    @Expose
//    private int id;
//    @SerializedName("operator")
//    @Expose
//    private String operator;
//    @SerializedName("circle")
//    @Expose
//    private String circle;
//    @SerializedName("txn_key")
//    @Expose
//    private String txnKey;
//    @SerializedName("dmtKey")
//    @Expose
//    private String dmtKey;
//    @SerializedName("activity")
//    @Expose
//    private String activity;
//    @SerializedName("mobile")
//    @Expose
//    private String mobile;
//    @SerializedName("name")
//    @Expose
//    private String name;
//    @SerializedName("remainingLimit")
//    @Expose
//    private String remainingLimit;
//    @SerializedName("utiStatus")
//    @Expose
//    private String utiStatus;
//    @SerializedName("psaid")
//    @Expose
//    private String psaid;
//    @SerializedName("couponCharge")
//    @Expose
//    private double couponCharge;
//    @SerializedName("total_record")
//    @Expose
//    private int totalRecord;
//
//    @SerializedName("type")
//    @Expose
//    private String type;
//
//    @SerializedName("key")
//    @Expose
//    private Integer key;
//
//    @SerializedName("msg")
//    @Expose
//    private String msg;
//
//    @SerializedName("url")
//    @Expose
//    private String url;
//
//
//    public String getUrl() {
//        return url;
//    }
//
//    public void setUrl(String url) {
//        this.url = url;
//    }
//
//    public String getMsg() {
//        return msg;
//    }
//
//    public void setMsg(String msg) {
//        this.msg = msg;
//    }
//
//    public Integer getKey() {
//        return key;
//    }
//
//    public void setKey(Integer key) {
//        this.key = key;
//    }
//
//    public String getType() {return type;}
//
//    public void setType(String type) {this.type = type;}
//
//    public String getMobile() {
//        return mobile;
//    }
//
//    public void setMobile(String mobile) {
//        this.mobile = mobile;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getRemainingLimit() {
//        return remainingLimit;
//    }
//
//    public void setRemainingLimit(String remainingLimit) {
//        this.remainingLimit = remainingLimit;
//    }
//
//    public String getDmtKey() {
//        return dmtKey;
//    }
//
//    public void setDmtKey(String dmtKey) {
//        this.dmtKey = dmtKey;
//    }
//
//    public String getActivity() {
//        return activity;
//    }
//
//    public void setActivity(String activity) {
//        this.activity = activity;
//    }
//
//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
//
//    public boolean isError() {
//        return error;
//    }
//
//    public void setError(boolean error) {
//        this.error = error;
//    }
//
//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }
//
//    public MainData getData() {
//        return data;
//    }
//
//    public void setData(MainData data) {
//        this.data = data;
//    }
//
//    public String getMode() {
//        return mode;
//    }
//
//    public void setMode(String mode) {
//        this.mode = mode;
//    }
//
//    public String getLogKey() {
//        return logKey;
//    }
//
//    public void setLogKey(String logKey) {
//        this.logKey = logKey;
//    }
//
//    public String getHeaderToken() {
//        return headerToken;
//    }
//
//    public void setHeaderToken(String headerToken) {
//        this.headerToken = headerToken;
//    }
//
//    public String getHeaderKey() {
//        return headerKey;
//    }
//
//    public void setHeaderKey(String headerKey) {
//        this.headerKey = headerKey;
//    }
//
//    public String getNews() {
//        return news;
//    }
//
//    public void setNews(String news) {
//        this.news = news;
//    }
//
//    public double getWalletBalance() {
//        return walletBalance;
//    }
//
//    public void setWalletBalance(double walletBalance) {
//        this.walletBalance = walletBalance;
//    }
//
//    public String getSkey() {
//        return skey;
//    }
//
//    public void setSkey(String skey) {
//        this.skey = skey;
//    }
//
//    public String getAepsStatus() {
//        return aepsStatus;
//    }
//
//    public void setAepsStatus(String aepsStatus) {
//        this.aepsStatus = aepsStatus;
//    }
//
//    public boolean isTfa() {
//        return tfa;
//    }
//
//    public void setTfa(boolean tfa) {
//        this.tfa = tfa;
//    }
//
//    public boolean isAptfa() {
//        return aptfa;
//    }
//
//    public void setAptfa(boolean aptfa) {
//        this.aptfa = aptfa;
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public String getOperator() {
//        return operator;
//    }
//
//    public void setOperator(String operator) {
//        this.operator = operator;
//    }
//
//    public String getCircle() {
//        return circle;
//    }
//
//    public void setCircle(String circle) {
//        this.circle = circle;
//    }
//
//    public String getTxnKey() {
//        return txnKey;
//    }
//
//    public void setTxnKey(String txnKey) {
//        this.txnKey = txnKey;
//    }
//
//    public String getUtiStatus() {
//        return utiStatus;
//    }
//
//    public void setUtiStatus(String utiStatus) {
//        this.utiStatus = utiStatus;
//    }
//
//    public String getPsaid() {
//        return psaid;
//    }
//
//    public void setPsaid(String psaid) {
//        this.psaid = psaid;
//    }
//
//    public double getCouponCharge() {
//        return couponCharge;
//    }
//
//    public void setCouponCharge(double couponCharge) {
//        this.couponCharge = couponCharge;
//    }
//
//    public int getTotalRecord() {
//        return totalRecord;
//    }
//
//    public void setTotalRecord(int totalRecord) {
//        this.totalRecord = totalRecord;
//    }
//}
