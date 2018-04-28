package com.tim.pollution.bean.weather;

import java.io.Serializable;

/**
 * Created by tcy on 2018/4/25.
 */

public class RegionListBean implements Serializable {
    /**
     * RegionId : 140201
     * RegionName : 大同市
     * time : 2018-04-10 10:00:00
     * AQI : 156
     * AQIcolor : #FF0000
     * SO2 : 9
     * SO2color : #00E400
     * NO2 : 8
     * NO2color : #00E400
     * PM10 : 262
     * PM10color : #FF0000
     * CO : 0.5
     * COcolor : #00E400
     * O3 : 85
     * O3color : #00E400
     * PM25 : 58
     * PM25color : #FFFF00
     * PollutionLevel : 中度污染
     * TopPollution : PM10
     * HealthEffect : 健康影响：进一步加剧易感人群症状，可能对健康人群心脏、呼吸系统有影响
     * Advice : 建议措施：儿童、老年人及心脏病、呼吸系统疾病患者避免长时间、高强度的户外锻炼，一般人群适量减少户外活动
     * weather : 晴
     * weathercode : 100
     * wind : 东南风 3-4级
     * temperature : 23
     * humidity : 30
     * Ranking : 32
     */

    private String RegionId;
    private String RegionName;
    private String time;
    private String AQI;
    private String AQIcolor;
    private String SO2;
    private String SO2color;
    private String NO2;
    private String NO2color;
    private String PM10;
    private String PM10color;
    private String CO;
    private String COcolor;
    private String O3;
    private String O3color;
    private String PM25;
    private String PM25color;
    private String PollutionLevel;
    private String TopPollution;
    private String HealthEffect;
    private String Advice;
    private String weather;
    private String weathercode;
    private String wind;
    private String temperature;
    private String humidity;
    private String Ranking;

    public String getRegionId() {
        return RegionId;
    }

    public void setRegionId(String RegionId) {
        this.RegionId = RegionId;
    }

    public String getRegionName() {
        return RegionName;
    }

    public void setRegionName(String RegionName) {
        this.RegionName = RegionName;
    }

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

    public String getSO2() {
        return SO2;
    }

    public void setSO2(String SO2) {
        this.SO2 = SO2;
    }

    public String getSO2color() {
        return SO2color;
    }

    public void setSO2color(String SO2color) {
        this.SO2color = SO2color;
    }

    public String getNO2() {
        return NO2;
    }

    public void setNO2(String NO2) {
        this.NO2 = NO2;
    }

    public String getNO2color() {
        return NO2color;
    }

    public void setNO2color(String NO2color) {
        this.NO2color = NO2color;
    }

    public String getPM10() {
        return PM10;
    }

    public void setPM10(String PM10) {
        this.PM10 = PM10;
    }

    public String getPM10color() {
        return PM10color;
    }

    public void setPM10color(String PM10color) {
        this.PM10color = PM10color;
    }

    public String getCO() {
        return CO;
    }

    public void setCO(String CO) {
        this.CO = CO;
    }

    public String getCOcolor() {
        return COcolor;
    }

    public void setCOcolor(String COcolor) {
        this.COcolor = COcolor;
    }

    public String getO3() {
        return O3;
    }

    public void setO3(String O3) {
        this.O3 = O3;
    }

    public String getO3color() {
        return O3color;
    }

    public void setO3color(String O3color) {
        this.O3color = O3color;
    }

    public String getPM25() {
        return PM25;
    }

    public void setPM25(String PM25) {
        this.PM25 = PM25;
    }

    public String getPM25color() {
        return PM25color;
    }

    public void setPM25color(String PM25color) {
        this.PM25color = PM25color;
    }

    public String getPollutionLevel() {
        return PollutionLevel;
    }

    public void setPollutionLevel(String PollutionLevel) {
        this.PollutionLevel = PollutionLevel;
    }

    public String getTopPollution() {
        return TopPollution;
    }

    public void setTopPollution(String TopPollution) {
        this.TopPollution = TopPollution;
    }

    public String getHealthEffect() {
        return HealthEffect;
    }

    public void setHealthEffect(String HealthEffect) {
        this.HealthEffect = HealthEffect;
    }

    public String getAdvice() {
        return Advice;
    }

    public void setAdvice(String Advice) {
        this.Advice = Advice;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getWeathercode() {
        return weathercode;
    }

    public void setWeathercode(String weathercode) {
        this.weathercode = weathercode;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getRanking() {
        return Ranking;
    }

    public void setRanking(String Ranking) {
        this.Ranking = Ranking;
    }
}
