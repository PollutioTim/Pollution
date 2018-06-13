package com.tim.pollution.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.*;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tim.pollution.R;
import com.tim.pollution.bean.MyData;
import com.tim.pollution.bean.RankLastBean;
import com.tim.pollution.bean.RankMainBean;
import com.tim.pollution.general.MData;
import com.tim.pollution.general.MDataType;

import java.util.List;

import static android.R.attr.data;

/**
 * Created by lenovo on 2018/4/25.
 */

public class RankAdapter extends BaseQuickAdapter<RankMainBean.Message.Content> {
    public RankAdapter(List<RankMainBean.Message.Content> data) {
        super(R.layout.item_rank,data);
    }

    @Override
    protected void convert(BaseViewHolder holder, RankMainBean.Message.Content data) {
        holder.setText(R.id.item_number_tv,data.getRanking());
        holder.setText(R.id.item_city_tv,data.getName());
        holder.setText(R.id.item_pollution_tv,data.getToppoll());
        TextView tvValue = holder.getView(R.id.item_vlaue_tv);
        tvValue.setText(data.getValue());
        tvValue.setBackgroundResource(R.drawable.shape_item_rank);
        // shape_left_select.xml里是一个shape元素，设置该元素的颜色为用户指定的颜色
        if(!TextUtils.isEmpty(data.getValueColor())){
            GradientDrawable drawable =(GradientDrawable)tvValue.getBackground();
            drawable.setColor(Color.parseColor(data.getValueColor()));
        }
    }
}
