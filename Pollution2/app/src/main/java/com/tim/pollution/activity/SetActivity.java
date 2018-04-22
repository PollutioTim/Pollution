package com.tim.pollution.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tim.pollution.MainActivity;
import com.tim.pollution.R;
import com.tim.pollution.general.BaseActivity;
import com.tim.pollution.utils.niorgai.StatusBarCompat;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by lenovo on 2018/4/19.
 */

public class SetActivity extends BaseActivity {

    @Bind(R.id.explain_quality_tv)
    TextView tvQuality;
    @Bind(R.id.explain_release_tv)
    TextView tvRelease;
    @Bind(R.id.about_us_tv)
    TextView tvAboutUs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_setting;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.explain_quality_tv, R.id.explain_release_tv, R.id.about_us_tv})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.explain_quality_tv:
                startActivity(new Intent(this,QualityReleaseActivity.class));
                break;
            case R.id.explain_release_tv:
                startActivity(new Intent(this,RelesExplainActvity.class));
                break;
            case R.id.about_us_tv:
                startActivity(new Intent(this,AboutUsActivity.class));
                break;
        }
    }
}
