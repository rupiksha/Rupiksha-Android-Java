package com.app.rupiksha.apipresenter;

import android.accounts.NetworkErrorException;
import android.app.Activity;

import com.app.rupiksha.R;
import com.app.rupiksha.apis.AppController;
import com.app.rupiksha.extra.CustomToastNotification;
import com.app.rupiksha.interfaces.IITRFormView;
import com.app.rupiksha.models.BaseResponse;
import com.app.rupiksha.utils.Utils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ITRPresenter extends BasePresenter<IITRFormView> {

    public static int retryCount = 0;

    public void sendItrImages(final Activity context, final HashMap<String, RequestBody> map, final HashMap<String, String> headers, MultipartBody.Part uploadedFile) {
        getView().enableLoadingBar(true, "");
        AppController.getInstance().getApiInterfaceTimeOut15Sec().uploadImage(map,headers,uploadedFile)
                .enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<BaseResponse> call, @NotNull Response<BaseResponse> response) {
                        getView().enableLoadingBar(false, "");
                        retryCount = 0;
                        boolean active = true;
                        String error = "";
                        JSONObject jObjError;
                        if (response.code() == 401 && response.errorBody() != null)
                        {
                            try {
                                error = response.errorBody().string();
                                jObjError = new JSONObject(error);
                                String title = jObjError.has("error") ? jObjError.getString("error") : "";
                                String reason = jObjError.has("message") ? jObjError.getString("message") : "";
                                if(jObjError.has("active"))
                                {
                                    if(jObjError.getBoolean("active")){
                                        new CustomToastNotification(context,reason);
                                        Utils.userLogoutDeleteAccount(context);}
                                }
                                if(jObjError.has("message"))
                                {
                                    new CustomToastNotification(context,reason);

                                }
                                if(jObjError.has("error"))
                                {
                                    new CustomToastNotification(context,title);
                                        Utils.userLogoutDeleteAccount(context);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else if (response.isSuccessful() && response.code() == 200) {
                            getView().onUploadImageSuccess(response.body());
                        }

                    }

                    @Override
                    public void onFailure(@NotNull Call<BaseResponse> call, @NotNull Throwable t) {
                        getView().enableLoadingBar(false, "");
                        try {
                            t.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (t instanceof TimeoutException || t instanceof SocketTimeoutException) {
                            retryCount++;
                            if (retryCount < 3) {
                                getView().onErrorToast(context.getString(R.string.msg_internet_seems_slow));
                                sendItrImages(context,map,headers,uploadedFile);
                            } else {
                                getView().onErrorToast(context.getString(R.string.msg_unable_connect_server));
                                getView().enableLoadingBar(false, "");
                            }
                        } else {
                            if (t instanceof NetworkErrorException || t instanceof SocketException) {
                                getView().onErrorToast(context.getString(R.string.msg_check_internet_connection));
                            } else {
                                getView().onErrorToast(context.getString(R.string.msg_something_went_wrong));
                            }
                            retryCount = 3;
                            getView().enableLoadingBar(false, "");
                        }
                    }
                });
        }


    public void submitITRForm(final Activity context, final HashMap<String, RequestBody> map, final HashMap<String, String> headers) {
        getView().enableLoadingBar(true, "");
        AppController.getInstance().getApiInterfaceTimeOut15Sec().submitItrForm(map,headers)
                .enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<BaseResponse> call, @NotNull Response<BaseResponse> response) {
                        getView().enableLoadingBar(false, "");
                        retryCount = 0;
                        boolean active = true;
                        String error = "";
                        JSONObject jObjError;
                        if (response.code() == 401 && response.errorBody() != null)
                        {
                            try {
                                error = response.errorBody().string();
                                jObjError = new JSONObject(error);
                                String title = jObjError.has("error") ? jObjError.getString("error") : "";
                                String reason = jObjError.has("message") ? jObjError.getString("message") : "";
                                if(jObjError.has("active"))
                                {
                                    if(jObjError.getBoolean("active")){
                                        new CustomToastNotification(context,reason);
                                        Utils.userLogoutDeleteAccount(context);}
                                }
                                if(jObjError.has("message"))
                                {
                                    new CustomToastNotification(context,reason);

                                }
                                if(jObjError.has("error"))
                                {
                                    new CustomToastNotification(context,title);
                                        Utils.userLogoutDeleteAccount(context);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else if (response.isSuccessful() && response.code() == 200) {
                            getView().onFormSubmitSuccess(response.body());
                        }

                    }

                    @Override
                    public void onFailure(@NotNull Call<BaseResponse> call, @NotNull Throwable t) {
                        getView().enableLoadingBar(false, "");
                        try {
                            t.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (t instanceof TimeoutException || t instanceof SocketTimeoutException) {
                            retryCount++;
                            if (retryCount < 3) {
                                getView().onErrorToast(context.getString(R.string.msg_internet_seems_slow));
                                submitITRForm(context,map,headers);
                            } else {
                                getView().onErrorToast(context.getString(R.string.msg_unable_connect_server));
                                getView().enableLoadingBar(false, "");
                            }
                        } else {
                            if (t instanceof NetworkErrorException || t instanceof SocketException) {
                                getView().onErrorToast(context.getString(R.string.msg_check_internet_connection));
                            } else {
                                getView().onErrorToast(context.getString(R.string.msg_something_went_wrong));
                            }
                            retryCount = 3;
                            getView().enableLoadingBar(false, "");
                        }
                    }
                });
        }
}
