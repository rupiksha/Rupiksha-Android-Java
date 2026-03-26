package com.app.rupiksha.interfaces;

import com.app.rupiksha.models.BaseResponse;

public interface IRemitterRegistrationView extends IView
{

    void onRemitterRegisterSuccess(BaseResponse body);

    void vAadharSuccess(BaseResponse body);

    void onValidateOtpSuccess(BaseResponse body);
}
