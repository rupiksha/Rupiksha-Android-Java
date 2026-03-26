package com.app.rupiksha.interfaces;


import com.app.rupiksha.models.BaseResponse;

public interface IITRFormView extends IView {

    void onUploadImageSuccess(BaseResponse body);
    void onFormSubmitSuccess(BaseResponse body);

}
