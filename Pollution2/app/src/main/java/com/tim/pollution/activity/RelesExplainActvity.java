package com.tim.pollution.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tim.pollution.R;
import com.tim.pollution.general.BaseActivity;
import com.tim.pollution.utils.niorgai.StatusBarCompat;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by lenovo on 2018/4/19.
 */

public class RelesExplainActvity extends BaseActivity {
    @BindView(R.id.comment_back_iv)
    ImageView ivBack;
    @BindView(R.id.comment_title_tv)
    TextView tvTitle;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_relesexplain;
    }

    @Override
    public void initView() {
        setActivityName("发布说明");
        tvTitle.setText(getResources().getString(R.string.release_title));
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
