package com.tim.pollution.general;

import com.baidu.mapapi.model.LatLng;
import com.tim.pollution.bean.MapBean;

/**
 * Created by lenovo on 2018/4/29.
 */

public class LocationData {
    private LatLng latLng;
    private MapBean mapBean;

    public MapBean getMapBean() {
        return mapBean;
    }

    public void setMapBean(MapBean mapBean) {
        this.mapBean = mapBean;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }
}
