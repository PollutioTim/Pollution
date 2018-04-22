package com.tim.pollution.utils;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.Button;

import com.tim.pollution.R;

/**
 * Created by lili on 2017/10/24 0024.
 * 验证码倒计时
 */

public class MyCountDownTimer extends CountDownTimer {
    private Button btn;
    private Context context;

    public MyCountDownTimer(Button btn, long millisInFuture,
                            long countDownInterval, Context context) {
        super(millisInFuture, countDownInterval);
        this.btn = btn;
        this.context = context;
    }

    //计时过程
    @Override
    public void onTick(long l) {
        if (null != btn) {
            //防止计时过程中重复点击
            btn.setClickable(false);
            btn.setText(  l / 1000 + "s");
        }
    }

    //计时完毕的方法
    @Override
    public void onFinish() {
        if (null != btn) {
            //重新给Button设置文字
            //设置可点击
            btn.setClickable(true);
            this.cancel();
        }
    }



}
