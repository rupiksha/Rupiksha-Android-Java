package com.app.rupiksha.interfaces;


import com.app.rupiksha.models.BaseResponse;

public interface IPSARegistrationView extends IView
{
    void onPSARegistrationSuccess(BaseResponse body);
    void onStateListSuccess(BaseResponse body);
}
