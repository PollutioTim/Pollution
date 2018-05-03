package com.tim.pollution.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tim.pollution.R;
import com.tim.pollution.bean.changetrend.RegionNetBean;
import com.tim.pollution.bean.weather.PointAQIBean;

import java.util.List;

/**
 * Created by tcy on 2018/4/25.
 */

public class CitySpinnerAdapter extends BaseAdapter {
    private Context context;
    private List<RegionNetBean.RegionBean>regionBean;
    private LayoutInflater inflater;
    public CitySpinnerAdapter(Context context, List<RegionNetBean.RegionBean> regionBean){
        this.context=context;
        this.regionBean=regionBean;
        inflater=LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return regionBean.size();
    }

    @Override
    public Object getItem(int position) {
        return regionBean.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        RegionNetBean.RegionBean regionBean= (RegionNetBean.RegionBean) getItem(position);
        if(convertView==null){
            holder=new ViewHolder();
            convertView=inflater.inflate(R.layout.city_item,null);
            holder.tvCity= (TextView) convertView.findViewById(R.id.city_item_city);

            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        holder.tvCity.setText(regionBean.getRegionName());

        return convertView;
    }
    private ColorDrawable getDrawableFormString(String colorStr) {
        ColorDrawable colorDrawable = new ColorDrawable();
        int color = Color.parseColor(colorStr);
        colorDrawable.setColor(color);
        return colorDrawable;
    }
    class ViewHolder{
        TextView tvCity;

    }
}
