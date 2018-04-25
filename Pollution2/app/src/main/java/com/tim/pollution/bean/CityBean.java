package com.tim.pollution.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by lenovo on 2018/4/24.
 */

public class CityBean implements Serializable {
//    "RegionId": "140100",
//            "RegionName": "太原",
//            "AQI": "90",
//            "AQIcolor": "#FFFF00",
//            "SO2": "9",
//            "SO2color": "#00E400",
//            "NO2": "8",
//            "NO2color": "#00E400",
//            "PM10": "262",
//            "PM10color": "#FF0000",
//            "CO": "0.5",
//            "COcolor": "#00E400",
//            "O3": "85",
//            "O3color": "#00E400",
//            "PM25": "58",
//            "PM25color": "#FFFF00"
    @SerializedName("RegionId")
    private String regionId;
    @SerializedName("RegionName")
    private String regionName;
    @SerializedName("AQI")
    private String AQI;
    @SerializedName("AQIcolor")
    private String AQIColor;
    @SerializedName("SO2")
    private String SO2;
    @SerializedName("SO2color")
    private String SO2Color;
    @SerializedName("NO2")
    private String NO2;
    @SerializedName("NO2color")
    private String NO2Color;
    @SerializedName("PM10")
    private String PM10;
    @SerializedName("PM10color")
    private String PM10Color;
    @SerializedName("CO")
    private String CO;
    @SerializedName("COcolor")
    private String COColor;
    @SerializedName("O3")
    private String O3;
    @SerializedName("O3color")
    private String O3Color;
    @SerializedName("PM25")
    private String PM25;
    @SerializedName("PM25color")
    private String PM25Color;

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

    public String getSO2() {
        return SO2;
    }

    public void setSO2(String SO2) {
        this.SO2 = SO2;
    }

    public String getSO2Color() {
        return SO2Color;
    }

    public void setSO2Color(String SO2Color) {
        this.SO2Color = SO2Color;
    }

    public String getNO2() {
        return NO2;
    }

    public void setNO2(String NO2) {
        this.NO2 = NO2;
    }

    public String getNO2Color() {
        return NO2Color;
    }

    public void setNO2Color(String NO2Color) {
        this.NO2Color = NO2Color;
    }

    public String getPM10() {
        return PM10;
    }

    public void setPM10(String PM10) {
        this.PM10 = PM10;
    }

    public String getPM10Color() {
        return PM10Color;
    }

    public void setPM10Color(String PM10Color) {
        this.PM10Color = PM10Color;
    }

    public String getCO() {
        return CO;
    }

    public void setCO(String CO) {
        this.CO = CO;
    }

    public String getCOColor() {
        return COColor;
    }

    public void setCOColor(String COColor) {
        this.COColor = COColor;
    }

    public String getO3() {
        return O3;
    }

    public void setO3(String o3) {
        O3 = o3;
    }

    public String getO3Color() {
        return O3Color;
    }

    public void setO3Color(String o3Color) {
        O3Color = o3Color;
    }

    public String getPM25() {
        return PM25;
    }

    public void setPM25(String PM25) {
        this.PM25 = PM25;
    }

    public String getPM25Color() {
        return PM25Color;
    }

    public void setPM25Color(String PM25Color) {
        this.PM25Color = PM25Color;
    }
}
