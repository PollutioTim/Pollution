package com.tim.pollution.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.SDKInitializer;
import com.google.gson.Gson;
import com.tim.pollution.R;
import com.tim.pollution.adapter.WeatherPointAdapter;
import com.tim.pollution.bean.RegionWeather;
import com.tim.pollution.bean.weather.AQI24hBean;
import com.tim.pollution.callback.ICallBack;
import com.tim.pollution.general.Constants;
import com.tim.pollution.general.MData;
import com.tim.pollution.general.MDataType;
import com.tim.pollution.net.WeatherDal;
import com.tim.pollution.utils.LocationUtil;
import com.tim.pollution.view.WrapContentListView;
import com.woodnaonly.arcprogress.ArcProgress;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.view.ColumnChartView;

/**
 * 首页
 */
public class WeatherActivity extends AppCompatActivity implements ICallBack, View.OnClickListener {

    @Bind(R.id.weather_title_location)
    TextView weatherTitleLocation;
    @Bind(R.id.textView)
    TextView textView;
    @Bind(R.id.weather_title_location_select)
    TextView weatherTitleLocationSelect;
    @Bind(R.id.weather_location_time)
    TextView weatherLocationTime;
    @Bind(R.id.weather_arcProgress)
    ArcProgress weatherArcProgress;
    @Bind(R.id.weather_info_img)
    ImageView weatherInfoImg;
    @Bind(R.id.weather_temperature)
    TextView weatherTemperature;
    @Bind(R.id.weather_wind)
    TextView weatherWind;
    @Bind(R.id.weather_pm25)
    TextView weatherPM25;
    @Bind(R.id.weather_pm25_pro)
    ProgressBar weatherPM25Pro;
    @Bind(R.id.weather_pm10)
    TextView weatherPm10;
    @Bind(R.id.weather_pm10_pro)
    ProgressBar weatherPm10Pro;
    @Bind(R.id.weather_so2)
    TextView weatherSo2;
    @Bind(R.id.weather_so2_pro)
    ProgressBar weatherSo2Pro;
    @Bind(R.id.weather_no2)
    TextView weatherNo2;
    @Bind(R.id.weather_no2_pro)
    ProgressBar weatherNo2Pro;
    @Bind(R.id.weather_o3)
    TextView weatherO3;
    @Bind(R.id.weather_o3_pro)
    ProgressBar weatherO3Pro;
    @Bind(R.id.weather_co)
    TextView weatherCo;
    @Bind(R.id.weather_co_pro)
    ProgressBar weatherCoPro;
    @Bind(R.id.weather_health_tip)
    TextView weatherHealthTip;
    @Bind(R.id.weather_detail)
    TextView weatherDetail;
    @Bind(R.id.weather_chart)
    ColumnChartView weatherChart;
    @Bind(R.id.weather_point_list)
    WrapContentListView weatherPointList;
    private RegionWeather regionWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        ButterKnife.bind(this);
        SDKInitializer.initialize(getApplicationContext());

        location();

        findView();
        setClick();
        iniData();
    }

    /**
     * 点击事件
     */
    private void setClick() {
        weatherDetail.setOnClickListener(this);
        weatherHealthTip.setOnClickListener(this);
    }

    private void location() {
        LocationUtil locationUtil = new LocationUtil(this, new ICallBack() {
            @Override
            public void onProgress(Object data) {
                MData mData = (MData) data;
                if (MDataType.MAP.equals(mData.getType())) {
                    BDLocation bdLocation = (BDLocation) mData.getData();
                    if (bdLocation != null) {

                    } else {
                        Log.e("提示：", "定位失败");
                    }
                }
            }

            @Override
            public void onError(String msg, String eCode) {
                Log.e("提示：", "定位失败");
            }
        });

    }

    String regionid = "140101";

    /**
     * 获取数据
     */
    private void iniData() {
        Map<String, String> params = new HashMap<>();
        params.put("key", Constants.key);
        params.put("regionid", regionid);//// TODO: 2018/4/25 暂时写死
        WeatherDal.getInstance().getHomeData(params, this);
    }

    private void setData(RegionWeather regionWeather) {
        if (regionWeather != null) {
            if (regionWeather.getMessage().getRegionList() != null) {
                weatherLocationTime.setText("地址 " + regionWeather.getMessage().getRegionList().getTime());
                weatherArcProgress.setProgressTextTop(regionWeather.getMessage().getRegionList().getPollutionLevel());
                weatherArcProgress.setProgressTextBottom(regionWeather.getMessage().getRegionList().getTopPollution());
                weatherArcProgress.setProgress(Double.valueOf(getTopPollutionPrograss(regionWeather, regionWeather.getMessage().getRegionList().getTopPollution())));
                String name = "R.mipmap.w" + regionWeather.getMessage().getRegionList().getWeathercode();
                weatherInfoImg.setImageResource(getImageResourceId(name));
                weatherTemperature.setText(regionWeather.getMessage().getRegionList().getTemperature() + " °C");
                weatherWind.setText(regionWeather.getMessage().getRegionList().getWind() + "\n" + "湿度" + regionWeather.getMessage().getRegionList().getHumidity() + "%");
                weatherPM25.setText(regionWeather.getMessage().getRegionList().getPM25());
                weatherPM25Pro.setProgress(getIntFromString(regionWeather.getMessage().getRegionList().getPM25()));
                weatherPM25Pro.setProgressDrawable(getDrawableFormString(regionWeather.getMessage().getRegionList().getPM25color()));
                weatherPm10.setText(regionWeather.getMessage().getRegionList().getPM10());
                weatherPm10Pro.setProgress(getIntFromString(regionWeather.getMessage().getRegionList().getPM10()));
                weatherPm10Pro.setProgressDrawable(getDrawableFormString(regionWeather.getMessage().getRegionList().getPM10color()));
                weatherSo2.setText(regionWeather.getMessage().getRegionList().getSO2());
                weatherSo2Pro.setProgress(getIntFromString(regionWeather.getMessage().getRegionList().getSO2()));
                weatherSo2Pro.setProgressDrawable(getDrawableFormString(regionWeather.getMessage().getRegionList().getSO2color()));
                weatherNo2.setText(regionWeather.getMessage().getRegionList().getNO2());
                weatherNo2Pro.setProgress(getIntFromString(regionWeather.getMessage().getRegionList().getNO2()));
                weatherNo2Pro.setProgressDrawable(getDrawableFormString(regionWeather.getMessage().getRegionList().getNO2color()));
                weatherO3.setText(regionWeather.getMessage().getRegionList().getO3());
                weatherO3Pro.setProgress(getIntFromString(regionWeather.getMessage().getRegionList().getO3()));
                weatherO3Pro.setProgressDrawable(getDrawableFormString(regionWeather.getMessage().getRegionList().getO3color()));
                weatherCo.setText(regionWeather.getMessage().getRegionList().getCO());
                weatherCoPro.setProgress(getIntFromString(regionWeather.getMessage().getRegionList().getCO()));
                weatherCoPro.setProgressDrawable(getDrawableFormString(regionWeather.getMessage().getRegionList().getCOcolor()));
                initForm(regionWeather);
                intList(regionWeather);
            }

        }


    }

    private void intList(RegionWeather regionWeather) {
        if (regionWeather != null) {
            if (regionWeather.getMessage().getPoint_AQI() != null) {
                weatherPointList.setAdapter(new WeatherPointAdapter(this, regionWeather.getMessage().getPoint_AQI()));
            }
        }
    }

    /**
     * string-->int
     *
     * @param s
     * @return
     */
    private int getIntFromString(String s) {
        int i = (int) Math.ceil(Double.valueOf(s));
        return i;
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
            //对每个集合的柱子进行遍历
            for (int i = 0; i < numColumns; ++i) {

                values = new ArrayList<SubcolumnValue>();
                //循环所有柱子（list）
                for (int j = 0; j < numSubcolumns; ++j) {
                    //创建一个柱子，然后设置值和颜色，并添加到list中
                    values.add(new SubcolumnValue(Float.valueOf(list.get(i).getAQI()), Color.parseColor(list.get(i).getAQIcolor())));
                    //设置X轴的柱子所对应的属性名称
                    String time = list.get(i).getTime();
                    if (list.get(i).getTime().contains("1:00") || list.get(i).getTime().contains("8:00") || list.get(i).getTime().contains("18:00") || list.get(i).getTime().contains("2:00")) {
                        axisXValues.add(new AxisValue(i).setLabel(time.substring(time.indexOf(" "))));
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
            data.setAxisXBottom(new Axis(axisXValues).setHasLines(false).setTextColor(Color.WHITE).setHasTiltedLabels(false).setMaxLabelChars(8));
            //属性值含义同X轴
            data.setAxisYLeft(new Axis().setHasLines(false).setTextColor(Color.WHITE).setMaxLabelChars(5));
            //最后将所有值显示在View中
            weatherChart.setColumnChartData(data);
        }


    }

    private ColorDrawable getDrawableFormString(String colorStr) {
        ColorDrawable colorDrawable = new ColorDrawable();
        int color = Color.parseColor(colorStr);
        colorDrawable.setColor(color);
        return colorDrawable;
    }

    private String getTopPollutionPrograss(RegionWeather regionWeather, String top) {
        if ("PM25".equals(top)) {
            return regionWeather.getMessage().getRegionList().getPM25();
        } else if ("PM10".equals(top)) {
            return regionWeather.getMessage().getRegionList().getPM10();
        } else if ("SO2".equals(top)) {
            return regionWeather.getMessage().getRegionList().getSO2();
        } else if ("NO2".equals(top)) {
            return regionWeather.getMessage().getRegionList().getNO2();
        } else if ("O3".equals(top)) {
            return regionWeather.getMessage().getRegionList().getO3();
        } else if ("CO".equals(top)) {
            return regionWeather.getMessage().getRegionList().getCO();
        }
        return "0";
    }

    public int getImageResourceId(String name) {
        Context ctx = getBaseContext();
        int resId = getResources().getIdentifier(name, "mipmap", ctx.getPackageName());
        //如果没有在"mipmap"下找到imageName,将会返回0
        return resId;
    }

    /**
     *
     */
    private void findView() {

    }

    @Override
    public void onProgress(Object data) {
        MData mData = (MData) data;
        if (MDataType.REGION_WEATHER.equals(mData.getType())) {
//            RegionWeather regionWeather = (RegionWeather) mData.getData();

            regionWeather = getTest();
            if (regionWeather != null) {
                setData(regionWeather);
            }

        }
    }

    @Override
    public void onError(String msg, String eCode) {

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
            case R.id.weather_detail://变化趋势
                Intent intent = new Intent(this, WeatherDetailActivity.class);
                intent.putExtra("regionid", regionid);
                startActivity(intent);
                break;
            case R.id.weather_health_tip://健康提示
                showTip();
                break;
            case R.id.weather_title_location_select://选择城市

                break;

        }
    }

    private void showTip() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("温馨提示");
        builder.setMessage(regionWeather.getMessage().getRegionList().getHealthEffect() + "\n" + regionWeather.getMessage().getRegionList().getAdvice());
        builder.setIcon(R.mipmap.prompt);
        //点击对话框以外的区域是否让对话框消失
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        //显示对话框
        dialog.show();

    }
}
