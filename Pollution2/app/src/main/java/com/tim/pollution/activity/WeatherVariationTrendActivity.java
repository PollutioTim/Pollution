package com.tim.pollution.activity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tim.pollution.R;
import com.tim.pollution.adapter.ChangeTrendAdapter;
import com.tim.pollution.bean.changetrend.ChangeTrend;
import com.tim.pollution.bean.changetrend.DataInfoBean;
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
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.ColumnChartView;

/**
 * 变化趋势
 */
public class WeatherVariationTrendActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, ICallBack {

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
    ListView weatherDetailList;
    @BindView(R.id.weather_detail_rb12_time)
    RadioButton weatherDetailRb12Time;
    @BindView(R.id.weather_detail_rb24_time)
    RadioButton weatherDetailRb24Time;
    @BindView(R.id.weather_detail_rb30_time)
    RadioButton weatherDetailRb30Time;
    @BindView(R.id.weather_detail_rbaqi_type)
    RadioButton weatherDetailRbaqiType;
    @BindView(R.id.weather_detail_back)
    ImageView ivBack;
    private ChangeTrend changeTrend;

    @BindView(R.id.weather_detail_type)
    TextView ivType;
    private ProgressDialog pd;

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
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WeatherVariationTrendActivity.this.finish();
            }
        });
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
        pd = ProgressDialog.show(this, "标题", "加载数据中，请耐心等待......");
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
    public void onSuccess(Object data) {
        if(pd!=null){
            pd.dismiss();
        }
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
        String type="";
        if (id == R.id.weather_detail_rbpm25_type) {
            initData = changeTrend.getMessage().getPM25_data();
            type="PM2.5";
            ivType.setText("PM2.5");
        } else if (id == R.id.weather_detail_rbpm10_type) {
            initData = changeTrend.getMessage().getPM10_data();
            type="PM10";
            ivType.setText("PM10");
        } else if (id == R.id.weather_detail_rbso2_type) {
            initData = changeTrend.getMessage().getSO2_data();
            type="SO₂";
            ivType.setText(getResources().getString(R.string.SO2));
        } else if (id == R.id.weather_detail_rbno2_type) {
            initData = changeTrend.getMessage().getNO2_data();
            type="NO₂";
            ivType.setText(getResources().getString(R.string.NO2));
        } else if (id == R.id.weather_detail_rbo3_type) {
            initData = changeTrend.getMessage().getO3_data();
            type="O₃";
            ivType.setText(getResources().getString(R.string.O3));
        } else if (id == R.id.weather_detail_rbco_type) {
            initData = changeTrend.getMessage().getCO_data();
            type="CO";
            ivType.setText(getResources().getString(R.string.CO));
        }else if(id==R.id.weather_detail_rbaqi_type){
            initData = changeTrend.getMessage().getAQI_data();
            type="AQI";
            ivType.setText(getResources().getString(R.string.AQI));
        }
        if(initData==null){
            return;
        }
        weatherDetailList.setAdapter(new ChangeTrendAdapter(this, initData));
        initChart(initData,type);

    }

    private int value_code=2;
    /**
     * 初始化图标
     */
    private void initChart(List<DataInfoBean> initData,String type) {
        if (initData != null) {
            List<DataInfoBean> list =initData;
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
                        SubcolumnValue sub = new SubcolumnValue(Float.valueOf(list.get(i).getValue()), Color.parseColor(list.get(i).getValuecolor()));
                        sub.setLabel(type+" "+Float.valueOf(list.get(i).getValue()));
                        values.add(sub);
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
                column.setHasLabelsOnlyForSelected(true);
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
            weatherDetailChart.setZoomEnabled(false);
            Viewport v = new Viewport(weatherDetailChart.getMaximumViewport());
            v.bottom = 0f;
            v.top += v.top*0.2;

            //固定Y轴的范围,如果没有这个,Y轴的范围会根据数据的最大值和最小值决定,这不是我想要的
            weatherDetailChart.setMaximumViewport(v);

            //这2个属性的设置一定要在lineChart.setMaximumViewport(v);这个方法之后,不然显示的坐标数据是不能左右滑动查看更多数据的
            v.right =30;
            weatherDetailChart.setCurrentViewport(v);
        }
    }

    @Override
    public void onError(String msg, String eCode) {
        if(pd!=null){
            pd.dismiss();
        }
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
    }
}
