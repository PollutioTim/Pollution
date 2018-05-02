package com.tim.pollution.general;

import com.baidu.mapapi.model.LatLng;

/**
 * Created by lili on 2018/4/29.
 */

public class MessageEvent {

    private LocationData message;

    public MessageEvent(LocationData data ) {
        this.message = data;
    }

    public LocationData getMessage() {
        return message;
    }

    public void setMessage(LocationData message) {
        this.message = message;
    }
}
