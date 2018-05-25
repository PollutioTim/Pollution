package com.tim.pollution.fragment;

import com.tim.pollution.bean.weather.MessageBean;

public  interface FragmentCallBack{
    void prograss(MessageBean messageBean,String regionId);
    void error(String regionId);
}
