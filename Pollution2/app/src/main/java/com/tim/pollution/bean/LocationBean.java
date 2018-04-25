package com.tim.pollution.bean;

import com.baidu.location.BDLocation;
import com.tim.pollution.utils.LocationUtil;

/**
 * Created by lenovo on 2018/4/23.
 */

public class LocationBean extends StateCode {

    private BDLocation location;

    public void setLocation(BDLocation location) {
        this.location = location;
    }
}
