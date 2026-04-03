package com.app.rupiksha.storage

import android.content.Context
import android.content.SharedPreferences
import com.app.rupiksha.constant.AppConstants
import com.app.rupiksha.models.ModelUserInfo
import com.app.rupiksha.models.KycModel
import com.app.rupiksha.models.DmtProfileDataModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class StorageUtil(private val context: Context) {

    private fun getSharedPreferences(): SharedPreferences {
        return context.getSharedPreferences(AppConstants.SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }

    var accessToken: String
        get() = getSharedPreferences().getString(AppConstants.SHARED_PREF_ACCESS_TOKEN, "") ?: ""
        set(value) = getSharedPreferences().edit()
            .putString(AppConstants.SHARED_PREF_ACCESS_TOKEN, value).apply()

    var apiKey: String
        get() = getSharedPreferences().getString(AppConstants.SHARED_PREF_API_KEY, "") ?: ""
        set(value) = getSharedPreferences().edit()
            .putString(AppConstants.SHARED_PREF_API_KEY, value).apply()

    var deviceToken: String
        get() = getSharedPreferences().getString(AppConstants.SHARED_PREF_DEVICE_TOKEN, "") ?: ""
        set(value) = getSharedPreferences().edit()
            .putString(AppConstants.SHARED_PREF_DEVICE_TOKEN, value).apply()

    var dmtKey: String
        get() = getSharedPreferences().getString(AppConstants.SHARED_PREF_DMT_KEY, "") ?: ""
        set(value) = getSharedPreferences().edit()
            .putString(AppConstants.SHARED_PREF_DMT_KEY, value).apply()

    fun saveUserInfo(userInfo: ModelUserInfo) {
        val json = Gson().toJson(userInfo)
        getSharedPreferences().edit().putString(AppConstants.SHARED_PREF_USER_INFO, json).apply()
    }

    fun getUserInfo(): ModelUserInfo? {
        val json = getSharedPreferences().getString(AppConstants.SHARED_PREF_USER_INFO, null)
            ?: return null
        return Gson().fromJson(json, object : TypeToken<ModelUserInfo>() {}.type)
    }

    fun saveKYCInfo(kycInfo: KycModel) {
        val json = Gson().toJson(kycInfo)
        getSharedPreferences().edit().putString(AppConstants.SHARED_PREF_KYC_INFO, json).apply()
    }

    fun getKYCInfo(): KycModel? {
        val json = getSharedPreferences().getString(AppConstants.SHARED_PREF_KYC_INFO, null)
            ?: return null
        return Gson().fromJson(json, object : TypeToken<KycModel>() {}.type)
    }

    fun saveDMTInfo(dmtInfo: DmtProfileDataModel) {
        val json = Gson().toJson(dmtInfo)
        getSharedPreferences().edit().putString(AppConstants.SHARED_PREF_DMT_INFO, json).apply()
    }

    fun getDMTInfo(): DmtProfileDataModel? {
        val json = getSharedPreferences().getString(AppConstants.SHARED_PREF_DMT_INFO, null)
            ?: return null
        return Gson().fromJson(json, object : TypeToken<DmtProfileDataModel>() {}.type)
    }

    fun isLogin(): Boolean {
        return accessToken.isNotEmpty()
    }

    fun clearAll() {
        getSharedPreferences().edit().clear().apply()
    }
}
