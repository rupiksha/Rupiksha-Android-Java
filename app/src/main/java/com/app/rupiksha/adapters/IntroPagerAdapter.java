package com.app.rupiksha.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;


import com.app.rupiksha.R;
import com.app.rupiksha.models.IntroModal;

import java.util.List;

public class IntroPagerAdapter extends PagerAdapter {
    TextView textView, textView1;
    ImageView imageView, imageView1;
    private Activity activity;
    private LayoutInflater layoutInflater;
    private List<IntroModal> list;

    public IntroPagerAdapter(Activity activity, List<IntroModal> list) {
        this.activity = activity;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.onboarding_screen_1, null);

        if (position == 1) {
            view = layoutInflater.inflate(R.layout.onboarding_screen_2, null);

        } else if (position == 2) {
            view = layoutInflater.inflate(R.layout.onboarding_screen_3, null);

        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
