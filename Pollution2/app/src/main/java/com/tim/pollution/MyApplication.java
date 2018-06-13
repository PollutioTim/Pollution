package com.tim.pollution;

import android.Manifest;
import android.app.Application;
import android.content.Context;

import com.baidu.mobstat.StatService;
import com.tim.pollution.bean.LevePollutionBean;
import com.tim.pollution.general.BaseActivity;
import com.tim.pollution.general.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * name 项目名称
 */
public class MyApplication extends Application {
    public static MyApplication instance;
    private static Context mContext;
    private SharedPreferencesHelper myPreferences;
    private List<LevePollutionBean> LevePollutionBeans;

    public List<LevePollutionBean> getLevePollutionBeans() {
        return LevePollutionBeans;
    }

    public void setLevePollutionBeans(List<LevePollutionBean> levePollutionBeans) {
        LevePollutionBeans = levePollutionBeans;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mContext = getApplicationContext();
        //初始化自定首选项
        myPreferences = new SharedPreferencesHelper(mContext,"pollution");
        LevePollutionBeans = new ArrayList<>();


    }

    /**
     * 获取全局的context
     */
    public static Context getContext() {
        return mContext;
    }


}
