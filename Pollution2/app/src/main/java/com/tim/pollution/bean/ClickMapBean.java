package com.tim.pollution.bean;

import com.google.gson.annotations.SerializedName;
import com.tim.pollution.R;

import java.io.Serializable;

/**
 * Created by lenovo on 2018/4/29.
 */

public class ClickMapBean extends StateCode implements Serializable {
//    "RegionId": "140101",
//            "RegionName": "太原市",
//            "PointLongitude": "112.555135",
//            "PointLatitude": "37.876817",
//            "AQI": "133",
//            "AQIcolor": "#FF7E00"

    @SerializedName("RegionId")
    private String regionId;
    @SerializedName("RegionName")
    private String regionName;
    @SerializedName("PointLongitude")
    private String pointLongitude;
    @SerializedName("PointLatitude")
    private String pointLatitude;
    @SerializedName("AQI")
    private String AQI;
    @SerializedName("AQIcolor")
    private String AQIColor;

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getPointLongitude() {
        return pointLongitude;
    }

    public void setPointLongitude(String pointLongitude) {
        this.pointLongitude = pointLongitude;
    }

    public String getPointLatitude() {
        return pointLatitude;
    }

    public void setPointLatitude(String pointLatitude) {
        this.pointLatitude = pointLatitude;
    }

    public String getAQI() {
        return AQI;
    }

    public void setAQI(String AQI) {
        this.AQI = AQI;
    }

    public String getAQIColor() {
        return AQIColor;
    }

    public void setAQIColor(String AQIColor) {
        this.AQIColor = AQIColor;
    }
}
