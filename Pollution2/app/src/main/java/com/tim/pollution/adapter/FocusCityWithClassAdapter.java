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
import com.tim.pollution.bean.changetrend.DataBankNetBean;
import com.tim.pollution.bean.changetrend.DataInfoBean;
import com.tim.pollution.bean.changetrend.RegionNetBean;
import com.tim.pollution.callback.OnItemClickListener;
import com.tim.pollution.utils.CityListSaveUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by lenovo on 2018/4/27.
 */

public class FocusCityWithClassAdapter extends RecyclerView.Adapter<FocusCityWithClassAdapter.ViewHolder>{
    private Context context;
    private List<RegionNetBean.RegionBean>data;
    private int selectedPosition = -5; //默认一个参数

//    private List<String> focusCity=new ArrayList<>();
    private  List<String> regionBeans;
    public FocusCityWithClassAdapter(Context context, List<RegionNetBean.RegionBean> cityBeens){
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
        RegionNetBean.RegionBean regionBean = getDataByPosition(position);

        if(regionBean!=null){
            Log.e("城市显示：",position+","+regionBean.getRegionName());
            if(regionBean.isClass()){
                holder.tvCityClass.setVisibility(View.VISIBLE);
                holder.tvCityClass.setText(regionBean.getRegionName());
                holder.llCity.setVisibility(View.GONE);
            }else{
                holder.llCity.setVisibility(View.VISIBLE);
                holder.tvCityClass.setVisibility(View.GONE);
                holder.tvName.setText(regionBean.getRegionName());
                regionBeans=CityListSaveUtil.getList(context,CityListSaveUtil.CITY_FILE, CityListSaveUtil.CITY_KEY);
                if(regionBeans==null){
                    regionBeans=new ArrayList<String>();
                }
                if(regionBeans.contains(regionBean.getRegionId())){
                    holder.ivSelect.setVisibility(View.VISIBLE);
                }else{
                    holder.ivSelect.setVisibility(View.INVISIBLE);
                }
                holder.llCity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RegionNetBean.RegionBean  regionBean=getDataByPosition(position);
                        //tcy  tijiao
                        if(regionBeans==null){
                            regionBeans=new ArrayList<String>();
                        }
                        if(regionBeans.contains(regionBean.getRegionId())){
                            holder.ivSelect.setVisibility(View.INVISIBLE);
                            regionBeans.remove(regionBean.getRegionId());
                        }else{
                            holder.ivSelect.setVisibility(View.VISIBLE);
                            regionBeans.add(regionBean.getRegionId());
                        }
                    }
                });
            }
        }

    }

  /*  class MyOnClickListener implements View.OnClickListener{
        int position;
        public MyOnClickListener(int position){
            this.position
        }
        @Override
        public void onClick(View v) {

        }
    }*/

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
