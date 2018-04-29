package com.tim.pollution.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tim.pollution.R;
import com.tim.pollution.activity.CityActivity;
import com.tim.pollution.bean.CityBean;
import com.tim.pollution.bean.changetrend.RegionNetBean;

import java.util.List;

/**
 * Created by lenovo on 2018/4/27.
 */

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> {
    private Context context;
    private List<RegionNetBean.RegionBean>data;

    public CityAdapter(Context context, List<RegionNetBean.RegionBean> cityBeens){
        this.context = context;
        this.data = cityBeens;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_city,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.tvName.setText(data.get(position).getRegionName());
        holder.llCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.ivSelect.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        ImageView ivSelect;
        LinearLayout llCity;
        public ViewHolder(View itemView) {
            super(itemView);
            llCity = (LinearLayout) itemView.findViewById(R.id.item_city_ll);
            tvName = (TextView) itemView.findViewById(R.id.item_city_name_tv);
            ivSelect = (ImageView) itemView.findViewById(R.id.item_select_iv);
        }
    }
}
