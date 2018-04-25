package com.tim.pollution.general;

import java.io.Serializable;

/**
 * Created by lenovo on 2018/4/24.
 */

public class MData<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private String type;
    private T data;

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
