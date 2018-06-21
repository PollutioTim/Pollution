package com.tim.pollution.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.tim.pollution.MyApplication;
import com.tim.pollution.R;
import com.tim.pollution.general.BaseActivity;
import com.tim.pollution.utils.niorgai.StatusBarCompat;

import butterknife.BindView;


/**
 * Created by lenovo on 2018/4/19.
 */

public class AboutUsActivity extends BaseActivity {
    @BindView(R.id.appbar)
    AppBarLayout appBarLayout;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.versionname_tv)
    TextView tvVersionName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityName("关于我们");
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_about_us;
    }

    @Override
    public void initView() {
        collapsingToolbarLayout.setTitle(getResources().getString(R.string.about_us));
        tvVersionName.setText("山西省空气质量发布VERSION"+ MyApplication.getInstance().getVersionName());
    }

    @Override
    public void initData() {

    }
}
