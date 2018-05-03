package com.tim.pollution.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lenovo on 2018/5/2.
 * 地图所有站点的实体类
 */

public class MapPointBean extends StateCode implements Serializable {
    @SerializedName("message")
    private List<PointBean>messages;

    public List<PointBean> getMessages() {
        return messages;
    }

    public void setMessages(List<PointBean> messages) {
        this.messages = messages;
    }

    public class PointBean{
//        "PointCode": "140101001",
//                "PointName": "上兰",
//                "PointAddress": "太原市尖草坪区通顺街",
//                "PointLongitude": "112.434",
//                "PointLatitude": "38.0108",
//                "PointType": "国控",
//                "AQI": "99",
//                "AQIcolor": "#FFFF00",
//                "SO2": "33",
//                "NO2": "43",
//                "PM10": "109",
//                "CO": "0.8",
//                "O3": "82",
//                "PM25": "74",
//                "Feng":"4",
//                "FengXiang":"288",
//                "Qiya":"855",
//                "QiWen":"22",
//                "ShiDu":"18"
        @SerializedName("PointCode")
        private String pointCode;
        @SerializedName("PointName")
        private String pointName;
        @SerializedName("PointAddress")
        private String pointAddress;
        @SerializedName("PointLongitude")
        private String pointLongitude;
        @SerializedName("PointLatitude")
        private String pointLatitude;
        @SerializedName("PointType")
        private String pointType;
        @SerializedName("AQI")
        private String AQI;
        @SerializedName("AQIcolor")
        private String AQIColor;
        @SerializedName("SO2")
        private String So2;
        @SerializedName("NO2")
        private String No2;
        @SerializedName("PM10")
        private String PM10;
        @SerializedName("CO")
        private String Co;
        @SerializedName("O3")
        private String O3;
        @SerializedName("PM25")
        private String PM25;
        @SerializedName("Feng")
        private String Feng;
        @SerializedName("FengXiang")
        private String FengXiang;
        @SerializedName("Qiya")
        private String qiYa;
        @SerializedName("QiWen")
        private String qiWen;
        @SerializedName("ShiDu")
        private String ShiDu;

        public String getPointCode() {
            return pointCode;
        }

        public void setPointCode(String pointCode) {
            this.pointCode = pointCode;
        }

        public String getPointName() {
            return pointName;
        }

        public void setPointName(String pointName) {
            this.pointName = pointName;
        }

        public String getPointAddress() {
            return pointAddress;
        }

        public void setPointAddress(String pointAddress) {
            this.pointAddress = pointAddress;
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

        public String getPointType() {
            return pointType;
        }

        public void setPointType(String pointType) {
            this.pointType = pointType;
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

        public String getSo2() {
            return So2;
        }

        public void setSo2(String so2) {
            So2 = so2;
        }

        public String getNo2() {
            return No2;
        }

        public void setNo2(String no2) {
            No2 = no2;
        }

        public String getPM10() {
            return PM10;
        }

        public void setPM10(String PM10) {
            this.PM10 = PM10;
        }

        public String getCo() {
            return Co;
        }

        public void setCo(String co) {
            Co = co;
        }

        public String getO3() {
            return O3;
        }

        public void setO3(String o3) {
            O3 = o3;
        }

        public String getPM25() {
            return PM25;
        }

        public void setPM25(String PM25) {
            this.PM25 = PM25;
        }

        public String getFeng() {
            return Feng;
        }

        public void setFeng(String feng) {
            Feng = feng;
        }

        public String getFengXiang() {
            return FengXiang;
        }

        public void setFengXiang(String fengXiang) {
            FengXiang = fengXiang;
        }

        public String getQiYa() {
            return qiYa;
        }

        public void setQiYa(String qiYa) {
            this.qiYa = qiYa;
        }

        public String getQiWen() {
            return qiWen;
        }

        public void setQiWen(String qiWen) {
            this.qiWen = qiWen;
        }

        public String getShiDu() {
            return ShiDu;
        }

        public void setShiDu(String shiDu) {
            ShiDu = shiDu;
        }
    }
}
