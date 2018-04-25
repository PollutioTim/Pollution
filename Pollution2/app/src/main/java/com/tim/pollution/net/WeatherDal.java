package com.tim.pollution.net;

import android.util.Log;

import com.google.gson.Gson;
import com.tim.pollution.bean.MapBean;
import com.tim.pollution.bean.RegionWeather;
import com.tim.pollution.bean.StateCode;
import com.tim.pollution.bean.weather.AQI24hBean;
import com.tim.pollution.callback.ICallBack;
import com.tim.pollution.general.MData;
import com.tim.pollution.general.MDataType;
import com.tim.pollution.general.okhttp.OkHttpUtils;
import com.tim.pollution.general.okhttp.builder.PostFormBuilder;
import com.tim.pollution.general.okhttp.callback.StringCallback;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by tcy on 2018/4/25.
 */

public class WeatherDal {
    private static String weatherUal;

    static {
        weatherUal = "http://218.26.106.43:10009/AppInterface/HomeData";

    }

    //唯一实例
    private static WeatherDal instance;

    //私有构造函数
    private WeatherDal() {
    }

    /**
     * 单例模式中获取唯一的MyApplication实例
     *
     * @return
     */
    public static WeatherDal getInstance() {
        if (null == instance) {
            instance = new WeatherDal();
        }
        return instance;
    }

    /**
     * http:// 218.26.106.43:10009/AppInterface/HomeData?key=6DlLqAyx3mY=&regionid=140101
     * @param params
     * @param callBack
     */
    public void getHomeData(Map<String ,String> params, final ICallBack callBack){
        PostFormBuilder postFormBuilder = OkHttpUtils.post().url(weatherUal).params(params);
        postFormBuilder.build().connTimeOut(20*1000)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("test","response:"+response);
                        StateCode code = new Gson().fromJson(response.toString(),StateCode.class);
                        if(1 == code.getCode()){


                            RegionWeather regionWeather=new Gson().fromJson(response.toString(),RegionWeather.class);
                            MData<RegionWeather>mData  = new MData<RegionWeather>();
                            mData.setType(MDataType.REGION_WEATHER);
                            mData.setData(regionWeather);
                            callBack.onProgress(mData);
                        }else{
                            callBack.onError("服务器异常",code.getCode()+"");
                        }
                    }
                });
    }


}
