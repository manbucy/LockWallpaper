package net.manbucy.lockwallpaper.base;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * ActivityCollector
 * Created by yang on 2017/image2/26.
 */

public class ActivityCollector {
    private static List<Activity> activityList = new ArrayList<>();

    public static void addActivity(Activity activity) {
        activityList.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activityList.remove(activity);
    }

    public static Activity getTopActivity() {
        if (activityList.isEmpty()){
            return null;
        }else{
            return activityList.get(activityList.size()-1);
        }
    }
}
