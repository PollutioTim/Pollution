package com.tim.pollution.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by lenovo on 2018/5/2.
 */

public class PointBean extends StateCode implements Serializable {
//    "time": "2018-04-16 12:00:00",
//            "value": "58",
//            "valuecolor": "#00E400"
    private String time;
    @SerializedName("value")
    private String value;
    @SerializedName("valuecolor")
    private String valueColor;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValueColor() {
        return valueColor;
    }

    public void setValueColor(String valueColor) {
        this.valueColor = valueColor;
    }
}
