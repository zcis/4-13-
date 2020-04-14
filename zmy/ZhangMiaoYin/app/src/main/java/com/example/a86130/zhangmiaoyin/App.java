package com.example.a86130.zhangmiaoyin;

import android.app.Activity;
import android.app.Application;
import android.os.Process;

import com.example.a86130.zhangmiaoyin.component.DaggerAppComponent;
import com.example.a86130.zhangmiaoyin.module.AppModule;

import java.util.HashSet;
import java.util.Set;

public class App extends Application {
    private static App mInstance;
    private Set<Activity> activity;
    private static DaggerAppComponent  daggerAppCompat;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance=this;
        initInject();
    }

    private void initInject() {
        if(daggerAppCompat==null){
            daggerAppCompat = (DaggerAppComponent) DaggerAppComponent.builder().
                    appModule(new AppModule(this)).build();
        }
    }
    public static DaggerAppComponent daggerAppComponent() {
        return daggerAppCompat;
    }
    public static synchronized  App getInstance(){
        return mInstance;
    }
    public void addActivity (Activity act){
        if(activity==null){
            activity=new HashSet<Activity>();
            activity.add(act);
        }

    }
    public void removeActivity() {
        if (activity != null) {
            for (Activity act : activity) {
                act.finish();
            }
        }
        android.os.Process.killProcess(Process.myPid());
        System.exit(0);
    }


}
