package com.tim.pollution.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.baidu.mapapi.http.HttpClient;
import com.baidu.mapapi.map.BaiduMap;
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
import com.baidu.mapapi.search.district.DistrictResult;
import com.baidu.mapapi.search.district.DistrictSearch;
import com.baidu.mapapi.search.district.DistrictSearchOption;
import com.baidu.mapapi.search.district.OnGetDistricSearchResultListener;
import com.baidu.mapapi.utils.poi.DispathcPoiData;
import com.tim.pollution.R;
import com.tim.pollution.bean.CityBean;
import com.tim.pollution.bean.MapBean;
import com.tim.pollution.callback.ICallBack;
import com.tim.pollution.general.MData;
import com.tim.pollution.general.MDataType;
import com.tim.pollution.net.MapDAL;
import com.tim.pollution.utils.LocationUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.baidu.location.d.a.i;
import static com.baidu.location.d.j.I;

/**
 * Created by lenovo on 2018/4/20.
 */

public class MapFragment extends Fragment implements ICallBack,
        OnGetDistricSearchResultListener {

    @BindView(R.id.fg_mTexturemap)
    MapView mapView;
    @BindView(R.id.pm_2)
    LinearLayout llPM2;
    @BindView(R.id.pm_10)
    LinearLayout llPM10;
    @BindView(R.id.so_2)
    LinearLayout llSO2;
    @BindView(R.id.o_3)
    LinearLayout llO3;
    @BindView(R.id.co_tv)
    TextView tvCo;
    @BindView(R.id.map_swicth_tv)
    TextView tvSwicth;
    @BindView(R.id.map_time_tv)
    TextView tvTime;
    private BaiduMap mBaiduMap;
    private MyLocationConfiguration.LocationMode mCurrentMode;
    private LocationClient mLocClient;
    private LocationUtil locationUtil;
    private boolean isFirstLocate = true;
    private Map<String, String> parms;
    private List<CityBean> cityBeens = new LinkedList<>();
    private DistrictSearch mDistrictSearch;
    private String colors = "";
    private DistrictSearchOption districtSearchOption;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        SDKInitializer.initialize(getActivity().getApplicationContext());
        View view = inflater.inflate(R.layout.fragment_map, null);
        ButterKnife.bind(this, view);
        mapView = (MapView) view.findViewById(R.id.fg_mTexturemap);
        mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
        mBaiduMap = mapView.getMap();
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(mCurrentMode,
                true, null));
        locationUtil = new LocationUtil(getActivity(), this);
        return view;
    }

    private void init() {
        if (parms == null) {
            parms = new HashMap<>();
        }
        parms.put("key", "6DlLqAyx3mY=");
        parms.put("regiontype", "area");
        MapDAL.getInstance().getCityData(parms, this);
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

    @Override
    public void onProgress(Object data) {
        MData data1 = (MData) data;
        if (data1.getType().equals(MDataType.MAP_DATA)) {
            MapBean mapBean = (MapBean) data1.getData();
            if (cityBeens.size() > 0) {
                cityBeens.clear();
            }
            cityBeens = mapBean.getMessage().getCityBeens();
            getArea(cityBeens);
        } else if (data1.getType().equals(MDataType.MAP)) {
            BDLocation location = (BDLocation) data1.getData();
            if (isFirstLocate) {
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
                mBaiduMap.animateMapStatus(update);
                update = MapStatusUpdateFactory.zoomTo(9f);
                mBaiduMap.animateMapStatus(update);
            /*判断baiduMap是已经移动到指定位置*/
                if (mBaiduMap.getLocationData() != null)
                    if (mBaiduMap.getLocationData().latitude == location.getLatitude()
                            && mBaiduMap.getLocationData().longitude == location.getLongitude()) {
                        isFirstLocate = false;
                    }
            }
            MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
            locationBuilder.latitude(location.getLatitude());
            locationBuilder.longitude(location.getLongitude());
            MyLocationData locationData = locationBuilder.build();
            mBaiduMap.setMyLocationData(locationData);
            init();
        }
    }

    @Override
    public void onError(String msg, String eCode) {

    }

    private void getArea(List<CityBean> cityBeens) {

        for(int i= 0;i<cityBeens.size();i++){
            if(mDistrictSearch !=null || districtSearchOption != null){
                mDistrictSearch = null;
                districtSearchOption = null;
            }
            mDistrictSearch = DistrictSearch.newInstance();
            //初始化行政区检索
            mDistrictSearch.setOnDistrictSearchListener(this);//设置回调监听
            districtSearchOption = new DistrictSearchOption();
            districtSearchOption.cityName(cityBeens.get(i).getRegionName());//检索城市名称
            mDistrictSearch.searchDistrict(districtSearchOption);//请求行政区数据
            colors = cityBeens.get(i).getAQIColor();
        }
    }

    //画行政区域
    @Override
    public void onGetDistrictResult(DistrictResult districtResult) {
        districtResult.getCenterPt();//获取行政区中心坐标点
        districtResult.getCityName();//获取行政区域名称
        Log.e("lili", "getCityName" + districtResult.getCityName());
        List<List<LatLng>> polyLines = districtResult.getPolylines();
        //获取行政区域边界坐标点 //边界就是坐标点的集合，在地图上画出来就是多边形图层。
        // 有的行政区可能有多个区域，所以会有多个点集合。
        if (polyLines == null) return;
        //地理边界对象
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (List<LatLng> polyline : polyLines) {
            OverlayOptions ooPolyline11 = new PolylineOptions().width(10)
                    .points(polyline).dottedLine(true).color(Color.RED);
            mBaiduMap.addOverlay(ooPolyline11);//添加OverLay
            OverlayOptions ooPolygon = new PolygonOptions().points(polyline).
                        stroke(new Stroke(2, 0xAA00FF88)).
                        fillColor(Color.parseColor(colors));
            mBaiduMap.addOverlay(ooPolygon);//添加OverLay
            for (LatLng latLng : polyline) {
                builder.include(latLng);//包含这些点
            }
        }

    }

    /*
      * 异步任务执行网络下载图片
      * */
    public class DownTask extends AsyncTask<CityBean, Void, CityBean> {

        @Override
        protected CityBean doInBackground(CityBean... params) {
            return params[0];
        }

        @Override
        //在界面上显示进度条
        protected void onPreExecute() {
        }

        ;

        //主要是更新UI
        @Override
        protected void onPostExecute(CityBean result) {

            super.onPostExecute(result);
        }
    }

}
