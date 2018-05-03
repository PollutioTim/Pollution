package com.tim.pollution.activity;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
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
import com.tim.pollution.bean.MapBean;
import com.tim.pollution.bean.MapPointBean;
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
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static android.R.attr.data;
import static com.baidu.location.d.j.v;
import static com.tim.pollution.R.mipmap.location;

/**
 * Created by lenovo on 2018/4/29.
 */

public class MapActivity extends BaseActivity implements ICallBack,BaiduMap.OnMarkerClickListener {
    @BindView(R.id.map_view)
    MapView mapView;
    @BindView(R.id.map_ac_recyview)
    RecyclerView recyclerView;
    @BindView(R.id.map_city_recyclerview)
    RecyclerView recyCity;
    @BindView(R.id.map_ac_city_tv)
    TextView tvCityName;
    private MyLocationConfiguration.LocationMode mCurrentMode;
    private BaiduMap mBaiduMap;
    private boolean isFirstLocate = true;
    private LinearLayoutManager lm;
    private ArrayList<LevePollutionBean> datas;
    private List<ClickMapBean>clickMapBeens;
    private List<MapPointBean.PointBean>points;
    private MapColorAdapter adapter;
    private Map<String,String> parms;
    private ClickMapListBean mapBean;
    private String regionid="";
    private String regiontype = "region";
    private GridLayoutManager lms;
    private MapCityAdapter cityAdapter;
    private boolean isFirst = true;

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
        MyApplication myApplication = (MyApplication) getApplication();
        datas = (ArrayList<LevePollutionBean>) myApplication.getLevePollutionBeans();
        adapter = new MapColorAdapter(this, datas);
        recyclerView.setAdapter(adapter);

        lms = new GridLayoutManager(this,5);
        recyCity.setLayoutManager(lms);
        clickMapBeens = new ArrayList<>();
        cityAdapter = new MapCityAdapter(this, clickMapBeens);
        recyCity.setAdapter(cityAdapter);
    }

    @Override
    public void initData() {
        EventBus.getDefault().register(this);
        regionid = getIntent().getStringExtra("id");
        //获取数据
        points = new ArrayList<>();
        init();
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
            update = MapStatusUpdateFactory.zoomTo(12f);
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
        parms.put("regionid", regionid);
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
     * @param data
     */
    @Override
    public void onProgress(Object data) {
        MData data1 = (MData) data;
        if (data1.getType().equals(MDataType.MAP_CLICK_DATA)) {
            ClickMapListBean mapBean = (ClickMapListBean) data1.getData();
            this.mapBean = mapBean;
            clickMapBeens .addAll(mapBean.getMessage());
            cityAdapter.notifyDataSetChanged();
            if(isFirst){
                isFirst = false;
                tvCityName.setText(clickMapBeens.get(0).getRegionName());
                regiontype = "point";
                regionid =clickMapBeens.get(0).getRegionId();
                initPoint();
            }
        }else if(data1.getType().equals(MDataType.MAP_POINT_DATA)){
            MapPointBean bean = (MapPointBean) data1.getData();
            points.addAll(bean.getMessages());
            for(MapPointBean.PointBean clickMapBean :points){
                //设置坐标点
                LatLng point1 = new LatLng(Double.valueOf(clickMapBean.getPointLatitude()),
                        Double.valueOf(clickMapBean.getPointLongitude()));
                setMarker(clickMapBean,point1);
            }
        }else if(data1.getType().equals(MDataType.MAP_POINT_CLICK_DATA)) {
            ClickPointBean bean = (ClickPointBean) data1.getData();

        }
    }

    private void setMarker(MapPointBean.PointBean clickMapBean, LatLng latLng) {
        View markView = View.inflate(getApplicationContext(), R.layout.icon_maker, null);
        TextView tvBg = (TextView) markView.findViewById(R.id.maker_tv);
        tvBg.setBackgroundResource(R.drawable.shape_circle_blue);
        GradientDrawable drawable =(GradientDrawable)tvBg.getBackground();
        drawable.setColor(Color.parseColor(clickMapBean.getAQIColor()));
        tvBg.setText(clickMapBean.getAQI());
        //将View转换为BitmapDescriptor
        BitmapDescriptor descriptor =  BitmapDescriptorFactory.fromView(markView);
        OverlayOptions options = new MarkerOptions().position(latLng).icon(descriptor)
                .zIndex(8).draggable(true);
        mBaiduMap.addOverlay(options);
    }

    @Override
    public void onError(String msg, String eCode) {

    }

    /**
     * 地图marker的点击事件
     * @param marker
     * @return
     */
    @Override
    public boolean onMarkerClick(Marker marker) {

        return false;
    }
}
