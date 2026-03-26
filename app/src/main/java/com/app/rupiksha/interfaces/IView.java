package com.app.rupiksha.interfaces;

import android.content.Context;

public interface IView {

    Context getContext();

    void enableLoadingBar(boolean enable, String s);

    void onError(String reason);

    void dialogAccountDeactivate(String reason);

    void onErrorToast(String reason);
}
