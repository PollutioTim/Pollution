package com.tim.pollution.activity;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.tim.pollution.R;
import com.tim.pollution.bean.changetrend.DataBankNetBean;
import com.tim.pollution.bean.changetrend.DataInfoBean;
import com.tim.pollution.callback.ICallBack;
import com.tim.pollution.general.Constants;
import com.tim.pollution.net.WeatherDal;
import com.tim.pollution.view.WrapContentListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import lecho.lib.hellocharts.view.ColumnChartView;

public class CityContrastActivity extends AppCompatActivity implements ICallBack{

    @Bind(R.id.city_contrast_sp1)
    Spinner cityContrastSp1;
    @Bind(R.id.city_contrast_sp2)
    Spinner cityContrastSp2;
    @Bind(R.id.city_contrast_sp3)
    Spinner cityContrastSp3;
    @Bind(R.id.city_contrast_chart)
    ColumnChartView cityContrastChart;
    @Bind(R.id.city_contrast_info)
    TextView cityContrastInfo;
    @Bind(R.id.city_contrast_rbpm25_type)
    RadioButton cityContrastRbpm25Type;
    @Bind(R.id.city_contrast_rbpm10_type)
    RadioButton cityContrastRbpm10Type;
    @Bind(R.id.city_contrast_rbso2_type)
    RadioButton cityContrastRbso2Type;
    @Bind(R.id.city_contrast_rbno2_type)
    RadioButton cityContrastRbno2Type;
    @Bind(R.id.city_contrast_rbo3_type)
    RadioButton cityContrastRbo3Type;
    @Bind(R.id.city_contrast_rbco_type)
    RadioButton cityContrastRbcoType;
    @Bind(R.id.city_contrast_rg_type)
    RadioGroup cityContrastRgType;
    @Bind(R.id.city_contrast_list)
    WrapContentListView cityContrastList;

    DataBankNetBean dataBankNetBean;
    private String pointtype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_contrast);
        ButterKnife.bind(this);
        pointtype=getIntent().getStringExtra("regiontype");
        loadData();
        cityContrastRgType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                loadData();
            }
        });
    }

    private String datatype="PM25";
    private String ranktype="real";
    private void loadData() {
        int id = cityContrastRgType.getCheckedRadioButtonId();
        if (id == R.id.weather_detail_rbpm25_type) {
            datatype = "PM25";
        } else if (id == R.id.weather_detail_rbpm10_type) {
            datatype = "PM10";
        } else if (id == R.id.weather_detail_rbso2_type) {
            datatype = "SO2";
        } else if (id == R.id.weather_detail_rbno2_type) {
            datatype = "NO2";
        } else if (id == R.id.weather_detail_rbo3_type) {
            datatype = "O3";
        } else if (id == R.id.weather_detail_rbco_type) {
            datatype = "CO";
        }
        Map<String ,String>params=new HashMap<>();
//        params.put("key", Constants.key);
//        params.put("regiontype",regiontype);
//        params.put("datatype",datatype);
//        params.put("ranktype",);
//        params.put("pointtype",pointtype);

        WeatherDal.getInstance().getDataRank(params,this);
    }


    @Override
    public void onProgress(Object data) {

    }

    @Override
    public void onError(String msg, String eCode) {

    }
}
