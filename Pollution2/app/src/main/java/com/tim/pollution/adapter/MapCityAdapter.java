package com.tim.pollution.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tim.pollution.R;
import com.tim.pollution.activity.MapActivity;
import com.tim.pollution.bean.ClickMapBean;

import java.util.List;

/**
 * Created by lenovo on 2018/5/2.
 */

public class MapCityAdapter extends RecyclerView.Adapter<MapCityAdapter.ViewHolder> {

    private Context context;
    private List<ClickMapBean> datas;
    public MapCityAdapter(Context context, List<ClickMapBean> clickMapBeens) {
        this.context = context;
        this.datas = clickMapBeens;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_map_city,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvValue.setText(datas.get(position).getAQI());
        holder.tvName.setText(datas.get(position).getRegionName());
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvValue;
        public ViewHolder(View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.item_map_city_name_tv);
            tvValue = (TextView) itemView.findViewById(R.id.item_map_city_value_tv);
        }
    }
}
