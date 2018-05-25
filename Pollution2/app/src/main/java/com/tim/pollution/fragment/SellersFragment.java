package com.tim.pollution.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tim.pollution.MainActivity;
import com.tim.pollution.R;
import com.tim.pollution.adapter.RankAdapter;
import com.tim.pollution.adapter.RankLastAdapter;
import com.tim.pollution.bean.MyData;
import com.tim.pollution.bean.RankLastBean;
import com.tim.pollution.bean.RankMainBean;
import com.tim.pollution.callback.ICallBack;
import com.tim.pollution.general.MData;
import com.tim.pollution.general.MDataType;
import com.tim.pollution.net.RankDAL;
import com.woodnaonly.arcprogress.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.support.v4.widget.DrawerLayout.STATE_DRAGGING;
import static android.support.v4.widget.DrawerLayout.STATE_IDLE;
import static android.support.v4.widget.DrawerLayout.STATE_SETTLING;

/**
 * Created by lenovo on 2018/4/23.
 * 排行
 */


public class SellersFragment extends Fragment implements ICallBack {
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


    @BindView(R.id.index_all_tv)
    TextView tvIndex;
    @BindView(R.id.sellers_pm_25)
    LinearLayout llPM25;
    @BindView(R.id.sellers_pm_10)
    LinearLayout llPM10;
    @BindView(R.id.sellers_no_2)
    LinearLayout llNO2;
    @BindView(R.id.sellers_so_2)
    LinearLayout llSO2;
    @BindView(R.id.sellers_o_3)
    LinearLayout llO3;

    @BindView(R.id.sellers_pm_25_tv)
    TextView tvPM2;
    @BindView(R.id.sellers_2_5_tv)
    TextView tv25;
    @BindView(R.id.sellers_pm_10_tv)
    TextView tvPM10;
    @BindView(R.id.sellers_10_tv)
    TextView tv10;
    @BindView(R.id.sellers_no_2_tv)
    TextView tvNO;
    @BindView(R.id.sellers_2_tv)
    TextView tv2;
    @BindView(R.id.sellers_so_tv)
    TextView tvSO;
    @BindView(R.id.sellers_so_2_tv)
    TextView tvSO2;
    @BindView(R.id.sellers_o_3_tv)
    TextView tvO;
    @BindView(R.id.sellers_3_tv)
    TextView tvO3;
    @BindView(R.id.sellers_co_tv)
    TextView tvCO;

    @BindView(R.id.item_rank_type_tv)
    TextView tvTitleType;
    @BindView(R.id.sellers_recyview)
    RecyclerView recyclerView;
    @BindView(R.id.sellers_month_recyview)
    RecyclerView recviewMonth;
    @BindView(R.id.sellers_rl)
    RelativeLayout rl;

//    @BindView(R.id.city_tv)
    TextView tvCity;
//    @BindView(R.id.region_tv)
    TextView tvRegion;
//    @BindView(R.id.point_tv)
    TextView tvPoint;
    @BindView(R.id.sellers_sort_ll)
    LinearLayout llSort;
    private Map<String, String> parms;
    private List<RankMainBean.Message> datas;
    private List<RankLastBean.Message> rankLasts;
    private RankAdapter adapter;
    private RankLastAdapter lastAdapter;
    private String datatype = "real";//初始为real
    private String pointtype = "";
    private String ranktype = "AQI";//初始为aqi
    private LinearLayoutManager lm;
    private LinearLayoutManager lms;
    private String regiontype = "city";
    private boolean flag = true;
    int height ;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sellers, null);
        ButterKnife.bind(this, view);
        setClear();
        tvSwitch.setSelected(true);
        llNow.setSelected(true);
        tvNow.setTextColor(getResources().getColor(R.color.color_white));
        vNow.setVisibility(View.VISIBLE);
        setTextColor();
        tvIndex.setTextColor(getResources().getColor(R.color.color_white));
        tvIndex.setText("AQI");
        tvTitleType.setText("AQI");
        initMain();
        initLast();
        getData();
        return view;
    }

    private void initMain() {
        lm = new LinearLayoutManager(getActivity());
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(lm);
        datas = new ArrayList<>();
        adapter = new RankAdapter(getActivity(), datas);
        recyclerView.setAdapter(adapter);

        ViewTreeObserver vto = rl.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                rl.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                height =rl.getHeight();
                Log.e("lili","height="+height);
                int statusBarHeight1 = -1;
//获取status_bar_height资源的ID
                int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
                if (resourceId > 0) {
                    //根据资源ID获取响应的尺寸值
                    statusBarHeight1 = getResources().getDimensionPixelSize(resourceId);
                }
                height = height+statusBarHeight1;
            }
        });
    }

    private void initLast() {
        lms = new LinearLayoutManager(getActivity());
        lms.setOrientation(LinearLayoutManager.VERTICAL);
        recviewMonth.setLayoutManager(lms);

        rankLasts = new ArrayList<>();
        lastAdapter = new RankLastAdapter(getActivity(), rankLasts);
        recviewMonth.setAdapter(lastAdapter);
    }

    private void getData() {
        if (parms == null) {
            parms = new HashMap<>();
        }
        parms.put("key", "6DlLqAyx3mY=");
        parms.put("regiontype", regiontype);
        parms.put("datatype", datatype);
        parms.put("ranktype", ranktype);
        parms.put("pointtype", pointtype);
        if (!llLast.isSelected()) {
            RankDAL.getInstance().getRank(parms, this);
        } else {
            RankDAL.getInstance().getRankLast(parms, this);
        }
    }

    @OnClick({R.id.now_ll, R.id.last_ll, R.id.yesterday_ll, R.id.all_ll, R.id.sellers_swicth_tv
            , R.id.sellers_o_3, R.id.index_all_tv, R.id.sellers_pm_25, R.id.sellers_pm_10, R.id.sellers_so_2,
            R.id.sellers_co_tv, R.id.sellers_no_2,R.id.sellers_station_tv, R.id.sellers_sort_ll})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.sellers_sort_ll:
                if(flag){
                    Collections.reverse(datas);
                    adapter.notifyDataSetChanged();
                }else{
                    Collections.reverse(rankLasts);
                    lastAdapter.notifyDataSetChanged();
                }
                break;
            case R.id.sellers_station_tv:
                //弹出站点的选择框
                View popupView = getActivity().getLayoutInflater().inflate(R.layout.popuwindow_layout, null);
                tvPoint = (TextView) popupView.findViewById(R.id.point_tv);
                tvCity = (TextView) popupView.findViewById(R.id.city_tv);
                tvRegion = (TextView) popupView.findViewById(R.id.region_tv);
                final PopupWindow window = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,true);
                window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F8F8F8")));
                window.setFocusable(true);
                window.setOutsideTouchable(true);
                window.update();
                window.showAsDropDown(popupView, 10, height);
                tvPoint.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        regiontype = "point";
                        if("".equals(pointtype)){
                            Toast.makeText(getActivity(),"请选择站点类型",Toast.LENGTH_LONG).show();
                        }else{
                            getData();
                        }
                        window.dismiss();
                    }
                });
                tvCity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        regiontype = "city";
                        getData();
                        window.dismiss();
                    }
                });
                tvRegion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        regiontype = "region";
                        getData();
                        window.dismiss();
                    }
                });
                break;
            case R.id.now_ll://实时
                setClear();
                datatype = "real";
                llNow.setSelected(true);
                if (llNow.isSelected()) {
                    flag = true;
                    tvNow.setTextColor(getResources().getColor(R.color.color_white));
                    vNow.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                    recviewMonth.setVisibility(View.GONE);

                    if(ranktype.equals("ZH")){
                        ranktype = "AQI";
                    }
                    tvIndex.setText("AQI");
                    tvO3.setText("O3");
                    getData();
                }
                break;
            case R.id.last_ll://上月
                setClear();
                datatype = "month";
                llLast.setSelected(true);
                if (llLast.isSelected()) {
                    flag = false;
                    tvLast.setTextColor(getResources().getColor(R.color.color_white));
                    vLast.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    recviewMonth.setVisibility(View.VISIBLE);
                    tvIndex.setText("综指");
                    tvO3.setText("3_8H");
                    getData();
                }
                break;
            case R.id.yesterday_ll://昨日排
                setClear();
                datatype = "day";
                llYestday.setSelected(true);
                if (llYestday.isSelected()) {
                    flag = true;
                    tvYesterday.setTextColor(getResources().getColor(R.color.color_white));
                    vYesterday.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                    recviewMonth.setVisibility(View.GONE);
                    tvIndex.setText("AQI");
                    tvO3.setText("3_8H");
                    getData();
                }
                break;
            case R.id.all_ll://累计排
                setClear();
                datatype = "realsum";
                llAll.setSelected(true);
                if (llAll.isSelected()) {
                    flag = true;
                    tvAll.setTextColor(getResources().getColor(R.color.color_white));
                    vAll.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                    recviewMonth.setVisibility(View.GONE);
                    tvIndex.setText("AQI");
                    tvO3.setText("3_8H");
                    getData();
                }
                break;
            case R.id.sellers_swicth_tv:
                if (tvSwitch.isSelected()) {
                    tvSwitch.setText("省控");
                    pointtype = "S";
                    tvSwitch.setSelected(false);
                } else {
                    tvSwitch.setText("国控");
                    pointtype = "G";
                    tvSwitch.setSelected(true);
                }
                getData();
                break;
            case R.id.sellers_o_3:
                setTextColor();
                tvO.setTextColor(getResources().getColor(R.color.color_white));
                tvO3.setTextColor(getResources().getColor(R.color.color_white));
                ranktype = "O3";
                tvTitleType.setText("O3");
                getData();
                break;
            case R.id.index_all_tv://aqi 和zh选择切换
                setTextColor();
                tvIndex.setTextColor(getResources().getColor(R.color.color_white));
                if (!llLast.isSelected()) {
                    ranktype = "AQI";
                    tvTitleType.setText("AQI");
                } else {
                    ranktype = "ZH";
                    tvTitleType.setText("综指");
                }
                getData();
                break;
            case R.id.sellers_pm_25:
                setTextColor();
                tv25.setTextColor(getResources().getColor(R.color.color_white));
                tvPM2.setTextColor(getResources().getColor(R.color.color_white));
                ranktype = "PM25";
                tvTitleType.setText("PM25");
                Log.e("lili","rankType="+ranktype+"dataType="+datatype+"pointtype="+pointtype);
                getData();
                break;
            case R.id.sellers_pm_10:
                setTextColor();
                tv10.setTextColor(getResources().getColor(R.color.color_white));
                tvPM10.setTextColor(getResources().getColor(R.color.color_white));
                ranktype = "PM10";
                tvTitleType.setText("PM10");
                getData();
                break;
            case R.id.sellers_so_2:
                setTextColor();
                tvSO.setTextColor(getResources().getColor(R.color.color_white));
                tvSO2.setTextColor(getResources().getColor(R.color.color_white));
                ranktype = "SO2";
                tvTitleType.setText("SO2");
                getData();
                break;
            case R.id.sellers_co_tv:
                setTextColor();
                tvCO.setTextColor(getResources().getColor(R.color.color_white));
                ranktype = "CO";
                tvTitleType.setText("CO");
                getData();
                break;
            case R.id.sellers_no_2:
                setTextColor();
                tvNO.setTextColor(getResources().getColor(R.color.color_white));
                tv2.setTextColor(getResources().getColor(R.color.color_white));
                ranktype = "NO2";
                tvTitleType.setText("NO2");
                getData();
                break;
        }
    }

    private void setClear() {
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

    private void setTextColor() {
        tvIndex.setTextColor(getResources().getColor(R.color.gree_blue));
        tvO3.setTextColor(getResources().getColor(R.color.gree_blue));
        tvCO.setTextColor(getResources().getColor(R.color.gree_blue));
        tvO.setTextColor(getResources().getColor(R.color.gree_blue));
        tvPM2.setTextColor(getResources().getColor(R.color.gree_blue));
        tv2.setTextColor(getResources().getColor(R.color.gree_blue));
        tv10.setTextColor(getResources().getColor(R.color.gree_blue));
        tvSO.setTextColor(getResources().getColor(R.color.gree_blue));
        tvPM10.setTextColor(getResources().getColor(R.color.gree_blue));
        tvSO2.setTextColor(getResources().getColor(R.color.gree_blue));
        tv25.setTextColor(getResources().getColor(R.color.gree_blue));
        tvNO.setTextColor(getResources().getColor(R.color.gree_blue));
    }

    @Override
    public void onSuccess(Object data) {
        MData mData = (MData) data;
        if (mData.getType().equals(MDataType.RANK)) {
            RankMainBean rankMainBean = (RankMainBean) mData.getData();
            if (rankMainBean != null) {
                if (datas.size() > 0 || rankLasts.size() > 0) {
                    datas.clear();
                }
                datas .addAll( rankMainBean.getMessages());
                Log.e("lili","datas="+datas.toString());
                adapter.notifyDataSetChanged();
            }
        } else if (mData.getType().equals(MDataType.RANK_LAST)) {
            RankLastBean rankLastBean = (RankLastBean) mData.getData();
            if (rankLastBean != null) {
                if (rankLasts.size() > 0 || datas.size() > 0) {
                    rankLasts.clear();
                }
                rankLasts.addAll(rankLastBean.getMessages());
                lastAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onError(String msg, String eCode) {
    }
}
