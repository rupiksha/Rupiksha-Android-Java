package com.app.rupiksha.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.rupiksha.R;
import com.app.rupiksha.adapters.ViewPlanAdapter;
import com.app.rupiksha.databinding.ActivityViewRechargePlanBinding;
import com.app.rupiksha.models.AllPlans;
import com.app.rupiksha.models.PlanDataModel;
import com.app.rupiksha.models.PlanDetailModel;
import com.app.rupiksha.utils.Utils;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class  ViewRechargePlanActivity extends AppCompatActivity
{
    ActivityViewRechargePlanBinding binding;
    Activity activity;
    List<PlanDataModel> categoryList=new ArrayList<>();
    List<AllPlans> allPlan=new ArrayList<>();
    List<PlanDetailModel> planList=new ArrayList<>();
    TextView tab_name;
    int cat_id = -1;
    ViewPlanAdapter adapter;
    boolean flag=false;
    int amount;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_view_recharge_plan);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_view_recharge_plan);
        activity=ViewRechargePlanActivity.this;
        binding.includeLayout.toolBar.setTitle(getResources().getString(R.string.label_view_plans));
        binding.includeLayout.toolBar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });

       /* if (getIntent().hasExtra("flag"))
            flag = getIntent().getBooleanExtra("flag",false);*/

      if (getIntent().hasExtra("title")) {
            categoryList = (List<PlanDataModel>) getIntent().getSerializableExtra("title");
        }

        if (getIntent().hasExtra("data")) {
            allPlan = (List<AllPlans>) getIntent().getSerializableExtra("data");
        }

         if(categoryList.size()>0)
         {
             cat_id=categoryList.get(0).getOpCatId();
         }
         Log.e("catdescription", String.valueOf(allPlan));
         Log.e("catlist", String.valueOf(categoryList));
        setCategoryTab(categoryList);


    }

    private void setPlanCategory()
    {
        adapter = new ViewPlanAdapter(activity, planList, new ViewPlanAdapter.OnItemClick() {
            @Override
            public void onClick(int position)
            {
                flag=true;
                amount=planList.get(position).getRs();
                onBackPressed();
            }
        });
        binding.bbpsrecycler.setLayoutManager(new LinearLayoutManager(activity));
        binding.bbpsrecycler.setItemAnimator(new DefaultItemAnimator());
        binding.bbpsrecycler.setAdapter(adapter);
    }

    public void setCategoryTab(List<PlanDataModel> categoryList)
    {
        for (int i = 0; i < categoryList.size(); i++)
        {
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText(categoryList.get(i).getOpCatName()));
        }

        for (int i = 0; i < binding.tabLayout.getTabCount(); i++)
        {
            binding.tabLayout.getTabAt(i).setCustomView(R.layout.layout_custom_tab);

            tab_name = (TextView) binding.tabLayout.getTabAt(i).getCustomView().findViewById(R.id.txt_tab_name);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            params.setMargins(Utils.convertSpToPixel(i == 0 ? 16 : 0, activity), 0, Utils.convertSpToPixel((i == binding.tabLayout.getTabCount() - 1) ? 16 : 0, activity), 0);
            tab_name.setLayoutParams(params);
            tab_name.setBackground(ContextCompat.getDrawable(activity, R.drawable.tablayout_selector_bg));
            tab_name.setSelected(i == 0);
            tab_name.setText("" + binding.tabLayout.getTabAt(i).getText().toString());
            if (i == 0) {
                binding.tabLayout.getTabAt(i).view.setPadding(Utils.convertDpToPixel(16, activity), 0, Utils.convertDpToPixel(2, activity), 0);
            } else if (i == binding.tabLayout.getTabCount() - 1) {
                binding.tabLayout.getTabAt(i).view.setPadding(Utils.convertDpToPixel(2, activity), 0, Utils.convertDpToPixel(16, activity), 0);
            } else {
                binding.tabLayout.getTabAt(i).view.setPadding(Utils.convertDpToPixel(2, activity), 0, Utils.convertDpToPixel(2, activity), 0);
            }
        }

        setTabListener();

        if(cat_id !=-1)
        {
            new Handler(Looper.myLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < binding.tabLayout.getTabCount(); i++)
                    {
                        if(categoryList != null && i <=(categoryList.size()-1)) {
                            if (cat_id == categoryList.get(i).getOpCatId()) {
                                binding.tabLayout.getTabAt(i).select();
                            }
                        }
                    }
                }
            },100);
        }else{
            cat_id = categoryList.get(0).getOpCatId();
            getProductOfCategory();
        }
    }

    private void setTabListener()
    {
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                cat_id = categoryList.get(tab.getPosition()).getOpCatId();
                setTabColor();
                getProductOfCategory();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab)
            {
                setTabColor();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab)
            {
                cat_id = categoryList.get(tab.getPosition()).getOpCatId();
                setTabColor();
                getProductOfCategory();
            }
        });
    }

    private void getProductOfCategory()
    {
        if(categoryList.size()>0)
        {
                for(int j=0;j<allPlan.size();j++)
                {
                    if(cat_id==allPlan.get(j).getCatId())
                    {
                        if(planList.size()>0)
                            planList.clear();

                        planList.addAll(allPlan.get(j).getCatArray());
                        setPlanCategory();
                    }
                }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("flag", flag);
        intent.putExtra("amount", amount);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

    private void setTabColor()
    {

        for (int i = 0; i < binding.tabLayout.getTabCount(); i++)
        {
          tab_name.setTextColor(getResources().getColor(R.color.tablayout_selector_color));
          tab_name.setBackground(ContextCompat.getDrawable(activity, R.drawable.tablayout_selector_bg));

        }
    }
}