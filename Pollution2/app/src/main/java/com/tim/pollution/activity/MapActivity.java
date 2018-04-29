package com.tim.pollution.activity;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.tim.pollution.R;
import com.tim.pollution.general.BaseActivity;

import butterknife.BindView;

/**
 * Created by lenovo on 2018/4/29.
 */

public class MapActivity extends BaseActivity {
    @BindView(R.id.map_view)
    MapView mapView;
    private MyLocationConfiguration.LocationMode mCurrentMode;
    private BaiduMap mBaiduMap;

    @Override
    public int intiLayout() {
        return R.layout.activity_map;
    }

    @Override
    public void initView() {
        //初始化地图
        SDKInitializer.initialize(this.getApplicationContext());
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        mBaiduMap = mapView.getMap();
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(mCurrentMode,
                true, null));
    }

    @Override
    public void initData() {
        
    }
}
