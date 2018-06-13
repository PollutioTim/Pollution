package com.tim.pollution.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.*;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tim.pollution.R;
import com.tim.pollution.bean.LocationBean;
import com.tim.pollution.bean.RankLastBean;
import com.tim.pollution.bean.RankMainBean;

import java.util.List;

import static android.R.attr.data;

/**
 * Created by lenovo on 2018/4/25.
 */

public class RankLastAdapter extends BaseQuickAdapter<RankLastBean.Message.Content> {


    public RankLastAdapter( List<RankLastBean.Message.Content> data) {
        super(R.layout.item_rank_last, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, RankLastBean.Message.Content data) {
        holder.setText(R.id.item_last_number_tv,data.getRanking());
        holder.setText(R.id.item_last_city_tv,data.getName());
        holder.setText(R.id.item_last_pollution_tv,data.getMainpoll());
        holder.setText(R.id.item_last_vlaue_tv,data.getValue());
    }
}
