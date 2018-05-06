package com.tim.pollution.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tim.pollution.R;
import com.tim.pollution.activity.CityActivity;
import com.tim.pollution.activity.MapActivity;
import com.tim.pollution.bean.CityBean;
import com.tim.pollution.bean.changetrend.RegionNetBean;
import com.tim.pollution.callback.OnItemClickListener;
import com.tim.pollution.utils.CityListSaveUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by lenovo on 2018/4/27.
 */

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> {
    private Context context;
    private List<CityBean>data;
    private int selectedPosition = -5; //默认一个参数

    public CityAdapter(Context context, List<CityBean> cityBeens){
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

                //tcy  tijiao 
                List<String> regionBeans=CityListSaveUtil.getList(context,CityListSaveUtil.CITY_FILE, CityListSaveUtil.CITY_KEY);
                if(regionBeans==null){
                    regionBeans=new ArrayList<String>();
                }
                regionBeans.add(data.get(position).getRegionId());
                List newList = new ArrayList(new HashSet(regionBeans));
                CityListSaveUtil.putList(context,CityListSaveUtil.CITY_FILE, CityListSaveUtil.CITY_KEY,newList);
                //
                holder.ivSelect.setVisibility(View.VISIBLE);
                Intent intent =  new Intent(context, MapActivity.class);
                intent.putExtra("id",data.get(position).getRegionId());
                context.startActivity(intent);
                ((CityActivity)context).finish();
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
