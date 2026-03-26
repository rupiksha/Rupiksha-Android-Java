package com.app.rupiksha.interfaces;


import com.app.rupiksha.models.BaseResponse;

public interface IBankKycView extends IView
{
    void onBankKycSuccess(BaseResponse body);
    void onStateListSuccess(BaseResponse body);

    void onBankListSuccess(BaseResponse body);
}
