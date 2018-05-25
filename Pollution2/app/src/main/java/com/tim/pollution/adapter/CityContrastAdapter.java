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
import android.widget.Toast;

import com.tim.pollution.R;
import com.tim.pollution.bean.changetrend.ChangeTrendMessageBean;
import com.tim.pollution.bean.changetrend.DataInfoBean;
import com.tim.pollution.bean.weather.PointAQIBean;
import com.tim.pollution.utils.DateUtil;
import com.tim.pollution.utils.ViewUtils;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tcy on 2018/4/25.
 */

public class CityContrastAdapter extends BaseAdapter {
    private Context context;
    private Map<Integer, ChangeTrendMessageBean> map;
    private LayoutInflater inflater;
    private String code;
    public static String PM25 = "PM25";
    public static String PM10 = "PM10";
    public static String SO2 = "SO2";
    public static String O3 = "O3";
    public static String NO2 = "NO2";
    public static String CO = "CO";
    public static String AQI = "AQI";

    public CityContrastAdapter(Context context, Map<Integer, ChangeTrendMessageBean> map, String code) {
        this.context = context;
        this.map = map;
        this.code = code;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        Map<Integer, List<DataInfoBean>> datamap= getData();
        if(datamap!=null&&datamap.size()>0){
            List<DataInfoBean>data= datamap.get(1);
            if(data!=null){
                return data.size();
            }
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {

        return null;
    }

    /**
     * @return
     */
    private Map<Integer, List<DataInfoBean>> getData() {
        if (map == null || map.size() < 3) {
            return null;
        }
        Map<Integer, List<DataInfoBean>> dataInfoBeanMap = new HashMap<>();
        if (PM25.equals(code)) {
            dataInfoBeanMap.put(1, map.get(1).getPM25_data());
            dataInfoBeanMap.put(2, map.get(2).getPM25_data());
            dataInfoBeanMap.put(3, map.get(3).getPM25_data());
        } else if (PM10.equals(code)) {
            dataInfoBeanMap.put(1, map.get(1).getPM10_data());
            dataInfoBeanMap.put(2, map.get(2).getPM10_data());
            dataInfoBeanMap.put(3, map.get(3).getPM10_data());
        } else if (NO2.equals(code)) {
            dataInfoBeanMap.put(1, map.get(1).getNO2_data());
            dataInfoBeanMap.put(2, map.get(2).getNO2_data());
            dataInfoBeanMap.put(3, map.get(3).getNO2_data());
        } else if (SO2.equals(code)) {
            dataInfoBeanMap.put(1, map.get(1).getSO2_data());
            dataInfoBeanMap.put(2, map.get(2).getSO2_data());
            dataInfoBeanMap.put(3, map.get(3).getSO2_data());
        } else if (O3.equals(code)) {
            dataInfoBeanMap.put(1, map.get(1).getO3_data());
            dataInfoBeanMap.put(2, map.get(2).getO3_data());
            dataInfoBeanMap.put(3, map.get(3).getO3_data());
        } else if (CO.equals(code)) {
            dataInfoBeanMap.put(1, map.get(1).getCO_data());
            dataInfoBeanMap.put(2, map.get(2).getCO_data());
            dataInfoBeanMap.put(3, map.get(3).getCO_data());
        }else if(AQI.equals(code)){
            dataInfoBeanMap.put(1, map.get(1).getAQI_data());
            dataInfoBeanMap.put(2, map.get(2).getAQI_data());
            dataInfoBeanMap.put(3, map.get(3).getAQI_data());
        }
        return dataInfoBeanMap;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        DataInfoBean data1 = null;
        DataInfoBean data2 = null;
        DataInfoBean data3 = null;
        try{
            data1 = getData().get(1).get(position);
            data2 = getData().get(2).get(position);
            data3 = getData().get(3).get(position);
        }catch (Exception e){
            Toast.makeText(context,"数组越界，请检查数据",Toast.LENGTH_LONG).show();
        }

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.city_contrast_item, null);
            holder.tvCity = (TextView) convertView.findViewById(R.id.city_contrast_item_city);
            holder.tvCode1 = (TextView) convertView.findViewById(R.id.city_contrast_item_city1);
            holder.tvCode2 = (TextView) convertView.findViewById(R.id.city_contrast_item_city2);
            holder.tvCode3 = (TextView) convertView.findViewById(R.id.city_contrast_item_city3);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        if(data1!=null){
            holder.tvCity.setText(switchTime(data1.getTime()));
            holder.tvCode1.setText(data1.getValue());
            holder.tvCode1.setBackground(ViewUtils.getShapeDrawable(data1.getValuecolor()));
        }
        if(data2!=null){
            holder.tvCode2.setText(data2.getValue());
            holder.tvCode2.setBackground(ViewUtils.getShapeDrawable(data2.getValuecolor()));
        }
        if(data3!=null){
            holder.tvCode3.setText(data3.getValue());
            holder.tvCode3.setBackground(ViewUtils.getShapeDrawable(data3.getValuecolor()));
        }
        return convertView;
    }


    private String switchTime(String time){
        try{
            SimpleDateFormat sdf=new SimpleDateFormat("MM.dd HH:mm");
            return sdf.format(DateUtil.strToDateLong(time));
        }catch (Exception e){
            return time;
        }

    }


    class ViewHolder {
        TextView tvCity;
        TextView tvCode1;
        TextView tvCode2;
        TextView tvCode3;
    }
}
