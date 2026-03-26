package com.app.rupiksha.interfaces;


import com.app.rupiksha.models.BaseResponse;

public interface ILoginView extends IView
{
    void onLoginSuccess(BaseResponse body);
    void onForgetPasswordSuccess(BaseResponse body);
    void onForgetPinSuccess(BaseResponse body);
}
