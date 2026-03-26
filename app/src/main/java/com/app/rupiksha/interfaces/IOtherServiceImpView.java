package com.app.rupiksha.interfaces;


import com.app.rupiksha.models.BaseResponse;

public interface IOtherServiceImpView extends IView
{
    void onBillerListSuccess(BaseResponse body);
    void onBillerDetailsSuccess(BaseResponse body);
    void onFetchBillSuccess(BaseResponse body);
    void onPayBillSuccess(BaseResponse body);
}
