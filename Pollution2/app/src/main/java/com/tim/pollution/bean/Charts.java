package com.tim.pollution.bean;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by tcy on 2018/4/27.
 */

public class Charts {

    //坐标绘图
    private LineChartView lineChart;
    private LineChartData data = new LineChartData();
    private List<Line> lines = new ArrayList<Line>();

    public Charts(LineChartView lineChart) {
        //设置x轴y轴长度，间隔
        int xLength = 600;
        int yLength = 300;
        int subX = 20;
        int subY = 20;

        this.lineChart = lineChart;
        data.setLines(lines);

        //设置X坐标轴
        Axis axisX = new Axis();
        axisX.setTextSize(10);//字体大小
        axisX.setTextColor(Color.rgb(55, 93, 93));  //字体颜色
        List<AxisValue> Xtab = new ArrayList<AxisValue>();
        for (int i = 0; i < xLength; i += subX) {
            Xtab.add(new AxisValue(i).setLabel("" + i));
        }
        axisX.setValues(Xtab);  //填充X轴的坐标名称
        axisX.setHasLines(true); //x 轴分割线
        axisX.setName("力值 N");

        //设置Y坐标轴
        Axis axisY = new Axis();
        axisY.setTextColor(Color.rgb(55, 93, 93));  //字体颜色
        List<AxisValue> Ytab = new ArrayList<AxisValue>();
        for (int i = 0; i < yLength; i += subY) {
            Ytab.add(new AxisValue(i).setLabel("" + i));
        }
        axisY.setValues(Ytab);  //填充X轴的坐标名称
        axisY.setTextSize(10);//设置字体大小
        axisY.setHasLines(true); //Y 轴分割
        axisY.setName("高度 mm");
        data.setAxisXBottom(axisX); //x 轴在底部
        data.setAxisYLeft(axisY);  //Y轴设置在左边

        //将坐标轴固定
        lineChart.setViewportCalculationEnabled(false);
        Viewport v = new Viewport(lineChart.getMaximumViewport());
        v.bottom = 0;
        v.top = yLength;
        v.left = 0;
        v.right = xLength;
        lineChart.setMaximumViewport(v);
        lineChart.setCurrentViewport(v);
        lineChart.setZoomEnabled(false);
    }

    public void flush() {
        lineChart.setLineChartData(data);
    }

    public  class LINE {
        List<PointValue> Xval = new ArrayList<PointValue>();
        Line line = new Line(Xval);

        public LINE(String color) {
            line.setColor(Color.parseColor(color));
            line.setStrokeWidth(2);
            line.setCubic(true);//是否平滑
            line.setHasLabelsOnlyForSelected(true);
            line.setHasLines(true);//是否用线显示
            line.setHasPoints(false);//是否显示圆点
            lines.add(line);
        }

        public void add(float x, float y) {
            Xval.add(new PointValue(x, y));
        }

        public void clean() {
            Xval.clear();
        }
    }
}
