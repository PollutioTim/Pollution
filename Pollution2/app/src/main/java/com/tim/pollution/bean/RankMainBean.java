package com.tim.pollution.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lenovo on 2018/4/25.
 */

public class RankMainBean extends StateCode implements Serializable {

    @SerializedName("message")
    private Message messages;

    public Message getMessages() {
        return messages;
    }

    public void setMessages(Message messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "RankMainBean{" +
                "messages=" + messages +
                '}';
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
            @SerializedName("areaid")
            private String id;
            private String ranking;
            private String name;
            private String toppoll;
            private String value;
            @SerializedName("valuecolor")
            private String valueColor;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
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

            public String getValueColor() {
                return valueColor;
            }

            public void setValueColor(String valueColor) {
                this.valueColor = valueColor;
            }

            @Override
            public String toString() {
                return "Message{" +
                        "ranking='" + ranking + '\'' +
                        ", name='" + name + '\'' +
                        ", toppoll='" + toppoll + '\'' +
                        ", value='" + value + '\'' +
                        ", valueColor='" + valueColor + '\'' +
                        '}';
            }
        }

    }
}
