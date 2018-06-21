package com.tim.pollution.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tim.pollution.R;
import com.tim.pollution.adapter.RankAdapter;
import com.tim.pollution.adapter.RankLastAdapter;
import com.tim.pollution.bean.RankLastBean;
import com.tim.pollution.bean.RankMainBean;
import com.tim.pollution.callback.ICallBack;
import com.tim.pollution.general.BaseActivity;
import com.tim.pollution.general.MData;
import com.tim.pollution.general.MDataType;
import com.tim.pollution.net.RankDAL;
import com.tim.pollution.utils.TimeString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.http.Field;

import static com.tim.pollution.R.layout.fragment_sellers;

/**
 * Created by lenovo on 2018/5/23.
 */

public class SellersDetailActivity extends BaseActivity implements ICallBack {

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
    @BindView(R.id.switch_ll)
    LinearLayout llBtn;
    @BindView(R.id.title_ll)
    LinearLayout llTitle;
    @BindView(R.id.top_down_iv)
    ImageView ivTopDown;
    @BindView(R.id.sellers_detail_back)
    ImageView ivBack;
    private Map<String, String> parms;

    private LinearLayoutManager lm;
    private LinearLayoutManager lms;
    private boolean flag = true;

    private String id = "";

    private List<RankMainBean.Message.Content> datas;
    private List<RankLastBean.Message.Content> rankLasts;
    private RankAdapter adapter;
    private RankLastAdapter lastAdapter;
    private String regiontype ="";
    private String datatype = "";
    private String areatype ="";
    private String ranktype = "";//初始为aqis
    private boolean isClick = true;

    @Override
    public int intiLayout() {
        return R.layout.fragment_sellers;
    }

    @Override
    public void initView() {
        setActivityName("排名详情列表");
        llBtn.setVisibility(View.GONE);
        tvSwitch.setVisibility(View.GONE);
        llTitle.setVisibility(View.GONE);
        ivBack.setVisibility(View.VISIBLE);
        id = getIntent().getStringExtra("item");
        regiontype = getIntent().getStringExtra("regiontype");
        datatype = getIntent().getStringExtra("datatype");
        ranktype = getIntent().getStringExtra("ranktype");
        areatype  =getIntent().getStringExtra("areatype");
        setTextColor();
        if("month".equals(datatype)||"year".equals(datatype)){
            tvIndex.setTextColor(getResources().getColor(R.color.color_white));
            tvIndex.setText("综指");
            tvTitleType.setText("综指");
        }else{
            tvIndex.setTextColor(getResources().getColor(R.color.color_white));
            tvIndex.setText("AQI");
            tvTitleType.setText("AQI");
        }
        tvUnit.setText("ug/m3");
    }

    @Override
    public void initData() {
        initLastAdapter();
        intitAdapter();
        getData();
    }

    private void getData() {
        if (parms == null) {
            parms = new HashMap<>();
        }
        parms.put("key", "6DlLqAyx3mY=");
        parms.put("ranktype", ranktype);
        parms.put("regiontype", regiontype);
        parms.put("datatype", datatype);
        parms.put("ranktype", ranktype);
        parms.put("areatype", areatype);
        parms.put("areaid", id);
        if ("month".equals(datatype)||"year".equals(datatype)) {
            recyclerView.setVisibility(View.GONE);
            recviewMonth.setVisibility(View.VISIBLE);
            RankDAL.getInstance().getRankLast(parms, this);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            recviewMonth.setVisibility(View.GONE);
            RankDAL.getInstance().getRank(parms, this);
        }
    }

    @OnClick({R.id.now_ll, R.id.last_ll, R.id.yesterday_ll, R.id.all_ll, R.id.sellers_swicth_tv
            , R.id.sellers_o_3, R.id.index_all_tv, R.id.sellers_pm_25, R.id.sellers_pm_10, R.id.sellers_so_2,
            R.id.sellers_co_tv, R.id.sellers_no_2, R.id.sellers_sort_ll, R.id.seller_one_rb, R.id.seller_two_rb
            , R.id.year_ll, R.id.seller_four_rb, R.id.seller_three_rb,R.id.sellers_detail_back})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.sellers_sort_ll:
                if (flag) {
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
                if (!llLast.isSelected()) {
                    ranktype = "AQI";
                    tvTitleType.setText("AQI");
                } else {
                    ranktype = "ZH";
                    tvTitleType.setText("综指");
                }
                tvUnit.setText("ug/m3");
                getData();
                break;
            case R.id.sellers_pm_25:
                setTextColor();
                tv25.setTextColor(getResources().getColor(R.color.color_white));
                tvPM2.setTextColor(getResources().getColor(R.color.color_white));
                ranktype = "PM25";
                tvTitleType.setText("PM25");
                tvUnit.setText("ug/m3");
                getData();
                break;
            case R.id.sellers_pm_10:
                setTextColor();
                tv10.setTextColor(getResources().getColor(R.color.color_white));
                tvPM10.setTextColor(getResources().getColor(R.color.color_white));
                ranktype = "PM10";
                tvTitleType.setText("PM10");
                tvUnit.setText("ug/m3");
                getData();
                break;
            case R.id.sellers_so_2:
                setTextColor();
                tvSO.setTextColor(getResources().getColor(R.color.color_white));
                tvSO2.setTextColor(getResources().getColor(R.color.color_white));
                ranktype = "SO2";
                tvTitleType.setText("SO2");
                tvUnit.setText("ug/m3");
                getData();
                break;
            case R.id.sellers_co_tv:
                setTextColor();
                tvCO.setTextColor(getResources().getColor(R.color.color_white));
                ranktype = "CO";
                tvTitleType.setText("CO");
                tvUnit.setText("mg/m3");
                getData();
                break;
            case R.id.sellers_no_2:
                setTextColor();
                tvNO.setTextColor(getResources().getColor(R.color.color_white));
                tv2.setTextColor(getResources().getColor(R.color.color_white));
                ranktype = "NO2";
                tvTitleType.setText("NO2");
                tvUnit.setText("ug/m3");
                getData();
                break;
            case R.id.sellers_detail_back:
                finish();
                break;
        }
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

                datas.addAll(rankMainBean.getMessages().getContents());
                if("realsum".equals(datatype)){
                    tvTime.setText(TimeString.switchAllTimes(
                            rankMainBean.getMessages().getTime()));
                }else if("day".equals(datatype)){
                    tvTime.setText(TimeString.switchTime(rankMainBean.getMessages().getTime()));
                }else if("real".equals(datatype)){
                    tvTime.setText(TimeString.switchAllTimes(
                            rankMainBean.getMessages().getTime()));
                }
                adapter.notifyDataSetChanged();
            }
        } else if (mData.getType().equals(MDataType.RANK_LAST)) {
            RankLastBean rankLastBean = (RankLastBean) mData.getData();
            if (rankLastBean != null) {
                if (rankLasts.size() > 0 || datas.size() > 0) {
                    rankLasts.clear();
                }
                rankLasts.addAll(rankLastBean.getMessages().getContents());
                tvTime.setText(TimeString.switchYearTimes(
                        rankLastBean.getMessages().getTime()));
                lastAdapter.notifyDataSetChanged();
            }
        }
    }

    private void initLastAdapter() {
        lms = new LinearLayoutManager(SellersDetailActivity.this);
        lms.setOrientation(LinearLayoutManager.VERTICAL);
        recviewMonth.setLayoutManager(lms);

        rankLasts = new ArrayList<>();
        lastAdapter = new RankLastAdapter(rankLasts);
        recviewMonth.setAdapter(lastAdapter);
    }

    private void intitAdapter() {
        lm = new LinearLayoutManager(SellersDetailActivity.this);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(lm);
        datas = new ArrayList<>();
        adapter = new RankAdapter(datas);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onError(String msg, String eCode) {

    }
}
