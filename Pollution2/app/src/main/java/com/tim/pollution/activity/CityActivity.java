package com.tim.pollution.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.tim.pollution.R;
import com.tim.pollution.adapter.CityAdapter;
import com.tim.pollution.general.BaseActivity;

import butterknife.Bind;

/**
 * Created by lenovo on 2018/4/27.
 */

public class CityActivity extends BaseActivity {

    @Bind(R.id.city_recyview)
    RecyclerView recyclerView;
    @Bind(R.id.city_back_iv)
    ImageView ivBack;

    private CityAdapter adapter;
    private LinearLayoutManager lm;

    @Override
    public int intiLayout() {
        return R.layout.activity_city;
    }

    @Override
    public void initView() {
        lm = new LinearLayoutManager(this);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(lm);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void initData() {

    }
}
