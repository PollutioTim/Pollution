package com.tim.pollution.utils;

import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;

/**
 * Created by tcy on 2018/4/28.
 */

public class ViewUtils {

    /**
     * 带圆角的背景
     *
     * @param color
     * @return
     */
    public static ShapeDrawable getShapeDrawable(String color) {
        /**
         * 外部矩形弧度
        */
        float[] outerRadian = new float[]{20, 20, 20, 20, 20, 20, 20, 20};
        /**
         * 内部矩形与外部矩形的距离
        */
        RectF insetDistance = new RectF(100, 100, 50, 50);
        /**
        *  内部矩形弧度
         * */
        float[] insideRadian = new float[]{20, 20, 20, 20, 20, 20, 20, 20};
        /**
        * 如果insetDistance与insideRadian设为null亦可
        */
        RoundRectShape roundRectShape = new RoundRectShape(outerRadian, insetDistance, insideRadian);
        ShapeDrawable drawable = new ShapeDrawable(roundRectShape);
        /**
        * 指定填充颜色
         */
        drawable.getPaint().setColor(Color.parseColor(color));
        return drawable;
    }
}
