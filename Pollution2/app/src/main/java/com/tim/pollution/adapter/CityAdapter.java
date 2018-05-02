package com.tim.pollution.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tim.pollution.R;
import com.tim.pollution.bean.changetrend.RegionNetBean;
import com.tim.pollution.callback.OnItemClickListener;

import java.util.List;

/**
 * Created by lenovo on 2018/4/27.
 */

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> {
    private Context context;
    private List<RegionNetBean.RegionBean>data;
    private int selectedPosition = -5; //默认一个参数

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
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tvName.setText(data.get(position).getRegionName());
        holder.llCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               for(RegionNetBean.RegionBean bean:data){
                    bean.setClick(false);
               }
                data.get(position).setClick(true);
                Log.e("lili","position="+position+"boolean="+data.get(position).getClick());
                if(data.get(position).getClick() == true){
                    holder.ivSelect.setVisibility(View.VISIBLE);
                    Log.e("lili","------------------");
                }else{
                    Log.e("lili","++++++++++++++++++");
                    holder.ivSelect.setVisibility(View.GONE);
                }
                notifyDataSetChanged();
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

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
