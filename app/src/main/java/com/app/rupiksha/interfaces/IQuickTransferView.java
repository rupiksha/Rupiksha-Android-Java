package com.app.rupiksha.interfaces;


import com.app.rupiksha.models.BaseResponse;

public interface IQuickTransferView extends IView
{

    void onBankListSuccess(BaseResponse body);
    void onFetchAccountSuccess(BaseResponse body);
    void onInitiateTransactionSuccess(BaseResponse body);
    void onQTTransactionSuccess(BaseResponse body);
    void onAccountVerifySuccess(BaseResponse body);
}
