package com.app.rupiksha.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelSettingData {

    @SerializedName("web_url")
    @Expose
    private String webUrl;
    @SerializedName("web_url_status")
    @Expose
    private Boolean webUrlStatus;
    @SerializedName("fb_url")
    @Expose
    private String fbUrl;
    @SerializedName("fb_url_status")
    @Expose
    private Boolean fbUrlStatus;
    @SerializedName("insta_url")
    @Expose
    private String instaUrl;
    @SerializedName("insta_url_status")
    @Expose
    private Boolean instaUrlStatus;
    @SerializedName("twitter_url")
    @Expose
    private String twitterUrl;
    @SerializedName("twitter_url_status")
    @Expose
    private Boolean twitterUrlStatus;
    @SerializedName("linked_in_url")
    @Expose
    private String linkedInUrl;
    @SerializedName("linked_in_url_status")
    @Expose
    private Boolean linkedInUrlStatus;
    @SerializedName("suggest_an_idea_link")
    @Expose
    private String suggestAnIdeaLink;
    @SerializedName("suggest_an_idea_link_status")
    @Expose
    private Boolean suggestAnIdeaLinkStatus;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("reportemail")
    @Expose
    private String reportemail;
    @SerializedName("app_version")
    @Expose
    private String appVersion;
    @SerializedName("display_app")
    @Expose
    private String displayApp;
    @SerializedName("force_status")
    @Expose
    private Boolean forceStatus;
    @SerializedName("contactemail")
    @Expose
    private String contactemail;
    @SerializedName("logical_tier")
    @Expose
    private String logicalTier;
    @SerializedName("app_version_ios")
    @Expose
    private String appVersionIos;
    @SerializedName("become_partner")
    @Expose
    private String becomePartner;

    @SerializedName("confidentiality")
    @Expose
    private String confidentiality;
    @SerializedName("coach_info")
    @Expose
    private String coachInfo;
    @SerializedName("terms_condition")
    @Expose
    private String termsCondition;
    @SerializedName("force_status_ios")
    @Expose
    private Boolean forceStatusIos;
    @SerializedName("should_show_popup")
    @Expose
    private Boolean shouldShowPopup;
    @SerializedName("become_partnair")
    @Expose
    private String becomePartnair;

  /*  @SerializedName("profile_type_list")
    @Expose
    private List<ProfileTypeList> profileTypeList = null;
    @SerializedName("h_d_y_h_a_youprep_list")
    @Expose
    private List<HDYHAYouprep> hDYHAYouprepList = null;*/

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public Boolean getWebUrlStatus() {
        return webUrlStatus;
    }

    public void setWebUrlStatus(Boolean webUrlStatus) {
        this.webUrlStatus = webUrlStatus;
    }

    public String getFbUrl() {
        return fbUrl;
    }

    public void setFbUrl(String fbUrl) {
        this.fbUrl = fbUrl;
    }

    public Boolean getFbUrlStatus() {
        return fbUrlStatus;
    }

    public void setFbUrlStatus(Boolean fbUrlStatus) {
        this.fbUrlStatus = fbUrlStatus;
    }

    public String getInstaUrl() {
        return instaUrl;
    }

    public void setInstaUrl(String instaUrl) {
        this.instaUrl = instaUrl;
    }

    public Boolean getInstaUrlStatus() {
        return instaUrlStatus;
    }

    public void setInstaUrlStatus(Boolean instaUrlStatus) {
        this.instaUrlStatus = instaUrlStatus;
    }

    public String getTwitterUrl() {
        return twitterUrl;
    }

    public void setTwitterUrl(String twitterUrl) {
        this.twitterUrl = twitterUrl;
    }

    public Boolean getTwitterUrlStatus() {
        return twitterUrlStatus;
    }

    public void setTwitterUrlStatus(Boolean twitterUrlStatus) {
        this.twitterUrlStatus = twitterUrlStatus;
    }

    public String getLinkedInUrl() {
        return linkedInUrl;
    }

    public void setLinkedInUrl(String linkedInUrl) {
        this.linkedInUrl = linkedInUrl;
    }

    public Boolean getLinkedInUrlStatus() {
        return linkedInUrlStatus;
    }

    public void setLinkedInUrlStatus(Boolean linkedInUrlStatus) {
        this.linkedInUrlStatus = linkedInUrlStatus;
    }

    public String getSuggestAnIdeaLink() {
        return suggestAnIdeaLink;
    }

    public void setSuggestAnIdeaLink(String suggestAnIdeaLink) {
        this.suggestAnIdeaLink = suggestAnIdeaLink;
    }

    public Boolean getSuggestAnIdeaLinkStatus() {
        return suggestAnIdeaLinkStatus;
    }

    public void setSuggestAnIdeaLinkStatus(Boolean suggestAnIdeaLinkStatus) {
        this.suggestAnIdeaLinkStatus = suggestAnIdeaLinkStatus;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getReportemail() {
        return reportemail;
    }

    public void setReportemail(String reportemail) {
        this.reportemail = reportemail;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getDisplayApp() {
        return displayApp;
    }

    public void setDisplayApp(String displayApp) {
        this.displayApp = displayApp;
    }

    public Boolean getForceStatus() {
        return forceStatus;
    }

    public void setForceStatus(Boolean forceStatus) {
        this.forceStatus = forceStatus;
    }

    public String getContactemail() {
        return contactemail;
    }

    public void setContactemail(String contactemail) {
        this.contactemail = contactemail;
    }

    public String getLogicalTier() {
        return logicalTier;
    }

    public void setLogicalTier(String logicalTier) {
        this.logicalTier = logicalTier;
    }

    public String getAppVersionIos() {
        return appVersionIos;
    }

    public void setAppVersionIos(String appVersionIos) {
        this.appVersionIos = appVersionIos;
    }

    public String getConfidentiality() {
        return confidentiality;
    }

    public void setConfidentiality(String confidentiality) {
        this.confidentiality = confidentiality;
    }

    public String getTermsCondition() {
        return termsCondition;
    }

    public void setTermsCondition(String termsCondition) {
        this.termsCondition = termsCondition;
    }

    public Boolean getForceStatusIos() {
        return forceStatusIos;
    }

    public void setForceStatusIos(Boolean forceStatusIos) {
        this.forceStatusIos = forceStatusIos;
    }

    public Boolean getShouldShowPopup() {
        return shouldShowPopup;
    }

    public void setShouldShowPopup(Boolean shouldShowPopup) {
        this.shouldShowPopup = shouldShowPopup;
    }

    public String getBecomePartnair() {
        return becomePartnair;
    }

    public void setBecomePartnair(String becomePartnair) {
        this.becomePartnair = becomePartnair;
    }

    public String getCoachInfo() {
        return coachInfo;
    }

    public void setCoachInfo(String coachInfo) {
        this.coachInfo = coachInfo;
    }

   /* public List<HDYHAYouprep> gethDYHAYouprepList() {
        return hDYHAYouprepList;
    }

    public void sethDYHAYouprepList(List<HDYHAYouprep> hDYHAYouprepList) {
        this.hDYHAYouprepList = hDYHAYouprepList;
    }
    public List<ProfileTypeList> getProfileTypeList() {
        return profileTypeList;
    }

    public void setProfileTypeList(List<ProfileTypeList> profileTypeList) {
        this.profileTypeList = profileTypeList;
    }*/

    public String getBecomePartner() {
        return becomePartner;
    }

    public void setBecomePartner(String becomePartner) {
        this.becomePartner = becomePartner;
    }
}
