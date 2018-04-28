package com.tim.pollution.bean.weather;

import java.io.Serializable;

/**
 * Created by tcy on 2018/4/25.
 */

public class AQI24hBean implements Serializable {
    /**
     * time : 2018-04-09 11:00:00
     * AQI : 113
     * AQIcolor : #FF7E00
     */

    private String time;
    private String AQI;
    private String AQIcolor;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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
