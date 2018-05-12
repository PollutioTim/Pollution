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


    Unbinder unbinder;
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
        SeekBar seekBar;
        unbinder1 = ButterKnife.bind(this, view);
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setRegionId(regionId);

    }


    /**
     *
     */
    public void setRegionId(String regionid) {
        this.regionid = regionid;

        iniData();
    }

    /**
     * 点击事件
     */
    private void setClick() {
//        weatherDetail.setOnClickListener(this);
        weatherHealthTip.setOnClickListener(this);
//        weatherTitleLocationSelect.setOnClickListener(this);
    }


    String regionid ;


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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
//                weatherPm25Pro.setProgressTintList(ColorStateList.valueOf(Color.parseColor(regionWeather.getMessage().getRegionList().getPM25color())));
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
//        callBack = (FragmentCallBack) context;
//        FragmentManager manager = getFragmentManager();
//        callBack= (FragmentCallBack) manager.getFragments().get(0);
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

    private void initForm(RegionWeather regionWeather) {
//        weatherChart
        if (regionWeather.getMessage().getAQI_24h() != null) {
            List<AQI24hBean> list = regionWeather.getMessage().getAQI_24h();
            //每个集合显示几条柱子
            int numSubcolumns = 1;
            //显示多少个集合
            int numColumns = list.size();
            //保存所有的柱子
            List<Column> columns = new ArrayList<Column>();
            //保存每个竹子的值
            List<SubcolumnValue> values;
            List<AxisValue> axisXValues = new ArrayList<AxisValue>();
            List<AxisValue> axY = new ArrayList<AxisValue>();
            int value = 0;
            //对每个集合的柱子进行遍历
            for (int i = 0; i < numColumns; ++i) {

                values = new ArrayList<SubcolumnValue>();
                //循环所有柱子（list）
                for (int j = 0; j < numSubcolumns; ++j) {
                    //创建一个柱子，然后设置值和颜色，并添加到list中
                    if (list.get(i).getAQI() != null) {
                        values.add(new SubcolumnValue(Float.valueOf(list.get(i).getAQI()), Color.parseColor(list.get(i).getAQIcolor())));
                    }
                    //设置X轴的柱子所对应的属性名称
                    String time = list.get(i).getTime();
                    if (value == 5) {
                        value = 0;
                        Log.e("test", "....." + time);
                        axisXValues.add(new AxisValue(i).setLabel(time.substring(time.indexOf(" "))));
                    } else {
                        value++;
                    }
                }
                //将每个属性的拥有的柱子，添加到Column中
                Column column = new Column(values);
                //是否显示每个柱子的Lable
                if (list.get(i).getTime().contains("1:00") || list.get(i).getTime().contains("8:00") || list.get(i).getTime().contains("18:00") || list.get(i).getTime().contains("2:00")) {
                    column.setHasLabels(false);
                } else {
                    column.setHasLabels(false);
                }
                //设置每个柱子的Lable是否选中，为false，表示不用选中，一直显示在柱子上
                column.setHasLabelsOnlyForSelected(false);
                //将每个属性得列全部添加到List中
                columns.add(column);
            }

            //设置Columns添加到Data中
            ColumnChartData data = new ColumnChartData(columns);
            //设置X轴显示在底部，并且显示每个属性的Lable，字体颜色为黑色，X轴的名字为“学历”，每个柱子的Lable斜着显示，距离X轴的距离为8
            data.setAxisXBottom(new Axis(axisXValues).setHasLines(false).setTextColor(Color.WHITE).setHasTiltedLabels(false).setMaxLabelChars(3));
            //属性值含义同X轴
            data.setAxisYLeft(new Axis().setHasLines(false).setTextColor(Color.WHITE).setMaxLabelChars(10));
            //最后将所有值显示在View中
//            weatherChart.setColumnChartData(data);
        }


    }


    private ColorDrawable getDrawableFormString(String colorStr) {
        ColorDrawable colorDrawable = new ColorDrawable();
        int color = Color.parseColor(colorStr);
        colorDrawable.setColor(color);
        return colorDrawable;
    }

    private double getTopPollutionPrograss(RegionWeather regionWeather, String top) {
//        if (top.contains("PM25")||top.contains("PM2.5")) {
//            return regionWeather.getMessage().getRegionList().getPM25();
//        } else if (top.contains("PM10")) {
//            return regionWeather.getMessage().getRegionList().getPM10();
//        } else if (top.contains("SO2")) {
//            return regionWeather.getMessage().getRegionList().getSO2();
//        } else if (top.contains("NO2")) {
//            return regionWeather.getMessage().getRegionList().getNO2();
//        } else if (top.contains("O3")) {
//            return regionWeather.getMessage().getRegionList().getO3();
//        } else if (top.contains("CO")) {
//            return regionWeather.getMessage().getRegionList().getCO();
//        }
        try{
            return Double.valueOf(regionWeather.getMessage().getRegionList().getAQI());
        }catch (Exception e){
            e.printStackTrace();
            return 0;
        }

    }
    private int getTopPollutionPrograssColor(RegionWeather regionWeather, String top) {
//        if (top.contains("PM25")||top.contains("PM2.5")) {
//            return Color.parseColor(regionWeather.getMessage().getRegionList().getPM25color());
//        } else if (top.contains("PM10")) {
//            return Color.parseColor( regionWeather.getMessage().getRegionList().getPM10color());
//        } else if (top.contains("SO2")) {
//            return Color.parseColor( regionWeather.getMessage().getRegionList().getSO2color());
//        } else if (top.contains("NO2")) {
//            return Color.parseColor( regionWeather.getMessage().getRegionList().getNO2color());
//        } else if (top.contains("O3")) {
//            return Color.parseColor( regionWeather.getMessage().getRegionList().getO3color());
//        } else if (top.contains("CO")) {
//            return Color.parseColor( regionWeather.getMessage().getRegionList().getCOcolor());
//        }
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
//        weatherTitleLocation = (TextView) view.findViewById(R.id.weather_title_location);
//
//        textView = (TextView) view.findViewById(R.id.textView);
//
//        weatherTitleLocationSelect = (TextView) view.findViewById(R.id.weather_title_location_select);

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

//        weatherDetail = (TextView) view.findViewById(R.id.weather_detail);
//
//        weatherChart = (ColumnChartView) view.findViewById(R.id.weather_chart);
//
//        weatherPointList = (WrapContentListView) view.findViewById(R.id.weather_point_list);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onProgress(Object data) {
        Log.e("tcy","onProgress regionId:"+regionId);
        MData mData = (MData) data;
        if (MDataType.REGION_WEATHER.equals(mData.getType())) {
            regionWeather = (RegionWeather) mData.getData();
//            regionWeather = getTest();
            if (regionWeather != null) {
                Log.e("tcy","fragment 城市："+regionWeather.getMessage().getRegionList().getRegionName());
                setData(regionWeather);
                if (callBack != null) {
                    callBack.prograss(regionWeather.getMessage());
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
        Toast.makeText(getContext(),msg,Toast.LENGTH_LONG);
        if (callBack != null) {
            callBack.error(regionId);
        }

    }



    private RegionWeather getTest() {

        InputStreamReader inputStreamReader = null;
        try {
            InputStreamReader inputReader = new InputStreamReader(getResources().getAssets().open("test.json"));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null)
                Result += line;
            return new Gson().fromJson(Result, RegionWeather.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.weather_detail://变化趋势
//                Intent intent = new Intent(getContext(), WeatherVariationTrendActivity.class);
//                intent.putExtra("regionid", regionid);
//                startActivity(intent);
//                break;
            case R.id.weather_health_tip://健康提示
                showTip();
                break;
            case R.id.weather_title_location_select://选择城市

                break;

        }
    }

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

