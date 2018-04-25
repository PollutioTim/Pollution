package com.tim.pollution.general.okhttp.builder;


import com.tim.pollution.general.okhttp.OkHttpUtils;
import com.tim.pollution.general.okhttp.request.OtherRequest;
import com.tim.pollution.general.okhttp.request.RequestCall;

/**
 * Created by zhy on 16/3/2.
 */
public class HeadBuilder extends GetBuilder
{
    @Override
    public RequestCall build()
    {
        return new OtherRequest(null, null, OkHttpUtils.METHOD.HEAD, url, tag, params, headers,id).build();
    }
}
