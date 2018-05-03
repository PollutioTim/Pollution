package com.tim.pollution.activity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Gravity;
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

import com.google.gson.Gson;
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
import com.tim.pollution.callback.ICallBack;
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
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;

public class CityContrastActivity extends AppCompatActivity implements ICallBack, AdapterView.OnItemSelectedListener, View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.city_contrast_sp1)
    TextView cityContrastSp1;
    @BindView(R.id.city_contrast_sp2)
    TextView cityContrastSp2;
    @BindView(R.id.city_contrast_sp3)
    TextView cityContrastSp3;
    @BindView(R.id.city_contrast_chart)
    LineChartView cityContrastChart;
    @BindView(R.id.city_contrast_info)
    TextView cityContrastInfo;
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
    WrapContentListView cityContrastList;

    DataBankNetBean dataBankNetBean;
    @BindView(R.id.city_contrast_city1)
    TextView cityContrastCity1;
    @BindView(R.id.city_contrast_city2)
    TextView cityContrastCity2;
    @BindView(R.id.city_contrast_city3)
    TextView cityContrastCity3;
    private String pointtype;
    RegionNetBean regionNetBean;

    private CityContrastAdapter cityContrastAdapter;

    private Map<Integer, ChangeTrendMessageBean> citys = new HashMap<>();

    private PopupWindow mPopupWindow;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_contrast);
        ButterKnife.bind(this);
        pointtype = getIntent().getStringExtra("regiontype");
        loadRegionData();
        initOnclick();
        cityContrastRgType.setOnCheckedChangeListener(this);
    }

    private void initOnclick() {
        cityContrastSp1.setOnClickListener(this);
        cityContrastSp2.setOnClickListener(this);
        cityContrastSp3.setOnClickListener(this);
    }

    /**
     * 加载县区列表
     */
    private void loadRegionData() {
        Map<String, String> params = new HashMap<>();
        params.put("key", Constants.key);
        params.put("regiontype", "allregion");
        WeatherDal.getInstance().getRegion(params, this);
    }

    private String datatype = "PM25";
    private String ranktype = "real";

    private void loadData(String regionid, final int state) {

        Map<String, String> params = new HashMap<>();
        params.put("key", Constants.key);
        params.put("type", "12h");
        params.put("regionid", regionid);
        params.put("state", state + "");

        WeatherDal.getInstance().getPollTrend(params, new ICallBack() {
            @Override
            public void onProgress(Object data) {
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
    public void onProgress(Object data) {
        MData mData = (MData) data;
        if (MDataType.REGIONNET_BEAN.equals(mData.getType())) {
            regionNetBean = (RegionNetBean) mData.getData();
//            regionNetBean = getTest();
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
        String time = citys.get(1).getAQI_data().get(citys.get(1).getAQI_data().size() - 1).getTime();
        String vaule1 = cityContrastSp1.getText() + " " + citys.get(1).getAQI_data().get(citys.get(1).getAQI_data().size() - 1).getValue();
        String vaule2 = cityContrastSp2.getText() + " " + citys.get(2).getAQI_data().get(citys.get(2).getAQI_data().size() - 1).getValue();
        String vaule3 = cityContrastSp3.getText() + " " + citys.get(3).getAQI_data().get(citys.get(3).getAQI_data().size() - 1).getValue();
        String value = "AQI " + switchTime(time) + "\n" + vaule1 + "\n" + vaule2 + "\n" + vaule3;
        cityContrastInfo.setText(value);
//        initChars();
        initFrom();
        cityContrastAdapter = new CityContrastAdapter(this, citys, CityContrastAdapter.PM25);
        cityContrastList.setAdapter(cityContrastAdapter);
    }

    private String switchTime(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(DateUtil.strToDateLong(time));
    }

    /**
     * 初始化折线图
     */
    private void initChars() {
        if (citys.size() < 3) {
            return;
        }


        Axis axisY = new Axis().setHasLines(true);// Y轴属性
        Axis axisX = new Axis();// X轴属性
        ArrayList<AxisValue> axisValuesX = new ArrayList<AxisValue>();//定义X轴刻度值的数据集合
        ArrayList<AxisValue> axisValuesY = new ArrayList<AxisValue>();//定义Y轴刻度值的数据集合
        axisX.setValues(axisValuesX);//为X轴显示的刻度值设置数据集合
        axisX.setLineColor(Color.WHITE);// 设置X轴轴线颜色
        axisY.setLineColor(Color.WHITE);// 设置Y轴轴线颜色
        axisX.setTextColor(Color.WHITE);// 设置X轴文字颜色
        axisY.setTextColor(Color.WHITE);// 设置Y轴文字颜色
        axisX.setTextSize(14);// 设置X轴文字大小
        axisX.setTypeface(Typeface.DEFAULT);// 设置文字样式，此处为默认
        axisX.setHasTiltedLabels(false);// 设置X轴文字向左旋转45度
        axisX.setHasLines(false);// 是否显示X轴网格线
        axisY.setHasLines(false);// 是否显示Y轴网格线
        axisX.setHasSeparationLine(true);// 设置是否有分割线
        axisX.setInside(false);// 设置X轴文字是否在X轴内部
        axisX.setMaxLabelChars(2);
        axisY.setValues(axisValuesY);


        List<Line> lines = new ArrayList<Line>();//定义线的集合

        lines.add(getLine(citys.get(1).getAQI_data(), "#FFF1C55F"));
        lines.add(getLine(citys.get(2).getAQI_data(), "#FF47F646"));
        lines.add(getLine(citys.get(3).getAQI_data(), "#FF42DAFC"));

        for (int j = 0; j <= 5; j += 1) {//循环为节点、X、Y轴添加数据
            axisValuesY.add(new AxisValue(j).setValue(j).setLabel(j * 100 + ""));// 添加Y轴显示的刻度值
        }
        int x = Integer.valueOf(citys.get(1).getAQI_data().get(0).getTime().substring(12, 13)) + 1;
        for (int j = 1; j <= 13; j += 3) {//循环为节点、X、Y轴添加数据
            axisValuesX.add(new AxisValue(j).setValue(j).setLabel(x + ":00"));// 添加Y轴显示的刻度值
            x = x + 3;
        }

        LineChartData chartData = new LineChartData(lines);//将线的集合设置为折线图的数据
        chartData.setAxisYLeft(axisY);// 将Y轴属性设置到左边
        chartData.setAxisXBottom(axisX);// 将X轴属性设置到底部
        chartData.setBaseValue(20);// 设置反向覆盖区域颜色
        chartData.setValueLabelBackgroundAuto(false);// 设置数据背景是否跟随节点颜色
        chartData.setValueLabelBackgroundColor(Color.BLUE);// 设置数据背景颜色
        chartData.setValueLabelBackgroundEnabled(false);// 设置是否有数据背景
        chartData.setValueLabelsTextColor(Color.BLACK);// 设置数据文字颜色
        chartData.setValueLabelTextSize(15);// 设置数据文字大小
        chartData.setValueLabelTypeface(Typeface.MONOSPACE);// 设置数据文字样式
        cityContrastChart.setLineChartData(chartData);

        cityContrastChart.setZoomEnabled(true);//设置是否支持缩放
        cityContrastChart.setInteractive(true);//设置图表是否可以与用户互动
        cityContrastChart.setZoomType(ZoomType.HORIZONTAL);

        Viewport v = new Viewport(cityContrastChart.getMaximumViewport());
        v.left = 0;
        v.right = 7;
        cityContrastChart.setCurrentViewport(v);
    }

    private void initFrom() {
        if (citys.size() < 3) {
            return;
        }
        // TODO Auto-generated method stub
        int numberOfPoints = citys.get(1).getAQI_data().size();
        // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
        List<AxisValue> axisXValues = new ArrayList<AxisValue>();
        List<Line> lines = new ArrayList<Line>();
        for (int i = 0; i < 3; ++i) {
            List<PointValue> values = new ArrayList<PointValue>();
            for (int j = 0; j < numberOfPoints; ++j) {
                values.add(new PointValue(j, Float.valueOf(citys.get(i + 1).getAQI_data().get(j).getValue())));

                axisXValues.add(new AxisValue(j).setLabel(citys.get(i + 1).getAQI_data().get(j).getTime()));
            }

//            Line line = new Line(values);
//            line.setColor(ChartUtils.COLORS[i]);
//            line.setShape(ValueShape.CIRCLE); //节点的形状
//            line.setHasLabels(true); //是否显示标签
//            line.setHasLabelsOnlyForSelected(false);  //标签是否只能选中
//            line.setHasLines(true); //是否显示折线
//            line.setHasPoints(true); //是否显示节点
//            line.setPointColor(ChartUtils.COLORS[(i + 1) % ChartUtils.COLORS.length]);
//            lines.add(line);

        }
        lines.add(getLine(citys.get(1).getAQI_data(), "#FFF1C55F"));
        lines.add(getLine(citys.get(2).getAQI_data(), "#FF47F646"));
        lines.add(getLine(citys.get(3).getAQI_data(), "#FF42DAFC"));
        LineChartData data = new LineChartData(lines);

        if (true) {
            data.setAxisXBottom(new Axis(axisXValues).setHasLines(true).setTextColor(Color.WHITE).setName("").setHasTiltedLabels(false).setMaxLabelChars(7));
            data.setAxisYLeft(new Axis().setHasLines(true).setName("").setTextColor(Color.WHITE).setMaxLabelChars(4));
        } else {
            data.setAxisXBottom(null);
            data.setAxisYLeft(null);
        }

        data.setBaseValue(Float.NEGATIVE_INFINITY);
        cityContrastChart.setValueSelectionEnabled(false);
        cityContrastChart.setLineChartData(data);
        cityContrastChart.setZoomEnabled(false);
    }

    private Line getLine(List<DataInfoBean> data, String color) {
        if (data == null) {
            return null;
        }
        List<PointValue> pointValues = new ArrayList<PointValue>();// 节点数据结合
        for (int i = 0; i < data.size(); i++) {
            pointValues.add(new PointValue(i, Float.parseFloat(data.get(i).getValue()) / 100));
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
        line.setHasLabelsOnlyForSelected(false);// 隐藏数据，触摸可以显示
        return line;
    }


    /**
     *
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
        }
    }

    private int state1;

//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    private void openPopwindow(View view, int state) {
//        this.state1 = state;
//        if (mPopupWindow != null) {
//            mPopupWindow.showAsDropDown(view, 0, 0, 30);
//            return;
//        }
//        // 将布局文件转换成View对象，popupview 内容视图
//        final View mPopView = getLayoutInflater().inflate(R.layout.pop_window, null);
//        // 将转换的View放置到 新建一个popuwindow对象中
//        mPopupWindow = new PopupWindow(mPopView,
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT);
//        ListView listview = (ListView) mPopView.findViewById(R.id.pop_window_list);
//        listview.setAdapter(new CitySpinnerAdapter(this, regionNetBean.getMessage()));
//        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                loadData(regionNetBean.getMessage().get(position).getRegionId(), state1);
//                if (state1 == 1) {
//                    cityContrastSp1.setText(regionNetBean.getMessage().get(position).getRegionName());
//                    cityContrastCity1.setText(regionNetBean.getMessage().get(position).getRegionName());
//                } else if (state1 == 2) {
//                    cityContrastCity2.setText(regionNetBean.getMessage().get(position).getRegionName());
//                    cityContrastSp2.setText(regionNetBean.getMessage().get(position).getRegionName());
//                } else {
//                    cityContrastCity3.setText(regionNetBean.getMessage().get(position).getRegionName());
//                    cityContrastSp3.setText(regionNetBean.getMessage().get(position).getRegionName());
//                }
//                if (mPopupWindow.isShowing()) {
//                    mPopupWindow.dismiss();
//                }
//            }
//        });
//        // 点击popuwindow外让其消失
//        mPopupWindow.setOutsideTouchable(true);
////        mPopupWindow.showAsDropDown(view, Gravity.CENTER, 200, 300);
//        mPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
//    }

    private void showCitySelect(View view, int state) {
        this.state1 = state;
        if (dialog != null) {
            dialog.show();
        }
        View viewDialog = getLayoutInflater().inflate(R.layout.pop_window, null);
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(viewDialog, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = dialog.getWindow();
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.65); // 宽度设置为屏幕的0.65
        window.setAttributes(p);
        dialog.onWindowAttributesChanged(p);
        ListView listview = (ListView) viewDialog.findViewById(R.id.pop_window_list);
        listview.setAdapter(new CitySpinnerAdapter(this, regionNetBean.getMessage()));
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
                if (mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
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

    private RegionNetBean getTest() {

        InputStreamReader inputStreamReader = null;
        try {
            InputStreamReader inputReader = new InputStreamReader(getResources().getAssets().open("city.json"));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null)
                Result += line;
            return new Gson().fromJson(Result, RegionNetBean.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        int id = group.getCheckedRadioButtonId();
        if (id == R.id.city_contrast_rbpm25_type) {
            datatype = cityContrastAdapter.PM25;
        } else if (id == R.id.city_contrast_rbpm10_type) {
            datatype = cityContrastAdapter.PM10;
        } else if (id == R.id.city_contrast_rbso2_type) {
            datatype = cityContrastAdapter.SO2;
        } else if (id == R.id.city_contrast_rbno2_type) {
            datatype = cityContrastAdapter.NO2;
        } else if (id == R.id.city_contrast_rbo3_type) {
            datatype = cityContrastAdapter.O3;
        } else if (id == R.id.city_contrast_rbco_type) {
            datatype = cityContrastAdapter.CO;
        }
        if (cityContrastAdapter != null) {
            cityContrastAdapter.setCode(datatype);
            cityContrastAdapter.notifyDataSetChanged();
        }
    }
}
