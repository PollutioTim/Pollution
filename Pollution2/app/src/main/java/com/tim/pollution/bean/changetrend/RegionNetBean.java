package com.tim.pollution.bean.changetrend;

import com.google.gson.annotations.SerializedName;
import com.tim.pollution.bean.StateCode;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tcy on 2018/4/27.
 */

public class RegionNetBean extends StateCode implements Serializable {


    @SerializedName("message")
    private List<RegionBean> message;

    public List<RegionBean> getMessage() {
        return message;
    }

    public void setMessage(List<RegionBean> message) {
        this.message = message;
    }

    public static class RegionBean implements Serializable {
        /**
         * RegionId : 140101
         * RegionName : 太原市
         */

        private String RegionId;
        private String RegionName;
        private boolean isClick;

        public boolean isClick() {
            return isClick;
        }

        public void setClick(boolean click) {
            isClick = click;
        }
        public boolean getClick() {
            return isClick;
        }
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
    }
}
