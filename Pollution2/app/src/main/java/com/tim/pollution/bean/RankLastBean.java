package com.tim.pollution.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lenovo on 2018/4/25.
 */

public class RankLastBean extends StateCode implements Serializable {
    @SerializedName("message")
    private Message messages;

    public Message getMessages() {
        return messages;
    }

    public void setMessages(Message messages) {
        this.messages = messages;
    }
    public class Message{

        private String time;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        @SerializedName("rank_data")
        private List<Content>contents;

        public List<Content> getContents() {
            return contents;
        }

        public void setContents(List<Content> contents) {
            this.contents = contents;
        }

        public class Content {
            private String areaid;
            private String ranking;
            private String name;
            private String mainpoll;
            private String value;

            public String getAreaid() {
                return areaid;
            }

            public void setAreaid(String areaid) {
                this.areaid = areaid;
            }

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

            public String getMainpoll() {
                return mainpoll;
            }

            public void setMainpoll(String mainpoll) {
                this.mainpoll = mainpoll;
            }

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }
    }
}
