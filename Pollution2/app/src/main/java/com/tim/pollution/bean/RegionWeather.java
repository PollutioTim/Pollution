package com.tim.pollution.bean;

import com.google.gson.annotations.SerializedName;
import com.tim.pollution.bean.weather.MessageBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tcy on 2018/4/25.
 */

public class RegionWeather extends StateCode implements Serializable {
    /**
     * status : 1
     * message : {"RegionList":{"RegionId":"140201","RegionName":"大同市","time":"2018-04-10 10:00:00","AQI":"156","AQIcolor":"#FF0000","SO2":"9","SO2color":"#00E400","NO2":"8","NO2color":"#00E400","PM10":"262","PM10color":"#FF0000","CO":"0.5","COcolor":"#00E400","O3":"85","O3color":"#00E400","PM25":"58","PM25color":"#FFFF00","PollutionLevel":"中度污染","TopPollution":"PM10","HealthEffect":"健康影响：进一步加剧易感人群症状，可能对健康人群心脏、呼吸系统有影响","Advice":"建议措施：儿童、老年人及心脏病、呼吸系统疾病患者避免长时间、高强度的户外锻炼，一般人群适量减少户外活动","weather":"晴","weathercode":"100","wind":"东南风 3-4级","temperature":"23","humidity":"30","Ranking":"32"},"AQI_24h":[{"time":"2018-04-09 11:00:00","AQI":"113","AQIcolor":"#FF7E00"},{"time":"2018-04-09 12:00:00","AQI":"102","AQIcolor":"#FF7E00"},{"time":"2018-04-09 13:00:00","AQI":"88","AQIcolor":"#FFFF00"},{"time":"2018-04-09 14:00:00","AQI":"84","AQIcolor":"#FFFF00"},{"time":"2018-04-09 15:00:00","AQI":"83","AQIcolor":"#FFFF00"},{"time":"2018-04-09 16:00:00","AQI":"112","AQIcolor":"#FF7E00"},{"time":"2018-04-09 17:00:00","AQI":"126","AQIcolor":"#FF7E00"},{"time":"2018-04-09 18:00:00","AQI":"146","AQIcolor":"#FF7E00"},{"time":"2018-04-09 19:00:00","AQI":"150","AQIcolor":"#FF7E00"},{"time":"2018-04-09 20:00:00","AQI":"158","AQIcolor":"#FF0000"},{"time":"2018-04-09 21:00:00","AQI":"161","AQIcolor":"#FF0000"},{"time":"2018-04-09 22:00:00","AQI":"165","AQIcolor":"#FF0000"},{"time":"2018-04-09 23:00:00","AQI":"243","AQIcolor":"#99004C"},{"time":"2018-04-10 00:00:00","AQI":"388","AQIcolor":"#7E0023"},{"time":"2018-04-10 01:00:00","AQI":"496","AQIcolor":"#7E0023"},{"time":"2018-04-10 02:00:00","AQI":"500","AQIcolor":"#7E0023"},{"time":"2018-04-10 03:00:00","AQI":"500","AQIcolor":"#7E0023"},{"time":"2018-04-10 04:00:00","AQI":"500","AQIcolor":"#7E0023"},{"time":"2018-04-10 05:00:00","AQI":"500","AQIcolor":"#7E0023"},{"time":"2018-04-10 06:00:00","AQI":"500","AQIcolor":"#7E0023"},{"time":"2018-04-10 07:00:00","AQI":"419","AQIcolor":"#7E0023"},{"time":"2018-04-10 08:00:00","AQI":"154","AQIcolor":"#FF0000"},{"time":"2018-04-10 09:00:00","AQI":"140","AQIcolor":"#FF7E00"},{"time":"2018-04-10 10:00:00","AQI":"156","AQIcolor":"#FF0000"}],"Point_AQI":[{"PointCode":"140201251","PointName":"果树场","time":"2018-04-10 10:00:00","PointType":"国控","AQI":"151","AQIcolor":"#FF0000"},{"PointCode":"140201253","PointName":"云冈宾馆","time":"2018-04-10 10:00:00","PointType":"国控","AQI":"172","AQIcolor":"#FF0000"},{"PointCode":"140201254","PointName":"大同大学","time":"2018-04-10 10:00:00","PointType":"国控","AQI":"156","AQIcolor":"#FF0000"},{"PointCode":"140201255","PointName":"安家小村","time":"2018-04-10 10:00:00","PointType":"国控","AQI":"145","AQIcolor":"#FF7E00"},{"PointCode":"140201256","PointName":"教育学院","time":"2018-04-10 10:00:00","PointType":"国控","AQI":"164","AQIcolor":"#FF0000"},{"PointCode":"140201257","PointName":"供排水公司","time":"2018-04-10 10:00:00","PointType":"国控","AQI":"150","AQIcolor":"#FF7E00"}]}
     */
    @SerializedName("message")
    private MessageBean message;

    public MessageBean getMessage() {
        return message;
    }

    public void setMessage(MessageBean message) {
        this.message = message;
    }


}
