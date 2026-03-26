package com.app.rupiksha.interfaces;


import com.app.rupiksha.models.BaseResponse;

public interface IAddMoneyView extends IView
{

    void onBankListSuccess(BaseResponse body);
    void onAddMoneySuccess(BaseResponse body);

}
