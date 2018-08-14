package com.tim.pollution.general;

import com.baidu.mapapi.model.LatLng;
import com.tim.pollution.bean.LevePollutionBean;
import com.tim.pollution.bean.MapBean;

import java.util.List;

/**
 * Created by lili on 2018/4/29.
 */

public class MessageEvent {

    private String  id;

    public MessageEvent ( String  id){
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
