package com.tim.pollution;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

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

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    private String versionName;


    public List<LevePollutionBean> getLevePollutionBeans() {
        return LevePollutionBeans;
    }

    public void setLevePollutionBeans(List<LevePollutionBean> levePollutionBeans) {
        LevePollutionBeans = levePollutionBeans;
    }

    public static MyApplication getInstance() {
        return instance;
    }

    public static void setInstance(MyApplication instance) {
        MyApplication.instance = instance;
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
        versionName = getVersionNames();
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

    private String getVersionNames()
    {
        // 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(getPackageName(),0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = packInfo.versionName;
        return version;
    }
}
