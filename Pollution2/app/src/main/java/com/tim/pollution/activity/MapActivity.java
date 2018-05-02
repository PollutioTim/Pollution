package com.tim.pollution.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.tim.pollution.R;
import com.tim.pollution.adapter.MapColorAdapter;
import com.tim.pollution.bean.LevePollutionBean;
import com.tim.pollution.bean.MapBean;
import com.tim.pollution.callback.ICallBack;
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
import java.util.Map;

import butterknife.BindView;

import static com.tim.pollution.R.mipmap.location;

/**
 * Created by lenovo on 2018/4/29.
 */

public class MapActivity extends BaseActivity implements ICallBack{
    @BindView(R.id.map_view)
    MapView mapView;
    @BindView(R.id.map_ac_recyview)
    RecyclerView recyclerView;
    private MyLocationConfiguration.LocationMode mCurrentMode;
    private BaiduMap mBaiduMap;
    private boolean isFirstLocate = true;
    private LinearLayoutManager lm;
    private ArrayList<LevePollutionBean> datas;
    private MapColorAdapter adapter;
    private Map<String,String> parms;
    private MapBean mapBean;

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
        lm = new LinearLayoutManager(this);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(lm);
        datas = new ArrayList<>();
        adapter = new MapColorAdapter(this, datas);
        recyclerView.setAdapter(adapter);
        // 注册订阅者
        EventBus.getDefault().register(MapActivity.this);
    }

    @Override
    public void initData() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true) //在ui线程执行
    public void onMessageEvent(MessageEvent event) {
        LocationData data = event.getMessage();
        LatLng latLng = data.getLatLng();
        // 更新界面
        if (isFirstLocate) {
            LatLng ll = new LatLng(latLng.latitude, latLng.longitude);
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
            mBaiduMap.animateMapStatus(update);
            update = MapStatusUpdateFactory.zoomTo(16f);
            mBaiduMap.animateMapStatus(update);
            /*判断baiduMap是已经移动到指定位置*/
            if (mBaiduMap.getLocationData() != null)
                if (mBaiduMap.getLocationData().latitude == latLng.latitude
                        && mBaiduMap.getLocationData().longitude == latLng.longitude) {
                    isFirstLocate = false;
                }
        }
        MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
        locationBuilder.latitude(latLng.latitude);
        locationBuilder.longitude(latLng.longitude);
        MyLocationData locationData = locationBuilder.build();
        mBaiduMap.setMyLocationData(locationData);
        setMarker(latLng);
    }

    private void setMarker(LatLng latLng) {
        View markView = View.inflate(getApplicationContext(), R.layout.icon_maker, null);
        //将View转换为BitmapDescriptor
        BitmapDescriptor descriptor =  BitmapDescriptorFactory.fromView(markView);
        OverlayOptions options = new MarkerOptions().position(latLng).icon(descriptor)
                .zIndex(9).draggable(true);
        mBaiduMap.addOverlay(options);
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

    private void init() {//获得站点
        if (parms == null) {
            parms = new HashMap<>();
        }
        parms.put("key", Constants.key);
        parms.put("regiontype", "region");
        parms.put("datatype", "many");
        parms.put("regionid", "region");
        MapDAL.getInstance().getCityData(parms, this);
    }

    @Override
    public void onProgress(Object data) {
        MData data1 = (MData) data;
        if (data1.getType().equals(MDataType.MAP_DATA)) {
            MapBean mapBean = (MapBean) data1.getData();
            this.mapBean = mapBean;
            if (datas.size() > 0) {
                datas.clear();
            }
            datas.addAll(mapBean.getMessage().getLevePollutionBeens());
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onError(String msg, String eCode) {

    }
}
