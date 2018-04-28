package com.tim.pollution.bean;

/**
 * Created by lenovo on 2018/4/26.
 */

public class MyData <T> {
    T data;
    String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
