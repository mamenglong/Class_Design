package mml.com.class_design.utils;

import android.app.Activity;
import android.widget.Toast;

import java.util.HashSet;

import mml.com.class_design.application.MyApplication;

/**
 * 项目名称：Class_Design
 * Created by Long on 2018/7/1.
 * 修改时间：2018/7/1 23:59
 */
public class ActivityManager {

    /**
     *  定义HashSet集合来装Activity，是可以防止Activity不被重复
     */
    private static HashSet<Activity> hashSet = new HashSet<Activity>();

    private static ActivityManager instance = new ActivityManager();;

    private ActivityManager() {}

    public static ActivityManager getInstance() {
        return instance;
    }

    /**
     * 每一个Activity 在 onCreate 方法的时候，可以装入当前this
     * @param activity
     */
    public void addActivity(Activity activity) {
        try {
            hashSet.add(activity);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 调用此方法用于退出整个Project
     */
    public void exit() {
        try {
            for (Activity activity : hashSet) {
                if (activity != null&&!activity.isFinishing())
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
           System.exit(0);
           android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    public void removeActivty(Activity activity ){
        hashSet.remove(activity);
    }
    // 此方法用于扩展使用
    /*
    public void onLowMemory() {
      super.onLowMemory();
      System.gc();
    }
    */

}