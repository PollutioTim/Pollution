package com.tim.pollution.net;

import com.google.gson.Gson;
import com.tim.pollution.bean.MapBean;
import com.tim.pollution.bean.StateCode;
import com.tim.pollution.callback.ICallBack;
import com.tim.pollution.general.MData;
import com.tim.pollution.general.MDataType;
import com.tim.pollution.general.okhttp.OkHttpUtils;
import com.tim.pollution.general.okhttp.builder.PostFormBuilder;
import com.tim.pollution.general.okhttp.callback.StringCallback;

import java.util.Map;

import okhttp3.Call;

/**
 * Created by lenovo on 2018/4/24.
 */

public class MapDAL {
    private static String mapUrls;

    static {
        mapUrls = "http://218.26.106.43:10009/AppInterface/MapData";

    }

    //唯一实例
    private static MapDAL instance;

    //私有构造函数
    private MapDAL() {
    }

    /**
     * 单例模式中获取唯一的MyApplication实例
     *
     * @return
     */
    public static MapDAL getInstance() {
        if (null == instance) {
            instance = new MapDAL();
        }
        return instance;
    }

    public void getCityData(Map<String ,String>params, final ICallBack callBack){
        PostFormBuilder postFormBuilder = OkHttpUtils.post().url(mapUrls).params(params);
        postFormBuilder.build().connTimeOut(20*1000)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        StateCode code = new Gson().fromJson(response.toString(),StateCode.class);
                        if(1 == code.getCode()){
                            MapBean mapBean = new Gson().fromJson(response.toString(),MapBean.class);
                            MData<MapBean>mData  = new MData<MapBean>();
                            mData.setType(MDataType.MAP_DATA);
                            mData.setData(mapBean);
                            callBack.onProgress(mData);
                        }
                    }
                });
    }

}
