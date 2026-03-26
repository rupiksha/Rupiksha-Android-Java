package com.app.rupiksha.apipresenter;

import android.accounts.NetworkErrorException;
import android.app.Activity;

import androidx.annotation.NonNull;

import com.app.rupiksha.R;
import com.app.rupiksha.apis.AppController;
import com.app.rupiksha.extra.CustomToastNotification;
import com.app.rupiksha.interfaces.IBBPSServiceImpView;
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

public class BBPSServiceImpPresenter extends BasePresenter<IBBPSServiceImpView>
{
    public static int retryCount = 0;

    public void getbillerlist(Activity context,HashMap<String, String> headers, RequestBody requestBody,boolean isprocess) {
        // getView().enableLoadingBar(true, context.getString(R.string.msg_please_wait));
        if(isprocess)
            Utils.showProgressDialog(context,"");
        AppController.getInstance().getApiInterfaceTimeOut30Sec().getBillList(headers,requestBody)
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
                            getView().onBillerListSuccess(response.body());
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
                                getbillerlist(context,headers, requestBody,isprocess);
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


    public void getFetchbilllist(Activity context,HashMap<String, String> headers, RequestBody requestBody,boolean isprocess) {
        // getView().enableLoadingBar(true, context.getString(R.string.msg_please_wait));
        if(isprocess)
            Utils.showProgressDialog(context,"");
        AppController.getInstance().getApiInterfaceTimeOut30Sec().getFetchBill(headers,requestBody)
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
                            getView().onFetchBillSuccess(response.body());
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
                                getFetchbilllist(context,headers, requestBody,isprocess);
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


    public void getPayBillList(Activity context,HashMap<String, String> headers, RequestBody requestBody,boolean isprocess) {
        // getView().enableLoadingBar(true, context.getString(R.string.msg_please_wait));
        if(isprocess)
            Utils.showProgressDialog(context,"");
        AppController.getInstance().getApiInterfaceTimeOut30Sec().getPayBill(headers,requestBody)
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
                            getView().onPayBillSuccess(response.body());
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
                                getPayBillList(context,headers, requestBody,isprocess);
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
