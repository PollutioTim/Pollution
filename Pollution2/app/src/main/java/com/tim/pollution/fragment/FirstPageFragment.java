package com.tim.pollution.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.tim.pollution.MyApplication;
import com.tim.pollution.R;
import com.tim.pollution.activity.CityActivity;
import com.tim.pollution.activity.SiteWeatherDetailActivity;
import com.tim.pollution.activity.WeatherVariationTrendActivity;
import com.tim.pollution.adapter.HomeFragmentAdapter;
import com.tim.pollution.adapter.WeatherPointAdapter;
import com.tim.pollution.bean.changetrend.RegionNetBean;
import com.tim.pollution.bean.weather.AQI24hBean;
import com.tim.pollution.bean.weather.MessageBean;
import com.tim.pollution.callback.ICallBack;
import com.tim.pollution.general.Constants;
import com.tim.pollution.general.MData;
import com.tim.pollution.general.MDataType;
import com.tim.pollution.net.WeatherDal;
import com.tim.pollution.utils.DateUtil;
import com.tim.pollution.view.WrapContentHeightViewPager;
import com.tim.pollution.view.WrapContentListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.ColumnChartView;

public class FirstPageFragment extends Fragment implements ICallBack, AdapterView.OnItemClickListener, FragmentCallBack {


    @BindView(R.id.weather_title_location)
    TextView weatherTitleLocation;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.weather_title_location_select)
    TextView weatherTitleLocationSelect;
    @BindView(R.id.home_vp)
    WrapContentHeightViewPager homeVp;
    @BindView(R.id.ll_dots)
    LinearLayout llDots;
    @BindView(R.id.home_top)
    LinearLayout homeTop;
    @BindView(R.id.home_detail)
    TextView homeDetail;
    @BindView(R.id.home_chart)
    ColumnChartView homeChart;
    @BindView(R.id.home_list)
    WrapContentListView homeList;
    @BindView(R.id.home_weather_info_name)
    TextView homeWeatherInfoName;
    @BindView(R.id.home_weather_info_va)
    TextView homeWeatherInfoVa;
    @BindView(R.id.home_weather_info_city)
    TextView homeWeatherInfoCity;
    @BindView(R.id.home_weather_info_city_va)
    TextView homeWeatherInfoCityVa;
    private ArrayList<ImageView> dotsList;

    private List<Fragment> fragments;
    private RegionNetBean regionNetBean;
    private ArrayList<String> regionIds;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //引用创建好的xml布局
        View view = inflater.inflate(R.layout.activity_home, container, false);
        findView(view);
        loadLocation();
        initClick();
        return view;

    }

    /**
     *
     */
    private void findView(View view) {

         weatherTitleLocation= (TextView) view.findViewById(R.id.weather_title_location);

         textView= (TextView) view.findViewById(R.id.textView);

         weatherTitleLocationSelect= (TextView) view.findViewById(R.id.weather_title_location_select);

         homeVp= (WrapContentHeightViewPager) view.findViewById(R.id.home_vp);

         llDots= (LinearLayout) view.findViewById(R.id.ll_dots);

         homeTop= (LinearLayout) view.findViewById(R.id.home_top);

         homeDetail= (TextView) view.findViewById(R.id.home_detail);

         homeChart= (ColumnChartView) view.findViewById(R.id.home_chart);

         homeList= (WrapContentListView) view.findViewById(R.id.home_list);

         homeWeatherInfoName= (TextView) view.findViewById(R.id.home_weather_info_name);

         homeWeatherInfoVa= (TextView) view.findViewById(R.id.home_weather_info_va);

         homeWeatherInfoCity= (TextView) view.findViewById(R.id.home_weather_info_city);

         homeWeatherInfoCityVa= (TextView) view.findViewById(R.id.home_weather_info_city_va);
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
       activity=activity;
    }
    private void initClick() {
        homeDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), WeatherVariationTrendActivity.class);
                int position = homeVp.getCurrentItem();
                intent.putExtra("regionid", regionIds.get(position));
                startActivity(intent);
            }
        });
        weatherTitleLocationSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CityActivity.class);
               startActivity(intent);
            }
        });


    }

    private void loadLocation() {
        if (true) {
            Map<String, String> params = new HashMap<>();
            params.put("key", Constants.key);
            params.put("regiontype", "allregion");
            WeatherDal.getInstance().getRegion(params, this);
        }
    }

    private void loadData() {
        //todo 写死
        regionIds = new ArrayList<>();
        regionIds.add("140123");
        regionIds.add("140201");
        if (regionNetBean == null || regionNetBean.getMessage() == null) {
            return;
        }
        regionIds.add(regionNetBean.getMessage().get(0).getRegionId());
        fragments = new ArrayList<>();
        for (int i = 0; i < regionIds.size(); i++) {
            FirstPageTopFragment fragment = new FirstPageTopFragment();
            Bundle bundle = new Bundle();
            bundle.putString("regionId", regionIds.get(i));//这里的values就是我们要传的值
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }
//        loadWeatherData(regionIds.get(0));
        initDots();
        homeVp.setAdapter(new HomeFragmentAdapter(getChildFragmentManager(), fragments));
        homeVp.setCurrentItem(0);
        // 设置ViewPager的监听
        homeVp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                MessageBean msg = weatherDatamap.get(regionIds.get(position));
                if (msg != null) {
                    initBaseInfo(msg);
                    initCharts2(msg);
                    initList(msg);
                }

                //遍历存放图片的数组
                for (int i = 0; i < fragments.size(); i++) {
                    //判断小点点与当前的图片是否对应，对应设置为亮色 ，否则设置为暗色
                    if (i == position % fragments.size()) {
                        dotsList.get(i).setImageDrawable(
                                getResources().getDrawable(
                                        R.drawable.dot_white));
                    } else {
                        dotsList.get(i).setImageDrawable(
                                getResources().getDrawable(
                                        R.drawable.dot_gray));
                    }

                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }


    /**
     * 初始化小点
     */
    private void initDots() {
        //创建存放小点点的集合
        dotsList = new ArrayList<ImageView>();
        //每次初始化之前清空集合
        dotsList.clear();
        // 每次初始化之前  移除  布局中的所有小点
        llDots.removeAllViews();
        for (int i = 0; i < fragments.size(); i++) {
            //创建小点点图片
            ImageView imageView = new ImageView(getContext());
            Drawable drawable = null;
            if (i == 0) {
                // 亮色图片
                drawable = getResources().getDrawable(R.drawable.dot_white);

            } else {
                drawable = getResources().getDrawable(R.drawable.dot_gray);
            }
            imageView.setImageDrawable(drawable);
            // 考虑屏幕适配
            LayoutParams params = new LayoutParams(dip2px(getContext(), 10), dip2px(getContext(), 10));
            //设置小点点之间的间距    
            params.setMargins(dip2px(getContext(), 5), 0, dip2px(getContext(), 5), 0);
            //将小点点添加大线性布局中
            llDots.addView(imageView, params);
            // 将小点的控件添加到集合中
            dotsList.add(imageView);
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private Map<String, MessageBean> weatherDatamap = new HashMap<>();

    @Override
    public void onProgress(Object data) {
        MData mData = (MData) data;
        if (MDataType.REGIONNET_BEAN.equals(mData.getType())) {
            regionNetBean = (RegionNetBean) mData.getData();
//            regionNetBean = getTest();
            if (regionNetBean != null) {
                loadData();
            }
        }

    }


    @Override
    public void onError(String msg, String eCode) {

    }

    /**
     * 初始化表格
     */
    private void initCharts(MessageBean msg) {
        if (msg.getAQI_24h() != null && msg.getAQI_24h().size() > 0) {
            List<AQI24hBean> list = msg.getAQI_24h();
            //每个集合显示几条柱子
            int numSubcolumns = 1;
            //显示多少个集合
            int numColumns = list.size();
            //保存所有的柱子
            List<Column> columns = new ArrayList<Column>();
            //保存每个竹子的值
            List<SubcolumnValue> values;
            List<AxisValue> axisXValues = new ArrayList<AxisValue>();
            List<AxisValue> axisYValues = new ArrayList<AxisValue>();
            for (int i = 0; i <= 500; i += 50) {
                axisYValues.add(new AxisValue(i).setValue(i).setLabel(i + ""));
            }
            //对每个集合的柱子进行遍历
            for (int i = 0; i < numColumns; ++i) {

                values = new ArrayList<SubcolumnValue>();
                //循环所有柱子（list）
                for (int j = 0; j < numSubcolumns; ++j) {
                    //创建一个柱子，然后设置值和颜色，并添加到list中
                    values.add(new SubcolumnValue(Float.valueOf(list.get(i).getAQI()), Color.parseColor(list.get(i).getAQIcolor())));
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
            data.setAxisXBottom(new Axis(axisXValues).setHasLines(true).setTextColor(Color.WHITE).setName("").setHasTiltedLabels(false).setMaxLabelChars(5));
            Axis axisY = new Axis().setHasLines(true);
            axisY.setMaxLabelChars(6);//max label length, for example 60
            List<AxisValue> valuesY = new ArrayList<>();
            for (int i = 0; i < 100; i += 10) {
                AxisValue value = new AxisValue(i);
                String label = "";
                value.setLabel(label);
                valuesY.add(value);
            }
            axisY.setValues(valuesY);
            //属性值含义同X轴
//            data.setAxisYLeft(new Axis().setHasLines(true).setName("").setTextColor(Color.WHITE).setMaxLabelChars(1));
            data.setAxisYLeft(axisY);
            //最后将所有值显示在View中
            homeChart.setColumnChartData(data);
            homeChart.setZoomEnabled(true);
            Viewport v = new Viewport(homeChart.getMaximumViewport());
//            v.top=5000;
            homeChart.setCurrentViewport(v);
        } else {
            homeChart.setColumnChartData(null);
//            homeChart.setVisibility(View.GONE);
        }
    }

    private void initCharts2(MessageBean msg) {
        if (msg.getAQI_24h() != null && msg.getAQI_24h().size() > 0) {
            List<AQI24hBean> list = msg.getAQI_24h();
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
                    values.add(new SubcolumnValue(Float.valueOf(list.get(i).getAQI()), Color.parseColor(list.get(i).getAQIcolor())));
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
            for (int i = 0; i <= 500; i += 50) {
                axisYValues.add(new AxisValue(i).setValue(i).setLabel(i + ""));
            }
            //设置Columns添加到Data中
            ColumnChartData data = new ColumnChartData(columns);
            //设置X轴显示在底部，并且显示每个属性的Lable，字体颜色为黑色，X轴的名字为“学历”，每个柱子的Lable斜着显示，距离X轴的距离为8
            data.setAxisXBottom(new Axis(axisXValues).setHasLines(false).setTextColor(Color.WHITE).setName("").setHasTiltedLabels(false).setMaxLabelChars(8));
            //属性值含义同X轴
            data.setAxisYLeft(new Axis(axisYValues).setHasLines(false).setAutoGenerated(true).setName("").setTextColor(Color.WHITE).setMaxLabelChars(3));
            //最后将所有值显示在View中
            homeChart.setColumnChartData(data);
            Viewport v = new Viewport(homeChart.getMaximumViewport());
            v.right = 10;
            v.top = 500;
            homeChart.setCurrentViewport(v);
        }else{
            homeChart.setColumnChartData(null);
        }
    }

    private String switchTime(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(DateUtil.strToDateLong(time));
    }

    /**
     * 初始化列表
     *
     * @param
     */
    private void initList(MessageBean msg) {
        if (msg != null) {
            if (msg.getPoint_AQI() != null) {
                homeList.setAdapter(new WeatherPointAdapter(getContext(), msg.getPoint_AQI()));
            }
        }
        homeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), SiteWeatherDetailActivity.class);
                String regionid = regionIds.get(homeVp.getCurrentItem());
                String pointcode = weatherDatamap.get(regionid).getPoint_AQI().get(position).getPointCode();
                intent.putExtra("pointcode", pointcode);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    /**
     * fragment回调
     *
     * @param messageBean
     */
    @Override
    public void prograss(MessageBean messageBean) {
        int index = regionIds.indexOf(messageBean.getRegionList().getRegionId());
        weatherDatamap.put(regionIds.get(index), messageBean);
        if (index == 0) {
            initBaseInfo(messageBean);
            initCharts2(messageBean);
            initList(messageBean);
        }
    }

    private void initBaseInfo(MessageBean messageBean) {
        weatherTitleLocation.setText(messageBean.getRegionList().getRegionName());
        String info = messageBean.getRegionList().getPM25() + "\n" + messageBean.getRegionList().getPM10() + "\n" + messageBean.getRegionList().getSO2() + "\n" + messageBean.getRegionList().getNO2() + "\n" + messageBean.getRegionList().getO3() + "\n" + messageBean.getRegionList().getCO();
        homeWeatherInfoVa.setText(info);
        homeWeatherInfoCity.setText(messageBean.getRegionList().getRegionName());
        homeWeatherInfoCityVa.setText(messageBean.getRegionList().getAQI());
    }
}
