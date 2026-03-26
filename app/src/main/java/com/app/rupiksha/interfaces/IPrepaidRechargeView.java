package com.app.rupiksha.interfaces;


import com.app.rupiksha.models.BaseResponse;

public interface IPrepaidRechargeView extends IView
{

    void onGetOperatorSuccess(BaseResponse body);
    void onFetchOperatorSuccess(BaseResponse body);
    void onDoRechargeSuccess(BaseResponse body);

}
