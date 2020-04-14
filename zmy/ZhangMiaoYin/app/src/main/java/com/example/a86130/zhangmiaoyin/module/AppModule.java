package com.example.a86130.zhangmiaoyin.module;

import android.content.Context;
import android.content.SharedPreferences;


import com.example.a86130.zhangmiaoyin.App;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private App mApp;

    public AppModule(App app) {
        this.mApp = app;
    }

    @Provides
    @Singleton
    public SharedPreferences provideSp() {
        return mApp.getSharedPreferences("config", Context.MODE_PRIVATE);
    }


}
