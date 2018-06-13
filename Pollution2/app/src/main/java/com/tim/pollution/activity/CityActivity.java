package com.tim.pollution.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.tim.pollution.MyApplication;
import com.tim.pollution.R;
import com.tim.pollution.adapter.CityAdapter;
import com.tim.pollution.bean.CityBean;
import com.tim.pollution.bean.LevePollutionBean;
import com.tim.pollution.bean.MapBean;
import com.tim.pollution.bean.MyData;
import com.tim.pollution.bean.changetrend.RegionNetBean;
import com.tim.pollution.callback.ICallBack;
import com.tim.pollution.callback.OnItemClickListener;
import com.tim.pollution.general.BaseActivity;
import com.tim.pollution.general.Constants;
import com.tim.pollution.general.LocationData;
import com.tim.pollution.general.MData;
import com.tim.pollution.general.MDataType;
import com.tim.pollution.general.MessageEvent;
import com.tim.pollution.net.MapDAL;
import com.tim.pollution.net.WeatherDal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by lenovo on 2018/4/27.
 */

public class CityActivity extends BaseActivity implements ICallBack {

    @BindView(R.id.city_recyview)
    RecyclerView recyclerView;
    @BindView(R.id.city_back_iv)
    ImageView ivBack;
    private List<CityBean>cityBeens;

    private CityAdapter adapter;
    private GridLayoutManager lm;
    private RegionNetBean regionNetBean;
    private Map<String,String> parms;

    @Override
    public int intiLayout() {
        return R.layout.activity_city;
    }

    @Override
    public void initView() {
        setActivityName("城市列表选择");
        cityBeens = new ArrayList<>();

        adapter = new CityAdapter(this,cityBeens);
        lm = new GridLayoutManager(this,4);
        recyclerView.setLayoutManager(lm);
        recyclerView.setAdapter(adapter);
        loadRegionData();
    }
    /**
     * 加载县区列表
     */
    private void loadRegionData() {
        if (parms == null) {
            parms = new HashMap<>();
        }
        parms.put("key", Constants.key);
        parms.put("regiontype", "area");
        MapDAL.getInstance().getCityData(parms, this);
    }
    @Override
    public void initData() {

    }

    @OnClick({R.id.city_back_iv})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.city_back_iv:
                finish();
                break;
        }
    }

    @Override
    public void onSuccess(Object data) {
        MData mData = (MData) data;
        if (mData.getType().equals(MDataType.MAP_DATA)) {//获得城市名
            MapBean mapBean = (MapBean) mData.getData();
            if (cityBeens.size() > 0) {
                cityBeens.clear();
            }
            cityBeens.addAll(mapBean.getMessage().getCityBeens());
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onError(String msg, String eCode) {

    }

}
