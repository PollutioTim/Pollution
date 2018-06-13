package com.tim.pollution.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.text.style.SuperscriptSpan;
import android.util.Log;
import android.util.TimeUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tim.pollution.MainActivity;
import com.tim.pollution.R;
import com.tim.pollution.activity.SellersDetailActivity;
import com.tim.pollution.adapter.RankAdapter;
import com.tim.pollution.adapter.RankLastAdapter;
import com.tim.pollution.bean.MyData;
import com.tim.pollution.bean.RankLastBean;
import com.tim.pollution.bean.RankMainBean;
import com.tim.pollution.callback.ICallBack;
import com.tim.pollution.general.MData;
import com.tim.pollution.general.MDataType;
import com.tim.pollution.net.RankDAL;
import com.tim.pollution.utils.DateUtil;
import com.tim.pollution.utils.TimeString;
import com.woodnaonly.arcprogress.Utils;

import java.text.SimpleDateFormat;
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
 * 排名
 */


public class SellersFragment extends Fragment implements ICallBack {
    @BindView(R.id.sellers_swicth_tv)
    TextView tvSwitch;
    @BindView(R.id.now_ll)
    LinearLayout llNow;
    @BindView(R.id.all_ll)
    LinearLayout llAll;
    @BindView(R.id.yesterday_ll)
    LinearLayout llYestday;
    @BindView(R.id.year_ll)
    LinearLayout llYear;
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
    @BindView(R.id.year_view)
    View vYear;
    @BindView(R.id.year_tv)
    TextView tvYear;

    @BindView(R.id.seller_one_rb)
    Button btn11;
    @BindView(R.id.seller_two_rb)
    Button btn102;
    @BindView(R.id.seller_three_rb)
    Button btntongdao;
    @BindView(R.id.seller_four_rb)
    Button btnPinYuan;

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
    @BindView(R.id.unit_tv)
    TextView tvUnit;
    @BindView(R.id.time_seller_tv)
    TextView tvTime;

    //    @BindView(R.id.city_tv)
    TextView tvCity;
    //    @BindView(R.id.region_tv)
    TextView tvRegion;
    //    @BindView(R.id.point_tv)
    TextView tvPoint;
    @BindView(R.id.sellers_sort_ll)
    LinearLayout llSort;
    @BindView(R.id.top_down_iv)
    ImageView ivTopDown;

    private Map<String, String> parms;
    private List<RankMainBean.Message.Content> datas;
    private List<RankLastBean.Message.Content> rankLasts;
    private RankAdapter adapter;
    private RankLastAdapter lastAdapter;
    private String datatype = "real";//初始为real
    private String pointtype = "";
    private String ranktype = "AQI";//初始为aqi
    private LinearLayoutManager lm;
    private LinearLayoutManager lms;
    private String regiontype = "city";//区域类型
    private String areatype = "allregion";//区县 allregion、Point
    private boolean flag = true;
    private boolean isClick= true;//箭头的选择
    int height;
    private boolean isSelect = true;//是否选择了aqi

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
        setBtnClear();
        btn11.setSelected(true);
        tvIndex.setTextColor(getResources().getColor(R.color.color_white));
        tvIndex.setText("AQI");
        tvTitleType.setText("AQI");
        tvUnit.setText("mg/m3");
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
        adapter = new RankAdapter(datas);
        recyclerView.setAdapter(adapter);

        adapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                Intent intent = new Intent(getActivity(), SellersDetailActivity.class);
                intent.putExtra("item", datas.get(i).getId());
                intent.putExtra("regiontype", regiontype);
                intent.putExtra("datatype", datatype);
                intent.putExtra("ranktype", ranktype);
                intent.putExtra("areatype", areatype);
                startActivity(intent);
            }
        });

        ViewTreeObserver vto = rl.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                rl.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                height = rl.getHeight();
                int statusBarHeight1 = -1;
                //获取status_bar_height资源的ID
                int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
                if (resourceId > 0) {
                    //根据资源ID获取响应的尺寸值
                    statusBarHeight1 = getResources().getDimensionPixelSize(resourceId);
                }
                height = height + statusBarHeight1;
            }
        });
    }

    private void initLast() {
        lms = new LinearLayoutManager(getActivity());
        lms.setOrientation(LinearLayoutManager.VERTICAL);
        recviewMonth.setLayoutManager(lms);

        rankLasts = new ArrayList<>();
        lastAdapter = new RankLastAdapter(rankLasts);
        recviewMonth.setAdapter(lastAdapter);
        lastAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                Intent intent = new Intent(getActivity(), SellersDetailActivity.class);
                intent.putExtra("item", rankLasts.get(i).getAreaid());
                intent.putExtra("regiontype", regiontype);
                intent.putExtra("datatype", datatype);
                intent.putExtra("ranktype", ranktype);
                intent.putExtra("areatype", areatype);
                startActivity(intent);
            }
        });
    }

    private void getData() {
        if (parms == null) {
            parms = new HashMap<>();
        }
        parms.put("key", "6DlLqAyx3mY=");
        parms.put("regiontype", regiontype);
        parms.put("datatype", datatype);
        parms.put("ranktype", ranktype);
        parms.put("areatype", areatype);
        Log.e("lili", "regiontype = " + regiontype + ",datatype=" + datatype + ",ranktype=" + ranktype
                + ",areatype=" + areatype);
        if (llLast.isSelected() || llYear.isSelected()) {
            RankDAL.getInstance().getRankLast(parms, this);
        } else {
            RankDAL.getInstance().getRank(parms, this);
        }
    }

    @OnClick({R.id.now_ll, R.id.last_ll, R.id.yesterday_ll, R.id.all_ll, R.id.sellers_swicth_tv
            , R.id.sellers_o_3, R.id.index_all_tv, R.id.sellers_pm_25, R.id.sellers_pm_10, R.id.sellers_so_2,
            R.id.sellers_co_tv, R.id.sellers_no_2, R.id.sellers_sort_ll, R.id.seller_one_rb, R.id.seller_two_rb
            , R.id.year_ll, R.id.seller_four_rb, R.id.seller_three_rb})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.sellers_sort_ll://顺序
                if (flag) {//正序
                    Collections.reverse(datas);
                    adapter.notifyDataSetChanged();
                } else {
                    Collections.reverse(rankLasts);
                    lastAdapter.notifyDataSetChanged();
                }
                if(isClick){
                    isClick = false;
                    ivTopDown.setImageResource(R.drawable.ic_down_jiantou);
                }else{
                    isClick = true;
                    ivTopDown.setImageResource(R.drawable.ic_top_jiantou);
                }
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

                    if (ranktype.equals("ZH")) {
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
                    if (isSelect) {
                        ranktype = "ZH";
                    }
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
            case R.id.year_ll://年排
                setClear();
                datatype = "year";
                llYear.setSelected(true);
                if (llYear.isSelected()) {
                    flag = false;
                    tvYear.setTextColor(getResources().getColor(R.color.color_white));
                    vYear.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    recviewMonth.setVisibility(View.VISIBLE);
                    tvIndex.setText("综指");
                    if (isSelect) {
                        ranktype = "ZH";
                    }
                    tvO3.setText("3_8H");
                    getData();
                }
                break;
            case R.id.sellers_swicth_tv:
                if (tvSwitch.isSelected()) {
                    tvSwitch.setText("区县");
                    areatype = "allregion";
                    tvSwitch.setSelected(false);
                } else {
                    tvSwitch.setText("站点");
                    areatype = "point";
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
                tvUnit.setText("ug/m3");
                getData();
                break;
            case R.id.index_all_tv://aqi 和zh选择切换
                setTextColor();
                tvIndex.setTextColor(getResources().getColor(R.color.color_white));
                isSelect = true;
                if (llLast.isSelected() || llYear.isSelected()) {
                    ranktype = "ZH";
                    tvTitleType.setText("综指");
                } else {
                    ranktype = "AQI";
                    tvTitleType.setText("AQI");
                }
                isSelect = true;
                tvUnit.setText("ug/m3");
                getData();
                break;
            case R.id.sellers_pm_25:
                setTextColor();
                isSelect = false;
                tv25.setTextColor(getResources().getColor(R.color.color_white));
                tvPM2.setTextColor(getResources().getColor(R.color.color_white));
                ranktype = "PM25";
                tvTitleType.setText("PM25");
                Log.e("lili", "rankType=" + ranktype + "dataType=" + datatype + "pointtype=" + pointtype);
                getData();
                tvUnit.setText("ug/m3");
                break;
            case R.id.sellers_pm_10:
                setTextColor();
                isSelect = false;
                tv10.setTextColor(getResources().getColor(R.color.color_white));
                tvPM10.setTextColor(getResources().getColor(R.color.color_white));
                ranktype = "PM10";
                tvTitleType.setText("PM10");
                tvUnit.setText("ug/m3");
                getData();
                break;
            case R.id.sellers_so_2:
                setTextColor();
                isSelect = false;
                tvSO.setTextColor(getResources().getColor(R.color.color_white));
                tvSO2.setTextColor(getResources().getColor(R.color.color_white));
                ranktype = "SO2";
                tvTitleType.setText("SO2");
                tvUnit.setText("ug/m3");
                getData();
                break;
            case R.id.sellers_co_tv:
                setTextColor();
                isSelect = false;
                tvCO.setTextColor(getResources().getColor(R.color.color_white));
                ranktype = "CO";
                tvTitleType.setText("CO");
                tvUnit.setText("mg/m3");
                getData();
                break;
            case R.id.sellers_no_2:
                setTextColor();
                isSelect = false;
                tvNO.setTextColor(getResources().getColor(R.color.color_white));
                tv2.setTextColor(getResources().getColor(R.color.color_white));
                ranktype = "NO2";
                tvTitleType.setText("NO2");
                tvUnit.setText("ug/m3");
                getData();
                break;
            case R.id.seller_one_rb://11个城市
                regiontype = "city";
                setBtnClear();
                btn11.setSelected(true);
                getData();
                break;
            case R.id.seller_two_rb://102个县
                regiontype = "region";
                setBtnClear();
                btn102.setSelected(true);
                getData();
                break;
            case R.id.seller_three_rb://通道
                regiontype = "TD";
                setBtnClear();
                btntongdao.setSelected(true);
                getData();
                break;
            case R.id.seller_four_rb://平原
                regiontype = "PY";
                setBtnClear();
                btnPinYuan.setSelected(true);
                getData();
                break;
        }
    }

    private void setClear() {
        tvNow.setTextColor(getResources().getColor(R.color.tx_gray));
        tvAll.setTextColor(getResources().getColor(R.color.tx_gray));
        tvLast.setTextColor(getResources().getColor(R.color.tx_gray));
        tvYesterday.setTextColor(getResources().getColor(R.color.tx_gray));
        tvYear.setTextColor(getResources().getColor(R.color.tx_gray));
        vYear.setVisibility(View.INVISIBLE);
        vNow.setVisibility(View.INVISIBLE);
        vAll.setVisibility(View.INVISIBLE);
        vYesterday.setVisibility(View.INVISIBLE);
        vLast.setVisibility(View.INVISIBLE);
        llNow.setSelected(false);
        llLast.setSelected(false);
        llYestday.setSelected(false);
        llAll.setSelected(false);
        llYear.setSelected(false);
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

    private void setBtnClear() {
        btn11.setSelected(false);
        btn102.setSelected(false);
        btntongdao.setSelected(false);
        btnPinYuan.setSelected(false);
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
                datas.addAll(rankMainBean.getMessages().getContents());

                tvTime.setText(TimeString.switchTime(
                        rankMainBean.getMessages().getTime()));
                adapter.notifyDataSetChanged();
            }
        } else if (mData.getType().equals(MDataType.RANK_LAST)) {
            RankLastBean rankLastBean = (RankLastBean) mData.getData();
            if (rankLastBean != null) {
                if (rankLasts.size() > 0 || datas.size() > 0) {
                    rankLasts.clear();
                }

                tvTime.setText(rankLastBean.getMessages().getTime());
                rankLasts.addAll(rankLastBean.getMessages().getContents());
                Log.e("lili", "ranklasts=" + rankLasts.size());
                lastAdapter.notifyDataSetChanged();
            }
        }
    }


    @Override
    public void onError(String msg, String eCode) {
    }
}
