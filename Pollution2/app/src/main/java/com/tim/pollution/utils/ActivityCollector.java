package com.tim.pollution.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lili
 * @E-mail:18800185524@163.com Create on 2017-08-28
 */

public class ActivityCollector {

    public static List<Activity> activities = new ArrayList<Activity>();

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }
}
