package com.tim.pollution.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.district.DistrictResult;
import com.baidu.mapapi.search.district.DistrictSearch;
import com.baidu.mapapi.search.district.DistrictSearchOption;
import com.baidu.mapapi.search.district.OnGetDistricSearchResultListener;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.tim.pollution.MyApplication;
import com.tim.pollution.R;
import com.tim.pollution.activity.FocusCityActivity;
import com.tim.pollution.activity.MapActivity;
import com.tim.pollution.adapter.MapColorAdapter;
import com.tim.pollution.bean.CityBean;
import com.tim.pollution.bean.LevePollutionBean;
import com.tim.pollution.bean.MapBean;
import com.tim.pollution.bean.MapPointBean;
import com.tim.pollution.bean.changetrend.RegionNetBean;
import com.tim.pollution.callback.ICallBack;
import com.tim.pollution.general.Constants;
import com.tim.pollution.general.LocationData;
import com.tim.pollution.general.MData;
import com.tim.pollution.general.MDataType;
import com.tim.pollution.net.MapDAL;
import com.tim.pollution.utils.LocationUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;


/**
 * Created by lili on 2018/4/20.
 */

public class NewMapFragment extends Fragment implements ICallBack,
        OnGetDistricSearchResultListener, OnGetGeoCoderResultListener {

    @BindView(R.id.new_fg_mTexturemap)
    MapView mapView;
    @BindView(R.id.pm_25)
    LinearLayout llPM2;
    @BindView(R.id.pm_10)
    LinearLayout llPM10;
    @BindView(R.id.so_2)
    LinearLayout llSO2;
    @BindView(R.id.no_2)
    LinearLayout llNO2;
    @BindView(R.id.o_3)
    LinearLayout llO3;
    @BindView(R.id.map_aqi_tv)
    TextView tvAQI;
    @BindView(R.id.pm_25_tv)
    TextView tvPM;
    @BindView(R.id.map_2_5_tv)
    TextView tvPm2_5;
    @BindView(R.id.pm_10_tv)
    TextView tvPM10;
    @BindView(R.id.map_10_tv)
    TextView tv10;
    @BindView(R.id.no_2_tv)
    TextView tvNO;
    @BindView(R.id.map_2_tv)
    TextView tvNO2;
    @BindView(R.id.so_2_tv)
    TextView tvSO;
    @BindView(R.id.map_so_2_tv)
    TextView tvSO2;
    @BindView(R.id.o_3_tv)
    TextView tvO;
    @BindView(R.id.map_3_tv)
    TextView tvO3;
    @BindView(R.id.co_tv)
    TextView tvCo;
    @BindView(R.id.map_swicth_tv)
    TextView tvSwicth;
    @BindView(R.id.new_map_recyview)
    RecyclerView recyclerView;
    @BindView(R.id.map_wind_speed_ll)
    LinearLayout llWindSpeed;
    @BindView(R.id.map_wind_speed_tv)
    TextView tvWindSpeed;
    @BindView(R.id.map_wind_direction_ll)
    LinearLayout llWindDirection;
    @BindView(R.id.map_wind_direction_tv)
    TextView tvWindDirection;
    @BindView(R.id.map_kpa_ll)
    LinearLayout llKPA;
    @BindView(R.id.map_kpa_tv)
    TextView tvKPA;

    @BindView(R.id.map_temperature_ll)
    LinearLayout lltemperature;
    @BindView(R.id.map_temperature_tv)
    TextView tvTemperature;
    @BindView(R.id.map_humidness_ll)
    LinearLayout llHumidness;
    @BindView(R.id.map_humidness_tv)
    TextView tvHumidness;


    private BaiduMap mBaiduMap;
    private MyLocationConfiguration.LocationMode mCurrentMode;
    private LocationClient mLocClient;
    private LocationUtil locationUtil;
    private boolean isFirstLocate = true;
    private Map<String, String> parms;
    private List<CityBean> cityBeens = new ArrayList<>();
    private DistrictSearch mDistrictSearch;
    private DistrictSearchOption districtSearchOption;
    private LinearLayoutManager lm;
    private List<MapPointBean.PointBean> points = new ArrayList<>();
    private MapColorAdapter adapter;
    private List<LevePollutionBean> datas;
    private String colorType = "AQI";
    private MapBean mapBean;
    private GeoCoder mSearch;
    String[] citys = new String[]{
            "太原市",
            "大同市",
            "朔州市",
            "阳泉市",
            "长治市",
            "晋城市",
            "忻州市",
            "吕梁市",
            "晋中市",
            "临汾市",
            "运城市"};
    private ThreadPoolExecutor pool;
    private float zoomTo = 8;
    private String regiontype = "region";
    private String pointCityId = "";//太原
    int count = 0;

    private IntentFilter intentFilter;
/*    private IdReceiver idReceiver;*/
    private List<List<LatLng>> polyLines;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        SDKInitializer.initialize(getActivity().getApplicationContext());
        View view = inflater.inflate(R.layout.fragment_new_map, null);
        ButterKnife.bind(this, view);
        initBrost();

        mapView = (MapView) view.findViewById(R.id.new_fg_mTexturemap);
        mapView.showZoomControls(false);
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        mBaiduMap = mapView.getMap();
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(mCurrentMode,
                true, null));
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);

        locationUtil = new LocationUtil(getActivity(), this);

        lm = new LinearLayoutManager(getActivity());
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(lm);
        datas = new ArrayList<>();
        adapter = new MapColorAdapter(getActivity(), datas);
        recyclerView.setAdapter(adapter);

        // 创建线程池。线程池的"最大池大小"和"核心池大小"都为1，"线程池"的阻塞队列容量为1。
        pool = new ThreadPoolExecutor(50, 50, 0,
                TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(50));

        // 设置线程池的拒绝策略为DiscardOldestPolicy
        pool.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        initAllCityId();
        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus, int i) {

            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {
                zoomTo = mapStatus.zoom;
                if (zoomTo >= 9.0) {
                    mBaiduMap.clear();
                    if (!colorType.equals("FengXiang")) {
                        for (final CityBean cityBean : cityBeens) {
                            //设置坐标点
                            try {
                                LatLng point1 = new LatLng(stringToDouble(cityBean.getPointLatitude()),
                                        stringToDouble(cityBean.getPointLongitude()));
                                setMarker(cityBean, point1);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        setBigJTMarker();
                    }
                } else if (zoomTo <= 8.0) {
                    mBaiduMap.clear();
                    if (!colorType.equals("FengXiang")) {
                        for (final CityBean cityBean : cityBeens) {
                            //设置坐标点
                            try {
                                LatLng point1 = new LatLng(stringToDouble(cityBean.getPointLatitude()),
                                        stringToDouble(cityBean.getPointLongitude()));
                                setSmallMarker(cityBean, point1);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        setJTMarker();
                    }
                }

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {

            }
        });
        return view;
    }

    private void initBrost() {
        /*//动态接受网络变化的广播接收器
        intentFilter = new IntentFilter();
        intentFilter.addAction("city_id");

        idReceiver = new IdReceiver();
        getActivity().registerReceiver(idReceiver, intentFilter);*/
    }

/*    //自定义接受网络变化的广播接收器
    class IdReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            pointCityId = intent.getStringExtra("id");
            Log.e("lili", "pointCityId=" + pointCityId);
            parms.put("key", Constants.key);
            parms.put("regiontype", regiontype);
            parms.put("datatype", "many");
            parms.put("regionid", pointCityId);
            MapDAL.getInstance().getPointData(parms, NewMapFragment.this);
        }
    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1003&&resultCode==RESULT_OK){
            RegionNetBean.RegionBean  regionBean= (RegionNetBean.RegionBean) data.getSerializableExtra("regionBean");
            pointCityId=regionBean.getRegionId();
            Log.e("test","pointCityId:"+pointCityId);
            parms.put("key", Constants.key);
            parms.put("regiontype", regiontype);
            parms.put("datatype", "many");
            parms.put("regionid", pointCityId);
            MapDAL.getInstance().getPointData(parms, NewMapFragment.this);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    private void initAllCityId() {
        if (parms == null) {
            parms = new HashMap<>();
        }
        //获取城市
        parms.put("key", Constants.key);
        parms.put("regiontype", "area");
        MapDAL.getInstance().getCityData(parms, this);
    }

    //
    private void init() {
        if (parms == null) {
            parms = new HashMap<>();
        }

        for (int i = 0; i < cityBeens.size(); i++) {
            pointCityId = cityBeens.get(i).getRegionId();
            parms.put("key", Constants.key);
            parms.put("regiontype", regiontype);
            parms.put("datatype", "many");
            parms.put("regionid", pointCityId);
            MapDAL.getInstance().getPointData(parms, NewMapFragment.this);
            count++;
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
        //在Fragment执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mapView.onDestroy();
        if (mSearch != null) {
            mSearch.destroy();
        }
        if (pool.isShutdown()) {
            pool.shutdown();
        }
       /* getActivity().unregisterReceiver(idReceiver);*/
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0://画点
                    for (final CityBean cityBean : cityBeens) {
                        //设置坐标点
                        try {
                            pool.execute(new Runnable() {
                                @Override
                                public void run() {
                                    LatLng point1 = new LatLng(stringToDouble(cityBean.getPointLatitude()),
                                            stringToDouble(cityBean.getPointLongitude()));
                                    setSmallMarker(cityBean, point1);
                                }
                            });
                        } catch (Exception e) {
                        }
                    }
                    // 关闭线程池
                    pool.shutdown();
                    break;
            }
        }
    };

    private double stringToDouble(String value){
        try{
          return   Double.valueOf(value);
        }catch (Exception e){
            return 0;
        }
    }
    @Override
    public void onSuccess(Object data) {
        MData data1 = (MData) data;
        if (data1.getType().equals(MDataType.MAP_POINT_DATA)) {//获得站点详情
            MapPointBean bean = (MapPointBean) data1.getData();
            points.addAll(bean.getMessages());
            if (count == cityBeens.size()) {//如果他等于相应的城市个数，最后的点位开始画
                handler.sendEmptyMessage(0);
            }
        } else if (data1.getType().equals(MDataType.MAP)) {//定位到当前位置
            BDLocation location = (BDLocation) data1.getData();
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
            locationBuilder.latitude(location.getLatitude());
            locationBuilder.longitude(location.getLongitude());
            MyLocationData locationData = locationBuilder.build();
            mBaiduMap.setMyLocationData(locationData);
            //定义地图状态
            MapStatus mMapStatus = new MapStatus.Builder()
                    //要移动的点
                    .target(ll)
                    //放大地图到20倍
                    .zoom(zoomTo)
                    .build();
            //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
            MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
                    .newMapStatus(mMapStatus);
            //改变地图状态
            mBaiduMap.setMapStatus(mMapStatusUpdate);
        } else if (data1.getType().equals(MDataType.MAP_DATA)) {//获得城市名
            MapBean mapBean = (MapBean) data1.getData();
            this.mapBean = mapBean;
            if (cityBeens.size() > 0) {
                cityBeens.clear();
            }
            cityBeens = mapBean.getMessage().getCityBeens();
            if (datas.size() > 0) {
                datas.clear();
            }
            MyApplication myApplication = (MyApplication) getActivity().getApplication();
            datas.addAll(mapBean.getMessage().getLevePollutionBeens());
            myApplication.setLevePollutionBeans(datas);
            adapter.notifyDataSetChanged();
            init();
        }
    }

    private void setMarker(CityBean clickMapBean, LatLng latLng) {
        try {

            View markView = View.inflate(getActivity(), R.layout.icon_maker, null);
            TextView tvBorder = (TextView) markView.findViewById(R.id.maker_border_tv);
            TextView tvBg = (TextView) markView.findViewById(R.id.maker_tv);
            tvBg.setBackgroundResource(R.drawable.shape_circle_blue);
            tvBorder.setBackgroundResource(R.drawable.shape_circle_blue);
            GradientDrawable drawable = (GradientDrawable) tvBg.getBackground();
            GradientDrawable drawables = (GradientDrawable) tvBorder.getBackground();

            if (colorType.equals("AQI")) {
                drawables.setColor(Color.parseColor(insertStrcolor(clickMapBean.getQAqiColor())));
                drawable.setColor(Color.parseColor(insertStrcolor(clickMapBean.getAQIColor())));
                tvBg.setText(clickMapBean.getAQI());
            } else if (colorType.equals("PM25")) {
                drawables.setColor(Color.parseColor("#000000"));
                drawable.setColor(Color.parseColor(insertStrcolor(clickMapBean.getPM25Color())));
                tvBg.setText(clickMapBean.getPM25());
            } else if (colorType.equals("PM10")) {
                drawables.setColor(Color.parseColor("#000000"));
                drawable.setColor(Color.parseColor(insertStrcolor(clickMapBean.getPM10Color())));
                tvBg.setText(clickMapBean.getPM10());
            } else if (colorType.equals("NO2")) {
                drawables.setColor(Color.parseColor("#000000"));
                drawable.setColor(Color.parseColor(insertStrcolor(clickMapBean.getNO2Color())));
                tvBg.setText(clickMapBean.getNO2());
            } else if (colorType.equals("SO2")) {
                drawables.setColor(Color.parseColor("#000000"));
                drawable.setColor(Color.parseColor(insertStrcolor(clickMapBean.getSO2Color())));
                tvBg.setText(clickMapBean.getSO2());
            } else if (colorType.equals("O3")) {
                drawables.setColor(Color.parseColor("#000000"));
                drawable.setColor(Color.parseColor(insertStrcolor(clickMapBean.getO3Color())));
                tvBg.setText(clickMapBean.getO3());
            } else if (colorType.equals("CO")) {
                drawables.setColor(Color.parseColor("#000000"));
                drawable.setColor(Color.parseColor(insertStrcolor(clickMapBean.getCOColor())));
                tvBg.setText(clickMapBean.getCO());
            } else if (colorType.equals("Feng")) {
                drawables.setColor(Color.parseColor("#000000"));
                drawable.setColor(Color.parseColor(insertStrcolor(clickMapBean.getFengcolor())));
                tvBg.setText(clickMapBean.getFeng());
            } else if (colorType.equals("QiYa")) {
                drawables.setColor(Color.parseColor("#000000"));
                drawable.setColor(Color.parseColor(insertStrcolor(clickMapBean.getQiYacolor())));
                tvBg.setText(clickMapBean.getQiYa());
            } else if (colorType.equals("QiWen")) {
                drawables.setColor(Color.parseColor("#000000"));
                drawable.setColor(Color.parseColor(insertStrcolor(clickMapBean.getQiWencolor())));
                tvBg.setText(clickMapBean.getQiWen());
            } else if (colorType.equals("ShiDu")) {
                drawables.setColor(Color.parseColor("#000000"));
                drawable.setColor(Color.parseColor(insertStrcolor(clickMapBean.getShiDucolor())));
                tvBg.setText(clickMapBean.getShiDu());
            }
            //将View转换为BitmapDescriptor
            BitmapDescriptor descriptor = BitmapDescriptorFactory.fromView(markView);
            OverlayOptions options = new MarkerOptions().position(latLng).icon(descriptor)
                    .zIndex(7).draggable(true);
//            moveMap(latLng.latitude, latLng.longitude);
            mBaiduMap.addOverlay(options);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setSmallMarker(CityBean clickMapBean, LatLng latLng) {
        try {

            View markView = View.inflate(getActivity(), R.layout.icon_small_maker, null);
            TextView tvBorder = (TextView) markView.findViewById(R.id.maker_border_tv);
            TextView tvBg = (TextView) markView.findViewById(R.id.maker_tv);
            tvBg.setBackgroundResource(R.drawable.shape_circle_blue);
            tvBorder.setBackgroundResource(R.drawable.shape_circle_blue);
            GradientDrawable drawable = (GradientDrawable) tvBg.getBackground();
            GradientDrawable drawables = (GradientDrawable) tvBorder.getBackground();

            if (colorType.equals("AQI")) {
                drawables.setColor(Color.parseColor(insertStrcolor(clickMapBean.getQAqiColor())));
                drawable.setColor(Color.parseColor(insertStrcolor(clickMapBean.getAQIColor())));
            } else if (colorType.equals("PM25")) {
                drawables.setColor(Color.parseColor("#000000"));
                drawable.setColor(Color.parseColor(insertStrcolor(clickMapBean.getPM25Color())));
            } else if (colorType.equals("PM10")) {
                drawables.setColor(Color.parseColor("#000000"));
                drawable.setColor(Color.parseColor(insertStrcolor(clickMapBean.getPM10Color())));
            } else if (colorType.equals("NO2")) {
                drawables.setColor(Color.parseColor("#000000"));
                drawable.setColor(Color.parseColor(insertStrcolor(clickMapBean.getNO2Color())));
            } else if (colorType.equals("SO2")) {
                drawables.setColor(Color.parseColor("#000000"));
                drawable.setColor(Color.parseColor(insertStrcolor(clickMapBean.getSO2Color())));
            } else if (colorType.equals("O3")) {
                drawables.setColor(Color.parseColor("#000000"));
                drawable.setColor(Color.parseColor(insertStrcolor(clickMapBean.getO3Color())));
            } else if (colorType.equals("CO")) {
                drawables.setColor(Color.parseColor("#000000"));
                drawable.setColor(Color.parseColor(insertStrcolor(clickMapBean.getCOColor())));
            } else if (colorType.equals("Feng")) {
                drawables.setColor(Color.parseColor("#000000"));
                drawable.setColor(Color.parseColor(insertStrcolor(clickMapBean.getFengcolor())));
            } else if (colorType.equals("QiYa")) {
                drawables.setColor(Color.parseColor("#000000"));
                drawable.setColor(Color.parseColor(insertStrcolor(clickMapBean.getQiYacolor())));
            } else if (colorType.equals("QiWen")) {
                drawables.setColor(Color.parseColor("#000000"));
                drawable.setColor(Color.parseColor(insertStrcolor(clickMapBean.getQiWencolor())));
            } else if (colorType.equals("ShiDu")) {
                drawables.setColor(Color.parseColor("#000000"));
                drawable.setColor(Color.parseColor(insertStrcolor(clickMapBean.getShiDucolor())));
            }
            //将View转换为BitmapDescriptor
            BitmapDescriptor descriptor = BitmapDescriptorFactory.fromView(markView);
            OverlayOptions options = new MarkerOptions().position(latLng).icon(descriptor)
                    .zIndex(4).draggable(true);
//            moveMap(latLng.latitude, latLng.longitude);
            mBaiduMap.addOverlay(options);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setJTMarker() {
        try {
            for (final CityBean cityBean : cityBeens) {
                //定义Maker坐标点
                View markView = View.inflate(getActivity(), R.layout.icon_jianton, null);
                LatLng point = new LatLng(stringToDouble(cityBean.getPointLatitude()),
                        stringToDouble(cityBean.getPointLongitude()));
                //构建Marker图标
                //将View转换为BitmapDescriptor
                BitmapDescriptor descriptor = BitmapDescriptorFactory.fromView(markView);
                //构建MarkerOption，用于在地图上添加Marker
                OverlayOptions option = new MarkerOptions()
                        .position(point)
                        .rotate(Float.valueOf(cityBean.getFengXiang()))
                        .icon(descriptor);
                mBaiduMap.addOverlay(option);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setBigJTMarker() {
        try {
            for (final CityBean cityBean : cityBeens) {
                //定义Maker坐标点
                View markView = View.inflate(getActivity(), R.layout.icon_big_jiantou, null);
                LatLng point = new LatLng(stringToDouble(cityBean.getPointLatitude()),
                        stringToDouble(cityBean.getPointLongitude()));
                //构建Marker图标
                //将View转换为BitmapDescriptor
                BitmapDescriptor descriptor = BitmapDescriptorFactory.fromView(markView);
                //构建MarkerOption，用于在地图上添加Marker
                OverlayOptions option = new MarkerOptions()
                        .position(point)
                        .rotate(Float.valueOf(cityBean.getFengXiang()))
                        .icon(descriptor);
                mBaiduMap.addOverlay(option);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(String msg, String eCode) {

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            for (int i = 0; i < citys.length; i++) {
                mDistrictSearch = DistrictSearch.newInstance();
                mDistrictSearch.setOnDistrictSearchListener(NewMapFragment.this);
                mDistrictSearch.searchDistrict(new DistrictSearchOption().cityName(citys[i]));
            }
        }
    }

    private void setColorMarker() {
        mBaiduMap.clear();

        for (final CityBean clickMapBean : cityBeens) {
            //设置坐标点
            LatLng point1 = new LatLng(stringToDouble(clickMapBean.getPointLatitude()),
                    stringToDouble(clickMapBean.getPointLongitude()));
            if (zoomTo >= 9.0) {
                setMarker(clickMapBean, point1);
            } else if (zoomTo <= 8.0) {
                setSmallMarker(clickMapBean, point1);
            }
        }
    }

    //画行政区域
    @Override
    public void onGetDistrictResult(DistrictResult districtResult) {
        districtResult.getCenterPt();//获取行政区中心坐标点
        districtResult.getCityName();//获取行政区域名称
        polyLines = districtResult.getPolylines();
        //获取行政区域边界坐标点 //边界就是坐标点的集合，在地图上画出来就是多边形图层。
        // 有的行政区可能有多个区域，所以会有多个点集合。
        if (polyLines == null) return;
        //地理边界对象
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (List<LatLng> polyline : polyLines) {
                    OverlayOptions ooPolygon = new PolylineOptions().width(5)
                            .points(polyline).dottedLine(true).color(0xAA00FF88);
                    mBaiduMap.addOverlay(ooPolygon);
                }
            }
        }).start();

    }

    private String insertStr(String colors) throws ClassCastException {
        StringBuilder sb = new StringBuilder(colors);
        sb.insert(1, "90");
        return sb.toString();
    }

    private String insertStrcolor(String colors) throws ClassCastException {
        return colors;
    }

    @OnClick({R.id.no_2, R.id.map_aqi_tv, R.id.pm_25, R.id.pm_10, R.id.so_2,
            R.id.o_3, R.id.co_tv, R.id.map_swicth_tv,
            R.id.map_wind_speed_tv, R.id.map_wind_direction_tv, R.id.map_kpa_tv,
            R.id.map_temperature_tv, R.id.map_humidness_tv})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.map_humidness_tv://湿度
                clearTv();
                colorType = "ShiDu";
                tvHumidness.setTextColor(getResources().getColor(R.color.color_white));
                setColorMarker();
                break;
            case R.id.map_temperature_tv://温度
                clearTv();
                colorType = "QiWen";
                tvTemperature.setTextColor(getResources().getColor(R.color.color_white));
                setColorMarker();
                break;
            case R.id.map_kpa_tv://气压
                clearTv();
                colorType = "QiYa";
                tvKPA.setTextColor(getResources().getColor(R.color.color_white));
                setColorMarker();
                break;
            case R.id.map_wind_direction_tv://风向
                clearTv();
                colorType = "FengXiang";
                mBaiduMap.clear();
                tvWindDirection.setTextColor(getResources().getColor(R.color.color_white));

                if (zoomTo >= 9.0) {
                    setBigJTMarker();
                } else {
                    setJTMarker();
                }
                break;
            case R.id.map_wind_speed_tv://风速
                clearTv();
                colorType = "Feng";
                tvWindSpeed.setTextColor(getResources().getColor(R.color.color_white));
                setColorMarker();
                break;
            case R.id.map_aqi_tv:
                clearTv();
                tvAQI.setTextColor(getResources().getColor(R.color.color_white));
                colorType = "AQI";
                setColorMarker();
                break;
            case R.id.pm_25:
                clearTv();
                tvPM.setTextColor(getResources().getColor(R.color.color_white));
                tvPm2_5.setTextColor(getResources().getColor(R.color.color_white));
                colorType = "PM25";
                setColorMarker();
                break;
            case R.id.pm_10:
                clearTv();
                tvPM10.setTextColor(getResources().getColor(R.color.color_white));
                tv10.setTextColor(getResources().getColor(R.color.color_white));
                colorType = "PM10";
                setColorMarker();
                break;
            case R.id.no_2:
                clearTv();
                tvNO2.setTextColor(getResources().getColor(R.color.color_white));
                tvNO.setTextColor(getResources().getColor(R.color.color_white));
                colorType = "NO2";
                setColorMarker();
                break;
            case R.id.so_2:
                clearTv();
                tvSO2.setTextColor(getResources().getColor(R.color.color_white));
                tvSO.setTextColor(getResources().getColor(R.color.color_white));
                colorType = "SO2";
                setColorMarker();
                break;
            case R.id.o_3:
                clearTv();
                tvO3.setTextColor(getResources().getColor(R.color.color_white));
                tvO.setTextColor(getResources().getColor(R.color.color_white));
                colorType = "O3";
                setColorMarker();
                break;
            case R.id.co_tv:
                clearTv();
                tvCo.setTextColor(getResources().getColor(R.color.color_white));
                colorType = "CO";
                setColorMarker();
                break;
            case R.id.map_swicth_tv:
              /*  Intent intent = new Intent(getActivity(), CityActivity.class);
                startActivity(intent);*/
              Intent intent=new Intent(getActivity(), FocusCityActivity.class);
              intent.putExtra("state","02");
              startActivityForResult(intent,1003);
              break;
        }
    }

    private void clearTv() {
        tvAQI.setTextColor(getResources().getColor(R.color.gree_blue));
        tvCo.setTextColor(getResources().getColor(R.color.gree_blue));
        tvPM.setTextColor(getResources().getColor(R.color.gree_blue));
        tvPm2_5.setTextColor(getResources().getColor(R.color.gree_blue));
        tvPM10.setTextColor(getResources().getColor(R.color.gree_blue));
        tv10.setTextColor(getResources().getColor(R.color.gree_blue));
        tvNO.setTextColor(getResources().getColor(R.color.gree_blue));
        tvNO2.setTextColor(getResources().getColor(R.color.gree_blue));
        tvSO.setTextColor(getResources().getColor(R.color.gree_blue));
        tvSO2.setTextColor(getResources().getColor(R.color.gree_blue));
        tvO.setTextColor(getResources().getColor(R.color.gree_blue));
        tvO3.setTextColor(getResources().getColor(R.color.gree_blue));

        tvHumidness.setTextColor(getResources().getColor(R.color.gree_blue));
        tvKPA.setTextColor(getResources().getColor(R.color.gree_blue));
        tvTemperature.setTextColor(getResources().getColor(R.color.gree_blue));
        tvWindDirection.setTextColor(getResources().getColor(R.color.gree_blue));
        tvWindSpeed.setTextColor(getResources().getColor(R.color.gree_blue));
    }

    /**
     * 实现经纬度逆编码
     *
     * @param result
     */
    @Override
    public void onGetGeoCodeResult(GeoCodeResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            //没有检索到结果
        }
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            //没有找到检索结果
        }
        for (CityBean cityBean : cityBeens) {
            if (result.getAddress().contains(cityBean.getRegionName())) {
                //获取到城市的ID；
                if (mapBean != null) {
                    LocationData data = new LocationData();
                    data.setLatLng(result.getLocation());

                    Intent intent = new Intent(getActivity(), MapActivity.class);
                    intent.putExtra("id", cityBean.getRegionId());
                    startActivity(intent);
                }
            }
        }
    }

}
