package com.app.rupiksha.interfaces;


import com.app.rupiksha.models.BaseResponse;

public interface IAEPSVerifyOtpView extends IView
{
    void onDeviceListSuccess(BaseResponse body);

    void onVerifyOtpSuccess(BaseResponse body);

    void onGetOtpSuccess(BaseResponse body);

    void onBiometricAuthenticationSuccess(BaseResponse body);
}
