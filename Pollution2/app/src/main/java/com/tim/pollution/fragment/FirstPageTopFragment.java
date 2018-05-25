package com.tim.pollution.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tim.pollution.MyApplication;
import com.tim.pollution.R;
import com.tim.pollution.activity.SiteWeatherDetailActivity;
import com.tim.pollution.bean.RegionWeather;
import com.tim.pollution.bean.weather.AQI24hBean;
import com.tim.pollution.callback.ICallBack;
import com.tim.pollution.general.Constants;
import com.tim.pollution.general.MData;
import com.tim.pollution.general.MDataType;
import com.tim.pollution.net.WeatherDal;
import com.tim.pollution.utils.DateUtil;
import com.tim.pollution.view.ProgressView;
import com.woodnaonly.arcprogress.ArcProgress;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;

/**
 * 首页-》上
 */
public class FirstPageTopFragment extends Fragment implements ICallBack, View.OnClickListener, AdapterView.OnItemClickListener {

    TextView weatherLocationTime;
    ArcProgress weatherArcProgress;
    ImageView weatherInfoImg;
    TextView weatherTemperature;
    TextView weatherWind;
    TextView weatherPm25;
    ProgressView weatherPm25Pro;
    TextView weatherPm10;
    ProgressView weatherPm10Pro;
    TextView weatherSo2;
    ProgressView weatherSo2Pro;
    TextView weatherNo2;
    ProgressView weatherNo2Pro;
    TextView weatherO3;
    ProgressView weatherO3Pro;
    TextView weatherCo;
    ProgressView weatherCoPro;
    ImageView weatherHealthTip;
    Unbinder unbinder1;
    private RegionWeather regionWeather;

    String regionId = "";
    String regionid ;
    private FragmentCallBack callBack;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //引用创建好的xml布局
        View view = inflater.inflate(R.layout.activity_weather, container, false);
        Bundle bundle = getArguments();
        regionId = bundle.getString("regionId");
        Log.e("tcy","接受的城市id01:"+regionId);
        findView(view);
        setClick();
        unbinder1 = ButterKnife.bind(this, view);
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRegionId(regionId);
    }


    /**
     * 加载数据
     * @param regionid
     */
    public void setRegionId(String regionid) {
        this.regionid = regionid;
        iniData();
    }

    /**
     * 点击事件
     */
    private void setClick() {
        weatherHealthTip.setOnClickListener(this);
    }

    /**
     * 获取数据
     */
    private void iniData() {
        Log.e("tcy","接受的城市id:"+regionId);
        Map<String, String> params = new HashMap<>();
        params.put("key", Constants.key);
        params.put("regionid", regionid);//// TODO: 2018/4/25 暂时写死
        WeatherDal.getInstance().getHomeData(params, this);
    }

    /**
     * 设置数据
     * @param regionWeather
     */
    private void setData(RegionWeather regionWeather) {
        if (regionWeather != null) {
            if (regionWeather.getMessage().getRegionList() != null) {
//                weatherTitleLocation.setText(regionWeather.getMessage().getRegionList().getRegionName());
                weatherLocationTime.setText(regionWeather.getMessage().getRegionList().getRegionName()+" " + switchTime(regionWeather.getMessage().getRegionList().getTime()));
                weatherArcProgress.setProgressTextTop(regionWeather.getMessage().getRegionList().getPollutionLevel());
                weatherArcProgress.setProgressTextBottom("首要污染物 " + regionWeather.getMessage().getRegionList().getTopPollution());
                weatherArcProgress.setProgress(getTopPollutionPrograss(regionWeather, regionWeather.getMessage().getRegionList().getTopPollution()));
                weatherArcProgress.setFinishedStrokeColor(getTopPollutionPrograssColor(regionWeather, regionWeather.getMessage().getRegionList().getTopPollution()));
                String name = "w" + regionWeather.getMessage().getRegionList().getWeathercode();
                weatherInfoImg.setImageResource(getImageResourceId(name));
                weatherTemperature.setText(regionWeather.getMessage().getRegionList().getTemperature() + " °C");
                weatherWind.setText(regionWeather.getMessage().getRegionList().getWind() +" "+regionWeather.getMessage().getRegionList().getWeather() +"\n" + "湿度" + regionWeather.getMessage().getRegionList().getHumidity() + "%");
                weatherPm25.setText(regionWeather.getMessage().getRegionList().getPM25());
                weatherPm25Pro.setMaxCount(500);
                weatherPm25Pro.setCurrentCount(getIntFromString(regionWeather.getMessage().getRegionList().getPM25()));
                weatherPm25Pro.setPrograssColor(regionWeather.getMessage().getRegionList().getPM25color());
                weatherPm10.setText(regionWeather.getMessage().getRegionList().getPM10());
                weatherPm10Pro.setMaxCount(500);
                weatherPm10Pro.setCurrentCount(getIntFromString(regionWeather.getMessage().getRegionList().getPM10()));
                weatherPm10Pro.setPrograssColor(regionWeather.getMessage().getRegionList().getPM10color());
                weatherSo2.setText(regionWeather.getMessage().getRegionList().getSO2());
                weatherSo2Pro.setMaxCount(500);
                weatherSo2Pro.setCurrentCount(getIntFromString(regionWeather.getMessage().getRegionList().getSO2()));
                weatherSo2Pro.setPrograssColor(regionWeather.getMessage().getRegionList().getSO2color());
                weatherNo2.setText(regionWeather.getMessage().getRegionList().getNO2());
                weatherNo2Pro.setMaxCount(500);
                weatherNo2Pro.setCurrentCount(getIntFromString(regionWeather.getMessage().getRegionList().getNO2()));
                weatherNo2Pro.setPrograssColor(regionWeather.getMessage().getRegionList().getNO2color());
                weatherO3.setText(regionWeather.getMessage().getRegionList().getO3());
                weatherO3Pro.setMaxCount(500);
                weatherO3Pro.setCurrentCount(getIntFromString(regionWeather.getMessage().getRegionList().getO3()));
                weatherO3Pro.setPrograssColor(regionWeather.getMessage().getRegionList().getO3color());
                weatherCo.setText(regionWeather.getMessage().getRegionList().getCO());
                weatherCoPro.setMaxCount(500);
                weatherCoPro.setCurrentCount(getIntFromString(regionWeather.getMessage().getRegionList().getCO()));
                weatherCoPro.setPrograssColor(regionWeather.getMessage().getRegionList().getCOcolor());
            }

        }


    }
    private String switchTime(String time) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            return sdf.format(DateUtil.strToDateLong(time));
        } catch (Exception e) {
            return time;
        }

    }
    private Activity activity;

    public Context getContext() {
        if (activity == null) {
            return MyApplication.getContext();
        }
        return activity;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
       callBack= (FragmentCallBack) getParentFragment();
    }

    /**
     * string-->int
     *
     * @param s
     * @return
     */
    private int getIntFromString(String s) {
        try {
            int i = (int) Math.ceil(Double.valueOf(s));
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    /**
     *  获取进度
     * @param regionWeather
     * @param top
     * @return
     */
    private double getTopPollutionPrograss(RegionWeather regionWeather, String top) {
/*        if (top.contains("PM25")||top.contains("PM2.5")) {
            return regionWeather.getMessage().getRegionList().getPM25();
        } else if (top.contains("PM10")) {
            return regionWeather.getMessage().getRegionList().getPM10();
        } else if (top.contains("SO2")) {
            return regionWeather.getMessage().getRegionList().getSO2();
        } else if (top.contains("NO2")) {
            return regionWeather.getMessage().getRegionList().getNO2();
        } else if (top.contains("O3")) {
            return regionWeather.getMessage().getRegionList().getO3();
        } else if (top.contains("CO")) {
            return regionWeather.getMessage().getRegionList().getCO();
        }*/
        try{
            return Double.valueOf(regionWeather.getMessage().getRegionList().getAQI());
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }

    }

    /**
     * 获取颜色值
     * @param regionWeather
     * @param top
     * @return
     */
    private int getTopPollutionPrograssColor(RegionWeather regionWeather, String top) {
     /*   if (top.contains("PM25")||top.contains("PM2.5")) {
            return Color.parseColor(regionWeather.getMessage().getRegionList().getPM25color());
        } else if (top.contains("PM10")) {
            return Color.parseColor( regionWeather.getMessage().getRegionList().getPM10color());
        } else if (top.contains("SO2")) {
            return Color.parseColor( regionWeather.getMessage().getRegionList().getSO2color());
        } else if (top.contains("NO2")) {
            return Color.parseColor( regionWeather.getMessage().getRegionList().getNO2color());
        } else if (top.contains("O3")) {
            return Color.parseColor( regionWeather.getMessage().getRegionList().getO3color());
        } else if (top.contains("CO")) {
            return Color.parseColor( regionWeather.getMessage().getRegionList().getCOcolor());
        }*/
        return  Color.parseColor( regionWeather.getMessage().getRegionList().getAQIcolor());
    }

    public int getImageResourceId(String name) {
        Context ctx = getContext();
        int resId=0;
        try{
            resId = getResources().getIdentifier(name, "mipmap", ctx.getPackageName());
        }catch (Exception e){
            e.printStackTrace();
        }
        //如果没有在"mipmap"下找到imageName,将会返回0
        return resId;
    }

    /**
     *
     */
    private void findView(View view) {
        weatherLocationTime = (TextView) view.findViewById(R.id.weather_location_time);
        weatherArcProgress = (ArcProgress) view.findViewById(R.id.weather_arcProgress);
        weatherInfoImg = (ImageView) view.findViewById(R.id.weather_info_img);
        weatherTemperature = (TextView) view.findViewById(R.id.weather_temperature);
        weatherWind = (TextView) view.findViewById(R.id.weather_wind);
        weatherPm25 = (TextView) view.findViewById(R.id.weather_pm25);
        weatherPm25Pro = (ProgressView) view.findViewById(R.id.weather_pm25_pro);
        weatherPm10 = (TextView) view.findViewById(R.id.weather_pm10);
        weatherPm10Pro = (ProgressView) view.findViewById(R.id.weather_pm10_pro);
        weatherSo2 = (TextView) view.findViewById(R.id.weather_so2);
        weatherSo2Pro = (ProgressView) view.findViewById(R.id.weather_so2_pro);
        weatherNo2 = (TextView) view.findViewById(R.id.weather_no2);
        weatherNo2Pro = (ProgressView) view.findViewById(R.id.weather_no2_pro);
        weatherO3 = (TextView) view.findViewById(R.id.weather_o3);
        weatherO3Pro = (ProgressView) view.findViewById(R.id.weather_o3_pro);
        weatherCo = (TextView) view.findViewById(R.id.weather_co);
        weatherCoPro = (ProgressView) view.findViewById(R.id.weather_co_pro);
        weatherHealthTip = (ImageView) view.findViewById(R.id.weather_health_tip);

    }

    @Override
    public void onSuccess(Object data) {
        Log.e("test","onSuccess regionId:"+regionId);
        MData mData = (MData) data;
        if (MDataType.REGION_WEATHER.equals(mData.getType())) {
            regionWeather = (RegionWeather) mData.getData();
            if (regionWeather != null) {
                Log.e("test","fragment 城市："+regionWeather.getMessage().getRegionList().getRegionName());
                setData(regionWeather);
                if (callBack != null) {
                    callBack.prograss(regionWeather.getMessage(),regionid);
                }
            }else{
                if (callBack != null) {
                    callBack.error(regionId);
                }
            }
        }
    }



    @Override
    public void onError(String msg, String eCode) {
        Toast.makeText(getContext(),msg,Toast.LENGTH_LONG).show();
        if (callBack != null) {
            callBack.error(regionId);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.weather_health_tip://健康提示
                showTip();
                break;
            case R.id.weather_title_location_select://选择城市

                break;

        }
    }

    /**
     * 温馨提示
     */
    private void showTip() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("温馨提示");
        builder.setMessage(regionWeather.getMessage().getRegionList().getHealthEffect() + "\n" + regionWeather.getMessage().getRegionList().getAdvice());
        builder.setIcon(R.mipmap.prompt);
        //点击对话框以外的区域是否让对话框消失
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        if (dialog != null) {
            //显示对话框
            dialog.show();
        }


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getContext(), SiteWeatherDetailActivity.class);
        intent.putExtra("pointcode", regionWeather.getMessage().getPoint_AQI().get(position).getPointCode());
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}

