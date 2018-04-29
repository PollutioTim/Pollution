package com.tim.pollution.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tim.pollution.R;
import com.tim.pollution.bean.LevePollutionBean;

import java.util.List;

/**
 * Created by lenovo on 2018/4/29.
 */

public class MapColorAdapter extends RecyclerView.Adapter<MapColorAdapter.VeiwHolder> {

    private List<LevePollutionBean>data;
    private Context context;
    public MapColorAdapter(Context context ,List<LevePollutionBean>data){
        this.context = context;
        this.data = data;
    }
    @Override
    public VeiwHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_map_leve_color,parent,false);
        return new VeiwHolder(view);
    }

    @Override
    public void onBindViewHolder(VeiwHolder holder, int position) {
        holder.tvName.setText(data.get(position).getName());
        holder.color.setBackgroundColor(Color.parseColor(data.get(position).getColor()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class VeiwHolder extends RecyclerView.ViewHolder {
        View color;
        TextView tvName;
        public VeiwHolder(View itemView) {
            super(itemView);
            color = itemView.findViewById(R.id.item_map_color_view);
            tvName = (TextView) itemView.findViewById(R.id.item_map_name_tv);
        }
    }
}
