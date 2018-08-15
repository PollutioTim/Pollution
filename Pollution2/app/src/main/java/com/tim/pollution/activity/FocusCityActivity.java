package com.tim.pollution.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tim.pollution.R;
import com.tim.pollution.adapter.FocusCityWithClassAdapter;
import com.tim.pollution.bean.changetrend.RegionNetBean;
import com.tim.pollution.callback.ICallBack;
import com.tim.pollution.general.BaseActivity;
import com.tim.pollution.general.Constants;
import com.tim.pollution.general.MData;
import com.tim.pollution.general.MDataType;
import com.tim.pollution.net.WeatherDal;
import com.tim.pollution.utils.CityListSaveUtil;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

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
    @BindView(R.id.city_focus)
    TextView tvFocus;
    private List<RegionNetBean.RegionBean>cityBeens;

    private FocusCityWithClassAdapter adapter;
    private RegionNetBean regionNetBean;
    private Map<String,String> parms;
    private AlertDialog dialogAgain;
    private StickyRecyclerHeadersDecoration headersDecor;
    private ProgressDialog pd;
    private String state;//状态：01 首页  02 选择城市

    @Override
    public int intiLayout() {
        return R.layout.activity_city;
    }

    @Override
    public void initView() {
        state=getIntent().getStringExtra("state");
        if("02".equals(state)){
            tvFocus.setVisibility(View.GONE);
        }
        setActivityName("城市列表");
        cityBeens = new ArrayList<>();
        tvFocus.setVisibility(View.VISIBLE);
        adapter = new FocusCityWithClassAdapter(this,cityBeens,state);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        pd = ProgressDialog.show(this, "提示", "加载数据中，请耐心等待......");
        loadData("area",null);
        tvFocus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    List<String> regions=((FocusCityWithClassAdapter)recyclerView.getAdapter()).getFocusCity();
                    CityListSaveUtil.putList(FocusCityActivity.this,CityListSaveUtil.CITY_FILE, CityListSaveUtil.CITY_KEY,regions);
                }catch (Exception e){
                    e.printStackTrace();
                }
                setResult(RESULT_OK);
                FocusCityActivity.this.finish();
            }
        });
    }
    int size =0;
    int index=0;
    private void  loadData(String type, final String id){//http://218.26.106.43:10009/AppInterface/Region?key=6DlLqAyx3mY=&regiontype=area
        parms=new HashMap<>();
        parms.put("key", Constants.key);
        parms.put("regiontype",type);
        if(id!=null){
            parms.put("regionid",id);
        }
        WeatherDal.getInstance().getRegion(parms, new ICallBack() {
            @Override
            public void onSuccess(Object data) {
                MData mData = (MData) data;
                if (MDataType.REGIONNET_BEAN.equals(mData.getType())) {
                    if(id==null){
                        regionNetBean = (RegionNetBean) mData.getData();
                        //            regionNetBean = getTest();
                        if (regionNetBean != null) {
                            size=regionNetBean.getMessage().size();
                            loadData("singlecity",regionNetBean.getMessage().get(index).getRegionId());
                            for(RegionNetBean.RegionBean regionBean:regionNetBean.getMessage()){
                                regionBean.setClass(true);
                            }
                            cityBeens.addAll(regionNetBean.getMessage());
                        }
                    }else{
                        if(regionNetBean!=null){
                            regionNetBean.getMessage().get(index).setRegionBeens(((RegionNetBean)mData.getData()).getMessage());
                            if(index==size-1){
                                cityBeens.clear();
                                cityBeens.addAll(regionNetBean.getMessage());
                                recyclerView.setAdapter(new FocusCityWithClassAdapter(FocusCityActivity.this,cityBeens,state));
                                if(pd!=null){
                                    pd.dismiss();
                                }
                                return;
                            }
                            index++;
                            loadData("singlecity",regionNetBean.getMessage().get(index).getRegionId());
                        }
                    }
                }
            }

            @Override
            public void onError(String msg, String eCode) {
                index++;
                if(index>=size){
                    cityBeens.clear();
                    cityBeens.addAll(regionNetBean.getMessage());
                    recyclerView.setAdapter(new FocusCityWithClassAdapter(FocusCityActivity.this,cityBeens,state));
                    if(pd!=null){
                        pd.dismiss();
                    }
                }
                Toast.makeText(FocusCityActivity.this, msg, Toast.LENGTH_LONG).show();
                showAgainDailog(msg);
            }
        });
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


    }

    @Override
    public void onError(String msg, String eCode) {

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
