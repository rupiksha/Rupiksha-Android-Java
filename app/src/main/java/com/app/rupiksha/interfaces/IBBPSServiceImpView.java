package com.app.rupiksha.interfaces;


import com.app.rupiksha.models.BaseResponse;

public interface IBBPSServiceImpView extends IView
{
    void onBillerListSuccess(BaseResponse body);
    void onFetchBillSuccess(BaseResponse body);
    void onPayBillSuccess(BaseResponse body);
}
