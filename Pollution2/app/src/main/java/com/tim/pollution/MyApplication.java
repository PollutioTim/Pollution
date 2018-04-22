package com.tim.pollution;

import android.app.Application;
import android.content.Context;

/**
 * name 项目名称
 * log 是否打印
 * initialize 是否网络初始化
 * width 适配宽度
 * scale 适配字体倍数
 */
public class MyApplication extends Application {
    public static MyApplication instance;
    private static Context mContext;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mContext = getApplicationContext();
    }

    /**
     * 获取全局的context
     */
    public static Context getContext() {
        return mContext;
    }

}
