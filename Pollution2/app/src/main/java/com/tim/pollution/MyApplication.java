package com.tim.pollution;

import android.app.Application;
import android.content.Context;

import com.tim.pollution.general.SharedPreferencesHelper;

/**
 * name 项目名称
 */
public class MyApplication extends Application {
    public static MyApplication instance;
    private static Context mContext;
    private SharedPreferencesHelper myPreferences;

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
    }

    /**
     * 获取全局的context
     */
    public static Context getContext() {
        return mContext;
    }

}
