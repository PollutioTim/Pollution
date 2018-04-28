package com.tim.pollution.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tim.pollution.R;
import com.tim.pollution.bean.MyData;
import com.tim.pollution.bean.RankLastBean;
import com.tim.pollution.bean.RankMainBean;
import com.tim.pollution.general.MData;
import com.tim.pollution.general.MDataType;

import java.util.List;

/**
 * Created by lenovo on 2018/4/25.
 */

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.ViewHolder> {
    private Context context;
    private List<RankMainBean.Message>data;

    public RankAdapter(Context context,List<RankMainBean.Message>data){
        this.context = context;
        this.data = data;
    }
    @Override
    public RankAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_rank,parent,false);
            return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(RankAdapter.ViewHolder holder, int position) {
            holder.tvNumber.setText(data.get(position).getRanking());
            holder.tvCity.setText(data.get(position).getName());
            holder.tvPollution.setText(data.get(position).getToppoll());
            holder.tvValue.setText(data.get(position).getValue());
            holder.tvValue.setBackgroundResource(R.drawable.shape_item_rank);
            // shape_left_select.xml里是一个shape元素，设置该元素的颜色为用户指定的颜色
            GradientDrawable drawable =(GradientDrawable)holder.tvValue.getBackground();
            drawable.setColor(Color.parseColor(data.get(position).getValueColor()));
    }

    @Override
    public int getItemCount() {
        return data.size() ;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNumber;
        TextView tvCity;
        TextView tvPollution;
        TextView tvValue;
        public ViewHolder(View itemView) {
            super(itemView);
            tvNumber = (TextView) itemView.findViewById(R.id.item_number_tv);
            tvCity = (TextView) itemView.findViewById(R.id.item_city_tv);
            tvPollution = (TextView) itemView.findViewById(R.id.item_pollution_tv);
            tvValue = (TextView) itemView.findViewById(R.id.item_vlaue_tv);
        }
    }
}
