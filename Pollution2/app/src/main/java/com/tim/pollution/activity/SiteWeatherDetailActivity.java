package com.tim.pollution.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tim.pollution.R;
import com.tim.pollution.bean.changetrend.DataInfoBean;
import com.tim.pollution.bean.changetrend.PointInfoNetBean;
import com.tim.pollution.callback.ICallBack;
import com.tim.pollution.general.Constants;
import com.tim.pollution.general.MData;
import com.tim.pollution.general.MDataType;
import com.tim.pollution.net.WeatherDal;
import com.tim.pollution.utils.DateUtil;
import com.tim.pollution.view.ProgressView;
import com.woodnaonly.arcprogress.ArcProgress;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.ColumnChartView;

/**
 * 站点天气详情
 */
public class SiteWeatherDetailActivity extends AppCompatActivity implements ICallBack {

    @BindView(R.id.weather_main_location_time)
    TextView weatherMainLocationTime;
    @BindView(R.id.weather_main_arcProgress)
    ArcProgress weatherMainArcProgress;
    @BindView(R.id.weather_main_info_img)
    ImageView weatherMainInfoImg;
    @BindView(R.id.weather_main_temperature)
    TextView weatherMainTemperature;
    @BindView(R.id.weather_main_wind)
    TextView weatherMainWind;
    @BindView(R.id.weather_main_pm25_va)
    TextView weatherMainPm25Va;
    @BindView(R.id.weather_main_pm25_pro)
    ProgressView weatherMainPm25Pro;
    @BindView(R.id.weather_main_no2_va)
    TextView weatherMainNo2Va;
    @BindView(R.id.weather_main_no2_pro)
    ProgressView weatherMainNo2Pro;
    @BindView(R.id.weather_main_pm10_va)
    TextView weatherMainPm10Va;
    @BindView(R.id.weather_main_pm10_pro)
    ProgressView weatherMainPm10Pro;
    @BindView(R.id.weather_main_o3_va)
    TextView weatherMainO3Va;
    @BindView(R.id.weather_main_o3_pro)
    ProgressView weatherMainO3Pro;
    @BindView(R.id.weather_main_so2_va)
    TextView weatherMainSo2Va;
    @BindView(R.id.weather_main_so2_pro)
    ProgressView weatherMainSo2Pro;
    @BindView(R.id.weather_main_co_va)
    TextView weatherMainCoVa;
    @BindView(R.id.weather_main_co_pro)
    ProgressView weatherMainCoPro;
    @BindView(R.id.weather_main_chart)
    ColumnChartView weatherMainChart;
    @BindView(R.id.weather_main_detail_rbpm25_type)
    RadioButton weatherMainDetailRbpm25Type;
    @BindView(R.id.weather_main_detail_rbpm10_type)
    RadioButton weatherMainDetailRbpm10Type;
    @BindView(R.id.weather_main_detail_rbso2_type)
    RadioButton weatherMainDetailRbso2Type;
    @BindView(R.id.weather_main_detail_rbno2_type)
    RadioButton weatherMainDetailRbno2Type;
    @BindView(R.id.weather_main_detail_rbo3_type)
    RadioButton weatherMainDetailRbo3Type;
    @BindView(R.id.weather_main_detail_rbco_type)
    RadioButton weatherMainDetailRbcoType;
    @BindView(R.id.weather_main_detail_rg_type)
    RadioGroup weatherMainDetailRgType;
    @BindView(R.id.weather_main_back)
    ImageView weatherMainBack;
    @BindView(R.id.weather_main_title)
    TextView weatherMainTitle;
    private String pointcode;
    private PointInfoNetBean pointInfoNetBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_main);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        pointcode = getIntent().getStringExtra("pointcode");
        loadData();
        weatherMainDetailRgType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                initChars2();
            }
        });
        weatherMainBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SiteWeatherDetailActivity.this.finish();
            }
        });

    }

    private void loadData() {
        Map<String, String> params = new HashMap<>();
        params.put("key", Constants.key);
        params.put("pointcode", pointcode);
        WeatherDal.getInstance().getPointData(params, this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onProgress(Object data) {
        MData mData = (MData) data;
        if (MDataType.POINT_INFONET_BEAN.equals(mData.getType())) {
            pointInfoNetBean = (PointInfoNetBean) mData.getData();
            initData();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initData() {
        if (pointInfoNetBean.getMessage() == null) {
            this.finish();
            return;
        }
        if (pointInfoNetBean.getMessage().getPointListBean() == null) {
            this.finish();
            return;
        }

        weatherMainTitle.setText(pointInfoNetBean.getMessage().getPointListBean().getPointName());
        weatherMainLocationTime.setText(pointInfoNetBean.getMessage().getPointListBean().getTime());
        weatherMainArcProgress.setProgressTextTop(pointInfoNetBean.getMessage().getPointListBean().getPollutionLevel());
        weatherMainArcProgress.setProgressTextBottom("首要污染物 " + pointInfoNetBean.getMessage().getPointListBean().getTopPollution());
        weatherMainArcProgress.setProgress(Double.valueOf(getTopPollutionPrograss(pointInfoNetBean, pointInfoNetBean.getMessage().getPointListBean().getTopPollution())));
        String name = "w" + pointInfoNetBean.getMessage().getPointListBean().getWeathercode();
        weatherMainInfoImg.setImageResource(getImageResourceId(name));
        weatherMainTemperature.setText(pointInfoNetBean.getMessage().getPointListBean().getTemperature() + " °C");
        weatherMainWind.setText(pointInfoNetBean.getMessage().getPointListBean().getWind() + "\n" + "湿度" + pointInfoNetBean.getMessage().getPointListBean().getHumidity() + "%");
        weatherMainPm25Va.setText(pointInfoNetBean.getMessage().getPointListBean().getPM25());
        weatherMainPm25Pro.setMaxCount(500);
        weatherMainPm25Pro.setCurrentCount(getIntFromString(pointInfoNetBean.getMessage().getPointListBean().getPM25()));
        weatherMainPm25Pro.setPrograssColor(pointInfoNetBean.getMessage().getPointListBean().getPM25color());
        weatherMainPm10Va.setText(pointInfoNetBean.getMessage().getPointListBean().getPM10());
        weatherMainPm10Pro.setMaxCount(500);
        weatherMainPm10Pro.setCurrentCount(getIntFromString(pointInfoNetBean.getMessage().getPointListBean().getPM10()));
        weatherMainPm10Pro.setPrograssColor(pointInfoNetBean.getMessage().getPointListBean().getPM10color());
        weatherMainSo2Va.setText(pointInfoNetBean.getMessage().getPointListBean().getSO2());
        weatherMainSo2Pro.setMaxCount(500);
        weatherMainSo2Pro.setCurrentCount(getIntFromString(pointInfoNetBean.getMessage().getPointListBean().getSO2()));
        weatherMainSo2Pro.setPrograssColor(pointInfoNetBean.getMessage().getPointListBean().getSO2color());
        weatherMainNo2Va.setText(pointInfoNetBean.getMessage().getPointListBean().getNO2());
        weatherMainNo2Pro.setMaxCount(500);
        weatherMainNo2Pro.setCurrentCount(getIntFromString(pointInfoNetBean.getMessage().getPointListBean().getNO2()));
        weatherMainNo2Pro.setPrograssColor(pointInfoNetBean.getMessage().getPointListBean().getNO2color());
        weatherMainO3Va.setText(pointInfoNetBean.getMessage().getPointListBean().getO3());
        weatherMainO3Pro.setMaxCount(500);
        weatherMainO3Pro.setCurrentCount(getIntFromString(pointInfoNetBean.getMessage().getPointListBean().getO3()));
        weatherMainO3Pro.setPrograssColor(pointInfoNetBean.getMessage().getPointListBean().getO3color());
        weatherMainCoVa.setText(pointInfoNetBean.getMessage().getPointListBean().getCO());
        weatherMainCoPro.setMaxCount(500);
        weatherMainCoPro.setCurrentCount(getIntFromString(pointInfoNetBean.getMessage().getPointListBean().getCO()));
        weatherMainCoPro.setPrograssColor(pointInfoNetBean.getMessage().getPointListBean().getCOcolor());
        //todo 颜色
        initChars2();

    }

    private void initChars() {
        if (pointInfoNetBean.getMessage() == null) {
            return;
        }
        List<DataInfoBean> datas = new ArrayList<>();
        int id = weatherMainDetailRgType.getCheckedRadioButtonId();
        if (id == R.id.weather_main_detail_rbpm25_type) {
            datas = pointInfoNetBean.getMessage().getPM25_24h();
        } else if (id == R.id.weather_main_detail_rbpm10_type) {
            datas = pointInfoNetBean.getMessage().getPM10_24h();
        } else if (id == R.id.weather_main_detail_rbso2_type) {
            datas = pointInfoNetBean.getMessage().getSO2_24h();
        } else if (id == R.id.weather_main_detail_rbno2_type) {
            datas = pointInfoNetBean.getMessage().getNO2_24h();
        } else if (id == R.id.weather_main_detail_rbo3_type) {
            datas = pointInfoNetBean.getMessage().getO3_24h();
        } else if (id == R.id.weather_main_detail_rbco_type) {
            datas = pointInfoNetBean.getMessage().getCO_24h();
        }
        if (datas != null) {
            List<DataInfoBean> list = datas;
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
            for (int i=0;i<=5;i++){
                axY.add(new AxisValue(i).setValue(i).setLabel(i*10+""));
            }
            //对每个集合的柱子进行遍历
            for (int i = 0; i < numColumns; ++i) {

                values = new ArrayList<SubcolumnValue>();
                //循环所有柱子（list）

                for (int j = 0; j < numSubcolumns; ++j) {
                    //创建一个柱子，然后设置值和颜色，并添加到list中
                    if (list.get(i).getValue() != null) {
                        float value = Float.valueOf(list.get(i).getValue());
                        Log.e("tcy","value:"+value);
//                        values.add(new SubcolumnValue(Float.valueOf(list.get(i).getValue())/10, Color.parseColor(list.get(i).getValuecolor())));
                        values.add(new SubcolumnValue(Float.valueOf(list.get(i).getValue()), Color.parseColor(list.get(i).getValuecolor())));
                    }
                    //设置X轴的柱子所对应的属性名称
                    axisXValues.add(new AxisValue(i).setLabel(switchTime(list.get(i).getTime())));


                }
                //将每个属性的拥有的柱子，添加到Column中
                Column column = new Column(values);
                //是否显示每个柱子的Lable
                column.setHasLabels(false);
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
            data.setAxisYLeft(new Axis(axY).setHasLines(false).setTextColor(Color.WHITE).setMaxLabelChars(5));
            //最后将所有值显示在View中
            weatherMainChart.setColumnChartData(data);
            weatherMainChart.setZoomEnabled(true);
        }
    }

    private  void initChars2(){
        if (pointInfoNetBean.getMessage() == null) {
            return;
        }
        List<DataInfoBean> datas = new ArrayList<>();
        int id = weatherMainDetailRgType.getCheckedRadioButtonId();
        if (id == R.id.weather_main_detail_rbpm25_type) {
            datas = pointInfoNetBean.getMessage().getPM25_24h();
        } else if (id == R.id.weather_main_detail_rbpm10_type) {
            datas = pointInfoNetBean.getMessage().getPM10_24h();
        } else if (id == R.id.weather_main_detail_rbso2_type) {
            datas = pointInfoNetBean.getMessage().getSO2_24h();
        } else if (id == R.id.weather_main_detail_rbno2_type) {
            datas = pointInfoNetBean.getMessage().getNO2_24h();
        } else if (id == R.id.weather_main_detail_rbo3_type) {
            datas = pointInfoNetBean.getMessage().getO3_24h();
        } else if (id == R.id.weather_main_detail_rbco_type) {
            datas = pointInfoNetBean.getMessage().getCO_24h();
        }else if(id==R.id.weather_main_detail_rbAQI_type){
            datas=pointInfoNetBean.getMessage().getAQI_24h();
        }
        if (datas != null) {
            List<DataInfoBean> list = datas;
            //每个集合显示几条柱子
            int numSubcolumns = 1;
            //显示多少个集合
            int numColumns = list.size();
            //保存所有的柱子
            List<Column> columns = new ArrayList<Column>();
            //保存每个竹子的值
            List<SubcolumnValue> values;
            List<AxisValue> axisXValues = new ArrayList<AxisValue>();
            //对每个集合的柱子进行遍历
            for (int i = 0; i < numColumns; ++i) {

                values = new ArrayList<SubcolumnValue>();
                //循环所有柱子（list）
                for (int j = 0; j < numSubcolumns; ++j) {
                    //创建一个柱子，然后设置值和颜色，并添加到list中
                    values.add(new SubcolumnValue(Float.valueOf(list.get(i).getValue()), Color.parseColor(list.get(i).getValuecolor())));
                    //设置X轴的柱子所对应的属性名称
                    axisXValues.add(new AxisValue(i).setLabel(switchTime(list.get(i).getTime())));
                }
                //将每个属性的拥有的柱子，添加到Column中
                Column column = new Column(values);
                //是否显示每个柱子的Lable
                column.setHasLabels(false);
                //设置每个柱子的Lable是否选中，为false，表示不用选中，一直显示在柱子上
                column.setHasLabelsOnlyForSelected(false);

                //将每个属性得列全部添加到List中
                columns.add(column);
            }
            List<AxisValue> axisYValues = new ArrayList<AxisValue>();
            for (int i=0;i<=500;i+=50){
                axisYValues.add(new AxisValue(i).setValue(i).setLabel(i+""));
            }
            //设置Columns添加到Data中
            ColumnChartData data = new ColumnChartData(columns);
            //设置X轴显示在底部，并且显示每个属性的Lable，字体颜色为黑色，X轴的名字为“学历”，每个柱子的Lable斜着显示，距离X轴的距离为8
            data.setAxisXBottom(new Axis(axisXValues).setHasLines(false).setTextColor(Color.WHITE).setName("").setHasTiltedLabels(false).setMaxLabelChars(8));
            //属性值含义同X轴
            data.setAxisYLeft(new Axis(axisYValues).setHasLines(false).setAutoGenerated(true).setName("").setTextColor(Color.WHITE).setMaxLabelChars(3));
            //最后将所有值显示在View中
            weatherMainChart.setColumnChartData(data);
            Viewport v = new Viewport(weatherMainChart.getMaximumViewport());
            v.right=30;
            v.top= 500;
            weatherMainChart.setCurrentViewport(v);
        }
    }

    private String switchTime(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(DateUtil.strToDateLong(time));
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

    public int getImageResourceId(String name) {
        Context ctx = getBaseContext();
        int resId = getResources().getIdentifier(name, "mipmap", ctx.getPackageName());
        //如果没有在"mipmap"下找到imageName,将会返回0
        return resId;
    }

    private String getTopPollutionPrograss(PointInfoNetBean pointInfoNetBean, String top) {
        if (top.contains("PM25")) {
            return pointInfoNetBean.getMessage().getPointListBean().getPM25();
        } else if (top.contains("PM10")) {
            return pointInfoNetBean.getMessage().getPointListBean().getPM10();
        } else if (top.contains("SO2")) {
            return pointInfoNetBean.getMessage().getPointListBean().getSO2();
        } else if (top.contains("NO2")) {
            return pointInfoNetBean.getMessage().getPointListBean().getNO2();
        } else if (top.contains("O3")) {
            return pointInfoNetBean.getMessage().getPointListBean().getO3();
        } else if (top.contains("CO")) {
            return pointInfoNetBean.getMessage().getPointListBean().getCO();
        }
        return "0";
    }

    @Override
    public void onError(String msg, String eCode) {
        Toast.makeText(this,msg,Toast.LENGTH_LONG);

    }
}
