package com.app.rupiksha.interfaces;


import com.app.rupiksha.models.BaseResponse;

public interface IHomeView extends IView
{
    void onViewProfileSuccess(BaseResponse body);
    void onCheckLogoutSuccess(BaseResponse bodey);
    void onCheckStatusSuccess(BaseResponse body);

}
