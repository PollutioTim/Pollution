package com.tim.pollution.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import static com.baidu.location.d.j.S;

/**
 * Created by lenovo on 2018/4/23.
 */

public class StateCode implements Serializable {

    @SerializedName("status")
    private int code;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
