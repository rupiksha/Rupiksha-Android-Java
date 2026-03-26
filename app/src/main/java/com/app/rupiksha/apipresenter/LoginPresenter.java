package com.app.rupiksha.apipresenter;

import android.accounts.NetworkErrorException;
import android.app.Activity;

import androidx.annotation.NonNull;

import com.app.rupiksha.R;
import com.app.rupiksha.apis.AppController;
import com.app.rupiksha.extra.CustomToastNotification;
import com.app.rupiksha.interfaces.ILoginView;
import com.app.rupiksha.models.BaseResponse;
import com.app.rupiksha.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPresenter extends BasePresenter<ILoginView>
{
    public static int retryCount = 0;
    public void getLogin(Activity context, RequestBody requestBody,boolean isprocess) {
        // getView().enableLoadingBar(true, context.getString(R.string.msg_please_wait));
        if(isprocess)
            Utils.showProgressDialog(context,"");
        AppController.getInstance().getApiInterfaceTimeOut30Sec().login(requestBody)
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
                            getView().onLoginSuccess(response.body());
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
                                getLogin(context, requestBody,isprocess);
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

    public void getforgetpassword(Activity context, RequestBody requestBody,boolean isprocess) {
        // getView().enableLoadingBar(true, context.getString(R.string.msg_please_wait));
        if(isprocess)
            Utils.showProgressDialog(context,"");
        AppController.getInstance().getApiInterfaceTimeOut30Sec().forgetpassword(requestBody)
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
                            getView().onForgetPasswordSuccess(response.body());
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
                                getLogin(context, requestBody,isprocess);
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

    public void getforgetpin(Activity context, RequestBody requestBody,boolean isprocess) {
        // getView().enableLoadingBar(true, context.getString(R.string.msg_please_wait));
        if(isprocess)
            Utils.showProgressDialog(context,"");
        AppController.getInstance().getApiInterfaceTimeOut30Sec().forgetpin(requestBody)
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
                            getView().onForgetPinSuccess(response.body());
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
                                getLogin(context, requestBody,isprocess);
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
