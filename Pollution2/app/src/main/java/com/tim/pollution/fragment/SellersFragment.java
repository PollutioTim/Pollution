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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lenovo on 2018/4/23.
 * 排行
 */


public class SellersFragment extends Fragment {
    @Bind(R.id.sellers_station_tv)
    TextView tvStation;
    @Bind(R.id.sellers_swicth_tv)
    TextView tvSwitch;
    @Bind(R.id.now_ll)
    LinearLayout llNow;
    @Bind(R.id.all_ll)
    LinearLayout llAll;
    @Bind(R.id.yesterday_ll)
    LinearLayout llYestday;
    @Bind(R.id.last_ll)
    LinearLayout llLast;
    @Bind(R.id.now_tv)
    TextView tvNow;
    @Bind(R.id.now_view)
    View vNow;
    @Bind(R.id.all_tv)
    TextView tvAll;
    @Bind(R.id.all_view)
    View vAll;
    @Bind(R.id.yesterday_tv)
    TextView tvYesterday;
    @Bind(R.id.yesterday_view)
    View vYesterday;
    @Bind(R.id.last_tv)
    TextView tvLast;
    @Bind(R.id.last_view)
    View vLast;


    public static SellersFragment newInstance(String param1) {
        SellersFragment fragment = new SellersFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }
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
}
