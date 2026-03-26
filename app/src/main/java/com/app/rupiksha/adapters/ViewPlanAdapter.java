package com.app.rupiksha.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.app.rupiksha.R;
import com.app.rupiksha.databinding.ViewPlanLayoutBinding;
import com.app.rupiksha.models.PlanDetailModel;

import java.util.List;

public class ViewPlanAdapter extends RecyclerView.Adapter<ViewPlanAdapter.ViewHolder> {
    private Context context;
    private List<PlanDetailModel> list;
    OnItemClick onItemClick;
    private int answerNum = -1;
    public ViewPlanAdapter(Context context, List<PlanDetailModel> stepList, OnItemClick onItemClick) {
        this.context = context;
        this.list = stepList;
        this.onItemClick=onItemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewPlanLayoutBinding view = DataBindingUtil.bind(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_plan_layout, parent, false));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        holder.binding.valuename.setText(""+list.get(position).getData());
        holder.binding.valuesvalidity.setText(""+list.get(position).getValidity());
        holder.binding.amount.setText(context.getResources().getString(R.string.rupees) +" "+list.get(position).getRs());
      /*  holder.binding.valuessms.setText(list.get(position).getSms()); */
        holder.binding.description.setText(list.get(position).getDesc());

        holder.binding.amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.onClick(position);
            }
        });
        setExpendableTextview(context,holder.binding.description,holder.binding.tvtitleview);


       /* holder.binding.viewmorelayout.setVisibility(position == answerNum ? View.VISIBLE : View.GONE);
        holder.binding.ivpaymentview.setRotation(position == answerNum ? 0 : 180);
        holder.binding.btnViewdetails.setOnClickListener(view -> {
            if(answerNum == position){
                answerNum = -1;
                holder.binding.tvtitleview.setText(context.getResources().getText(R.string.txt_view_more));
            }else{
                answerNum = position;
                holder.binding.tvtitleview.setText(context.getResources().getText(R.string.txt_view_less));
            }
            onItemClick.onClick(position);
        });*/


    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ViewPlanLayoutBinding binding;

        public ViewHolder(@NonNull ViewPlanLayoutBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }

    public static interface OnItemClick
    {
        public void onClick(int position);
    }

        public void setExpendableTextview(Context contaxt,TextView textview,TextView showMoreText){

            textview.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    textview.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    if (textview.getLineCount() > 2) {
                        // If it exceeds 2 lines, set maxLines to 2
                        textview.setMaxLines(2);
                        textview.setVisibility(View.VISIBLE);

                        showMoreText.setOnClickListener(view ->
                        {
                            if (showMoreText.getText().toString().equalsIgnoreCase("Show More")) {
                                textview.setMaxLines(Integer.MAX_VALUE);
                                showMoreText.setText("Show Less");
                            } else {
                                textview.setMaxLines(2);
                                showMoreText.setText("Show More");
                            }
                        });
                    }
                }
            });
    }
}

