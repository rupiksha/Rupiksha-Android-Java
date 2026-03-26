package com.app.rupiksha.interfaces;


import com.app.rupiksha.models.BaseResponse;

public interface IAPTwoFAView extends IView
{
    void onDeviceListSuccess(BaseResponse body);
    void onApBiometricAuthenticationSuccess(BaseResponse body);
}
