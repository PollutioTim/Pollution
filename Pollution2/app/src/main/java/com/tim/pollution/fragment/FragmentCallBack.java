package com.tim.pollution.fragment;

import com.tim.pollution.bean.weather.MessageBean;

public  interface FragmentCallBack{
    void prograss(MessageBean messageBean);
    void error(String regionId);
}
