package com.tim.pollution.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.tim.pollution.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lenovo on 2018/4/20.
 */

public class MapFragment extends Fragment {

    @Bind(R.id.fg_mTexturemap)
    MapView mapView;
    @Bind(R.id.pm_2)
    LinearLayout llPM2;
    @Bind(R.id.pm_10)
    LinearLayout llPM10;
    @Bind(R.id.so_2)
    LinearLayout llSO2;
    @Bind(R.id.o_3)
    LinearLayout llO3;
    @Bind(R.id.co_tv)
    TextView tvCo;
    @Bind(R.id.map_swicth_tv)
    TextView tvSwicth;
    @Bind(R.id.map_time_tv)
    TextView tvTime;
    private BaiduMap mBaiduMap;
    private MyLocationConfiguration.LocationMode mCurrentMode;
    private LocationClient mLocClient;

    public static MapFragment newInstance(String param1) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        SDKInitializer.initialize(getActivity().getApplicationContext());
        View view = inflater.inflate(R.layout.fragment_map,null);
        ButterKnife.bind(this, view);
        mapView = (MapView) view.findViewById(R.id.fg_mTexturemap);
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        mBaiduMap = mapView.getMap();
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(mCurrentMode,
                true, null));
// 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
// 定位初始化
        mLocClient = new LocationClient(getActivity());

        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
    }

}
