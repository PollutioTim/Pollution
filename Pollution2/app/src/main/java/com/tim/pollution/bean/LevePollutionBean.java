package com.tim.pollution.bean;

import java.io.Serializable;

/**
 * Created by lenovo on 2018/4/24.
 */

public class LevePollutionBean implements Serializable {
//    "name": "优",
//            "color": "#00E400"
    private String name;
    private String color;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
