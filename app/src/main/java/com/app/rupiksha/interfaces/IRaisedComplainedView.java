package com.app.rupiksha.interfaces;


import com.app.rupiksha.models.BaseResponse;

public interface IRaisedComplainedView extends IView
{

    void onRaisedComplainedSuccess(BaseResponse body);
    void onBankListSuccess(BaseResponse body);

}
