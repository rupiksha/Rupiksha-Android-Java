package com.app.rupiksha.interfaces;


import com.app.rupiksha.models.BaseResponse;

public interface IPayoutView extends IView
{

    void onBankListSuccess(BaseResponse body);
    void onDeleteBankSuccess(BaseResponse body);
    void onInitiateTransactionSuccess(BaseResponse body);
    void onPayoutTransactionSuccess(BaseResponse body);

}
