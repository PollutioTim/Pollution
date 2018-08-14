package com.tim.pollution.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.tim.pollution.MyApplication;
import com.tim.pollution.R;
import com.tim.pollution.adapter.MapCityAdapter;
import com.tim.pollution.adapter.MapColorAdapter;
import com.tim.pollution.bean.ClickMapBean;
import com.tim.pollution.bean.ClickMapListBean;
import com.tim.pollution.bean.ClickPointBean;
import com.tim.pollution.bean.LevePollutionBean;
import com.tim.pollution.bean.MapPointBean;
import com.tim.pollution.callback.ICallBack;
import com.tim.pollution.callback.OnItemClickListener;
import com.tim.pollution.general.BaseActivity;
import com.tim.pollution.general.Constants;
import com.tim.pollution.general.LocationData;
import com.tim.pollution.general.MData;
import com.tim.pollution.general.MDataType;
import com.tim.pollution.general.MessageEvent;
import com.tim.pollution.net.MapDAL;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static android.media.CamcorderProfile.get;

/**
 * Created by lenovo on 2018/4/29.
 */

public class MapActivity extends BaseActivity implements ICallBack,BaiduMap.OnMarkerClickListener, OnItemClickListener {
    @BindView(R.id.map_view)
    MapView mapView;
    @BindView(R.id.map_ac_recyview)
    RecyclerView recyclerView;
    @BindView(R.id.map_city_recyclerview)
    RecyclerView recyCity;
    @BindView(R.id.map_ac_city_tv)
    TextView tvCityName;
    @BindView(R.id.map_ac_swicth_tv)
    TextView tvSwicth;
    @BindView(R.id.map_all_ll)
    LinearLayout llAll;
    @BindView(R.id.map_all_tv)
    TextView tvAll;
    private MyLocationConfiguration.LocationMode mCurrentMode;
    private BaiduMap mBaiduMap;
    private boolean isFirstLocate = true;
    private LinearLayoutManager lm;
    private ArrayList<LevePollutionBean> datas;
    private List<ClickMapBean> clickMapBeens;
    private List<MapPointBean.PointBean> points;
    private MapColorAdapter adapter;
    private Map<String, String> parms;
    private String regionid = "";
    private String regiontype = "region";
    private GridLayoutManager lms;
    private MapCityAdapter cityAdapter;
    private boolean isFirst = true;
    @BindView(R.id.map_time_tv)
    TextView tvTime;
    private String time;

    private String pointCityId;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        try {
            isFirst = true;
            tvAll.setText("全部");
            llAll.setSelected(false);
            regionid = intent.getStringExtra("id");
            Log.e("lili", "onNewIntent====regionid=" + regionid);
            regiontype = "region";
            mBaiduMap.clear();
            if (clickMapBeens != null) {
                clickMapBeens.clear();
            }
            init();
        }catch (Exception e){
            Log.e("lili","onNewIntent =====e="+e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_map;
    }

    @Override
    public void initView() {
        //初始化地图
        setActivityName("污染地图");
        SDKInitializer.initialize(this.getApplicationContext());
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        mBaiduMap = mapView.getMap();
        mapView.showZoomControls(false);
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(mCurrentMode,
                true, null));
        lm = new LinearLayoutManager(this);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(lm);
        datas = new ArrayList<>();
        MyApplication myApplication = (MyApplication) getApplication();
        datas = (ArrayList<LevePollutionBean>) myApplication.getLevePollutionBeans();
        adapter = new MapColorAdapter(this, datas);
        recyclerView.setAdapter(adapter);

        lms = new GridLayoutManager(this, 4);
        recyCity.setLayoutManager(lms);
        clickMapBeens = new ArrayList<>();
        cityAdapter = new MapCityAdapter(this, clickMapBeens);
        cityAdapter.setOnItemClickListener(this);
        recyCity.setAdapter(cityAdapter);
        llAll.setSelected(false);//全部 还是 个别
    }

    @Override
    public void initData() {
        EventBus.getDefault().register(this);
        regionid = getIntent().getStringExtra("id");
        //获取数据
        points = new ArrayList<>();
        time = getIntent().getStringExtra("time");
        tvTime.setText(time);
        init();
    }

    @OnClick({R.id.map_ac_swicth_tv, R.id.map_all_ll})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.map_ac_swicth_tv:
                Intent intent = new Intent(this, CityActivity.class);
                startActivity(intent);
                break;
            case R.id.map_all_ll:
                if (llAll.isSelected()) {
                    tvAll.setText("全部");
                    llAll.setSelected(false);
                } else {
                    tvAll.setText("区县");
                    llAll.setSelected(true);
                }
                setAllMarker();
                break;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 注销订阅者
        EventBus.getDefault().unregister(this);
        //在Fragment执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mapView.onDestroy();
    }

    private void init() {//
        if (parms == null) {
            parms = new HashMap<>();
        }

        parms.put("key", Constants.key);
        parms.put("regiontype", regiontype);
        parms.put("datatype", "many");
        parms.put("regionid", regionid);
        MapDAL.getInstance().getClickCityData(parms, this);
    }


    private void initPoint() {//获得站点
        if (parms == null) {
            parms = new HashMap<>();
        }

        parms.put("key", Constants.key);
        parms.put("regiontype", regiontype);
        parms.put("datatype", "many");
        parms.put("regionid", pointCityId);
        Log.e("lili", "initPoint====regionid=" + pointCityId);
        MapDAL.getInstance().getPointData(parms, this);
    }

    private void initClickPoint() {//获得站点
        if (parms == null) {
            parms = new HashMap<>();
        }
        parms.put("key", Constants.key);
        parms.put("regiontype", regiontype);
        parms.put("datatype", "many");
        parms.put("regionid", regionid);
        MapDAL.getInstance().getPointClickData(parms, this);
    }

    /**
     * 数据请求
     *
     * @param data
     */
    @Override
    public void onSuccess(Object data) {
        try {
            MData data1 = (MData) data;
            if (data1.getType().equals(MDataType.MAP_CLICK_DATA)) {//获得区县
                ClickMapListBean mapBean = (ClickMapListBean) data1.getData();
                if (clickMapBeens != null) {
                    clickMapBeens.clear();
                }

                clickMapBeens.addAll(mapBean.getMessage());
                cityAdapter.notifyDataSetChanged();
                //获取县城的第一个数据的站点显示在地图上
                moveMap(Double.valueOf(clickMapBeens.get(0).getPointLatitude()),
                        Double.valueOf(clickMapBeens.get(0).getPointLongitude()));

                if (isFirst) {
                    isFirst = false;
                    tvCityName.setText(clickMapBeens.get(0).getRegionName());
                    regiontype = "point";
                    pointCityId = clickMapBeens.get(0).getRegionId();
                }
                initPoint();
            } else if (data1.getType().equals(MDataType.MAP_POINT_DATA)) {//获得站点详情
                MapPointBean bean = (MapPointBean) data1.getData();
                if (points.size() > 0) {
                    points.clear();
                }
                points.addAll(bean.getMessages());
                for (MapPointBean.PointBean clickMapBean : points) {
                    //设置坐标点
                    LatLng point1 = new LatLng(Double.valueOf(clickMapBean.getPointLatitude()),
                            Double.valueOf(clickMapBean.getPointLongitude()));
                    setMarker(clickMapBean, point1);
                }
            } else if (data1.getType().equals(MDataType.MAP_POINT_CLICK_DATA)) {//轮播
                ClickPointBean bean = (ClickPointBean) data1.getData();

            }
        }catch (Exception e){
            Log.e("lili","onProgress =====e="+e.toString());
            e.printStackTrace();
        }
    }

    private void setAllMarker() {
        try {
            mBaiduMap.clear();
            if (llAll.isSelected()) {
                for (ClickMapBean city : clickMapBeens) {
                    pointCityId = city.getRegionId();
                    initPoint();
                }
            } else {
                pointCityId = clickMapBeens.get(0).getRegionId();
                initPoint();
            }
        }catch (Exception e){
            Log.e("lili","setAllMarker =====e="+e.toString());
            e.printStackTrace();
        }

    }

    private void moveMap(double latitude, double longtitude) {
        try {
            LatLng ll = new LatLng(latitude, longtitude);
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
            mBaiduMap.animateMapStatus(update);
            update = MapStatusUpdateFactory.zoomTo(16f);
            mBaiduMap.animateMapStatus(update);

            MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
            locationBuilder.latitude(latitude);
            locationBuilder.longitude(longtitude);
            MyLocationData locationData = locationBuilder.build();
            mBaiduMap.setMyLocationData(locationData);
            //移动到屏幕中间
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(ll));
        }catch (Exception e){
            Log.e("lili","moveMap =====e="+e.toString());
            e.printStackTrace();
        }
    }

    private void setMarker(MapPointBean.PointBean clickMapBean, LatLng latLng) {
        try {
            View markView = View.inflate(getApplicationContext(), R.layout.icon_maker, null);
            TextView tvBorder = (TextView) markView.findViewById(R.id.maker_border_tv);
            TextView tvBg = (TextView) markView.findViewById(R.id.maker_tv);
            tvBg.setBackgroundResource(R.drawable.shape_circle_blue);
            tvBorder.setBackgroundResource(R.drawable.shape_circle_blue);

            GradientDrawable drawable = (GradientDrawable) tvBg.getBackground();
            GradientDrawable drawables = (GradientDrawable) tvBorder.getBackground();

            drawables.setColor(Color.parseColor(clickMapBean.getQAqiColor()));
            drawable.setColor(Color.parseColor(clickMapBean.getAQIColor()));

            tvBg.setText(clickMapBean.getAQI());
            //将View转换为BitmapDescriptor
            BitmapDescriptor descriptor = BitmapDescriptorFactory.fromView(markView);
            OverlayOptions options = new MarkerOptions().position(latLng).icon(descriptor)
                    .zIndex(8).draggable(true);
            moveMap(latLng.latitude, latLng.longitude);

            mBaiduMap.addOverlay(options);
            mBaiduMap.setOnMarkerClickListener(this);
        }catch (Exception e){
            Log.e("lili","setMarker =====e="+e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void onError(String msg, String eCode) {
    }

    /**
     * 地图marker的点击事件
     *
     * @param marker
     * @return
     */
    @Override
    public boolean onMarkerClick(Marker marker) {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_inforwindow, null);
        TextView tv = (TextView) view.findViewById(R.id.marker_city_tv);
        TextView tvPoint = (TextView) view.findViewById(R.id.marker_point_tv);
        TextView tvAQI = (TextView) view.findViewById(R.id.marker_aqi_tv);
        TextView tvPM2_5 = (TextView) view.findViewById(R.id.marker_pm2_5_tv);
        TextView tvPM10 = (TextView) view.findViewById(R.id.marker_pm_10_tv);
        TextView tvNO2 = (TextView) view.findViewById(R.id.marker_no_2_tv);
        TextView tvSO2 = (TextView) view.findViewById(R.id.marker_so_2_tv);
        TextView tvO3 = (TextView) view.findViewById(R.id.marker_o_3_tv);
        TextView tvCO = (TextView) view.findViewById(R.id.marker_co_tv);
        TextView tvFeng = (TextView) view.findViewById(R.id.marker_feng_tv);
        TextView tvFengXiang = (TextView) view.findViewById(R.id.marker_fengxiang_tv);
        TextView tvAirPressure = (TextView) view.findViewById(R.id.marker_air_pressure_tv);
        TextView tvTemperature = (TextView) view.findViewById(R.id.marker_temperature_tv);
        TextView tvHumidity = (TextView) view.findViewById(R.id.marker_humidity_tv);
        double latitude = Double.valueOf(marker.getPosition().latitude);
        double longtitude = Double.valueOf(marker.getPosition().longitude);
        for (MapPointBean.PointBean click : points) {
            if (Double.valueOf(click.getPointLatitude()) == latitude &&
                    Double.valueOf(click.getPointLongitude()) == longtitude) {
                //定义用于显示该InfoWindow的坐标点
                tv.setText(click.getPointName());
                tvPoint.setText(click.getPointType() + "站点");
                tvAQI.setText(click.getAQI());
                tvPM2_5.setText(click.getPM25());
                tvPM10.setText(click.getPM10());
                tvNO2.setText(click.getNo2());
                tvSO2.setText(click.getSo2());
                tvO3.setText(click.getO3());
                tvCO.setText(click.getCo());
                tvFeng.setText(click.getFeng());
                tvFengXiang.setText(click.getFengXiang());
                tvAirPressure.setText(click.getQiYa());
                tvTemperature.setText(click.getQiWen());
                tvHumidity.setText(click.getShiDu());
                LatLng pt = new LatLng(latitude, longtitude);
                //创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
                InfoWindow mInfoWindow = new InfoWindow(view, pt, -50);
                //显示InfoWindows
                mBaiduMap.showInfoWindow(mInfoWindow);
            }
        }
        return false;
    }

    //点击底部城市的回调
    @Override
    public void onItemClick(int position) {
        tvAll.setText("全部");
        llAll.setSelected(false);

        pointCityId = clickMapBeens.get(position).getRegionId();
        cityAdapter.notifyDataSetChanged();
        mBaiduMap.clear();
        initPoint();
    }
}
