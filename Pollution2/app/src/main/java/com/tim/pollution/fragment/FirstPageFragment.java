package com.tim.pollution.fragment;

import android.app.Activity;
import android.app.AppOpsManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.AppOpsManagerCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.SDKInitializer;
import com.tim.pollution.MyApplication;
import com.tim.pollution.R;
import com.tim.pollution.activity.FocusCityActivity;
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
import com.tim.pollution.utils.CityListSaveUtil;
import com.tim.pollution.utils.DateUtil;
import com.tim.pollution.utils.LocationUtil;
import com.tim.pollution.view.WrapContentHeightViewPager;
import com.tim.pollution.view.WrapContentListView;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import lecho.lib.hellocharts.formatter.SimpleColumnChartValueFormatter;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.ColumnChartView;

import static android.app.Activity.RESULT_OK;

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

    @BindView(R.id.home_sw)
    SwipeRefreshLayout homeSw;
    private List<Fragment> fragments;
    private RegionNetBean regionNetBean;
    private ArrayList<String> regionIds;
    private AlertDialog dialogAgain;
    @BindView(R.id.home_weather_info_time)
    TextView homeWeatherInfoTime;
    private LocationUtil locationUtil;
    private boolean isFirstLocate = true;
    private HomeFragmentAdapter homeFragmentAdapter;
    private Map<String, Fragment> fragmentMap = new HashMap<>();
    private ProgressDialog pd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //重新提交
        //引用创建好的xml布局
        View view = inflater.inflate(R.layout.activity_home, container, false);
        findView(view);
        initClick();
        pd = ProgressDialog.show(getContext(), "提示", "加载数据中，请耐心等待......");
        checkLocationPermission();
        SDKInitializer.initialize(getActivity().getApplicationContext());
        return view;

    }
    /**
     * 检查定位服务、权限
     */
    private void checkLocationPermission() {
        homeSw.setRefreshing(false);
        if (!isLocServiceEnable(MyApplication.getContext())) {//检测是否开启定位服务
            showLocationDailog(0);
        } else {//检测用户是否将当前应用的定位权限拒绝
            int checkResult = checkOp(MyApplication.getContext(), 2, AppOpsManager.OPSTR_FINE_LOCATION);//其中2代表AppOpsManager.OP_GPS，如果要判断悬浮框权限，第二个参数需换成24即AppOpsManager。OP_SYSTEM_ALERT_WINDOW及，第三个参数需要换成AppOpsManager.OPSTR_SYSTEM_ALERT_WINDOW
            int checkResult2 = checkOp(MyApplication.getContext(), 1, AppOpsManager.OPSTR_FINE_LOCATION);
            if (AppOpsManagerCompat.MODE_IGNORED == checkResult || AppOpsManagerCompat.MODE_IGNORED == checkResult2) {
                showLocationDailog(1);
            }else{
                locationUtil = new LocationUtil(getActivity(), this);
            }
        }
    }
    /**
     * 检查权限列表
     *
     * @param context
     * @param op       这个值被hide了，去AppOpsManager类源码找，如位置权限  AppOpsManager.OP_GPS==2
     * @param opString 如判断定位权限 AppOpsManager.OPSTR_FINE_LOCATION
     * @return @see 如果返回值 AppOpsManagerCompat.MODE_IGNORED 表示被禁用了
     */
    public static int checkOp(Context context, int op, String opString) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 19) {
            Object object = context.getSystemService(Context.APP_OPS_SERVICE);
//            Object object = context.getSystemService("appops");
            Class c = object.getClass();
            try {
                Class[] cArg = new Class[3];
                cArg[0] = int.class;
                cArg[1] = int.class;
                cArg[2] = String.class;
                Method lMethod = c.getDeclaredMethod("checkOp", cArg);
                return (Integer) lMethod.invoke(object, op, Binder.getCallingUid(), context.getPackageName());
            } catch (Exception e) {
                e.printStackTrace();
                if (Build.VERSION.SDK_INT >= 23) {
                    return AppOpsManagerCompat.noteOp(context, opString, context.getApplicationInfo().uid,
                            context.getPackageName());
                }

            }
        }
        return -1;
    }
    /**
     * 开启定位权限
     */
    private void showLocationDailog(final int state ) {
        if (pd != null) {
            pd.dismiss();
        }
        if (dialogAgain != null) {
            dialogAgain.show();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("提示");
        builder.setMessage("定位权限没有打开，请打开权限");
        builder.setIcon(R.mipmap.prompt);
        //点击对话框以外的区域是否让对话框消失
        builder.setCancelable(false);
        builder.setNegativeButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogAgain.dismiss();
                Intent intent = new Intent();
                if (state == 0) {
                    //定位服务页面
                    intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                } else {
                    //应用详情页面
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.parse("package:" + activity.getPackageName()));
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(intent);
            }
        });
        builder.setPositiveButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pd = ProgressDialog.show(getContext(), "提示", "加载数据中，请耐心等待......");
                dialogAgain.dismiss();
                loadLocation();
            }
        });
        dialogAgain = builder.create();
        if (dialogAgain != null) {
            //显示对话框
            dialogAgain.show();
        }
    }
    /**
     * 手机是否开启位置服务，如果没有开启那么所有app将不能使用定位功能
     */
    public static boolean isLocServiceEnable(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }
        return false;
    }
    @Override
    public void onStart() {
        super.onStart();
    }
    private void findView(View view) {
        homeWeatherInfoTime = (TextView) view.findViewById(R.id.home_weather_info_time);
        weatherTitleLocation = (TextView) view.findViewById(R.id.weather_title_location);
        textView = (TextView) view.findViewById(R.id.textView);
        weatherTitleLocationSelect = (TextView) view.findViewById(R.id.weather_title_location_select);
        homeVp = (WrapContentHeightViewPager) view.findViewById(R.id.home_vp);
        llDots = (LinearLayout) view.findViewById(R.id.ll_dots);
        homeTop = (LinearLayout) view.findViewById(R.id.home_top);
        homeDetail = (TextView) view.findViewById(R.id.home_detail);
        homeChart = (ColumnChartView) view.findViewById(R.id.home_chart);
        homeList = (WrapContentListView) view.findViewById(R.id.home_list);
        homeWeatherInfoName = (TextView) view.findViewById(R.id.home_weather_info_name);
        homeWeatherInfoVa = (TextView) view.findViewById(R.id.home_weather_info_va);
        homeWeatherInfoCity = (TextView) view.findViewById(R.id.home_weather_info_city);
        homeWeatherInfoCityVa = (TextView) view.findViewById(R.id.home_weather_info_city_va);
        homeSw= (SwipeRefreshLayout) view.findViewById(R.id.home_sw);
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
     * 初始化点击事件
     */
    private void initClick() {
        homeDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), WeatherVariationTrendActivity.class);
                int position = homeVp.getCurrentItem();
                if(regionIds!=null&&regionIds.size()>0){
                    intent.putExtra("regionid", regionIds.get(position));
                    startActivity(intent);
                }
            }
        });
        weatherTitleLocationSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FocusCityActivity.class);
                intent.putExtra("state","01");
                startActivityForResult(intent,1001);
            }
        });
        homeSw.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pd = ProgressDialog.show(getContext(), "提示", "加载数据中，请耐心等待......");
                checkLocationPermission();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1001&&resultCode==RESULT_OK){
            pd = ProgressDialog.show(getContext(), "提示", "加载数据中，请耐心等待......");
            checkLocationPermission();
        }
    }

    /**
     * 获取县区列表
     */
    private void loadLocation() {
        Map<String, String> params = new HashMap<>();
        params.put("key", Constants.key);
//        params.put("regiontype", "region");
        params.put("regiontype", "allregion");
        WeatherDal.getInstance().getRegion(params, this);
    }
    private RegionNetBean.RegionBean getRegion(List<RegionNetBean.RegionBean> regionBeanList,String locationName){
        Log.e("test","城市长度："+regionBeanList.size()+":"+locationName);
        if(regionBeanList==null|| TextUtils.isEmpty(locationName)){
            return null;
        }
        for(RegionNetBean.RegionBean regionBean:regionBeanList){
            Log.e("test","城市："+regionBean.getRegionName()+":"+locationName);
            if(regionBean.getRegionName().contains(locationName)){
                return regionBean;
            }
        }
        return null;
    }

    /**
     * 显示关注城市，无关注城市，显示县区列表的第一项
     */
    private void loadData() {
        homeVp.setAdapter(null);
        regionIds = new ArrayList<>();
        List<String> regionBeans = CityListSaveUtil.getList(getContext(), CityListSaveUtil.CITY_FILE, CityListSaveUtil.CITY_KEY);
        if (regionBeans != null && regionBeans.size() > 0) {
            RegionNetBean.RegionBean regionBean=new RegionNetBean.RegionBean();
            regionBean.setRegionName(weatherTitleLocation.getText().toString());
//            regionBean.setRegionName("阳曲县");
            /*if(regionNetBean.getMessage().contains(regionBean)){//添加定位城市
                int index=regionNetBean.getMessage().indexOf(regionBean);
                if(!regionBeans.contains(regionNetBean.getMessage().get(index).getRegionId())){
                    regionIds.add(regionNetBean.getMessage().get(index).getRegionId());
                }
            }*/
            RegionNetBean.RegionBean regionBeanLocation=getRegion(regionNetBean.getMessage(),weatherTitleLocation.getText().toString());//获取定位城市信息
            if(regionBeanLocation!=null&&!regionBeans.contains(regionBeanLocation.getRegionId())){
                regionIds.add(regionBeanLocation.getRegionId());
            }
            for (String rb : regionBeans) {
                regionIds.add(rb);
            }
        } else {
            if (regionNetBean == null || regionNetBean.getMessage() == null) {
                Toast.makeText(getContext(), "服务器异常，请稍后重试", Toast.LENGTH_LONG).show();
                return;
            }
            RegionNetBean.RegionBean regionBean=new RegionNetBean.RegionBean();
            regionBean.setRegionName(weatherTitleLocation.getText().toString());
            RegionNetBean.RegionBean regionBeanLocation=getRegion(regionNetBean.getMessage(),weatherTitleLocation.getText().toString());//获取定位城市信息
//            if(regionNetBean.getMessage().contains(regionBean)){
//                int index=regionNetBean.getMessage().indexOf(regionBean);
//                regionIds.add(regionNetBean.getMessage().get(index).getRegionId());
            if(regionBeanLocation!=null){
                regionIds.add(regionBeanLocation.getRegionId());
            }else{
                regionIds.add(regionNetBean.getMessage().get(0).getRegionId());
            }

        }
        fragments = new ArrayList<>();
        for (int i = 0; i < regionIds.size(); i++) {
            FirstPageTopFragment fragment = new FirstPageTopFragment();
            Bundle bundle = new Bundle();
            bundle.putString("regionId", regionIds.get(i));//这里的values就是我们要传的值
            fragment.setArguments(bundle);
            fragments.add(fragment);
            fragmentMap.put(regionIds.get(i), fragment);
        }
        initDots();
        homeFragmentAdapter = new HomeFragmentAdapter(getChildFragmentManager(), fragments);
        homeVp.setAdapter(homeFragmentAdapter);
        homeVp.setCurrentItem(0);
        // 设置ViewPager的监听
        homeVp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                homeChart.setColumnChartData(null);
                MessageBean msg = weatherDatamap.get(regionIds.get(position));
                if (msg != null) {
                    initList(msg);
                    initBaseInfo(msg);
                    initCharts2(msg);
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
    public void onSuccess(Object data) {
        MData mData = (MData) data;
        if (MDataType.REGIONNET_BEAN.equals(mData.getType())) {
            regionNetBean = (RegionNetBean) mData.getData();
            if (regionNetBean != null) {
                loadData();
            }
        } else if (mData.getType().equals(MDataType.MAP)) {
            try {
                BDLocation location = (BDLocation) mData.getData();
//                Toast.makeText(getContext(),"定位成功",Toast.LENGTH_LONG).show();
                if (isFirstLocate) {
                    weatherTitleLocation.setText(location.getCity());
//                    weatherTitleLocation.setText("长治");
                }
                loadLocation();
            } catch (Exception e) {

            }

        }

    }


    @Override
    public void onError(String msg, String eCode) {
        if (pd != null) {
            pd.dismiss();
        }
        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
        showAgainDailog(msg);
    }

    /**
     * 重试
     */
    private void showAgainDailog(String msg) {
        if (dialogAgain != null) {
            dialogAgain.show();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("提示");
        builder.setMessage(msg + ",是否重试？");
        builder.setIcon(R.mipmap.prompt);
        //点击对话框以外的区域是否让对话框消失
        builder.setCancelable(false);
        builder.setNegativeButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                loadLocation();
                dialogAgain.dismiss();
            }
        });
        dialogAgain = builder.create();
        if (dialogAgain != null) {
            //显示对话框
            dialogAgain.show();
        }
    }


    /**
     * 初始化表格
     */
    private void initCharts2(MessageBean msg) {
        if (msg.getAQI_24h() != null && msg.getAQI_24h().size() > 0) {
            List<AQI24hBean> list = msg.getAQI_24h();
            Log.w("tcy","柱状图："+list.toString());
            //每个集合显示几条柱子
            int numSubcolumns = 1;
            //显示多少个集合
            int numColumns = list.size();
            //保存所有的柱子
            final List<Column> columns = new ArrayList<Column>();
            //保存每个竹子的值
            List<SubcolumnValue> values;
            List<AxisValue> axisXValues = new ArrayList<AxisValue>();
            //对每个集合的柱子进行遍历
            for (int i = 0; i < numColumns; ++i) {

                values = new ArrayList<SubcolumnValue>();
                //循环所有柱子（list）
                for (int j = 0; j < numSubcolumns; ++j) {
                    //创建一个柱子，然后设置值和颜色，并添加到list中
                    SubcolumnValue sub = new SubcolumnValue(Float.valueOf(list.get(i).getAQI()), Color.parseColor(list.get(i).getAQIcolor()));
                    sub.setLabel("AQI " + getIntFromString(list.get(i).getAQI())+"\n 时间 "+switchTime(list.get(i).getTime()));
                    values.add(sub);
                    //设置X轴的柱子所对应的属性名称
                    axisXValues.add(new AxisValue(i).setLabel(switchTime(list.get(i).getTime())));
                }
                //将每个属性的拥有的柱子，添加到Column中
                Column column = new Column(values);
                //是否显示每个柱子的Lable
                column.setHasLabels(false);
                //设置每个柱子的Lable是否选中，为false，表示不用选中，一直显示在柱子上
                column.setHasLabelsOnlyForSelected(true);
                column.setFormatter(new SimpleColumnChartValueFormatter(2));

                //将每个属性得列全部添加到List中
                columns.add(column);
            }
          /*  List<AxisValue> axisYValues = new ArrayList<AxisValue>();
            for (int i = 0; i <= 500; i += 50) {
                axisYValues.add(new AxisValue(i).setValue(i).setLabel("AQI " + i));
            }*/
            //设置Columns添加到Data中
            ColumnChartData data = new ColumnChartData(columns);
            //设置X轴显示在底部，并且显示每个属性的Lable，字体颜色为黑色，X轴的名字为“学历”，每个柱子的Lable斜着显示，距离X轴的距离为8
            data.setAxisXBottom(new Axis(axisXValues).setHasLines(false).setTextColor(Color.WHITE).setName("").setHasTiltedLabels(false).setMaxLabelChars(5));
            //属性值含义同X轴
            data.setAxisYLeft(new Axis().setHasLines(false).setAutoGenerated(true).setName("").setTextColor(Color.WHITE).setMaxLabelChars(3));
            //最后将所有值显示在View中
            homeChart.setColumnChartData(data);
            homeChart.setZoomEnabled(false);
            //
            homeChart.setValueSelectionEnabled(true);
            homeChart.setInteractive(true);
            //
            Viewport v = new Viewport(homeChart.getMaximumViewport());
            v.bottom = 0f;
            v.top += v.top * 0.2;

            //固定Y轴的范围,如果没有这个,Y轴的范围会根据数据的最大值和最小值决定,这不是我想要的
            homeChart.setMaximumViewport(v);

            //这2个属性的设置一定要在lineChart.setMaximumViewport(v);这个方法之后,不然显示的坐标数据是不能左右滑动查看更多数据的
            v.right = 30;
            v.left = 13;
            homeChart.setCurrentViewport(v);

        } else {
            homeChart.setColumnChartData(null);
        }
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
    private String switchTime(String time) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            return sdf.format(DateUtil.strToDateLong(time));
        } catch (Exception e) {
            return time;
        }

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
                try{
                    Intent intent = new Intent(getContext(), SiteWeatherDetailActivity.class);
                    String regionid = regionIds.get(homeVp.getCurrentItem());
                    String pointcode = weatherDatamap.get(regionid).getPoint_AQI().get(position).getPointCode();
                    intent.putExtra("pointcode", pointcode);
                    startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getContext(),"系统繁忙，请刷新后重试",Toast.LENGTH_LONG).show();
                }

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
    public void prograss(MessageBean messageBean,String regionId) {
        if(messageBean!=null){
            if(messageBean.getRegionList()!=null){
                int index = regionIds.indexOf(messageBean.getRegionList().getRegionId());
                Log.e("tcy1", "回来的城市：" + messageBean.getRegionList().getRegionName() + ",id:" + messageBean.getRegionList().getRegionId());
                if (index >= 0) {
                    weatherDatamap.put(regionIds.get(index), messageBean);
                    if (index == 0) {
                        initBaseInfo(messageBean);
                        initCharts2(messageBean);
                        initList(messageBean);
                    }
                }
            }else{
                int index = regionIds.indexOf(regionId);
                if (index > -1 && homeFragmentAdapter != null) {
                    regionIds.remove(regionId);
                    fragments.remove(fragmentMap.get(regionId));
                    initDots();
                    homeFragmentAdapter.notifyDataSetChanged();
                }
            }
        }else{
            int index = regionIds.indexOf(regionId);
            if (index > -1 && homeFragmentAdapter != null) {
                regionIds.remove(regionId);
                fragments.remove(fragmentMap.get(regionId));
                initDots();
                homeFragmentAdapter.notifyDataSetChanged();
            }
        }

        if (pd != null) {
            pd.dismiss();
        }
    }

    /**
     * 获取信息失败
     *
     * @param regionId
     */
    @Override
    public void error(String regionId) {
        Log.e("tcy1", "移除：" + regionId);
        int index = regionIds.indexOf(regionId);//// TODO: 2018/5/4
        if (index > -1 && homeFragmentAdapter != null) {
            regionIds.remove(regionId);
            fragments.remove(fragmentMap.get(regionId));
            initDots();
            homeFragmentAdapter.notifyDataSetChanged();
        }
        if (pd != null) {
            pd.dismiss();
        }
    }

    private void initBaseInfo(MessageBean messageBean) {
        homeWeatherInfoTime.setText(messageBean.getRegionList().getTime());
        //只显示定位的数据不显示后台返回数据
//        weatherTitleLocation.setText(messageBean.getRegionList().getRegionName());
//        weatherTitleLocationSelect.setText(messageBean.getRegionList().getRegionName());
        String info = messageBean.getRegionList().getPM25() + "\n" + messageBean.getRegionList().getPM10() + "\n" + messageBean.getRegionList().getSO2() + "\n" + messageBean.getRegionList().getNO2() + "\n" + messageBean.getRegionList().getO3() + "\n" + messageBean.getRegionList().getCO();
        homeWeatherInfoVa.setText(info);
        homeWeatherInfoCity.setText(messageBean.getRegionList().getRegionName());
        homeWeatherInfoCityVa.setText(messageBean.getRegionList().getAQI());
    }
}
