package com.tim.pollution.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by lenovo on 2018/4/25.
 */

public class RankBean extends StateCode implements Serializable {
//    "ranking": "1",
//            "name": "五台县",
//            "toppoll": "无",
//            "value": "16",
//            "valuecolor": "#00E400"

    private String ranking;
    private String name;
    private String toppoll;
    private String value;
    @SerializedName("valuecolor")
    private String valueColor;

    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToppoll() {
        return toppoll;
    }

    public void setToppoll(String toppoll) {
        this.toppoll = toppoll;
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
