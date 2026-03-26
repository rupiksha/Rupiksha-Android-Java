package com.app.rupiksha.interfaces;


import com.app.rupiksha.models.BaseResponse;

public interface IAEPSServiceView extends IView
{
    void onDeviceListSuccess(BaseResponse body);
    void onBankListSuccess(BaseResponse body);
    void onAadharPaySuccess(BaseResponse body);
    void onMinistatementSuccess(BaseResponse body);
    void onBalanceEnquiresSuccess(BaseResponse body);
    void onCashWithdrawlSuccess(BaseResponse body);

}
