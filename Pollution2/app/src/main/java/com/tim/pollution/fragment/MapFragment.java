package com.tim.pollution.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.cloud.CloudListener;
import com.baidu.mapapi.cloud.CloudManager;
import com.baidu.mapapi.cloud.CloudRgcInfo;
import com.baidu.mapapi.cloud.CloudRgcResult;
import com.baidu.mapapi.cloud.CloudSearchResult;
import com.baidu.mapapi.cloud.DetailSearchResult;
import com.baidu.mapapi.http.HttpClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.Stroke;
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
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.utils.poi.DispathcPoiData;
import com.tim.pollution.MyApplication;
import com.tim.pollution.R;
import com.tim.pollution.activity.CityActivity;
import com.tim.pollution.activity.FocusCityActivity;
import com.tim.pollution.activity.MapActivity;
import com.tim.pollution.adapter.MapColorAdapter;
import com.tim.pollution.bean.CityBean;
import com.tim.pollution.bean.LevePollutionBean;
import com.tim.pollution.bean.MapBean;
import com.tim.pollution.callback.ICallBack;
import com.tim.pollution.general.Constants;
import com.tim.pollution.general.LocationData;
import com.tim.pollution.general.MData;
import com.tim.pollution.general.MDataType;
import com.tim.pollution.general.MessageEvent;
import com.tim.pollution.net.MapDAL;
import com.tim.pollution.utils.ConstUtils;
import com.tim.pollution.utils.DateUtil;
import com.tim.pollution.utils.LocationUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.R.attr.data;


/**
 * Created by lili on 2018/4/20.
 */

public class MapFragment extends Fragment implements ICallBack,
        OnGetDistricSearchResultListener, OnMapClickListener, OnGetGeoCoderResultListener {

    @BindView(R.id.fg_mTexturemap)
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
    @BindView(R.id.map_time_tv)
    TextView tvTime;
    @BindView(R.id.map_recyview)
    RecyclerView recyclerView;

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

    private MapColorAdapter adapter;
    private List<LevePollutionBean> datas;
    private String colorType = "AQI";
    private MapBean mapBean;
    private GeoCoder mSearch;
    private String strTime = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        SDKInitializer.initialize(getActivity().getApplicationContext());
        View view = inflater.inflate(R.layout.fragment_map, null);
        ButterKnife.bind(this, view);
        mapView = (MapView) view.findViewById(R.id.fg_mTexturemap);
        mapView.showZoomControls(false);
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        mBaiduMap = mapView.getMap();
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(mCurrentMode,
                true, null));
        locationUtil = new LocationUtil(getActivity(), this);
        mBaiduMap.setOnMapClickListener(this);
        lm = new LinearLayoutManager(getActivity());
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(lm);
        datas = new ArrayList<>();
        adapter = new MapColorAdapter(getActivity(), datas);
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void init() {
        if (parms == null) {
            parms = new HashMap<>();
        }
        parms.put("key", Constants.key);
        parms.put("regiontype", "area");
        MapDAL.getInstance().getCityData(parms, this);
    }

    private String switchTime(String time) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            return sdf.format(DateUtil.strToDateLong(time));
        } catch (Exception e) {
            return time;
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
    }

    @Override
    public void onSuccess(Object data) {
        MData data1 = (MData) data;
        if (data1.getType().equals(MDataType.MAP_DATA)) {//获得城市名
            MapBean mapBean = (MapBean) data1.getData();
            this.mapBean = mapBean;
            if (cityBeens.size() > 0) {
                cityBeens.clear();
            }
            cityBeens = mapBean.getMessage().getCityBeens();
            strTime = switchTime(mapBean.getMessage().getTime());
            tvTime.setText(switchTime(mapBean.getMessage().getTime()));
            if (datas.size() > 0) {
                datas.clear();
            }
            MyApplication myApplication = (MyApplication) getActivity().getApplication();
            datas.addAll(mapBean.getMessage().getLevePollutionBeens());
            myApplication.setLevePollutionBeans(datas);
            adapter.notifyDataSetChanged();
            getArea(cityBeens);
        } else if (data1.getType().equals(MDataType.MAP)) {
            BDLocation location = (BDLocation) data1.getData();
//            if (isFirstLocate) {
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
                    .zoom(9)
                    .build();
            //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
            MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
            //改变地图状态
            mBaiduMap.setMapStatus(mMapStatusUpdate);
            init();
        }
    }

    @Override
    public void onError(String msg, String eCode) {

    }

    private void getArea(List<CityBean> cityBeens) {
        for (int i = 0; i < cityBeens.size(); i++) {
            if (mDistrictSearch != null || districtSearchOption != null) {
                mDistrictSearch = null;
                districtSearchOption = null;
            }
            mDistrictSearch = DistrictSearch.newInstance();
            //初始化行政区检索
            mDistrictSearch.setOnDistrictSearchListener(this);//设置回调监听
            districtSearchOption = new DistrictSearchOption();
            districtSearchOption.cityName(cityBeens.get(i).getRegionName());//检索城市名称
            mDistrictSearch.searchDistrict(districtSearchOption);//请求行政区数据
        }
    }

    //画行政区域
    @Override
    public void onGetDistrictResult(DistrictResult districtResult) {
        districtResult.getCenterPt();//获取行政区中心坐标点
        districtResult.getCityName();//获取行政区域名称
        List<List<LatLng>> polyLines = districtResult.getPolylines();
        //获取行政区域边界坐标点 //边界就是坐标点的集合，在地图上画出来就是多边形图层。
        // 有的行政区可能有多个区域，所以会有多个点集合。
        if (polyLines == null) return;
        //地理边界对象
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (CityBean cityBean : cityBeens) {
            if (districtResult.getCityName().contains(cityBean.getRegionName())) {
                for (List<LatLng> polyline : polyLines) {
                    setColor(polyline, cityBean);
                    for (LatLng latLng : polyline) {
                        builder.include(latLng);//包含这些点
                    }

                }
            }
        }

    }

    private void setColor(List<LatLng> polyline, CityBean cityBean) {
        try {
            OverlayOptions ooPolyline11 = new PolylineOptions().width(10)
                    .points(polyline).dottedLine(true).color(Color.RED);
            mBaiduMap.addOverlay(ooPolyline11);//添加OverLay
            OverlayOptions ooPolygon = null;
            if (colorType.equals("AQI")) {
                ooPolygon = new PolygonOptions().points(polyline).
                        fillColor(Color.parseColor(insertStr(cityBean.getAQIColor())));
            } else if (colorType.equals("PM25")) {
                ooPolygon = new PolygonOptions().points(polyline).
                        fillColor(Color.parseColor(insertStr(cityBean.getPM25Color())));
            } else if (colorType.equals("PM10")) {
                ooPolygon = new PolygonOptions().points(polyline).
                        fillColor(Color.parseColor(insertStr(cityBean.getPM10Color())));
            } else if (colorType.equals("NO2")) {
                ooPolygon = new PolygonOptions().points(polyline).
                        fillColor(Color.parseColor(insertStr(cityBean.getNO2Color())));
            } else if (colorType.equals("SO2")) {
                ooPolygon = new PolygonOptions().points(polyline).
                        fillColor(Color.parseColor(insertStr(cityBean.getSO2Color())));
            } else if (colorType.equals("O3")) {
                ooPolygon = new PolygonOptions().points(polyline).
                        fillColor(Color.parseColor(insertStr(cityBean.getO3Color())));
            } else if (colorType.equals("CO")) {
                ooPolygon = new PolygonOptions().points(polyline).
                        fillColor(Color.parseColor(insertStr(cityBean.getCOColor())));
            }
            mBaiduMap.addOverlay(ooPolygon);//添加OverLay
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private String insertStr(String colors) throws ClassCastException{
        StringBuilder  sb = new StringBuilder (colors);
        sb.insert(1, "90");
        return sb.toString();
    }

    @OnClick({R.id.no_2, R.id.map_aqi_tv, R.id.pm_25, R.id.pm_10, R.id.so_2,
            R.id.o_3, R.id.co_tv, R.id.map_swicth_tv})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.map_aqi_tv:
                clearTv();
                tvAQI.setTextColor(getResources().getColor(R.color.color_white));
                colorType = "AQI";
                getArea(cityBeens);
                break;
            case R.id.pm_25:
                clearTv();
                tvPM.setTextColor(getResources().getColor(R.color.color_white));
                tvPm2_5.setTextColor(getResources().getColor(R.color.color_white));
                colorType = "PM25";
                getArea(cityBeens);
                break;
            case R.id.pm_10:
                clearTv();
                tvPM10.setTextColor(getResources().getColor(R.color.color_white));
                tv10.setTextColor(getResources().getColor(R.color.color_white));
                colorType = "PM10";
                getArea(cityBeens);
                break;
            case R.id.no_2:
                clearTv();
                tvNO2.setTextColor(getResources().getColor(R.color.color_white));
                tvNO.setTextColor(getResources().getColor(R.color.color_white));
                colorType = "NO2";
                getArea(cityBeens);
                break;
            case R.id.so_2:
                clearTv();
                tvSO2.setTextColor(getResources().getColor(R.color.color_white));
                tvSO.setTextColor(getResources().getColor(R.color.color_white));
                colorType = "SO2";
                getArea(cityBeens);
                break;
            case R.id.o_3:
                clearTv();
                tvO3.setTextColor(getResources().getColor(R.color.color_white));
                tvO.setTextColor(getResources().getColor(R.color.color_white));
                colorType = "O3";
                getArea(cityBeens);
                break;
            case R.id.co_tv:
                clearTv();
                tvCo.setTextColor(getResources().getColor(R.color.color_white));
                colorType = "CO";
                getArea(cityBeens);
                break;
            case R.id.map_swicth_tv:
                Intent intent = new Intent(getActivity(), FocusCityActivity.class);
                intent.putExtra("tag", 1);
                startActivity(intent);
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
    }

    @Override
    public void onMapClick(LatLng latLng) {
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);
        mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                .location(latLng));

//        // 发布事件
//
    }

    /**
     * 地图内 Poi 单击事件回调函数
     *
     * @param mapPoi 点击的 poi 信息
     */
    @Override
    public boolean onMapPoiClick(MapPoi mapPoi) {
        return false;
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

        //获取地理编码结果
        Log.e("lili", "GeoCodeResult=" + result);
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
                    intent.putExtra("id",cityBean.getRegionId());
                    intent.putExtra("time",strTime);
                    startActivity(intent);
                }
            }
        }
        Log.e("lili", "ReverseGeoCodeResult=" + result.getAddress() + "-" + result.getLocation() + "-" + result.getCityCode());
    }
}
