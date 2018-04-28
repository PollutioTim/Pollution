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
import com.tim.pollution.bean.changetrend.ChangeTrend;
import com.tim.pollution.bean.changetrend.DataInfoBean;
import com.tim.pollution.bean.weather.PointAQIBean;

import java.util.List;

/**
 * Created by tcy on 2018/4/25.
 */

public class ChangeTrendAdapter extends BaseAdapter {
    private Context context;
    private List<DataInfoBean>dataInfoBeens;
    private LayoutInflater inflater;
    public ChangeTrendAdapter(Context context, List<DataInfoBean> dataInfoBeens){
        this.context=context;
        this.dataInfoBeens=dataInfoBeens;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return dataInfoBeens.size();
    }

    @Override
    public Object getItem(int position) {
        return dataInfoBeens.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        DataInfoBean dataInfoBeen= (DataInfoBean) getItem(position);
        if(convertView==null){
            holder=new ViewHolder();
            convertView=inflater.inflate(R.layout.weather_detail_list_item,null);
            holder.tvTime= (TextView) convertView.findViewById(R.id.weather_detail_list_item_time);
            holder.tvAQI= (TextView) convertView.findViewById(R.id.weather_detail_list_item_api);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        holder.tvTime.setText(dataInfoBeen.getTime());
        holder.tvAQI.setText(dataInfoBeen.getValue());

        return convertView;
    }
    private ColorDrawable getDrawableFormString(String colorStr) {
        ColorDrawable colorDrawable = new ColorDrawable();
        int color = Color.parseColor(colorStr);
        colorDrawable.setColor(color);
        return colorDrawable;
    }
    class ViewHolder{
        TextView tvTime;
        TextView tvAQI;
    }
}
