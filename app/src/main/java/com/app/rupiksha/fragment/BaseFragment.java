package com.app.rupiksha.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {

    private fragmentnavigationhelper fragmenthelper;
    private View view;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            fragmenthelper = (fragmentnavigationhelper) activity;
        } catch (Exception e) {
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(getlayoutid(), container, false);
        view.setClickable(true);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initviews(view, savedInstanceState);

    }

    public fragmentnavigationhelper gethelper() {
        return this.fragmenthelper;
    }

    public View getparentview() {
        return this.view;
    }

    public abstract int getlayoutid();


    public void initviews(View parent, Bundle savedInstanceState) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }


    public View findViewById(int id) {
        return getparentview().findViewById(id);
    }

    /**
     * An interface to load and make navigation. The parent mActivity must provide an implementation for this interface.
     *
     * @author khawarraza
     */
    public interface fragmentnavigationhelper {

        public void addFragment(BaseFragment f, boolean clearBackStack, boolean addToBackstack);
        public void addFragment(BaseFragment f, int layoutId, boolean clearBackStack, boolean addToBackstack);
        public void replaceFragment(BaseFragment f, boolean clearBackStack, boolean addToBackstack);
        public void replaceFragment(BaseFragment f, int layoutId, boolean clearBackStack, boolean addToBackstack);
        public void replaceFragment(BaseFragment f, View view, boolean clearBackStack, boolean addToBackstack);
        public void replaceFragment(BaseFragment f, View view, int layoutId, boolean clearBackStack, boolean addToBackstack);
        public void onBack();
        public void changeStatusBarColor(int color);
        public void blockScreenShotFragment();
        public String getCity(double mLat,double mLong);
        public String getLocationFromAddress(Context context, String strAddress);
        //public void xapipost_sendjson(Context mContext, String Action, HashMap<String, Object> mPairList, apiresponselistener mListener);
        //public basefragment getcurrentfragment();
    }
    @Override
    public void onPause() {
        super.onPause();
    }

}
