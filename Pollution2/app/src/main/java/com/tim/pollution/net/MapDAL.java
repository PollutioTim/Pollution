package com.tim.pollution.net;

import com.google.gson.Gson;
import com.tim.pollution.bean.ClickMapListBean;
import com.tim.pollution.bean.ClickPointBean;
import com.tim.pollution.bean.MapBean;
import com.tim.pollution.bean.MapPointBean;
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
        mapUrls = "http://202.97.152.195:5004/AppInterface/MapData";

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
                            callBack.onSuccess(mData);
                        }
                    }
                });
    }

    public void getClickCityData(Map<String ,String>params, final ICallBack callBack){
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
                            ClickMapListBean mapBean = new Gson().fromJson(response.toString(),ClickMapListBean.class);
                            MData<ClickMapListBean>mData  = new MData<ClickMapListBean>();
                            mData.setType(MDataType.MAP_CLICK_DATA);
                            mData.setData(mapBean);
                            callBack.onSuccess(mData);
                        }
                    }
                });
    }

    public void getPointData(Map<String ,String>params, final ICallBack callBack){
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
                            MapPointBean mapBean = new Gson().fromJson(response.toString(),
                                    MapPointBean.class);
                            MData<MapPointBean>mData  = new MData<MapPointBean>();
                            mData.setType(MDataType.MAP_POINT_DATA);
                            mData.setData(mapBean);
                            callBack.onSuccess(mData);
                        }
                    }
                });
    }
    //暂时不用
    public void getPointClickData(Map<String ,String>params, final ICallBack callBack){
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
                            ClickPointBean mapBean = new Gson().fromJson(response.toString(),
                                    ClickPointBean.class);
                            MData<ClickPointBean>mData  = new MData<ClickPointBean>();
                            mData.setType(MDataType.MAP_POINT_CLICK_DATA);
                            mData.setData(mapBean);
                            callBack.onSuccess(mData);
                        }
                    }
                });
    }
}
