package com.tim.pollution.bean.changetrend;

import com.tim.pollution.bean.StateCode;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tcy on 2018/4/26.
 */

public class DataBankNetBean extends StateCode implements Serializable {


    /**
     * message : {"time":"2018年5月12日12时","rank_data":[{"ranking":"1","areaid":"140500","name":"沁水县","toppoll":"无","value":"47","valuecolor":"#00E400"},{"ranking":"2","areaid":"140500","name":"晋城市","toppoll":"无","value":"48","valuecolor":"#00E400"},{"ranking":"3","areaid":"140500","name":"陵川县","toppoll":"PM10","value":"55","valuecolor":"#FFCC00"},{"ranking":"4","areaid":"140500","name":"阳城县","toppoll":"PM10","value":"61","valuecolor":"#FFCC00"},{"ranking":"5","areaid":"140500","name":"泽州县","toppoll":"PM2.5","value":"70","valuecolor":"#FFCC00"},{"ranking":"6","areaid":"140500","name":"高平市","toppoll":"PM10","value":"74","valuecolor":"#FFCC00"}]}
     */

    private MessageBean message;

    public MessageBean getMessage() {
        return message;
    }

    public void setMessage(MessageBean message) {
        this.message = message;
    }

    public static class MessageBean {
        /**
         * time : 2018年5月12日12时
         * rank_data : [{"ranking":"1","areaid":"140500","name":"沁水县","toppoll":"无","value":"47","valuecolor":"#00E400"},{"ranking":"2","areaid":"140500","name":"晋城市","toppoll":"无","value":"48","valuecolor":"#00E400"},{"ranking":"3","areaid":"140500","name":"陵川县","toppoll":"PM10","value":"55","valuecolor":"#FFCC00"},{"ranking":"4","areaid":"140500","name":"阳城县","toppoll":"PM10","value":"61","valuecolor":"#FFCC00"},{"ranking":"5","areaid":"140500","name":"泽州县","toppoll":"PM2.5","value":"70","valuecolor":"#FFCC00"},{"ranking":"6","areaid":"140500","name":"高平市","toppoll":"PM10","value":"74","valuecolor":"#FFCC00"}]
         */

        private String time;
        private List<RankDataBean> rank_data;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public List<RankDataBean> getRank_data() {
            return rank_data;
        }

        public void setRank_data(List<RankDataBean> rank_data) {
            this.rank_data = rank_data;
        }

        public static class RankDataBean {
            /**
             * ranking : 1
             * areaid : 140500
             * name : 沁水县
             * toppoll : 无
             * value : 47
             * valuecolor : #00E400
             */

            private String ranking;
            private String areaid;
            private String name;
            private String toppoll;
            private String value;
            private String valuecolor;
            private boolean isClass;

            private List<RankDataBean> rankDataBeens;

            public List<RankDataBean> getRankDataBeens() {
                return rankDataBeens;
            }

            public void setRankDataBeens(List<RankDataBean> rankDataBeens) {
                this.rankDataBeens = rankDataBeens;
            }

            public String getRanking() {
                return ranking;
            }

            public void setRanking(String ranking) {
                this.ranking = ranking;
            }

            public String getAreaid() {
                return areaid;
            }

            public void setAreaid(String areaid) {
                this.areaid = areaid;
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

            public String getValuecolor() {
                return valuecolor;
            }

            public void setValuecolor(String valuecolor) {
                this.valuecolor = valuecolor;
            }

            public boolean isClass() {
                return isClass;
            }

            public void setClass(boolean aClass) {
                isClass = aClass;
            }
        }
    }
}
