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
import com.tim.pollution.bean.LevePollutionBean;
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
import com.tim.pollution.net.WeatherDal;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static android.R.attr.data;

/**
 * Created by lenovo on 2018/4/27.
 */

public class CityActivity extends BaseActivity implements ICallBack {

    @BindView(R.id.city_recyview)
    RecyclerView recyclerView;
    @BindView(R.id.city_back_iv)
    ImageView ivBack;
    private List<RegionNetBean.RegionBean>cityBeens;

    private CityAdapter adapter;
    private GridLayoutManager lm;
    private RegionNetBean regionNetBean;

    @Override
    public int intiLayout() {
        return R.layout.activity_city;
    }

    @Override
    public void initView() {
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
        Map<String, String> params = new HashMap<>();
        params.put("key", Constants.key);
        params.put("regiontype", "city");
        WeatherDal.getInstance().getRegion(params, this);
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
    public void onProgress(Object data) {
        MData mData = (MData) data;
        if (MDataType.REGIONNET_BEAN.equals(mData.getType())) {
            regionNetBean = (RegionNetBean) mData.getData();
            if (regionNetBean != null) {
                if(cityBeens.size()>0){
                    cityBeens.clear();
                }

                cityBeens .addAll(regionNetBean.getMessage());
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onError(String msg, String eCode) {

    }

}
