package com.tim.pollution.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tim.pollution.R;
import com.tim.pollution.callback.ICallBack;
import com.tim.pollution.general.MData;
import com.tim.pollution.general.MDataType;
import com.tim.pollution.net.RankDAL;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lenovo on 2018/4/23.
 * 排行
 */


public class SellersFragment extends Fragment implements ICallBack{
    @BindView(R.id.sellers_station_tv)
    TextView tvStation;
    @BindView(R.id.sellers_swicth_tv)
    TextView tvSwitch;
    @BindView(R.id.now_ll)
    LinearLayout llNow;
    @BindView(R.id.all_ll)
    LinearLayout llAll;
    @BindView(R.id.yesterday_ll)
    LinearLayout llYestday;
    @BindView(R.id.last_ll)
    LinearLayout llLast;
    @BindView(R.id.now_tv)
    TextView tvNow;
    @BindView(R.id.now_view)
    View vNow;
    @BindView(R.id.all_tv)
    TextView tvAll;
    @BindView(R.id.all_view)
    View vAll;
    @BindView(R.id.yesterday_tv)
    TextView tvYesterday;
    @BindView(R.id.yesterday_view)
    View vYesterday;
    @BindView(R.id.last_tv)
    TextView tvLast;
    @BindView(R.id.last_view)
    View vLast;
    private Map<String ,String>parms;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sellers,null);
        ButterKnife.bind(this,view);
        setClear();
        tvSwitch.setSelected(true);
        llNow.setSelected(true);
        if(llNow.isSelected()){
            tvNow.setTextColor(getResources().getColor(R.color.color_white));
            vNow.setVisibility(View.VISIBLE);
        }
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(parms ==null){
            parms = new HashMap<>();
        }
        parms.put("regiontype","city");
        parms.put("regiontype","AQI");
        parms.put("ranktype","real");
        parms.put("pointtype","");
        RankDAL.getInstance().getRank(parms,this);
    }

    @OnClick({R.id.now_ll,R.id.last_ll,R.id.yesterday_ll,R.id.all_ll,R.id.sellers_swicth_tv})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.now_ll:
                setClear();
                llNow.setSelected(true);
                if(llNow.isSelected()){
                    tvNow.setTextColor(getResources().getColor(R.color.color_white));
                    vNow.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.last_ll:
                setClear();
                llLast.setSelected(true);
                if(llLast.isSelected()){
                    tvLast.setTextColor(getResources().getColor(R.color.color_white));
                    vLast.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.yesterday_ll:
                setClear();
                llYestday.setSelected(true);
                if(llYestday.isSelected()){
                    tvYesterday.setTextColor(getResources().getColor(R.color.color_white));
                    vYesterday.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.all_ll:
                setClear();
                llAll.setSelected(true);
                if(llAll.isSelected()){
                    tvAll.setTextColor(getResources().getColor(R.color.color_white));
                    vAll.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.sellers_swicth_tv:
                if(tvSwitch.isSelected()){
                    tvSwitch.setText("省控");
                    tvSwitch.setSelected(false);
                }else{
                    tvSwitch.setText("国控");
                    tvSwitch.setSelected(true);
                }

                break;
        }
    }

    private void setClear(){
        tvNow.setTextColor(getResources().getColor(R.color.tx_gray));
        tvAll.setTextColor(getResources().getColor(R.color.tx_gray));
        tvLast.setTextColor(getResources().getColor(R.color.tx_gray));
        tvYesterday.setTextColor(getResources().getColor(R.color.tx_gray));
        vNow.setVisibility(View.INVISIBLE);
        vAll.setVisibility(View.INVISIBLE);
        vYesterday.setVisibility(View.INVISIBLE);
        vLast.setVisibility(View.INVISIBLE);
        llNow.setSelected(false);
        llLast.setSelected(false);
        llYestday.setSelected(false);
        llAll.setSelected(false);
    }

    @Override
    public void onProgress(Object data) {
        MData mData = (MData) data;
        if(mData.getType().equals(MDataType.RANK)){

        }
    }

    @Override
    public void onError(String msg, String eCode) {

    }
}
