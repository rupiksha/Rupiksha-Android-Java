package com.app.rupiksha.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ModelSettingData(
    @SerializedName("web_url")
    @Expose
    val webUrl: String? = null,

    @SerializedName("web_url_status")
    @Expose
    val webUrlStatus: Boolean? = null,

    @SerializedName("fb_url")
    @Expose
    val fbUrl: String? = null,

    @SerializedName("fb_url_status")
    @Expose
    val fbUrlStatus: Boolean? = null,

    @SerializedName("insta_url")
    @Expose
    val instaUrl: String? = null,

    @SerializedName("insta_url_status")
    @Expose
    val instaUrlStatus: Boolean? = null,

    @SerializedName("twitter_url")
    @Expose
    val twitterUrl: String? = null,

    @SerializedName("twitter_url_status")
    @Expose
    val twitterUrlStatus: Boolean? = null,

    @SerializedName("linked_in_url")
    @Expose
    val linkedInUrl: String? = null,

    @SerializedName("linked_in_url_status")
    @Expose
    val linkedInUrlStatus: Boolean? = null,

    @SerializedName("suggest_an_idea_link")
    @Expose
    val suggestAnIdeaLink: String? = null,

    @SerializedName("suggest_an_idea_link_status")
    @Expose
    val suggestAnIdeaLinkStatus: Boolean? = null,

    @SerializedName("phone")
    @Expose
    val phone: String? = null,

    @SerializedName("reportemail")
    @Expose
    val reportemail: String? = null,

    @SerializedName("app_version")
    @Expose
    val appVersion: String? = null,

    @SerializedName("display_app")
    @Expose
    val displayApp: String? = null,

    @SerializedName("force_status")
    @Expose
    val forceStatus: Boolean? = null,

    @SerializedName("contactemail")
    @Expose
    val contactemail: String? = null,

    @SerializedName("logical_tier")
    @Expose
    val logicalTier: String? = null,

    @SerializedName("app_version_ios")
    @Expose
    val appVersionIos: String? = null,

    @SerializedName("become_partner")
    @Expose
    val becomePartner: String? = null,

    @SerializedName("confidentiality")
    @Expose
    val confidentiality: String? = null,

    @SerializedName("coach_info")
    @Expose
    val coachInfo: String? = null,

    @SerializedName("terms_condition")
    @Expose
    val termsCondition: String? = null,

    @SerializedName("force_status_ios")
    @Expose
    val forceStatusIos: Boolean? = null,

    @SerializedName("should_show_popup")
    @Expose
    val shouldShowPopup: Boolean? = null,

    @SerializedName("become_partnair")
    @Expose
    val becomePartnair: String? = null
)
