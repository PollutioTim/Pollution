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
import com.tim.pollution.bean.changetrend.RegionNetBean;
import com.tim.pollution.callback.OnItemClickListener;
import com.tim.pollution.fragment.NewMapFragment;
import com.tim.pollution.general.MessageEvent;
import com.tim.pollution.utils.CityListSaveUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2018/4/27.
 */

public class MapSelectAdapter extends RecyclerView.Adapter<MapSelectAdapter.ViewHolder>{
    private Context context;
    private List<RegionNetBean.RegionBean>data;
    private int selectedPosition = -5; //默认一个参数

    private int tag ;

    private  List<String> regionBeans;
    public MapSelectAdapter(Context context, List<RegionNetBean.RegionBean> cityBeens){
        this.context = context;
        this.data = cityBeens;
        Log.e("lili","==================");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.focus_city_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (onItemClickListener != null) {
        holder.llCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(position);
            }
        });
        }

        if(data.get(position)!=null){
            Log.e("城市显示：",position+","+data.get(position).getRegionName());
            if(data.get(position).isClass()){
                holder.tvCityClass.setVisibility(View.VISIBLE);
                holder.tvCityClass.setText(data.get(position).getRegionName());
                holder.llCity.setVisibility(View.GONE);
            }else{
                holder.llCity.setVisibility(View.VISIBLE);
                holder.tvCityClass.setVisibility(View.GONE);
                holder.tvName.setText(data.get(position).getRegionName());

                if(regionBeans==null){
                    regionBeans=new ArrayList<String>();
                }
                if(regionBeans.contains(data.get(position).getRegionId())){
                    holder.ivSelect.setVisibility(View.VISIBLE);
                }else{
                    holder.ivSelect.setVisibility(View.INVISIBLE);
                }

            }
        }

    }

    @Override
    public int getItemCount() {
        int count=data.size();
        for (RegionNetBean.RegionBean regionBean:data ) {
            try{
                count+=regionBean.getRegionBeens().size()+1;
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        return count;
    }

    private RegionNetBean.RegionBean getDataByPosition(int position){
        try{
            int count=0;
            for(RegionNetBean.RegionBean regionBean:data){
                if((position+1-count)<=(regionBean.getRegionBeens().size()+1)){

                    if(position+1-count==1){
                        return  regionBean;
                    }else{
                        return regionBean.getRegionBeens().get(position-count-1);
                    }
                }
                count+=regionBean.getRegionBeens().size()+1;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        ImageView ivSelect;
        LinearLayout llCity;
        TextView tvCityClass;
        public ViewHolder(View itemView) {
            super(itemView);
            llCity = (LinearLayout) itemView.findViewById(R.id.focus_item_city_ll);
            tvName = (TextView) itemView.findViewById(R.id.focus_item_city_name_tv);
            ivSelect = (ImageView) itemView.findViewById(R.id.focus_item_select_iv);
            tvCityClass=(TextView)itemView.findViewById(R.id.focus_item_city_class);
        }
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
