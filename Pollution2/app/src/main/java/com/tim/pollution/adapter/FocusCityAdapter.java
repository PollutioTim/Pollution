package com.tim.pollution.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
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

public class FocusCityAdapter extends RecyclerView.Adapter<FocusCityAdapter.ViewHolder> {
    private Context context;
    private List<RegionNetBean.RegionBean>data;
    private int selectedPosition = -5; //默认一个参数

//    private List<String> focusCity=new ArrayList<>();
    private List<String> regionBeans;
    public FocusCityAdapter(Context context, List<RegionNetBean.RegionBean> cityBeens){
        this.context = context;
        this.data = cityBeens;
        regionBeans=CityListSaveUtil.getList(context,CityListSaveUtil.CITY_FILE, CityListSaveUtil.CITY_KEY);
//        focusCity.addAll(regionBeans);

    }

    public List<String> getFocusCity(){
        return regionBeans;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.focus_city_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tvName.setText(data.get(position).getRegionName());
        regionBeans=CityListSaveUtil.getList(context,CityListSaveUtil.CITY_FILE, CityListSaveUtil.CITY_KEY);
        if(regionBeans==null){
            regionBeans=new ArrayList<String>();
        }
        if(regionBeans.contains(data.get(position).getRegionId())){
            holder.ivSelect.setVisibility(View.VISIBLE);
        }else{
            holder.ivSelect.setVisibility(View.INVISIBLE);
        }
        holder.llCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //tcy  tijiao 

                if(regionBeans==null){
                    regionBeans=new ArrayList<String>();
                }

                //
                if(regionBeans.contains(data.get(position).getRegionId())){
                    holder.ivSelect.setVisibility(View.INVISIBLE);
                    regionBeans.remove(data.get(position).getRegionId());
//                    CityListSaveUtil.putList(context,CityListSaveUtil.CITY_FILE, CityListSaveUtil.CITY_KEY,regionBeans);
                }else{
                    holder.ivSelect.setVisibility(View.VISIBLE);
                    regionBeans.add(data.get(position).getRegionId());
                    List newList = new ArrayList(new HashSet(regionBeans));
//                    CityListSaveUtil.putList(context,CityListSaveUtil.CITY_FILE, CityListSaveUtil.CITY_KEY,newList);
                }

//                Intent intent =  new Intent(context, MapActivity.class);
//                intent.putExtra("id",data.get(position).getRegionId());
//                context.startActivity(intent);
//                ((CityActivity)context).finish();
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
            llCity = (LinearLayout) itemView.findViewById(R.id.focus_item_city_ll);
            tvName = (TextView) itemView.findViewById(R.id.focus_item_city_name_tv);
            ivSelect = (ImageView) itemView.findViewById(R.id.focus_item_select_iv);
        }
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
