package com.tim.pollution.activity;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.tim.pollution.R;
import com.tim.pollution.adapter.CityAdapter;
import com.tim.pollution.adapter.FocusCityAdapter;
import com.tim.pollution.bean.CityBean;
import com.tim.pollution.bean.MapBean;
import com.tim.pollution.bean.changetrend.RegionNetBean;
import com.tim.pollution.callback.ICallBack;
import com.tim.pollution.general.BaseActivity;
import com.tim.pollution.general.Constants;
import com.tim.pollution.general.MData;
import com.tim.pollution.general.MDataType;
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

public class FocusCityActivity extends BaseActivity implements ICallBack {

    @BindView(R.id.city_recyview)
    RecyclerView recyclerView;
    @BindView(R.id.city_back_iv)
    ImageView ivBack;
    private List<RegionNetBean.RegionBean>cityBeens;

    private FocusCityAdapter adapter;
    private GridLayoutManager lm;
    private RegionNetBean regionNetBean;
    private Map<String,String> parms;
    private AlertDialog dialogAgain;

    @Override
    public int intiLayout() {
        return R.layout.activity_city;
    }

    @Override
    public void initView() {
        cityBeens = new ArrayList<>();

        adapter = new FocusCityAdapter(this,cityBeens);
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
//        parms.put("key", Constants.key);
//        parms.put("regiontype", "region");
        parms.put("key", Constants.key);
        parms.put("regiontype", "region");
        WeatherDal.getInstance().getRegion(parms, this);
//        MapDAL.getInstance().getCityData(parms, this);
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
//            regionNetBean = getTest();
            if (regionNetBean != null) {
                cityBeens.addAll(regionNetBean.getMessage());
                adapter.notifyDataSetChanged();
            }

        }

    }

    @Override
    public void onError(String msg, String eCode) {
//        swipeRefreshLayout.setRefreshing(false);
        Toast.makeText(this, msg, Toast.LENGTH_LONG);
        showAgainDailog(msg);
    }

    /**
     * 重试
     */
    private void showAgainDailog(String msg) {
        if (dialogAgain != null) {
            dialogAgain.show();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage(msg + "是否重试？");
        builder.setIcon(R.mipmap.prompt);
        //点击对话框以外的区域是否让对话框消失
        builder.setCancelable(false);
        builder.setNegativeButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (parms == null) {
                    parms = new HashMap<>();
                }
                parms.put("key", Constants.key);
                parms.put("regiontype", "region");
                WeatherDal.getInstance().getRegion(parms, FocusCityActivity.this);
            }
        });
        dialogAgain = builder.create();
        if (dialogAgain != null) {
            //显示对话框
            dialogAgain.show();
        }
    }

}
