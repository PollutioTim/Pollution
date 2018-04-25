package com.tim.pollution.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


/**
 * Created by lenovo on 2018/4/24.
 */

public class MapBean extends StateCode implements Serializable  {

    @SerializedName("message")
    private Message message;

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public class Message {
        private String time;
        @SerializedName("parent_data")
        private List<CityBean>cityBeens;
        @SerializedName("legend")
        private List<LevePollutionBean>levePollutionBeens;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public List<CityBean> getCityBeens() {
            return cityBeens;
        }

        public void setCityBeens(List<CityBean> cityBeens) {
            this.cityBeens = cityBeens;
        }

        public List<LevePollutionBean> getLevePollutionBeens() {
            return levePollutionBeens;
        }

        public void setLevePollutionBeens(List<LevePollutionBean> levePollutionBeens) {
            this.levePollutionBeens = levePollutionBeens;
        }
    }

}
