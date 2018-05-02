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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tim.pollution.R;
import com.tim.pollution.bean.weather.PointAQIBean;
import com.tim.pollution.utils.ViewUtils;

import java.util.List;

/**
 * Created by tcy on 2018/4/25.
 */

public class WeatherPointAdapter extends BaseAdapter {
    private Context context;
    private List<PointAQIBean>pointAQIBeanList;
    private LayoutInflater inflater;
    public WeatherPointAdapter(Context context, List<PointAQIBean> pointAQIBeanList){
        this.context=context;
        this.pointAQIBeanList=pointAQIBeanList;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return pointAQIBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return pointAQIBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        PointAQIBean pointAQIBean= (PointAQIBean) getItem(position);
        if(convertView==null){
            holder=new ViewHolder();
            convertView=inflater.inflate(R.layout.weather_point_item,null);
            holder.tvCity= (TextView) convertView.findViewById(R.id.weather_point_item_point);
            holder.tvTime= (TextView) convertView.findViewById(R.id.weather_point_item_time);
            holder.tvAQI= (TextView) convertView.findViewById(R.id.weather_point_item_vaule);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        holder.tvCity.setText(pointAQIBean.getPointName());
        holder.tvTime.setText(pointAQIBean.getTime());
        holder.tvAQI.setText(pointAQIBean.getAQI());
//        holder.tvAQI.setBackground(getDrawableFormString(pointAQIBean.getAQIcolor()));
        holder.tvAQI.setBackground(ViewUtils.getShapeDrawable(pointAQIBean.getAQIcolor()));

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
        TextView tvTime;
        TextView tvAQI;
    }
}
