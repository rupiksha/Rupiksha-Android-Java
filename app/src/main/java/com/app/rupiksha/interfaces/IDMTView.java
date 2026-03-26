package com.app.rupiksha.interfaces;

import com.app.rupiksha.models.BaseResponse;

public interface IDMTView extends IView
{
    void onDmtAccountListSuccess(BaseResponse body);
    void onDeleteAccountSuccess(BaseResponse body);
    void onInitiateTransactionSuccess(BaseResponse body);
    void onDmtTransactionSuccess(BaseResponse body);
    void onDmtLogoutSuccess(BaseResponse body);
}
