package com.app.rupiksha.interfaces;


import com.app.rupiksha.models.BaseResponse;

public interface IUserKycView extends IView
{
    void onKycSuccess(BaseResponse body);
    void onStateListSuccess(BaseResponse body);

    void onDocumentListSuccess(BaseResponse body);
}
