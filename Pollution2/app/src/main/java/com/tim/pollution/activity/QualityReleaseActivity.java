package com.tim.pollution.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tim.pollution.R;
import com.tim.pollution.general.BaseActivity;
import com.tim.pollution.utils.niorgai.StatusBarCompat;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by lenovo on 2018/4/19.
 */

public class QualityReleaseActivity extends BaseActivity {

    @Bind(R.id.comment_back_iv)
    ImageView ivBack;
    @Bind(R.id.comment_title_tv)
    TextView tvTitle;

    @Override
    public int intiLayout() {
        return R.layout.activity_quality_release;
    }

    @Override
    public void initView() {
        tvTitle.setText(getResources().getString(R.string.quity_release_title));
        StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.color_white), 0);
    }

    @Override
    public void initData() {

    }
    @OnClick({R.id.comment_back_iv})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.comment_back_iv:
                finish();
                break;
        }
    }
}
