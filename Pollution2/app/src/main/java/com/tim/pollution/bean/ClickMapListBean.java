package com.tim.pollution.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lenovo on 2018/5/2.
 */

public class ClickMapListBean extends StateCode implements Serializable {

    @SerializedName("message")
    private List<ClickMapBean> message;

    public List<ClickMapBean> getMessage() {
        return message;
    }

    public void setMessage(List<ClickMapBean> message) {
        this.message = message;
    }
}
