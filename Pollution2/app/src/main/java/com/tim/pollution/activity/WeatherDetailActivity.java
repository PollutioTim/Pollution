package com.tim.pollution.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.tim.pollution.R;
import com.tim.pollution.adapter.ChangeTrendAdapter;
import com.tim.pollution.bean.changetrend.ChangeTrend;
import com.tim.pollution.bean.changetrend.DataInfoBean;
import com.tim.pollution.bean.weather.AQI24hBean;
import com.tim.pollution.callback.ICallBack;
import com.tim.pollution.general.Constants;
import com.tim.pollution.general.MData;
import com.tim.pollution.general.MDataType;
import com.tim.pollution.net.WeatherDal;
import com.tim.pollution.view.WrapContentListView;

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
import lecho.lib.hellocharts.view.ColumnChartView;

public class WeatherDetailActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, ICallBack {

    String regionid;

    @BindView(R.id.weather_detail_rg_time)
    RadioGroup weatherDetailRgTime;
    @BindView(R.id.weather_detail_chart)
    ColumnChartView weatherDetailChart;
    @BindView(R.id.weather_detail_rbpm25_type)
    RadioButton weatherDetailRbpm25Type;
    @BindView(R.id.weather_detail_rbpm10_type)
    RadioButton weatherDetailRbpm10Type;
    @BindView(R.id.weather_detail_rbso2_type)
    RadioButton weatherDetailRbso2Type;
    @BindView(R.id.weather_detail_rbno2_type)
    RadioButton weatherDetailRbno2Type;
    @BindView(R.id.weather_detail_rbo3_type)
    RadioButton weatherDetailRbo3Type;
    @BindView(R.id.weather_detail_rbco_type)
    RadioButton weatherDetailRbcoType;
    @BindView(R.id.weather_detail_rg_type)
    RadioGroup weatherDetailRgType;
    @BindView(R.id.weather_detail_list)
    WrapContentListView weatherDetailList;
    @BindView(R.id.weather_detail_rb12_time)
    RadioButton weatherDetailRb12Time;
    @BindView(R.id.weather_detail_rb24_time)
    RadioButton weatherDetailRb24Time;
    @BindView(R.id.weather_detail_rb30_time)
    RadioButton weatherDetailRb30Time;
    private ChangeTrend changeTrend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_detail);
        ButterKnife.bind(this);
        regionid = getIntent().getStringExtra("regionid");
        setClick();

    }

    private void setClick() {
        weatherDetailRgTime.setOnCheckedChangeListener(this);
        weatherDetailRgType.setOnCheckedChangeListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadData("12h");

    }

    @Override
    public void onClick(View v) {

    }

    private void loadData(String type) {
        Map<String, String> params = new HashMap<>();
        params.put("regionid", regionid);
        params.put("key", Constants.key);
        params.put("type", type);
        WeatherDal.getInstance().getPollTrend(params, this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        if (group.getId() == R.id.weather_detail_rg_time) {
            switch (checkedId) {
                case R.id.weather_detail_rb12_time:
                    loadData("12h");
                    value_code=2;
                    break;
                case R.id.weather_detail_rb24_time:
                    loadData("24h");
                    value_code=5;
                    break;
                case R.id.weather_detail_rb30_time:
                    loadData("day");
                    value_code=6;
                    break;
            }
        }else {
            initList();
        }
    }

    @Override
    public void onProgress(Object data) {
        MData mData = (MData) data;
        if (MDataType.CHANGE_TREND.equals(mData.getType())) {
            changeTrend = (ChangeTrend) mData.getData();
            initData();
        }
    }

    /**
     *
     */
    private void initData() {
//        weatherDetailChart
        initChart();
        initList();
    }

    /**
     * 初始化列表
     */
    private void initList() {
        if (changeTrend!=null&&changeTrend.getMessage() == null) {
            //// TODO: 2018/4/26 数据为空
            return;
        }
        int id = weatherDetailRgType.getCheckedRadioButtonId();
        List<DataInfoBean> initData = new ArrayList<>();
        if (id == R.id.weather_detail_rbpm25_type) {
            initData = changeTrend.getMessage().getPM25_data();
        } else if (id == R.id.weather_detail_rbpm10_type) {
            initData = changeTrend.getMessage().getPM10_data();
        } else if (id == R.id.weather_detail_rbso2_type) {
            initData = changeTrend.getMessage().getSO2_data();
        } else if (id == R.id.weather_detail_rbno2_type) {
            initData = changeTrend.getMessage().getNO2_data();
        } else if (id == R.id.weather_detail_rbo3_type) {
            initData = changeTrend.getMessage().getO3_data();
        } else if (id == R.id.weather_detail_rbco_type) {
            initData = changeTrend.getMessage().getCO_data();
        }
        if(initData==null){
            return;
        }
        weatherDetailList.setAdapter(new ChangeTrendAdapter(this, initData));


    }

    private int value_code=2;
    private String type="12h";
    /**
     * 初始化图标
     */
    private void initChart() {
//        weatherDetailChart

        if (changeTrend.getMessage().getAQI_data() != null) {
            List<DataInfoBean> list = changeTrend.getMessage().getAQI_data();
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
            int value = 0;
            for (int i = 0; i < numColumns; ++i) {

                values = new ArrayList<SubcolumnValue>();
                //循环所有柱子（list）

                for (int j = 0; j < numSubcolumns; ++j) {
                    //创建一个柱子，然后设置值和颜色，并添加到list中
                    if(list.get(i).getValue()!=null){
                        values.add(new SubcolumnValue(Float.valueOf(list.get(i).getValue()), Color.parseColor(list.get(i).getValuecolor())));
                    }
                    //设置X轴的柱子所对应的属性名称
                    String time = list.get(i).getTime();

                    if (value == value_code) {
                        value = 0;
                        Log.e("test","....."+time);
                        try{
                            if(time.contains(" ")){
                                axisXValues.add(new AxisValue(i).setLabel(time.substring(time.indexOf(" "))));
                            }else{
                                axisXValues.add(new AxisValue(i).setLabel(time));
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }

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
            data.setAxisYLeft(new Axis().setHasLines(false).setTextColor(Color.WHITE).setMaxLabelChars(5));
            //最后将所有值显示在View中
            weatherDetailChart.setColumnChartData(data);
        }
    }

    @Override
    public void onError(String msg, String eCode) {

    }
}
