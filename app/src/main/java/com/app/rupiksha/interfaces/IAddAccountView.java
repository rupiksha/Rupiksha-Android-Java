package com.app.rupiksha.interfaces;


import com.app.rupiksha.models.BaseResponse;

public interface IAddAccountView extends IView
{

    void onBankListSuccess(BaseResponse body);

    void onAddBankSuccess(BaseResponse body);

}
