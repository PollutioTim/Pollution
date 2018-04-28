package com.tim.pollution.bean.weather;

import java.io.Serializable;

/**
 * Created by tcy on 2018/4/25.
 */

public class PointAQIBean implements Serializable {
    /**
     * PointCode : 140201251
     * PointName : 果树场
     * time : 2018-04-10 10:00:00
     * PointType : 国控
     * AQI : 151
     * AQIcolor : #FF0000
     */

    private String PointCode;
    private String PointName;
    private String time;
    private String PointType;
    private String AQI;
    private String AQIcolor;

    public String getPointCode() {
        return PointCode;
    }

    public void setPointCode(String PointCode) {
        this.PointCode = PointCode;
    }

    public String getPointName() {
        return PointName;
    }

    public void setPointName(String PointName) {
        this.PointName = PointName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPointType() {
        return PointType;
    }

    public void setPointType(String PointType) {
        this.PointType = PointType;
    }

    public String getAQI() {
        return AQI;
    }

    public void setAQI(String AQI) {
        this.AQI = AQI;
    }

    public String getAQIcolor() {
        return AQIcolor;
    }

    public void setAQIcolor(String AQIcolor) {
        this.AQIcolor = AQIcolor;
    }
}
