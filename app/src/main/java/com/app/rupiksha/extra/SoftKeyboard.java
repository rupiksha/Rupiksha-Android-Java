package com.app.rupiksha.extra;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.content.Context;
//import android.os.Handler;
//import android.os.Looper;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.View.OnTouchListener;
//import android.view.ViewGroup;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.EditText;
//
//public class SoftKeyboard
//{
//    private static Activity mActivity;
//
//    public SoftKeyboard(Activity activity) {
//        this.mActivity = activity;
//    }
//    public void setupUI(final View view) {
//        try {
//            if (!(view instanceof EditText)) {
//                view.setOnTouchListener(new OnTouchListener() {
//                    @Override
//                    @SuppressLint("ClickableViewAccessibility")
//                    public boolean onTouch(View v, MotionEvent event) {
//                        hideSoftKeyboard();
//                        hideSoftKeyboardDialog(view);
//                        return false;
//                    }
//                });
//            }
//
//            if (view instanceof ViewGroup) {
//                for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
//                    View innerView = ((ViewGroup) view).getChildAt(i);
//                    setupUI(innerView);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    public void show_keyboard() {
//        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
//                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
//            }
//        },500);
//    }
//    public void hideSoftKeyboardDialog(View view) {
//        try {
//            view.requestFocus();
//            view.postDelayed(() -> {
//                InputMethodManager keyboard = (InputMethodManager)
//                        mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
//                if (false) {
//                    keyboard.showSoftInput(view, 0);
//                }else{
//                    keyboard.hideSoftInputFromWindow(view.getWindowToken(), 0);
//                }
//            }, 200);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//        public static void hideSoftKeyboard() {
//        try {
//            InputMethodManager inputMethodManager = (InputMethodManager) mActivity
//                    .getSystemService(Context.INPUT_METHOD_SERVICE);
//            inputMethodManager.hideSoftInputFromWindow(mActivity
//                    .getCurrentFocus().getWindowToken(), 0);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void hidekeyboard(Context context){
//        InputMethodManager imm1 = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//        assert imm1 != null;
//        imm1.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
//    }
//
//
//    //Show-Hide
//    public void edtRequestKeyboard(final View view, final boolean isOpen) {
//
//            view.requestFocus();
//            view.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    InputMethodManager keyboard = (InputMethodManager)
//                            mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
//                    if (isOpen) {
//                            keyboard.showSoftInput(view, 0);
//                    } else {
//                            keyboard.hideSoftInputFromWindow(view.getWindowToken(), 0);
//                    }
//                }
//            }, 200);
//    }
//}
