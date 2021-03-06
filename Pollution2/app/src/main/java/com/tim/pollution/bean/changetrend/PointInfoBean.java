package com.tim.pollution.bean.changetrend;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by tcy on 2018/4/26.
 */

public class PointInfoBean {
    /**
     * PointList : {"PointCode":"140225A","PointName":"环保局","time":"2018-04-12 18:00:00","AQI":"190","AQIcolor":"#FF0000","SO2":"12","SO2color":"#00E400","NO2":"16","NO2color":"#00E400","PM10":"330","PM10color":"#FF0000","CO":"0.8","COcolor":"#00E400","O3":"93","O3color":"#00E400","PM25":"56","PM25color":"#FFFF00","PollutionLevel":"中度污染","TopPollution":"PM10","weather":"多云","weathercode":"101","wind":"东南风 5级","temperature":"15","humidity":"32"}
     * AQI_24h : [{"time":"2018-04-11 19:00:00","value":"59","valuecolor":"#FFFF00"},{"time":"2018-04-11 20:00:00","value":"73","valuecolor":"#FFFF00"},{"time":"2018-04-11 21:00:00","value":"96","valuecolor":"#FFFF00"},{"time":"2018-04-11 22:00:00","value":"99","valuecolor":"#FFFF00"},{"time":"2018-04-11 23:00:00","value":"108","valuecolor":"#FF7E00"},{"time":"2018-04-12 00:00:00","value":"97","valuecolor":"#FFFF00"},{"time":"2018-04-12 01:00:00","value":"99","valuecolor":"#FFFF00"},{"time":"2018-04-12 02:00:00","value":"100","valuecolor":"#FFFF00"},{"time":"2018-04-12 03:00:00","value":"105","valuecolor":"#FF7E00"},{"time":"2018-04-12 04:00:00","value":"123","valuecolor":"#FF7E00"},{"time":"2018-04-12 05:00:00","value":"118","valuecolor":"#FF7E00"},{"time":"2018-04-12 06:00:00","value":"112","valuecolor":"#FF7E00"},{"time":"2018-04-12 07:00:00","value":"89","valuecolor":"#FFFF00"},{"time":"2018-04-12 08:00:00","value":"133","valuecolor":"#FF7E00"},{"time":"2018-04-12 09:00:00","value":"175","valuecolor":"#FF0000"},{"time":"2018-04-12 10:00:00","value":"229","valuecolor":"#99004C"},{"time":"2018-04-12 11:00:00","value":"204","valuecolor":"#99004C"},{"time":"2018-04-12 12:00:00","value":"147","valuecolor":"#FF7E00"},{"time":"2018-04-12 13:00:00","value":"112","valuecolor":"#FF7E00"},{"time":"2018-04-12 14:00:00","value":"95","valuecolor":"#FFFF00"},{"time":"2018-04-12 15:00:00","value":"84","valuecolor":"#FFFF00"},{"time":"2018-04-12 16:00:00","value":"100","valuecolor":"#FFFF00"},{"time":"2018-04-12 17:00:00","value":"129","valuecolor":"#FF7E00"},{"time":"2018-04-12 18:00:00","value":"190","valuecolor":"#FF0000"}]
     * SO2_24h : [{"time":"2018-04-11 19:00:00","value":"19","valuecolor":"#00E400"},{"time":"2018-04-11 20:00:00","value":"21","valuecolor":"#00E400"},{"time":"2018-04-11 21:00:00","value":"13","valuecolor":"#00E400"},{"time":"2018-04-11 22:00:00","value":"16","valuecolor":"#00E400"},{"time":"2018-04-11 23:00:00","value":"15","valuecolor":"#00E400"},{"time":"2018-04-12 00:00:00","value":"16","valuecolor":"#00E400"},{"time":"2018-04-12 01:00:00","value":"14","valuecolor":"#00E400"},{"time":"2018-04-12 02:00:00","value":"17","valuecolor":"#00E400"},{"time":"2018-04-12 03:00:00","value":"17","valuecolor":"#00E400"},{"time":"2018-04-12 04:00:00","value":"17","valuecolor":"#00E400"},{"time":"2018-04-12 05:00:00","value":"15","valuecolor":"#00E400"},{"time":"2018-04-12 06:00:00","value":"14","valuecolor":"#00E400"},{"time":"2018-04-12 07:00:00","value":"18","valuecolor":"#00E400"},{"time":"2018-04-12 08:00:00","value":"24","valuecolor":"#00E400"},{"time":"2018-04-12 09:00:00","value":"18","valuecolor":"#00E400"},{"time":"2018-04-12 10:00:00","value":"24","valuecolor":"#00E400"},{"time":"2018-04-12 11:00:00","value":"17","valuecolor":"#00E400"},{"time":"2018-04-12 12:00:00","value":"10","valuecolor":"#00E400"},{"time":"2018-04-12 13:00:00","value":"11","valuecolor":"#00E400"},{"time":"2018-04-12 14:00:00","value":"10","valuecolor":"#00E400"},{"time":"2018-04-12 15:00:00","value":"9","valuecolor":"#00E400"},{"time":"2018-04-12 16:00:00","value":"9","valuecolor":"#00E400"},{"time":"2018-04-12 17:00:00","value":"11","valuecolor":"#00E400"},{"time":"2018-04-12 18:00:00","value":"12","valuecolor":"#00E400"}]
     * NO2_24h : [{"time":"2018-04-11 19:00:00","value":"60","valuecolor":"#00E400"},{"time":"2018-04-11 20:00:00","value":"67","valuecolor":"#00E400"},{"time":"2018-04-11 21:00:00","value":"57","valuecolor":"#00E400"},{"time":"2018-04-11 22:00:00","value":"43","valuecolor":"#00E400"},{"time":"2018-04-11 23:00:00","value":"46","valuecolor":"#00E400"},{"time":"2018-04-12 00:00:00","value":"44","valuecolor":"#00E400"},{"time":"2018-04-12 01:00:00","value":"48","valuecolor":"#00E400"},{"time":"2018-04-12 02:00:00","value":"44","valuecolor":"#00E400"},{"time":"2018-04-12 03:00:00","value":"45","valuecolor":"#00E400"},{"time":"2018-04-12 04:00:00","value":"51","valuecolor":"#00E400"},{"time":"2018-04-12 05:00:00","value":"41","valuecolor":"#00E400"},{"time":"2018-04-12 06:00:00","value":"24","valuecolor":"#00E400"},{"time":"2018-04-12 07:00:00","value":"49","valuecolor":"#00E400"},{"time":"2018-04-12 08:00:00","value":"46","valuecolor":"#00E400"},{"time":"2018-04-12 09:00:00","value":"50","valuecolor":"#00E400"},{"time":"2018-04-12 10:00:00","value":"89","valuecolor":"#00E400"},{"time":"2018-04-12 11:00:00","value":"66","valuecolor":"#00E400"},{"time":"2018-04-12 12:00:00","value":"49","valuecolor":"#00E400"},{"time":"2018-04-12 13:00:00","value":"42","valuecolor":"#00E400"},{"time":"2018-04-12 14:00:00","value":"41","valuecolor":"#00E400"},{"time":"2018-04-12 15:00:00","value":"19","valuecolor":"#00E400"},{"time":"2018-04-12 16:00:00","value":"9","valuecolor":"#00E400"},{"time":"2018-04-12 17:00:00","value":"14","valuecolor":"#00E400"},{"time":"2018-04-12 18:00:00","value":"16","valuecolor":"#00E400"}]
     * PM10_24h : [{"time":"2018-04-11 19:00:00","value":"68","valuecolor":"#FFFF00"},{"time":"2018-04-11 20:00:00","value":"96","valuecolor":"#FFFF00"},{"time":"2018-04-11 21:00:00","value":"141","valuecolor":"#FFFF00"},{"time":"2018-04-11 22:00:00","value":"148","valuecolor":"#FFFF00"},{"time":"2018-04-11 23:00:00","value":"166","valuecolor":"#FF7E00"},{"time":"2018-04-12 00:00:00","value":"144","valuecolor":"#FFFF00"},{"time":"2018-04-12 01:00:00","value":"147","valuecolor":"#FFFF00"},{"time":"2018-04-12 02:00:00","value":"150","valuecolor":"#FFFF00"},{"time":"2018-04-12 03:00:00","value":"160","valuecolor":"#FF7E00"},{"time":"2018-04-12 04:00:00","value":"195","valuecolor":"#FF7E00"},{"time":"2018-04-12 05:00:00","value":"186","valuecolor":"#FF7E00"},{"time":"2018-04-12 06:00:00","value":"173","valuecolor":"#FF7E00"},{"time":"2018-04-12 07:00:00","value":"128","valuecolor":"#FFFF00"},{"time":"2018-04-12 08:00:00","value":"216","valuecolor":"#FF7E00"},{"time":"2018-04-12 09:00:00","value":"299","valuecolor":"#FF0000"},{"time":"2018-04-12 10:00:00","value":"370","valuecolor":"#99004C"},{"time":"2018-04-12 11:00:00","value":"306","valuecolor":"#FF0000"},{"time":"2018-04-12 12:00:00","value":"235","valuecolor":"#FF7E00"},{"time":"2018-04-12 13:00:00","value":"168","valuecolor":"#FF7E00"},{"time":"2018-04-12 14:00:00","value":"140","valuecolor":"#FFFF00"},{"time":"2018-04-12 15:00:00","value":"118","valuecolor":"#FFFF00"},{"time":"2018-04-12 16:00:00","value":"150","valuecolor":"#FFFF00"},{"time":"2018-04-12 17:00:00","value":"207","valuecolor":"#FF7E00"},{"time":"2018-04-12 18:00:00","value":"330","valuecolor":"#FF0000"}]
     * CO_24h : [{"time":"2018-04-11 19:00:00","value":"1","valuecolor":"#00E400"},{"time":"2018-04-11 20:00:00","value":"2","valuecolor":"#00E400"},{"time":"2018-04-11 21:00:00","value":"1","valuecolor":"#00E400"},{"time":"2018-04-11 22:00:00","value":"1","valuecolor":"#00E400"},{"time":"2018-04-11 23:00:00","value":"2","valuecolor":"#00E400"},{"time":"2018-04-12 00:00:00","value":"2","valuecolor":"#00E400"},{"time":"2018-04-12 01:00:00","value":"2","valuecolor":"#00E400"},{"time":"2018-04-12 02:00:00","value":"2","valuecolor":"#00E400"},{"time":"2018-04-12 03:00:00","value":"1","valuecolor":"#00E400"},{"time":"2018-04-12 04:00:00","value":"2","valuecolor":"#00E400"},{"time":"2018-04-12 05:00:00","value":"2","valuecolor":"#00E400"},{"time":"2018-04-12 06:00:00","value":"1","valuecolor":"#00E400"},{"time":"2018-04-12 07:00:00","value":"2","valuecolor":"#00E400"},{"time":"2018-04-12 08:00:00","value":"2","valuecolor":"#00E400"},{"time":"2018-04-12 09:00:00","value":"2","valuecolor":"#00E400"},{"time":"2018-04-12 10:00:00","value":"2","valuecolor":"#00E400"},{"time":"2018-04-12 11:00:00","value":"2","valuecolor":"#00E400"},{"time":"2018-04-12 12:00:00","value":"1","valuecolor":"#00E400"},{"time":"2018-04-12 13:00:00","value":"1","valuecolor":"#00E400"},{"time":"2018-04-12 14:00:00","value":"1","valuecolor":"#00E400"},{"time":"2018-04-12 15:00:00","value":"1","valuecolor":"#00E400"},{"time":"2018-04-12 16:00:00","value":"1","valuecolor":"#00E400"},{"time":"2018-04-12 17:00:00","value":"1","valuecolor":"#00E400"},{"time":"2018-04-12 18:00:00","value":"1","valuecolor":"#00E400"}]
     * O3_24h : [{"time":"2018-04-11 19:00:00","value":"36","valuecolor":"#00E400"},{"time":"2018-04-11 20:00:00","value":"36","valuecolor":"#00E400"},{"time":"2018-04-11 21:00:00","value":"29","valuecolor":"#00E400"},{"time":"2018-04-11 22:00:00","value":"23","valuecolor":"#00E400"},{"time":"2018-04-11 23:00:00","value":"23","valuecolor":"#00E400"},{"time":"2018-04-12 00:00:00","value":"21","valuecolor":"#00E400"},{"time":"2018-04-12 01:00:00","value":"22","valuecolor":"#00E400"},{"time":"2018-04-12 02:00:00","value":"20","valuecolor":"#00E400"},{"time":"2018-04-12 03:00:00","value":"20","valuecolor":"#00E400"},{"time":"2018-04-12 04:00:00","value":"20","valuecolor":"#00E400"},{"time":"2018-04-12 05:00:00","value":"21","valuecolor":"#00E400"},{"time":"2018-04-12 06:00:00","value":"22","valuecolor":"#00E400"},{"time":"2018-04-12 07:00:00","value":"22","valuecolor":"#00E400"},{"time":"2018-04-12 08:00:00","value":"22","valuecolor":"#00E400"},{"time":"2018-04-12 09:00:00","value":"22","valuecolor":"#00E400"},{"time":"2018-04-12 10:00:00","value":"21","valuecolor":"#00E400"},{"time":"2018-04-12 11:00:00","value":"28","valuecolor":"#00E400"},{"time":"2018-04-12 12:00:00","value":"44","valuecolor":"#00E400"},{"time":"2018-04-12 13:00:00","value":"55","valuecolor":"#00E400"},{"time":"2018-04-12 14:00:00","value":"58","valuecolor":"#00E400"},{"time":"2018-04-12 15:00:00","value":"74","valuecolor":"#00E400"},{"time":"2018-04-12 16:00:00","value":"85","valuecolor":"#00E400"},{"time":"2018-04-12 17:00:00","value":"90","valuecolor":"#00E400"},{"time":"2018-04-12 18:00:00","value":"93","valuecolor":"#00E400"}]
     * PM25_24h : [{"time":"2018-04-11 19:00:00","value":"19","valuecolor":"#00E400"},{"time":"2018-04-11 20:00:00","value":"26","valuecolor":"#00E400"},{"time":"2018-04-11 21:00:00","value":"66","valuecolor":"#FFFF00"},{"time":"2018-04-11 22:00:00","value":"72","valuecolor":"#FFFF00"},{"time":"2018-04-11 23:00:00","value":"67","valuecolor":"#FFFF00"},{"time":"2018-04-12 00:00:00","value":"50","valuecolor":"#FFFF00"},{"time":"2018-04-12 01:00:00","value":"48","valuecolor":"#FFFF00"},{"time":"2018-04-12 02:00:00","value":"52","valuecolor":"#FFFF00"},{"time":"2018-04-12 03:00:00","value":"35","valuecolor":"#00E400"},{"time":"2018-04-12 04:00:00","value":"36","valuecolor":"#FFFF00"},{"time":"2018-04-12 05:00:00","value":"31","valuecolor":"#00E400"},{"time":"2018-04-12 06:00:00","value":"31","valuecolor":"#00E400"},{"time":"2018-04-12 07:00:00","value":"37","valuecolor":"#FFFF00"},{"time":"2018-04-12 08:00:00","value":"63","valuecolor":"#FFFF00"},{"time":"2018-04-12 09:00:00","value":"123","valuecolor":"#FF0000"},{"time":"2018-04-12 10:00:00","value":"148","valuecolor":"#FF0000"},{"time":"2018-04-12 11:00:00","value":"154","valuecolor":"#99004C"},{"time":"2018-04-12 12:00:00","value":"112","valuecolor":"#FF7E00"},{"time":"2018-04-12 13:00:00","value":"84","valuecolor":"#FF7E00"},{"time":"2018-04-12 14:00:00","value":"54","valuecolor":"#FFFF00"},{"time":"2018-04-12 15:00:00","value":"33","valuecolor":"#00E400"},{"time":"2018-04-12 16:00:00","value":"36","valuecolor":"#FFFF00"},{"time":"2018-04-12 17:00:00","value":"40","valuecolor":"#FFFF00"},{"time":"2018-04-12 18:00:00","value":"56","valuecolor":"#FFFF00"}]
     */
    @SerializedName("PointList")
    private PointListBean pointListBean;
    private List<DataInfoBean> AQI_24h;
    private List<DataInfoBean> SO2_24h;
    private List<DataInfoBean> NO2_24h;
    private List<DataInfoBean> PM10_24h;
    private List<DataInfoBean> CO_24h;
    private List<DataInfoBean> O3_24h;
    private List<DataInfoBean> PM25_24h;


    public List<DataInfoBean> getAQI_24h() {
        return AQI_24h;
    }

    public void setAQI_24h(List<DataInfoBean> AQI_24h) {
        this.AQI_24h = AQI_24h;
    }

    public List<DataInfoBean> getSO2_24h() {
        return SO2_24h;
    }

    public void setSO2_24h(List<DataInfoBean> SO2_24h) {
        this.SO2_24h = SO2_24h;
    }

    public List<DataInfoBean> getNO2_24h() {
        return NO2_24h;
    }

    public void setNO2_24h(List<DataInfoBean> NO2_24h) {
        this.NO2_24h = NO2_24h;
    }

    public List<DataInfoBean> getPM10_24h() {
        return PM10_24h;
    }

    public void setPM10_24h(List<DataInfoBean> PM10_24h) {
        this.PM10_24h = PM10_24h;
    }

    public List<DataInfoBean> getCO_24h() {
        return CO_24h;
    }

    public void setCO_24h(List<DataInfoBean> CO_24h) {
        this.CO_24h = CO_24h;
    }

    public List<DataInfoBean> getO3_24h() {
        return O3_24h;
    }

    public void setO3_24h(List<DataInfoBean> o3_24h) {
        O3_24h = o3_24h;
    }

    public List<DataInfoBean> getPM25_24h() {
        return PM25_24h;
    }

    public void setPM25_24h(List<DataInfoBean> PM25_24h) {
        this.PM25_24h = PM25_24h;
    }

    public PointListBean getPointListBean() {
        return pointListBean;
    }

    public void setPointListBean(PointListBean pointListBean) {
        this.pointListBean = pointListBean;
    }
}
