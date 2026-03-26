package com.app.rupiksha.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.app.rupiksha.R;
import com.app.rupiksha.adapters.IntroPagerAdapter;
import com.app.rupiksha.databinding.ActivityIntroScreenBinding;
import com.app.rupiksha.models.IntroModal;
import com.app.rupiksha.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class IntroScreenActivity extends AppCompatActivity {

    private List<IntroModal> list;
    private IntroPagerAdapter adapter;
    private ActivityIntroScreenBinding binding;
    private Activity activity;
    private  Animation fadeinAnimation;
    private  Animation fadeoutAnimation;
    String from;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_screen);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_intro_screen);
        activity = IntroScreenActivity.this;
        list = new ArrayList<>();
        Utils.setStatusBarColor(activity,getResources().getColor(R.color.color_90CAF9,null));

        list.add(new IntroModal(getResources().getString(R.string.text_intro_1), R.drawable.intro_1));
        list.add(new IntroModal(getResources().getString(R.string.text_intro_2), R.drawable.intro_2));
        list.add(new IntroModal(getResources().getString(R.string.text_intro_3), R.drawable.intro_3));

        adapter = new IntroPagerAdapter(activity, list);
        binding.viewPager.setAdapter(adapter);
        binding.dotsIndicator.setViewPager(binding.viewPager);
        binding.tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();

            }
        });

        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.e("PositionValue",""+position);

                switch (position){
                    case 0:
                        binding.btnSuivant.setText(getResources().getString(R.string.text_next));
                        setAnimation(0);
                        break;
                    case 1:
                        binding.btnSuivant.setText(getResources().getString(R.string.text_next));
                        setAnimation(1);
                        break;
                    case 2:
                        binding.btnSuivant.setText(getResources().getString(R.string.txt_finish));
                        setAnimation(2);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        binding.btnSuivant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (binding.viewPager.getCurrentItem()) {
                    case 0:
                        binding.btnSuivant.setText(getResources().getString(R.string.text_next));
                        setAnimation(0);
                        binding.viewPager.setCurrentItem(1);
                        break;
                    case 1:
                        binding.btnSuivant.setText(getResources().getString(R.string.text_next));
                        setAnimation(1);
                        binding.viewPager.setCurrentItem(2);
                        break;
                    case 2:
                        binding.btnSuivant.setText(getResources().getString(R.string.txt_finish));
                        setAnimation(2);
                        startActivity(new Intent(activity, LoginActivity.class));
                        finish();
                        break;

                }

            }
        });

    }

    public void  setAnimation(int position){
        switch (position){
            case 0:
                if(binding.bgFirst.getVisibility()== View.GONE){
                    binding.bgFirst.setBackgroundColor(getResources().getColor(R.color.color_90CAF9,null));
                    Utils.setStatusBarColor(activity,getResources().getColor(R.color.color_90CAF9,null));
                    setFadeinAnimation(binding.bgFirst);
                    setFadeoutAnimation(binding.bgSecond);
                }else{
                    binding.bgSecond.setBackgroundColor(getResources().getColor(R.color.color_90CAF9,null));
                    Utils.setStatusBarColor(activity,getResources().getColor(R.color.color_90CAF9,null));
                    setFadeinAnimation(binding.bgSecond);
                    setFadeoutAnimation(binding.bgFirst);
                }
                break;
            case 1:
                if(binding.bgFirst.getVisibility()== View.GONE){
                    binding.bgFirst.setBackgroundColor(getResources().getColor(R.color.color_E6EE9C,null));
                    Utils.setStatusBarColor(activity,getResources().getColor(R.color.color_E6EE9C,null));
                    setFadeinAnimation(binding.bgFirst);
                    setFadeoutAnimation(binding.bgSecond);
                }else{
                    binding.bgSecond.setBackgroundColor(getResources().getColor(R.color.color_E6EE9C,null));
                    Utils.setStatusBarColor(activity,getResources().getColor(R.color.color_E6EE9C,null));
                    setFadeinAnimation(binding.bgSecond);
                    setFadeoutAnimation(binding.bgFirst);
                }
                break;
            case 2:
                if(binding.bgFirst.getVisibility()== View.GONE){
                    binding.bgFirst.setBackgroundColor(getResources().getColor(R.color.color_81D4FA,null));
                    Utils.setStatusBarColor(activity,getResources().getColor(R.color.color_81D4FA,null));
                    setFadeinAnimation(binding.bgFirst);
                    setFadeoutAnimation(binding.bgSecond);
                }else{
                    binding.bgSecond.setBackgroundColor(getResources().getColor(R.color.color_81D4FA,null));
                    Utils.setStatusBarColor(activity,getResources().getColor(R.color.color_81D4FA,null));
                    setFadeinAnimation(binding.bgSecond);
                    setFadeoutAnimation(binding.bgFirst);
                }
                break;
        }
    }

    public void setFadeinAnimation(View animationView){
        fadeinAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_in);

        fadeinAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                animationView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animationView.startAnimation(fadeinAnimation);

    }

    public void setFadeoutAnimation(View animationView){
        fadeinAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_out);

        fadeinAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                animationView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animationView.startAnimation(fadeinAnimation);

    }
}