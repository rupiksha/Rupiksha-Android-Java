package com.app.rupiksha.apipresenter;

import android.accounts.NetworkErrorException;
import android.app.Activity;

import androidx.annotation.NonNull;

import com.app.rupiksha.R;
import com.app.rupiksha.apis.AppController;
import com.app.rupiksha.extra.CustomToastNotification;
import com.app.rupiksha.interfaces.IPSARegistrationView;
import com.app.rupiksha.models.BaseResponse;
import com.app.rupiksha.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PSARegisterPresenter extends BasePresenter<IPSARegistrationView>
{
    public static int retryCount = 0;
    public void getStateList(Activity context, boolean isprocess) {
        if(isprocess)
            Utils.showProgressDialog(context,"");
        AppController.getInstance().getApiInterfaceTimeOut30Sec().getStateList()
                .enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<BaseResponse> call, @NonNull Response<BaseResponse> response) {

                        if(isprocess)
                            Utils.hideProgressDialog();
                        getView().enableLoadingBar(false, "");
                        retryCount = 0;

                        boolean active = true;
                        JSONObject jObjError;
                        String error = "";
                        if (response.code() == 401 && response.errorBody() != null)
                        {
                            try {
                                error = response.errorBody().string();
                                jObjError = new JSONObject(error);
                                String title = jObjError.has("error") ? jObjError.getString("error") : "";
                                String reason = jObjError.has("message") ? jObjError.getString("message") : "";
                                if(jObjError.has("active"))
                                {
                                    if(jObjError.getBoolean("active"))
                                        new CustomToastNotification(context,reason);
                                }
                                if(jObjError.has("message"))
                                {
                                    new CustomToastNotification(context,reason);
                                }
                                if(jObjError.has("error"))
                                {
                                    if(jObjError.getBoolean("error"))
                                    {
                                        new CustomToastNotification(context, reason);
                                        Utils.userLogoutDeleteAccount(context);
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else if (response.isSuccessful() && response.code() == 200)
                        {
                            getView().onStateListSuccess(response.body());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<BaseResponse> call, @NonNull Throwable t) {
                        Utils.hideProgressDialog();
                        try {
                            t.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (t instanceof TimeoutException || t instanceof SocketTimeoutException) {
                            Utils.hideProgressDialog();
                            retryCount++;
                            if (retryCount < 3) {
                                getView().onErrorToast(context.getString(R.string.msg_internet_seems_slow));
                                getStateList(context, isprocess);
                            } else {
                                getView().onErrorToast(context.getString(R.string.msg_unable_connect_server));
                                //getView().enableLoadingBar(false, "");
                                Utils.hideProgressDialog();
                            }
                        } else {
                            if (t instanceof NetworkErrorException || t instanceof SocketException) {
                                getView().onErrorToast(context.getString(R.string.msg_check_internet_connection));
                            } else {
                                getView().onErrorToast(context.getString(R.string.msg_something_went_wrong));
                            }
                            retryCount = 3;
                            //getView().enableLoadingBar(false, "");
                            Utils.hideProgressDialog();
                        }
                    }
                });
    }

    public void getPSARegistration(Activity context, HashMap<String, String> headers, HashMap<String, RequestBody> map, boolean isprocess)
    {
        if(isprocess)
            Utils.showProgressDialog(context,"");
        AppController.getInstance().getApiInterfaceTimeOut30Sec().getPSARegistration(headers,map)
                .enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<BaseResponse> call, @NonNull Response<BaseResponse> response) {
                        // getView().enableLoadingBar(false, "");
                        if(isprocess)
                            Utils.hideProgressDialog();
                        retryCount = 0;

                        boolean active = true;
                        JSONObject jObjError;
                        String error = "";
                        if (response.isSuccessful() && response.code() == 200)
                        {
                            getView().onPSARegistrationSuccess(response.body());
                        }else {
                            try {
                                error = response.errorBody().string();
                                jObjError = new JSONObject(error);
                                String title = jObjError.has("error") ? jObjError.getString("error") : "";
                                String reason = jObjError.has("message") ? jObjError.getString("message") : "";
                                if(jObjError.has("active"))
                                {
                                    if(jObjError.getBoolean("active"))
                                        new CustomToastNotification(context,reason);

                                    Utils.userLogoutDeleteAccount(context);
                                }
                                if(jObjError.has("message"))
                                {
                                    new CustomToastNotification(context,reason);
                                }
                                if(jObjError.has("error"))
                                {
                                    if(jObjError.getBoolean("error")) {
                                        new CustomToastNotification(context, reason);
                                        Utils.userLogoutDeleteAccount(context);
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<BaseResponse> call, @NonNull Throwable t) {
                        Utils.hideProgressDialog();
                        try {
                            t.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (t instanceof TimeoutException || t instanceof SocketTimeoutException) {
                            retryCount++;
                            if (retryCount < 3) {
                                getView().onErrorToast(context.getString(R.string.msg_internet_seems_slow));
                                getPSARegistration(context, headers,map,isprocess);
                            } else {
                                getView().onErrorToast(context.getString(R.string.msg_unable_connect_server));
                                //  getView().enableLoadingBar(false, "");
                                Utils.hideProgressDialog();
                            }
                        } else {
                            if (t instanceof NetworkErrorException || t instanceof SocketException) {
                                getView().onErrorToast(context.getString(R.string.msg_check_internet_connection));
                            } else {
                                getView().onErrorToast(context.getString(R.string.msg_something_went_wrong));
                            }
                            retryCount = 3;
                            // getView().enableLoadingBar(false, "");
                            Utils.hideProgressDialog();
                        }
                    }
                });
    }


}
