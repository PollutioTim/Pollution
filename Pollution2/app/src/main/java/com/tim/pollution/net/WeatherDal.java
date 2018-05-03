package com.tim.pollution.net;

import android.util.Log;

import com.google.gson.Gson;
import com.tim.pollution.bean.MapBean;
import com.tim.pollution.bean.RegionWeather;
import com.tim.pollution.bean.StateCode;
import com.tim.pollution.bean.changetrend.ChangeTrend;
import com.tim.pollution.bean.changetrend.DataBankNetBean;
import com.tim.pollution.bean.changetrend.PointInfoNetBean;
import com.tim.pollution.bean.changetrend.RegionNetBean;
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

    private static final String changeTrendUrl;

    private static final String pointDataUrl;

    private static final String dataBankUrl;

    private static final String regionUrl;

    static {
        weatherUal = "http://218.26.106.43:10009/AppInterface/HomeData";
        changeTrendUrl="http://218.26.106.43:10009/AppInterface/PollTrend";
        pointDataUrl="http://218.26.106.43:10009/AppInterface/PointData";
        dataBankUrl="http://218.26.106.43:10009/AppInterface/DataRank";
        regionUrl ="http://218.26.106.43:10009/AppInterface/Region";

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
     * http://218.26.106.43:10009/AppInterface/HomeData?key=6DlLqAyx3mY=&regionid=140201
     * @param params
     * @param callBack
     */
    public void getHomeData(Map<String ,String> params, final ICallBack callBack){
        PostFormBuilder postFormBuilder = OkHttpUtils.post().url(weatherUal).params(params);
        postFormBuilder.build().connTimeOut(20*1000)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        callBack.onError("网络不可用",id+"");
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

    /**
     * http://218.26.106.43:10009/AppInterface/ PollTrend?key=6DlLqAyx3mY=&regionid=140000&type=12h
     * 县区污染物变化趋势
     * @param params
     * @param callBack
     * regionid	是	String	县区代码
     * type	是	String	数据类型 12h为近12小时数据  24h为近24小时数据 day为近30日数据
     */
    public void getPollTrend(Map<String ,String> params, final ICallBack callBack){
        PostFormBuilder postFormBuilder = OkHttpUtils.post().url(changeTrendUrl).params(params);
        postFormBuilder.build().connTimeOut(20*1000)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        callBack.onError("网络不可用",id+"");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("test","response:"+response);
                        StateCode code = new Gson().fromJson(response.toString(),StateCode.class);
                        if(1 == code.getCode()){
                            ChangeTrend changeTrend=new Gson().fromJson(response.toString(),ChangeTrend.class);
                            MData<ChangeTrend>mData  = new MData<ChangeTrend>();
                            mData.setType(MDataType.CHANGE_TREND);
                            mData.setData(changeTrend);
                            callBack.onProgress(mData);
                        }else{
                            callBack.onError("服务器异常",code.getCode()+"");
                        }
                    }
                });
    }

    /**
     * http://218.26.106.43:10009/AppInterface/PointData?key=6DlLqAyx3mY=&pointcode=140101
     * 站点实时评价
     * @param params
     * @param callBack
     */
    public void getPointData(Map<String ,String> params, final ICallBack callBack){
        PostFormBuilder postFormBuilder = OkHttpUtils.post().url(pointDataUrl).params(params);
        postFormBuilder.build().connTimeOut(20*1000)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        callBack.onError("网络不可用",id+"");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("test","response:"+response);
                        StateCode code = new Gson().fromJson(response.toString(),StateCode.class);
                        if(1 == code.getCode()){
                            PointInfoNetBean pointDataNet=new Gson().fromJson(response.toString(),PointInfoNetBean.class);
                            MData<PointInfoNetBean>mData  = new MData<PointInfoNetBean>();
                            mData.setType(MDataType.POINT_INFONET_BEAN);
                            mData.setData(pointDataNet);
                            callBack.onProgress(mData);
                        }else{
                            callBack.onError("服务器异常",code.getCode()+"");
                        }
                    }
                });
    }
    /**
     * http://218.26.106.43:10009/AppInterface/DataRank?key=6DlLqAyx3mY=&pointcode=140101
     * @param params
     * @param callBack
     * regiontype :区域类型 city为地级市 region为县区  point为站点
     * ranktype
     * pointtype 站点类型 S为省控站点 G为国控站点 空为全部站点 注：当区域类型为point时，必选此参数
     */
    public void getDataRank(Map<String ,String> params, final ICallBack callBack){
        PostFormBuilder postFormBuilder = OkHttpUtils.post().url(dataBankUrl).params(params);
        postFormBuilder.build().connTimeOut(20*1000)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        callBack.onError("网络不可用",id+"");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("test","response:"+response);
                        StateCode code = new Gson().fromJson(response.toString(),StateCode.class);
                        if(1 == code.getCode()){
                            DataBankNetBean dataBankNetBean=new Gson().fromJson(response.toString(),DataBankNetBean.class);
                            MData<DataBankNetBean>mData  = new MData<DataBankNetBean>();
                            mData.setType(MDataType.DATA_BANKNET_BEAN);
                            mData.setData(dataBankNetBean);
                            callBack.onProgress(mData);
                        }else{
                            callBack.onError("服务器异常",code.getCode()+"");
                        }
                    }
                });
    }

    /**
     * http://218.26.106.43:10009/AppInterface/Region?key=6DlLqAyx3mY=&regiontype=allregion
     * 县区列表
     * @param params
     * @param callBack
     */
//    http://218.26.106.43:10009/AppInterface/Region?regiontype=allregion&key=6DlLqAyx3mY=
    public void getRegion(Map<String ,String> params, final ICallBack callBack){
        PostFormBuilder postFormBuilder = OkHttpUtils.post().url(regionUrl).params(params);
        postFormBuilder.build().connTimeOut(20*1000)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        callBack.onError("网络不可用",id+"");

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("test","response:"+response);
                        StateCode code = new Gson().fromJson(response.toString(),StateCode.class);
                        if(1 == code.getCode()){
                            RegionNetBean regionNetBean=new Gson().fromJson(response.toString(),RegionNetBean.class);
                            MData<RegionNetBean>mData  = new MData<RegionNetBean>();
                            mData.setType(MDataType.REGIONNET_BEAN);
                            mData.setData(regionNetBean);
                            callBack.onProgress(mData);
                        }else{
                            callBack.onError("服务器异常",code.getCode()+"");
                        }
                    }
                });
    }


}
