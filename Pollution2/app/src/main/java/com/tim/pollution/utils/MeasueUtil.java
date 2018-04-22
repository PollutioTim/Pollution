package com.tim.pollution.utils;

import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by lenovo on 2018/2/8.
 */

public class MeasueUtil {

    public  int getWMargins (View view) {
        int width =view.getMeasuredWidth();
        return width;
    }
    public  int getHMargins (View view) {
        int height =view.getMeasuredHeight();
        return height;
    }

    public void setMargin(View view,int width){
        LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams)
                view.getLayoutParams(); //取控件textView当前的布局参数 linearParams.height = 20;// 控件的高强制设成20

        linearParams.width = width;// 控件的宽强制设成30

        view.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
    }
}
