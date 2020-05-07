package com.example.myfirstpracticeapplication.util;

import android.app.Activity;

import java.util.HashMap;
import java.util.Map;

public class ActivityManageUtil {

    private Map<String, Activity> mActivities;
    public static ActivityManageUtil mInstance;

    private ActivityManageUtil(){
        mActivities = new HashMap<>();
    }

    /**
     * 单例模式
     * @return ActivityManageUtil
     */
    public static ActivityManageUtil getInstance(){
        if(mInstance == null){
            synchronized (ActivityManageUtil.class){
                if(mInstance == null){
                    mInstance = new ActivityManageUtil();
                }
            }
        }
        return mInstance;
    }

    /**
     * 添加活动
     */
    public void addActivity(Activity activity){
        mActivities.put(activity.getClass().getName(),activity);
    }

    /**
     * 关闭活动
     */
    public void finishActivity(Activity activity){
        Activity finishActivity = mActivities.get(activity.getClass().getName());
        finishActivity.finish();
        mActivities.remove(activity.getClass().getName());
    }
    /**
     * 关闭活动
     */
    public void finishActivity(Class<? extends Activity> activityClass){
        Activity finishActivity = mActivities.get(activityClass.getName());
        finishActivity.finish();
        mActivities.remove(activityClass.getName());
    }
}
