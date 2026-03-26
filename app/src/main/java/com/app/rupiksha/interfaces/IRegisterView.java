package com.app.rupiksha.interfaces;


import com.app.rupiksha.models.BaseResponse;

public interface IRegisterView extends IView
{
    void onRoleListSuccess(BaseResponse body);
    void onRegisterSuccess(BaseResponse body);
}
