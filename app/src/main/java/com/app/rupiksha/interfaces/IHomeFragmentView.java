package com.app.rupiksha.interfaces;


import com.app.rupiksha.models.BaseResponse;

public interface IHomeFragmentView extends IView
{

    void onServiceListSuccess(BaseResponse body);
    void onWalletBalanceSuccess(BaseResponse body);
    void onCheckKycSuccess(BaseResponse body);
    void onCheckPSASuccess(BaseResponse body);
}
