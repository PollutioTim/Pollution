package com.tim.pollution.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tim.pollution.MyApplication;
import com.tim.pollution.R;
import com.tim.pollution.adapter.CityContrastAdapter;
import com.tim.pollution.adapter.CitySpinnerAdapter;
import com.tim.pollution.bean.Charts;
import com.tim.pollution.bean.RegionWeather;
import com.tim.pollution.bean.changetrend.ChangeTrend;
import com.tim.pollution.bean.changetrend.ChangeTrendMessageBean;
import com.tim.pollution.bean.changetrend.DataBankNetBean;
import com.tim.pollution.bean.changetrend.DataInfoBean;
import com.tim.pollution.bean.changetrend.RegionNetBean;
import com.tim.pollution.bean.weather.AQI24hBean;
import com.tim.pollution.callback.ICallBack;
import com.tim.pollution.fragment.FragmentCallBack;
import com.tim.pollution.general.Constants;
import com.tim.pollution.general.MData;
import com.tim.pollution.general.MDataType;
import com.tim.pollution.net.WeatherDal;
import com.tim.pollution.utils.DateUtil;
import com.tim.pollution.view.WrapContentListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * 城市对比
 */
public class CityContrastFragment extends Fragment implements ICallBack, AdapterView.OnItemSelectedListener, View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.city_contrast_sp1)
    TextView cityContrastSp1;
    @BindView(R.id.city_contrast_sp2)
    TextView cityContrastSp2;
    @BindView(R.id.city_contrast_sp3)
    TextView cityContrastSp3;
    @BindView(R.id.city_contrast_chart)
    LineChartView cityContrastChart;
    RadioButton cityContrastRbaqiType;
    @BindView(R.id.city_contrast_rbpm25_type)
    RadioButton cityContrastRbpm25Type;
    @BindView(R.id.city_contrast_rbpm10_type)
    RadioButton cityContrastRbpm10Type;
    @BindView(R.id.city_contrast_rbso2_type)
    RadioButton cityContrastRbso2Type;
    @BindView(R.id.city_contrast_rbno2_type)
    RadioButton cityContrastRbno2Type;
    @BindView(R.id.city_contrast_rbo3_type)
    RadioButton cityContrastRbo3Type;
    @BindView(R.id.city_contrast_rbco_type)
    RadioButton cityContrastRbcoType;
    @BindView(R.id.city_contrast_rg_type)
    RadioGroup cityContrastRgType;
    @BindView(R.id.city_contrast_list)
    ListView cityContrastList;
    @BindView(R.id.city_contrast_city1)
    TextView cityContrastCity1;
    @BindView(R.id.city_contrast_city2)
    TextView cityContrastCity2;
    @BindView(R.id.city_contrast_city3)
    TextView cityContrastCity3;
    RegionNetBean regionNetBean;

    private CityContrastAdapter cityContrastAdapter;

    private Map<Integer, ChangeTrendMessageBean> citys = new HashMap<>();

    //切换时间
    private TextView cityContrastTimeSwitch;
    private List<String> timeTypes = new ArrayList<>();
    private String timeType;
    private Dialog dialog;
    private TextView city1;
    private TextView city1Value;
    private TextView city2;
    private TextView city2Value;
    private TextView city3;
    private TextView city3Value;
    private TextView className;
    private TextView classTime;
    private ProgressDialog pd;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_city_contrast, container, false);
        timeTypes.add("12h");
        timeTypes.add("24h");
        timeTypes.add("day");
        timeType = timeTypes.get(0);
        findView(view);
        loadRegionData();
        initOnclick();
        return view;
    }

    /**
     * 初始化控件
     */
    private void findView(View view) {
        cityContrastSp1 = (TextView) view.findViewById(R.id.city_contrast_sp1);
        cityContrastSp2 = (TextView) view.findViewById(R.id.city_contrast_sp2);
        cityContrastSp3 = (TextView) view.findViewById(R.id.city_contrast_sp3);
        cityContrastChart = (LineChartView) view.findViewById(R.id.city_contrast_chart);
        cityContrastRbaqiType = (RadioButton) view.findViewById(R.id.city_contrast_rbaqi_type);
        cityContrastRbpm25Type = (RadioButton) view.findViewById(R.id.city_contrast_rbpm25_type);
        cityContrastRbpm10Type = (RadioButton) view.findViewById(R.id.city_contrast_rbpm10_type);
        cityContrastRbso2Type = (RadioButton) view.findViewById(R.id.city_contrast_rbso2_type);
        cityContrastRbno2Type = (RadioButton) view.findViewById(R.id.city_contrast_rbno2_type);
        cityContrastRbo3Type = (RadioButton) view.findViewById(R.id.city_contrast_rbo3_type);
        cityContrastRbcoType = (RadioButton) view.findViewById(R.id.city_contrast_rbco_type);
        cityContrastRgType = (RadioGroup) view.findViewById(R.id.city_contrast_rg_type);
        cityContrastList = (ListView) view.findViewById(R.id.city_contrast_list);
        cityContrastCity1 = (TextView) view.findViewById(R.id.city_contrast_city1);
        cityContrastCity2 = (TextView) view.findViewById(R.id.city_contrast_city2);
        cityContrastCity3 = (TextView) view.findViewById(R.id.city_contrast_city3);
        cityContrastTimeSwitch = (TextView) view.findViewById(R.id.city_contrast_time_switch);
        city1 = (TextView) view.findViewById(R.id.city_contrast_info_city1);
        city1Value = (TextView) view.findViewById(R.id.city_contrast_info_city1_value);
        city2 = (TextView) view.findViewById(R.id.city_contrast_info_city2);
        city2Value = (TextView) view.findViewById(R.id.city_contrast_info_city2_value);
        city3 = (TextView) view.findViewById(R.id.city_contrast_info_city3);
        city3Value = (TextView) view.findViewById(R.id.city_contrast_info_city3_value);
        className = (TextView) view.findViewById(R.id.city_contrast_info_class);
        classTime = (TextView) view.findViewById(R.id.city_contrast_info_class_time);
    }

    private void initOnclick() {
        cityContrastSp1.setOnClickListener(this);
        cityContrastSp2.setOnClickListener(this);
        cityContrastSp3.setOnClickListener(this);
        cityContrastRgType.setOnCheckedChangeListener(this);
        cityContrastTimeSwitch.setOnClickListener(this);
    }

    /**
     * 加载县区列表
     */
    private void loadRegionData() {
        pd = ProgressDialog.show(getContext(), "提示", "加载数据中，请耐心等待......");
        Map<String, String> params = new HashMap<>();
        params.put("key", Constants.key);
        params.put("regiontype", "allregion");
        WeatherDal.getInstance().getRegion(params, this);
    }

    private String datatype = "PM25";

    /**
     * 加载数据
     * @param regionid
     * @param state
     */
    private void loadData(String regionid, final int state) {
        cityContrastRgType.check(R.id.city_contrast_rbaqi_type);
        Map<String, String> params = new HashMap<>();
        params.put("key", Constants.key);
        params.put("type", timeType);
        params.put("regionid", regionid);
        params.put("state", state + "");
        WeatherDal.getInstance().getPollTrend(params, new ICallBack() {
            @Override
            public void onSuccess(Object data) {
                MData mData = (MData) data;
                if (MDataType.CHANGE_TREND.equals(mData.getType())) {
                    ChangeTrend changTrend = (ChangeTrend) mData.getData();
                    if (changTrend != null) {
                        citys.put(state, changTrend.getMessage());
                        initData(state);
                    }

                }
            }


            @Override
            public void onError(String msg, String eCode) {
            }
        });
    }


    @Override
    public void onSuccess(Object data) {
        if(pd!=null){
            pd.dismiss();
        }
        MData mData = (MData) data;
        if (MDataType.REGIONNET_BEAN.equals(mData.getType())) {
            regionNetBean = (RegionNetBean) mData.getData();
            if (regionNetBean != null) {
                initSpinner();
            }
        }
    }

    /**
     * 显示数据
     */
    private void initData(int state) {
        if (citys.size() < 3) {
            return;
        }
        try {
            String time = citys.get(1).getAQI_data().get(citys.get(1).getAQI_data().size() - 1).getTime();
            String vaule1 = cityContrastSp1.getText() + "  " + citys.get(1).getAQI_data().get(citys.get(1).getAQI_data().size() - 1).getValue();
            String vaule2 = cityContrastSp2.getText() + "  " + citys.get(2).getAQI_data().get(citys.get(2).getAQI_data().size() - 1).getValue();
            String vaule3 = cityContrastSp3.getText() + "  " + citys.get(3).getAQI_data().get(citys.get(3).getAQI_data().size() - 1).getValue();
            String value = "AQI  " + switchTime(time) + "\n" + vaule1 + "\n" + vaule2 + "\n" + vaule3;
            className.setText("AQI");
            classTime.setText(switchTime(time));
            city1.setText(cityContrastSp1.getText());
            city1Value.setText(citys.get(1).getAQI_data().get(citys.get(1).getAQI_data().size() - 1).getValue());
            city2.setText(cityContrastSp2.getText());
            city2Value.setText(citys.get(2).getAQI_data().get(citys.get(2).getAQI_data().size() - 1).getValue());
            city3.setText(cityContrastSp3.getText());
            city3Value.setText(citys.get(3).getAQI_data().get(citys.get(3).getAQI_data().size() - 1).getValue());
            cityContrastTimeSwitch.setText(timeType.toUpperCase());
//        initChars();
            initFrom();
            cityContrastRgType.check(R.id.city_contrast_rbaqi_type);
            cityContrastAdapter = new CityContrastAdapter(getContext(), citys, CityContrastAdapter.PM25);
            cityContrastList.setAdapter(cityContrastAdapter);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "获取数据失败，请重试", Toast.LENGTH_LONG).show();
        }
    }

    private String switchTime(String time) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    /**
     * 初始化折线图
     */
    private void initFrom() {
        if (citys.size() < 3) {
            cityContrastChart.setLineChartData(null);
            return;
        }
        List<AxisValue> axisXValues = new ArrayList<AxisValue>();
        final List<Line> lines = new ArrayList<Line>();
        int size1 = 0;
        int size2 = 0;
        int size3 = 0;

        try {
            size1 = citys.get(1).getAQI_data().size();
        } catch (Exception e) {
        }
        try {
            size2 = citys.get(2).getAQI_data().size();
        } catch (Exception e) {
        }
        try {
            size3 = citys.get(3).getAQI_data().size();
        } catch (Exception e) {
        }
        int max = Math.max(size1, size2);
        max = Math.max(max, size3);
        if (max == size1) {
            max = 1;
        } else if (max == size2) {
            max = 2;
        } else {
            max = 3;
        }
        for (int i = 0; i < citys.get(max).getAQI_data().size(); i++) {
            String time=DateUtil.switchTime(citys.get(max).getAQI_data().get(i).getTime(),DateUtil.TIME_TYPE02);
            if(timeType.contains("day")){
                time=DateUtil.switchTime02(citys.get(max).getAQI_data().get(i).getTime(),DateUtil.TIME_TYPE03);
            }
            axisXValues.add(new AxisValue(i).setLabel(time));
        }
        int id = cityContrastRgType.getCheckedRadioButtonId();
        if (id == R.id.city_contrast_rbpm25_type) {
            lines.add(getLine(citys.get(1).getPM25_data(), "#FFF1C55F", "PM2.5"));
            lines.add(getLine(citys.get(2).getPM25_data(), "#FF47F646", "PM2.5"));
            lines.add(getLine(citys.get(3).getPM25_data(), "#FF42DAFC", "PM2.5"));
        } else if (id == R.id.city_contrast_rbpm10_type) {
            lines.add(getLine(citys.get(1).getPM10_data(), "#FFF1C55F", "PM10"));
            lines.add(getLine(citys.get(2).getPM10_data(), "#FF47F646", "PM10"));
            lines.add(getLine(citys.get(3).getPM10_data(), "#FF42DAFC", "PM10"));
        } else if (id == R.id.city_contrast_rbso2_type) {
            lines.add(getLine(citys.get(1).getSO2_data(), "#FFF1C55F", "SO₂"));
            lines.add(getLine(citys.get(2).getSO2_data(), "#FF47F646", "SO₂"));
            lines.add(getLine(citys.get(3).getSO2_data(), "#FF42DAFC", "SO₂"));
        } else if (id == R.id.city_contrast_rbno2_type) {
            lines.add(getLine(citys.get(1).getNO2_data(), "#FFF1C55F", "NO₂"));
            lines.add(getLine(citys.get(2).getNO2_data(), "#FF47F646", "NO₂"));
            lines.add(getLine(citys.get(3).getNO2_data(), "#FF42DAFC", "NO₂"));
        } else if (id == R.id.city_contrast_rbo3_type) {
            lines.add(getLine(citys.get(1).getO3_data(), "#FFF1C55F", "O₃"));
            lines.add(getLine(citys.get(2).getO3_data(), "#FF47F646", "O₃"));
            lines.add(getLine(citys.get(3).getO3_data(), "#FF42DAFC", "O₃"));
        } else if (id == R.id.city_contrast_rbco_type) {
            lines.add(getLine(citys.get(1).getCO_data(), "#FFF1C55F", "CO"));
            lines.add(getLine(citys.get(2).getCO_data(), "#FF47F646", "CO"));
            lines.add(getLine(citys.get(3).getCO_data(), "#FF42DAFC", "CO"));
        } else if (id == R.id.city_contrast_rbaqi_type) {
            lines.add(getLine(citys.get(1).getAQI_data(), "#FFF1C55F", "AQI"));
            lines.add(getLine(citys.get(2).getAQI_data(), "#FF47F646", "AQI"));
            lines.add(getLine(citys.get(3).getAQI_data(), "#FF42DAFC", "AQI"));
        }
        lines.remove(null);
        LineChartData data = new LineChartData(lines);
        if (true) {
            data.setAxisXBottom(new Axis(axisXValues).setHasLines(false).setTextColor(Color.WHITE).setName("").setHasTiltedLabels(false).setMaxLabelChars(4));
            data.setAxisYLeft(new Axis().setHasLines(false).setName("").setTextColor(Color.WHITE).setMaxLabelChars(4));
        } else {
            data.setAxisXBottom(null);
            data.setAxisYLeft(null);
        }
        data.setBaseValue(Float.NEGATIVE_INFINITY);
        cityContrastChart.setValueSelectionEnabled(false);
        cityContrastChart.setLineChartData(data);
        cityContrastChart.setZoomEnabled(false);
        cityContrastChart.setMaxZoom(3);
        cityContrastChart.setInteractive(true);

        Viewport v = new Viewport(cityContrastChart.getMaximumViewport());
        v.bottom = 0f;
        v.top += 30f;

        //固定Y轴的范围,如果没有这个,Y轴的范围会根据数据的最大值和最小值决定,这不是我想要的
        cityContrastChart.setMaximumViewport(v);


        //这2个属性的设置一定要在lineChart.setMaximumViewport(v);这个方法之后,不然显示的坐标数据是不能左右滑动查看更多数据的
        v.right = 30;
        v.left = 5;
        cityContrastChart.setCurrentViewport(v);
        cityContrastChart.setOnValueTouchListener(new LineChartOnValueSelectListener() {
            @Override
            public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
                Log.e("折线图", "lineIndex:" + lineIndex + ",pointIndex:" + pointIndex + ",PointValue" + value);
                int id = cityContrastRgType.getCheckedRadioButtonId();
                String type = "";
                List<DataInfoBean> data = null;
                List<DataInfoBean> data1 = null;
                List<DataInfoBean> data2 = null;
                List<DataInfoBean> data3 = null;
                ChangeTrendMessageBean msg = citys.get(lineIndex + 1);
                ChangeTrendMessageBean msg1 = citys.get(1);
                ChangeTrendMessageBean msg2 = citys.get(2);
                ChangeTrendMessageBean msg3 = citys.get(3);
                if (id == R.id.city_contrast_rbpm25_type) {
                    type = "PM2.5";
                    data = msg.getPM25_data();
                    data1 = msg1.getPM25_data();
                    data2 = msg2.getPM25_data();
                    data3 = msg3.getPM25_data();
                } else if (id == R.id.city_contrast_rbpm10_type) {
                    type = "PM10";
                    data = msg.getPM10_data();
                    data1 = msg1.getPM10_data();
                    data2 = msg2.getPM10_data();
                    data3 = msg3.getPM10_data();
                } else if (id == R.id.city_contrast_rbso2_type) {
                    type = "SO2";
                    data = msg.getSO2_data();
                    data1 = msg1.getSO2_data();
                    data2 = msg2.getSO2_data();
                    data3 = msg3.getSO2_data();
                } else if (id == R.id.city_contrast_rbno2_type) {
                    type = "NO2";
                    data = msg.getNO2_data();
                    data1 = msg1.getNO2_data();
                    data2 = msg2.getNO2_data();
                    data3 = msg3.getNO2_data();
                } else if (id == R.id.city_contrast_rbo3_type) {
                    type = "O3";
                    data = msg.getO3_data();
                    data1 = msg1.getO3_data();
                    data2 = msg2.getO3_data();
                    data3 = msg3.getO3_data();
                } else if (id == R.id.city_contrast_rbco_type) {
                    type = "CO";
                    data = msg.getCO_data();
                    data1 = msg1.getCO_data();
                    data2 = msg2.getCO_data();
                    data3 = msg3.getCO_data();
                } else if (id == R.id.city_contrast_rbaqi_type) {
                    type = "AQI";
                    data = msg.getAQI_data();
                    data1 = msg1.getAQI_data();
                    data2 = msg2.getAQI_data();
                    data3 = msg3.getAQI_data();
                }

                if (data != null) {
                    String time = switchTime(data.get(pointIndex).getTime());
                    className.setText(type);
                    classTime.setText(switchTime(time));
                    try {
                        city1.setText(cityContrastSp1.getText());
                        city1Value.setText(data1.get(pointIndex).getValue());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        city2.setText(cityContrastSp2.getText());
                        city2Value.setText(data2.get(pointIndex).getValue());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        city3.setText(cityContrastSp3.getText());
                        city3Value.setText(data3.get(pointIndex).getValue());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    initData(-1);
                }
            }
            @Override
            public void onValueDeselected() {

            }
        });
    }


    /**
     * 获取折线
     * @param data
     * @param color
     * @param type
     * @return
     */
    private Line getLine(List<DataInfoBean> data, String color, String type) {
        if (data == null) {
            Toast.makeText(getContext(), "数据为空，不能作图！", Toast.LENGTH_LONG).show();
            return null;
        }
        List<PointValue> pointValues = new ArrayList<PointValue>();// 节点数据结合
        for (int i = 0; i < data.size(); i++) {
            try {
                String time=DateUtil.switchTime(data.get(i).getTime(),DateUtil.TIME_TYPE02);
                if(timeType.contains("day")){
                    time=DateUtil.switchTime02(data.get(i).getTime(),DateUtil.TIME_TYPE03);
                }

                PointValue point = new PointValue(i, getFloatFromString(data.get(i).getValue()));
                point.setLabel(type + " " + getIntFromString(data.get(i).getValue())+" 时间 "+time);
                pointValues.add(point);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Line line = new Line(pointValues);//将值设置给折线
        line.setColor(Color.parseColor(color));// 设置折线颜色
        line.setStrokeWidth(3);// 设置折线宽度
        line.setFilled(false);// 设置折线覆盖区域是否填充
        line.setCubic(false);// 是否设置为立体的
        line.setPointColor(Color.parseColor(color));// 设置节点颜色
        line.setPointRadius(3);// 设置节点半径
        line.setHasLabels(false);// 是否显示节点数据
        line.setHasLines(true);// 是否显示折线
        line.setHasPoints(true);// 是否显示节点
        line.setShape(ValueShape.CIRCLE);// 节点图形样式 DIAMOND菱形、SQUARE方形、CIRCLE圆形
        line.setHasLabelsOnlyForSelected(true);// 隐藏数据，触摸可以显示
        return line;
    }

    /**
     * string-->int
     *
     * @param s
     * @return
     */
    private int getIntFromString(String s) {
        try{
            int i = (int) Math.ceil(Double.valueOf(s));
            return i;
        }catch (Exception e){
            return 0;
        }

    }
    /**
     * string-->int
     *
     * @param s
     * @return
     */
    private float getFloatFromString(String s) {
        try{
            float i = Float.valueOf(s);
            return i;
        }catch (Exception e){
            return 0;
        }

    }
    /**
     * 初始化城市
     */
    private void initSpinner() {
        if (regionNetBean != null) {
            if (regionNetBean.getMessage().get(0) != null) {
                cityContrastSp1.setText(regionNetBean.getMessage().get(0).getRegionName());
                cityContrastCity1.setText(regionNetBean.getMessage().get(0).getRegionName());
                loadData(regionNetBean.getMessage().get(0).getRegionId(), 1);
            } else {

            }
            if (regionNetBean.getMessage().get(1) != null) {
                cityContrastSp2.setText(regionNetBean.getMessage().get(1).getRegionName());
                cityContrastCity2.setText(regionNetBean.getMessage().get(1).getRegionName());
                loadData(regionNetBean.getMessage().get(1).getRegionId(), 2);
            } else {

            }
            if (regionNetBean.getMessage().get(2) != null) {
                cityContrastSp3.setText(regionNetBean.getMessage().get(2).getRegionName());
                cityContrastCity3.setText(regionNetBean.getMessage().get(2).getRegionName());
                loadData(regionNetBean.getMessage().get(2).getRegionId(), 3);
            } else {

            }
        }
    }

    @Override
    public void onError(String msg, String eCode) {
        if(pd!=null){
            pd.dismiss();
        }
        Toast.makeText(getContext(),msg,Toast.LENGTH_LONG).show();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.city_contrast_sp1:
                showCitySelect(cityContrastSp1, 1);
                break;
            case R.id.city_contrast_sp2:
                showCitySelect(cityContrastSp2, 2);

                break;
            case R.id.city_contrast_sp3:
                showCitySelect(cityContrastSp3, 3);
                break;
            case R.id.city_contrast_time_switch://切换时间
                int index = timeTypes.indexOf(timeType);
                index++;
                if (index > 2) {
                    index = 0;
                }
                Log.e("index", "index:" + index);
                timeType = timeTypes.get(index);
                citys.clear();
                initSpinner();
                break;
        }
    }

    private int state1;


    /**
     * 城市选择弹框
     * @param view
     * @param state
     */
    private void showCitySelect(View view, int state) {
        this.state1 = state;
        View viewDialog = LayoutInflater.from(getContext()).inflate(R.layout.pop_window, null);
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(viewDialog, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        WindowManager m = getActivity().getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.65
        window.setAttributes(p);
        dialog.onWindowAttributesChanged(p);
        ListView listview = (ListView) viewDialog.findViewById(R.id.pop_window_list);
        listview.setAdapter(new CitySpinnerAdapter(getActivity(), regionNetBean.getMessage()));
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                loadData(regionNetBean.getMessage().get(position).getRegionId(), state1);
                if (state1 == 1) {
                    cityContrastSp1.setText(regionNetBean.getMessage().get(position).getRegionName());
                    cityContrastCity1.setText(regionNetBean.getMessage().get(position).getRegionName());
                } else if (state1 == 2) {
                    cityContrastCity2.setText(regionNetBean.getMessage().get(position).getRegionName());
                    cityContrastSp2.setText(regionNetBean.getMessage().get(position).getRegionName());
                } else {
                    cityContrastCity3.setText(regionNetBean.getMessage().get(position).getRegionName());
                    cityContrastSp3.setText(regionNetBean.getMessage().get(position).getRegionName());
                }
                dialog.dismiss();
            }
        });
        TextView close = (TextView) viewDialog.findViewById(R.id.pop_window_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        int id = group.getCheckedRadioButtonId();
        if (id == R.id.city_contrast_rbpm25_type) {
            datatype = cityContrastAdapter.PM25;
            try {
                String time = citys.get(1).getPM25_data().get(citys.get(1).getPM25_data().size() - 1).getTime();
                String vaule1 = cityContrastSp1.getText() + " " + citys.get(1).getPM25_data().get(citys.get(1).getPM25_data().size() - 1).getValue();
                String vaule2 = cityContrastSp2.getText() + " " + citys.get(2).getPM25_data().get(citys.get(2).getPM25_data().size() - 1).getValue();
                String vaule3 = cityContrastSp3.getText() + " " + citys.get(3).getPM25_data().get(citys.get(3).getPM25_data().size() - 1).getValue();
                String value = "PM2.5 " + switchTime(time) + "\n" + vaule1 + "\n" + vaule2 + "\n" + vaule3;
//                cityContrastInfo.setText(value);
                className.setText("PM2.5");
                classTime.setText(switchTime(time));
                city1.setText(cityContrastSp1.getText());
                city1Value.setText(citys.get(1).getPM25_data().get(citys.get(1).getPM25_data().size() - 1).getValue());
                city2.setText(cityContrastSp2.getText());
                city2Value.setText(citys.get(2).getPM25_data().get(citys.get(2).getPM25_data().size() - 1).getValue());
                city3.setText(cityContrastSp3.getText());
                city3Value.setText(citys.get(3).getPM25_data().get(citys.get(3).getPM25_data().size() - 1).getValue());
            } catch (Exception e) {
            }

        } else if (id == R.id.city_contrast_rbpm10_type) {
            try {
                String time = citys.get(1).getPM10_data().get(citys.get(1).getPM10_data().size() - 1).getTime();
                String vaule1 = cityContrastSp1.getText() + " " + citys.get(1).getPM10_data().get(citys.get(1).getPM10_data().size() - 1).getValue();
                String vaule2 = cityContrastSp2.getText() + " " + citys.get(2).getPM10_data().get(citys.get(2).getPM10_data().size() - 1).getValue();
                String vaule3 = cityContrastSp3.getText() + " " + citys.get(3).getPM10_data().get(citys.get(3).getPM10_data().size() - 1).getValue();
                String value = "PM10 " + switchTime(time) + "\n" + vaule1 + "\n" + vaule2 + "\n" + vaule3;
//                cityContrastInfo.setText(value);
                className.setText("PM10");
                classTime.setText(switchTime(time));
                city1.setText(cityContrastSp1.getText());
                city1Value.setText(citys.get(1).getPM10_data().get(citys.get(1).getPM10_data().size() - 1).getValue());
                city2.setText(cityContrastSp2.getText());
                city2Value.setText(citys.get(2).getPM10_data().get(citys.get(2).getPM10_data().size() - 1).getValue());
                city3.setText(cityContrastSp3.getText());
                city3Value.setText(citys.get(3).getPM10_data().get(citys.get(3).getPM10_data().size() - 1).getValue());
            } catch (Exception e) {
            }

            datatype = cityContrastAdapter.PM10;
        } else if (id == R.id.city_contrast_rbso2_type) {
            try {
                String time = citys.get(1).getSO2_data().get(citys.get(1).getSO2_data().size() - 1).getTime();
                String vaule1 = cityContrastSp1.getText() + " " + citys.get(1).getSO2_data().get(citys.get(1).getSO2_data().size() - 1).getValue();
                String vaule2 = cityContrastSp2.getText() + " " + citys.get(2).getSO2_data().get(citys.get(2).getSO2_data().size() - 1).getValue();
                String vaule3 = cityContrastSp3.getText() + " " + citys.get(3).getSO2_data().get(citys.get(3).getSO2_data().size() - 1).getValue();
                String value = "SO₂ " + switchTime(time) + "\n" + vaule1 + "\n" + vaule2 + "\n" + vaule3;
//                cityContrastInfo.setText(value);
                className.setText("SO₂");
                classTime.setText(switchTime(time));
                city1.setText(cityContrastSp1.getText());
                city1Value.setText(citys.get(1).getSO2_data().get(citys.get(1).getSO2_data().size() - 1).getValue());
                city2.setText(cityContrastSp2.getText());
                city2Value.setText(citys.get(2).getSO2_data().get(citys.get(2).getSO2_data().size() - 1).getValue());
                city3.setText(cityContrastSp3.getText());
                city3Value.setText(citys.get(3).getSO2_data().get(citys.get(3).getSO2_data().size() - 1).getValue());
            } catch (Exception e) {
            }

            datatype = cityContrastAdapter.SO2;
        } else if (id == R.id.city_contrast_rbno2_type) {
            try {
                String time = citys.get(1).getNO2_data().get(citys.get(1).getNO2_data().size() - 1).getTime();
                String vaule1 = cityContrastSp1.getText() + " " + citys.get(1).getNO2_data().get(citys.get(1).getNO2_data().size() - 1).getValue();
                String vaule2 = cityContrastSp2.getText() + " " + citys.get(2).getNO2_data().get(citys.get(2).getNO2_data().size() - 1).getValue();
                String vaule3 = cityContrastSp3.getText() + " " + citys.get(3).getNO2_data().get(citys.get(3).getNO2_data().size() - 1).getValue();
                String value = "NO₂ " + switchTime(time) + "\n" + vaule1 + "\n" + vaule2 + "\n" + vaule3;
//                cityContrastInfo.setText(value);
                className.setText("NO₂");
                classTime.setText(switchTime(time));
                city1.setText(cityContrastSp1.getText());
                city1Value.setText(citys.get(1).getNO2_data().get(citys.get(1).getNO2_data().size() - 1).getValue());
                city2.setText(cityContrastSp2.getText());
                city2Value.setText(citys.get(2).getNO2_data().get(citys.get(2).getNO2_data().size() - 1).getValue());
                city3.setText(cityContrastSp3.getText());
                city3Value.setText(citys.get(3).getNO2_data().get(citys.get(3).getNO2_data().size() - 1).getValue());
            } catch (Exception e) {
            }

            datatype = cityContrastAdapter.NO2;
        } else if (id == R.id.city_contrast_rbo3_type) {
            try {
                String time = citys.get(1).getO3_data().get(citys.get(1).getO3_data().size() - 1).getTime();
                String vaule1 = cityContrastSp1.getText() + " " + citys.get(1).getO3_data().get(citys.get(1).getO3_data().size() - 1).getValue();
                String vaule2 = cityContrastSp2.getText() + " " + citys.get(2).getO3_data().get(citys.get(2).getO3_data().size() - 1).getValue();
                String vaule3 = cityContrastSp3.getText() + " " + citys.get(3).getO3_data().get(citys.get(3).getO3_data().size() - 1).getValue();
                String value = "O₃ " + switchTime(time) + "\n" + vaule1 + "\n" + vaule2 + "\n" + vaule3;
//                cityContrastInfo.setText(value);
                className.setText("O₃");
                classTime.setText(switchTime(time));
                city1.setText(cityContrastSp1.getText());
                city1Value.setText(citys.get(1).getO3_data().get(citys.get(1).getO3_data().size() - 1).getValue());
                city2.setText(cityContrastSp2.getText());
                city2Value.setText(citys.get(2).getO3_data().get(citys.get(2).getO3_data().size() - 1).getValue());
                city3.setText(cityContrastSp3.getText());
                city3Value.setText(citys.get(3).getO3_data().get(citys.get(3).getO3_data().size() - 1).getValue());
            } catch (Exception e) {
            }

            datatype = cityContrastAdapter.O3;
        } else if (id == R.id.city_contrast_rbco_type) {
            try {
                String time = citys.get(1).getCO_data().get(citys.get(1).getCO_data().size() - 1).getTime();
                String vaule1 = cityContrastSp1.getText() + " " + citys.get(1).getCO_data().get(citys.get(1).getCO_data().size() - 1).getValue();
                String vaule2 = cityContrastSp2.getText() + " " + citys.get(2).getCO_data().get(citys.get(2).getCO_data().size() - 1).getValue();
                String vaule3 = cityContrastSp3.getText() + " " + citys.get(3).getCO_data().get(citys.get(3).getCO_data().size() - 1).getValue();
                String value = "CO " + switchTime(time) + "\n" + vaule1 + "\n" + vaule2 + "\n" + vaule3;
//                cityContrastInfo.setText(value);
                className.setText("CO");
                classTime.setText(switchTime(time));
                city1.setText(cityContrastSp1.getText());
                city1Value.setText(citys.get(1).getCO_data().get(citys.get(1).getCO_data().size() - 1).getValue());
                city2.setText(cityContrastSp2.getText());
                city2Value.setText(citys.get(2).getCO_data().get(citys.get(2).getCO_data().size() - 1).getValue());
                city3.setText(cityContrastSp3.getText());
                city3Value.setText(citys.get(3).getCO_data().get(citys.get(3).getCO_data().size() - 1).getValue());
            } catch (Exception e) {
            }

            datatype = cityContrastAdapter.CO;
        } else if (id == R.id.city_contrast_rbaqi_type) {
            try {
                String time = citys.get(1).getAQI_data().get(citys.get(1).getAQI_data().size() - 1).getTime();
                String vaule1 = cityContrastSp1.getText() + " " + citys.get(1).getAQI_data().get(citys.get(1).getAQI_data().size() - 1).getValue();
                String vaule2 = cityContrastSp2.getText() + " " + citys.get(2).getAQI_data().get(citys.get(2).getAQI_data().size() - 1).getValue();
                String vaule3 = cityContrastSp3.getText() + " " + citys.get(3).getAQI_data().get(citys.get(3).getAQI_data().size() - 1).getValue();
                String value = "AQI " + switchTime(time) + "\n" + vaule1 + "\n" + vaule2 + "\n" + vaule3;
//                cityContrastInfo.setText(value);
                className.setText("AQI");
                classTime.setText(switchTime(time));
                city1.setText(cityContrastSp1.getText());
                city1Value.setText(citys.get(1).getAQI_data().get(citys.get(1).getAQI_data().size() - 1).getValue());
                city2.setText(cityContrastSp2.getText());
                city2Value.setText(citys.get(2).getAQI_data().get(citys.get(2).getAQI_data().size() - 1).getValue());
                city3.setText(cityContrastSp3.getText());
                city3Value.setText(citys.get(3).getAQI_data().get(citys.get(3).getAQI_data().size() - 1).getValue());
            } catch (Exception e) {
            }

            datatype = CityContrastAdapter.AQI;
        }
        initFrom();
        if (cityContrastAdapter != null) {
            cityContrastAdapter.setCode(datatype);
            cityContrastAdapter.notifyDataSetChanged();
        }
    }
}
