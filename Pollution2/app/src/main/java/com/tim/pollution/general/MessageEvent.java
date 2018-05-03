package com.tim.pollution.general;

import com.baidu.mapapi.model.LatLng;
import com.tim.pollution.bean.LevePollutionBean;
import com.tim.pollution.bean.MapBean;

import java.util.List;

/**
 * Created by lili on 2018/4/29.
 */

public class MessageEvent {

    private LocationData message;
    private List<LevePollutionBean> LevePollutionBeans;

    public MessageEvent(LocationData data ) {
        this.message = data;
    }

    public MessageEvent (List<LevePollutionBean> LevePollutionBeans){
        this.LevePollutionBeans = LevePollutionBeans;
    }
    public List<LevePollutionBean> getLevePollutionBeans() {
        return LevePollutionBeans;
    }

    public void setLevePollutionBeans(List<LevePollutionBean> levePollutionBeans) {
        LevePollutionBeans = levePollutionBeans;
    }

    public LocationData getMessage() {
        return message;
    }

    public void setMessage(LocationData message) {
        this.message = message;
    }
}
