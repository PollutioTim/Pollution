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
import com.tim.pollution.bean.RankLastBean;
import com.tim.pollution.bean.RankMainBean;

import java.util.List;

/**
 * Created by lenovo on 2018/4/25.
 */

public class RankLastAdapter extends RecyclerView.Adapter<RankLastAdapter.ViewHolder>{
    private Context context;
    private List<RankLastBean.Message> data;
    public RankLastAdapter(Context context, List<RankLastBean.Message>data){
        this.context = context;
        this.data = data;
    }
    @Override
    public RankLastAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_rank_last,parent,false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(RankLastAdapter.ViewHolder holder, int position) {

        holder.tvNumber.setText(data.get(position).getRanking());
        holder.tvCity.setText(data.get(position).getName());
        holder.tvPollution.setText(data.get(position).getMainpoll());
        holder.tvValue.setText(data.get(position).getValue());
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
            tvNumber = (TextView) itemView.findViewById(R.id.item_last_number_tv);
            tvCity = (TextView) itemView.findViewById(R.id.item_last_city_tv);
            tvPollution = (TextView) itemView.findViewById(R.id.item_last_pollution_tv);
            tvValue = (TextView) itemView.findViewById(R.id.item_last_vlaue_tv);
        }
    }
}