package com.tim.pollution.callback;

/**
 * Created by lenovo on 2018/4/23.
 */

public interface ICallBack<T> {

    public void onProgress(T data);

    public void onError(String msg, String eCode);
}
