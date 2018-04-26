package com.tim.pollution.bean.changetrend;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by tcy on 2018/4/26.
 */

public class ChangeTrendMessageBean {

    @SerializedName("AQI_data")
    private List<DataInfoBean> AQI_data;
    @SerializedName("SO2_data")
    private List<DataInfoBean> SO2_data;
    @SerializedName("NO2_data")
    private List<DataInfoBean> NO2_data;
    @SerializedName("PM10_data")
    private List<DataInfoBean> PM10_data;
    @SerializedName("CO_data")
    private List<DataInfoBean> CO_data;
    @SerializedName("O3_data")
    private List<DataInfoBean> O3_data;
    @SerializedName("PM25_data")
    private List<DataInfoBean> PM25_data;

    public List<DataInfoBean> getAQI_data() {
        return AQI_data;
    }

    public void setAQI_data(List<DataInfoBean> AQI_data) {
        this.AQI_data = AQI_data;
    }

    public List<DataInfoBean> getSO2_data() {
        return SO2_data;
    }

    public void setSO2_data(List<DataInfoBean> SO2_data) {
        this.SO2_data = SO2_data;
    }

    public List<DataInfoBean> getNO2_data() {
        return NO2_data;
    }

    public void setNO2_data(List<DataInfoBean> NO2_data) {
        this.NO2_data = NO2_data;
    }

    public List<DataInfoBean> getPM10_data() {
        return PM10_data;
    }

    public void setPM10_data(List<DataInfoBean> PM10_data) {
        this.PM10_data = PM10_data;
    }

    public List<DataInfoBean> getCO_data() {
        return CO_data;
    }

    public void setCO_data(List<DataInfoBean> CO_data) {
        this.CO_data = CO_data;
    }

    public List<DataInfoBean> getO3_data() {
        return O3_data;
    }

    public void setO3_data(List<DataInfoBean> o3_data) {
        O3_data = o3_data;
    }

    public List<DataInfoBean> getPM25_data() {
        return PM25_data;
    }

    public void setPM25_data(List<DataInfoBean> PM25_data) {
        this.PM25_data = PM25_data;
    }
}
