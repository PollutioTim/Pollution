package com.tim.pollution.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lenovo on 2018/5/2.
 * 点击单个point时的实体类
 */

public class ClickPointBean extends StateCode implements Serializable{

    @SerializedName("message")
    private List<AllPointData>allPointDatas;

    class AllPointData{
        @SerializedName("AQI_data")
        private List<MapPointBean.PointBean>AQI_data;
        @SerializedName("SO2_data")
        private List<MapPointBean.PointBean>SO2_data;
        @SerializedName("PM10_data")
        private List<MapPointBean.PointBean>PM10_data;
        @SerializedName("O3_data")
        private List<MapPointBean.PointBean>O3_data;
        @SerializedName("PM25_data")
        private List<MapPointBean.PointBean>PM25_data;

        public List<MapPointBean.PointBean> getAQI_data() {
            return AQI_data;
        }

        public void setAQI_data(List<MapPointBean.PointBean> AQI_data) {
            this.AQI_data = AQI_data;
        }

        public List<MapPointBean.PointBean> getSO2_data() {
            return SO2_data;
        }

        public void setSO2_data(List<MapPointBean.PointBean> SO2_data) {
            this.SO2_data = SO2_data;
        }

        public List<MapPointBean.PointBean> getPM10_data() {
            return PM10_data;
        }

        public void setPM10_data(List<MapPointBean.PointBean> PM10_data) {
            this.PM10_data = PM10_data;
        }

        public List<MapPointBean.PointBean> getO3_data() {
            return O3_data;
        }

        public void setO3_data(List<MapPointBean.PointBean> o3_data) {
            O3_data = o3_data;
        }

        public List<MapPointBean.PointBean> getPM25_data() {
            return PM25_data;
        }

        public void setPM25_data(List<MapPointBean.PointBean> PM25_data) {
            this.PM25_data = PM25_data;
        }
    }
}
