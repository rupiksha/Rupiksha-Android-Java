package com.app.rupiksha.storage

import android.content.Context
import android.content.SharedPreferences
import com.app.rupiksha.constant.AppConstants
import com.app.rupiksha.models.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StorageUtil @Inject constructor(private val context: Context) {

    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(AppConstants.SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }

    private val gson = Gson()

    fun isLogin(): Boolean = getAccessToken()?.isNotEmpty() == true

    fun getDeviceToken(): String = sharedPreferences.getString(AppConstants.SHARED_PREF_DEVICE_TOKEN, "") ?: ""

    fun setDeviceToken(token: String) {
        sharedPreferences.edit().putString(AppConstants.SHARED_PREF_DEVICE_TOKEN, token).apply()
    }

    fun getApiKey(): String = sharedPreferences.getString(AppConstants.SHARED_PREF_API_KEY, "") ?: ""

    fun setApiKey(key: String) {
        sharedPreferences.edit().putString(AppConstants.SHARED_PREF_API_KEY, key).apply()
    }

    fun getAccessToken(): String? = sharedPreferences.getString(AppConstants.SHARED_PREF_ACCESS_TOKEN, null)

    fun setAccessToken(token: String) {
        sharedPreferences.edit().putString(AppConstants.SHARED_PREF_ACCESS_TOKEN, token).apply()
    }

    fun saveUserInfo(userInfo: ModelUserInfo) {
        val json = gson.toJson(userInfo)
        sharedPreferences.edit().putString(AppConstants.SHARED_PREF_USER_INFO, json).apply()
    }

    fun getUserInfo(): ModelUserInfo? {
        val json = sharedPreferences.getString(AppConstants.SHARED_PREF_USER_INFO, null) ?: return null
        val type = object : TypeToken<ModelUserInfo>() {}.type
        return gson.fromJson(json, type)
    }

    fun setDmtKey(key: String) {
        sharedPreferences.edit().putString(AppConstants.SHARED_PREF_DMT_KEY, key).apply()
    }

    fun getDmtKey(): String? = sharedPreferences.getString(AppConstants.SHARED_PREF_DMT_KEY, null)

    fun saveDMTInfo(dmtInfo: DmtProfileDataModel) {
        val json = gson.toJson(dmtInfo)
        sharedPreferences.edit().putString(AppConstants.SHARED_PREF_DMT_INFO, json).apply()
    }

    fun getDMTInfo(): DmtProfileDataModel? {
        val json = sharedPreferences.getString(AppConstants.SHARED_PREF_DMT_INFO, null) ?: return null
        val type = object : TypeToken<DmtProfileDataModel>() {}.type
        return gson.fromJson(json, type)
    }

    fun clearAll() {
        sharedPreferences.edit().clear().apply()
    }

    companion object {
        fun getDate(milliSeconds: Long, dateFormat: String): String {
            val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
            val calendar = Calendar.getInstance().apply { timeInMillis = milliSeconds }
            return formatter.format(calendar.time)
        }
    }
}