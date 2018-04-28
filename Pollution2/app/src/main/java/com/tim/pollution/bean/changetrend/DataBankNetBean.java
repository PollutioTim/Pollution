package com.tim.pollution.bean.changetrend;

import com.tim.pollution.bean.StateCode;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tcy on 2018/4/26.
 */

public class DataBankNetBean extends StateCode implements Serializable {

    private List<MessageBean> message;

    public List<MessageBean> getMessage() {
        return message;
    }

    public void setMessage(List<MessageBean> message) {
        this.message = message;
    }

    public static class MessageBean {
        /**
         * ranking : 1
         * name : 五台县
         * toppoll : 无
         * value : 16
         * valuecolor : #00E400
         */

        private String ranking;
        private String name;
        private String toppoll;
        private String value;
        private String valuecolor;

        public String getRanking() {
            return ranking;
        }

        public void setRanking(String ranking) {
            this.ranking = ranking;
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
    }
}
