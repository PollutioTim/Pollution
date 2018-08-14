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
    private List<MapPointBean.PointBean> messages;

    public MapPointBean() {
    }

    public List<MapPointBean.PointBean> getMessages() {
        return this.messages;
    }

    public void setMessages(List<MapPointBean.PointBean> messages) {
        this.messages = messages;
    }

    public class PointBean {
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
        @SerializedName("Q_AQIcolor")
        private String QAqiColor;
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
        @SerializedName("SO2color")
        private String SO2Color;
        @SerializedName("Q_SO2color")
        private String QSO2Color;
        @SerializedName("NO2color")
        private String NO2Color;
        @SerializedName("Q_NO2color")
        private String QNO2Color;
        @SerializedName("PM10color")
        private String PM10Color;
        @SerializedName("Q_PM10color")
        private String QPM10Color;
        @SerializedName("COcolor")
        private String COColor;
        @SerializedName("Q_COcolor")
        private String QCOColor;
        @SerializedName("O3color")
        private String O3Color;
        @SerializedName("Q_O3color")
        private String QO3Color;
        @SerializedName("PM25color")
        private String PM25Color;
        @SerializedName("Q_PM25color")
        private String QPM25Color;
        @SerializedName("Feng")
        private String Feng;
        @SerializedName("Fengcolor")
        private String Fengcolor;
        @SerializedName("Q_Fengcolor")
        private String QFengColor;
        @SerializedName("FengXiang")
        private String FengXiang;
        @SerializedName("QiWen")
        private String QiWen;
        @SerializedName("QiWencolor")
        private String QiWencolor;
        @SerializedName("Q_QiWencolor")
        private String QQiWenColor;
        @SerializedName("QiYa")
        private String QiYa;
        @SerializedName("QiYacolor")
        private String QiYacolor;
        @SerializedName("Q_QiYacolor")
        private String QQiYaColor;
        @SerializedName("ShiDu")
        private String ShiDu;
        @SerializedName("ShiDucolor")
        private String ShiDucolor;
        @SerializedName("Q_ShiDucolor")
        private String QShiDuColor;

        public PointBean() {
        }

        public String getQSO2Color() {
            return this.QSO2Color;
        }

        public void setQSO2Color(String QSO2Color) {
            this.QSO2Color = QSO2Color;
        }

        public String getQNO2Color() {
            return this.QNO2Color;
        }

        public void setQNO2Color(String QNO2Color) {
            this.QNO2Color = QNO2Color;
        }

        public String getQPM10Color() {
            return this.QPM10Color;
        }

        public void setQPM10Color(String QPM10Color) {
            this.QPM10Color = QPM10Color;
        }

        public String getQCOColor() {
            return this.QCOColor;
        }

        public void setQCOColor(String QCOColor) {
            this.QCOColor = QCOColor;
        }

        public String getQO3Color() {
            return this.QO3Color;
        }

        public void setQO3Color(String QO3Color) {
            this.QO3Color = QO3Color;
        }

        public String getQPM25Color() {
            return this.QPM25Color;
        }

        public void setQPM25Color(String QPM25Color) {
            this.QPM25Color = QPM25Color;
        }

        public String getQFengColor() {
            return this.QFengColor;
        }

        public void setQFengColor(String QFengColor) {
            this.QFengColor = QFengColor;
        }

        public String getQQiWenColor() {
            return this.QQiWenColor;
        }

        public void setQQiWenColor(String QQiWenColor) {
            this.QQiWenColor = QQiWenColor;
        }

        public String getQQiYaColor() {
            return this.QQiYaColor;
        }

        public void setQQiYaColor(String QQiYaColor) {
            this.QQiYaColor = QQiYaColor;
        }

        public String getQShiDuColor() {
            return this.QShiDuColor;
        }

        public void setQShiDuColor(String QShiDuColor) {
            this.QShiDuColor = QShiDuColor;
        }

        public String getFengcolor() {
            return this.Fengcolor;
        }

        public String getSO2Color() {
            return this.SO2Color;
        }

        public void setSO2Color(String SO2Color) {
            this.SO2Color = SO2Color;
        }

        public String getNO2Color() {
            return this.NO2Color;
        }

        public void setNO2Color(String NO2Color) {
            this.NO2Color = NO2Color;
        }

        public String getPM10Color() {
            return this.PM10Color;
        }

        public void setPM10Color(String PM10Color) {
            this.PM10Color = PM10Color;
        }

        public String getCOColor() {
            return this.COColor;
        }

        public void setCOColor(String COColor) {
            this.COColor = COColor;
        }

        public String getO3Color() {
            return this.O3Color;
        }

        public void setO3Color(String o3Color) {
            this.O3Color = o3Color;
        }

        public String getPM25Color() {
            return this.PM25Color;
        }

        public void setPM25Color(String PM25Color) {
            this.PM25Color = PM25Color;
        }

        public void setFengcolor(String fengcolor) {
            this.Fengcolor = fengcolor;
        }

        public String getQiWen() {
            return this.QiWen;
        }

        public void setQiWen(String qiWen) {
            this.QiWen = qiWen;
        }

        public String getQiWencolor() {
            return this.QiWencolor;
        }

        public void setQiWencolor(String qiWencolor) {
            this.QiWencolor = qiWencolor;
        }

        public String getQiYa() {
            return this.QiYa;
        }

        public void setQiYa(String qiYa) {
            this.QiYa = qiYa;
        }

        public String getQiYacolor() {
            return this.QiYacolor;
        }

        public void setQiYacolor(String qiYacolor) {
            this.QiYacolor = qiYacolor;
        }

        public String getShiDucolor() {
            return this.ShiDucolor;
        }

        public void setShiDucolor(String shiDucolor) {
            this.ShiDucolor = shiDucolor;
        }

        public String getPointCode() {
            return this.pointCode;
        }

        public void setPointCode(String pointCode) {
            this.pointCode = pointCode;
        }

        public String getPointName() {
            return this.pointName;
        }

        public void setPointName(String pointName) {
            this.pointName = pointName;
        }

        public String getPointAddress() {
            return this.pointAddress;
        }

        public void setPointAddress(String pointAddress) {
            this.pointAddress = pointAddress;
        }

        public String getPointLongitude() {
            return this.pointLongitude;
        }

        public void setPointLongitude(String pointLongitude) {
            this.pointLongitude = pointLongitude;
        }

        public String getPointLatitude() {
            return this.pointLatitude;
        }

        public void setPointLatitude(String pointLatitude) {
            this.pointLatitude = pointLatitude;
        }

        public String getPointType() {
            return this.pointType;
        }

        public void setPointType(String pointType) {
            this.pointType = pointType;
        }

        public String getAQI() {
            return this.AQI;
        }

        public void setAQI(String AQI) {
            this.AQI = AQI;
        }

        public String getAQIColor() {
            return this.AQIColor;
        }

        public void setAQIColor(String AQIColor) {
            this.AQIColor = AQIColor;
        }

        public String getSo2() {
            return this.So2;
        }

        public void setSo2(String so2) {
            this.So2 = so2;
        }

        public String getNo2() {
            return this.No2;
        }

        public void setNo2(String no2) {
            this.No2 = no2;
        }

        public String getPM10() {
            return this.PM10;
        }

        public void setPM10(String PM10) {
            this.PM10 = PM10;
        }

        public String getCo() {
            return this.Co;
        }

        public void setCo(String co) {
            this.Co = co;
        }

        public String getO3() {
            return this.O3;
        }

        public void setO3(String o3) {
            this.O3 = o3;
        }

        public String getPM25() {
            return this.PM25;
        }

        public void setPM25(String PM25) {
            this.PM25 = PM25;
        }

        public String getFeng() {
            return this.Feng;
        }

        public void setFeng(String feng) {
            this.Feng = feng;
        }

        public String getFengXiang() {
            return this.FengXiang;
        }

        public void setFengXiang(String fengXiang) {
            this.FengXiang = fengXiang;
        }

        public String getShiDu() {
            return this.ShiDu;
        }

        public void setShiDu(String shiDu) {
            this.ShiDu = shiDu;
        }

        public String getQAqiColor() {
            return this.QAqiColor;
        }

        public void setQAqiColor(String QAqiColor) {
            this.QAqiColor = QAqiColor;
        }
    }
}
