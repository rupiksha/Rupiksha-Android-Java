package com.app.rupiksha.interfaces;


import com.app.rupiksha.models.BaseResponse;

public interface IWalletFragmentView extends IView
{

    void onReportListSuccess(BaseResponse body);
    void onFetchUserSuccess(BaseResponse body);
    void onDoTransactionSuccess(BaseResponse body);

}
