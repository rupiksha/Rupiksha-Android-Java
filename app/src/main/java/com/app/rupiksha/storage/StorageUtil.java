//package com.app.rupiksha.storage;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//
//
//import com.app.rupiksha.constant.AppConstants;
//import com.app.rupiksha.models.BankModel;
//import com.app.rupiksha.models.BillerModel;
//import com.app.rupiksha.models.DmtProfileDataModel;
//import com.app.rupiksha.models.KycModel;
//import com.app.rupiksha.models.ModelSubscriptionBiller;
//import com.app.rupiksha.models.ModelUserInfo;
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//
//import java.lang.reflect.Type;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//
//public class StorageUtil
//{
//    private static SharedPreferences mSharedPreferences;
//    private Context context;
//
//    public StorageUtil(Context context) {
//        this.context = context;
//    }
//
//    public static SharedPreferences getSharedPref(Context context) {
//        if (mSharedPreferences == null) {
//            mSharedPreferences = context.getSharedPreferences(AppConstants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
//        }
//        return mSharedPreferences;
//    }
//
//    public boolean isLogin() {
//        return getAccessToken() != null;
//    }
//
//    public static String getString(Context context, String key) {
//
//        return getSharedPref(context).getString(key, "");
//    }
//
//    public SharedPreferences getSharedPreferences() {
//        return context.getSharedPreferences(AppConstants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
//    }
//
//
//    public String getDeviceToken() {
//        return getSharedPreferences().getString(AppConstants.SHARED_PREF_DEVICE_TOKEN, "");
//    }
//
//    public void setDeviceToken(String id) {
//        SharedPreferences.Editor editor = getSharedPreferences().edit();
//        editor.putString(AppConstants.SHARED_PREF_DEVICE_TOKEN, id);
//        editor.apply();
//    }
//
//
//
//    public String getApiKey() {
//        return getSharedPreferences().getString(AppConstants.SHARED_PREF_API_KEY, "");
//    }
//
//    public void setApiKey(String id) {
//        SharedPreferences.Editor editor = getSharedPreferences().edit();
//        editor.putString(AppConstants.SHARED_PREF_API_KEY, id);
//        editor.apply();
//    }
//
//    public String getAccessToken() {
//        return getSharedPreferences().getString(AppConstants.SHARED_PREF_ACCESS_TOKEN, "");
//    }
//
//    public void setAccessToken(String id) {
//        SharedPreferences.Editor editor = getSharedPreferences().edit();
//        editor.putString(AppConstants.SHARED_PREF_ACCESS_TOKEN, id);
//        editor.apply();
//    }
//
//    public String getTokenType() {
//        return getSharedPreferences().getString(AppConstants.SHARED_PREF_TOKENTYPE, "");
//    }
//
//    public void setTokenType(String snapProfileImage) {
//        SharedPreferences.Editor editor = getSharedPreferences().edit();
//        editor.putString(AppConstants.SHARED_PREF_TOKENTYPE, snapProfileImage);
//        editor.apply();
//    }
//
//
//    public String getDmtKey() {
//        return getSharedPreferences().getString(AppConstants.SHARED_PREF_DMT_KEY, "");
//    }
//
//    public void setDMTkey(String key) {
//        SharedPreferences.Editor editor = getSharedPreferences().edit();
//        editor.putString(AppConstants.SHARED_PREF_DMT_KEY, key);
//        editor.apply();
//    }
//
//
//    public void saveDMTInfo(DmtProfileDataModel userInfo) {
//        SharedPreferences.Editor editor = getSharedPreferences().edit();
//        Gson gson = new Gson();
//        String json = gson.toJson(userInfo);
//        editor.putString(AppConstants.SHARED_PREF_DMT_INFO, json).apply();
//    }
//
//    public DmtProfileDataModel getDMTInfo() {
//        Gson gson = new Gson();
//        String json = getSharedPreferences().getString(AppConstants.SHARED_PREF_DMT_INFO, null);
//        Type type = new TypeToken<DmtProfileDataModel>() {
//        }.getType();
//        return gson.fromJson(json, type);
//    }
//
//
//    public void saveUserInfo(ModelUserInfo userInfo) {
//        SharedPreferences.Editor editor = getSharedPreferences().edit();
//        Gson gson = new Gson();
//        String json = gson.toJson(userInfo);
//        editor.putString(AppConstants.SHARED_PREF_USER_INFO, json).apply();
//    }
//
//    public ModelUserInfo getUserInfo() {
//        Gson gson = new Gson();
//        String json = getSharedPreferences().getString(AppConstants.SHARED_PREF_USER_INFO, null);
//        Type type = new TypeToken<ModelUserInfo>() {
//        }.getType();
//        return gson.fromJson(json, type);
//    }
//
//    public void saveKYCInfo(KycModel userInfo) {
//        SharedPreferences.Editor editor = getSharedPreferences().edit();
//        Gson gson = new Gson();
//        String json = gson.toJson(userInfo);
//        editor.putString(AppConstants.SHARED_PREF_KYC_INFO, json).apply();
//    }
//
//    public KycModel getKYCInfo() {
//        Gson gson = new Gson();
//        String json = getSharedPreferences().getString(AppConstants.SHARED_PREF_KYC_INFO, null);
//        Type type = new TypeToken<KycModel>() {
//        }.getType();
//        return gson.fromJson(json, type);
//    }
//
//
//
//
//    public void clearAll() {
//        getSharedPreferences().edit()
//                .clear()
//                .apply();
//    }
//
//    public static void setSubscriptionEndTime(Context context, long endtime)
//    {
//        SharedPreferences.Editor editor = getSharedPref(context).edit();
//        editor.putLong(AppConstants.SHARED_PREF_EndTime, endtime);
//        editor.apply();
//    }
//
//    public static void setTutorialShown(Context context, boolean isShown) {
//        SharedPreferences.Editor editor = getSharedPref(context).edit();
//        editor.putBoolean(AppConstants.SHARED_PREF_TOOLTIP2, isShown);
//        editor.apply();
//    }
//
//    public static Boolean getTutorialShown(Context context) {
//        return getSharedPref(context).getBoolean(AppConstants.SHARED_PREF_TOOLTIP2, true);
//    }
//
//    public static void setDeviceType(Context context, boolean isShown) {
//        SharedPreferences.Editor editor = getSharedPref(context).edit();
//        editor.putBoolean(AppConstants.KEY_PHONE_TYPE, isShown);
//        editor.apply();
//    }
//
//    public static Boolean getDeviceType(Context context) {
//        return getSharedPref(context).getBoolean(AppConstants.KEY_PHONE_TYPE, false);
//    }
//
//    public static long getSubscriptionEndTime(Context context) {
//        return getSharedPref(context).getLong(AppConstants.SHARED_PREF_EndTime, (long) 0.0);
//    }
//
//    public static String getDate(long milliSeconds, String dateFormat) {
//        // Create a DateFormatter object for displaying date in specified format.
//        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
//        // Create a calendar object that will convert the date and time value in milliseconds to date.
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(milliSeconds);
//        return formatter.format(calendar.getTime());
//    }
//
//    public void saveOperatorList(ArrayList<BillerModel> subCategoryList)
//    {
//        Gson gson = new Gson();
//        String json = gson.toJson(subCategoryList);
//        SharedPreferences.Editor editor = getSharedPref(context).edit();
//        editor.putString("subCategoryList", json);
//        editor.apply();
//    }
//
//    public static ArrayList<BillerModel> getOperatorList(Context context) {
//        String json = getSharedPref(context).getString("subCategoryList", null);
//        ArrayList<BillerModel> channelList;
//        Gson gson = new Gson();
//        Type type = new TypeToken<ArrayList<BillerModel>>() {
//        }.getType();
//        channelList = gson.fromJson(json, type);
//        return channelList;
//    }
//
//    public void saveBankList(ArrayList<BankModel> subCategoryList)
//    {
//        Gson gson = new Gson();
//        String json = gson.toJson(subCategoryList);
//        SharedPreferences.Editor editor = getSharedPref(context).edit();
//        editor.putString("subCategoryList", json);
//        editor.apply();
//    }
//    public static ArrayList<BankModel> getBankList(Context context) {
//        String json = getSharedPref(context).getString("subCategoryList", null);
//        ArrayList<BankModel> channelList;
//        Gson gson = new Gson();
//        Type type = new TypeToken<ArrayList<BankModel>>() {
//        }.getType();
//        channelList = gson.fromJson(json, type);
//        return channelList;
//    }
//
//    public void saveSubscriptionBiller(ArrayList<ModelSubscriptionBiller> subCategoryList)
//    {
//        Gson gson = new Gson();
//        String json = gson.toJson(subCategoryList);
//        SharedPreferences.Editor editor = getSharedPref(context).edit();
//        editor.putString("subCategoryList", json);
//        editor.apply();
//    }
//    public static ArrayList<ModelSubscriptionBiller> getSubscriptionBiller(Context context) {
//        String json = getSharedPref(context).getString("subCategoryList", null);
//        ArrayList<ModelSubscriptionBiller> channelList;
//        Gson gson = new Gson();
//        Type type = new TypeToken<ArrayList<ModelSubscriptionBiller>>() {
//        }.getType();
//        channelList = gson.fromJson(json, type);
//        return channelList;
//    }
//}
