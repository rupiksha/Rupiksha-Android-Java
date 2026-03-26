package com.app.rupiksha.interfaces;


import com.app.rupiksha.models.BaseResponse;

public interface ITwoFAView extends IView
{
    void onDeviceListSuccess(BaseResponse body);
    void onBiometricAuthenticationSuccess(BaseResponse body);
}
